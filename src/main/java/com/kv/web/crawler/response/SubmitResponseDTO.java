package com.kv.web.crawler.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kv.web.crawler.constants.RequestStatus;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubmitResponseDTO {
    private String        message;
    private String        status;
    private Integer       requestId;
    private RequestStatus requestStatus;

}
