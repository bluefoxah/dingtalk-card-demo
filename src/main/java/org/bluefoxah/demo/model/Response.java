package org.bluefoxah.demo.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Response {
    private Boolean success;
    private String message;
}
