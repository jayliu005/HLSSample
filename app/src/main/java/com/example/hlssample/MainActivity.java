package com.example.hlssample;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ui.StyledPlayerView;

public class MainActivity extends AppCompatActivity implements PlayerManager.Listener {
    private StyledPlayerView playerView;
    private PlayerManager playerManager;
    private EditText inputUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerView = findViewById(R.id.player_view);
        playerView.requestFocus();
        inputUrl = findViewById(R.id.input_url);
    }

    @Override
    public void onResume() {
        super.onResume();
        playerManager = new PlayerManager(this, this, playerView);
    }

    @Override
    public void onPause() {
        super.onPause();
        playerManager.release();
        playerManager = null;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event) || playerManager.dispatchKeyEvent(event);
    }

    public void onClickPlaybackByMediaItem(View view) {
        playerManager.playbackByMediaItem(inputUrl.getText().toString());
    }

    public void onClickPlaybackByMediaSource(View view) {
        playerManager.playbackByMediaSource(inputUrl.getText().toString());
    }

    @Override
    public void onUnsupportedTrack(int trackType) {
        if (trackType == C.TRACK_TYPE_AUDIO) {
            showToast(R.string.error_unsupported_audio);
        } else if (trackType == C.TRACK_TYPE_VIDEO) {
            showToast(R.string.error_unsupported_video);
        }
    }

    private void showToast(int messageId) {
        Toast.makeText(getApplicationContext(), messageId, Toast.LENGTH_LONG).show();
    }
}