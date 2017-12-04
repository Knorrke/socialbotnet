package modules.util;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.security.MessageDigest;

public class Identicons {

	public static byte[] hash(String input) {
		try{
		byte[] bytesOfMessage = input.getBytes("UTF-8");

		MessageDigest md = MessageDigest.getInstance("MD5");
		return md.digest(bytesOfMessage);
		} catch(Exception ignore){return null;}
	}

	public static BufferedImage generateIdenticons(String text, int image_width, int image_height) {
		int width = 5, height = 5;

		byte[] hash = hash(text);
		
		BufferedImage identicon = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		WritableRaster raster = identicon.getRaster();

		int[] background = new int[] { 255, 255, 255, 0 };
		int[] foreground = new int[] { hash[0] & 255, hash[1] & 255, hash[2] & 255, 255 };

		for (int x = 0; x < width; x++) {
			// Enforce horizontal symmetry
			int i = x < 3 ? x : 4 - x;
			for (int y = 0; y < height; y++) {
				int[] pixelColor;
				// toggle pixels based on bit being on/off
				if ((hash[i] >> y & 1) == 1)
					pixelColor = foreground;
				else
					pixelColor = background;
				raster.setPixel(x, y, pixelColor);
			}
		}

		BufferedImage finalImage = new BufferedImage(image_width, image_height, BufferedImage.TYPE_INT_ARGB);

		// Scale image to the size you want
		AffineTransform at = new AffineTransform();
		at.scale(image_width / width, image_height / height);
		AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		finalImage = op.filter(identicon, finalImage);

		return finalImage;
	}
}