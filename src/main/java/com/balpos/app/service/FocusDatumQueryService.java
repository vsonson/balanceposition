package com.balpos.app.service;


import java.time.ZonedDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.balpos.app.domain.FocusDatum;
import com.balpos.app.domain.*; // for static metamodels
import com.balpos.app.repository.FocusDatumRepository;
import com.balpos.app.service.dto.FocusDatumCriteria;

import com.balpos.app.service.dto.FocusDatumDTO;
import com.balpos.app.service.mapper.FocusDatumMapper;

/**
 * Service for executing complex queries for FocusDatum entities in the database.
 * The main input is a {@link FocusDatumCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link FocusDatumDTO} or a {@link Page} of {%link FocusDatumDTO} which fullfills the criterias
 */
@Service
@Transactional(readOnly = true)
public class FocusDatumQueryService extends QueryService<FocusDatum> {

    private final Logger log = LoggerFactory.getLogger(FocusDatumQueryService.class);


    private final FocusDatumRepository focusDatumRepository;

    private final FocusDatumMapper focusDatumMapper;
    public FocusDatumQueryService(FocusDatumRepository focusDatumRepository, FocusDatumMapper focusDatumMapper) {
        this.focusDatumRepository = focusDatumRepository;
        this.focusDatumMapper = focusDatumMapper;
    }

    /**
     * Return a {@link List} of {%link FocusDatumDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FocusDatumDTO> findByCriteria(FocusDatumCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<FocusDatum> specification = createSpecification(criteria);
        return focusDatumMapper.toDto(focusDatumRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {%link FocusDatumDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FocusDatumDTO> findByCriteria(FocusDatumCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<FocusDatum> specification = createSpecification(criteria);
        final Page<FocusDatum> result = focusDatumRepository.findAll(specification, page);
        return result.map(focusDatumMapper::toDto);
    }

    /**
     * Function to convert FocusDatumCriteria to a {@link Specifications}
     */
    private Specifications<FocusDatum> createSpecification(FocusDatumCriteria criteria) {
        Specifications<FocusDatum> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), FocusDatum_.id));
            }
            if (criteria.getLevel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLevel(), FocusDatum_.level));
            }
            if (criteria.getTimestamp() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTimestamp(), FocusDatum_.timestamp));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getUserId(), FocusDatum_.user, User_.id));
            }
        }
        return specification;
    }

}
