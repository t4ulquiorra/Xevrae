package com.xevrae.expect

actual fun getDownloadFolderPath(): String = System.getProperty("user.home") + "/Downloads"