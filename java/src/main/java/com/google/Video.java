package com.google;

import java.util.Collections;
import java.util.List;

/** A class used to represent a video. */
class Video {

  private final String title;
  private final String videoId;
  private final List<String> tags;
  private boolean flagged;
  private String flagReason;

  Video(String title, String videoId, List<String> tags) {
    this.title = title;
    this.videoId = videoId;
    this.tags = Collections.unmodifiableList(tags);
    this.flagged = false;
    this.flagReason = null;
  }

  /** Returns the title of the video. */
  String getTitle() {
    return title;
  }

  /** Returns the video id of the video. */
  String getVideoId() {
    return videoId;
  }

  /** Returns a readonly collection of the tags of the video. */
  List<String> getTags() {
    return tags;
  }

  /**
   * Produces lowercase string of all tags seperated with a space.
   */
  String tagString() {
    StringBuilder tagStr = new StringBuilder();
    for (String tag : tags) {
      tagStr.append(" ").append(tag.toLowerCase());
    }
    return tagStr.toString();
  }

  boolean isFlagged() {
    return flagged;
  }

  String getFlagReason() {
    if (flagReason!=null) {
      return " (reason: "+flagReason+")";
    } else {
      return " (reason: Not supplied)";
    }
  }

  void flag(){
    flagged = true;
  }

  void flag(String reason){
    flagged = true;
    flagReason = reason;
  }

  void removeFlag() {
    flagged = false;
    flagReason = null;
  }





  @Override
  public String toString() {
    StringBuilder str = new StringBuilder();
    str.append(this.getTitle()).append(" (").append(this.getVideoId()).append(") [");
    int count = 0;
    for (String tag : this.getTags()) {
      str.append(tag);
      count++;
      if (count < this.getTags().size()) {
        str.append(" ");
      }
    }
    str.append("]");
    return str.toString();
  }
}
