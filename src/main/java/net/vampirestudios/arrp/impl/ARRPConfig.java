package net.vampirestudios.arrp.impl;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * ARRP's own configuration, loaded once from {@code config/arrp.properties}.
 * <p>
 * Every value can be overridden for a single run with a system property of the
 * same name prefixed with {@code arrp.}, e.g. {@code -Darrp.dump_on_close=true}.
 * A missing or partially invalid file never gets overwritten; only a missing
 * file is written (with defaults and documentation comments) on first run.
 * <p>
 * The legacy {@code config/rrp.properties} ({@code threads}, {@code dump assets},
 * {@code debug performance}) is migrated automatically the first time.
 */
@ApiStatus.Internal
public final class ARRPConfig {
	private static final Logger LOGGER = LogManager.getLogger("ARRP/Config");
	private static final String FILE_NAME = "arrp.properties";
	private static ARRPConfig instance;

	/** number of worker threads used to generate resources */
	public final int threads;
	/** dump every pack's contents to {@link #dumpDirectory} when it is closed */
	public final boolean dumpOnClose;
	/** where {@code dump()} and {@link #dumpOnClose} write packs, one folder per pack id */
	public final Path dumpDirectory;
	/** log a warning when a thread had to wait for a pack lock */
	public final boolean debugPerformance;
	/** pretty-print generated JSON; disable for slightly smaller and faster output */
	public final boolean prettyJson;

	private ARRPConfig(int threads, boolean dumpOnClose, Path dumpDirectory, boolean debugPerformance, boolean prettyJson) {
		this.threads = threads;
		this.dumpOnClose = dumpOnClose;
		this.dumpDirectory = dumpDirectory;
		this.debugPerformance = debugPerformance;
		this.prettyJson = prettyJson;
	}

	public static synchronized ARRPConfig get() {
		if (instance == null) {
			instance = load();
		}
		return instance;
	}

	private static ARRPConfig load() {
		Path configDir = configDir();
		Path file = configDir.resolve(FILE_NAME);

		Properties properties = new Properties();
		boolean fileExists = Files.exists(file);
		if (fileExists) {
			try (Reader reader = Files.newBufferedReader(file)) {
				properties.load(reader);
			} catch (IOException e) {
				LOGGER.error("Could not read {}, using defaults", file, e);
			}
		} else {
			migrateLegacy(configDir, properties);
		}

		int defaultThreads = Math.max(Runtime.getRuntime().availableProcessors() / 2 - 1, 1);
		int threads = Math.max(intValue(properties, "threads", defaultThreads), 1);
		boolean dumpOnClose = boolValue(properties, "dump_on_close", false);
		Path dumpDirectory = Path.of(stringValue(properties, "dump_directory", "rrp.debug"));
		boolean debugPerformance = boolValue(properties, "debug_performance", false);
		boolean prettyJson = boolValue(properties, "pretty_json", true);

		ARRPConfig config = new ARRPConfig(threads, dumpOnClose, dumpDirectory, debugPerformance, prettyJson);
		if (!fileExists) {
			config.write(file);
		}
		return config;
	}

	/** the loader's config directory, or {@code config/} when running outside Fabric (e.g. tests) */
	private static Path configDir() {
		try {
			return FabricLoader.getInstance().getConfigDir();
		} catch (Throwable outsideFabric) {
			return Path.of("config");
		}
	}

	/** carries over values from the pre-rewrite {@code rrp.properties} */
	private static void migrateLegacy(Path configDir, Properties target) {
		Path legacy = configDir.resolve("rrp.properties");
		if (!Files.exists(legacy)) return;

		Properties old = new Properties();
		try (Reader reader = Files.newBufferedReader(legacy)) {
			old.load(reader);
		} catch (IOException e) {
			LOGGER.warn("Could not migrate legacy rrp.properties", e);
			return;
		}
		copyIfPresent(old, "threads", target, "threads");
		copyIfPresent(old, "dump assets", target, "dump_on_close");
		copyIfPresent(old, "debug performance", target, "debug_performance");
		LOGGER.info("Migrated legacy rrp.properties to {}", FILE_NAME);
	}

	private static void copyIfPresent(Properties from, String fromKey, Properties to, String toKey) {
		String value = from.getProperty(fromKey);
		if (value != null) to.setProperty(toKey, value.trim());
	}

	private void write(Path file) {
		try {
			Files.createDirectories(file.getParent());
			Properties out = new Properties();
			out.setProperty("threads", String.valueOf(threads));
			out.setProperty("dump_on_close", String.valueOf(dumpOnClose));
			out.setProperty("dump_directory", dumpDirectory.toString());
			out.setProperty("debug_performance", String.valueOf(debugPerformance));
			out.setProperty("pretty_json", String.valueOf(prettyJson));
			try (Writer writer = Files.newBufferedWriter(file)) {
				out.store(writer, """
						 ARRP configuration
						 threads           - worker threads used to generate resources
						 dump_on_close     - write every pack's contents to dump_directory when it is closed
						 dump_directory    - where dumps are written, one folder per pack id
						 debug_performance - log when a thread had to wait for a pack lock
						 pretty_json       - pretty-print generated JSON
						 every key can be overridden per run with -Darrp.<key>=<value>""");
			}
		} catch (IOException e) {
			LOGGER.error("Unable to write {}", file, e);
		}
	}

	// per-key parsing: a bad value logs a warning and falls back to the default
	// instead of discarding the whole file

	private static String stringValue(Properties properties, String key, String defaultValue) {
		String value = System.getProperty("arrp." + key, properties.getProperty(key));
		return value == null || value.isBlank() ? defaultValue : value.trim();
	}

	private static int intValue(Properties properties, String key, int defaultValue) {
		String value = stringValue(properties, key, null);
		if (value == null) return defaultValue;
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			LOGGER.warn("Invalid value '{}' for {}, using {}", value, key, defaultValue);
			return defaultValue;
		}
	}

	private static boolean boolValue(Properties properties, String key, boolean defaultValue) {
		String value = stringValue(properties, key, null);
		if (value == null) return defaultValue;
		if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
			return Boolean.parseBoolean(value);
		}
		LOGGER.warn("Invalid value '{}' for {}, using {}", value, key, defaultValue);
		return defaultValue;
	}
}
