package com.kv.web.crawler.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CrawlerResponseDTO {
    private int       totalLinks;
    private int       totalImages;
    private List<Detail> details;

    @Override public String toString() {
        return "CrawlerResponseDTO{" +
                "totalLinks=" + totalLinks +
                ", totalImages=" + totalImages +
                ", details=" + details +
                '}';
    }

}
