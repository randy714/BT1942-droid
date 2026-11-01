package com.b1942.engine;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

        Button btnLaunch = findViewById(R.id.btnLaunchGame);
        Button btnSettings = findViewById(R.id.btnOpenSettings);
        Button btnFolder = findViewById(R.id.btnSelectFolder);
        Button btnLayout = findViewById(R.id.btnControlsLayout);

        // Ekstrak Engine Internal secara Background
        checkAndExtractEngine();

        btnLaunch.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            startActivity(intent);
        });

        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        btnFolder.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FileBrowserActivity.class);
            startActivity(intent);
        });

        btnLayout.setOnClickListener(v -> {
            Toast.makeText(this, "Opening Control Layout Editor...", Toast.LENGTH_SHORT).show();
        });
    }

    private void checkAndExtractEngine() {
        ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Unpacking Integrated B1942 Engine Assets...");
        progress.setCancelable(false);
        progress.show();

        new Thread(() -> {
            boolean success = EngineExtractor.extractAssetsIfNeeded(MainActivity.this);
            new Handler(Looper.getMainLooper()).post(() -> {
                progress.dismiss();
                if (success) {
                    tvEngineStatus.setText("Engine: Embedded & Ready");
                    Toast.makeText(MainActivity.this, "Engine Ready!", Toast.LENGTH_SHORT).show();
                } else {
                    tvEngineStatus.setText("Engine: Extraction Failed");
                }
            });
        }).start();
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
