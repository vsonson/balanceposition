package com.balpos.app.service;

import com.balpos.app.domain.NetworkMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing NetworkMember.
 */
public interface NetworkMemberService {

    /**
     * Save a networkMember.
     *
     * @param networkMember the entity to save
     * @return the persisted entity
     */
    NetworkMember save(NetworkMember networkMember);

    /**
     *  Get all the networkMembers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<NetworkMember> findAll(Pageable pageable);

    /**
     *  Get the "id" networkMember.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    NetworkMember findOne(Long id);

    /**
     *  Delete the "id" networkMember.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
