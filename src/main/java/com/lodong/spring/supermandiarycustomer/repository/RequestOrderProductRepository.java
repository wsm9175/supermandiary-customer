package com.lodong.spring.supermandiarycustomer.repository;

import com.lodong.spring.supermandiarycustomer.domain.request_order.RequestOrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestOrderProductRepository extends JpaRepository<RequestOrderProduct, String> {

}
