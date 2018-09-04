package com.balpos.app.service.impl;

import com.balpos.app.domain.Note;
import com.balpos.app.domain.User;
import com.balpos.app.repository.NoteRepository;
import com.balpos.app.service.NoteService;
import com.balpos.app.service.dto.NoteDTO;
import com.balpos.app.service.mapper.NoteMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


/**
 * Service Implementation for managing Note.
 */
@Service
@Transactional
@Slf4j
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;

    public NoteServiceImpl(NoteRepository noteRepository, NoteMapper noteMapper) {
        this.noteRepository = noteRepository;
        this.noteMapper = noteMapper;
    }

    /**
     * Save a note.
     *
     * @param note the entity to save
     * @param user
     * @return the persisted entity
     */
    @Override
    public NoteDTO save(NoteDTO note, User user) {
        log.debug("Request to save Note : {}", note);
        Note entity = noteMapper.toEntity(note);
        entity.setUser(user);
        return noteMapper.toDto(noteRepository.save(entity));
    }

    /**
     * Get all the notes.
     */
    @Override
    @Transactional(readOnly = true)
    public List<NoteDTO> findAll(User user, LocalDate dateOfNote) {
        log.debug("Request to get all Notes");
        return noteMapper.toDto(noteRepository.findByUserAndDateBetween(user, dateOfNote, dateOfNote));
    }

    /**
     * Get one note by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public NoteDTO findOne(Long id, User user) {
        log.debug("Request to get Note : {}", id);
        Note entity = noteRepository.findOne(id);
        if (entity == null
            || !user.equals(entity.getUser())) {
            return null;
        }
        return noteMapper.toDto(entity);
    }

    /**
     * Delete the  note by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Note : {}", id);
        noteRepository.delete(id);
    }
}
