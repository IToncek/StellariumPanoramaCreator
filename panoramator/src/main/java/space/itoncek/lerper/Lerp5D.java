package space.itoncek.lerper;

import org.jetbrains.annotations.NotNull;
import space.itoncek.Snapshot3D;

import static space.itoncek.lerper.Lerp.*;

import java.time.LocalDate;
import java.time.LocalTime;

public class Lerp5D {
    public static @NotNull Snapshot5D interpolateDirect(@NotNull Snapshot5D start, @NotNull Snapshot5D end, double ratio) {
        double eased = EaseInOut(ratio);

        double azi = lerp(start.azi(), end.azi(), eased);
        double alt = lerp(start.alt(), end.alt(), eased);
        double fov = lerp(start.fov(), end.fov(), eased);
        LocalDate day = LocalDate.ofEpochDay(Math.round(lerp(start.day().toEpochDay()+.5d, end.day().toEpochDay()+.5, eased)));
        LocalTime hour = LocalTime.ofSecondOfDay(Math.round(lerp(start.hour().toSecondOfDay(), end.hour().toSecondOfDay(), eased)));

        return new Snapshot5D(azi, alt, fov, day, hour);
    }

    public static Snapshot5D interpolateMidpoint(Snapshot5D start, Snapshot3D mid, Snapshot5D end, double ratio) {
        double eased = EaseInOut(ratio);
        LocalDate day = LocalDate.ofEpochDay(Math.round(lerp(start.day().toEpochDay()+.5d, end.day().toEpochDay()+.5, eased)));
        LocalTime hour = LocalTime.ofSecondOfDay(Math.round(lerp(start.hour().toSecondOfDay(), end.hour().toSecondOfDay(), eased)));
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
        LocalDate day = LocalDate.ofEpochDay(Math.round(lerp(start.day().toEpochDay()+.5d, end.day().toEpochDay()+.5, eased)));
        LocalTime hour = LocalTime.ofSecondOfDay(Math.round(lerp(start.hour().toSecondOfDay(), end.hour().toSecondOfDay(), eased)));
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
