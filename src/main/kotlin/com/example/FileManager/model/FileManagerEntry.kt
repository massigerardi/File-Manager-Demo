package com.example.FileManager.model

import java.time.Instant
import java.util.UUID

sealed class FileManagerEntry {
        abstract val id: UUID
        abstract val name: String
        abstract val self: String
        abstract val parent: String?

        data class Folder(
                override val id: UUID,
                override val name: String,
                override val self: String,
                override val parent: String?,
                val files: String,
                val updatedAt: Instant
        ): FileManagerEntry()

        data class File(
                override val id: UUID,
                override val name: String,
                override val self: String,
                override val parent: String?,
                val contentType: String,
                val size: Int,
                val downloadUrl: String,
                val scanStatus: String,
                val updatedAt: Instant
                ): FileManagerEntry()

}


