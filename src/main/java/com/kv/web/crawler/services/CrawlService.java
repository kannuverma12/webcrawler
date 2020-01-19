package com.kv.web.crawler.services;

import com.kv.web.crawler.constants.RequestStatus;
import com.kv.web.crawler.response.BulkCrawlResponse;
import com.kv.web.crawler.response.CrawlerResponseDTO;
import com.kv.web.crawler.response.SubmitResponseDTO;

public interface CrawlService {

    BulkCrawlResponse crawl();

    SubmitResponseDTO submit(String url, int depth);

    CrawlerResponseDTO fetchResults();

    RequestStatus fetchRequestStatus(Integer requestId);
}
