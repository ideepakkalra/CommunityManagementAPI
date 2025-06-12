package com.github.ideepakkalra.eventmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.ideepakkalra.eventmanagement.model.CommunityReferralRequest;
import com.github.ideepakkalra.eventmanagement.services.JWTService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

    @Autowired
    private JWTService jwtService;

    private static final ObjectWriter OBJECT_WRITER = new ObjectMapper().writer();
    private static String TOKEN_ADMIN = null;
    private static String TOKEN_STANDARD = null;

    @BeforeEach
    public void setUp() {
        if (TOKEN_ADMIN == null) {
            TOKEN_ADMIN = "Bearer " + jwtService.generateToken("0", "ADMIN");
            TOKEN_STANDARD = "Bearer " + jwtService.generateToken("0", "STANDARD");
        }
    }

    @Test
    public void testContextLoad() {
        Assertions.assertThat(communityReferralController).isNotNull();
    }



    @Test
    public void testGet4xxResponse() throws Exception {
        // With invalid id
        mockMvc.perform(get("/referral/-1/code")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
        // With invalid code
        mockMvc.perform(get("/referral/0/code")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
        // All okay
        mockMvc.perform(get("/referral/0/e3f2d82b-2859-4d0f-a2e7-5c8bc7fee333")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void testPost4xxResponse() throws Exception {
        // With No access
        mockMvc.perform(post("/referral")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
        // With No Content
        mockMvc.perform(post("/referral")
                        .header("Authorization", TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
        // With all fields null
        CommunityReferralRequest communityReferralRequest = new CommunityReferralRequest();
        mockMvc.perform(post("/referral")
                        .header("Authorization", TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        // Invalid Id negative value
        communityReferralRequest.setId(-1L);
        mockMvc.perform(post("/referral")
                        .header("Authorization", TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        // Invalid Id not null
        communityReferralRequest.setId(1L);
        mockMvc.perform(post("/referral")
                        .header("Authorization", TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        // Invalid version negative value
        communityReferralRequest.setVersion(-1);
        mockMvc.perform(post("/referral")
                        .header("Authorization", TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        communityReferralRequest.setVersion(0);
        // Invalid referrer null value
        communityReferralRequest.setReferrer(null);
        mockMvc.perform(post("/referral")
                        .header("Authorization", TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        // Invalid referrer nagetive value
        communityReferralRequest.setReferrer(-1L);
        mockMvc.perform(post("/referral")
                        .header("Authorization", TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        communityReferralRequest.setReferrer(0L);
        // Invalid phone number null value
        communityReferralRequest.setPhoneNumber(null);
        mockMvc.perform(post("/referral")
                        .header("Authorization", TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        // Invalid phone number character values
        communityReferralRequest.setPhoneNumber("abcdefghijkl");
        mockMvc.perform(post("/referral")
                        .header("Authorization", TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        // Invalid phone number invalid country code
        communityReferralRequest.setPhoneNumber("910000000000");
        mockMvc.perform(post("/referral")
                        .header("Authorization", TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        // Invalid phone number length incorrect
        communityReferralRequest.setPhoneNumber("+910000000000000000");
        mockMvc.perform(post("/referral")
                        .header("Authorization", TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        communityReferralRequest.setPhoneNumber("+910000");
        mockMvc.perform(post("/referral")
                        .header("Authorization", TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        communityReferralRequest.setPhoneNumber("+910000000001");
        mockMvc.perform(post("/referral")
                        .header("Authorization", TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        // Invalid state blank
        communityReferralRequest.setState("");
        mockMvc.perform(post("/referral")
                        .header("Authorization", TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        // Invalid state wrong value
        communityReferralRequest.setState("INVALID");
        mockMvc.perform(post("/referral")
                        .header("Authorization", TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        communityReferralRequest.setState("CLOSED");
        // Invalid id / version / code / state (from controller)
        mockMvc.perform(post("/referral")
                        .header("Authorization", TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        communityReferralRequest.setCode(null);
        mockMvc.perform(post("/referral")
                        .header("Authorization", TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        communityReferralRequest.setVersion(null);
        mockMvc.perform(post("/referral")
                        .header("Authorization", TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        communityReferralRequest.setId(null);
        mockMvc.perform(post("/referral")
                        .header("Authorization", TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        communityReferralRequest.setState("OPEN");
        mockMvc.perform(post("/referral")
                        .header("Authorization", TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void testPut4xxResponse() throws Exception {
        // With No access
        mockMvc.perform(put("/referral")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
        // With No Content
        mockMvc.perform(put("/referral")
                        .header("Authorization", TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
        // With all fields null
        CommunityReferralRequest communityReferralRequest = new CommunityReferralRequest();
        mockMvc.perform(put("/referral")
                        .header("Authorization", TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        // Invalid Id negative value
        communityReferralRequest.setId(-1L);
        mockMvc.perform(put("/referral")
                        .header("Authorization", TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        // Invalid Id null
        communityReferralRequest.setId(null);
        mockMvc.perform(put("/referral")
                        .header("Authorization", TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        // Invalid version negative value
        communityReferralRequest.setVersion(-1);
        mockMvc.perform(put("/referral")
                        .header("Authorization", TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        communityReferralRequest.setVersion(null);
        mockMvc.perform(put("/referral")
                        .header("Authorization", TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        // Invalid referrer null value
        communityReferralRequest.setReferrer(null);
        mockMvc.perform(put("/referral")
                        .header("Authorization", TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        // Invalid referrer nagetive value
        communityReferralRequest.setReferrer(-1L);
        mockMvc.perform(put("/referral")
                        .header("Authorization", TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        communityReferralRequest.setReferrer(0L);
        // Invalid phone number null value
        communityReferralRequest.setPhoneNumber(null);
        mockMvc.perform(put("/referral")
                        .header("Authorization", TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        // Invalid phone number character values
        communityReferralRequest.setPhoneNumber("abcdefghijkl");
        mockMvc.perform(put("/referral")
                        .header("Authorization", TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        // Invalid phone number invalid country code
        communityReferralRequest.setPhoneNumber("910000000000");
        mockMvc.perform(put("/referral")
                        .header("Authorization", TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        // Invalid phone number length incorrect
        communityReferralRequest.setPhoneNumber("+910000000000000000");
        mockMvc.perform(put("/referral")
                        .header("Authorization", TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        communityReferralRequest.setPhoneNumber("+910000");
        mockMvc.perform(put("/referral")
                        .header("Authorization", TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        communityReferralRequest.setPhoneNumber("+910000000000");
        mockMvc.perform(put("/referral")
                        .header("Authorization", TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        // Invalid state blank
        communityReferralRequest.setState("");
        mockMvc.perform(put("/referral")
                        .header("Authorization", TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        // Invalid state wrong value
        communityReferralRequest.setState("INVALID");
        mockMvc.perform(put("/referral")
                        .header("Authorization", TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        communityReferralRequest.setState("CLOSED");
        // Invalid id / version / code / state (from controller)
        mockMvc.perform(put("/referral")
                        .header("Authorization", TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        communityReferralRequest.setCode(null);
        mockMvc.perform(put("/referral")
                        .header("Authorization", TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        communityReferralRequest.setVersion(null);
        mockMvc.perform(put("/referral")
                        .header("Authorization", TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isBadRequest());
        communityReferralRequest.setId(null);
        mockMvc.perform(put("/referral")
                        .header("Authorization", TOKEN_ADMIN)
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
                        .header("Authorization", TOKEN_STANDARD)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().isForbidden());
        // Set type as ADMIN in session.
        httpSession.setAttribute("user.type", "ADMIN");
        mockMvc.perform(put("/referral")
                        .header("Authorization", TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(OBJECT_WRITER.writeValueAsBytes(communityReferralRequest)))
                .andExpect(status().is2xxSuccessful());
    }
}
