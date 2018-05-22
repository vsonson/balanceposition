package com.balpos.app.service;

import com.balpos.app.domain.DataPoint;
import com.balpos.app.repository.DataPointRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Service Implementation for managing DataPoint.
 */
@Service
@Transactional
public class DataPointService {

    private final Logger log = LoggerFactory.getLogger(DataPointService.class);

    private final DataPointRepository dataPointRepository;
    public DataPointService(DataPointRepository dataPointRepository) {
        this.dataPointRepository = dataPointRepository;
    }

    /**
     * Save a dataPoint.
     *
     * @param dataPoint the entity to save
     * @return the persisted entity
     */
    public DataPoint save(DataPoint dataPoint) {
        log.debug("Request to save DataPoint : {}", dataPoint);
        return dataPointRepository.save(dataPoint);
    }

    /**
     *  Get all the dataPoints.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DataPoint> findAll(Pageable pageable) {
        log.debug("Request to get all DataPoints");
        return dataPointRepository.findAll(pageable);
    }

    /**
     *  Get one dataPoint by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public DataPoint findOne(Long id) {
        log.debug("Request to get DataPoint : {}", id);
        return dataPointRepository.findOne(id);
    }

    /**
     *  Delete the  dataPoint by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete DataPoint : {}", id);
        dataPointRepository.delete(id);
    }

    @PostConstruct
    @Profile("dev")
    public void postConstruct() throws IOException {

        if( dataPointRepository.count() == 0){

            File file = new File("data_points.csv");

            if(!file.exists()){
                URL resource = this.getClass().getResource("/csv/data_points.csv");
                file = new File(resource.getFile());
            }

            BufferedReader reader = new BufferedReader(
                new FileReader(file)
            );

            String line = reader.readLine();

            List<DataPoint> dataPoints = new ArrayList<>();

            while (line != null){

                String[] values = line.split(",");
                DataPoint dp = new DataPoint();
                dp.setName(values[1]);
                dp.setOrder(Integer.valueOf(values[2]));
                dp.setType(values[3]);
                dataPoints.add(dp);

                line = reader.readLine();

            }

            if( !dataPoints.isEmpty()){
                dataPointRepository.save(dataPoints);
            }

        }


    }
}
