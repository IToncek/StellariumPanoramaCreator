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
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import space.itoncek.lerper.Lerp;
import space.itoncek.lerper.Lerp5D;
import space.itoncek.lerper.Snapshot5D;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.LineMetrics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
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
import java.util.Stack;

import static java.lang.Thread.sleep;

public class Panoramator {
    public static ProgressBarBuilder pbb = new ProgressBarBuilder()
            .setTaskName("Lerpin'")
            .setMaxRenderedLength(150)
            .setUnit("images", 1)
            .setStyle(ProgressBarStyle.COLORFUL_UNICODE_BAR)
            .setSpeedUnit(ChronoUnit.SECONDS)
            .showSpeed()
            .clearDisplayOnFinish()
            .continuousUpdate();

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

    public static void main(String[] args) throws InterruptedException, IOException, FontFormatException, Lerp.LerpException {
//        command("StelMovementMgr.zoomTo(100,0)");
//        move(20,270);
//        command("core.moveToAltAzi(20., 270., 0.)");
//		action("actionToggle_GuiHidden_Global");
//
//		Runtime.getRuntime().addShutdownHook(new Thread(()->{
//			action("actionToggle_GuiHidden_Global");
//		}));

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

		//A
//        slideTo(src, target, LocalDateTime.of(2024, 1, 8, 6, 46),
//                LocalDateTime.of(2024, 1, 14, 17, 15, 00),
//                500,
//				new Snapshot3D(145.9676, 9.1711, 16),
//				new Snapshot3D(216.4507, 11.0995, 40));


		//B
//        slideTo(src, new File(target + "/b"), LocalDateTime.of(2024, 1, 14, 17, 15, 00),
//				LocalDateTime.of(2024,1,18,21,30,00),
//                500,
//				new Snapshot3D(216.4507, 11.0995, 40),
//				new Snapshot3D(242.8905,37.5064,20));
		//C
//        slideTo(src, new File(target + "/C"), LocalDateTime.of(2024,1,18,21,30,00),
//				LocalDateTime.of(2024,3,13,18,47,22),
//                500,
//				new Snapshot3D(242.8905,37.5064,20),
//				new Snapshot3D(255.4026,26.0740,50));
		//D
        slideTo(src, new File(target + "/d"), LocalDateTime.of(2024,3,13,18,47,22),
				LocalDateTime.of(2024,3,24,19,00,00),
                500,
				new Snapshot3D(255.4026,26.0740,50),
				new Snapshot3D(268.4509,20.3217,40));
    }

    public static void slideTo(File source, File target, LocalDateTime start, LocalDateTime end, long steps, Snapshot3D in, Snapshot3D out) throws IOException, InterruptedException, FontFormatException, Lerp.LerpException {
        target.mkdirs();
		long startDays = integerPart(julian(start));
		long endDays = integerPart(julian(end));

		double startHours = fractionalPart(julian(start));
		double endHours = fractionalPart(julian(end));

		try (ProgressBar pb = pbb.setInitialMax(steps+1).build()) {
            long begin = 318;
            long total = steps;
            pb.stepTo(begin);
            pb.maxHint(total);
			for (long step = begin; step <= total; step++) {
				Snapshot5D snapshot5D = Lerp5D.interpolateDirect(
						in.convertTo5D(start),
						out.convertTo5D(end),
						step / Double.valueOf(steps)
				);
                captureTimestamp(new File(target + "/timestamp/"), unJulian(snapshot5D.day() + snapshot5D.hour()), step);
                zoom(snapshot5D.fov());
				setJD(snapshot5D.day() + snapshot5D.hour());
				move(snapshot5D.alt(), snapshot5D.azi(), snapshot5D.fov());
				bareCapture(target, source, step + "");
                sleep(5);
				//pb.setExtraMessage(String.valueOf(step / Double.valueOf(steps)));
				pb.step();
			}
		}
	}

	public static void midpoint(LocalDateTime start, LocalDateTime end) throws IOException {
		long startDays = integerPart(julian(start));
		long endDays = integerPart(julian(end));

		double startHours = fractionalPart(julian(start));
		double endHours = fractionalPart(julian(end));

		Snapshot5D snapshot5D = Lerp5D.interpolateDirect(
				new Snapshot5D(145.9676, 9.1711, 16, startDays, startHours),
				new Snapshot5D(216.4507, 11.0995, 40, endDays, endHours),
				.5
		);
		setJD(snapshot5D.day() + snapshot5D.hour());
		//move(snapshot5D.alt(), snapshot5D.azi(), );
		zoom(snapshot5D.fov());
	}

    private static void captureTimestamp(File f, LocalDateTime t, long step) throws IOException, FontFormatException {
        f.mkdirs();
        BufferedImage image = new BufferedImage(580, 80, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setFont(fnt.deriveFont(50f));
        String st = t.format(DateTimeFormatter.ofPattern("dd.MM.YYYY HH:mm:ss"));
        LineMetrics lineMetrics = fnt.deriveFont(50f).getLineMetrics(st, graphics.getFontRenderContext());
        graphics.drawString(st, 10, 10 + lineMetrics.getHeight());
        ImageIO.write(image, "png", new File(f + "/" + step + ".png"));
        graphics.dispose();
    }

    public static void bareCapture(File dest, File in, String name) throws InterruptedException {
        sleep(100);
        action("actionSave_Screenshot_Global");
        sleep(100);
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

    public static void move(double alt, double az, double fov) {
        command(String.format("core.moveToAltAzi(%f, %f, 0.);\n" +
                "StelMovementMgr.zoomTo(%f,0);", alt, az, fov));
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
        long seconds = Math.round((julian - 38) * 86400);
        ZonedDateTime jul = ZonedDateTime.of(-4712, 1, 1, 12, 0, 0, 0, ZoneId.of("UTC"));
        return jul.toLocalDateTime().plus(seconds, ChronoUnit.SECONDS);
    }

	public static void setJD(double jd) throws IOException {
		setJD(jd,Speed.STOP);
	}
    public static void setJD(double jd , Speed rate) throws IOException {
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
            if(!new String(res.getEntity().getContent().readAllBytes()).equals("ok")) {
                System.out.println(new String(res.getEntity().getContent().readAllBytes()));
            }
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