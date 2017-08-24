package com.balpos.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.balpos.app.domain.UserNotification;
import com.balpos.app.service.UserNotificationService;
import com.balpos.app.web.rest.util.HeaderUtil;
import com.balpos.app.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing UserNotification.
 */
@RestController
@RequestMapping("/api")
public class UserNotificationResource {

    private final Logger log = LoggerFactory.getLogger(UserNotificationResource.class);

    private static final String ENTITY_NAME = "userNotification";

    private final UserNotificationService userNotificationService;

    public UserNotificationResource(UserNotificationService userNotificationService) {
        this.userNotificationService = userNotificationService;
    }

    /**
     * POST  /user-notifications : Create a new userNotification.
     *
     * @param userNotification the userNotification to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userNotification, or with status 400 (Bad Request) if the userNotification has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-notifications")
    @Timed
    public ResponseEntity<UserNotification> createUserNotification(@RequestBody UserNotification userNotification) throws URISyntaxException {
        log.debug("REST request to save UserNotification : {}", userNotification);
        if (userNotification.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new userNotification cannot already have an ID")).body(null);
        }
        UserNotification result = userNotificationService.save(userNotification);
        return ResponseEntity.created(new URI("/api/user-notifications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-notifications : Updates an existing userNotification.
     *
     * @param userNotification the userNotification to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userNotification,
     * or with status 400 (Bad Request) if the userNotification is not valid,
     * or with status 500 (Internal Server Error) if the userNotification couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-notifications")
    @Timed
    public ResponseEntity<UserNotification> updateUserNotification(@RequestBody UserNotification userNotification) throws URISyntaxException {
        log.debug("REST request to update UserNotification : {}", userNotification);
        if (userNotification.getId() == null) {
            return createUserNotification(userNotification);
        }
        UserNotification result = userNotificationService.save(userNotification);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userNotification.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-notifications : get all the userNotifications.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of userNotifications in body
     */
    @GetMapping("/user-notifications")
    @Timed
    public ResponseEntity<List<UserNotification>> getAllUserNotifications(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of UserNotifications");
        Page<UserNotification> page = userNotificationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/user-notifications");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /user-notifications/:id : get the "id" userNotification.
     *
     * @param id the id of the userNotification to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userNotification, or with status 404 (Not Found)
     */
    @GetMapping("/user-notifications/{id}")
    @Timed
    public ResponseEntity<UserNotification> getUserNotification(@PathVariable Long id) {
        log.debug("REST request to get UserNotification : {}", id);
        UserNotification userNotification = userNotificationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(userNotification));
    }

    /**
     * DELETE  /user-notifications/:id : delete the "id" userNotification.
     *
     * @param id the id of the userNotification to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-notifications/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserNotification(@PathVariable Long id) {
        log.debug("REST request to delete UserNotification : {}", id);
        userNotificationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
