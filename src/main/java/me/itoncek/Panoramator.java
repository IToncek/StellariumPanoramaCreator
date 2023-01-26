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

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.awt.*;
import java.awt.event.InputEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.lang.Thread.sleep;

public class Panoramator {
	public static Robot r;
	
	public static void main(String[] args) throws AWTException, InterruptedException {
		command("StelMovementMgr.zoomTo(60,0)");
		r = new Robot();
		r.setAutoDelay(10);
		
		move(93, 12);
		click();
		
		List<Integer> rows = new ArrayList<>();
		rows.add(80);
		rows.add(60);
		rows.add(30);
		rows.add(0);
		rows.add(-30);
		rows.add(-60);
		rows.add(-80);
		
		File clear = new File("C:\\Users\\user\\Pictures\\Stellarium\\panounor-zapad\\clear");
		File constellations = new File("C:\\Users\\user\\Pictures\\Stellarium\\panounor-zapad\\constellations");
		File atmoclear = new File("C:\\Users\\user\\Pictures\\Stellarium\\panounor-zapad\\atmoclear");
		File src = new File("C:\\Users\\user\\Pictures\\Stellarium\\panounor-zapad\\in");
		
		for (File file : Arrays.asList(clear, constellations, atmoclear, src)) {
			file.delete();
			file.mkdir();
		}
		
		for (Integer row : rows) {
			for (int i = 0; i < 8; i++) {
				int img = i * 45;
				System.out.println("Screenshotting image at azimuth " + img + " and row " + row);
				capture(row, img, clear, src);
			}
		}
		
		action("actionShow_Constellation_Lines");
		
		sleep(5000);
		
		for (Integer row : rows) {
			for (int i = 0; i < 8; i++) {
				int img = i * 45;
				System.out.println("Screenshotting image at azimuth " + img + " and row " + row);
				capture(row, img, constellations, src);
			}
		}
		
		action("actionShow_Atmosphere");
		
		sleep(5000);
		
		for (Integer row : rows) {
			for (int i = 0; i < 8; i++) {
				int img = i * 45;
				System.out.println("Screenshotting image at azimuth " + img + " and row " + row);
				capture(row, img, atmoclear, src);
			}
		}
	}
	
	public static void capture(int alt, int az, File dest, File in) {
		command("core.moveToAltAzi(" + alt + "., " + az + "., 0.)");
		r.delay(100);
		action("actionSave_Screenshot_Global");
		r.delay(200);
		int i = 0;
		
		for (File file : Objects.requireNonNull(in.listFiles())) {
			try {
				String suffix = "";
				if(i > 0) {
					suffix = "(" + i + ")";
				}
				Files.move(file.toPath(), Path.of(dest.getAbsolutePath() + "\\" + alt + "x" + az + suffix + ".tif"));
				System.out.println("Moved file");
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
			
			System.out.println(new String(client.execute(post).getEntity().getContent().readAllBytes()));
			
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
			
			System.out.println(new String(client.execute(post).getEntity().getContent().readAllBytes()));
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void move(int x, int y) {
		r.mouseMove(x, y);
	}
	
	public static void click() {
		r.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		r.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	}
}