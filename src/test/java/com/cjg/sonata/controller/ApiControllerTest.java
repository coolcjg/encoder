package com.cjg.sonata.controller;


import com.cjg.sonata.dto.BatchDTO;
import com.cjg.sonata.service.ApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.security.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = ApiController.class
        , includeFilters={
        @ComponentScan.Filter(
                type= FilterType.ASSIGNABLE_TYPE,
                classes = SecurityConfig.class
        )
})
public class ApiControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    ApiService apiService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("batchInsert")
    public void batchInsert() throws Exception{

        BatchDTO batchDto = new BatchDTO();
        batchDto.setType("video");
        batchDto.setMediaId(1L);
        batchDto.setOriginalFile("D:/NAS/upload/original/2024/05/18/598f0930-27bb-4212-ae1a-68339c7452a1.mp4");
        batchDto.setReturnUrl("https://cjg.com");

        Map<String,Object> result = new HashMap();
        result.put("message", "success");

        given(apiService.batchInsert(any(BatchDTO.class))).willReturn(result);

        mvc.perform(post("/batch")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .content(objectMapper.writeValueAsString(batchDto))
                    )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andDo(print());
    }
}
