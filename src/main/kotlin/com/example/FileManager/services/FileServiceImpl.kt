package com.example.FileManager.services

import com.example.FileManager.entities.FileEntity
import com.example.FileManager.model.FileInput
import com.example.FileManager.model.FileManagerEntry
import com.example.FileManager.model.isNotBlank
import com.example.FileManager.model.isNullOrBlank
import com.example.FileManager.repositories.FileRepository
import com.example.FileManager.repositories.FolderRepository
import com.example.FileManager.services.FileManagerEntryMapper as mapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@Service
class FileServiceImpl(
    val fileRepository: FileRepository,
    val folderRepository: FolderRepository,
    val s3Client: S3Client
) : FileService {

    @Transactional
    override fun upload(file: MultipartFile, folderId: UUID) : FileManagerEntry.File {
        val folder = folderRepository.findById(folderId).orElseThrow()
        val filename = file.originalFilename
        val contentType = file.contentType!!
        val size = file.size.toInt()
        val url = s3Client.putObject(filename!!)
        val fileEntity = fileRepository.save(FileEntity(
            name = filename,
            parent = folderId,
            contentType = contentType,
            size = size,
            url = url,
        ))
        folderRepository.save(folder.also { it.children.add(fileEntity) })
        return mapper.toFileDto(fileEntity) as FileManagerEntry.File
    }

    override fun getFile(fileId: UUID): FileManagerEntry.File {
        return mapper.toFileDto(fileRepository.findById(fileId).get()) as FileManagerEntry.File
    }

    override fun update(fileId: UUID, action: String, input: FileInput): FileManagerEntry.File {
        val file = fileRepository.findById(fileId).orElseThrow()
        if (input.folder.isNotBlank()) folderRepository.findById(input.folder!!).orElseThrow().let { fileRepository.moveFile(it.id!!, fileId) }
        if (input.hasChanges()) {
            fileRepository.save(file.also {
                if (input.name?.isNotBlank() == true) it.name = input.name
                if (input.folder?.isNotBlank()  == true) it.parent = input.folder
                if (input.contentType?.isNotBlank()  == true) it.contentType = input.contentType
            }
            )
        }
        return fileRepository.findById(fileId).orElseThrow().let { mapper.toFileDto(it!!) as FileManagerEntry.File}
    }

    override fun delete(fileId: UUID): FileManagerEntry.File? {
        return fileRepository.findById(fileId).orElseThrow()
            .let { fileRepository.delete(fileId) }
            .let { mapper.toFileDto(it!!) as FileManagerEntry.File }
    }
}