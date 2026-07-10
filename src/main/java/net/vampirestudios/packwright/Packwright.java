package net.vampirestudios.packwright;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.vampirestudios.packwright.api.PackwrightPreGenEntrypoint;
import net.vampirestudios.packwright.impl.RuntimeResourcePackImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Packwright implements PreLaunchEntrypoint {
	private static final Logger LOGGER = LogManager.getLogger("Packwright");
	private static List<Future<?>> futures;

	@Override
	public void onPreLaunch() {
		LOGGER.info("I used the json to destroy the json");
		FabricLoader loader = FabricLoader.getInstance();
		List<Future<?>> futures = new ArrayList<>();
		for (PackwrightPreGenEntrypoint entrypoint : loader.getEntrypoints("packwright:pregen", PackwrightPreGenEntrypoint.class)) {
			futures.add(RuntimeResourcePackImpl.EXECUTOR_SERVICE.submit(entrypoint::pregen));
		}
		// legacy ARRP entrypoint key, kept so pregen never silently stops running
		List<PackwrightPreGenEntrypoint> legacy = loader.getEntrypoints("rrp:pregen", PackwrightPreGenEntrypoint.class);
		if (!legacy.isEmpty()) {
			LOGGER.warn("{} mod(s) still use the legacy 'rrp:pregen' entrypoint; rename it to 'packwright:pregen'", legacy.size());
			for (PackwrightPreGenEntrypoint entrypoint : legacy) {
				futures.add(RuntimeResourcePackImpl.EXECUTOR_SERVICE.submit(entrypoint::pregen));
			}
		}
		Packwright.futures = futures;
	}

	public static void waitForPregen() throws ExecutionException, InterruptedException {
		if(futures != null) {
			for(Future<?> future : futures) {
				future.get();
			}
			futures = null;
		}
	}
}
