package com.balpos.app.service.impl;

import com.balpos.app.service.WellnessItemService;
import com.balpos.app.domain.WellnessItem;
import com.balpos.app.repository.WellnessItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing WellnessItem.
 */
@Service
@Transactional
public class WellnessItemServiceImpl implements WellnessItemService{

    private final Logger log = LoggerFactory.getLogger(WellnessItemServiceImpl.class);

    private final WellnessItemRepository wellnessItemRepository;
    public WellnessItemServiceImpl(WellnessItemRepository wellnessItemRepository) {
        this.wellnessItemRepository = wellnessItemRepository;
    }

    /**
     * Save a wellnessItem.
     *
     * @param wellnessItem the entity to save
     * @return the persisted entity
     */
    @Override
    public WellnessItem save(WellnessItem wellnessItem) {
        log.debug("Request to save WellnessItem : {}", wellnessItem);
        return wellnessItemRepository.save(wellnessItem);
    }

    /**
     *  Get all the wellnessItems.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<WellnessItem> findAll(Pageable pageable) {
        log.debug("Request to get all WellnessItems");
        return wellnessItemRepository.findAll(pageable);
    }

    /**
     *  Get one wellnessItem by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public WellnessItem findOne(Long id) {
        log.debug("Request to get WellnessItem : {}", id);
        return wellnessItemRepository.findOne(id);
    }

    /**
     *  Delete the  wellnessItem by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete WellnessItem : {}", id);
        wellnessItemRepository.delete(id);
    }
}
