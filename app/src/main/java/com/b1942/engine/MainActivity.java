package com.b1942.engine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnLaunchGame).setOnClickListener(v -> 
            startActivity(new Intent(this, GameActivity.class)));

        findViewById(R.id.btnOpenSettings).setOnClickListener(v -> 
            startActivity(new Intent(this, SettingsActivity.class)));

        findViewById(R.id.btnSelectFolder).setOnClickListener(v -> 
            startActivity(new Intent(this, FileBrowserActivity.class)));
    }
    }
