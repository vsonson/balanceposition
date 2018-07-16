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

    @PostConstruct
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

            String line = reader.readLine(); //discard first line
            List<DataPoint> dataPoints = new ArrayList<>();
            for (line = reader.readLine(); line != null; line = reader.readLine()) {
                String[] values = line.split(",");
                DataPoint dp = new DataPoint();
                dp.setId(Long.parseLong(values[0]));
                dp.setName(values[1]);
                dp.setType(values[2]);
                dp.setOnePerDay("true".equals(values[3]));
                dataPoints.add(dp);

            }

            if (!dataPoints.isEmpty()) {
                dataPointRepository.save(dataPoints);
            }

        }
    }
}
