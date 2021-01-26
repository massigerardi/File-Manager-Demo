package com.example.FileManager.services

import java.nio.file.Path

object S3Format {

    fun getDownloadableUrl(url: String): String {
        val path = Path.of(url.substringAfterLast("s3://"))
        return "https://${path.parent}.aws.com/${path.fileName}"
    }
}