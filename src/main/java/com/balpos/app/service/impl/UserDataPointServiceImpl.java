package com.balpos.app.service.impl;

import com.balpos.app.domain.Color;
import com.balpos.app.domain.DataPoint;
import com.balpos.app.domain.User;
import com.balpos.app.domain.UserDataPoint;
import com.balpos.app.repository.UserDataPointRepository;
import com.balpos.app.service.UserDataPointService;
import com.balpos.app.service.dto.UserDataPointDTO;
import com.balpos.app.service.mapper.UserDataPointMapper;
import com.balpos.app.stat.StatConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
public class UserDataPointServiceImpl implements UserDataPointService {

    private final UserDataPointRepository userDataPointRepository;
    private final UserDataPointMapper userDataPointMapper;

    public UserDataPointServiceImpl(UserDataPointRepository userDataPointRepository, UserDataPointMapper userDataPointMapper) {
        this.userDataPointRepository = userDataPointRepository;
        this.userDataPointMapper = userDataPointMapper;
    }

    @Override
    public UserDataPointDTO save(UserDataPointDTO userDataPointDTO, User user) {
        log.debug("Request to save UserDataPoint : {}", userDataPointDTO);
        UserDataPoint userDataPoint = userDataPointMapper.toEntity(userDataPointDTO);
        UserDataPoint dbUserDataPoint = userDataPointRepository.findByUserAndDataPoint_Name(user, userDataPointDTO.getDataPoint().getName());
        if (dbUserDataPoint != null) {
            userDataPoint.setId(dbUserDataPoint.getId());
        }
        userDataPoint.setUser(user);
        userDataPoint = userDataPointRepository.save(userDataPoint);
        return userDataPointMapper.toDto(userDataPoint);
    }

    @Override
    public List<UserDataPointDTO> findByUser(User user) {
        return userDataPointMapper.toDto(findByUserRightJoin(user));
    }

    private List<UserDataPoint> findByUserRightJoin(User user) {
        // functional equivalent of database RIGHT JOIN -- which is not supported by hibernate
        List<Object[]> relationships = userDataPointRepository.findByUser(user);
        List<UserDataPoint> resultList = new ArrayList<>();
        relationships.forEach(relationship -> {
            UserDataPoint udp = (UserDataPoint) relationship[0];
            DataPoint dp = (DataPoint) relationship[1];
            if (udp == null) {
                udp = new UserDataPoint();
                udp.setUser(user).setDataPoint(dp);
            }
            resultList.add(udp);
        });
        return resultList;
    }

    @Override
    public UserDataPointDTO findByUserAndDataPoint(User user, DataPoint dataPoint) {
        List<UserDataPoint> udpList = findByUserRightJoin(user);
        for (UserDataPoint udp : udpList) {
            if (StringUtils.equalsIgnoreCase(udp.getDataPoint().getName(), dataPoint.getName())) {
                return userDataPointMapper.toDto(udp);
            }
        }
        return null;
    }
}

