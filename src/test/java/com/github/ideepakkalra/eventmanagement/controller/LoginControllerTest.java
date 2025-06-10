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
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Sql("/LoginControllerTest.sql")
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MockHttpSession httpSession;

    @Autowired
    private LoginController loginController;

    private static final ObjectWriter OBJECT_WRITER = new ObjectMapper().writer();

    @Test
    public void testContextLoad() {
        Assertions.assertThat(loginController).isNotNull();
    }

    @Test
    public void testPost4xxResponse() throws Exception {
        // With No Content
        mockMvc.perform(post("/login")
                        //.with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
        // With No Content
        mockMvc.perform(post("/login")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
        LoginRequest loginRequest = new LoginRequest();
        // With All Fields Null
        mockMvc.perform(post("/login")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(loginRequest)))
                .andExpect(status().isBadRequest());
        // With PhoneNumber Blank
        loginRequest.setPhoneNumber("");
        mockMvc.perform(post("/login")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(loginRequest)))
                .andExpect(status().isBadRequest());
        // With PhoneNumber Invalid Characters
        loginRequest.setPhoneNumber("abcd");
        mockMvc.perform(post("/login")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(loginRequest)))
                .andExpect(status().isBadRequest());
        // With PhoneNumber Invalid Country Code
        loginRequest.setPhoneNumber("910000000000");
        mockMvc.perform(post("/login")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(loginRequest)))
                .andExpect(status().isBadRequest());
        // With PhoneNumber Invalid Length
        loginRequest.setPhoneNumber("+9100000000000000000");
        mockMvc.perform(post("/login")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(loginRequest)))
                .andExpect(status().isBadRequest());
        // With PhoneNumber Valid
        loginRequest.setPhoneNumber("+910000000000");
        mockMvc.perform(post("/login")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(loginRequest)))
                .andExpect(status().isBadRequest());
        // With Passcode Blank
        loginRequest.setPasscode("");
        mockMvc.perform(post("/login")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(loginRequest)))
                .andExpect(status().isBadRequest());
        // With Passcode Invalid Characters
        loginRequest.setPasscode("abcd");
        mockMvc.perform(post("/login")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(loginRequest)))
                .andExpect(status().isBadRequest());
        // With Passcode Invalid Length
        loginRequest.setPasscode("123456789");
        mockMvc.perform(post("/login")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(loginRequest)))
                .andExpect(status().isBadRequest());
        loginRequest.setPasscode("000000");
        // All Good But No Record Found
        mockMvc.perform(post("/login")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(loginRequest)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testPost2xxResponse() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPhoneNumber("+10000000000");
        loginRequest.setPasscode("000000");
        // All Good But No Record Found
        mockMvc.perform(post("/login")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(loginRequest)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void testPost5xxResponse() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPhoneNumber("+10000000001");
        loginRequest.setPasscode("111111");
        // First try
        mockMvc.perform(post("/login")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(loginRequest)))
                .andExpect(status().is4xxClientError());
        // Second try
        mockMvc.perform(post("/login")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(loginRequest)))
                .andExpect(status().is4xxClientError());
        // Third try
        mockMvc.perform(post("/login")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(loginRequest)))
                .andExpect(status().is4xxClientError());
        // Fourth try
        mockMvc.perform(post("/login")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(loginRequest)))
                .andExpect(status().is4xxClientError());
        // Fifth try
        mockMvc.perform(post("/login")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(loginRequest)))
                .andExpect(status().is4xxClientError());
        // Sixth try. Account should be locked by now.
        mockMvc.perform(post("/login")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(loginRequest)))
                .andExpect(status().is5xxServerError());
    }
}
