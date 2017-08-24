package com.balpos.app.service.impl;

import com.balpos.app.service.NetworkMemberService;
import com.balpos.app.domain.NetworkMember;
import com.balpos.app.repository.NetworkMemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing NetworkMember.
 */
@Service
@Transactional
public class NetworkMemberServiceImpl implements NetworkMemberService{

    private final Logger log = LoggerFactory.getLogger(NetworkMemberServiceImpl.class);

    private final NetworkMemberRepository networkMemberRepository;
    public NetworkMemberServiceImpl(NetworkMemberRepository networkMemberRepository) {
        this.networkMemberRepository = networkMemberRepository;
    }

    /**
     * Save a networkMember.
     *
     * @param networkMember the entity to save
     * @return the persisted entity
     */
    @Override
    public NetworkMember save(NetworkMember networkMember) {
        log.debug("Request to save NetworkMember : {}", networkMember);
        return networkMemberRepository.save(networkMember);
    }

    /**
     *  Get all the networkMembers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<NetworkMember> findAll(Pageable pageable) {
        log.debug("Request to get all NetworkMembers");
        return networkMemberRepository.findAll(pageable);
    }

    /**
     *  Get one networkMember by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public NetworkMember findOne(Long id) {
        log.debug("Request to get NetworkMember : {}", id);
        return networkMemberRepository.findOne(id);
    }

    /**
     *  Delete the  networkMember by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete NetworkMember : {}", id);
        networkMemberRepository.delete(id);
    }
}
