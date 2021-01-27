package lsbStego;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ExtractorTest {
	static BufferedImage fourPixel;
	static BufferedImage sword, sword1, imbeddedSword;
	static BufferedImage fourPixelInSword, msgInSword;
	static String msg;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		fourPixel = Loader.loadImage("129481956_153559649792840_2118012743996484739_n.png");
		sword = Loader.loadImage("src/lsbStego/pngkit_pixel-sword-png_2202123.png");
		fourPixelInSword = Embedder.embedImage(sword, fourPixel);
		msg = Loader.loadText("src/text");
		sword1 = Loader.loadImage("src/lsbStego/pngkit_pixel-sword-png_2202123.png");
		msgInSword = Embedder.embedMessage(sword1, msg);
		
	}
	
	@Test
	void extractedImgShouldBeCorrect() {
		int minX = fourPixel.getMinX();
		int minY = fourPixel.getMinY();
		int maxX = fourPixel.getMinX() + fourPixel.getWidth();
		int maxY = fourPixel.getMinY()+ fourPixel.getHeight();
		BufferedImage extractedFourPixel = Extractor.extractImage(fourPixelInSword, 2, 2);
		assertEquals(minX, extractedFourPixel.getMinX()); 
	    assertEquals(minY, extractedFourPixel.getMinY()); 
	    for (int y_i = minY; y_i < maxY; y_i++) {
	    	for (int x_i = minX; x_i < maxX; x_i++){
	            assertEquals(fourPixel.getRGB(x_i, y_i), extractedFourPixel.getRGB(x_i, y_i));
	        }
	    }
	    
	}
	@Test 
	void negativeHeightAndWidthShouldBeDenied() {
		assertThrows(IllegalArgumentException.class,() -> Extractor.extractImage(fourPixelInSword, -1, -1));
		assertThrows(IllegalArgumentException.class,() -> Extractor.extractImage(fourPixelInSword, 2, -2));
		assertThrows(IllegalArgumentException.class,() -> Extractor.extractImage(fourPixelInSword, -2, 2));
	}
	
	@Test
	void extractedTextShouldBeCorrect() {
		String extractedMsg = Extractor.extractText(msgInSword, msg.length());
		assertEquals(msg,extractedMsg);
	}

}
