package com.b1942.engine;

import android.content.Context;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class EngineExtractor {

    // Lokasi Engine Internal di dalam APK
    public static File getInternalEngineDir(Context context) {
        File engineDir = new File(context.getFilesDir(), "engine_runtime");
        if (!engineDir.exists()) {
            engineDir.mkdirs();
        }
        return engineDir;
    }

    // Penyiapan Konfigurasi & Runtime Engine B1942 (In-APK)
    public static void setupInternalEngine(Context context) {
        File engineDir = getInternalEngineDir(context);
        File configFile = new File(engineDir, "engine_config.ini");

        if (!configFile.exists()) {
            try {
                FileOutputStream fos = new FileOutputStream(configFile);
                String defaultEngineConfig = 
                    "[Engine]\n" +
                    "Version=2.5-GDROID-STANDALONE\n" +
                    "RenderDriver=OpenGL_ES3\n" +
                    "MultiThreading=1\n" +
                    "MemoryPool=512MB\n\n" +
                    "[InputBridge]\n" +
                    "TouchController=Octagonal_V2\n" +
                    "Deadzone=0.15\n";
                fos.write(defaultEngineConfig.getBytes());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
