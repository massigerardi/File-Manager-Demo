package com.example.FileManager.services

import com.example.FileManager.entities.FileEntity
import com.example.FileManager.entities.FileManagerEntity
import com.example.FileManager.entities.FolderEntity
import com.example.FileManager.model.FileManagerEntry
import java.lang.IllegalArgumentException
import java.util.UUID

object FileManagerEntryMapper {

    fun toDto(entity: FileManagerEntity): FileManagerEntry =
        when (entity) {
            is FileEntity -> toFileDto(entity)
            is FolderEntity -> toFolderDto(entity)
        }

    fun toFolderDto(entity: FolderEntity): FileManagerEntry.Folder =
            FileManagerEntry.Folder(entity.id!!, entity.name, getSelf(entity.id!!, "folder"), getParent(entity.parent, "folder"), getFiles(entity.id!!), entity.updated)

    fun toFileDto(entity: FileEntity): FileManagerEntry.File =
        FileManagerEntry.File(entity.id!!, entity.name, getSelf(entity.id!!, "file"), getParent(entity.parent, "folder"), entity.contentType, entity.size!!, S3Format.getDownloadableUrl(entity.url!!), entity.scanStatus, entity.updated)

    private fun getFiles(id: UUID): String = "/api/folder/$id/files"

    private fun getSelf(id: UUID, path: String): String = "/api/$path/$id"

    private fun getParent(id: UUID?, path: String): String? = if(id != null) "/api/$path/$id" else null
}