package com.lodong.spring.supermandiarycustomer.repository;

import com.lodong.spring.supermandiarycustomer.domain.request_order.RequestOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface RequestOrderRepository extends JpaRepository<RequestOrder, String> {
    @Override
    Optional<RequestOrder> findById(String s);
    public Optional<List<RequestOrder>> findByCustomer_Id(String customerId);

    public Optional<List<RequestOrder>> findByCustomer_IdAndApartment_IdAndDongAndHosu(String customerId,String apartmentId, String dong, String hosu);

    public Optional<List<RequestOrder>> findByCustomer_IdAndOtherHome_IdAndOtherHomeDongAndOtherHomeHosu(String customerId, String otherHomeId, String otherHomeDong, String otherHomeHosu);
}
