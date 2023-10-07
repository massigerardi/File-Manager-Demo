package com.example.FileManager.api

import com.example.FileManager.access.HasAccessToFile
import com.example.FileManager.model.FileInput
import com.example.FileManager.model.FileManagerEntry
import com.example.FileManager.services.FileService
import com.example.FileManager.services.FileServiceImpl
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
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@RestController
@RequestMapping("/api/file")
class FileController(
        val fileService: FileService
) {

    @PostMapping(
            produces = [MediaType.APPLICATION_JSON_VALUE],
            consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
    )
    fun uploadFile(@RequestParam("file") files: MultipartFile, @RequestParam("folderId") folderId: UUID): ResponseEntity<FileManagerEntry.File> {
        return ResponseEntity.ok(fileService.upload(files, folderId))
    }

    @PutMapping(
        "/{id}",
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    @HasAccessToFile(id = "fileId", role = "modifyFile")
    fun update(
        @PathVariable("id") fileId: UUID,
        @RequestBody input: FileInput
        ): ResponseEntity<FileManagerEntry.File> {
        return ResponseEntity.ok(fileService.update(fileId = fileId, input = input))
    }

    @GetMapping(
        "/{id}",
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @HasAccessToFile(id = "fileId", role = "getFile")
    fun findById(
        @PathVariable("id") fileId: UUID): ResponseEntity<FileManagerEntry.File?> {
        return ResponseEntity.ok(fileService.getFile(fileId = fileId))
    }

    @DeleteMapping(
        "/{id}",
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @HasAccessToFile(id = "fileId", role = "deleteFile")
    fun delete(
        @PathVariable("id") fileId: UUID): ResponseEntity<FileManagerEntry.File> {
        return ResponseEntity.ok(fileService.delete(fileId = fileId)!!)
    }



}