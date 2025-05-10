package com.github.ideepakkalra.eventmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.ideepakkalra.eventmanagement.model.CommunityReferralRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Sql("/CommunityReferralControllerTest.sql")
public class CommunityReferralControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CommunityReferralController communityReferralController;

    @Autowired
    private MockHttpSession httpSession;

    private static final ObjectWriter OBJECT_WRITER = new ObjectMapper().writer();

    @BeforeEach
    public void setUp() {
        httpSession.setAttribute("user.id", 0L);
    }

    @Test
    public void testContextLoad() {
        Assertions.assertThat(communityReferralController).isNotNull();
    }

    @Test
    public void testPost403Response() throws Exception {
        // With No Content
        mockMvc.perform(post("/referral")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testPost400Response() throws Exception {
        // With No Content
        mockMvc.perform(post("/referral")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
        // With all fields null
        CommunityReferralRequest communityReferralRequest = new CommunityReferralRequest();
        mockMvc.perform(post("/referral")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        // Invalid Id negative value
        communityReferralRequest.setId(-1L);
        mockMvc.perform(post("/referral")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        // Invalid Id not null
        communityReferralRequest.setId(1L);
        mockMvc.perform(post("/referral")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        // Invalid version negative value
        communityReferralRequest.setVersion(-1);
        mockMvc.perform(post("/referral")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        communityReferralRequest.setVersion(0);
        // Invalid referrer null value
        communityReferralRequest.setReferrer(null);
        mockMvc.perform(post("/referral")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        // Invalid referrer nagetive value
        communityReferralRequest.setReferrer(-1L);
        mockMvc.perform(post("/referral")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        communityReferralRequest.setReferrer(0L);
        // Invalid phone number null value
        communityReferralRequest.setPhoneNumber(null);
        mockMvc.perform(post("/referral")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        // Invalid phone number character values
        communityReferralRequest.setPhoneNumber("abcdefghijkl");
        mockMvc.perform(post("/referral")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        // Invalid phone number invalid country code
        communityReferralRequest.setPhoneNumber("910000000000");
        mockMvc.perform(post("/referral")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        // Invalid phone number length incorrect
        communityReferralRequest.setPhoneNumber("+910000000000000000");
        mockMvc.perform(post("/referral")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        communityReferralRequest.setPhoneNumber("+910000");
        mockMvc.perform(post("/referral")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        communityReferralRequest.setPhoneNumber("+910000000001");
        mockMvc.perform(post("/referral")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        // Invalid state blank
        communityReferralRequest.setState("");
        mockMvc.perform(post("/referral")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        // Invalid state wrong value
        communityReferralRequest.setState("INVALID");
        mockMvc.perform(post("/referral")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        communityReferralRequest.setState("CLOSED");
        // Invalid id / version / code / state (from controller)
        mockMvc.perform(post("/referral")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        communityReferralRequest.setCode(null);
        mockMvc.perform(post("/referral")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        communityReferralRequest.setVersion(null);
        mockMvc.perform(post("/referral")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        communityReferralRequest.setId(null);
        mockMvc.perform(post("/referral")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        communityReferralRequest.setState("OPEN");
        mockMvc.perform(post("/referral")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void testPut400Response() throws Exception {
        // With No Content
        mockMvc.perform(put("/referral")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
        // With all fields null
        CommunityReferralRequest communityReferralRequest = new CommunityReferralRequest();
        mockMvc.perform(put("/referral")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        // Invalid Id negative value
        communityReferralRequest.setId(-1L);
        mockMvc.perform(put("/referral")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        // Invalid Id null
        communityReferralRequest.setId(null);
        mockMvc.perform(put("/referral")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        // Invalid version negative value
        communityReferralRequest.setVersion(-1);
        mockMvc.perform(put("/referral")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        communityReferralRequest.setVersion(null);
        mockMvc.perform(put("/referral")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        // Invalid referrer null value
        communityReferralRequest.setReferrer(null);
        mockMvc.perform(put("/referral")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        // Invalid referrer nagetive value
        communityReferralRequest.setReferrer(-1L);
        mockMvc.perform(put("/referral")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        communityReferralRequest.setReferrer(0L);
        // Invalid phone number null value
        communityReferralRequest.setPhoneNumber(null);
        mockMvc.perform(put("/referral")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        // Invalid phone number character values
        communityReferralRequest.setPhoneNumber("abcdefghijkl");
        mockMvc.perform(put("/referral")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        // Invalid phone number invalid country code
        communityReferralRequest.setPhoneNumber("910000000000");
        mockMvc.perform(put("/referral")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        // Invalid phone number length incorrect
        communityReferralRequest.setPhoneNumber("+910000000000000000");
        mockMvc.perform(put("/referral")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        communityReferralRequest.setPhoneNumber("+910000");
        mockMvc.perform(put("/referral")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        communityReferralRequest.setPhoneNumber("+910000000000");
        mockMvc.perform(put("/referral")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        // Invalid state blank
        communityReferralRequest.setState("");
        mockMvc.perform(put("/referral")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        // Invalid state wrong value
        communityReferralRequest.setState("INVALID");
        mockMvc.perform(put("/referral")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        communityReferralRequest.setState("CLOSED");
        // Invalid id / version / code / state (from controller)
        mockMvc.perform(put("/referral")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        communityReferralRequest.setCode(null);
        mockMvc.perform(put("/referral")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        communityReferralRequest.setVersion(null);
        mockMvc.perform(put("/referral")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        communityReferralRequest.setId(null);
        mockMvc.perform(put("/referral")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        // Now lets set correct id / version / code / state
        // Should get bad request because of insufficient privilege.
        communityReferralRequest.setId(0L);
        communityReferralRequest.setVersion(0);
        communityReferralRequest.setCode("e3f2d82b-2859-4d0f-a2e7-5c8bc7fee333");
        communityReferralRequest.setState("CLOSED");
        mockMvc.perform(put("/referral")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
        // Set type as ADMIN in session.
        httpSession.setAttribute("user.type", "ADMIN");
        mockMvc.perform(put("/referral")
                        .with(csrf())
                        .session(httpSession)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().is2xxSuccessful());
    }
}
