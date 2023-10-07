package com.example.FileManager.services

import com.example.FileManager.model.FileInput
import com.example.FileManager.model.FileManagerEntry
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

interface FileService {
    @Transactional
    fun upload(file: MultipartFile, folderId: UUID) : FileManagerEntry.File
    fun getFile(fileId: UUID): FileManagerEntry.File
    fun update(fileId: UUID, action: String = "move", input: FileInput): FileManagerEntry.File
    fun delete(fileId: UUID): FileManagerEntry.File?
}

