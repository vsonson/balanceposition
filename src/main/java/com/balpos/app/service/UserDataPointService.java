package com.balpos.app.service;

import com.balpos.app.domain.DataPoint;
import com.balpos.app.domain.User;
import com.balpos.app.service.dto.UserDataPointDTO;

import java.util.List;

public interface UserDataPointService {
    UserDataPointDTO save(UserDataPointDTO userDataPointDTO, User user);

    List<UserDataPointDTO> findByUser(User user);

    UserDataPointDTO findByUserAndDataPoint(User user, DataPoint dataPoint);
}
