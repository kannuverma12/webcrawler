package com.kv.web.crawler.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrawlDTO {
    private String url;
    private int    depth;
}
