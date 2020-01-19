package com.kv.web.crawler.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BulkCrawlResponse {
    private String message;
    private String status;
}
