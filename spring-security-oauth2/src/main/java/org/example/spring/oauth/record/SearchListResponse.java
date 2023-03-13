package org.example.spring.oauth.record;

// response body from api https://www.googleapis.com/youtube/v3/search method: list
// that returns a collection of search results that match the query parameters specified in the API request.
// By default, a search result set identifies matching video, channel, and playlist resources, but you can also configure queries to only retrieve a specific type of resource.
public record SearchListResponse(String kind, String etag, String nextPageToken, String prevPageToken, PageInfo pageInfo,
                                 SearchResult[] items) {
}
