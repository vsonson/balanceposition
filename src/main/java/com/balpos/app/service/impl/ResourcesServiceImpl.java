package com.balpos.app.service.impl;

import com.balpos.app.service.ResourcesService;
import com.balpos.app.domain.Resources;
import com.balpos.app.repository.ResourcesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Resources.
 */
@Service
@Transactional
public class ResourcesServiceImpl implements ResourcesService{

    private final Logger log = LoggerFactory.getLogger(ResourcesServiceImpl.class);

    private final ResourcesRepository resourcesRepository;
    public ResourcesServiceImpl(ResourcesRepository resourcesRepository) {
        this.resourcesRepository = resourcesRepository;
    }

    /**
     * Save a resources.
     *
     * @param resources the entity to save
     * @return the persisted entity
     */
    @Override
    public Resources save(Resources resources) {
        log.debug("Request to save Resources : {}", resources);
        return resourcesRepository.save(resources);
    }

    /**
     *  Get all the resources.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Resources> findAll(Pageable pageable) {
        log.debug("Request to get all Resources");
        return resourcesRepository.findAll(pageable);
    }

    /**
     *  Get one resources by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Resources findOne(Long id) {
        log.debug("Request to get Resources : {}", id);
        return resourcesRepository.findOne(id);
    }

    /**
     *  Delete the  resources by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Resources : {}", id);
        resourcesRepository.delete(id);
    }
}
