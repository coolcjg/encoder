package com.cjg.sonata.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Entity
@Data
@DynamicUpdate
public class Gallery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long galleryId;

    @Column(nullable = false)
    private Long mediaId;

    @Column(nullable = false, length = 5)
    private String type;

    @CreationTimestamp
    @JsonFormat(pattern="yyyy-MM-dd hh:mm")
    @Column(nullable = false)
    private LocalDateTime regDate;

    @Column(nullable = false, length = 1)
    @ColumnDefault("'N'")
    private String status;

    @JsonFormat(pattern="yyyy-MM-dd hh:mm")
    @Column(nullable = false)
    private LocalDateTime completeDate;

    @Column(nullable = false, length = 40)
    private String thumbnailFilePath;

    @Column(nullable = false, length = 20)
    private String thumbnailFileName;

    @Column(nullable = false, length = 40)
    private String encodingFilePath;

    @Column(nullable = false, length = 20)
    private String encodingFileName;
}
