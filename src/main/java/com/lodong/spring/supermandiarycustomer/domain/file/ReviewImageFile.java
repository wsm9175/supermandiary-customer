package com.lodong.spring.supermandiarycustomer.domain.file;

import com.lodong.spring.supermandiarycustomer.domain.review.Review;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor @NoArgsConstructor
@Embeddable
public class ReviewImageFile {
    @Id
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    private FileList fileList;
}
