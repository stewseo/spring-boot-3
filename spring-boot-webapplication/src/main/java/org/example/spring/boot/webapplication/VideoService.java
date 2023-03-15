package org.example.spring.boot.webapplication;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that is denoted to be picked up during component scanning and added to the application context
 * The same operation used earlier in this to quickly put together a collection of Video objects
 * A utility method to return the current collection of Video objects
 */
@Service
public class VideoService {

  private List<Video> videos = List.of( //
    new Video("Need HELP with your SPRING BOOT 3 App?"), //
    new Video("Don't do THIS to your own CODE!"), //
    new Video("SECRETS to fix BROKEN CODE!"));

  public List<Video> getVideos() {
    return videos;
  }

  // Creates a new ArrayList, a mutable collection, using its List-based constructor. This new collection initializes with the proper size and then copies every entry into the new ArrayList.
  // Adds our new Video object onto the end of this ArrayList.
  // Copies our existing list's elements into a new immutable list using the Java 17 copyOf() operator.
  // Returns the new Video object.
  public Video create(Video newVideo) {
    List<Video> extend = new ArrayList<>(videos);
    extend.add(newVideo);
    this.videos = List.copyOf(extend);
    return newVideo;
  }
}
