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
