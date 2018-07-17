package com.balpos.app.repository;

import com.balpos.app.domain.User;
import com.balpos.app.domain.UserDataPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("unused")
@Repository
public interface UserDataPointRepository extends JpaRepository<UserDataPoint, Long>, JpaSpecificationExecutor<UserDataPoint> {

    @Query("SELECT udp,dp FROM DataPoint dp LEFT JOIN UserDataPoint udp ON dp.name = udp.dataPoint AND udp.user = :user")
    List<Object[]> findByUser(@Param("user") User user);
}
