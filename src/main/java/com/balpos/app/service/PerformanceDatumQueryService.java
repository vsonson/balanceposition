package com.balpos.app.service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.balpos.app.domain.PerformanceDatum;
import com.balpos.app.domain.*; // for static metamodels
import com.balpos.app.repository.PerformanceDatumRepository;
import com.balpos.app.service.dto.PerformanceDatumCriteria;

import com.balpos.app.service.dto.PerformanceDatumDTO;
import com.balpos.app.service.mapper.PerformanceDatumMapper;

/**
 * Service for executing complex queries for PerformanceDatum entities in the database.
 * The main input is a {@link PerformanceDatumCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link PerformanceDatumDTO} or a {@link Page} of {%link PerformanceDatumDTO} which fullfills the criterias
 */
@Service
@Transactional(readOnly = true)
public class PerformanceDatumQueryService extends QueryService<PerformanceDatum> {

    private final Logger log = LoggerFactory.getLogger(PerformanceDatumQueryService.class);


    private final PerformanceDatumRepository performanceDatumRepository;

    private final PerformanceDatumMapper performanceDatumMapper;
    public PerformanceDatumQueryService(PerformanceDatumRepository performanceDatumRepository, PerformanceDatumMapper performanceDatumMapper) {
        this.performanceDatumRepository = performanceDatumRepository;
        this.performanceDatumMapper = performanceDatumMapper;
    }

    /**
     * Return a {@link List} of {%link PerformanceDatumDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PerformanceDatumDTO> findByCriteria(PerformanceDatumCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<PerformanceDatum> specification = createSpecification(criteria);
        return performanceDatumMapper.toDto(performanceDatumRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {%link PerformanceDatumDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PerformanceDatumDTO> findByCriteria(PerformanceDatumCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<PerformanceDatum> specification = createSpecification(criteria);
        final Page<PerformanceDatum> result = performanceDatumRepository.findAll(specification, page);
        return result.map(performanceDatumMapper::toDto);
    }

    /**
     * Function to convert PerformanceDatumCriteria to a {@link Specifications}
     */
    private Specifications<PerformanceDatum> createSpecification(PerformanceDatumCriteria criteria) {
        Specifications<PerformanceDatum> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), PerformanceDatum_.id));
            }
            if (criteria.getFeel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFeel(), PerformanceDatum_.feel));
            }
            if (criteria.getTimestamp() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTimestamp(), PerformanceDatum_.timestamp));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getUserId(), PerformanceDatum_.user, User_.id));
            }
        }
        return specification;
    }

}
