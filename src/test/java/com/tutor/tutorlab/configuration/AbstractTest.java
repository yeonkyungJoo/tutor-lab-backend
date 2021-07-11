package com.tutor.tutorlab.configuration;

import com.tutor.tutorlab.TutorlabApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@ExtendWith({SpringExtension.class})
@SpringBootTest(classes = TutorlabApplication.class)
public abstract class AbstractTest {

    @Autowired
    private FilterChainProxy springSecurityFilterChain;
    protected MockMvc mockMvc;

    @BeforeEach
    private void setUp(WebApplicationContext webAppContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true), springSecurityFilterChain)
                .build();
    }

}
