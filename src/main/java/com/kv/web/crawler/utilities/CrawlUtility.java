package com.kv.web.crawler.utilities;

import com.kv.web.crawler.response.CrawlerResponseDTO;
import com.kv.web.crawler.response.Detail;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class CrawlUtility {

    static int totalLinks = 0;
    static int totalImages = 0;
    static int global_depth;

    static List<String> links = new ArrayList<>();

    public static CrawlerResponseDTO crawlUtility(String url, int depth){

        CrawlerResponseDTO crawlerResponseDTO = new CrawlerResponseDTO();
        links.add(url);

        global_depth = depth;
        int i = 0;
        while(global_depth > 0 && links.size() > i) {
            crawlLinks(links.get(i), crawlerResponseDTO);
            global_depth--;
            i++;
        }

        crawlerResponseDTO.setTotalImages(totalImages);
        crawlerResponseDTO.setTotalLinks(totalLinks);


        System.out.println("crawlerResponseDTO : "+crawlerResponseDTO.toString());
        return crawlerResponseDTO;

    }


    public static void crawlLinks(String url, CrawlerResponseDTO crawlerResposeDTO) {
        try {
            System.out.println("Crawling links for url : " + url);
            Connection con = Jsoup.connect(url);
            con.userAgent("*");
            Response resp = con.execute();
            Document doc = null;
            if (resp.statusCode() == 200) {
                doc = resp.parse();
            }

            Detail detail = findLinks(url, doc, "link", crawlerResposeDTO);

            List<Detail> detailList = crawlerResposeDTO.getDetails();
            if (Objects.isNull(detailList)) {
                detailList = new ArrayList<>();
            }
            detailList.add(detail);
            crawlerResposeDTO.setDetails(detailList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Detail findLinks(String url, Document doc, String rule,
            CrawlerResponseDTO crawlerResposeDTO) {
        String currentPageTitle = doc.title();
        Detail detail = new Detail();
        detail.setPageLink(url);
        detail.setPageTitle(currentPageTitle);
        int currentPageImageCount = 0;


        Elements elems = doc.select(rule);
        for (Element ele : elems) {

            String linkVal = ele.attr("href");
            String contentType = getContentType(linkVal);
            if (Objects.nonNull(contentType)) {
                if(isImage(contentType)) {
                    totalImages++;
                    currentPageImageCount++;
                }
                if(isAnotherLink(contentType)) {
                    totalLinks++;
                    links.add(linkVal);
                }
            }
        }
        detail.setImageCount(currentPageImageCount);
        return detail;
    }

    private static String getContentType(String linkVal) {
        HttpURLConnection urlConnection;
        String urlString = linkVal;
        String contentType = null;
        try {
            urlConnection = (HttpURLConnection) new URL(urlString).openConnection();
            urlConnection.setInstanceFollowRedirects(true);
            urlConnection.setRequestMethod("HEAD");
            HttpURLConnection.setFollowRedirects(true);

            int status = urlConnection.getResponseCode();
            if (status >= 300 && status <= 307) {
                urlString = urlConnection.getHeaderField("Location");
                urlConnection = (HttpURLConnection) new URL(urlString).openConnection();
            }
            contentType = urlConnection.getContentType();
        } catch (IOException e) {
            //e.printStackTrace();
        }
        return contentType;
    }

    private static boolean isAnotherLink(String contentType) {
        if (contentType.startsWith("text/html")) {
            return true;
        }
        return false;
    }

    private static boolean isImage(String contentType) {
        if (contentType.startsWith("image/")) {
            return true;
        }
        return false;
    }
}

