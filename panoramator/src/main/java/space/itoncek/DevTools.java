/*
 * Copyright (c) 2023 - IToncek
 *
 * All rights to modifying this source code are granted, except for changing licence.
 * Any and all products generated from this source code must be shared with a link
 * to the original creator with clear and well-defined mention of the original creator.
 * This applies to any lower level copies, that are doing approximately the same thing.
 * If you are not sure, if your usage is within these boundaries, please contact the
 * author on their public email address.
 */

package space.itoncek;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class DevTools {
    public static void main(String[] args) throws Exception {
        subtract();
//		while (true) {
//			System.out.println(MouseInfo.getPointerInfo().getLocation().x + " x " + MouseInfo.getPointerInfo().getLocation().y);
//			sleep(1000);
//		}
//
//		r = new Robot();
//
//		move(250, 123);
//		r.delay(5000);
//
//		move(86, 123);
//		int i = 1;
//		for (File file : new File("C:\\Users\\user\\Pictures\\digiCamControl\\landing\\downscale").listFiles()) {
//			BufferedImage img = ImageIO.read(file);
//			BufferedImage two = new BufferedImage(img.getWidth()/2, img.getHeight()/2, BufferedImage.TYPE_INT_RGB);
//			for (int y = 0; y < two.getHeight(); y++) {
//				for (int x = 0; x < two.getWidth(); x++) {
//					int p1 = img.getRGB(x*2,y*2);
//					int p2 = img.getRGB(x*2+1,y*2);
//					int p3 = img.getRGB(x*2,y*2+1);
//					int p4 = img.getRGB(x*2+1,y*2+1);
//
//					two.setRGB(x,y,average(List.of(p1,p2,p3,p4)));
//				}
//			}
//			BufferedImage three = new BufferedImage(img.getWidth()/3, img.getHeight()/3, BufferedImage.TYPE_INT_RGB);
//			for (int y = 0; y < three.getHeight(); y++) {
//				for (int x = 0; x < three.getWidth(); x++) {
//					int p1 = img.getRGB(x*3,y*3);
//					int p2 = img.getRGB(x*3,y*3+1);
//					int p3 = img.getRGB(x*3,y*3+2);
//					int p4 = img.getRGB(x*3+1,y*3);
//					int p5 = img.getRGB(x*3+1,y*3+1);
//					int p6 = img.getRGB(x*3+1,y*3+2);
//					int p7 = img.getRGB(x*3+2,y*3);
//					int p8 = img.getRGB(x*3+2,y*3+1);
//					int p9 = img.getRGB(x*3+2,y*3+2);
//
//					three.setRGB(x,y,average(List.of(p1,p2,p3,p4,p5,p6,p7,p8,p9)));
//				}
//			}
//			ImageIO.write(two,"png", new File("C:\\Users\\user\\Pictures\\digiCamControl\\landing\\downscale-binned\\2x2 - " + i + ".png"));
//			ImageIO.write(three,"png", new File("C:\\Users\\user\\Pictures\\digiCamControl\\landing\\downscale-binned\\3x3 - " + i + ".png"));
//			i++;
//		}
    }

    public static void subtract() throws IOException {
        BufferedImage orig = ImageIO.read(new File("D:\\#astro\\astrometry\\jakub.jpeg"));
        BufferedImage anot = ImageIO.read(new File("D:\\#astro\\astrometry\\jakub-ngc.png"));
        BufferedImage fin = new BufferedImage(orig.getWidth(), orig.getHeight(), orig.getType());

        for (int y = 0; y < orig.getHeight(); y++) {
            for (int x = 0; x < orig.getWidth(); x++) {
                Color origC = new Color(orig.getRGB(x, y));
                Color anotC = new Color(anot.getRGB(x, y));
                int rdif = Math.abs(anotC.getRed() - origC.getRed()) % 255;
                int gdif = Math.abs(anotC.getGreen() - origC.getGreen()) % 255;
                int bdif = Math.abs(anotC.getBlue() - origC.getBlue()) % 255;
                try {
                    fin.setRGB(x, y, new Color(rdif, gdif, bdif).getRGB());
                } catch (IllegalArgumentException e) {
                    System.out.print(rdif);
                    System.out.print(gdif);
                    System.out.print(bdif);
                }
            }
        }
        ImageIO.write(fin, "png", new File("D:\\#astro\\astrometry\\jakub-ngcdiff.png"));
    }

    public static int average(List<Integer> colors) {
        int totalR = 0;
        int totalG = 0;
        int totalB = 0;
        int images = 0;

        for (Integer color : colors) {
            Color c = new Color(color);
            totalR += c.getRed();
            totalG += c.getGreen();
            totalB += c.getBlue();
            images++;
        }

        return new Color(totalR / images, totalG / images, totalB / images).getRGB();
    }
}
