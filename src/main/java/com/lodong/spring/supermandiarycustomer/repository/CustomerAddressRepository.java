package com.lodong.spring.supermandiarycustomer.repository;

import com.lodong.spring.supermandiarycustomer.domain.usercustomer.CustomerAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, String> {
    public Optional<List<CustomerAddress>> findByUserCustomer_Id(String id);
}
