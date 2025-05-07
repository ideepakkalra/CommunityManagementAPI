package com.github.ideepakkalra.eventmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.ideepakkalra.eventmanagement.model.UserRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserController userController;

    private static final ObjectWriter OBJECT_WRITER = new ObjectMapper().writer();

    @Test
    public void testContextLoad() {
        Assertions.assertThat(userController).isNotNull();
    }

    @Test
    public void testPost_403Response() throws Exception {
        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testPost_400Response() throws Exception {
        mockMvc.perform(post("/user")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
        UserRequest userRequest = new UserRequest();
        mockMvc.perform(post("/user")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        // Invalid phone number
        userRequest.setPhoneNumber("+1000000000000");
        mockMvc.perform(post("/user")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        userRequest.setPhoneNumber("+11111111111");
        // Invalid referred by
        mockMvc.perform(post("/user")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        userRequest.setReferredBy(0);
        // Invalid updated by
        mockMvc.perform(post("/user")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        //userRequest.setUpdatedBy(0);
    }
}
