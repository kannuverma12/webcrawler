package com.kv.web.crawler.utilities;

import com.kv.web.crawler.constants.RequestStatus;
import com.kv.web.crawler.request.CrawlDTO;
import com.kv.web.crawler.response.CrawlerResponseDTO;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RequestProcessor {

    private AtomicInteger requestCounter = new AtomicInteger(0);

    private Map<Integer, RequestStatus> requestStatusMap = new ConcurrentHashMap<>();
    private Map<Integer, CrawlDTO> unprocessedRequests = new ConcurrentHashMap<>();

    private Map<Integer, CrawlerResponseDTO> responseMap = new ConcurrentHashMap<>();

    public Integer createRequest(String url, int depth) {
        int requestId = requestCounter.incrementAndGet();
        requestStatusMap.put(requestId, RequestStatus.SUBMITTED);
        unprocessedRequests.put(requestId, new CrawlDTO(url, depth));
        return requestId;
    }

    public void updateRequestStatus(Integer requestId, RequestStatus requestStatus) {
        if (requestStatusMap.containsKey(requestId)) {
            requestStatusMap.put(requestId, requestStatus);
        }
    }

    public void persistResponse(Integer requestId, CrawlerResponseDTO crawlerResponseDTO) {
        responseMap.put(requestId, crawlerResponseDTO);
    }

    public RequestStatus getRequestStatus(Integer requestId){
        return requestStatusMap.get(requestId);
    }

    public  Map<Integer, CrawlDTO> getUnprocessedRequests(){
        return unprocessedRequests;
    }

    public Map<Integer, CrawlerResponseDTO> getResponseMap() {
        return responseMap;
    }


}
