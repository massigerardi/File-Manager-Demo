package com.example.FileManager.services

import com.example.FileManager.entities.FileEntity
import com.example.FileManager.entities.FolderEntity
import com.example.FileManager.model.FileManagerEntry
import com.example.FileManager.model.FolderInput
import com.example.FileManager.model.isNotBlank
import com.example.FileManager.model.isNullOrBlank
import com.example.FileManager.repositories.FileRepository
import com.example.FileManager.repositories.FolderRepository
import com.example.FileManager.repositories.QueryBuilder
import org.springframework.data.neo4j.core.Neo4jTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.nio.file.Path
import java.util.UUID
import javax.annotation.PostConstruct
import com.example.FileManager.services.FileManagerEntryMapper as mapper

@Service
class FolderServiceImpl (
    val folderRepository: FolderRepository,
    val fileRepository: FileRepository,
    val neo4jTemplate: Neo4jTemplate
) : FolderService {

    @PostConstruct
    fun setUp() {
        val root = folderRepository.findByName("/")
        if (root.isEmpty()) {
            val rootId = folderRepository.save(
                FolderEntity(
                    name = "/"
                )
            ).id!!
            val folder1 = createFolder(rootId, "folder1")
            val folder2 = createFolder(rootId, "folder2")
            val folder3 = createFolder(rootId, "folder3")
            createFolder(folder1.id, "folder11")
            createFolder(folder1.id, "folder12")
            createFolder(folder2.id, "folder21")
            createFolder(folder2.id, "folder22")
            createFolder(folder3.id, "folder31")
            val folder32 = createFolder(folder3.id, "folder32")
            createFolder(folder32.id, "folder321")
            createFolder(folder32.id, "folder322")
        }
    }

    override fun createFolder(folderInput: FolderInput): FileManagerEntry.Folder {
        return if (folderInput.folder.isNullOrBlank()) createSingleFolder(folderInput.name!!)
        else createFolder(folderInput.folder!!, folderInput.name!!)
    }

    private fun createFolder(folderId: UUID, folderName: String): FileManagerEntry.Folder {
        val folder = folderRepository.findById(folderId).get()
        val folderEntity = folderRepository.save(FolderEntity(
            name = folderName,
            parent = folderId
            ))
        folderRepository.save(folder.also { it.children.add(folderEntity) })
        return mapper.toFolderDto(folderEntity) as FileManagerEntry.Folder
    }

    private fun createSingleFolder(folderName: String): FileManagerEntry.Folder =
         folderRepository.save(FolderEntity(
            name = folderName
        )).let { mapper.toFolderDto(it) as FileManagerEntry.Folder }

    override fun getFolder(folderId: UUID): FileManagerEntry.Folder {
        return folderRepository.findById(folderId).orElseThrow().let { mapper.toFolderDto(it) as FileManagerEntry.Folder }
    }

    override fun getFolderFiles(folderId: UUID, deleted: Boolean): List<FileManagerEntry?> =
        when (deleted) {
            true -> getDeletedChildren(folderId)
            else -> getChildren(folderId)
        }

    private fun getChildren(folderId: UUID): List<FileManagerEntry?> =
        fileRepository.findFiles(folderId)
            .plus(folderRepository.findFolders(folderId))
            .map { mapper.toDto(it) }

    override fun getDeletedChildren(folderId: UUID): List<FileManagerEntry?> =
        fileRepository.findDeletedFiles(folderId)
            .plus(folderRepository.findDeletedFolders(folderId))
            .map { mapper.toDto(it) }


    override fun findByPath(rootId: UUID, fullPath: String): List<FileManagerEntry?> =
        neo4jTemplate.findAll(QueryBuilder.buildQuery(rootId, Path.of(fullPath), FileManagerEntry.File::class.java), FileEntity::class.java)
            .plus(neo4jTemplate.findAll(QueryBuilder.buildQuery(rootId, Path.of(fullPath), FileManagerEntry.Folder::class.java), FolderEntity::class.java))
            .map { mapper.toDto(it) }

    override fun attach(folderId: UUID, attachId: UUID): FileManagerEntry.Folder =
        folderRepository.findById(folderId).orElseThrow()?.let { folderRepository.attach(folderId, attachId).let { mapper.toFolderDto(it) } }!!

    override fun detach(folderId: UUID, attachId: UUID): FileManagerEntry.Folder =
        folderRepository.findById(folderId).orElseThrow()?.let { folderRepository.detach(folderId, attachId).let { mapper.toFolderDto(it) } }!!

    @Transactional
    override fun update(folderId: UUID, input: FolderInput): FileManagerEntry.Folder {
        val folder = folderRepository.findById(folderId).orElseThrow()
        if (input.folder.isNotBlank()) folderRepository.findById(input.folder!!).orElseThrow().let { folderRepository.moveFolder(input.folder, folderId) }
        if (input.hasChanges()) {
            folderRepository.save(folder.also {
                if (input.name?.isNotBlank() == true) it.name = input.name
                if (input.folder?.isNotBlank()  == true) it.parent = input.folder
            }
            )
        }
        return folderRepository.findById(folderId).orElseThrow().let { mapper.toFolderDto(it!!) as FileManagerEntry.Folder}
    }

    override fun delete(folderId: UUID): FileManagerEntry.Folder =
        folderRepository.delete(folderId).let { mapper.toFolderDto(it!!) as FileManagerEntry.Folder}

    override fun search(folderId: UUID, query: String): List<FileManagerEntry?> =
        fileRepository.findFilesByName(folderId, query)
            .plus(folderRepository.findFoldersByName(folderId, query))
            .map { mapper.toDto(it) }

    override fun findByName(query: String): List<FileManagerEntry.Folder?> {
        return folderRepository.findByName(query).map { mapper.toFolderDto(it!!) as FileManagerEntry.Folder }
    }

}