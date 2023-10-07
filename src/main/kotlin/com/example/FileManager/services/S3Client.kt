package com.example.FileManager.services

import org.springframework.stereotype.Service
import java.nio.file.Path
import java.util.UUID

@Service
class S3Client {

    fun putObject(name: String): String {
        return "s3://filesBucket/${UUID.randomUUID()}/$name"
    }
}