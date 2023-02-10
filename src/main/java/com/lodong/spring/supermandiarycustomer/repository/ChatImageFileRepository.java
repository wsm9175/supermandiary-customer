package com.lodong.spring.supermandiarycustomer.repository;

import com.lodong.spring.supermandiarycustomer.domain.chat.ChatImageFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatImageFileRepository extends JpaRepository<ChatImageFile, String> {
}
