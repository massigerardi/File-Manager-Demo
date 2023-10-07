package com.example.FileManager

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories


@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
@EnableNeo4jRepositories
class FileManagerDemoApplication

fun main(args: Array<String>) {
	runApplication<FileManagerDemoApplication>(*args)
}
