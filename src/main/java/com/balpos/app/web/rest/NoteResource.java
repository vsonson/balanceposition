package com.balpos.app.web.rest;

import com.balpos.app.domain.User;
import com.balpos.app.service.NoteService;
import com.balpos.app.service.dto.NoteDTO;
import com.balpos.app.service.util.UserResourceUtil;
import com.balpos.app.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Note.
 */
@RestController
@Slf4j
@RequestMapping("/api")
public class NoteResource {

    private static final String ENTITY_NAME = "note";

    private final NoteService noteService;
    private final UserResourceUtil userResourceUtil;

    public NoteResource(NoteService noteService, UserResourceUtil userResourceUtil) {
        this.noteService = noteService;
        this.userResourceUtil = userResourceUtil;
    }

    /**
     * POST  /notes : Create a new note.
     *
     * @param note the note to create
     * @return the ResponseEntity with status 201 (Created) and with body the new note, or with status 400 (Bad Request) if the note has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/notes")
    @Timed
    public ResponseEntity<NoteDTO> createNote(@RequestBody NoteDTO note, Principal principal) throws URISyntaxException {
        log.debug("REST request to save Note : {}", note);
        User user = userResourceUtil.getUserFromLogin(principal.getName()).get();
        if (note.getId() != null) {
            NoteDTO noteFromDb = noteService.findOne(note.getId(), user);
            if (noteFromDb == null) {
                return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new note cannot already have an ID")).body(null);
            }
        }
        NoteDTO result = noteService.save(note, user);
        return ResponseEntity.created(new URI("/api/notes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * GET  /notes : get all the notes.
     */
    @GetMapping("/notes")
    @Timed
    public ResponseEntity<Map<String, List<NoteDTO>>> getAllNotes(@RequestParam LocalDate dateOfNote, Principal principal) {
        log.debug("REST request to get a page of Notes");
        User user = userResourceUtil.getUserFromLogin(principal.getName()).get();
        List<NoteDTO> notes = noteService.findAll(user, dateOfNote);

        Map<String, List<NoteDTO>> groupedNotes = notes.stream().collect(Collectors.groupingBy(NoteDTO::getCategory));

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(groupedNotes));
    }

    /**
     * DELETE  /notes/:id : delete the "id" note.
     *
     * @param id the id of the note to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/notes/{id}")
    @Timed
    public ResponseEntity<Void> deleteNote(@PathVariable Long id, Principal principal) {
        log.debug("REST request to delete Note : {}", id);
        User user = userResourceUtil.getUserFromLogin(principal.getName()).get();
        NoteDTO note = noteService.findOne(id, user);
        if (note == null) {
            return ResponseEntity.badRequest().build();
        }
        noteService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
