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

package me.itoncek;

import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import me.tongfei.progressbar.ProgressBarStyle;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static java.lang.Thread.sleep;

public class Panoramator {
	
	public static void main(String[] args) throws InterruptedException {
		command("StelMovementMgr.zoomTo(60,0)");
		command("core.moveToAltAzi(20., 270., 0.)");
		
		File target = new File("C:\\Users\\user\\Pictures\\Stellarium\\pano-zari");
		File src = new File("C:\\Users\\user\\Pictures\\Stellarium\\input");
		for (File file : Arrays.asList(target, src)) {
			file.delete();
			sleep(100);
			file.mkdir();
		}
		try (ProgressBar pbGlobal = new ProgressBarBuilder().setStyle(ProgressBarStyle.ASCII).setInitialMax(1).setUnit("panoramas", 1).setSpeedUnit(ChronoUnit.SECONDS).setMaxRenderedLength(200).build();
				ProgressBar pb = new ProgressBarBuilder().setStyle(ProgressBarStyle.COLORFUL_UNICODE_BLOCK).setInitialMax(56).setUnit("image", 1).setTaskName("Taking pictures").setSpeedUnit(ChronoUnit.SECONDS).showSpeed().setMaxRenderedLength(200).build()) {
			date(LocalDateTime.of(2023,9,15,20,0,0),Speed.STOP);
			capturePano( src, target, List.of(), "zapad-clear", pb);
			pbGlobal.step();
			capturePano( src, target, List.of(Action.LINES), "zapad-lines", pb);
			pbGlobal.step();
			capturePano( src, target, List.of(Action.ATMOSPHERE), "zapad-atmoclear", pb);
			pbGlobal.step();
			capturePano( src, target, List.of(Action.LINES, Action.ART), "zapad-art", pb);
			pbGlobal.step();
//			capturePano( src, target, List.of(Action.GROUND), "zapad-ng", pb);
//			pbGlobal.step();
//			capturePano( src, target, List.of(Action.GROUND,Action.ATMOSPHERE), "zapad-ng-na", pb);
//			pbGlobal.step();
//			capturePano( src, target, List.of(Action.GROUND, Action.LINES, Action.ART,Action.ATMOSPHERE), "zapad-ng-na-art", pb);
//			pbGlobal.step();
//			capturePano( src, target, List.of(Action.GROUND, Action.LINES, Action.ART), "zapad-ng-art", pb);
//			pbGlobal.step();
			date(LocalDateTime.of(2022, 2, 16, 3, 0, 0), Speed.STOP);
			capturePano( src, target, List.of(Action.ATMOSPHERE), "clear", pb);
			pbGlobal.step();
			capturePano( src, target, List.of(Action.LINES, Action.ATMOSPHERE), "lines", pb);
			pbGlobal.step();
			capturePano( src, target, List.of(Action.LINES, Action.ART, Action.ATMOSPHERE), "art", pb);
			pbGlobal.step();
			capturePano(src, target, List.of(Action.GROUND, Action.ATMOSPHERE), "no-ground", pb);
			pbGlobal.step();
		}
		
	}
	
	public static void capturePano(File source, File fintrg, List<Action> actions, String name, ProgressBar pb) throws InterruptedException {
		File target = new File(fintrg.getAbsolutePath() + "\\" + name);
		target.mkdir();
		pb.reset();
		List<Integer> rows = new ArrayList<>();
		rows.add(80);
		rows.add(60);
		rows.add(30);
		rows.add(0);
		rows.add(-30);
		rows.add(-60);
		rows.add(-80);
		
		for (Action action : actions) {
			action("actionShow_" + action.action);
		}
		
		command("core.moveToAltAzi(20., 270., 0.)");
		sleep(1000);
		
		for (Integer row : rows) {
			for (int i = 0; i < 8; i++) {
				int img = i * 45;
				capture(row, img, target, source);
				pb.step();
			}
		}
		
		for (Action action : actions) {
			action("actionShow_" + action.action);
		}
		
		try (Scanner sc = new Scanner(new File("template.pano"))) {
			try (FileWriter fileWriter = new FileWriter(fintrg.getAbsolutePath() + "\\" + name + ".pano")) {
				while (sc.hasNextLine()) {
					String line = sc.nextLine();
					line = line.replace("%replaceType%", name);
					line = line.replace("%replaceFolder%", target.getAbsolutePath() + "\\");
					line = line.replace("%replaceTargetFolder%", fintrg.getAbsolutePath() + "\\");
					fileWriter.write(line + "\n");
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		command("core.moveToAltAzi(20., 270., 0.)");
		sleep(1000);
	}
	
	public static void capture(int alt, int az, File dest, File in) throws InterruptedException {
		command("core.moveToAltAzi(" + alt + "., " + az + "., 0.)");
		sleep(50);
		action("actionSave_Screenshot_Global");
		sleep(100);
		int i = 0;
		
		for (File file : Objects.requireNonNull(in.listFiles())) {
			try {
				String suffix = "";
				if(i > 0) {
					suffix = "(" + i + ")";
				}
				Files.move(file.toPath(), Path.of(dest.getAbsolutePath() + "\\" + alt + "x" + az + suffix + ".tif"));
				i++;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
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
	
	public static void date(LocalDateTime date, Speed rate) {
		try {
			ZonedDateTime d = date.atZone(ZoneId.of("Europe/Prague"));
			ZonedDateTime jul = ZonedDateTime.of(-4712, 1, 1, 12, 0, 0, 0, ZoneId.of("UTC"));
			long secs = ChronoUnit.SECONDS.between(jul, d);
			double days = (secs / 86400d) + 38;
			
			//System.out.println(days);
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://192.168.99.64:8090/api/main/time");
			
			// add header
			List<NameValuePair> urlParameters = new ArrayList<>();
			urlParameters.add(new BasicNameValuePair("time", String.valueOf(days)));
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
	
}