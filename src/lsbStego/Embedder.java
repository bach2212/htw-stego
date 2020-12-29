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

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import lsbStego.Loader;


public class Embedder {
	
	public static BufferedImage embedMessage(BufferedImage cover, String msg) {
		int x = 0, y = 0;
		int capacity;
		if(msg.length() > cover.getWidth()* cover.getHeight()/8) capacity = cover.getWidth()* cover.getHeight()/8;
		else capacity = msg.length();
		for(int i=0; i<capacity; i++) {
			int character = (int)msg.charAt(i);
			for(int j= 0; j<8; j++) {
				int bitToHide = (character & 128)== 0 ? 0 : 1; // or 0x80 or 0b10000000
				if(bitToHide == 1) {
					if(x < cover.getWidth()) {
						cover.setRGB(x, y, cover.getRGB(x, y)|bitToHide);
						x++;
					}
					else {
						x = 0;
						y++;
						cover.setRGB(x, y, cover.getRGB(x, y)|bitToHide);
					}
				}
				else {
					if(x < cover.getWidth()) {
						cover.setRGB(x, y, cover.getRGB(x, y)&0xFFFFFFFE);
						x++;
					}
					else {
						x = 0;
						y++;
						cover.setRGB(x, y, cover.getRGB(x, y)&0xFFFFFFFE);
					}
				}
				character <<= 1;
			}
		}
		File output = new File("stego.png");
		try {
			ImageIO.write(cover, "png", output);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Failed to write image to file");
		}
		return cover;
	}
	
	public static BufferedImage embedTextFile(BufferedImage cover, String FileName) throws IOException {
		return embedMessage(cover, Loader.loadText(FileName));
	}
	
	public static BufferedImage embedImage(BufferedImage cover, BufferedImage msg) {
		int x = 0, y = 0, i = 0;
		int msgLen;
		if(msg.getHeight()* msg.getWidth() > cover.getHeight()*cover.getWidth()/32) msgLen = cover.getHeight()*cover.getWidth()/32;
		else msgLen = msg.getHeight() * msg.getWidth();
		System.out.println("Length of message: "+msg.getHeight() * msg.getWidth());
		System.out.println("capacity: "+ cover.getHeight()*cover.getWidth()/32);
		System.out.println("adjusted height in case LOM exceeds capacity: "+ cover.getHeight()*cover.getWidth()/32/msg.getWidth());
		System.out.println(msgLen);
		int[] msgRGB = new int[msgLen];
//		for(int i = 0; i < msgLen; i++) {
//			if(x < msg.getWidth()) {
//				msgRGB[i] = msg.getRGB(x, y);
//				System.out.println("check"+"("+x+","+y+")"+": "+Integer.toBinaryString(msgRGB[i]));
//				x++;
//			}
//			else {
//				x = 0;
//				y++;
//				msgRGB[i] = msg.getRGB(x, y);
//				System.out.println("check"+"("+x+","+y+")"+": "+Integer.toBinaryString(msgRGB[i]));
//				x++;
//			}			
//		}
		i = 0;
		for(y = 0; y < msgLen/msg.getWidth(); y++) {
			for(x = 0; x < msg.getWidth(); x++) {
				if(i < msgLen) {
					msgRGB[i] = msg.getRGB(x, y);
					i++;
				}
			}
		}
		x = 0; 
		y = 0;
		for(i = 0; i < msgLen; i++) {
			for(int j = 0; j < 32; j++) { // 8 bits for transparency, 8x3 for rgb
				int bitToHide = msgRGB[i] & 1; //get the last bit 
				if(x >= cover.getWidth()) y++;
				x = x % cover.getWidth();
				if(bitToHide == 1) {
					cover.setRGB(x, y, cover.getRGB(x, y)|bitToHide);
//					if(x < cover.getWidth()) {
//					}
//					else {
//						x = 0;
//						y++;
//						cover.setRGB(x, y, cover.getRGB(x, y)|bitToHide);
//					}
				}
				else {
					cover.setRGB(x, y, cover.getRGB(x, y)& (~1));//0b11111111111111111111111111111110
//					if(x < cover.getWidth()) {
//					}
//					else {
//						x = 0;
//						y++;
//						cover.setRGB(x, y, cover.getRGB(x, y)& (~1));
//					}
				}
				x++;
				msgRGB[i] >>>= 1;
			}
		}
		File output = new File("imageStego.png");
		try {
			ImageIO.write(cover, "png", output);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Failed to write image to file");
		}
		return cover;
	}
	public static void embedBlackPixel(BufferedImage cover, String msg) {
		int x = 0, y = 0;
		Color c = new Color(0,0,0);
		for(int i=0; i<msg.length(); i++) {
			int character = (int)msg.charAt(i);
			for(int j= 0; j<8; j++) {
				int bitToHide = (character & 128)== 0 ? 0 : 1; // or 0x80 or 0b10000000
				if(bitToHide == 1) {
					if(x < cover.getWidth()) {
						cover.setRGB(x, y,c.getRGB());
						x++;
					}
					else {
						x = 0;
						y++;
						cover.setRGB(x, y, c.getRGB());
					}
				}
				else {
					if(x < cover.getWidth()) {
						cover.setRGB(x, y, c.getRGB());
						x++;
					}
					else {
						x = 0;
						y++;
						cover.setRGB(x, y, c.getRGB());
					}
				}
				character <<= 1;
			}
		}
		File output = new File("src/lsbStego/stego.png");
		try {
			ImageIO.write(cover, "png", output);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Failed to write image to file");
		}
		Loader.showImage(cover);
	}
	
	public static void main(String[] args) throws IOException {
//		System.out.println(Integer.toBinaryString(0xFFFFFFFE));
//		String msg = "wertz";
//		int character = (int)msg.charAt(2);
//		System.out.println(Integer.toBinaryString(character));
//		System.out.println("result:"+Integer.toBinaryString(((character <<= 1)&128)));
//		System.out.println(Integer.toBinaryString(character));
//		int copy = character;
//		for(int j= 0; j<8; j++) {
//			int bitToHide = (copy & 128)== 0 ? 0 : 1;
//			System.out.println(bitToHide);
//			copy <<= 1;
//		}
//		Color c = new Color(255,0,0);
		BufferedImage fourPixel = Loader.loadImage("129481956_153559649792840_2118012743996484739_n.png");
		BufferedImage none = Loader.loadImage("src/lsbStego/pngkit_pixel-sword-png_2202123.png");
		System.out.println(none.getHeight());
		System.out.println(none.getWidth());
		BufferedImage cover = Loader.loadImage("src/lsbStego/Screenshot from 2020-11-22 00-15-15.png");
		System.out.println("cover size: "+cover.getHeight()* cover.getWidth());
		BufferedImage img = Loader.loadImage("src/lsbStego/Screenshot from 2020-11-22 00-15-15.png");
		System.out.println("rgb in integer: " +none.getRGB(1, 0));
		System.out.println("rgb in binary: " + Integer.toBinaryString(none.getRGB(1, 0)));
//		for(int i = 0; i < 10; i++) {
//			none.setRGB(i, i, c.getRGB());
//		}
//		Loader.showImage(none);
		System.out.println("rgb in integer: " +none.getRGB(0, 0));
		System.out.println("rgb in binary: " + Integer.toBinaryString(none.getRGB(0, 0)));
		String s = "Steganography is the science that involves communicating secret data in an appropriate multimedia "
				+ "carrier, e.g., image, audio, and video files. It comes under the assumption that if the feature is "
				+ "visible, the point of attack is evident, thus the goal here is always to conceal the very existence"
				+ " of the embedded data.";
		System.out.println(s.length());
		System.out.println(img.getHeight());
		System.out.println(img.getWidth());
		System.out.println((double)img.getHeight()/ img.getWidth());
		System.out.println(img.getHeight()* img.getWidth());
		System.out.println(img.getHeight()* img.getWidth()/ s.length());
		System.out.println(318*1630);
		System.out.println((int)Math.sqrt(1630/ 0.5625));
		System.out.println(1630/ 53);
		System.out.println((int)(53* 0.5625));
		System.out.println(53*29+ " " + 53*30);
		System.out.println((int)(540/30));
		System.out.println((int)960/53);
		System.out.println(18 *18);
		System.out.println("----------------------------------------------------");
//		embedMessage(cover, s);
		embedImage(cover, none);
	}
}
