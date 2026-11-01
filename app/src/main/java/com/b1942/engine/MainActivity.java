package com.b1942.engine;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private TextView tvEngineStatus, tvFpsDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvEngineStatus = findViewById(R.id.tvEngineStatus);
        tvFpsDisplay = findViewById(R.id.tvFpsDisplay);

        // Menyiapkan Engine Internal APK
        boolean isReady = EngineExtractor.prepareEmbeddedEngine(this);
        if(isReady) {
            tvEngineStatus.setText("Engine: Standalone Ready");
        }

        Button btnLaunch = findViewById(R.id.btnLaunchGame);
        Button btnSettings = findViewById(R.id.btnOpenSettings);
        Button btnFolder = findViewById(R.id.btnSelectFolder);

        btnLaunch.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, GameActivity.class));
        });

        btnSettings.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        });

        btnFolder.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, FileBrowserActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences("B1942_Prefs", MODE_PRIVATE);
        String[] fpsArray = {"30 FPS", "60 FPS", "90 FPS", "120 FPS", "MAX FPS"};
        int fpsIdx = prefs.getInt("fps_idx", 1);
        tvFpsDisplay.setText(fpsArray[Math.min(fpsIdx, fpsArray.length - 1)]);
    }
                          }
