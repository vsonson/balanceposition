package com.balpos.app.security;

import com.balpos.app.domain.LookupValue;
import com.balpos.app.repository.FrontendViewModelLookupRepository;
import com.balpos.app.service.mapper.DataPointMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FrontendViewModelLookupService {

    private final FrontendViewModelLookupRepository frontendViewModelLookupRepository;
    private final DataPointMapper dataPointMapper;

    public FrontendViewModelLookupService(FrontendViewModelLookupRepository frontendViewModelLookupRepository, DataPointMapper dataPointMapper) {
        this.frontendViewModelLookupRepository = frontendViewModelLookupRepository;
        this.dataPointMapper = dataPointMapper;
    }

    @Cacheable("frontendViewModelLookup")
    public Optional<LookupValue> findByDatapointNameAndSubclassNameAndSourceValue(String datapointName, String subclassName, String sourceValue) {
        return frontendViewModelLookupRepository.findByDatapointNameAndSubclassNameAndSourceValue(datapointName, subclassName, sourceValue);
    }

    @PostConstruct
    public void postConstruct() throws IOException {

        if (frontendViewModelLookupRepository.count() == 0) {

            File file = new File("lookup_values.csv");

            if (!file.exists()) {
                URL resource = this.getClass().getResource("/csv/lookup_values.csv");
                file = new File(resource.getFile());
            }

            BufferedReader reader = new BufferedReader(
                new FileReader(file)
            );

            String line = reader.readLine(); //discard first line
            List<LookupValue> luvs = new ArrayList<>();
            for (line = reader.readLine(); line != null; line = reader.readLine()) {
                String[] values = line.split(",");
                LookupValue luv = new LookupValue();
                luv.setId(Long.parseLong(values[0]));
                luv.setDatapoint(dataPointMapper.fromName(values[1]));
                luv.setSubclassName(values[2]);
                luv.setSourceValue(values[3]);
                luv.setMappedValue(Integer.parseInt(values[4]));
                luvs.add(luv);
            }

            if (!luvs.isEmpty()) {
                frontendViewModelLookupRepository.save(luvs);
            }

        }
    }
}
