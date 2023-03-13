package org.example.spring.oauth.record;

// Contains information about a YouTube video, channel, or playlist that matches the search parameters specified in an API request.
// While a search result points to a uniquely identifiable resource, like a video, it does not have its own persistent data.
record SearchResult(String kind, String etag, SearchId id, SearchSnippet snippet) {
}
