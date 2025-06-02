package com.kira.api.NotesAPI.database.repository

import com.kira.api.NotesAPI.database.model.Note
import org.springframework.data.mongodb.repository.MongoRepository

interface NoteRepository: MongoRepository<Note, String>