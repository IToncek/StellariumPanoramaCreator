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

import static java.lang.Thread.sleep;
import me.tongfei.progressbar.ProgressBarBuilder;
import me.tongfei.progressbar.ProgressBarStyle;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import space.itoncek.lerper.Lerp;
import space.itoncek.lerper.Lerp3D;
import space.itoncek.lerper.Lerp5D;
import space.itoncek.lerper.Snapshot5D;

import javax.imageio.ImageIO;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.font.LineMetrics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Panoramator {
	public static ProgressBarBuilder pbb = new ProgressBarBuilder().setTaskName("Lerpin'").setMaxRenderedLength(150).setUnit("images", 1).setStyle(ProgressBarStyle.COLORFUL_UNICODE_BAR).setSpeedUnit(ChronoUnit.SECONDS).showSpeed().clearDisplayOnFinish().continuousUpdate();

	public static Font fnt;

	static {
		try {
			fnt = Font.createFont(Font.TRUETYPE_FONT, URI.create("https://cdn.itoncek.space/fonts/VCR_OSD_MONO-Regular.ttf").toURL().openStream());
		} catch (FontFormatException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) throws InterruptedException, IOException, FontFormatException, Lerp.LerpException {
//        command("StelMovementMgr.zoomTo(100,0)");
//        move(20,270);
//        command("core.moveToAltAzi(20., 270., 0.)");
//		action("actionToggle_GuiHidden_Global");
//
//		Runtime.getRuntime().addShutdownHook(new Thread(()->{
//			action("actionToggle_GuiHidden_Global");
//		}));
		HashMap<String, List<String>> superscripts = new HashMap<>();

		File target = new File("X:\\AC Prosinec\\footage\\");
		target.mkdirs();

		long steps = 500;

		ArrayList<String> commands = new ArrayList<>(setup());
		String sequence = "0000";
		commands.addAll(move(LocalDateTime.of(2024, 12, 15, 18, 30, 0), new Snapshot3D(276.5888, 28.4216, 72.8)));

		commands.addAll(showConstellations(true));
		commands.add(cheese("0000_constellations", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(showConstellations(false));
		commands.add(cheese("0000_clear", new File(target.getAbsolutePath() + "\\" + "stills")));

		superscripts.put("0000", commands.stream().toList());
		commands.clear();
		commands.addAll(setup());
//
//		//------------------------------------------------------------------------------------------------------------------------------------------------
//		sequence = "0001";
//		commands.addAll(slideTo(new File(target + "\\" + sequence + "_cl"), LocalDateTime.of(2024, 12, 15, 18, 30, 0), steps, new Snapshot3D(276.5888, 28.4216, 72.8), new Snapshot3D(307.4430, 16.0075, 37.4), null));
//		commands.addAll(showConstellations(true));
//		commands.addAll(slideTo(new File(target + "\\" + sequence + "_cn"), LocalDateTime.of(2024, 12, 15, 18, 30, 0), steps, new Snapshot3D(276.5888, 28.4216, 72.8), new Snapshot3D(307.4430, 16.0075, 37.4), null));
//		commands.addAll(showConstellations(false));
//
//		commands.addAll(move(LocalDateTime.of(2024, 12, 15, 18, 30, 0), new Snapshot3D(307.4430, 16.0075, 37.4)));
//
//		commands.add(cheese(sequence + "_clear", new File(target.getAbsolutePath() + "\\" + "stills")));
//		commands.addAll(showConstellations(true));
//		commands.add(cheese(sequence + "_constellations", new File(target.getAbsolutePath() + "\\" + "stills")));
//		commands.addAll(showConstellations(false));
//
//		superscripts.put(sequence, commands.stream().toList());
//		commands.clear();
//		commands.addAll(setup());
//
//		//------------------------------------------------------------------------------------------------------------------------------------------------
//		sequence = "0002";
//		commands.addAll(slideTo(new File(target + "\\" + sequence + "_cl"), LocalDateTime.of(2024, 12, 15, 18, 30, 0), steps, new Snapshot3D(307.4430, 16.0075, 37.4), new Snapshot3D(281.0042, 20.1287, 50), null));
//		commands.addAll(showConstellations(true));
//		commands.addAll(slideTo(new File(target + "\\" + sequence + "_cn"), LocalDateTime.of(2024, 12, 15, 18, 30, 0), steps, new Snapshot3D(307.4430, 16.0075, 37.4), new Snapshot3D(281.0042, 20.1287, 50), null));
//		commands.addAll(showConstellations(false));
//
//		commands.addAll(move(LocalDateTime.of(2024, 12, 15, 18, 30, 0), new Snapshot3D(281.0042, 20.1287, 50)));
//
//		commands.add(cheese(sequence + "_clear", new File(target.getAbsolutePath() + "\\" + "stills")));
//		commands.addAll(showConstellations(true));
//		commands.add(cheese(sequence + "_constellations", new File(target.getAbsolutePath() + "\\" + "stills")));
//		commands.addAll(showConstellations(false));
//
//		superscripts.put(sequence, commands.stream().toList());
//		commands.clear();
//		commands.addAll(setup());
//
//		//------------------------------------------------------------------------------------------------------------------------------------------------
		sequence = "0003";
//		commands.addAll(slideTo(new File(target + "\\" + sequence + "_cl"), LocalDateTime.of(2024, 12, 15, 18, 30, 0), steps, new Snapshot3D(281.0042, 20.1287, 50), new Snapshot3D(67.7101, 75.6493, 70), null));
//		commands.addAll(showConstellations(true));
//		commands.addAll(slideTo(new File(target + "\\" + sequence + "_cn"), LocalDateTime.of(2024, 12, 15, 18, 30, 0), steps, new Snapshot3D(281.0042, 20.1287, 50), new Snapshot3D(67.7101, 75.6493, 70), null));
//		commands.addAll(showConstellations(false));

		commands.addAll(move(LocalDateTime.of(2024, 12, 15, 18, 30, 0), new Snapshot3D(67.7101, 75.6493, 70)));
		commands.addAll(setup());
		commands.add(cheese(sequence + "_clear", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(showConstellations(true));
		commands.add(cheese(sequence + "_constellations", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(showConstellations(false));

		superscripts.put(sequence, commands.stream().toList());
		commands.clear();
		commands.addAll(setup());

		//------------------------------------------------------------------------------------------------------------------------------------------------
		sequence = "0004";
		commands.addAll(slideTo(new File(target + "\\" + sequence + "_cl"), LocalDateTime.of(2024, 12, 15, 18, 30, 0), steps, new Snapshot3D(67.7101, 75.6493, 70), new Snapshot3D(180.2523, 68.6574, 55), null));
		commands.addAll(showConstellations(true));
		commands.addAll(slideTo(new File(target + "\\" + sequence + "_cn"), LocalDateTime.of(2024, 12, 15, 18, 30, 0), steps, new Snapshot3D(67.7101, 75.6493, 70), new Snapshot3D(180.2523, 68.6574, 55), null));

		commands.addAll(move(LocalDateTime.of(2024, 12, 15, 18, 30, 0), new Snapshot3D(180.2523, 68.6574, 55)));

		commands.add(cheese(sequence + "_constellations", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(showConstellations(false));
		commands.add(cheese(sequence + "_clear", new File(target.getAbsolutePath() + "\\" + "stills")));

		superscripts.put(sequence, commands.stream().toList());

		commands.clear();
		commands.addAll(setup());

		//------------------------------------------------------------------------------------------------------------------------------------------------
		sequence = "0005";
		commands.addAll(slideTo(new File(target + "\\" + sequence + "_cl"), LocalDateTime.of(2024, 12, 15, 18, 30, 0), steps, new Snapshot3D(180.2523, 68.6574, 55), new Snapshot3D(181.1800, 43.1769, 70), null));
		commands.addAll(showConstellations(true));
		commands.addAll(showGround(false));
		commands.addAll(slideTo(new File(target + "\\" + sequence + "_cn"), LocalDateTime.of(2024, 12, 15, 18, 30, 0), steps, new Snapshot3D(180.2523, 68.6574, 55), new Snapshot3D(181.1800, 43.1769, 70), null));

		commands.addAll(move(LocalDateTime.of(2024, 12, 15, 18, 30, 0), new Snapshot3D(181.1800, 43.1769, 70)));

		commands.add(cheese(sequence + "_constellations", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(showConstellations(false));
		commands.addAll(showGround(true));
		commands.add(cheese(sequence + "_clear", new File(target.getAbsolutePath() + "\\" + "stills")));

		superscripts.put(sequence, commands.stream().toList());

		commands.clear();
		commands.addAll(setup());

		//------------------------------------------------------------------------------------------------------------------------------------------------
		sequence = "0006";
		commands.addAll(slideTo(new File(target + "\\" + sequence + "_cl"), LocalDateTime.of(2024, 12, 15, 18, 30, 0), LocalDateTime.of(2024, 12, 15, 23, 30, 0), steps, new Snapshot3D(181.1800, 43.1769, 70), new Snapshot3D(166.0080, 77.3050, 30), null));
		commands.addAll(showConstellations(true));
		commands.addAll(slideTo(new File(target + "\\" + sequence + "_cn"), LocalDateTime.of(2024, 12, 15, 18, 30, 0), LocalDateTime.of(2024, 12, 15, 23, 30, 0), steps, new Snapshot3D(181.1800, 43.1769, 70), new Snapshot3D(166.0080, 77.3050, 30), null));

		commands.addAll(move(LocalDateTime.of(2024, 12, 15, 23, 30, 0), new Snapshot3D(166.0080, 77.3050, 30)));

		commands.add(cheese(sequence + "_constellations", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(showConstellations(false));
		commands.add(cheese(sequence + "_clear", new File(target.getAbsolutePath() + "\\" + "stills")));

		superscripts.put(sequence, commands.stream().toList());

		commands.clear();
		commands.addAll(setup());

		//------------------------------------------------------------------------------------------------------------------------------------------------
		sequence = "0007";
		commands.addAll(slideTo(new File(target + "\\" + sequence + "_cl"), LocalDateTime.of(2024, 12, 15, 23, 30, 0), steps, new Snapshot3D(166.0080, 77.3050, 30), new Snapshot3D(128.2890, 49.2255, 40), null));
		commands.addAll(showConstellations(true));
		commands.addAll(slideTo(new File(target + "\\" + sequence + "_cn"), LocalDateTime.of(2024, 12, 15, 23, 30, 0), steps, new Snapshot3D(166.0080, 77.3050, 30), new Snapshot3D(128.2890, 49.2255, 40), null));

		commands.addAll(move(LocalDateTime.of(2024, 12, 15, 23, 30, 0), new Snapshot3D(128.2890, 49.2255, 40)));

		commands.add(cheese(sequence + "_constellations", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(showConstellations(false));
		commands.add(cheese(sequence + "_clear", new File(target.getAbsolutePath() + "\\" + "stills")));

		superscripts.put(sequence, commands.stream().toList());

		commands.clear();
		commands.addAll(setup());

		//------------------------------------------------------------------------------------------------------------------------------------------------


		superscripts.forEach((filename, cmds) -> {
			try (FileWriter fw = new FileWriter(filename + ".ssc")) {
				for (String command : cmds) {
					fw.write(command + "\n");
					System.out.println(filename + " >> " + command);
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
	}

	private static Collection<String> showGround(boolean enabled) {
		return List.of("LandscapeMgr.setFlagLandscape(%b)".formatted(enabled));
	}

	private static List<String> setup() {
		return List.of("core.setGuiVisible(false);", "core.setTimeRate(0);");
	}

	private static List<String> showConstellations(boolean enabled) {
		return List.of("ConstellationMgr.setFlagLines(%b);".formatted(enabled), "ConstellationMgr.setFlagArt(%b);".formatted(enabled), "core.wait(3);");
	}

//    private static boolean isScriptRunning() {
//        JSONObject o;
//        try(Scanner sc = new Scanner(new URL("http://192.168.99.64:8090/api/scripts/status").openStream())) {
//            StringJoiner js = new StringJoiner("\n");
//            while (sc.hasNextLine()) js.add(sc.nextLine());
//
//            o = new JSONObject(js.toString());
//            return o.getBoolean("scriptIsRunning");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

//    private static void moveAllCapturesToAFolder(File in,File dest) {
//        dest.mkdirs();
//        int i = 0;
//        for (File file : Objects.requireNonNull(in.listFiles())) {
//            try {
//                Files.move(file.toPath(), Path.of(dest.getAbsolutePath() + "\\" + i + ".tif"));
//                i++;
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }

	public static ArrayList<String> slideTo(File target, LocalDateTime start, long steps, Snapshot3D in, Snapshot3D out, Double midZoom) {
		ArrayList<String> output = new ArrayList<>();
		target.mkdirs();
		output.add(setTime(start));
		output.addAll(move(in.alt(), in.azi(), in.fov()));
		output.add("core.wait(3);");
		for (long step = 0; step <= steps; step++) {
			Snapshot3D snapshot5D;
			if (midZoom == null) {
				snapshot5D = Lerp3D.interpolateDirect(in, out, step / (double) steps);
			} else {
				snapshot5D = Lerp3D.interpolateMidzoom(in, out, step / (double) steps, midZoom);
			}
			//captureTimestamp(new File(target + "/timestamp/"), unJulian(snapshot5D.day() + snapshot5D.hour()), step);
			output.addAll(move(snapshot5D.alt(), snapshot5D.azi(), snapshot5D.fov()));
			output.add(cheese(step + "", target));
			output.add("core.wait(0.01);");
		}
		return output;
	}

	private static String setTime(LocalDateTime time) {
		return "core.setDate(\"%s\");".formatted(time.format(DateTimeFormatter.ISO_DATE_TIME));
	}

	public static ArrayList<String> slideTo(File target, LocalDateTime start, LocalDateTime end, long steps, Snapshot3D in, Snapshot3D out, Double midZoom) {
		ArrayList<String> output = new ArrayList<>();
		target.mkdirs();

		output.add(setTime(start));
		output.addAll(move(in.alt(), in.azi(), in.fov()));
		output.add("core.wait(3);");

		LocalDate startDays = start.toLocalDate();
		LocalDate endDays = end.toLocalDate();

		LocalTime startHours = start.toLocalTime();
		LocalTime endHours = end.toLocalTime();
		for (long step = 0; step <= steps; step++) {
			Snapshot5D snapshot5D;
			if (midZoom == null) {
				snapshot5D = Lerp5D.interpolateDirect(in.convertTo5D(startDays, startHours), out.convertTo5D(endDays, endHours), step / (double) steps);
			} else {
				snapshot5D = Lerp5D.interpolateMidzoom(in.convertTo5D(startDays, startHours), out.convertTo5D(endDays, endHours), step / (double) steps, midZoom);
			}
			//captureTimestamp(new File(target + "/timestamp/"), unJulian(snapshot5D.day() + snapshot5D.hour()), step);
			output.addAll(move(LocalDateTime.of(snapshot5D.day(), snapshot5D.hour()), snapshot5D.stripTime()));
			output.add(cheese(step + "", target));
			output.add("core.wait(0.01);");
		}
		return output;
	}

	public static String cheese(String filename, File target) {
		if (!target.exists()) target.mkdirs();
		return "core.screenshot(\"%s\",false,\"%s\",true)".formatted(filename, target.getAbsolutePath().replace("\\", "\\\\"));
	}

//	public static void midpoint(LocalDateTime start, LocalDateTime end) throws IOException {
//		long startDays = integerPart(julian(start));
//		long endDays = integerPart(julian(end));
//
//		double startHours = fractionalPart(julian(start));
//		double endHours = fractionalPart(julian(end));
//
//		Snapshot5D snapshot5D = Lerp5D.interpolateDirect(
//				new Snapshot5D(145.9676, 9.1711, 16, startDays, startHours),
//				new Snapshot5D(216.4507, 11.0995, 40, endDays, endHours),
//				.5
//		);
//		setJD(snapshot5D.day() + snapshot5D.hour());
//		//move(snapshot5D.alt(), snapshot5D.azi(), );
//		zoom(snapshot5D.fov());
//	}

	private static void captureTimestamp(File f, LocalDateTime t, long step) throws IOException, FontFormatException {
		f.mkdirs();
		BufferedImage image = new BufferedImage(580, 80, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = image.createGraphics();
		graphics.setFont(fnt.deriveFont(50f));
		String st = t.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
		LineMetrics lineMetrics = fnt.deriveFont(50f).getLineMetrics(st, graphics.getFontRenderContext());
		graphics.drawString(st, 10, 10 + lineMetrics.getHeight());
		ImageIO.write(image, "png", new File(f + "/" + step + ".png"));
		graphics.dispose();
	}

	public static void bareCapture(File dest, File in, String name) {
		action("actionSave_Screenshot_Global");
		long i = 0;

		for (File file : Objects.requireNonNull(in.listFiles())) {
			try {
				String suffix = "";
				if (i > 0) {
					suffix = "(" + i + ")";
				}
				Files.move(file.toPath(), Path.of(dest.getAbsolutePath() + "\\" + name + suffix + ".tif"));
				i++;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static void capture(long alt, long az, File dest, File in, String name) throws InterruptedException {
		//move(alt, az, );
		sleep(50);
		action("actionSave_Screenshot_Global");
		sleep(50);
		long i = 0;

		for (File file : Objects.requireNonNull(in.listFiles())) {
			try {
				String suffix = "";
				if (i > 0) {
					suffix = "(" + i + ")";
				}
				Files.move(file.toPath(), Path.of(dest.getAbsolutePath() + "\\" + name + suffix + ".tif"));
				i++;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static ArrayList<String> move(double alt, double az, double fov) {
		ArrayList<String> out = new ArrayList<>();
		out.add("core.moveToAltAzi(%f, %f, 0.);".formatted(alt, az));
		out.add("StelMovementMgr.zoomTo(%f,0);".formatted(fov));
		return out;
	}

	public static ArrayList<String> move(LocalDateTime time, Snapshot3D orientation) {
		ArrayList<String> out = new ArrayList<>();
		out.add("core.moveToAltAzi(%f, %f, 0.);".formatted(orientation.alt(), orientation.azi()));
		out.add("StelMovementMgr.zoomTo(%f,0);".formatted(orientation.fov()));
		out.add("core.setDate(\"%s\");".formatted(time.format(DateTimeFormatter.ISO_DATE_TIME)));
		return out;
	}

	public static void zoom(double fov) {
		command("StelMovementMgr.zoomTo(%f,0)".formatted(fov));
	}

	public static void command(String command) {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://192.168.99.64:8090/api/scripts/direct");

			// add header
			List<NameValuePair> urlParameters = new ArrayList<>();
			urlParameters.add(new BasicNameValuePair("code", command));
			urlParameters.add(new BasicNameValuePair("useIncludes", "true"));

			post.setEntity(new UrlEncodedFormEntity(urlParameters));
			post.setHeader("Content-Type", "application/x-www-form-urlencoded");
			post.setHeader("Origin", "http://localhost:8090");

			HttpResponse execute = client.execute(post);

			//System.out.println(readInputStream(execute.getEntity().getContent()));

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static String readInputStream(InputStream content) {
		StringJoiner js = new StringJoiner("\n");

		try (Scanner sc = new Scanner(content)) {
			while (sc.hasNextLine()) js.add(sc.nextLine());
		}

		return js.toString();
	}

	public static double julian(LocalDateTime date) {
		ZonedDateTime d = date.atZone(ZoneId.of("Europe/Prague"));
		ZonedDateTime jul = ZonedDateTime.of(-4712, 1, 1, 12, 0, 0, 0, ZoneId.of("UTC"));
		long secs = ChronoUnit.SECONDS.between(jul, d);
		return (secs / 86400d) + 38;
	}

	public static LocalDateTime unJulian(double julian) {
		long seconds = Math.round((julian - 38) * 86400);
		ZonedDateTime jul = ZonedDateTime.of(-4712, 1, 1, 12, 0, 0, 0, ZoneId.of("UTC"));
		return jul.toLocalDateTime().plus(seconds, ChronoUnit.SECONDS);
	}

	public static void setJD(double jd) throws IOException {
		setJD(jd, Speed.STOP);
	}

	public static void setJD(double jd, Speed rate) throws IOException {
		//System.out.println(days);
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://192.168.99.64:8090/api/main/time");

		// add header
		List<NameValuePair> urlParameters = new ArrayList<>();
		urlParameters.add(new BasicNameValuePair("time", String.valueOf(jd)));
		urlParameters.add(new BasicNameValuePair("timerate", String.valueOf(rate.getSpeed())));

		post.setEntity(new UrlEncodedFormEntity(urlParameters));
		post.setHeader("Content-Type", "application/x-www-form-urlencoded");
		post.setHeader("Origin", "http://localhost:8090");

		client.execute(post);
	}

	public static void date(LocalDateTime date, Speed rate) {
		try {
			setJD(julian(date), rate);
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

			HttpResponse res = client.execute(post);
//            if(!new String(res.getEntity().getContent().readAllBytes()).equals("ok")) {
//                System.out.println(new String(res.getEntity().getContent().readAllBytes()));
//            }
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


	public static long integerPart(double julian) {
		BigDecimal temp = new BigDecimal(julian);
		return temp.intValue();
	}

	;

	public static double fractionalPart(double julian) {
		BigDecimal temp = new BigDecimal(julian);
		return temp.subtract(new BigDecimal(integerPart(julian))).doubleValue();
	}

	public static double EaseIn(double t) {
		return Math.pow(t, 2);
	}

	static double Flip(double x) {
		return 1 - x;
	}

	public static double EaseOut(double t) {
		return Flip(Math.pow(Flip(t), 2));
	}

	public static double EaseInOut(double t) {
		return lerp(EaseIn(t), EaseOut(t), t);
	}

	// Precise method, which guarantees v = v1 when t = 1. This method is monotonic only when v0 * v1 < 0.
	// Lerping between same values might not produce the same value
	public static double lerp(double v0, double v1, double t) {
		return (1d - t) * v0 + t * v1;
	}


}