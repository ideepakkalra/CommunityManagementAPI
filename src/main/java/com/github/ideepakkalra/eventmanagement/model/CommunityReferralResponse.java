package com.github.ideepakkalra.eventmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommunityReferralResponse extends BaseResponse {
    private Long id;
    private String code;
    private Integer version;
    private Long referrer;
    private String phoneNumber;
    private String state;
}
