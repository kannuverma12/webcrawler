package com.kv.web.crawler.controller;

import com.kv.web.crawler.constants.RequestStatus;
import com.kv.web.crawler.response.BulkCrawlResponse;
import com.kv.web.crawler.response.CrawlerResponseDTO;
import com.kv.web.crawler.response.SubmitResponseDTO;
import com.kv.web.crawler.services.impl.CrawlServiceImpl;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@RestController
@Validated
@RequestMapping("crawler")
public class CrawlController {

    private CrawlServiceImpl crawlService;

    @GetMapping(path = "/v1/crawl")
    public @ResponseBody BulkCrawlResponse crawl(){
        return crawlService.crawl();
    }

    @PostMapping(path = "/v1/submit")
    public @ResponseBody SubmitResponseDTO submit(
            @RequestParam(name = "url") @NotBlank String url,
            @RequestParam(name = "depth") @NotNull @Min(1) int depth){
        return crawlService.submit(url, depth);
    }

    @GetMapping(path = "/v1/fetch/result")
    public @ResponseBody CrawlerResponseDTO fetchResults(){
        return crawlService.fetchResults();
    }

    @GetMapping(path = "/v1/fetch/status")
    public @ResponseBody RequestStatus fetchRequestStatus(
            @RequestParam(name = "requestId") @NonNull @Min(1) Integer requestId) {
        return crawlService.fetchRequestStatus(requestId);
    }

}
