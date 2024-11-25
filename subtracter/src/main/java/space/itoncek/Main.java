package space.itoncek;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException {
		File dir = new File("D:\\#projekty\\#sources\\meteority\\geminidy\\c");

		//DO NOT EDIT BELOW THIS LINE

		File avg = new File(dir.getAbsolutePath() + "\\average\\");
		File sub = new File(dir.getAbsolutePath() + "\\subtract\\");
		File target = new File(dir.getAbsolutePath() + "\\target\\");
		target.mkdirs();

		int[][][] avgImg = new int[0][0][0];
		int steps = 0;
		boolean first = true;
		System.out.println("Adding");
		for (File file : avg.listFiles()) {
			if (file.getName().endsWith(".png")) {
				BufferedImage read = ImageIO.read(file);
				if (first) {
					first = false;
					avgImg = new int[read.getWidth()][read.getHeight()][3];
				}

				for (int x = 0; x < read.getWidth(); x++) {
					for (int y = 0; y < read.getHeight(); y++) {
						avgImg[x][y][0] += new Color(read.getRGB(x, y)).getRed();
						avgImg[x][y][1] += new Color(read.getRGB(x, y)).getGreen();
						avgImg[x][y][2] += new Color(read.getRGB(x, y)).getBlue();
					}
				}

				steps++;
				System.out.println(steps + " / " + avg.listFiles().length + " done");
			}
		}


		System.out.println("Averaging");
		Color[][] avgImgDiv = new Color[avgImg.length][avgImg[0].length];
		for (int x = 0; x < avgImg.length; x++) {
			for (int y = 0; y < avgImg[0].length; y++) {
				avgImgDiv[x][y] = new Color(
						((float) avgImg[x][y][0] / steps) / 255f,
						((float) avgImg[x][y][0] / steps) / 255f,
						((float) avgImg[x][y][0] / steps) / 255f
				);
			}
		}

		System.out.println("Subtracting");
		for (File file : sub.listFiles()) {
			BufferedImage read = ImageIO.read(file);
			for (int x = 0; x < read.getWidth(); x++) {
				for (int y = 0; y < read.getHeight(); y++) {
					Color orig = new Color(read.getRGB(x, y));
					Color base = avgImgDiv[x][y];
					read.setRGB(x, y, subtract(orig, base));
				}
			}
			ImageIO.write(read, "png", new File(target + "/" + file.getName()));
		}
	}

	private static int subtract(Color orig, Color base) {
		return new Color(normalize(orig.getRed() - base.getRed()),
				normalize(orig.getGreen() - base.getGreen()),
				normalize(orig.getBlue() - base.getBlue())).getRGB();
	}

	private static int normalize(int i) {
		if (i < 0) return 0;
		else return Math.min(i, 255);
	}
}