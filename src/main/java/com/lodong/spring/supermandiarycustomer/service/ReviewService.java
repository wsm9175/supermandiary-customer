package com.lodong.spring.supermandiarycustomer.service;

import com.lodong.spring.supermandiarycustomer.domain.constructor.Constructor;
import com.lodong.spring.supermandiarycustomer.domain.file.FileList;
import com.lodong.spring.supermandiarycustomer.domain.file.ReviewImageFile;
import com.lodong.spring.supermandiarycustomer.domain.review.Review;
import com.lodong.spring.supermandiarycustomer.domain.usercustomer.UserCustomer;
import com.lodong.spring.supermandiarycustomer.domain.working.Working;
import com.lodong.spring.supermandiarycustomer.dto.constructor.MyReviewDTO;
import com.lodong.spring.supermandiarycustomer.dto.review.MyWorkDTO;
import com.lodong.spring.supermandiarycustomer.dto.review.WriteReviewDTO;
import com.lodong.spring.supermandiarycustomer.repository.FileRepository;
import com.lodong.spring.supermandiarycustomer.repository.ReviewRepository;
import com.lodong.spring.supermandiarycustomer.repository.UserCustomerRepository;
import com.lodong.spring.supermandiarycustomer.repository.WorkingRepository;
import com.lodong.spring.supermandiarycustomer.util.DateUtil;
import edu.emory.mathcs.backport.java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class ReviewService {
    private final WorkingRepository workingRepository;

    private final ReviewRepository reviewRepository;
    private final UserCustomerRepository userCustomerRepository;

    private final String STORAGE_ROOT_PATH = "/home/lodong/TestStorage/";
    private final String File_PATH = "review-file/";

    private final FileRepository fileRepository;

    @Transactional(readOnly = true)
    public List<MyWorkDTO> myWorkDTOList(String customerID) {
        // 나의 작업
        List<Working> workings = workingRepository.findByUserCustomer_Id(customerID).orElse(new ArrayList<>());
        List<MyWorkDTO> myWorkDTOList = new ArrayList<>();

        for (Working working : workings) {
            MyWorkDTO myWorkDTO = null;
            if (working.getApartment() != null) {
                myWorkDTO = new MyWorkDTO(working.getId(), working.getConstructor().getId(), working.isCompletePay(), working.getConstructor().getName(), working.getApartment().getName(), working.getApartmentDong(), working.getApartmentHosu(), working.getApartmentType(), working.getConstructorProduct().getName(), working.getNowWorkInfo().getWorkDetail().getName());
            } else if (working.getOtherHome() != null) {
                myWorkDTO = new MyWorkDTO(working.getId(), working.getConstructor().getId(), working.isCompletePay(), working.getConstructor().getName(), working.getOtherHome().getName(), working.getOtherHomeDong(), working.getOtherHomeHosu(), working.getOtherHomeType(), working.getConstructorProduct().getName(), working.getNowWorkInfo().getWorkDetail().getName());
            }
            myWorkDTOList.add(myWorkDTO);
        }
        return myWorkDTOList;
    }

    @Transactional(readOnly = true)
    public List<MyReviewDTO> myReviewDTOList(String customerId) {
        //나의 리뷰
        List<Review> reviews = reviewRepository.findByCustomer_Id(customerId).orElse(new ArrayList<>());
        List<MyReviewDTO> myReviewDTOList = new ArrayList<>();

        for (Review review : reviews) {
            List<String> reviewImageFileNameList = new ArrayList<>();
            for (ReviewImageFile reviewImageFile : review.getReviewImageFileList()) {
                reviewImageFileNameList.add(reviewImageFile.getFileList().getName());
            }
            myReviewDTOList.add(new MyReviewDTO(review.getId(), reviewImageFileNameList, review.getCustomer().getId(), review.getCustomerName(), review.isSatisfaction(), review.getContents(), review.getCreateAt(), Optional.ofNullable(review.getReviewLikeList()).orElseGet(Collections::emptySet).size(), review.getWorking().getConstructorProduct().getName()));
        }

        return myReviewDTOList;
    }

    @Transactional
    public void writeReview(List<MultipartFile> images, WriteReviewDTO writeReviewDTO, String customerId) throws IOException {
        List<FileList> fileLists = new ArrayList<>();
        int count = 0;
        String reviewId = UUID.randomUUID().toString();

        // 파일 추가
        Working working = workingRepository.findById(writeReviewDTO.getWorkId()).orElseThrow();
        Constructor constructor = Constructor.builder()
                .id(writeReviewDTO.getConstructorId())
                .build();
        UserCustomer customer = userCustomerRepository.findById(customerId).orElseThrow();

        Review review = Review.builder()
                .id(reviewId)
                .constructor(constructor)
                .working(working)
                .customer(customer)
                .customerName(customer.getName())
                .constructorDate(working.getCompleteConstructDate().toLocalDate())
                .contents(writeReviewDTO.getContent())
                .isSatisfaction(writeReviewDTO.isSatisfaction())
                .createAt(LocalDate.now())
                .build();
        reviewRepository.save(review);

        if (images != null) {
            for (MultipartFile image : images) {
                String fileName = reviewId + count++ + image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf("."));
                String storage = STORAGE_ROOT_PATH + File_PATH + fileName;
                FileList fileList = new FileList();
                fileList.setId(UUID.randomUUID().toString());
                fileList.setName(fileName);
                fileList.setExtension(image.getContentType());
                fileList.setStorage(storage);
                fileList.setCreateAt(DateUtil.getNowDateTime().toString());
                fileLists.add(fileList);
                saveFile(image, storage);

                ReviewImageFile reviewImageFile = ReviewImageFile.builder()
                        .id(UUID.randomUUID().toString())
                        .review(review)
                        .fileList(fileList)
                        .build();

                reviewImageFile.setReview(review);

                fileList.setReviewImageFile(reviewImageFile);
                fileLists.add(fileList);
            }
            fileRepository.saveAll(fileLists);
        }
    }
    private void saveFile(MultipartFile file, String storage) throws IOException {
        File saveFile = new File(storage);
        file.transferTo(saveFile);
    }
}
