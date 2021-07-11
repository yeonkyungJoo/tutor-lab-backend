package com.tutor.tutorlab.configuration;

import com.tutor.tutorlab.TutorlabApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class})
@SpringBootTest(classes = TutorlabApplication.class)
public class ResponseTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void errorResponseTest() throws Exception {
        mockMvc.perform(get("/tests/rest"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void restResponseTest() throws Exception {
        mockMvc.perform(get("/tests/error"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}
