package com.example.FileManager.repositories

import com.example.FileManager.model.FileManagerEntry
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.nio.file.Path
import java.util.UUID

class FileRepositoryTest {

    @Test
    fun testBuildQuery() {
        val rootId = UUID.fromString("ea53d922-e060-407b-ae35-72dfb1a15d33")
        val query = QueryBuilder.buildQuery(rootId, Path.of("/folder1/folder11"), FileManagerEntry.File::class.java)
        assertThat(query).isEqualTo("match (:Folder{id:'ea53d922-e060-407b-ae35-72dfb1a15d33'})-[:CONTAINS]->(:Folder{name:'folder1'})-[:CONTAINS]->(:Folder{name:'folder11'})-[:CONTAINS]->(F:File) return F")
    }
 
}