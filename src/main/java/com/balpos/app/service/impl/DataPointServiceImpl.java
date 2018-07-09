package com.balpos.app.service.impl;

import com.balpos.app.domain.DataPoint;
import com.balpos.app.repository.DataPointRepository;
import com.balpos.app.service.DataPointService;
import com.balpos.app.service.dto.DataPointDTO;
import com.balpos.app.service.mapper.DataPointMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Service Implementation for managing DataPoint.
 */
@Service
@Transactional
@Slf4j
public class DataPointServiceImpl implements DataPointService {

    private final DataPointRepository dataPointRepository;

    private final DataPointMapper dataPointMapper;

    public DataPointServiceImpl(DataPointRepository dataPointRepository, DataPointMapper dataPointMapper) {
        this.dataPointRepository = dataPointRepository;
        this.dataPointMapper = dataPointMapper;
    }

    /**
     * Save a dataPoint.
     *
     * @param dataPointDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public DataPointDTO save(DataPointDTO dataPointDTO) {
        log.debug("Request to save DataPoint : {}", dataPointDTO);
        DataPoint dataPoint = dataPointMapper.toEntity(dataPointDTO);
        dataPoint = dataPointRepository.save(dataPoint);
        return dataPointMapper.toDto(dataPoint);
    }

    /**
     * Get all the dataPoints.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DataPointDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DataPoints");
        return dataPointRepository.findAll(pageable)
            .map(dataPointMapper::toDto);
    }

    /**
     * Get one dataPoint by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public DataPointDTO findOne(Long id) {
        log.debug("Request to get DataPoint : {}", id);
        DataPoint dataPoint = dataPointRepository.findOne(id);
        return dataPointMapper.toDto(dataPoint);
    }

    /**
     * Delete the  dataPoint by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete DataPoint : {}", id);
        dataPointRepository.delete(id);
    }


    @PostConstruct
    @Profile("dev")
    public void postConstruct() throws IOException {

        if (dataPointRepository.count() == 0) {

            File file = new File("data_points.csv");

            if (!file.exists()) {
                URL resource = this.getClass().getResource("/csv/data_points.csv");
                file = new File(resource.getFile());
            }

            BufferedReader reader = new BufferedReader(
                new FileReader(file)
            );

            String line = reader.readLine();

            List<DataPoint> dataPoints = new ArrayList<>();

            while (line != null) {

                String[] values = line.split(",");
                DataPoint dp = new DataPoint();
                dp.setName(values[1]);
                dp.setOrder(Integer.valueOf(values[2]));
                dp.setType(values[3]);
                dataPoints.add(dp);

                line = reader.readLine();

            }

            if (!dataPoints.isEmpty()) {
                dataPointRepository.save(dataPoints);
            }

        }


    }
}
