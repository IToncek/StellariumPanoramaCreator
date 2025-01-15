package space.itoncek.lerper;

import org.jetbrains.annotations.NotNull;
import space.itoncek.Snapshot3D;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static space.itoncek.lerper.Lerp.EaseInOut;
import static space.itoncek.lerper.Lerp.lerp;

public class Lerp2D {
	public static @NotNull LocalDateTime interpolateDirect(@NotNull LocalDateTime start, @NotNull LocalDateTime end, double ratio) {
		double eased = EaseInOut(ratio);
		long star = LocalDateTime.of(start.toLocalDate(), start.toLocalTime()).toEpochSecond(ZoneOffset.systemDefault().getRules().getOffset(start.toLocalDate().atTime(start.toLocalTime())));
		long en = LocalDateTime.of(end.toLocalDate(), end.toLocalTime()).toEpochSecond(ZoneOffset.systemDefault().getRules().getOffset(end.toLocalDate().atTime(end.toLocalTime())));
		double time = lerp(star, en, eased);
		return LocalDateTime.ofEpochSecond(Math.round(time), 0, ZoneOffset.systemDefault().getRules().getOffset(end.toLocalDate().atTime(end.toLocalTime())));
	}
}
