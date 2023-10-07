package com.example.FileManager.entities

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship
import java.time.Instant
import java.util.UUID


sealed class FileManagerEntity(): AbstractIdEntity<UUID>() {
    @CreatedDate
    var created: Instant = Instant.now()
    @LastModifiedDate
    var updated: Instant = Instant.now()
}


@Node(labels = ["Folder"])
data class FolderEntity(
    var name: String = "",
    var parent: UUID? = null,

    @Relationship(type = "CONTAINS", direction = Relationship.Direction.OUTGOING)
    var children: MutableList<FileManagerEntity> = mutableListOf(),

    @Relationship(type = "SEES", direction = Relationship.Direction.OUTGOING)
    var deleted: MutableList<FileManagerEntity> = mutableListOf(),
): FileManagerEntity()


@Node(labels = ["File"])
data class FileEntity(
    var name: String = "",
    var parent: UUID,
    var contentType: String = "",
    var size: Int? = null,
    var url: String? = null,
    var scanStatus: String = "PENDING",
): FileManagerEntity()


