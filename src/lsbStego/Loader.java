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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Loader {
	public static BufferedImage loadImage(String carrier) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(carrier));
		}
		catch(IOException e) {
			System.out.println("invalid File");
		}
		return img;
	}
	
	public static void showImage(BufferedImage img) {
		try {
			JFrame frame = new JFrame();
			ImageIcon icon = new ImageIcon(img);
			JLabel label = new JLabel(icon);
			frame.add(label);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.pack();
			frame.setVisible(true);
		}
		catch(NullPointerException npe) {
			System.out.println("File does not exist");
		}
	}
	
	public static String loadText(String FileName) throws IOException {
		String text = "";
		FileReader fr = new FileReader(FileName);
		BufferedReader br = new BufferedReader(fr);
		try {
			String s;
			while((s = br.readLine()) != null) {
				text+=s;	
			}
		} catch (FileNotFoundException e) {
			System.out.println("no such file to be found");
			e.printStackTrace();
		} finally {
			br.close();
		}
		return text;
	}
}
