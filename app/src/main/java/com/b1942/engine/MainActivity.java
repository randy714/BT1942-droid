package com.b1942.engine;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private String path = "";
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences("b1942", MODE_PRIVATE);

        TextView tvInfo = findViewById(R.id.tvInfo);
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        long ramGB = mi.totalMem / (1024L * 1024L * 1024L);
        tvInfo.setText("RAM: " + ramGB + "GB | Android " + Build.VERSION.RELEASE);

        findViewById(R.id.btnFolder).setOnClickListener(v -> {
            if (!hasPerm()) {
                reqPerm();
            } else {
                startActivity(new Intent(this, FileBrowserActivity.class));
            }
        });

        findViewById(R.id.btnPlay).setOnClickListener(v -> {
            if (valid()) {
                Intent i = new Intent(this, GameActivity.class);
                i.putExtra("path", path);
                startActivity(i);
            } else {
                Toast.makeText(this, "File game tidak valid!", Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.btnSettings).setOnClickListener(v ->
                startActivity(new Intent(this, SettingsActivity.class)));
    }

    private boolean hasPerm() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        }
        return ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                == android.content.pm.PackageManager.PERMISSION_GRANTED;
    }

    private void reqPerm() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent i = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                i.setData(Uri.parse("package:" + getPackageName()));
                startActivity(i);
            } catch (Exception e) {
                startActivity(new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION));
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);
        }
    }

    private boolean valid() {
        path = sp.getString("path", "");
        if (path.isEmpty()) return false;
        File d = new File(path);
        if (!d.exists() || !d.isDirectory()) return false;
        return new File(d, "BF1942.exe").exists() || new File(d, "Mods").exists();
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView st = findViewById(R.id.tvStatus);
        Button bp = findViewById(R.id.btnPlay);
        if (valid()) {
            st.setText("✓ Game B1942 terdeteksi!");
            st.setTextColor(getColor(R.color.hp));
            bp.setEnabled(true);
            bp.setAlpha(1f);
        } else {
            st.setText("Folder game belum dipilih");
            st.setTextColor(getColor(R.color.lgray));
            bp.setEnabled(false);
            bp.setAlpha(0.5f);
        }
    }
    }
