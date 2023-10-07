package com.example.FileManager.repositories

import java.nio.file.Path
import java.util.UUID

object QueryBuilder {
        //MATCH (:Folder{id:'56c0b362-20cf-41db-9498-3c60730af3b8'})-[:CONTAINS]->(D:Folder{name:'folder3'}) OPTIONAL MATCH (D)-[:CONTAINS]->(F:File) OPTIONAL MATCH (D)-[:CONTAINS]->(FF:Folder) return F,FF
        fun buildQuery(rootId: UUID, path: Path, result: Class<*>): String {
            val query = StringBuilder("match (:Folder{id:'$rootId'})-[:CONTAINS]->")
            path.forEach {
                query.append("(:Folder{name:'$it'})-[:CONTAINS]->")
            }

            query.append("(F:${result.simpleName}) return F")
            return query.toString()
        }
}