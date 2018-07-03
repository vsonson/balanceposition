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

import com.balpos.app.domain.SleepDatum;
import com.balpos.app.domain.*; // for static metamodels
import com.balpos.app.repository.SleepDatumRepository;
import com.balpos.app.service.dto.SleepDatumCriteria;

import com.balpos.app.service.dto.SleepDatumDTO;
import com.balpos.app.service.mapper.SleepDatumMapper;

/**
 * Service for executing complex queries for SleepDatum entities in the database.
 * The main input is a {@link SleepDatumCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link SleepDatumDTO} or a {@link Page} of {%link SleepDatumDTO} which fullfills the criterias
 */
@Service
@Transactional(readOnly = true)
public class SleepDatumQueryService extends QueryService<SleepDatum> {

    private final Logger log = LoggerFactory.getLogger(SleepDatumQueryService.class);


    private final SleepDatumRepository sleepDatumRepository;

    private final SleepDatumMapper sleepDatumMapper;
    public SleepDatumQueryService(SleepDatumRepository sleepDatumRepository, SleepDatumMapper sleepDatumMapper) {
        this.sleepDatumRepository = sleepDatumRepository;
        this.sleepDatumMapper = sleepDatumMapper;
    }

    /**
     * Return a {@link List} of {%link SleepDatumDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SleepDatumDTO> findByCriteria(SleepDatumCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<SleepDatum> specification = createSpecification(criteria);
        return sleepDatumMapper.toDto(sleepDatumRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {%link SleepDatumDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SleepDatumDTO> findByCriteria(SleepDatumCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<SleepDatum> specification = createSpecification(criteria);
        final Page<SleepDatum> result = sleepDatumRepository.findAll(specification, page);
        return result.map(sleepDatumMapper::toDto);
    }

    /**
     * Function to convert SleepDatumCriteria to a {@link Specifications}
     */
    private Specifications<SleepDatum> createSpecification(SleepDatumCriteria criteria) {
        Specifications<SleepDatum> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), SleepDatum_.id));
            }
            if (criteria.getDurationHours() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDurationHours(), SleepDatum_.durationHours));
            }
            if (criteria.getFeel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFeel(), SleepDatum_.feel));
            }
            if (criteria.getTimestamp() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTimestamp(), SleepDatum_.timestamp));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getUserId(), SleepDatum_.user, User_.id));
            }
        }
        return specification;
    }

}
