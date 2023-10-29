package com.alcadia.bovid.Exception;

import lombok.Data;

@Data
public class APIResponse {
    private Integer status;
    private Object data;
    private Object error;
}
