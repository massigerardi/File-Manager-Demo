package com.example.FileManager.repositories

import com.example.FileManager.entities.FileEntity
import com.example.FileManager.entities.FileManagerEntity
import com.example.FileManager.entities.FolderEntity
import com.example.FileManager.model.Labels
import com.example.FileManager.model.Relationship
import org.springframework.data.neo4j.repository.query.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface FolderRepository : PagingAndSortingRepository<FolderEntity, UUID> {

  fun findByName(name: String): List<FolderEntity?>

  @Query("match (p:Folder)-[rel:CONTAINS]->(f:Folder{id:\$fileId}),(d:Folder{id:\$destinationId}) MERGE (d)-[:CONTAINS]->(f) DELETE rel return f")
  fun moveFolder(destinationId: UUID, fileId: UUID): FolderEntity?

  @Query("match (d:Folder{id:\$folderId})-[rel:CONTAINS]->(f{id:\$attachId}) DELETE rel return d")
  fun detach(folderId: UUID, attachId: UUID): FolderEntity

  @Query("match (f{id:\$attachId}),(d:Folder{id:\$folderId}) MERGE (d)-[:CONTAINS]->(f) return d")
  fun attach(folderId: UUID, attachId: UUID): FolderEntity

  @Query("match (p:Folder)-[rel:CONTAINS]->(f:Folder{id:\$folderId}) MERGE (p)-[:SEES]-(f) DELETE rel return f")
  fun delete(folderId: UUID): FolderEntity?

  @Query("match (:Folder{id:\$folderId})-[:CONTAINS*]->(result:Folder) where toLower(result.name) CONTAINS toLower(\$name) return result")
  fun findFoldersByName(folderId: UUID, name: String): List<FolderEntity>

  @Query("match (p:Folder{id:\$folderId})-[:CONTAINS]->(F:Folder) return F")
  fun findFolders(folderId: UUID): List<FolderEntity>

  @Query("match (p:Folder{id:\$folderId})-[:SEES]->(F:Folder) return F")
  fun findDeletedFolders(folderId: UUID): List<FolderEntity>

}
