package com.cjg.sonata.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GalleryDTO {

    private Long galleryId;
    private Long mediaId;

    private String type;
    private String status;

    private LocalDateTime regDate;
    private LocalDateTime completeDate;

    private String thumbnailFilePath;
    private String thumbnailFileName;

    private String encodingFilePath;
    private String encodingFileName;

}
