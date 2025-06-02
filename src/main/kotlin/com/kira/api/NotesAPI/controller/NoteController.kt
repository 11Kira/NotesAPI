package com.kira.api.NotesAPI.controller

import com.kira.api.NotesAPI.database.model.Note
import com.kira.api.NotesAPI.database.repository.NoteRepository
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.time.Instant

@RestController
@RequestMapping("/api/notes")
class NoteController(
    private val noteRepository: NoteRepository
) {
    data class NoteRequest(
        @field:NotBlank(message = "Title should not be blank")
        val title: String,
        @field:NotBlank(message = "Body should not be blank")
        val body: String,
    )

    data class NoteResponse(
        val id: String,
        val title: String,
        val body: String,
        val createdAt: Instant,
        val updatedAt: Instant
    )

    @GetMapping
    fun getAllNotes(): List<Note> {
        return noteRepository.findAll()
    }

    @GetMapping("/{id}")
    fun getNoteById(@PathVariable("id") id: String): ResponseEntity<Note> {
        val note = noteRepository.findById(id)
        return if (note.isPresent) ResponseEntity.ok(note.get()) else ResponseEntity.notFound().build()
    }

    @PostMapping
    fun save(
        @Valid @RequestBody body: NoteRequest
    ): NoteResponse {
        val note = noteRepository.save(
            Note(
                title = body.title,
                body = body.body,
                createdAt = Instant.now(),
                updatedAt = Instant.now()
            )
        )
        return note.toResponse()
    }

    @PutMapping("/{id}")
    fun updateNoteById(
        @PathVariable id: String,
        @Valid @RequestBody body: NoteRequest
    ): NoteResponse {
        val existingNote = noteRepository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found!")
        }
        val updatedNote = existingNote.copy(
            title = body.title,
            body = body.body,
            updatedAt = Instant.now()
        )
        val savedNote = noteRepository.save(updatedNote)
        return savedNote.toResponse()
    }

    @DeleteMapping("/{id}")
    fun deleteNoteById(@PathVariable("id") id: String) {
        noteRepository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found!")
        }.apply {
            noteRepository.deleteById(id)
        }
    }

    private fun Note.toResponse(): NoteResponse =
        NoteResponse(
            id = this.id ?: "",
            title = title,
            body = body,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
}