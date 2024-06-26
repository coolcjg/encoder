package com.cjg.sonata.common;

import java.io.IOException;

import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;

@Component
public class FFprobeUtil {

	Logger logger = LoggerFactory.getLogger(FFprobeUtil.class);

	final String[] VIDEO_CODEC = {"h264"};
	final String[] AUDIO_CODEC = {"aac", "mp3"};

	@Setter
	@Value("${ffmpegPath}")
	private String ffmpegPath;

	@Setter
	@Value("${ffprobePath}")
	private String ffprobePath;

	public String getType(String path) {

		String result = "undefined";

		try {

			FFprobe ffprobe = new FFprobe(ffprobePath);

			FFmpegProbeResult probeResult = ffprobe.probe(path);

			FFmpegStream stream = probeResult.getStreams().get(0);

			String codec = stream.codec_name;

			for(String videoCodec : VIDEO_CODEC) {
				if(videoCodec.equals(codec)) {
					result =  "video";
					break;
				}
			}

			for(String audioCodec : AUDIO_CODEC) {
				if(audioCodec.equals(codec)) {
					result =  "audio";
					break;
				}
			}

		}catch(IOException e) {
			logger.error("ERROR : ", e);
		}

		return result;

	}

}
