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
