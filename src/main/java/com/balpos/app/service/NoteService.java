package com.balpos.app.service;

import com.balpos.app.domain.User;
import com.balpos.app.service.dto.NoteDTO;

import java.time.LocalDate;
import java.util.List;

/**
 * Service Interface for managing Note.
 */
public interface NoteService {

    /**
     * Save a note.
     *
     * @param note the entity to save
     * @param user
     * @return the persisted entity
     */
    NoteDTO save(NoteDTO note, User user);

    List<NoteDTO> findAll(User user, LocalDate date);

    /**
     * Get the "id" note.
     *
     * @param id the id of the entity
     * @return the entity
     */
    NoteDTO findOne(Long id, User user);

    /**
     * Delete the "id" note.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
