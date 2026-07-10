package net.vampirestudios.packwright.api;

/**
 * an entrypoint called on preLaunch asynchronously
 */
public interface PackwrightPreGenEntrypoint {
	/**
	 * pregenerate assets here and put them in a runtime resource pack, don't forget to register a callback
	 *
	 * @see PackwrightCallback
	 */
	void pregen();
}
