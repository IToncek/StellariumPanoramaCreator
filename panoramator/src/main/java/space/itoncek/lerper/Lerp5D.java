package space.itoncek.lerper;

import space.itoncek.Snapshot3D;

import static space.itoncek.lerper.Lerp.*;

public class Lerp5D {
    public static Snapshot5D interpolateDirect(Snapshot5D start, Snapshot5D end, double ratio) {
        double eased = EaseInOut(ratio);

        double azi = lerp(start.azi(), end.azi(), eased);
        double alt = lerp(start.alt(), end.alt(), eased);
        double fov = lerp(start.fov(), end.fov(), eased);
        long day = Math.round(Math.floor(lerp(start.day(), end.day(), eased)));
        double hour = lerp(start.hour(), end.hour(), eased);

        return new Snapshot5D(azi,alt,fov,day,hour);
    }
    public static Snapshot5D interpolateMidpoint(Snapshot5D start, Snapshot3D mid, Snapshot5D end, double ratio) {
        double eased = EaseInOut(ratio);
        long day = Math.round(Math.floor(lerp(start.day(), end.day(), eased)));
        double hour = lerp(start.hour(), end.hour(), eased);
        double azi, alt, fov;

        if(ratio < .5) {
            azi = lerp(start.azi(), mid.azi(), eased);
            alt = lerp(start.alt(), mid.alt(), eased);
            fov = lerp(start.fov(), mid.fov(), eased);
        } else {
            azi = lerp(mid.azi(), end.azi(), eased);
            alt = lerp(mid.alt(), end.alt(), eased);
            fov = lerp(mid.fov(), end.fov(), eased);
        }
        return new Snapshot5D(azi,alt,fov,day, hour);
    }
}
