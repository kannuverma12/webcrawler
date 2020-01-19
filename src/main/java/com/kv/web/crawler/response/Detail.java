package com.kv.web.crawler.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Detail {
    private String pageTitle;
    private String pageLink;
    private int imageCount;

    @Override public String toString() {
        return "Detail{" +
                "pageTitle='" + pageTitle + '\'' +
                ", pageLink='" + pageLink + '\'' +
                ", imageCount=" + imageCount +
                '}';
    }
}
