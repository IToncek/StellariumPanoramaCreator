package space.itoncek;

import space.itoncek.lerper.Snapshot5D;

import java.time.LocalDate;
import java.time.LocalTime;

public record Snapshot3D(double azi, double alt, double fov) {
	Snapshot5D convertTo5D(LocalDate date, LocalTime time) {
		return new Snapshot5D(this.azi, this.alt, this.fov, date, time);
	}
}
