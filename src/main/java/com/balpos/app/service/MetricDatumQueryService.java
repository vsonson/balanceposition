package com.balpos.app.service;


import com.balpos.app.domain.DataPoint_;
import com.balpos.app.domain.MetricDatum;
import com.balpos.app.domain.MetricDatum_;
import com.balpos.app.domain.User_;
import com.balpos.app.repository.MetricDatumRepository;
import com.balpos.app.service.dto.MetricDatumCriteria;
import com.balpos.app.service.dto.MetricDatumDTO;
import com.balpos.app.service.mapper.MetricDatumMapper;
import io.github.jhipster.service.QueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for executing complex queries for MetricDatum entities in the database.
 * The main input is a {@link MetricDatumCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link MetricDatumDTO} or a {@link Page} of {%link MetricDatumDTO} which fullfills the criterias
 */
@Service
@Transactional(readOnly = true)
@Slf4j
public class MetricDatumQueryService extends QueryService<MetricDatum> {
    private final MetricDatumRepository metricDatumRepository;

    private final MetricDatumMapper metricDatumMapper;

    public MetricDatumQueryService(MetricDatumRepository metricDatumRepository, MetricDatumMapper metricDatumMapper) {
        this.metricDatumRepository = metricDatumRepository;
        this.metricDatumMapper = metricDatumMapper;
    }

    /**
     * Return a {@link List} of {%link MetricDatumDTO} which matches the criteria from the database
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MetricDatumDTO> findByCriteria(MetricDatumCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<MetricDatum> specification = createSpecification(criteria);
        return metricDatumMapper.toDto(metricDatumRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {%link MetricDatumDTO} which matches the criteria from the database
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MetricDatumDTO> findByCriteria(MetricDatumCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<MetricDatum> specification = createSpecification(criteria);
        final Page<MetricDatum> result = metricDatumRepository.findAll(specification, page);
        return result.map(metricDatumMapper::toDto);
    }

    /**
     * Function to convert MetricDatumCriteria to a {@link Specifications}
     */
    private Specifications<MetricDatum> createSpecification(MetricDatumCriteria criteria) {
        Specifications<MetricDatum> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), MetricDatum_.id));
            }
            if (criteria.getDatumValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDatumValue(), MetricDatum_.datumValue));
            }
            if (criteria.getTimestamp() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTimestamp(), MetricDatum_.timestamp));
            }
            if (criteria.getDataPointName() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getDataPointName(), MetricDatum_.dataPoint, DataPoint_.name));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getUserId(), MetricDatum_.user, User_.id));
            }
        }
        return specification;
    }

}
