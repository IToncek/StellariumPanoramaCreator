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

import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import me.tongfei.progressbar.ProgressBarStyle;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class Panoramator {
	public static ProgressBarBuilder pbb = new ProgressBarBuilder()
			.setTaskName("Capturing panorama")
			.setMaxRenderedLength(150)
			.setUnit("images",1)
			.setStyle(ProgressBarStyle.COLORFUL_UNICODE_BLOCK)
			.setSpeedUnit(ChronoUnit.SECONDS);

	public static Font fnt;

	static {
		try {
			fnt = Font.createFont(Font.TRUETYPE_FONT, new URL("https://cdn.itoncek.space/fonts/VCR_OSD_MONO-Regular.ttf").openStream());
		} catch (FontFormatException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) throws InterruptedException, IOException, FontFormatException {
		command("StelMovementMgr.zoomTo(100,0)");
		//move(20,270);
		command("core.moveToAltAzi(20., 270., 0.)");
		action("actionToggle_GuiHidden_Global");

		Runtime.getRuntime().addShutdownHook(new Thread(()->{
			action("actionToggle_GuiHidden_Global");
		}));

		File target = new File("C:\\Users\\user\\Pictures\\Stellarium\\output");
		File src = new File("C:\\Users\\user\\Pictures\\Stellarium\\input");
		target.mkdirs();
		src.mkdirs();
		for (File file : src.listFiles()) {
			file.delete();
		}

//		//Hide planets
//		for (Action action : List.of(Action.PLANETS)) {
//			action("actionShow_" + action.action);
//		}

//		capturePano(src,target,"test");
//
//		//Show planets
//		for (Action action : List.of(Action.PLANETS)) {
//			action("actionShow_" + action.action);
//		}
//
		slideTo(src,target,LocalDateTime.of(2024,1,8,7,12,53),
				LocalDateTime.of(2024,1,14,17,15),
				250);
	}

	public static void slideTo(File source, File fintrg, LocalDateTime start, LocalDateTime end, int steps) throws IOException, InterruptedException, FontFormatException {
		long startDays = integerPart(julian(start));
		long endDays = integerPart(julian(end));

		double startHours = fractionalPart(julian(start));
		double endHours = fractionalPart(julian(end));

		for (int step = 0; step <= steps; step++) {
			double ratio = EaseInOut((step + 0d)/steps);
			long days = Math.round(Math.floor(lerp(startDays,endDays,ratio)));
			double hours = lerp(startHours,endHours,ratio);

			setJD(days+hours);
//			sleep(50);
			captureTimestamp(new File(fintrg + "/timestamp/"), unJulian(days+hours), step);
			capturePano(source, fintrg, step + "");
		}
	}

	private static void captureTimestamp(File f, LocalDateTime t, int step) throws IOException, FontFormatException {
		f.mkdirs();
		BufferedImage image = new BufferedImage(650,70, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = image.createGraphics();
		graphics.setFont(fnt.deriveFont(50f));
		String st = t.format(DateTimeFormatter.ofPattern("dd.MM.YY - HH:mm:ss"));
		LineMetrics lineMetrics = fnt.deriveFont(40f).getLineMetrics(st, graphics.getFontRenderContext());
		graphics.drawString(st,10, 10+lineMetrics.getHeight());
		ImageIO.write(image,"png", new File(f + "/" + step + ".png"));
		graphics.dispose();
	}

	
	public static void capturePano(File source, File fintrg, String name) throws InterruptedException {
		File target = new File(fintrg.getAbsolutePath() + "\\" + name);
		target.mkdir();

		List<Rotation> rotations = Rotation.generateCubemapRotations();

		for (Rotation rotation : ProgressBar.wrap(rotations,pbb)) {
			capture(rotation.alt(),rotation.azi(),target,source, rotation.dir());
		}
		
		try (Scanner sc = new Scanner(new File("template.pto"))) {
			try (FileWriter fileWriter = new FileWriter(fintrg.getAbsolutePath() + "\\" + name + ".pto")) {
				while (sc.hasNextLine()) {
					String line = sc.nextLine();
					line = line.replace("%prf%", target.getAbsolutePath() + "\\");
					fileWriter.write(line + "\n");
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void capture(int alt, int az, File dest, File in, String name) throws InterruptedException {
		move(alt,az);
		sleep(50);
		action("actionSave_Screenshot_Global");
		sleep(50);
		int i = 0;
		
		for (File file : Objects.requireNonNull(in.listFiles())) {
			try {
				String suffix = "";
				if(i > 0) {
					suffix = "(" + i + ")";
				}
				Files.move(file.toPath(), Path.of(dest.getAbsolutePath() + "\\" + name + suffix + ".tif"));
				i++;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static void move(int alt, int az) {
		command(String.format("core.moveToAltAzi(%d., %d., 0.)", alt,az));
	}

	public static void command(String command) {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://192.168.99.64:8090/api/scripts/direct");

			// add header
			List<NameValuePair> urlParameters = new ArrayList<>();
			urlParameters.add(new BasicNameValuePair("code", command));
			urlParameters.add(new BasicNameValuePair("useIncludes", "false"));

			post.setEntity(new UrlEncodedFormEntity(urlParameters));
			post.setHeader("Content-Type", "application/x-www-form-urlencoded");
			post.setHeader("Origin", "http://localhost:8090");

			client.execute(post);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static double julian(LocalDateTime date) {
		ZonedDateTime d = date.atZone(ZoneId.of("Europe/Prague"));
		ZonedDateTime jul = ZonedDateTime.of(-4712, 1, 1, 12, 0, 0, 0, ZoneId.of("UTC"));
		long secs = ChronoUnit.SECONDS.between(jul, d);
		return (secs / 86400d) + 38;
	}

	public static LocalDateTime unJulian(double julian) {
		long seconds = Math.round((julian - 38)*86400);
		ZonedDateTime jul = ZonedDateTime.of(-4712, 1, 1, 12, 0, 0, 0, ZoneId.of("UTC"));
		return jul.toLocalDateTime().plus(seconds, ChronoUnit.SECONDS);
	}

	public static void setJD(double jd) throws IOException {
		//System.out.println(days);
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://192.168.99.64:8090/api/main/time");

		// add header
		List<NameValuePair> urlParameters = new ArrayList<>();
		urlParameters.add(new BasicNameValuePair("time", String.valueOf(jd)));
		urlParameters.add(new BasicNameValuePair("timerate", String.valueOf(Speed.STOP)));

		post.setEntity(new UrlEncodedFormEntity(urlParameters));
		post.setHeader("Content-Type", "application/x-www-form-urlencoded");
		post.setHeader("Origin", "http://localhost:8090");

		client.execute(post);
	}

	public static void date(LocalDateTime date, Speed rate) {
		try {
			//System.out.println(days);
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://192.168.99.64:8090/api/main/time");

			// add header
			List<NameValuePair> urlParameters = new ArrayList<>();
			urlParameters.add(new BasicNameValuePair("time", String.valueOf(julian(date))));
			urlParameters.add(new BasicNameValuePair("timerate", String.valueOf(rate.getSpeed())));

			post.setEntity(new UrlEncodedFormEntity(urlParameters));
			post.setHeader("Content-Type", "application/x-www-form-urlencoded");
			post.setHeader("Origin", "http://localhost:8090");

			client.execute(post);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void action(String command) {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://192.168.99.64:8090/api/stelaction/do");
			
			// add header
			List<NameValuePair> urlParameters = new ArrayList<>();
			urlParameters.add(new BasicNameValuePair("id", command));
			
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
			post.setHeader("Content-Type", "application/x-www-form-urlencoded");
			post.setHeader("Origin", "http://localhost:8090");
			
			client.execute(post);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


	private static long integerPart(double julian) {
		BigDecimal temp = new BigDecimal(julian);
		return temp.intValue();
	};

	private static double fractionalPart(double julian) {
		BigDecimal temp = new BigDecimal(julian);
		return temp.subtract(new BigDecimal(integerPart(julian))).doubleValue();
	}

	public static double EaseIn(double t)
	{
		return Math.pow(t,2);
	}

	static double Flip(double x)
	{
		return 1 - x;
	}

	public static double EaseOut(double t)
	{
		return Flip(Math.pow(Flip(t), 2));
	}

	public static double EaseInOut(double t)
	{
		return lerp(EaseIn(t), EaseOut(t), t);
	}

	// Precise method, which guarantees v = v1 when t = 1. This method is monotonic only when v0 * v1 < 0.
	// Lerping between same values might not produce the same value
	public static double lerp(double v0, double v1, double t) {
		return (1d - t) * v0 + t * v1;
	}
}