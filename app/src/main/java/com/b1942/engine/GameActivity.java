package com.b1942.engine;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    private final Handler h = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        hideSystemUI();

        TextView tvLoad = findViewById(R.id.tvLoad);
        String[] steps = {
                "Init engine...",
                "Load core...",
                "Scan files...",
                "Load archives...",
                "Load textures...",
                "Load audio...",
                "Init renderer...",
                "Ready!"
        };

        for (int i = 0; i < steps.length; i++) {
            final String s = steps[i];
            final boolean last = (i == steps.length - 1);
            h.postDelayed(() -> {
                tvLoad.setText(s);
                if (last) {
                    h.postDelayed(() -> {
                        findViewById(R.id.loading).setVisibility(View.GONE);
                        findViewById(R.id.controls).setVisibility(View.VISIBLE);
                    }, 500);
                }
            }, (i + 1) * 600L);
        }
    }

    private void hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsController c = getWindow().getInsetsController();
            if (c != null) {
                c.hide(WindowInsets.Type.systemBars());
                c.setSystemBarsBehavior(
                        WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            }
        } else {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        h.removeCallbacksAndMessages(null);
    }
}
