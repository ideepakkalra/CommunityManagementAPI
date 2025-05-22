package com.github.ideepakkalra.eventmanagement.model;

import com.github.ideepakkalra.eventmanagement.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse extends BaseResponse {
    private Long id;
    private UserResponse userResponse;
}
