package com.example.FileManager.entities

import org.springframework.data.annotation.Version
import org.springframework.data.neo4j.core.schema.GeneratedValue
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.util.ProxyUtils
import java.io.Serializable
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class AbstractIdEntity<T : Serializable> : BaseEntity() {

  @Id
  @GeneratedValue
  var id: T? = null

  @Version
  var version: Long? = null

  override fun equals(other: Any?): Boolean {
    other ?: return false

    if (this === other) return true

    if (javaClass != ProxyUtils.getUserClass(other)) return false

    other as AbstractIdEntity<*>

    return if (null == this.id) false else this.id == other.id
  }

  override fun hashCode(): Int {
    return 31
  }

  override fun toString() = "Entity of type ${this.javaClass.name} with id: $id"
}
