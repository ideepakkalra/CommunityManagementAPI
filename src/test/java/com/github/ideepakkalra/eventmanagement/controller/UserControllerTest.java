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
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Sql ("/UserControllerTest.sql")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MockHttpSession httpSession;

    @Autowired
    private UserController userController;

    private static final ObjectWriter OBJECT_WRITER = new ObjectMapper().writer();

    @Test
    public void testContextLoad() {
        Assertions.assertThat(userController).isNotNull();
    }

    @Test
    public void testPost4xxResponse() throws Exception {
        // 403
        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden());
        mockMvc.perform(post("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
        UserRequest userRequest = new UserRequest();
        userRequest.setId(1L);
        mockMvc.perform(post("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        userRequest.setId(null);
        // Invalid phone number
        userRequest.setPhoneNumber("+1000000000000");
        mockMvc.perform(post("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        userRequest.setPhoneNumber("+11111111111");
        // Invalid email blank email
        userRequest.setEmail("");
        mockMvc.perform(post("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        // Invalid email incorrect email
        userRequest.setEmail("test.user[at]domain[dot]com");
        mockMvc.perform(post("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        userRequest.setEmail("testPost_400Response@unittest.com");
        // Invalid first name only numbers
        userRequest.setFirstName("123");
        mockMvc.perform(post("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        // Invalid first name text and numbers
        userRequest.setFirstName("ABC123");
        mockMvc.perform(post("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        // Invalid first name length more than 100
        userRequest.setFirstName("A".repeat(200));
        mockMvc.perform(post("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        userRequest.setFirstName("TestUserFirstName");
        // Invalid last name only numbers
        userRequest.setLastName("123");
        mockMvc.perform(post("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        // Invalid last name text and numbers
        userRequest.setLastName("ABC123");
        mockMvc.perform(post("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        // Invalid first name length more than 100
        userRequest.setLastName("A".repeat(200));
        mockMvc.perform(post("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        userRequest.setLastName("TestUserLastName");
        // Invalid first name length more than 100
        userRequest.setDescription("D".repeat(500));
        mockMvc.perform(post("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        userRequest.setDescription("D".repeat(200));
        // Invalid gender blank
        userRequest.setGender("");
        mockMvc.perform(post("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        // Invalid gender wrong value
        userRequest.setGender("UNKNOWN");
        mockMvc.perform(post("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        userRequest.setGender("MALE");
        // Invalid date of birth current time plus 1 day
        userRequest.setDateOfBirth(new Date(System.currentTimeMillis() + 86400000));
        mockMvc.perform(post("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        userRequest.setDateOfBirth(new Date(System.currentTimeMillis()));
        // Invalid referred by
        userRequest.setReferredBy(-1);
        mockMvc.perform(post("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        userRequest.setReferredBy(0);
        // Invalid updated by
        userRequest.setUpdatedBy(0);
        mockMvc.perform(post("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        userRequest.setUpdatedBy(null);
        // Invalid updated on
        userRequest.setUpdatedOn(new Date());
        mockMvc.perform(post("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        userRequest.setUpdatedOn(null);
        // Invalid status blank string
        userRequest.setStatus("");
        mockMvc.perform(post("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        // Invalid status wrong string
        userRequest.setStatus("UNKNOWN");
        mockMvc.perform(post("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        userRequest.setStatus("SUBMITTED");
        // Invalid type blank string
        userRequest.setType("");
        mockMvc.perform(post("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        // Invalid type wrong string
        userRequest.setType("UNKNOWN");
        mockMvc.perform(post("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        userRequest.setType("STANDARD");
        // Invalid passcode blank
        userRequest.setPasscode("");
        mockMvc.perform(post("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        // Invalid passcode non numeric
        userRequest.setPasscode("ABC");
        mockMvc.perform(post("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        // Invalid passcode length
        userRequest.setPasscode("1234");
        mockMvc.perform(post("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        userRequest.setPasscode("12345678");
        mockMvc.perform(post("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        userRequest.setPasscode("123456");
        // Invalid referral id / code
        mockMvc.perform(post("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        userRequest.setReferralId(-1L);
        mockMvc.perform(post("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        userRequest.setReferralId(1L);
        mockMvc.perform(post("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        userRequest.setReferralCode("InvalidCode");
        mockMvc.perform(post("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        userRequest.setReferralCode("e3f2d82b-2859-4d0f-a2e7-5c8bc7fee334");
        //Invalid phone number in referral
        mockMvc.perform(post("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isInternalServerError());
        userRequest.setPhoneNumber("+10000000001");
        mockMvc.perform(post("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void testPut4xxResponse() throws Exception {
        // 403
        mockMvc.perform(put("/user")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden());
        httpSession.setAttribute("user.id", 1L);
        httpSession.setAttribute("user.type", "STANDARD");
        // Invalid requests
        mockMvc.perform(put("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
        UserRequest userRequest = new UserRequest();
        userRequest.setId(1L);
        mockMvc.perform(put("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        // Invalid phone number
        userRequest.setPhoneNumber("+1000000000000");
        mockMvc.perform(put("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        userRequest.setPhoneNumber("+10000000001");
        // Invalid email blank email
        userRequest.setEmail("");
        mockMvc.perform(put("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        // Invalid email incorrect email
        userRequest.setEmail("test.user[at]domain[dot]com");
        mockMvc.perform(put("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        userRequest.setEmail("testput_400Response@unittest.com");
        // Invalid first name only numbers
        userRequest.setFirstName("123");
        mockMvc.perform(put("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        // Invalid first name text and numbers
        userRequest.setFirstName("ABC123");
        mockMvc.perform(put("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        // Invalid first name length more than 100
        userRequest.setFirstName("A".repeat(200));
        mockMvc.perform(put("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        userRequest.setFirstName("TestUserFirstName");
        // Invalid last name only numbers
        userRequest.setLastName("123");
        mockMvc.perform(put("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        // Invalid last name text and numbers
        userRequest.setLastName("ABC123");
        mockMvc.perform(put("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        // Invalid first name length more than 100
        userRequest.setLastName("A".repeat(200));
        mockMvc.perform(put("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        userRequest.setLastName("TestUserLastName");
        // Invalid first name length more than 100
        userRequest.setDescription("D".repeat(500));
        mockMvc.perform(put("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        userRequest.setDescription("D".repeat(200));
        // Invalid gender blank
        userRequest.setGender("");
        mockMvc.perform(put("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        // Invalid gender wrong value
        userRequest.setGender("UNKNOWN");
        mockMvc.perform(put("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        userRequest.setGender("MALE");
        // Invalid date of birth current time plus 1 day
        userRequest.setDateOfBirth(new Date(System.currentTimeMillis() + 86400000));
        mockMvc.perform(put("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        userRequest.setDateOfBirth(new Date(System.currentTimeMillis()));
        // Invalid referred by
        userRequest.setReferredBy(-1);
        mockMvc.perform(put("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        userRequest.setReferredBy(0);
        // Invalid updated by
        userRequest.setUpdatedBy(0);
        mockMvc.perform(put("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        userRequest.setUpdatedBy(null);
        // Invalid updated on
        userRequest.setUpdatedOn(new Date());
        mockMvc.perform(put("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        userRequest.setUpdatedOn(null);
        // Invalid status blank string
        userRequest.setStatus("");
        mockMvc.perform(put("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        // Invalid status wrong string
        userRequest.setStatus("UNKNOWN");
        mockMvc.perform(put("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        userRequest.setStatus("SUBMITTED");
        // Invalid type blank string
        userRequest.setType("");
        mockMvc.perform(put("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        // Invalid type wrong string
        userRequest.setType("UNKNOWN");
        mockMvc.perform(put("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        userRequest.setType("STANDARD");
        // Invalid passcode blank
        userRequest.setPasscode("");
        mockMvc.perform(put("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        // Invalid passcode non numeric
        userRequest.setPasscode("ABC");
        mockMvc.perform(put("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        // Invalid passcode length
        userRequest.setPasscode("1234");
        mockMvc.perform(put("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        userRequest.setPasscode("12345678");
        mockMvc.perform(put("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        userRequest.setPasscode("123456");
        mockMvc.perform(put("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().is2xxSuccessful());
        // Admin scenarios
        httpSession.setAttribute("user.id", 0L);
        httpSession.setAttribute("user.type", "ADMIN");
        // Cannot update own record
        userRequest.setId(0L);
        userRequest.setLastName("UpdatedLastName");
        mockMvc.perform(put("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().isBadRequest());
        userRequest.setId(1L);
        mockMvc.perform(put("/user")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(userRequest)))
                .andExpect(status().is2xxSuccessful());
    }
}
