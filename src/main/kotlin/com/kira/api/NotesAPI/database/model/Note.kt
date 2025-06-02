package com.kira.api.NotesAPI.database.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("notes")
data class Note(
    @Id
    val id: String? = null,
    val title: String = "",
    val body: String = "",
    val createdAt: Instant,
    val updatedAt: Instant
)