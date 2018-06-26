package com.balpos.app.service;


import com.balpos.app.domain.StressDatum;
import com.balpos.app.domain.StressDatum_;
import com.balpos.app.domain.User_;
import com.balpos.app.repository.StressDatumRepository;
import com.balpos.app.service.dto.StressDatumCriteria;
import com.balpos.app.service.dto.StressDatumDTO;
import com.balpos.app.service.mapper.StressDatumMapper;
import io.github.jhipster.service.QueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for executing complex queries for StressDatum entities in the database.
 * The main input is a {@link StressDatumCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link StressDatumDTO} or a {@link Page} of {%link StressDatumDTO} which fullfills the criterias
 */
@Service
@Transactional(readOnly = true)
@Slf4j
public class StressDatumQueryService extends QueryService<StressDatum> {

    private final StressDatumRepository stressDatumRepository;

    private final StressDatumMapper stressDatumMapper;

    public StressDatumQueryService(StressDatumRepository stressDatumRepository, StressDatumMapper stressDatumMapper) {
        this.stressDatumRepository = stressDatumRepository;
        this.stressDatumMapper = stressDatumMapper;
    }

    /**
     * Return a {@link List} of {%link StressDatumDTO} which matches the criteria from the database
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StressDatumDTO> findByCriteria(StressDatumCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<StressDatum> specification = createSpecification(criteria);
        return stressDatumMapper.toDto(stressDatumRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {%link StressDatumDTO} which matches the criteria from the database
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StressDatumDTO> findByCriteria(StressDatumCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<StressDatum> specification = createSpecification(criteria);
        final Page<StressDatum> result = stressDatumRepository.findAll(specification, page);
        return result.map(stressDatumMapper::toDto);
    }

    /**
     * Function to convert StressDatumCriteria to a {@link Specifications}
     */
    private Specifications<StressDatum> createSpecification(StressDatumCriteria criteria) {
        Specifications<StressDatum> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getValue(), StressDatum_.value));
            }
            if (criteria.getTimestamp() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTimestamp(), StressDatum_.timestamp));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getUserId(), StressDatum_.user, User_.id));
            }
        }
        return specification;
    }

}
