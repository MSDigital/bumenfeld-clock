package com.bumenfeld.clock;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

final class ExtractedAssetPackManifestEnsurer {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private ExtractedAssetPackManifestEnsurer() {
    }

    static void ensure(JavaPlugin plugin) {
        try {
            var pluginManifest = plugin.getManifest();
            if (pluginManifest == null || !pluginManifest.includesAssetPack()) {
                return;
            }

            Path dataDirectory = plugin.getDataDirectory();
            if (dataDirectory == null) {
                return;
            }

            Files.createDirectories(dataDirectory);
            Path extractedManifest = dataDirectory.resolve("manifest.json");
            String extractedPackName = extractedPackName(pluginManifest.getName());
            try (InputStream stream = plugin.getClass().getClassLoader().getResourceAsStream("manifest.json")) {
                if (stream == null) {
                    return;
                }
                JsonNode root = MAPPER.readTree(stream);
                if (!(root instanceof ObjectNode rootObject)) {
                    return;
                }
                if (!shouldWriteExtractedManifest(
                    extractedManifest,
                    pluginManifest.getGroup(),
                    pluginManifest.getName(),
                    extractedPackName,
                    rootObject
                )) {
                    return;
                }

                rootObject.put("Name", extractedPackName);
                rootObject.put("IncludesAssetPack", false);
                MAPPER.writerWithDefaultPrettyPrinter().writeValue(extractedManifest.toFile(), rootObject);
            }
        } catch (IOException ignored) {
            // Best-effort compatibility fix for hosters that create extracted mod folders without a root manifest.
        } catch (RuntimeException ignored) {
            // Best-effort compatibility fix for hosters that create extracted mod folders without a root manifest.
        }
    }

    private static boolean shouldWriteExtractedManifest(
        Path path,
        String pluginGroup,
        String pluginName,
        String extractedPackName,
        JsonNode bundledManifest
    ) {
        if (!Files.exists(path)) {
            return true;
        }

        try {
            JsonNode root = MAPPER.readTree(path.toFile());
            if (!(root instanceof ObjectNode)) {
                return true;
            }
            String group = root.path("Group").asText("");
            String name = root.path("Name").asText("");

            if (group.equals(pluginGroup) && name.equals(extractedPackName)) {
                // Managed extracted manifest must keep IncludesAssetPack=false to avoid duplicate asset-pack registration.
                if (root.path("IncludesAssetPack").asBoolean(true)) {
                    return true;
                }

                String extractedBuildId = root.path("Build").path("Id").asText("");
                String bundledBuildId = bundledManifest.path("Build").path("Id").asText("");
                if (!bundledBuildId.isBlank() && !bundledBuildId.equals(extractedBuildId)) {
                    return true;
                }

                String extractedServerVersion = root.path("ServerVersion").asText("");
                String bundledServerVersion = bundledManifest.path("ServerVersion").asText("");
                if (!bundledServerVersion.isBlank() && !bundledServerVersion.equals(extractedServerVersion)) {
                    return true;
                }

                return false;
            }
            if (group.equals(pluginGroup) && name.equals(pluginName)) {
                return true;
            }
            return false;
        } catch (IOException ignored) {
            return true;
        }
    }

    private static String extractedPackName(String pluginName) {
        if (pluginName == null || pluginName.isBlank()) {
            return "Config_Plugin";
        }
        return "Config_" + pluginName;
    }
}
