package com.b1942.engine;

import android.content.Context;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class EngineExtractor {

    public static File getEngineDir(Context context) {
        File internalEngine = new File(context.getFilesDir(), "bf1942_engine");
        if (!internalEngine.exists()) {
            internalEngine.mkdirs();
        }
        return internalEngine;
    }

    public static void prepareEmbeddedEngine(Context context) {
        File engineDir = getEngineDir(context);
        File configFile = new File(engineDir, "bf1942.ini");
        
        if (!configFile.exists()) {
            try {
                FileOutputStream fos = new FileOutputStream(configFile);
                String defaultCfg = "[Graphics]\nResolution=1280x720\nFPSLimit=60\nRenderer=OpenGL\n" +
                                   "[Engine]\nEmbedded=1\nVersion=2.5-GDROID\n";
                fos.write(defaultCfg.getBytes());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
