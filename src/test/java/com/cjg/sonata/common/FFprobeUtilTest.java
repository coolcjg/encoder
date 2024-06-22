package com.cjg.sonata.common;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FFprobeUtilTest {

    @InjectMocks
    FFprobeUtil ffprobeUtil;

    @Test
    @DisplayName("getType")
    public void getType(){
        ffprobeUtil.setFfmpegPath(TestPropertyUtil.ffmpegPath);
        ffprobeUtil.setFfprobePath(TestPropertyUtil.ffprobePath);
        String result = ffprobeUtil.getType("D:/NAS/uploadTest/video.mp4");
        Assertions.assertThat(result).isEqualTo("video");

        String result2 = ffprobeUtil.getType("D:/NAS/uploadTest/audio.mp3");
        Assertions.assertThat(result2).isEqualTo("audio");

        String result3 = ffprobeUtil.getType("D:/NAS/uploadTest/nothing.mp4");
        Assertions.assertThat(result3).isEqualTo("undefined");
    }
}
