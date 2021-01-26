package com.example.FileManager.model

import java.util.UUID

data class FileInput(
    val folder: UUID?,
    val name: String?,
    val contentType: String?
) {
    fun hasChanges(): Boolean =
        name?.isNotBlank() == true || contentType?.isNotBlank() == true || folder.isNotBlank()
}

data class FolderInput(
    val folder: UUID?,
    val name: String?,
) {
    fun hasChanges(): Boolean =
        name?.isNotBlank() == true || folder.isNotBlank()
}

fun UUID?.isNullOrBlank() = this == null || this.toString().isEmpty()
fun UUID?.isNotBlank() = this != null && this.toString().isNotBlank()