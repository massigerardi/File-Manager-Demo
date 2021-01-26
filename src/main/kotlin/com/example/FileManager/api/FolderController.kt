package com.example.FileManager.api

import com.example.FileManager.access.HasAccessToFolder
import com.example.FileManager.model.FileManagerEntry
import com.example.FileManager.model.FolderInput
import com.example.FileManager.services.FolderService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/folder")
class FolderController(
        val folderService: FolderService
) {

    @PostMapping(
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun createFolder(@RequestBody input: FolderInput): ResponseEntity<FileManagerEntry.Folder> {
        return ResponseEntity.ok(folderService.createFolder(input))
    }

    @PutMapping(
        "{id}",
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @HasAccessToFolder(id = "folderId", role = "modifyFolder")
    fun update(
        @PathVariable("id") folderId: UUID,
        @RequestBody input: FolderInput
    ): ResponseEntity<FileManagerEntry.Folder> {
        return ResponseEntity.ok(folderService.update(folderId = folderId, input = input))
    }

    @PutMapping(
        "{id}/attachments/{attachmentId}",
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun attach(
        @PathVariable("id") folderId: UUID,
        @PathVariable("attachmentId") attachmentId: UUID
    ): ResponseEntity<FileManagerEntry.Folder> {
        return ResponseEntity.ok(folderService.attach(folderId = folderId, attachId = attachmentId))
    }

    @DeleteMapping(
        "{id}/attachments/{attachmentId}",
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun detach(
        @PathVariable("id") folderId: UUID,
        @PathVariable("attachmentId") attachmentId: UUID
    ): ResponseEntity<FileManagerEntry.Folder> {
        return ResponseEntity.ok(folderService.detach(folderId = folderId, attachId = attachmentId))
    }

    @GetMapping(
            "/{id}",
            produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @HasAccessToFolder(id = "folderId", role = "getFolder")
    fun findById(
            @PathVariable("id") folderId: UUID): ResponseEntity<FileManagerEntry.Folder?> {
        return ResponseEntity.ok(folderService.getFolder(folderId = folderId))
    }

    @GetMapping(
        "/{id}/files",
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @HasAccessToFolder(id = "folderId", role = "getFolder")
    fun getFolderChilden(
        @PathVariable("id") folderId: UUID, @RequestParam("deleted", defaultValue = "false", required = false) deleted: Boolean): ResponseEntity<List<FileManagerEntry?>> {
        return ResponseEntity.ok(folderService.getFolderFiles(folderId = folderId, deleted = deleted))
    }

    @DeleteMapping(
        "/{id}",
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    @HasAccessToFolder(id = "folderId", role = "deleteFolder")
    fun delete(
        @PathVariable("id") folderId: UUID): ResponseEntity<FileManagerEntry.Folder> {
        return ResponseEntity.ok(folderService.delete(folderId = folderId))
    }

    @GetMapping(
        "/{id}/files",
        produces = [MediaType.APPLICATION_JSON_VALUE],
        params = ["q"]
    )
    @HasAccessToFolder(id = "folderId", role = "getFolder")
    fun searchByName(@PathVariable("id") folderId: UUID, @RequestParam("q") query: String): ResponseEntity<List<FileManagerEntry?>> {
        return ResponseEntity.ok(folderService.search(folderId = folderId, query = query))
    }

    @GetMapping(
        "/{id}/files",
        produces = [MediaType.APPLICATION_JSON_VALUE],
        params = ["path"]
    )
    @HasAccessToFolder(id = "folderId", role = "getFolder")
    fun searchByPath(
        @PathVariable("id") rootId: UUID, @RequestParam("path", required = true) path: String): ResponseEntity<List<FileManagerEntry?>> {
        return ResponseEntity.ok(folderService.findByPath(rootId = rootId, fullPath = path))
    }

    @GetMapping(
        produces = [MediaType.APPLICATION_JSON_VALUE],
        params = ["q"]
    )
    fun getRoot(@RequestParam("q") query: String): ResponseEntity<List<FileManagerEntry.Folder?>> {
        return ResponseEntity.ok(folderService.findByName(query))
    }

}