package com.lodong.spring.supermandiarycustomer.repository;

import com.lodong.spring.supermandiarycustomer.domain.usercustomer.UserCustomer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface UserCustomerRepository extends JpaRepository<UserCustomer, String> {
    @Override
    @EntityGraph(value = "get-with-all-customer", type = EntityGraph.EntityGraphType.LOAD)
    Optional<UserCustomer> findById(String s);
    public Optional<UserCustomer> findByPhoneNumber(String phoneNumber);
    @Transactional
    @Modifying
    @Query(value = "UPDATE UserCustomer u set u.refreshToken =:refreshToken where u.phoneNumber = :phoneNumber")
    void insertRefreshToken(String refreshToken, String phoneNumber);

    public boolean existsByEmail(String email);
}
