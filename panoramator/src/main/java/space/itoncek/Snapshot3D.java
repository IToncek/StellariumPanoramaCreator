package space.itoncek;

import space.itoncek.lerper.Snapshot5D;

import java.time.LocalDateTime;

import static space.itoncek.Panoramator.*;

public record Snapshot3D(double azi, double alt, int fov) {
    Snapshot5D convertTo5D(LocalDateTime time) {
        return new Snapshot5D(this.azi, this.alt, this.fov, integerPart(julian(time)), fractionalPart(julian(time)));
    };
}
