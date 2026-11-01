package com.b1942.engine;

import android.content.Context;
import android.content.res.AssetManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class EngineExtractor {

    // Folder tujuan internal aplikasi tempat game mengeksekusi file
    public static File getInternalEngineDir(Context context) {
        File dir = new File(context.getFilesDir(), "bf1942_standalone");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    // Fungsi untuk mengekstrak seluruh isi folder assets/bf1942 ke memori internal HP
    public static boolean extractAssetsIfNeeded(Context context) {
        File targetDir = getInternalEngineDir(context);
        File flagFile = new File(targetDir, ".extracted_v1");

        // Jika sudah pernah diekstrak sebelumnya, lewati proses extraction
        if (flagFile.exists()) {
            return true;
        }

        try {
            copyAssetFolder(context.getAssets(), "bf1942", targetDir.getAbsolutePath());
            // Buat tanda bahwa penyiapan engine sudah selesai 100%
            flagFile.createNewFile();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void copyAssetFolder(AssetManager assetManager, String fromAssetPath, String toPath) throws IOException {
        String[] files = assetManager.list(fromAssetPath);
        if (files == null) return;

        File targetFolder = new File(toPath);
        if (!targetFolder.exists()) {
            targetFolder.mkdirs();
        }

        for (String file : files) {
            String assetSubPath = fromAssetPath.isEmpty() ? file : fromAssetPath + "/" + file;
            String targetSubPath = toPath + "/" + file;

            String[] subFiles = assetManager.list(assetSubPath);
            if (subFiles != null && subFiles.length > 0) {
                // Folder -> Rekursif
                copyAssetFolder(assetManager, assetSubPath, targetSubPath);
            } else {
                // File -> Salin Ke Penyimpanan Internal
                copySingleFile(assetManager, assetSubPath, targetSubPath);
            }
        }
    }

    private static void copySingleFile(AssetManager assetManager, String fromAssetPath, String toPath) throws IOException {
        InputStream in = assetManager.open(fromAssetPath);
        OutputStream out = new FileOutputStream(toPath);

        byte[] buffer = new byte[1024 * 8];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        in.close();
        out.flush();
        out.close();
    }
}
