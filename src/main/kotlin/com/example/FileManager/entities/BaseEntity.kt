package com.example.FileManager.entities

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import com.vladmihalcea.hibernate.type.json.JsonStringType
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import javax.persistence.MappedSuperclass

@TypeDefs(
  TypeDef(name = "json", typeClass = JsonStringType::class),
  TypeDef(name = "jsonb", typeClass = JsonBinaryType::class)
)
@MappedSuperclass
abstract class BaseEntity {
  companion object {
    private val serialVersionUID = -5554308939380869754L
  }
}
