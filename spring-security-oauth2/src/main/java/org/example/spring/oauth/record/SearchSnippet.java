package org.example.spring.oauth.record;

import java.util.Map;

// Contains basic details about a search result, such as its title or description.
// For example, if the search result is a video, then the title will be the video's title and the description will be the video's description.
record SearchSnippet(String publishedAt, String channelId, String title, String description,
  Map<String, SearchThumbnail> thumbnails, String channelTitle) {

  String shortDescription() {
    if (this.description.length() <= 100) {
      return this.description;
    }
    return this.description.substring(0, 100);
  }

  SearchThumbnail thumbnail() {
    return this.thumbnails.entrySet().stream() //
      .filter(entry -> entry.getKey().equals("default")) //
      .findFirst() //
      .map(Map.Entry::getValue) //
      .orElse(null);
  }
}
