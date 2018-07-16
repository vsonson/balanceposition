package com.balpos.app.service;


import com.balpos.app.domain.DataPoint;
import com.balpos.app.domain.DataPoint_;
import com.balpos.app.repository.DataPointRepository;
import com.balpos.app.service.dto.DataPointCriteria;
import com.balpos.app.service.dto.DataPointDTO;
import com.balpos.app.service.mapper.DataPointMapper;
import io.github.jhipster.service.QueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for executing complex queries for DataPoint entities in the database.
 * The main input is a {@link DataPointCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link DataPointDTO} or a {@link Page} of {%link DataPointDTO} which fullfills the criterias
 */
@Service
@Transactional(readOnly = true)
@Slf4j
public class DataPointQueryService extends QueryService<DataPoint> {

    private final DataPointRepository dataPointRepository;

    private final DataPointMapper dataPointMapper;

    public DataPointQueryService(DataPointRepository dataPointRepository, DataPointMapper dataPointMapper) {
        this.dataPointRepository = dataPointRepository;
        this.dataPointMapper = dataPointMapper;
    }

    /**
     * Return a {@link List} of {%link DataPointDTO} which matches the criteria from the database
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DataPointDTO> findByCriteria(DataPointCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<DataPoint> specification = createSpecification(criteria);
        return dataPointMapper.toDto(dataPointRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {%link DataPointDTO} which matches the criteria from the database
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DataPointDTO> findByCriteria(DataPointCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<DataPoint> specification = createSpecification(criteria);
        final Page<DataPoint> result = dataPointRepository.findAll(specification, page);
        return result.map(dataPointMapper::toDto);
    }

    /**
     * Function to convert DataPointCriteria to a {@link Specifications}
     */
    private Specifications<DataPoint> createSpecification(DataPointCriteria criteria) {
        Specifications<DataPoint> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), DataPoint_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), DataPoint_.name));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), DataPoint_.type));
            }
        }
        return specification;
    }

}
