package com.github.ideepakkalra.eventmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.ideepakkalra.eventmanagement.model.LoginRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SessionController sessionController;

    private static final ObjectWriter OBJECT_WRITER = new ObjectMapper().writer();

    @Test
    public void testContextLoad() throws Exception {
        Assertions.assertThat(sessionController).isNotNull();
    }

    @Test
    public void testPost403Response() throws Exception {
        // With No Content
        mockMvc.perform(post("/login")
                        //.with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void testPost400Response() throws Exception {
        // With No Content
        mockMvc.perform(post("/login")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        LoginRequest loginRequest = new LoginRequest();
        // With All Fields Null
        mockMvc.perform(post("/login")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        // With PhoneNumber Blank
        loginRequest.setPhoneNumber("");
        mockMvc.perform(post("/login")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        // With PhoneNumber Invalid Characters
        loginRequest.setPhoneNumber("abcd");
        mockMvc.perform(post("/login")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        // With PhoneNumber Invalid Country Code
        loginRequest.setPhoneNumber("910000000000");
        mockMvc.perform(post("/login")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        // With PhoneNumber Invalid Length
        loginRequest.setPhoneNumber("+9100000000000000000");
        mockMvc.perform(post("/login")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        // With PhoneNumber Valid
        loginRequest.setPhoneNumber("+910000000000");
        mockMvc.perform(post("/login")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        // With Passcode Blank
        loginRequest.setPasscode("");
        mockMvc.perform(post("/login")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        // With Passcode Invalid Characters
        loginRequest.setPasscode("abcd");
        mockMvc.perform(post("/login")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        // With Passcode Invalid Length
        loginRequest.setPasscode("123456789");
        mockMvc.perform(post("/login")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        loginRequest.setPasscode("000000");
        // All Good Now
        mockMvc.perform(post("/login")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }
}
