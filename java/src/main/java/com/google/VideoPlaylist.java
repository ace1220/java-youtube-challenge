package com.google;

import java.util.ArrayList;
import java.util.List;

/** A class used to represent a Playlist */
class VideoPlaylist {
    private String name;
    private List<Video> videosInPlaylist;

    public VideoPlaylist(String playlistName){
        this.name = playlistName;
        videosInPlaylist = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public boolean addVideo(Video videoToBeAdded){
        if (videosInPlaylist.contains(videoToBeAdded)){
            return false;
        } else {
            videosInPlaylist.add(videoToBeAdded);
            return true;
        }
    }

    public boolean removeVideo(Video videoToBeRemoved){
        if (!videosInPlaylist.contains(videoToBeRemoved)){
            return false;
        } else {
            videosInPlaylist.remove(videoToBeRemoved);
            return true;
        }
    }

    public void clearAllVideos() {
        videosInPlaylist.clear();
    }

    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        if (videosInPlaylist.isEmpty()){
            str.append("\n\tNo videos here yet");
        } else {
            for (Video video : videosInPlaylist) {
                str.append("\n\t").append(video);
                if (video.isFlagged()){
                    str.append(" - FLAGGED").append(video.getFlagReason());
                }
            }
        }
        return str.toString();
    }



}
