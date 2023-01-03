package com.lodong.spring.supermandiarycustomer.domain.file;

import com.lodong.spring.supermandiarycustomer.domain.working.WorkDetail;
import jakarta.persistence.*;
import lombok.*;


@Entity
@ToString
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkFile {
    @Id
    private String id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name =  "file_id")
    private FileList fileList;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name =  "work_detail_id")
    private WorkDetail workDetail;

}
