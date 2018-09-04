package com.balpos.app.repository;

import com.balpos.app.domain.Note;
import com.balpos.app.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


/**
 * Spring Data JPA repository for the Note entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findByUserAndDateBetween(User user, LocalDate start, LocalDate end);
}
