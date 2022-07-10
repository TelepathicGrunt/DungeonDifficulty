package net.powerscale.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import net.fabricmc.loader.api.FabricLoader;
import net.powerscale.PowerScale;
import org.slf4j.Logger;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {
    static final Logger LOGGER = LogUtils.getLogger();
    public static Config currentConfig = Default.config;

    public static void initialize() {
        var config = Default.config;
        var configFileName = PowerScale.MODID + ".json";
        Path configDir = FabricLoader.getInstance().getConfigDir();

        try {
            var filePath = configDir.resolve(configFileName);
            if (Files.exists(filePath)) {
                // Read
                var gson = new Gson();
                Reader reader = Files.newBufferedReader(filePath);
                config = gson.fromJson(reader, Config.class);
                reader.close();
                LOGGER.info("PowerScale config loaded: " + gson.toJson(config));
            } else {
                // Write
                var gson = new GsonBuilder().setPrettyPrinting().create();
                Writer writer = Files.newBufferedWriter(filePath);
                var json = gson.toJson(config);
                writer.write(json);
                writer.close();
                LOGGER.info("PowerScale default config written: " + json);
            }
        } catch(Exception e) {
            LOGGER.error("Failed loading PowerScale config: " + e.getMessage());
        }

        currentConfig = config;
    }
}
