package com.balpos.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.balpos.app.domain.KeyPair;
import com.balpos.app.service.KeyPairService;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing KeyPair.
 */
@RestController
@RequestMapping("/api")
public class KeyPairResource {

    private final Logger log = LoggerFactory.getLogger(KeyPairResource.class);

    private static final String ENTITY_NAME = "keyPair";

    private final KeyPairService keyPairService;

    public KeyPairResource(KeyPairService keyPairService) {
        this.keyPairService = keyPairService;
    }

    /**
     * POST  /key-pairs : Create a new keyPair.
     *
     * @param keyPair the keyPair to create
     * @return the ResponseEntity with status 201 (Created) and with body the new keyPair, or with status 400 (Bad Request) if the keyPair has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/key-pairs")
    @Timed
    public ResponseEntity<KeyPair> createKeyPair(@Valid @RequestBody KeyPair keyPair) throws URISyntaxException {
        log.debug("REST request to save KeyPair : {}", keyPair);
        if (keyPair.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new keyPair cannot already have an ID")).body(null);
        }
        KeyPair result = keyPairService.save(keyPair);
        return ResponseEntity.created(new URI("/api/key-pairs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /key-pairs : Updates an existing keyPair.
     *
     * @param keyPair the keyPair to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated keyPair,
     * or with status 400 (Bad Request) if the keyPair is not valid,
     * or with status 500 (Internal Server Error) if the keyPair couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/key-pairs")
    @Timed
    public ResponseEntity<KeyPair> updateKeyPair(@Valid @RequestBody KeyPair keyPair) throws URISyntaxException {
        log.debug("REST request to update KeyPair : {}", keyPair);
        if (keyPair.getId() == null) {
            return createKeyPair(keyPair);
        }
        KeyPair result = keyPairService.save(keyPair);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, keyPair.getId().toString()))
            .body(result);
    }

    /**
     * GET  /key-pairs : get all the keyPairs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of keyPairs in body
     */
    @GetMapping("/key-pairs")
    @Timed
    public ResponseEntity<List<KeyPair>> getAllKeyPairs(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of KeyPairs");
        Page<KeyPair> page = keyPairService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/key-pairs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /key-pairs/:id : get the "id" keyPair.
     *
     * @param id the id of the keyPair to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the keyPair, or with status 404 (Not Found)
     */
    @GetMapping("/key-pairs/{id}")
    @Timed
    public ResponseEntity<KeyPair> getKeyPair(@PathVariable Long id) {
        log.debug("REST request to get KeyPair : {}", id);
        KeyPair keyPair = keyPairService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(keyPair));
    }

    /**
     * DELETE  /key-pairs/:id : delete the "id" keyPair.
     *
     * @param id the id of the keyPair to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/key-pairs/{id}")
    @Timed
    public ResponseEntity<Void> deleteKeyPair(@PathVariable Long id) {
        log.debug("REST request to delete KeyPair : {}", id);
        keyPairService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
