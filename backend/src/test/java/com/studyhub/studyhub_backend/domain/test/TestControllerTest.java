package com.studyhub.studyhub_backend.domain.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TestController.class)
class TestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /api/test/hello 호출 시 'Hello from StudyHub Backend!' 문자열을 반환한다")
    void givenNothing_whenRequestHelloEndpoint_thenReturnsHelloMessage() throws Exception {
        // given

        // when & then
        mockMvc.perform(get("/api/test/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello from StudyHub Backend!"));
    }
}
