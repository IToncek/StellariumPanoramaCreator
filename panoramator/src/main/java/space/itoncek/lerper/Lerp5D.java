package space.itoncek.lerper;

import org.jetbrains.annotations.NotNull;
import space.itoncek.Snapshot3D;

import static space.itoncek.lerper.Lerp.*;

public class Lerp5D {
    public static @NotNull Snapshot5D interpolateDirect(@NotNull Snapshot5D start, @NotNull Snapshot5D end, double ratio) {
        double eased = EaseInOut(ratio);

        double azi = lerp(start.azi(), end.azi(), eased);
        double alt = lerp(start.alt(), end.alt(), eased);
        double fov = lerp(start.fov(), end.fov(), eased);
        long day = Math.round(Math.floor(lerp(start.day()+.5, end.day()+.5, eased)));
        double hour = lerp(start.hour(), end.hour(), eased);

        return new Snapshot5D(azi, alt, fov, day, hour);
    }

    public static Snapshot5D interpolateMidpoint(Snapshot5D start, Snapshot3D mid, Snapshot5D end, double ratio) {
        double eased = EaseInOut(ratio);
        long day = Math.round(Math.floor(lerp(start.day()+.5, end.day()+.5, eased)));
        double hour = lerp(start.hour(), end.hour(), eased);
        double azi, alt, fov;

        if (ratio < .5) {
            double preeased = EaseInOut(ratio * 2);
            azi = lerp(start.azi(), mid.azi(), preeased);
            alt = lerp(start.alt(), mid.alt(), preeased);
            fov = lerp(start.fov(), mid.fov(), preeased);
        } else {
            double preeased = EaseInOut((ratio * 2d) - 1);
            azi = lerp(mid.azi(), end.azi(), preeased);
            alt = lerp(mid.alt(), end.alt(), preeased);
            fov = lerp(mid.fov(), end.fov(), preeased);
        }
        return new Snapshot5D(azi, alt, fov, day, hour);
    }

    public static Snapshot5D interpolateMidzoom(@NotNull Snapshot5D start, @NotNull Snapshot5D end, double ratio, @NotNull Double midZoom) {
        double eased = EaseInOut(ratio);

        double azi = lerp(start.azi(), end.azi(), eased);
        double alt = lerp(start.alt(), end.alt(), eased);
        long day = Math.round(Math.floor(lerp(start.day()+.5, end.day()+.5, eased)));
        double hour = lerp(start.hour(), end.hour(), eased);
        double fov;

        if(ratio<.5) {
            double easeIn = EaseInOut(ratio*2);
            fov = lerp(start.fov(), midZoom, easeIn);
        } else  {
            double easeIn = EaseInOut((ratio*2)-1);
            fov = lerp(midZoom, end.fov(), easeIn);
        }
        return new Snapshot5D(azi, alt, fov, day, hour);
    }
}
