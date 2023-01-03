package com.lodong.spring.supermandiarycustomer.repository;

import com.lodong.spring.supermandiarycustomer.domain.file.BusinessLicense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessLicenseRepository extends JpaRepository<BusinessLicense, String> {
}
