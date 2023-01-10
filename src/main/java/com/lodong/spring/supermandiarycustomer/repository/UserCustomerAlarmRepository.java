package com.lodong.spring.supermandiarycustomer.repository;

import com.lodong.spring.supermandiarycustomer.domain.usercustomer.UserCustomerAlarm;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserCustomerAlarmRepository extends JpaRepository<UserCustomerAlarm, String> {
    public Optional<List<UserCustomerAlarm>> findByUserCustomer_IdAndRead(String id, boolean isRead);

    @Transactional
    @Modifying
    @Query(value = "UPDATE UserCustomerAlarm u set u.isRead =:status where u.id =:id")
    public void updateReadAlarm(boolean isRead, String id);
}
