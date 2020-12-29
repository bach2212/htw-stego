package lsbStego;

import java.awt.image.BufferedImage;

public class UsefulMethods {
	
	public static String binaryConverter(String s) {
		  byte[] bytes = s.getBytes();
		  StringBuilder binary = new StringBuilder();
		  for (byte b : bytes)
		  {
		     int val = b;
		     System.out.println(Integer.toBinaryString(val));
		     for (int i = 0; i < 8; i++)
		     {
		        binary.append((val & 128) == 0 ? 0 : 1);
		        val <<= 1;
		        System.out.println(binary);
		        System.out.println(Integer.toBinaryString(val));
		     }
		     binary.append(' ');
		  }
		  System.out.println("'" + s + "' to binary: " + binary);
		  return binary.toString();
//		  System.out.println(Integer.toBinaryString(0b00001111 ^ 0b01010001));
	}
	public static void printRGBValues(BufferedImage img) {
		for(int y = 0; y < img.getHeight(); y++) {
			for(int x = 0; x < img.getWidth(); x++) {
				System.out.println(img.getRGB(x, y));
			}
		}
	}
	public static void compareRGBValues(BufferedImage i1, BufferedImage i2) {
		int i = 0;
		if(i1.getHeight() == i2.getHeight() && i1.getWidth() == i2.getWidth()) {
			for(int y = 0; y < i1.getHeight(); y++) {
				for(int x = 0; x < i1.getWidth(); x++) {
					if(i1.getRGB(x, y) != i2.getRGB(x, y)) {
						System.out.println(i1.getRGB(x, y)+" ~ "+ i2.getRGB(x, y) + " at (" + x + "," + y +")");
						i++;
					}
				}
			}
		}
		System.out.println("Number of differences: "+i);
	}
}
