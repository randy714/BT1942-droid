package com.b1942.engine;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;
import java.io.File;

public class GameActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);

        SharedPreferences prefs = getSharedPreferences("B1942_Prefs", MODE_PRIVATE);
        String gameDataPath = prefs.getString("game_path", "");
        File internalEngine = EngineExtractor.getInternalEngineDir(this);

        if (gameDataPath.isEmpty()) {
            Toast.makeText(this, "Please select Game Data folder first!", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Launching B1942 Engine...", Toast.LENGTH_SHORT).show();
            // Engine Logic Here
        }
    }
}
