package space.itoncek.lerper;

import org.jetbrains.annotations.NotNull;
import static space.itoncek.lerper.Lerp.EaseInOut;
import static space.itoncek.lerper.Lerp.lerp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;

public class Lerp5D {
	public static @NotNull Snapshot5D interpolateDirect(@NotNull Snapshot5D start, @NotNull Snapshot5D end, double ratio) {
//		double eased = ratio;
		double eased = EaseInOut(ratio);

		double azi = lerp(start.azi(), end.azi(), eased);
		double alt = lerp(start.alt(), end.alt(), eased);
		double fov = lerp(start.fov(), end.fov(), eased);
		long star = LocalDateTime.of(start.day(), start.hour()).toEpochSecond(ZoneOffset.systemDefault().getRules().getOffset(start.day().atTime(start.hour())));
		long en = LocalDateTime.of(end.day(), end.hour()).toEpochSecond(ZoneOffset.systemDefault().getRules().getOffset(end.day().atTime(end.hour())));

		double time = lerp(star, en, eased);

		LocalDateTime res = LocalDateTime.ofEpochSecond(Math.round(time), 0, ZoneOffset.systemDefault().getRules().getOffset(end.day().atTime(end.hour())));

		return new Snapshot5D(azi, alt, fov, res.toLocalDate(), res.toLocalTime());
	}

	public static Snapshot5D interpolateMidzoom(@NotNull Snapshot5D start, @NotNull Snapshot5D end, double ratio, @NotNull Double midZoom) {
		double eased = EaseInOut(ratio);

		double azi = lerp(start.azi(), end.azi(), eased);
		double alt = lerp(start.alt(), end.alt(), eased);
		LocalDate day = LocalDate.ofEpochDay(Math.round(lerp(start.day().toEpochDay() + .5d, end.day().toEpochDay() + .5, eased)));
		LocalTime hour = LocalTime.ofSecondOfDay(Math.round(lerp(start.hour().toSecondOfDay(), end.hour().toSecondOfDay(), eased)));
		double fov;

		if (ratio < .5) {
			double easeIn = EaseInOut(ratio * 2);
			fov = lerp(start.fov(), midZoom, easeIn);
		} else {
			double easeIn = EaseInOut((ratio * 2) - 1);
			fov = lerp(midZoom, end.fov(), easeIn);
		}
		return new Snapshot5D(azi, alt, fov, day, hour);
	}
}
