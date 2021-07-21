package com.tutor.tutorlab.configuration;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ResponseTest extends AbstractTest{

    @Test
    public void restResponseTest() throws Exception {
        mockMvc.perform(get("/tests/rest"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.result").exists())
                .andExpect(jsonPath("$.responseTime").exists());
    }

    @Test
    public void errorResponseTest() throws Exception {
        mockMvc.perform(get("/tests/error"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.errorDetails").exists())
                .andExpect(jsonPath("$.responseTime").exists());
    }

}
