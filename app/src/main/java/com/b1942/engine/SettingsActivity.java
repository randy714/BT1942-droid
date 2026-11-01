package com.b1942.engine;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences sp = getSharedPreferences("b1942", MODE_PRIVATE);
        SeekBar sbVol = findViewById(R.id.sbVol);
        SeekBar sbJoy = findViewById(R.id.sbJoy);
        SeekBar sbBtn = findViewById(R.id.sbBtn);
        SeekBar sbAlpha = findViewById(R.id.sbAlpha);
        Button btnSave = findViewById(R.id.btnSave);

        sbVol.setProgress(sp.getInt("vol", 80));
        sbJoy.setProgress(sp.getInt("joy", 50));
        sbBtn.setProgress(sp.getInt("btn", 50));
        sbAlpha.setProgress(sp.getInt("alpha", 70));

        btnSave.setOnClickListener(v -> {
            sp.edit()
                    .putInt("vol", sbVol.getProgress())
                    .putInt("joy", sbJoy.getProgress())
                    .putInt("btn", sbBtn.getProgress())
                    .putInt("alpha", sbAlpha.getProgress())
                    .apply();
            Toast.makeText(this, "Tersimpan!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
