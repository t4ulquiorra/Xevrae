package com.xevrae

import com.eygraber.uri.Uri
import com.xevrae.domain.data.model.intent.GenericIntent
import com.xevrae.logger.Logger
import java.io.File

/**
 * Singleton to handle deep link URIs on Desktop.
 * Caches URI if app UI is not ready yet, delivers immediately if listener is set.
 *
 * Also provides file-based IPC for single-instance deep link forwarding:
 * when a second instance launches with a URI, it writes the URI to a temp file,
 * and the first instance reads it on restore.
 *
 * Supported URI patterns:
 * - xevrae://open-app?url=<encoded_url>  (redirected from website)
 * - xevrae://watch?v=VIDEO_ID            (direct scheme)
 * - xevrae://playlist?list=PLAYLIST_ID   (direct scheme)
 * - xevrae://channel/CHANNEL_ID          (direct scheme)
 * - xevrae://album?id=ALBUM_ID           (direct scheme)
 * - https://xevrae.org/app/...            (web URL passed via args)
 */
object DesktopDeepLinkHandler {
    private const val TAG = "DesktopDeepLinkHandler"

    private val pendingUriFile: File by lazy {
        File(System.getProperty("java.io.tmpdir"), "xevrae_pending_deeplink.txt")
    }

    private var cached: String? = null

    var listener: ((GenericIntent) -> Unit)? = null
        set(value) {
            field = value
            if (value != null) {
                cached?.let { uri ->
                    Logger.d(TAG, "Delivering cached URI: $uri")
                    value.invoke(parseToIntent(uri))
                    cached = null
                }
            }
        }

    fun onNewUri(uri: String) {
        Logger.d(TAG, "Received URI: $uri")
        val intent = parseToIntent(uri)
        val currentListener = listener
        if (currentListener != null) {
            currentListener.invoke(intent)
            cached = null
        } else {
            Logger.d(TAG, "Listener not ready, caching URI: $uri")
            cached = uri
        }
    }

    /**
     * Write URI to a temp file so the running (first) instance can pick it up.
     * Called by the second instance before it exits.
     */
    fun writePendingUri(uri: String) {
        try {
            pendingUriFile.writeText(uri)
            Logger.d(TAG, "Wrote pending URI to file: $uri")
        } catch (e: Exception) {
            Logger.e(TAG, "Failed to write pending URI: ${e.message}")
        }
    }

    /**
     * Read and consume the pending URI file written by a second instance.
     * Called by the first instance when it receives a restore request.
     */
    fun consumePendingUri() {
        try {
            if (pendingUriFile.exists()) {
                val uri = pendingUriFile.readText().trim()
                pendingUriFile.delete()
                if (uri.isNotEmpty()) {
                    Logger.d(TAG, "Consumed pending URI from file: $uri")
                    onNewUri(uri)
                }
            }
        } catch (e: Exception) {
            Logger.e(TAG, "Failed to read pending URI: ${e.message}")
        }
    }

    /**
     * Converts a raw URI string into a [GenericIntent] that App.kt can process.
     *
     * Conversion rules:
     * 1. xevrae://open-app?url=<encoded_url>
     *    → Extract the `url` param and use it as intent data
     *
     * 2. xevrae://watch?v=xxx, xevrae://playlist?list=xxx, etc.
     *    → Convert to https://xevrae.org/app/watch?v=xxx format
     *      so App.kt handles it uniformly via the xevrae.org branch
     *
     * 3. https://xevrae.org/app/... or YouTube URLs
     *    → Pass through as-is
     */
    private fun parseToIntent(uri: String): GenericIntent {
        val parsed = Uri.parse(uri)

        val actualUri = when {
            // xevrae://open-app?url=<encoded_url>
            parsed.scheme == "xevrae" && parsed.host == "open-app" -> {
                val urlParam = parsed.getQueryParameter("url")
                if (urlParam != null) {
                    Logger.d(TAG, "Extracted URL from open-app: $urlParam")
                    Uri.parse(urlParam)
                } else {
                    // xevrae://open-app without params → just open the app, no navigation
                    Logger.d(TAG, "open-app without URL param, just opening app")
                    null
                }
            }

            // xevrae://watch?v=xxx → https://xevrae.org/app/watch?v=xxx
            // xevrae://playlist?list=xxx → https://xevrae.org/app/playlist?list=xxx
            // xevrae://channel/UCxxx → https://xevrae.org/app/channel/UCxxx
            // xevrae://album?id=xxx → https://xevrae.org/app/album?id=xxx
            parsed.scheme == "xevrae" && parsed.host != null -> {
                val host = parsed.host!!
                val query = parsed.query?.let { "?$it" } ?: ""
                val pathSuffix = parsed.pathSegments.joinToString("/").let {
                    if (it.isNotEmpty()) "/$it" else ""
                }
                val convertedUrl = "https://xevrae.org/app/$host$pathSuffix$query"
                Logger.d(TAG, "Converted xevrae:// to: $convertedUrl")
                Uri.parse(convertedUrl)
            }

            // https://xevrae.org/app/... or YouTube URLs → pass through
            else -> parsed
        }

        return if (actualUri != null) {
            GenericIntent(
                action = "android.intent.action.VIEW",
                data = actualUri,
            )
        } else {
            // No data → just triggers app restore, no navigation
            GenericIntent(action = "android.intent.action.VIEW")
        }
    }
}
