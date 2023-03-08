package org.example.spring.oauth.record;

// Contains information that can be used to uniquely identify the resource that matches the search request.
record SearchId(String kind, String videoId, String channelId, String playlistId) {
}
