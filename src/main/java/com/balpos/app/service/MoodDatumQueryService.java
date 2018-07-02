package com.balpos.app.service;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.balpos.app.domain.MoodDatum;
import com.balpos.app.domain.*; // for static metamodels
import com.balpos.app.repository.MoodDatumRepository;
import com.balpos.app.service.dto.MoodDatumCriteria;

import com.balpos.app.service.dto.MoodDatumDTO;
import com.balpos.app.service.mapper.MoodDatumMapper;

/**
 * Service for executing complex queries for MoodDatum entities in the database.
 * The main input is a {@link MoodDatumCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link MoodDatumDTO} or a {@link Page} of {%link MoodDatumDTO} which fullfills the criterias
 */
@Service
@Transactional(readOnly = true)
@Slf4j
public class MoodDatumQueryService extends QueryService<MoodDatum> {

    private final MoodDatumRepository moodDatumRepository;

    private final MoodDatumMapper moodDatumMapper;
    public MoodDatumQueryService(MoodDatumRepository moodDatumRepository, MoodDatumMapper moodDatumMapper) {
        this.moodDatumRepository = moodDatumRepository;
        this.moodDatumMapper = moodDatumMapper;
    }

    /**
     * Return a {@link List} of {%link MoodDatumDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MoodDatumDTO> findByCriteria(MoodDatumCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<MoodDatum> specification = createSpecification(criteria);
        return moodDatumMapper.toDto(moodDatumRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {%link MoodDatumDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MoodDatumDTO> findByCriteria(MoodDatumCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<MoodDatum> specification = createSpecification(criteria);
        final Page<MoodDatum> result = moodDatumRepository.findAll(specification, page);
        return result.map(moodDatumMapper::toDto);
    }

    /**
     * Function to convert MoodDatumCriteria to a {@link Specifications}
     */
    private Specifications<MoodDatum> createSpecification(MoodDatumCriteria criteria) {
        Specifications<MoodDatum> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), MoodDatum_.id));
            }
            if (criteria.getValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getValue(), MoodDatum_.value));
            }
            if (criteria.getTimestamp() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTimestamp(), MoodDatum_.timestamp));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getUserId(), MoodDatum_.user, User_.id));
            }
        }
        return specification;
    }

}
