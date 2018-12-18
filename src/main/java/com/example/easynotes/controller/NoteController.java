package com.example.easynotes.controller;

import com.example.easynotes.exception.ResourceNotFoundException;
import com.example.easynotes.model.Note;
import com.example.easynotes.repository.NoteRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by rajeevkumarsingh on 27/06/17.
 */
@RestController

@RequestMapping("/notes")
@Api(value = "noteController", description = "Api de gerenciamento de anotações")
public class NoteController {

    @Autowired
    NoteRepository noteRepository;

    @CrossOrigin(origins = "http://localhost:4200")
    @ApiOperation(value="Buscar todas as anotações")
    @GetMapping("/")
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @ApiOperation(value="Incluir uma nova anotação")
    @PostMapping("/")
    public Note createNote(@Valid @RequestBody Note note) {
        return noteRepository.save(note);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @ApiOperation(value="Buscar uma anotação pelo ID")
    @GetMapping("/{id}")
    public Note getNoteById(@PathVariable(value = "id") Long noteId) {
        return noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note", "id", noteId));
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @ApiOperation(value="Atualizar uma anotação por ID")
    @PutMapping("/{id}")
    public Note updateNote(@PathVariable(value = "id") Long noteId,
                                           @Valid @RequestBody Note noteDetails) {

        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note", "id", noteId));

        note.setTitle(noteDetails.getTitle());
        note.setContent(noteDetails.getContent());

        Note updatedNote = noteRepository.save(note);
        return updatedNote;
    } 

    @CrossOrigin(origins = "http://localhost:4200")
    @ApiOperation(value="Remover uma anotação por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable(value = "id") Long noteId) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note", "id", noteId));

        noteRepository.delete(note);

        return ResponseEntity.ok().build();
    }
}
