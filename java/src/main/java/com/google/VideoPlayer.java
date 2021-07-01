package com.google;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

public class VideoPlayer {

    private final VideoLibrary videoLibrary;
    private final List<VideoPlaylist> existingPlaylists;
    private Video playing;
    private boolean paused;


    public VideoPlayer() {
        this.videoLibrary = new VideoLibrary();
        this.playing = null;
        this.paused = false;
        this.existingPlaylists = new ArrayList<>();

    }

  /*
  Part 1
   */

    public void numberOfVideos() {
        System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
    }

    public void showAllVideos() {
        if (videoLibrary.getVideos().size() < 1) {
            System.out.println("No videos available");
        } else {
            System.out.println("Here's a list of all available videos:");
            List<Video> sorted = videoLibrary.getVideos().stream().sorted((vid1, vid2) -> vid1.getTitle().compareToIgnoreCase(vid2.getTitle())).collect(Collectors.toList());
            for (Video video : sorted) {
                System.out.println(video + addPause(video) + addFlag(video));
            }
        }
    }

    private String addPause(Video current) {
        if (playing == null) {
            return "";
        }
        if (current == playing) {
            if (paused)
                return " - PAUSED";
        }
        return "";
    }

    private String addFlag(Video current) {

        if (current.isFlagged())
            return " - FLAGGED" + current.getFlagReason();
        return "";
    }

    public void playVideo(String videoId) {
        Video videoInput = videoLibrary.getVideo(videoId);
        if (videoInput == null) {
            System.out.println("Cannot play video: Video does not exist");
            return;
        }
        if (videoInput.isFlagged()) {
            System.out.println("Cannot play video: Video is currently flagged" + videoInput.getFlagReason());
            return;
        }
        if (playing != null) {
            System.out.println("Stopping video: " + playing.getTitle());
        }
        playing = videoInput;
        paused = false;
        System.out.println("Playing video: " + videoInput.getTitle());
    }

    public void stopVideo() {
        if (playing == null) {
            System.out.println("Cannot stop video: No video is currently playing");
            return;
        }
        System.out.println("Stopping video: " + playing.getTitle());
        playing = null;
    }

    public void playRandomVideo() {

        if (videoLibrary.getNonFlaggedVideos().size() < 1) {
            System.out.println("No videos available");
            return;
        }
        Random random = new Random();
        int randInt = random.nextInt(videoLibrary.getNonFlaggedVideos().size());
        Video[] videoArr = videoLibrary.getNonFlaggedVideos().toArray(new Video[0]);
        Video randVideo = videoArr[randInt];
        if (playing != null) {
            System.out.println("Stopping video: " + playing.getTitle());
        }
        playing = randVideo;
        paused = false;
        System.out.println("Playing video: " + randVideo.getTitle());

    }

    public void pauseVideo() {
        if (playing == null) {
            System.out.println("Cannot pause video: No video is currently playing");
            return;
        }
        if (paused) {
            System.out.println("Video already paused: " + playing.getTitle());
        } else {
            System.out.println("Pausing video: " + playing.getTitle());
            paused = true;
        }
    }

    public void continueVideo() {
        if (playing == null) {
            System.out.println("Cannot continue video: No video is currently playing");
            return;
        }
        if (!paused) {
            System.out.println("Cannot continue video: Video is not paused");
        } else {
            System.out.println("Continuing video: " + playing.getTitle());
            paused = false;
        }
    }

    public void showPlaying() {
        if (playing == null) {
            System.out.println("No video is currently playing");
            return;
        }
        System.out.println("Currently playing: " + playing + addPause(playing));
    }

  /*
  Part 2
   */

    public void createPlaylist(String playlistName) {
        if (namePresent(playlistName)) {
            System.out.println("Cannot create playlist: A playlist with the same name already exists");
        } else {
            System.out.println("Successfully created new playlist: " + playlistName);
            existingPlaylists.add(new VideoPlaylist(playlistName));
        }
    }

    /**
     * Checks whether there is a playlist with the same name already present.
     *
     * @param playlistInput name to be checked
     * @return true if name present, false if name is not present
     */
    private boolean namePresent(String playlistInput) {
        return existingPlaylists.stream().anyMatch(playlist -> playlist.getName().equalsIgnoreCase(playlistInput));
    }

    private VideoPlaylist getPlaylist(String playlistName) {
        return existingPlaylists.stream().filter(pl -> pl.getName().equalsIgnoreCase(playlistName)).findAny().get();
    }

    public void addVideoToPlaylist(String playlistName, String videoId) {
        if (!namePresent(playlistName)) {
            System.out.println("Cannot add video to " + playlistName + ": Playlist does not exist");
            return;
        }
        if (videoLibrary.getVideo(videoId) == null) {
            System.out.println("Cannot add video to " + playlistName + ": Video does not exist");
            return;
        }
        VideoPlaylist playlist = getPlaylist(playlistName);
        Video video = videoLibrary.getVideo(videoId);
        if (video.isFlagged()) {
            System.out.println("Cannot add video to " + playlistName + ": Video is currently flagged" + video.getFlagReason());
            return;
        }
        if (playlist.addVideo(video)) {
            System.out.println("Added video to " + playlistName + ": " + video.getTitle());
        } else {
            System.out.println("Cannot add video to " + playlistName + ": Video already added");
        }
    }

    public void showAllPlaylists() {
        if (existingPlaylists.size() < 1) {
            System.out.println("No playlists exist yet");
        } else {
            System.out.println("Showing all playlists:");
            List<VideoPlaylist> sorted = existingPlaylists.stream().sorted((pl1, pl2) -> pl1.getName().compareToIgnoreCase(pl2.getName())).collect(Collectors.toList());
            for (VideoPlaylist playlist : sorted) {
                System.out.println(playlist.getName());
            }
        }
    }

    public void showPlaylist(String playlistName) {
        if (!namePresent(playlistName)) {
            System.out.println("Cannot show playlist " + playlistName + ": Playlist does not exist");
        } else {
            VideoPlaylist playlist = getPlaylist(playlistName);
            System.out.print("Showing playlist: " + playlistName + playlist);
            System.out.println();
        }

    }

    public void removeFromPlaylist(String playlistName, String videoId) {
        if (!namePresent(playlistName)) {
            System.out.println("Cannot remove video from " + playlistName + ": Playlist does not exist");
            return;
        }
        if (videoLibrary.getVideo(videoId) == null) {
            System.out.println("Cannot remove video from " + playlistName + ": Video does not exist");
            return;
        }
        VideoPlaylist playlist = getPlaylist(playlistName);
        Video video = videoLibrary.getVideo(videoId);
        if (playlist.removeVideo(video)) {
            System.out.println("Removed video from " + playlistName + ": " + video.getTitle());
        } else {
            System.out.println("Cannot remove video from " + playlistName + ": Video is not in playlist");
        }
    }

    public void clearPlaylist(String playlistName) {
        if (!namePresent(playlistName)) {
            System.out.println("Cannot clear playlist " + playlistName + ": Playlist does not exist");
            return;
        }
        VideoPlaylist playlist = getPlaylist(playlistName);
        playlist.clearAllVideos();
        System.out.println("Successfully removed all videos from " + playlistName);
    }

    public void deletePlaylist(String playlistName) {
        if (!namePresent(playlistName)) {
            System.out.println("Cannot delete playlist " + playlistName + ": Playlist does not exist");
            return;
        }
        VideoPlaylist playlist = getPlaylist(playlistName);
        existingPlaylists.remove(playlist);
        System.out.println("Deleted playlist: " + playlistName);
    }

  /*
  Part 3
   */

    public void searchVideos(String searchTerm) {
        List<Video> resultsList = videoLibrary.getNonFlaggedVideos().stream().filter(vid -> vid.getTitle().toLowerCase().contains(searchTerm.toLowerCase())).collect(Collectors.toList());
        resultsList = resultsList.stream().sorted((vid1, vid2) -> vid1.getTitle().compareToIgnoreCase(vid2.getTitle())).collect(Collectors.toList());
        Video[] resultsArr = resultsList.toArray(new Video[0]);
        if (resultsArr.length == 0) {
            System.out.println("No search results for " + searchTerm);
            return;
        }
        System.out.println("Here are the results for " + searchTerm + ":");
        for (int i = 1; i <= resultsArr.length; i++) {
            System.out.println(i + ") " + resultsArr[i - 1]);
        }
        System.out.println("Would you like to play any of the above? If yes, specify the number of the video.\n" +
                "If your answer is not a valid number, we will assume it's a no.");
        var scanner = new Scanner(System.in);
        if (scanner.hasNextInt()) {
            int numInput = scanner.nextInt();
            if (numInput > 0 && numInput <= resultsArr.length) {
                playVideo(resultsArr[numInput - 1].getVideoId());
            }
        }
    }

    public void searchVideosWithTag(String videoTag) {
        if (!videoTag.startsWith("#")) {
            System.out.println("No search results for " + videoTag);
            return;
        }
        List<Video> resultsList = videoLibrary.getNonFlaggedVideos().stream().filter(vid -> vid.tagString().contains(videoTag.toLowerCase())).collect(Collectors.toList());
        resultsList = resultsList.stream().sorted((vid1, vid2) -> vid1.getTitle().compareToIgnoreCase(vid2.getTitle())).collect(Collectors.toList());
        Video[] resultsArr = resultsList.toArray(new Video[0]);
        if (resultsArr.length == 0) {
            System.out.println("No search results for " + videoTag);
            return;
        }
        System.out.println("Here are the results for " + videoTag + ":");
        for (int i = 1; i <= resultsArr.length; i++) {
            System.out.println(i + ") " + resultsArr[i - 1]);
        }
        System.out.println("Would you like to play any of the above? If yes, specify the number of the video.\n" +
                "If your answer is not a valid number, we will assume it's a no.");
        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNextInt()) {
            int numInput = scanner.nextInt();
            if (numInput > 0 && numInput <= resultsArr.length) {
                playVideo(resultsArr[numInput - 1].getVideoId());

            }
        }
    }

    /*
    Part 4
     */

    public void flagVideo(String videoId) {
        if (videoLibrary.getVideo(videoId) == null) {
            System.out.println("Cannot flag video: Video does not exist");
            return;
        }
        Video video = videoLibrary.getVideo(videoId);
        if (video.isFlagged()) {
            System.out.println("Cannot flag video: Video is already flagged");
            return;
        }
        video.flag();
        if (playing!=null && playing.isFlagged()) {
            stopVideo();
        }
        System.out.println("Successfully flagged video: " + video.getTitle() + video.getFlagReason());
    }

    public void flagVideo(String videoId, String reason) {
        if (videoLibrary.getVideo(videoId) == null) {
            System.out.println("Cannot flag video: Video does not exist");
            return;
        }
        Video video = videoLibrary.getVideo(videoId);
        if (video.isFlagged()) {
            System.out.println("Cannot flag video: Video is already flagged");
            return;
        }
        video.flag(reason);
        if (playing!=null && playing.isFlagged()) {
            stopVideo();
        }
        System.out.println("Successfully flagged video: " + video.getTitle() + video.getFlagReason());
    }

    public void allowVideo(String videoId) {
        if (videoLibrary.getVideo(videoId) == null) {
            System.out.println("Cannot remove flag from video: Video does not exist");
            return;
        }
        Video video = videoLibrary.getVideo(videoId);
        if (!video.isFlagged()) {
            System.out.println("Cannot remove flag from video: Video is not flagged");
            return;
        }
        video.removeFlag();
        System.out.println("Successfully removed flag from video: " + video.getTitle());
    }
}

//Video is currently flagged"+flagReason