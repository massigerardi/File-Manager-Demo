package com.example.FileManager.repositories

import com.example.FileManager.entities.FileEntity
import org.springframework.data.neo4j.repository.query.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface FileRepository : PagingAndSortingRepository<FileEntity, UUID> {

  @Query("match (p:Folder)-[rel:CONTAINS]->(f:File{id:\$fileId}),(d:Folder{id:\$destinationId}) MERGE (d)-[:CONTAINS]->(f) DELETE rel return f")
  fun moveFile(destinationId: UUID, fileId: UUID): FileEntity?

  @Query("match (p:Folder)-[rel:CONTAINS]->(f:File{id:\$fileId}) MERGE (p)-[:SEES]-(f) DELETE rel return f")
  fun delete(fileId: UUID): FileEntity?

  @Query("match (:Folder{id:\$folderId})-[:CONTAINS*]->(result:File) where toLower(result.name) CONTAINS toLower(\$name) return result")
  fun findFilesByName(folderId: UUID, name: String): List<FileEntity>

  @Query("match (p:Folder{id:\$folderId})-[:SEES]->(F:File) return F")
  fun findDeletedFiles(folderId: UUID): List<FileEntity>

  @Query("match (p:Folder{id:\$folderId})-[:CONTAINS]->(F:File) return F")
  fun findFiles(folderId: UUID): List<FileEntity>

}
