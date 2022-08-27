package com.erdemakkuzu.accountservice.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDetails {
    @JsonProperty("date")
    private Date date;

    @JsonProperty("error_code")
    private String errorCode;

    @JsonProperty("details")
    private String details;

    @JsonProperty("field")
    private String field;

    @JsonProperty("value")
    private String value;
}
