package com.example.FileManager.services

import com.example.FileManager.model.FileManagerEntry
import com.example.FileManager.model.FolderInput
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

interface FolderService {
    fun createFolder(folderInput: FolderInput): FileManagerEntry.Folder
    fun getFolder(folderId: UUID): FileManagerEntry.Folder
    fun getFolderFiles(folderId: UUID, deleted: Boolean): List<FileManagerEntry?>
    fun getDeletedChildren(folderId: UUID): List<FileManagerEntry?>
    fun findByPath(rootId: UUID, fullPath: String): List<FileManagerEntry?>
    fun attach(folderId: UUID, attachId: UUID): FileManagerEntry.Folder
    fun detach(folderId: UUID, attachId: UUID): FileManagerEntry.Folder

    @Transactional
    fun update(folderId: UUID, input: FolderInput): FileManagerEntry.Folder
    fun delete(folderId: UUID): FileManagerEntry.Folder
    fun search(folderId: UUID, query: String): List<FileManagerEntry?>
    fun findByName(query: String): List<FileManagerEntry.Folder?>
}

