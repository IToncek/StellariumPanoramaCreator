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

public enum Speed {
	STOP(0.0d),
	NORMAL(0.000011574074074074073d);

	private final double speed;

	Speed(double i) {
		speed = i;
	}

	public double getSpeed() {
		return speed;
	}
}
