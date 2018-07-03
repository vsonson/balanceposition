package com.balpos.app.service;

import com.balpos.app.domain.BodyDatum;
import com.balpos.app.domain.BodyDatum_;
import com.balpos.app.domain.User_;
import com.balpos.app.repository.BodyDatumRepository;
import com.balpos.app.service.dto.BodyDatumCriteria;
import com.balpos.app.service.dto.BodyDatumDTO;
import com.balpos.app.service.mapper.BodyDatumMapper;
import io.github.jhipster.service.QueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for executing complex queries for BodyDatum entities in the database.
 * The main input is a {@link BodyDatumCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link BodyDatumDTO} or a {@link Page} of {%link BodyDatumDTO} which fullfills the criterias
 */
@Service
@Transactional(readOnly = true)
@Slf4j
public class BodyDatumQueryService extends QueryService<BodyDatum> {

    private final BodyDatumRepository bodyDatumRepository;

    private final BodyDatumMapper bodyDatumMapper;

    public BodyDatumQueryService(BodyDatumRepository bodyDatumRepository, BodyDatumMapper bodyDatumMapper) {
        this.bodyDatumRepository = bodyDatumRepository;
        this.bodyDatumMapper = bodyDatumMapper;
    }

    /**
     * Return a {@link List} of {%link BodyDatumDTO} which matches the criteria from the database
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BodyDatumDTO> findByCriteria(BodyDatumCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<BodyDatum> specification = createSpecification(criteria);
        return bodyDatumMapper.toDto(bodyDatumRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {%link BodyDatumDTO} which matches the criteria from the database
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BodyDatumDTO> findByCriteria(BodyDatumCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<BodyDatum> specification = createSpecification(criteria);
        final Page<BodyDatum> result = bodyDatumRepository.findAll(specification, page);
        return result.map(bodyDatumMapper::toDto);
    }

    /**
     * Function to convert BodyDatumCriteria to a {@link Specifications}
     */
    private Specifications<BodyDatum> createSpecification(BodyDatumCriteria criteria) {
        Specifications<BodyDatum> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), BodyDatum_.id));
            }
            if (criteria.getHeadache() != null) {
                specification = specification.and(buildSpecification(criteria.getHeadache(), BodyDatum_.headache));
            }
            if (criteria.getDigestive() != null) {
                specification = specification.and(buildSpecification(criteria.getDigestive(), BodyDatum_.digestive));
            }
            if (criteria.getTimestamp() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTimestamp(), BodyDatum_.timestamp));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getUserId(), BodyDatum_.user, User_.id));
            }
        }
        return specification;
    }

}
