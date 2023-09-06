/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.hlssample;

import android.content.Context;
import android.net.Uri;
import android.view.KeyEvent;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Player.DiscontinuityReason;
import com.google.android.exoplayer2.Player.TimelineChangeReason;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.TracksInfo;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.ui.StyledPlayerControlView;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.util.MimeTypes;

import java.util.ArrayList;
import java.util.HashMap;

class PlayerManager implements Player.Listener {

    private static final String cookieValue = "CloudFront-Key-Pair-Id=KLF6VW5TBETCE;CloudFront-Policy=eyJTdGF0ZW1lbnQiOlt7IlJlc291cmNlIjoiaHR0cHM6Ly9kdGhsOXF4bWM2YzNyLmNsb3VkZnJvbnQubmV0LyoiLCJDb25kaXRpb24iOnsiRGF0ZUxlc3NUaGFuIjp7IkFXUzpFcG9jaFRpbWUiOjE2NzEyNjQyMzl9fX1dfQ__;CloudFront-Signature=GfcTLRv~H0JH~-V0PIatS0hIX7YV5fQ8S-CIl5Cbz4cbm30QgbUDBmEu~4H4Hs7ywAR9TDzJfp~uJtbSXGZ0fJGdjfne~ReZ7EOjuZsFVhZcd1Gzt6-r5znEu~S8oKKFo-GI9iAVvwzn~Q2vkr9iNqKyvADFpydhNc06LOYwxL6vEMAfCVXyAhFdd70VKILxWnAP-JVrBZH-iSYfxe3goPFJFqXB1owL1v6mwK1SQhIf64kTBZfeIoBYoY9QBhmzLdPlesCjBuIX11H0XmtPquUJICTZmyUPLkg4sfHaMPw4xaQbmjP2y8mqgkF7xLz2up7HnFaUMKSRen3aluuDPw__";
    //private static final String signedUrl = "https://dthl9qxmc6c3r.cloudfront.net/playback.m3u8";
    //private static final String unsignedUrl = "https://vivoreco.s3.us-west-2.amazonaws.com/playback24hts.m3u8";

    interface Listener {
        void onUnsupportedTrack(int trackType);
    }

    private final Context context;
    private final StyledPlayerView playerView;
    private final ExoPlayer exoPlayer;
    private final ArrayList<MediaItem> mediaQueue;
    private final Listener listener;
    private DefaultHttpDataSource.Factory factory;

    public PlayerManager(
            Context context, Listener listener, StyledPlayerView playerView) {
        this.context = context;
        this.listener = listener;
        this.playerView = playerView;
        mediaQueue = new ArrayList<>();
        exoPlayer = new ExoPlayer.Builder(context).build();
        factory = new DefaultHttpDataSource.Factory();
        preparePlayer();
    }

    public void playbackByMediaItem(String url) {
        mediaQueue.clear();
        exoPlayer.removeMediaItem(0);

        MediaItem item = buildMediaItem(url);
        mediaQueue.add(item);
        exoPlayer.addMediaItem(item);
        exoPlayer.setMediaItems(mediaQueue, 0, C.TIME_UNSET);
        //exoPlayer.seekTo(itemIndex, C.TIME_UNSET); //if mediaQueue isn't change, we can use seek
        exoPlayer.prepare();
    }

    public void playbackByMediaSource(String url) {
        exoPlayer.setMediaSource(buildMediaSource(url));
        exoPlayer.prepare();
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        return playerView.dispatchKeyEvent(event);
    }

    public void release() {
        mediaQueue.clear();
        playerView.setPlayer(null);
        exoPlayer.release();
    }

    @Override
    public void onPlaybackStateChanged(@Player.State int playbackState) {
    }

    @Override
    public void onPositionDiscontinuity(
            Player.PositionInfo oldPosition,
            Player.PositionInfo newPosition,
            @DiscontinuityReason int reason) {
    }

    @Override
    public void onTimelineChanged(Timeline timeline, @TimelineChangeReason int reason) {
    }

    @Override
    public void onTracksInfoChanged(TracksInfo tracksInfo) {
        if (!tracksInfo.isTypeSupportedOrEmpty(C.TRACK_TYPE_VIDEO)) {
            listener.onUnsupportedTrack(C.TRACK_TYPE_VIDEO);
        }
        if (!tracksInfo.isTypeSupportedOrEmpty(C.TRACK_TYPE_AUDIO)) {
            listener.onUnsupportedTrack(C.TRACK_TYPE_AUDIO);
        }
    }

    private void preparePlayer() {
        exoPlayer.addListener(this);

        playerView.setPlayer(exoPlayer);
        playerView.setControllerHideOnTouch(true);
        playerView.setControllerShowTimeoutMs(StyledPlayerControlView.DEFAULT_SHOW_TIMEOUT_MS);

        exoPlayer.setPlayWhenReady(true);
    }

    private MediaSource buildMediaSource(String url) {

        HashMap header = new HashMap();
        header.put("Cookie", cookieValue);
        factory.setDefaultRequestProperties(header);

        MediaItem mediaItem = new MediaItem.Builder()
                .setUri(Uri.parse(url))
                .setMimeType(MimeTypes.APPLICATION_M3U8)
                .build();

        HlsMediaSource hlsMediaSource = new HlsMediaSource.Factory(factory).
                createMediaSource(mediaItem);
        return hlsMediaSource;
    }

    private MediaItem buildMediaItem(String url) {
        return new MediaItem.Builder()
                .setUri(url)
                .setMimeType(MimeTypes.APPLICATION_M3U8)
                .build();
    }
}
