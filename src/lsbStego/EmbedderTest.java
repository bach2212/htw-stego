package lsbStego;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;

import org.junit.Rule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;

class EmbedderTest {
	static BufferedImage fourPixel, ninePixel, black32Pixel;
	static BufferedImage sword, sword1;
	static BufferedImage fourPixelInSword, msgInSword, embeddedLetter;
	static String msg;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		black32Pixel = Loader.loadImage("4x8black.png");
		ninePixel = Loader.loadImage("Test9px.png");
		fourPixel = Loader.loadImage("129481956_153559649792840_2118012743996484739_n.png");
		sword = Loader.loadImage("src/lsbStego/pngkit_pixel-sword-png_2202123.png");
		fourPixelInSword = Embedder.embedImage(sword, fourPixel);
		msg = Loader.loadText("src/text");
		sword1 = Loader.loadImage("src/lsbStego/pngkit_pixel-sword-png_2202123.png");
		msgInSword = Embedder.embedMessage(sword1, msg);
		embeddedLetter = Embedder.embedMessage(ninePixel, "a");
		
	}
	
	@Test
	void embeddedTextShouldBeCorrect() {
		assertEquals(0, ((int)(embeddedLetter.getRGB(0, 0)) & 1));
		assertEquals(1, ((int)(embeddedLetter.getRGB(1, 0)) & 1));
		assertEquals(1, ((int)(embeddedLetter.getRGB(2, 0)) & 1));
		assertEquals(0, ((int)(embeddedLetter.getRGB(0, 1)) & 1));
		assertEquals(0, ((int)(embeddedLetter.getRGB(1, 1)) & 1));
		assertEquals(0, ((int)(embeddedLetter.getRGB(2, 1)) & 1));
		assertEquals(0, ((int)(embeddedLetter.getRGB(0, 2)) & 1));
		assertEquals(1, ((int)(embeddedLetter.getRGB(1, 2)) & 1));
	}

	@Test
	void wrongMessLenghtShouldBeDenied() {
		Throwable exception = assertThrows(IllegalArgumentException.class, () -> Embedder.embedMessage(ninePixel, "ab"));
		assertThrows(IllegalArgumentException.class, () -> Embedder.embedMessage(fourPixel, "a"));
		assertEquals(exception.getMessage(), "image is too small to embed this message");
	}
	
	@Test
	void embeddedImageShouldBeCorrect() {
		int minX = black32Pixel.getMinX();
		int minY = black32Pixel.getMinY();
		int maxX = black32Pixel.getMinX() + black32Pixel.getWidth();
		int maxY = black32Pixel.getMinY()+ black32Pixel.getHeight();
		for (int y_i = minY; y_i < maxY; y_i++) {
			for (int x_i = minX; x_i < maxX; x_i++){
				System.out.println(Integer.toBinaryString(black32Pixel.getRGB(x_i, y_i)));
	        	assertEquals(0, black32Pixel.getRGB(x_i, y_i) & 1);
	        }
	    }
		BufferedImage whiteDotInBlack32Pixel = Embedder.embedImageFile("4x8black.png", "whiteDot.png");
		for (int y_i = minY; y_i < maxY; y_i++) {
			for (int x_i = minX; x_i < maxX; x_i++){
				System.out.println(Integer.toBinaryString(whiteDotInBlack32Pixel.getRGB(x_i, y_i)));
	        	assertEquals(1, whiteDotInBlack32Pixel.getRGB(x_i, y_i) & 1);
	        }
	    }
	}
}
