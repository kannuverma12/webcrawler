package com.kv.web.crawler.services.impl;

import com.kv.web.crawler.constants.RequestStatus;
import com.kv.web.crawler.request.CrawlDTO;
import com.kv.web.crawler.response.BulkCrawlResponse;
import com.kv.web.crawler.response.CrawlerResponseDTO;
import com.kv.web.crawler.response.Detail;
import com.kv.web.crawler.response.SubmitResponseDTO;
import com.kv.web.crawler.services.CrawlService;
import com.kv.web.crawler.utilities.CrawlUtility;
import com.kv.web.crawler.utilities.RequestProcessor;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CrawlServiceImpl implements CrawlService {

    private RequestProcessor requestProcessor;

    public BulkCrawlResponse crawl() {
        BulkCrawlResponse response = new BulkCrawlResponse();

        Map<Integer, CrawlDTO> unprocessedRequests = requestProcessor.getUnprocessedRequests();
        try {
            for (Map.Entry<Integer, CrawlDTO>  entry : unprocessedRequests.entrySet()) {
                Integer requestId = entry.getKey();

                //mark request as in-process
                requestProcessor.updateRequestStatus(requestId, RequestStatus.IN_PROCESS);

                CrawlDTO crawlDTO = entry.getValue();
                CrawlerResponseDTO crawlerResponseDTO = CrawlUtility.crawlUtility(crawlDTO.getUrl(),
                        crawlDTO.getDepth());

                //mark request as processed
                requestProcessor.updateRequestStatus(requestId, RequestStatus.PROCESSED);

                //persist response
                requestProcessor.persistResponse(requestId, crawlerResponseDTO);

            }
            response.setMessage("Crawl completed");
            response.setStatus("ok");
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage("Crawl failed");
            response.setStatus("failed");
        }
        return response;
    }

    @Override public SubmitResponseDTO submit(String url, int depth) {
        SubmitResponseDTO submitResponseDTO = new SubmitResponseDTO();
        int requestId = requestProcessor.createRequest(url, depth);
        submitResponseDTO.setRequestId(requestId);
        submitResponseDTO.setRequestStatus(requestProcessor.getRequestStatus(requestId));
        return submitResponseDTO;
    }

    @Override public CrawlerResponseDTO fetchResults() {
        Map<Integer, CrawlerResponseDTO> responseMap = requestProcessor.getResponseMap();

        CrawlerResponseDTO finalResponse = new CrawlerResponseDTO();
        int totalLinks = 0;
        int totalImages = 0;
        List<Detail> detailList = new ArrayList<>();

        for (Map.Entry<Integer, CrawlerResponseDTO> entry : responseMap.entrySet()) {
            CrawlerResponseDTO crawlerResponseDTO = entry.getValue();

            totalLinks += crawlerResponseDTO.getTotalLinks();
            totalImages += crawlerResponseDTO.getTotalImages();
            detailList.addAll(crawlerResponseDTO.getDetails());

        }
        finalResponse.setTotalLinks(totalLinks);
        finalResponse.setTotalImages(totalImages);
        finalResponse.setDetails(detailList);

        return finalResponse;
    }

    @Override
    public RequestStatus fetchRequestStatus(Integer requestId) {
        return requestProcessor.getRequestStatus(requestId);
    }
}
