package me.itoncek;

import java.awt.*;

import static java.lang.Thread.sleep;

public class DevTools {
	public static void main(String[] args) throws Exception {
		while (true) {
			System.out.println(MouseInfo.getPointerInfo().getLocation().x + " x " + MouseInfo.getPointerInfo().getLocation().y);
			sleep(1000);
		}
//
//		r = new Robot();
//
//		move(250, 123);
//		r.delay(5000);
//
//		move(86, 123);
	}
}
