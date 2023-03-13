package org.example.spring.oauth.record;

// A map of thumbnail images associated with the search result.
// For each object in the map, the key is the name of the thumbnail image, and the value is an object that contains other information about the thumbnail.
record SearchThumbnail(String url, Integer width, Integer height) {
}
