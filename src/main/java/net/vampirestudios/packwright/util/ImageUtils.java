package net.vampirestudios.packwright.util;

import net.vampirestudios.packwright.PackwrightException;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.function.IntUnaryOperator;

/**
 * Static helpers for generating and transforming textures at runtime. All
 * pixel values are ARGB ints ({@code 0xAARRGGBB}); all methods return a new
 * {@link BufferedImage} of {@code TYPE_INT_ARGB} and never modify their
 * inputs. Scaling is always nearest-neighbor to keep pixel art crisp.
 *
 * @see net.vampirestudios.packwright.api.RuntimeResourcePack#addTexture
 */
public final class ImageUtils {
	private ImageUtils() {
	}

	/** reads a PNG (or any ImageIO-supported format) from the stream */
	public static BufferedImage read(InputStream stream) {
		try {
			BufferedImage image = ImageIO.read(stream);
			if (image == null) throw new PackwrightException("Stream does not contain a readable image");
			return toArgb(image);
		} catch (IOException e) {
			throw new PackwrightException("Failed to read image", e);
		}
	}

	/** encodes the image as PNG bytes */
	public static byte[] toPngBytes(BufferedImage image) {
		GrowableByteBuffer buffer = new GrowableByteBuffer();
		try {
			ImageIO.write(image, "png", buffer);
		} catch (IOException e) {
			throw new AssertionError("ImageIO.write to in-memory buffer should never fail", e);
		}
		return buffer.toByteArray();
	}

	/** copies the image into a fresh {@code TYPE_INT_ARGB} image */
	public static BufferedImage copy(BufferedImage image) {
		return recolor(image, IntUnaryOperator.identity());
	}

	/**
	 * applies the operator to every ARGB pixel
	 */
	public static BufferedImage recolor(BufferedImage image, IntUnaryOperator pixel) {
		BufferedImage out = blank(image.getWidth(), image.getHeight());
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				out.setRGB(x, y, pixel.applyAsInt(image.getRGB(x, y)));
			}
		}
		return out;
	}

	/**
	 * multiplies every pixel's color channels by the given color, keeping
	 * alpha — the most common way to make colored variants of a white or
	 * grayscale template
	 *
	 * @param color RGB color, e.g. {@code 0xFF8800}; any alpha byte is ignored
	 */
	public static BufferedImage tint(BufferedImage image, int color) {
		int tr = (color >> 16) & 0xFF, tg = (color >> 8) & 0xFF, tb = color & 0xFF;
		return recolor(image, argb -> {
			int a = (argb >>> 24);
			int r = ((argb >> 16) & 0xFF) * tr / 255;
			int g = ((argb >> 8) & 0xFF) * tg / 255;
			int b = (argb & 0xFF) * tb / 255;
			return (a << 24) | (r << 16) | (g << 8) | b;
		});
	}

	/**
	 * replaces exact colors according to the map — the runtime equivalent of
	 * an atlas {@code paletted_permutations} source. Pixels whose color is not
	 * in the map are kept as-is.
	 *
	 * @param palette map from old ARGB color to new ARGB color
	 */
	public static BufferedImage swapColors(BufferedImage image, Map<Integer, Integer> palette) {
		return recolor(image, argb -> palette.getOrDefault(argb, argb));
	}

	/**
	 * maps a grayscale template onto a color ramp: each pixel's luminance
	 * (0–255) picks a color from {@code palette}, where index 0 is darkest and
	 * the last entry is brightest. The template's alpha is kept.
	 * <p>
	 * useful for generating ore/tool/armor variants from one grayscale template
	 */
	public static BufferedImage grayscaleToPalette(BufferedImage template, int[] palette) {
		if (palette.length == 0) throw new IllegalArgumentException("palette must not be empty");
		return recolor(template, argb -> {
			int a = (argb >>> 24);
			int r = (argb >> 16) & 0xFF, g = (argb >> 8) & 0xFF, b = argb & 0xFF;
			int luminance = (r + g + b) / 3;
			int index = luminance * palette.length / 256;
			return (a << 24) | (palette[index] & 0xFFFFFF);
		});
	}

	/**
	 * draws the layers over the base in order with regular alpha blending;
	 * the result has the base's size, layers are drawn at the top-left corner
	 */
	public static BufferedImage overlay(BufferedImage base, BufferedImage... layers) {
		BufferedImage out = blank(base.getWidth(), base.getHeight());
		Graphics2D graphics = out.createGraphics();
		try {
			graphics.drawImage(base, 0, 0, null);
			for (BufferedImage layer : layers) {
				graphics.drawImage(layer, 0, 0, null);
			}
		} finally {
			graphics.dispose();
		}
		return out;
	}

	/** rotates clockwise by {@code quarterTurns * 90} degrees */
	public static BufferedImage rotate(BufferedImage image, int quarterTurns) {
		int turns = Math.floorMod(quarterTurns, 4);
		if (turns == 0) return copy(image);
		int w = image.getWidth(), h = image.getHeight();
		boolean swap = turns % 2 == 1;
		BufferedImage out = blank(swap ? h : w, swap ? w : h);
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				int argb = image.getRGB(x, y);
				switch (turns) {
					case 1 -> out.setRGB(h - 1 - y, x, argb);
					case 2 -> out.setRGB(w - 1 - x, h - 1 - y, argb);
					case 3 -> out.setRGB(y, w - 1 - x, argb);
				}
			}
		}
		return out;
	}

	/** mirrors horizontally (left/right) */
	public static BufferedImage flipX(BufferedImage image) {
		int w = image.getWidth(), h = image.getHeight();
		BufferedImage out = blank(w, h);
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				out.setRGB(w - 1 - x, y, image.getRGB(x, y));
			}
		}
		return out;
	}

	/** mirrors vertically (top/bottom) */
	public static BufferedImage flipY(BufferedImage image) {
		int w = image.getWidth(), h = image.getHeight();
		BufferedImage out = blank(w, h);
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				out.setRGB(x, h - 1 - y, image.getRGB(x, y));
			}
		}
		return out;
	}

	/** integer nearest-neighbor upscale, e.g. {@code scale(image, 2)} doubles the size */
	public static BufferedImage scale(BufferedImage image, int factor) {
		if (factor < 1) throw new IllegalArgumentException("factor must be >= 1, got " + factor);
		return resize(image, image.getWidth() * factor, image.getHeight() * factor);
	}

	/** nearest-neighbor resize to the exact given size */
	public static BufferedImage resize(BufferedImage image, int width, int height) {
		BufferedImage out = blank(width, height);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				out.setRGB(x, y, image.getRGB(x * image.getWidth() / width, y * image.getHeight() / height));
			}
		}
		return out;
	}

	/** extracts the given rectangle, e.g. one sprite out of a sheet */
	public static BufferedImage crop(BufferedImage image, int x, int y, int width, int height) {
		BufferedImage out = blank(width, height);
		for (int dy = 0; dy < height; dy++) {
			for (int dx = 0; dx < width; dx++) {
				out.setRGB(dx, dy, image.getRGB(x + dx, y + dy));
			}
		}
		return out;
	}

	/**
	 * stacks equally-sized frames vertically into one animation-strip image,
	 * the layout Minecraft expects for animated textures
	 *
	 * @see net.vampirestudios.packwright.api.RuntimeResourcePack#addAnimatedTexture
	 */
	public static BufferedImage stitchFrames(BufferedImage... frames) {
		if (frames.length == 0) throw new IllegalArgumentException("at least one frame is required");
		int w = frames[0].getWidth(), h = frames[0].getHeight();
		for (BufferedImage frame : frames) {
			if (frame.getWidth() != w || frame.getHeight() != h) {
				throw new IllegalArgumentException("all frames must be the same size; expected "
						+ w + "x" + h + ", got " + frame.getWidth() + "x" + frame.getHeight());
			}
		}
		BufferedImage out = blank(w, h * frames.length);
		Graphics2D graphics = out.createGraphics();
		try {
			for (int i = 0; i < frames.length; i++) {
				graphics.drawImage(frames[i], 0, i * h, null);
			}
		} finally {
			graphics.dispose();
		}
		return out;
	}

	private static BufferedImage blank(int width, int height) {
		return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}

	private static BufferedImage toArgb(BufferedImage image) {
		return image.getType() == BufferedImage.TYPE_INT_ARGB ? image : copy(image);
	}
}
