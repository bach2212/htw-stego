/*
 * Copyright 2020 Vinh Bach Nguyen
 * This file is part of HTWLSBStego.

    HTWLSBStego is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    HTWLSBStego is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with HTWLSBStego.  If not, see <https://www.gnu.org/licenses/>.
 */
package lsbStego;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import java.util.Random;
//import java.util.stream.Collectors;
//import java.util.stream.IntStream;

import javax.imageio.ImageIO;
import lsbStego.Loader;
import lsbStego.UsefulMethods;

public class Extractor {
	
	public static String extractText(BufferedImage stego, int msgLen) {
		char[] msg = new char[msgLen];
		int x = 0, y = 0;
		if(msgLen <= 0 || msgLen > stego.getWidth()* stego.getHeight()/8) msgLen = stego.getWidth()* stego.getHeight()/8;
		for(int i=0; i<msgLen; i++) {
			int character = 0;
			int lastBit = 0;
			for(int j= 0; j<8; j++) {
//				if(x < stego.getWidth()) {
//					lastBit = stego.getRGB(x, y) & 1;
//					x++;
//				}
//				else {
//					x = 0;
//					y++;
//					lastBit = stego.getRGB(x, y) & 1;
//				}
				if(x >= stego.getWidth()) y++;
				x = x % stego.getWidth();
				lastBit = stego.getRGB(x, y) & 1;
				x++;
				
				if(lastBit == 1) {
					character <<= 1;
					character = character | 1;
				}
				else character <<= 1;
			}
			msg[i] = (char) character;
		}
		return new String(msg);
	}
	
	public static BufferedImage extractImage(BufferedImage stego, int width, int height) {
		if(width *height <= 0) throw new IllegalArgumentException("Die Breite und die Höhe müssen positive Zahlen sein!");
		if(width * height > stego.getHeight() * stego.getWidth() / 32) {
			height = stego.getHeight() * stego.getWidth() / (32 * width);
			System.out.println("you may not be able to retrieve message without reasonable parameters!");
		}
		int msgLen = width * height;
		System.out.println("Message's length: " + msgLen);
		int x = 0, y = 0, bit = 0;
		BufferedImage msg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		int[] msgRGB = new int[msgLen];
		for(int i = 0; i < msgLen; i++ ) {
			int pixel = 0;
			for(int j = 0; j < 32; j++) {
				if(x >= stego.getWidth()) y++;
				x = x % stego.getWidth();
				bit = stego.getRGB(x, y) & 1;
				x++;
				if(bit == 1) {
					pixel >>>= 1;
					pixel = pixel | 0x80000000; //10000000000000000000000000000000
				}
				else {
					pixel >>>= 1;
//					bit = bit & 0x7FFFFFFF; //1111111111111111111111111111111
				}
			}
			msgRGB[i] = pixel;
		}
		int i = 0; 
		while(i < msgLen) {
			for(y = 0; y < height; y++) {
				for(x = 0; x < width; x++) {
					msg.setRGB(x, y, msgRGB[i]);
					i++;
				}
			}
		}
		try {
			File outputfile = new File("imageExtract.png");	
			ImageIO.write(msg, "png", outputfile);	
		} catch (IOException e) {
			
		}
		return msg;
	}
	
	public static void main(String[] args) {
//		int test = 0xff;
//		Color c = new Color(0,0,0);
//		BufferedImage steg = Loader.loadImage("stego.png");
//		Loader.showImage(steg);
		System.out.println(System.currentTimeMillis());
		BufferedImage imgSteg = Loader.loadImage("imageStego.png");
//		System.out.println(extractText(steg, 318));
		System.out.println("mess is:"+extractText(Loader.loadImage("stego.png"), 318));
		BufferedImage extractedImg = extractImage(imgSteg,171, 341);
//		BufferedImage extractedImg = extractImage(imgSteg, 2, 2);
//		System.out.println("rgb in integer: " +extractedImg.getRGB(0, 1));
//		System.out.println("rgb in binary: " + Integer.toBinaryString(extractedImg.getRGB(0, 1)));
		Loader.showImage(extractedImg);
		BufferedImage original = Loader.loadImage("src/lsbStego/pngkit_pixel-sword-png_2202123.png");
		UsefulMethods.compareRGBValues(extractedImg, original);
//		long seed = System.currentTimeMillis();
//		BufferedImage scramble = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_ARGB);
//		Integer[] pixels = new Integer[original.getWidth()* original.getHeight()];
//		int i = 0; 
//		for(int y = 0; y < original.getHeight(); y++) {
//			for(int x = 0; x < original.getWidth(); x++) {
//				if(i < pixels.length) {
//					pixels[i] = original.getRGB(x, y);
//					System.out.println(pixels[i]);
//					i++;
//				}
//			}
//		}
//		System.out.println("\n--------------------------------------\n");
//		i = 0;
//		List<Integer> pixel = Arrays.asList(pixels);
//		Collections.shuffle(pixel, new Random(seed));
//		while(i < original.getWidth()* original.getHeight()) {
//			for(int y = 0; y < original.getHeight(); y++) {
//				for(int x = 0; x < original.getWidth(); x++) {
//					System.out.println(pixel.get(i));
//					scramble.setRGB(x, y, pixel.get(i));
//					i++;
//				}
//			}
//		}
//		System.out.println("\n--------------------------------------\n");

//		Loader.showImage(scramble);
//		try {
//			File outputfile = new File("scramble.png");	
//			ImageIO.write(scramble, "png", outputfile);	
//		} catch (IOException e) {
//			
//		}
//		BufferedImage unscramble = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_ARGB);
//		List<Integer> mapping = IntStream.range(0, pixel.size()).boxed().collect(Collectors.toList());
//		Collections.shuffle(mapping, new Random(seed));
//		Integer[] out = new Integer[pixel.size()];
//		for(i = 0; i < out.length; i++) {
//		    out[mapping.get(i)] = pixel.get(i);
//		    System.out.println(mapping.get(i) + ": " + out[mapping.get(i)]);
//		}
//		i = 0;	
//		System.out.println("\n--------------------------------------\n");
//
//		while(i < original.getWidth()* original.getHeight()) {
//			for(int y = 0; y < original.getHeight(); y++) {
//				for(int x = 0; x < original.getWidth(); x++) {
//					unscramble.setRGB(x, y, out[i]);
//					System.out.println(out[i]);
//					i++;
//				}
//			}
//		}
//		Loader.showImage(unscramble);
//		try {
//			File outputfile = new File("unscramble.png");	
//			ImageIO.write(unscramble, "png", outputfile);	
//		} catch (IOException e) {
//			
//		}
	}
}
