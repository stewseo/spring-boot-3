package org.example.spring.oauth.service;


import org.example.spring.oauth.record.SearchListResponse;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface YouTube {

  // remotely invoke /search?part=snippet&type=video using an HTTP GET call
  // the path in the @GetExchange call is appended to the base URL configured earlier, https://www.googleapis.com/youtube/v3, forming a complete URL to access this API.
  @GetExchange("/search?part=snippet&type=video")
  SearchListResponse channelVideos( //
                                    @RequestParam String channelId, //
                                    @RequestParam int maxResults, //
                                    @RequestParam Sort order);

  enum Sort {
    DATE("date"), //
    VIEW_COUNT("viewCount"), //
    TITLE("title"), //
    RATING("rating");

    private final String type;

    Sort(String type) {
      this.type = type;
    }
  }
}
