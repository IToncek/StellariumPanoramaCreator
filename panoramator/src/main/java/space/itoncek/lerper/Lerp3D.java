package space.itoncek.lerper;

import org.jetbrains.annotations.NotNull;
import space.itoncek.Snapshot3D;
import static space.itoncek.lerper.Lerp.EaseInOut;
import static space.itoncek.lerper.Lerp.lerp;

public class Lerp3D {
	public static @NotNull Snapshot3D interpolateDirect(@NotNull Snapshot3D start, @NotNull Snapshot3D end, double ratio) {
		double eased = EaseInOut(ratio);

		double azi = lerp(start.azi(), end.azi(), eased);
		double alt = lerp(start.alt(), end.alt(), eased);
		double fov = lerp(start.fov(), end.fov(), eased);

		return new Snapshot3D(azi, alt, fov);
	}

	public static Snapshot3D interpolateMidzoom(@NotNull Snapshot3D start, @NotNull Snapshot3D end, double ratio, @NotNull Double midZoom) {
		double eased = EaseInOut(ratio);

		double azi = lerp(start.azi(), end.azi(), eased);
		double alt = lerp(start.alt(), end.alt(), eased);
		double fov;

		if (ratio < .5) {
			double easeIn = EaseInOut(ratio * 2);
			fov = lerp(start.fov(), midZoom, easeIn);
		} else {
			double easeIn = EaseInOut((ratio * 2) - 1);
			fov = lerp(midZoom, end.fov(), easeIn);
		}
		return new Snapshot3D(azi, alt, fov);
	}
}
