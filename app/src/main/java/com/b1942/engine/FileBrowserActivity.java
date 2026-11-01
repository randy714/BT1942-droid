package com.b1942.engine;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileBrowserActivity extends AppCompatActivity {

    private File dir;
    private TextView tvPath;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_browser);

        tvPath = findViewById(R.id.tvPath);
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));

        dir = Environment.getExternalStorageDirectory();
        load();

        findViewById(R.id.btnUp).setOnClickListener(v -> {
            File p = dir.getParentFile();
            if (p != null && p.canRead()) {
                dir = p;
                load();
            }
        });

        findViewById(R.id.btnPick).setOnClickListener(v -> {
            getSharedPreferences("b1942", MODE_PRIVATE).edit()
                    .putString("path", dir.getAbsolutePath()).apply();
            Toast.makeText(this, "Dipilih: " + dir.getAbsolutePath(), Toast.LENGTH_LONG).show();
            finish();
        });
    }

    private void load() {
        tvPath.setText(dir.getAbsolutePath());
        File[] fs = dir.listFiles();
        List<File> list = new ArrayList<>();
        if (fs != null) {
            for (File f : fs) {
                if (f.isDirectory() && !f.getName().startsWith(".")) {
                    list.add(f);
                }
            }
        }
        Collections.sort(list, (a, b) -> a.getName().compareToIgnoreCase(b.getName()));
        rv.setAdapter(new FolderAdapter(list));
    }

    private class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.VH> {

        private final List<File> items;

        FolderAdapter(List<File> items) {
            this.items = items;
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView tv = new TextView(parent.getContext());
            tv.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            tv.setPadding(32, 24, 32, 24);
            tv.setTextSize(14);
            tv.setTextColor(0xFFE8E4D8);
            tv.setBackgroundColor(0xFF1A2A1A);
            return new VH(tv);
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            File f = items.get(position);
            ((TextView) holder.itemView).setText("📁 " + f.getName());
            holder.itemView.setOnClickListener(v -> {
                dir = f;
                load();
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class VH extends RecyclerView.ViewHolder {
            VH(View v) {
                super(v);
            }
        }
    }
}
