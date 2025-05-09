package com.github.ideepakkalra.eventmanagement.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class BaseResponse {
    public enum Status {
        SUCCESS, FAILURE
    }
    private Status status;
    private String message;
}
