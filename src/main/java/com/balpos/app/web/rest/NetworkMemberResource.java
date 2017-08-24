package com.balpos.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.balpos.app.domain.NetworkMember;
import com.balpos.app.service.NetworkMemberService;
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
 * REST controller for managing NetworkMember.
 */
@RestController
@RequestMapping("/api")
public class NetworkMemberResource {

    private final Logger log = LoggerFactory.getLogger(NetworkMemberResource.class);

    private static final String ENTITY_NAME = "networkMember";

    private final NetworkMemberService networkMemberService;

    public NetworkMemberResource(NetworkMemberService networkMemberService) {
        this.networkMemberService = networkMemberService;
    }

    /**
     * POST  /network-members : Create a new networkMember.
     *
     * @param networkMember the networkMember to create
     * @return the ResponseEntity with status 201 (Created) and with body the new networkMember, or with status 400 (Bad Request) if the networkMember has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/network-members")
    @Timed
    public ResponseEntity<NetworkMember> createNetworkMember(@RequestBody NetworkMember networkMember) throws URISyntaxException {
        log.debug("REST request to save NetworkMember : {}", networkMember);
        if (networkMember.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new networkMember cannot already have an ID")).body(null);
        }
        NetworkMember result = networkMemberService.save(networkMember);
        return ResponseEntity.created(new URI("/api/network-members/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /network-members : Updates an existing networkMember.
     *
     * @param networkMember the networkMember to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated networkMember,
     * or with status 400 (Bad Request) if the networkMember is not valid,
     * or with status 500 (Internal Server Error) if the networkMember couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/network-members")
    @Timed
    public ResponseEntity<NetworkMember> updateNetworkMember(@RequestBody NetworkMember networkMember) throws URISyntaxException {
        log.debug("REST request to update NetworkMember : {}", networkMember);
        if (networkMember.getId() == null) {
            return createNetworkMember(networkMember);
        }
        NetworkMember result = networkMemberService.save(networkMember);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, networkMember.getId().toString()))
            .body(result);
    }

    /**
     * GET  /network-members : get all the networkMembers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of networkMembers in body
     */
    @GetMapping("/network-members")
    @Timed
    public ResponseEntity<List<NetworkMember>> getAllNetworkMembers(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of NetworkMembers");
        Page<NetworkMember> page = networkMemberService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/network-members");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /network-members/:id : get the "id" networkMember.
     *
     * @param id the id of the networkMember to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the networkMember, or with status 404 (Not Found)
     */
    @GetMapping("/network-members/{id}")
    @Timed
    public ResponseEntity<NetworkMember> getNetworkMember(@PathVariable Long id) {
        log.debug("REST request to get NetworkMember : {}", id);
        NetworkMember networkMember = networkMemberService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(networkMember));
    }

    /**
     * DELETE  /network-members/:id : delete the "id" networkMember.
     *
     * @param id the id of the networkMember to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/network-members/{id}")
    @Timed
    public ResponseEntity<Void> deleteNetworkMember(@PathVariable Long id) {
        log.debug("REST request to delete NetworkMember : {}", id);
        networkMemberService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
