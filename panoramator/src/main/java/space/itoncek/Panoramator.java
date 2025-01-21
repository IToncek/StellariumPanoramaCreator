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

import space.itoncek.lerper.Lerp3D;
import space.itoncek.lerper.Lerp5D;
import space.itoncek.lerper.Snapshot5D;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Panoramator {

	public static final Font fnt;

	static {
		try {
			fnt = Font.createFont(Font.TRUETYPE_FONT, URI.create("https://cdn.itoncek.space/fonts/VCR_OSD_MONO-Regular.ttf").toURL().openStream());
		} catch (FontFormatException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) throws InterruptedException {
//        command("StelMovementMgr.zoomTo(100,0)");
//        move(20,270);
//        command("core.moveToAltAzi(20., 270., 0.)");
//		action("actionToggle_GuiHidden_Global");
//
//		Runtime.getRuntime().addShutdownHook(new Thread(()->{
//			action("actionToggle_GuiHidden_Global");
//		}));
		HashMap<String, List<String>> superscripts = new HashMap<>();

		//maturitniPracePopulate(superscripts);
		populate2025(superscripts);

		ExecutorService es = Executors.newCachedThreadPool();
		superscripts.forEach((filename, cmds) -> es.submit(() -> {
			try (FileWriter fw = new FileWriter(filename + ".ssc")) {
				for (String command : cmds) {
					fw.write(command + "\n");
				}
				fw.write("core.quitStellarium();\n");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}));
		es.shutdown();
		es.close();
	}

	private static void populate2025(HashMap<String, List<String>> superscripts) {
		File target = new File("X:\\AC 2025\\footage");
		target.mkdirs();

		long steps = 500;

		ArrayList<String> commands = new ArrayList<>(setup());

//		//------------------------------------------------------------------------------------------------------------------------------------------------
		String sequence = "0000";

		commands.addAll(move(LocalDateTime.of(2025, 1, 3, 3, 0, 0), new Snapshot3D(57.8889, 40.0415, 100)));

		commands.addAll(clearConstellations());
		commands.add(cheese("0000_clear", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(showConstellations(List.of(Constellation.Boo)));
		commands.add(cheese("0000_boo", new File(target.getAbsolutePath() + "\\" + "stills")));

		superscripts.put(sequence, commands.stream().toList());
		commands.clear();
		commands.addAll(setup());
		//------------------------------------------------------------------------------------------------------------------------------------------------
		sequence = "0001";
		commands.addAll(clearConstellations());
		commands.addAll(slideTo(new File(target + "\\" + sequence),
				LocalDateTime.of(2025, 1, 3, 3, 0, 0),
				LocalDateTime.of(2025, 1, 4, 18, 0, 0),
				steps,
				new Snapshot3D(57.8889, 40.0415, 100),
				new Snapshot3D(211.3386, 26.8404, .5),
				50.));

		commands.add(cheese(sequence + "_end", new File(target.getAbsolutePath() + "\\" + "stills")));

		superscripts.put(sequence, commands.stream().toList());
		commands.clear();
		commands.addAll(setup());
		//------------------------------------------------------------------------------------------------------------------------------------------------
		sequence = "0002";
		commands.addAll(clearConstellations());
		commands.addAll(slideTrack(new File(target + "\\" + sequence),
				LocalDateTime.of(2025, 1, 4, 18, 0, 0),
				LocalDateTime.of(2025, 1, 4, 20, 0, 0),
				steps*2,
				"Saturn",
				.5));

		commands.add(cheese(sequence + "_end", new File(target.getAbsolutePath() + "\\" + "stills")));

		superscripts.put(sequence, commands.stream().toList());
		commands.clear();
		commands.addAll(setup());
		//------------------------------------------------------------------------------------------------------------------------------------------------
		sequence = "0003b";
		commands.addAll(clearConstellations());
		commands.addAll(travelTrack(new File(target + "\\" + sequence),
				LocalDateTime.of(2025, 1, 4, 20, 0, 0),
				LocalDateTime.of(2025, 1, 14, 16, 30, 0),
				steps,
				"Saturn",
				"C/2024 G3 (ATLAS)",
				.5,
				5));

		commands.addAll(showAtmo(false));
		commands.add(cheese("0003b_noatmo",new File(target + "\\stills\\")));
		commands.addAll(showAtmo(true));

		superscripts.put(sequence, commands.stream().toList());
		commands.clear();
		commands.addAll(setup());
		//------------------------------------------------------------------------------------------------------------------------------------------------
		sequence = "0003";
		commands.addAll(clearConstellations());
		commands.addAll(travelTrack(new File(target + "\\" + sequence),
				LocalDateTime.of(2025, 1, 14, 16, 30, 0),
				LocalDateTime.of(2025, 1, 14, 5, 19, 0),
				steps,
				"C/2024 G3 (ATLAS)",
				"Mars",
				5,
				60));

		superscripts.put(sequence, commands.stream().toList());
		commands.clear();
		commands.addAll(setup());
		//------------------------------------------------------------------------------------------------------------------------------------------------
		sequence = "0004";
		commands.addAll(clearConstellations());
		commands.addAll(travelTrack(new File(target + "\\" + sequence),
				LocalDateTime.of(2025, 1, 14, 5, 19, 0),
				LocalDateTime.of(2025, 2, 9, 20, 30, 0),
				steps,
				"Mars",
				"Mars",
				60,
				20));

		superscripts.put(sequence, commands.stream().toList());
		commands.clear();
		commands.addAll(setup());
		//------------------------------------------------------------------------------------------------------------------------------------------------
		sequence = "0005";
		commands.addAll(clearConstellations());
		commands.addAll(travelTrack(new File(target + "\\" + sequence),
				LocalDateTime.of(2025, 2, 9, 20, 30, 0),
				LocalDateTime.of(2025, 3, 14, 5, 0, 0),
				steps,
				"Mars",
				"Moon",
				20,
				3));

		superscripts.put(sequence, commands.stream().toList());
		commands.clear();
		commands.addAll(setup());
		//------------------------------------------------------------------------------------------------------------------------------------------------
		sequence = "0006";
		commands.addAll(clearConstellations());
		commands.addAll(slideTrack(new File(target + "\\" + sequence),
				LocalDateTime.of(2025, 3, 14, 5, 0, 0),
				LocalDateTime.of(2025, 3, 14, 6, 16, 30),
				steps*2,
				"Moon",
				3));

		commands.add(cheese(sequence + "_end", new File(target.getAbsolutePath() + "\\" + "stills")));

		superscripts.put(sequence, commands.stream().toList());
		commands.clear();
		commands.addAll(setup());
		//------------------------------------------------------------------------------------------------------------------------------------------------
		sequence = "0006-hurricane";
		commands.addAll(clearConstellations());
		commands.addAll(slideTrack(new File(target + "\\" + sequence),
				LocalDateTime.of(2025, 3, 14, 5, 0, 0),
				LocalDateTime.of(2025, 3, 14, 6, 16, 30),
				steps*2,
				"Moon",
				3));

		commands.add(cheese(sequence + "_end", new File(target.getAbsolutePath() + "\\" + "stills")));

		superscripts.put(sequence, commands.stream().toList());
		commands.clear();
		commands.addAll(setup());
		//------------------------------------------------------------------------------------------------------------------------------------------------
		sequence = "0007";
		commands.addAll(clearConstellations());
		commands.addAll(travelTrack(new File(target + "\\" + sequence),
				LocalDateTime.of(2025, 3, 14, 6, 16, 30),
				LocalDateTime.of(2025, 3, 29, 11, 0, 0),
				steps,
				"Moon",
				"Moon",
				3,
				84));

		superscripts.put(sequence, commands.stream().toList());
		commands.clear();
		commands.addAll(setup());
		//------------------------------------------------------------------------------------------------------------------------------------------------
		sequence = "0008";
		commands.addAll(clearConstellations());
		commands.addAll(travelTrack(new File(target + "\\" + sequence),
				LocalDateTime.of(2025, 3, 29, 11, 0, 0),
				LocalDateTime.of(2025, 3, 29, 11, 0, 0),
				steps,
				"Moon",
				"Sun",
				84,
				3));

		superscripts.put(sequence, commands.stream().toList());
		commands.clear();
		commands.addAll(setup());
		//------------------------------------------------------------------------------------------------------------------------------------------------
		sequence = "0009";
		commands.addAll(clearConstellations());
		commands.addAll(slideTrack(new File(target + "\\" + sequence),
				LocalDateTime.of(2025, 3, 29, 11, 0, 0),
				LocalDateTime.of(2025, 3, 29, 13, 30, 0),
				steps,
				"Sun",
				3));

		commands.add(cheese(sequence + "_end", new File(target.getAbsolutePath() + "\\" + "stills")));

		superscripts.put(sequence, commands.stream().toList());
		commands.clear();
		commands.addAll(setup());
		//------------------------------------------------------------------------------------------------------------------------------------------------
		sequence = "0010";
		commands.addAll(clearConstellations());
		commands.addAll(travelTrack(new File(target + "\\" + sequence),
				LocalDateTime.of(2025, 3, 29, 13, 30, 0),
				LocalDateTime.of(2025, 4, 1, 22, 0, 0),
				steps,
				"Sun",
				"Pleiades",
				3,
				60));

		superscripts.put(sequence, commands.stream().toList());
		commands.clear();
		commands.addAll(setup());
		//------------------------------------------------------------------------------------------------------------------------------------------------
		sequence = "0011";
		commands.addAll(clearConstellations());
		commands.addAll(travelTrack(new File(target + "\\" + sequence),
				LocalDateTime.of(2025, 4, 1, 22, 0, 0),
				LocalDateTime.of(2025, 4, 1, 22, 0, 0),
				steps,
				"Pleiades",
				"Pleiades",
				60,
				5));

		superscripts.put(sequence, commands.stream().toList());
		commands.clear();
		commands.addAll(setup());
		//------------------------------------------------------------------------------------------------------------------------------------------------
		sequence = "0012";
		commands.addAll(clearConstellations());
		commands.addAll(slideTrack(new File(target + "\\" + sequence),
				LocalDateTime.of(2025, 4, 1, 22, 0, 0),
				LocalDateTime.of(2025, 4, 1, 23, 45, 0),
				steps*2,
				"Pleiades",
				5));

		superscripts.put(sequence, commands.stream().toList());
		commands.clear();
		commands.addAll(setup());
		//------------------------------------------------------------------------------------------------------------------------------------------------
		sequence = "0013";
		commands.addAll(clearConstellations());
		commands.addAll(travelTrack(new File(target + "\\" + sequence),
				LocalDateTime.of(2025, 4, 1, 23, 45, 0),
				LocalDateTime.of(2025, 4, 23, 4, 0, 0),
				steps,
				"Pleiades",
				"4 Aql",
				5,
				100));

		superscripts.put(sequence, commands.stream().toList());
		commands.clear();
		commands.addAll(setup());
		//------------------------------------------------------------------------------------------------------------------------------------------------
		sequence = "0014";
		commands.addAll(clearConstellations());
		commands.addAll(travelTrack(new File(target + "\\" + sequence),
				LocalDateTime.of(2025, 4, 23, 4, 0, 0),
				LocalDateTime.of(2025, 8, 11, 23, 0, 0),
				steps,
				"4 Aql",
				"M31",
				100,
				80));

		superscripts.put(sequence, commands.stream().toList());
		commands.clear();
		commands.addAll(setup());
		//------------------------------------------------------------------------------------------------------------------------------------------------
	}


	private static String target(String target) {
		return "core.moveToObject(\"%s\",.1);".formatted(target);
	}

	private static void maturitniPracePopulate(HashMap<String, List<String>> superscripts) {
		File target = new File("X:\\AC Prosinec\\maturitni_prace\\");
		target.mkdirs();

		long steps = 500;

		ArrayList<String> commands = new ArrayList<>(setup());

//		//------------------------------------------------------------------------------------------------------------------------------------------------
		String sequence = "quadratic";
		commands.addAll(setup());
		commands.addAll(move(LocalDateTime.of(2024, 12, 15, 17, 0, 0), new Snapshot3D(259.2299, 49.1836, 100)));
		commands.add(cheese(sequence + "_before", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(slideTo(new File(target + "\\" + sequence), LocalDateTime.of(2024, 12, 15, 17, 0, 0), LocalDateTime.of(2024, 12, 15, 22, 0, 0), steps, new Snapshot3D(259.2299, 49.1836, 100), new Snapshot3D(145.8772, 36.3646, 44.44444), null));
		commands.addAll(move(LocalDateTime.of(2024, 12, 15, 22, 0, 0), new Snapshot3D(145.8772, 36.3646, 44.44444)));
		commands.add(cheese(sequence + "_after", new File(target.getAbsolutePath() + "\\" + "stills")));
		superscripts.put(sequence, commands.stream().toList());
	}

	private static void prosinecPopulate(HashMap<String, List<String>> superscripts) {
		File target = new File("X:\\AC Prosinec\\footage\\");
		target.mkdirs();

		long steps = 500;

		ArrayList<String> commands = new ArrayList<>(setup());

//		//------------------------------------------------------------------------------------------------------------------------------------------------
		String sequence = "0000";
		commands.addAll(move(LocalDateTime.of(2024, 12, 15, 18, 30, 0), new Snapshot3D(276.5888, 28.4216, 72.8)));
		commands.add("core.wait(1);");

		commands.addAll(showConstellations(List.of(Constellation.Lyr)));
		commands.add(cheese("0000_c_lyra", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(showConstellations(List.of(Constellation.Cyg)));
		commands.add(cheese("0000_c_cygnus", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(showConstellations(List.of(Constellation.Aql)));
		commands.add(cheese("0000_c_aql", new File(target.getAbsolutePath() + "\\" + "stills")));

		commands.addAll(clearConstellations());
		commands.add(cheese("0000_clear", new File(target.getAbsolutePath() + "\\" + "stills")));

		superscripts.put("0000", commands.stream().toList());
		commands.clear();
		commands.addAll(setup());

		//------------------------------------------------------------------------------------------------------------------------------------------------
		sequence = "0001";
		commands.addAll(showConstellations(List.of(Constellation.Lyr, Constellation.Cyg, Constellation.Aql)));
		commands.addAll(slideTo(new File(target + "\\" + sequence), LocalDateTime.of(2024, 12, 15, 18, 30, 0), steps, new Snapshot3D(276.5888, 28.4216, 72.8), new Snapshot3D(307.4430, 16.0075, 37.4), null));

		commands.addAll(move(LocalDateTime.of(2024, 12, 15, 18, 30, 0), new Snapshot3D(307.4430, 16.0075, 37.4)));

		commands.addAll(clearConstellations());
		commands.add(cheese(sequence + "_clear", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(showConstellations(List.of(Constellation.Lyr, Constellation.Cyg, Constellation.Aql)));
		commands.add(cheese(sequence + "_c_old", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(showConstellations(List.of(Constellation.Her)));
		commands.add(cheese(sequence + "_c_her", new File(target.getAbsolutePath() + "\\" + "stills")));

		superscripts.put(sequence, commands.stream().toList());
		commands.clear();
		commands.addAll(setup());

		//------------------------------------------------------------------------------------------------------------------------------------------------
		sequence = "0002";
		commands.addAll(showConstellations(List.of(Constellation.Lyr, Constellation.Cyg, Constellation.Aql, Constellation.Her)));
		commands.addAll(slideTo(new File(target + "\\" + sequence), LocalDateTime.of(2024, 12, 15, 18, 30, 0), steps, new Snapshot3D(307.4430, 16.0075, 37.4), new Snapshot3D(281.0042, 20.1287, 50), null));

		commands.addAll(move(LocalDateTime.of(2024, 12, 15, 18, 30, 0), new Snapshot3D(281.0042, 20.1287, 50)));

		commands.addAll(clearConstellations());
		commands.add(cheese(sequence + "_clear", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(showConstellations(List.of(Constellation.Lyr, Constellation.Cyg, Constellation.Aql, Constellation.Her)));
		commands.add(cheese(sequence + "_constellations", new File(target.getAbsolutePath() + "\\" + "stills")));

		superscripts.put(sequence, commands.stream().toList());
		commands.clear();
		commands.addAll(setup());

		//------------------------------------------------------------------------------------------------------------------------------------------------
		sequence = "0003";
		commands.addAll(slideTo(new File(target + "\\" + sequence), LocalDateTime.of(2024, 12, 15, 18, 30, 0), LocalDateTime.of(2024, 12, 15, 19, 30, 0), steps, new Snapshot3D(281.0042, 20.1287, 50), new Snapshot3D(67.7101, 80, 70), null));

		commands.addAll(move(LocalDateTime.of(2024, 12, 15, 19, 30, 0), new Snapshot3D(67.7101, 80, 70)));
		commands.addAll(setup());
		commands.addAll(clearConstellations());
		commands.add(cheese(sequence + "_clear", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(showConstellations(List.of(Constellation.Cep)));
		commands.add(cheese(sequence + "_c_cep", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(showConstellations(List.of(Constellation.Cas)));
		commands.add(cheese(sequence + "_c_cas", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(showConstellations(List.of(Constellation.And)));
		commands.add(cheese(sequence + "_c_and", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(showConstellations(List.of(Constellation.Per)));
		commands.add(cheese(sequence + "_c_per", new File(target.getAbsolutePath() + "\\" + "stills")));

		superscripts.put(sequence, commands.stream().toList());
		commands.clear();
		commands.addAll(setup());

		//------------------------------------------------------------------------------------------------------------------------------------------------
		sequence = "0004";
		commands.addAll(showConstellations(List.of(Constellation.Cep, Constellation.Cas, Constellation.And, Constellation.Per)));
		commands.addAll(slideTo(new File(target + "\\" + sequence), LocalDateTime.of(2024, 12, 15, 19, 30, 0), steps, new Snapshot3D(67.7101, 80, 70), new Snapshot3D(205.3829, 62.9077, 65), null));

		commands.addAll(move(LocalDateTime.of(2024, 12, 15, 19, 30, 0), new Snapshot3D(205.3829, 62.9077, 65)));

		commands.addAll(showConstellations(List.of(Constellation.Cep, Constellation.Cas, Constellation.And, Constellation.Per)));
		commands.add(cheese(sequence + "_c_old", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(showConstellations(List.of(Constellation.Peg)));
		commands.add(cheese(sequence + "_c_peg", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(clearConstellations());
		commands.add(cheese(sequence + "_clear", new File(target.getAbsolutePath() + "\\" + "stills")));

		superscripts.put(sequence, commands.stream().toList());

		commands.clear();
		commands.addAll(setup());

		//------------------------------------------------------------------------------------------------------------------------------------------------
		sequence = "0005";
		commands.addAll(showConstellations(List.of(Constellation.Cep, Constellation.Cas, Constellation.And, Constellation.Per, Constellation.Peg)));
		commands.addAll(slideTo(new File(target + "\\" + sequence), LocalDateTime.of(2024, 12, 15, 19, 30, 0), steps, new Snapshot3D(205.3829, 62.9077, 65), new Snapshot3D(198.4649, 39.1161, 86), null));

		commands.addAll(move(LocalDateTime.of(2024, 12, 15, 19, 30, 0), new Snapshot3D(198.4649, 39.1161, 86)));
		commands.addAll(showConstellations(List.of(Constellation.Cep, Constellation.Cas, Constellation.And, Constellation.Per, Constellation.Peg)));
		commands.add(cheese(sequence + "_c_old", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(showGround(false));
		commands.addAll(showConstellations(List.of(Constellation.Psc)));
		commands.add(cheese(sequence + "_c_psc", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(showConstellations(List.of(Constellation.Aqr)));
		commands.add(cheese(sequence + "_c_aqr", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(showConstellations(List.of(Constellation.Cet)));
		commands.add(cheese(sequence + "_c_cet", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(clearConstellations());
		commands.addAll(showGround(true));
		commands.add(cheese(sequence + "_clear", new File(target.getAbsolutePath() + "\\" + "stills")));

		superscripts.put(sequence, commands.stream().toList());

		commands.clear();
		commands.addAll(setup());

		//------------------------------------------------------------------------------------------------------------------------------------------------
		sequence = "0006";
		commands.addAll(slideTo(new File(target + "\\" + sequence), LocalDateTime.of(2024, 12, 15, 19, 30, 0), LocalDateTime.of(2024, 12, 15, 23, 30, 0), steps, new Snapshot3D(181.1800, 43.1769, 70), new Snapshot3D(166.0080, 77.3050, 30), null));

		commands.addAll(move(LocalDateTime.of(2024, 12, 15, 23, 30, 0), new Snapshot3D(166.0080, 77.3050, 30)));

		commands.addAll(showConstellations(List.of(Constellation.Aur)));
		commands.add(cheese(sequence + "_c_aur", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(clearConstellations());
		commands.add(cheese(sequence + "_clear", new File(target.getAbsolutePath() + "\\" + "stills")));

		superscripts.put(sequence, commands.stream().toList());

		commands.clear();
		commands.addAll(setup());
		//------------------------------------------------------------------------------------------------------------------------------------------------
		sequence = "0007";
		commands.addAll(showConstellations(List.of(Constellation.Aur)));
		commands.addAll(slideTo(new File(target + "\\" + sequence), LocalDateTime.of(2024, 12, 15, 23, 30, 0), steps, new Snapshot3D(166.0080, 77.3050, 30), new Snapshot3D(201.0845, 66.8315, 52), null));

		commands.addAll(move(LocalDateTime.of(2024, 12, 15, 23, 30, 0), new Snapshot3D(201.0845, 66.8315, 52)));

		commands.addAll(showConstellations(List.of(Constellation.Aur)));
		commands.add(cheese(sequence + "_c_aur", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(showConstellations(List.of(Constellation.Tau)));
		commands.add(cheese(sequence + "_c_tau", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(clearConstellations());
		commands.add(cheese(sequence + "_clear", new File(target.getAbsolutePath() + "\\" + "stills")));

		superscripts.put(sequence, commands.stream().toList());

		commands.clear();
		commands.addAll(setup());
		//------------------------------------------------------------------------------------------------------------------------------------------------
		sequence = "0008";
		commands.addAll(showConstellations(List.of(Constellation.Aur, Constellation.Tau)));
		commands.addAll(slideTo(new File(target + "\\" + sequence), LocalDateTime.of(2024, 12, 15, 23, 30, 0), steps, new Snapshot3D(201.0845, 66.8315, 52), new Snapshot3D(166.3988, 57.5968, 70), null));

		commands.addAll(move(LocalDateTime.of(2024, 12, 15, 23, 30, 0), new Snapshot3D(166.3988, 57.5968, 70)));

		commands.addAll(showConstellations(List.of(Constellation.Aur, Constellation.Tau)));
		commands.addAll(showGround(false));
		commands.add(cheese(sequence + "_c_old", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(showConstellations(List.of(Constellation.CMi)));
		commands.add(cheese(sequence + "_c_cmi", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(showConstellations(List.of(Constellation.Gem)));
		commands.add(cheese(sequence + "_c_gem", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(showConstellations(List.of(Constellation.Ori)));
		commands.add(cheese(sequence + "_c_ori", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(clearConstellations());
		commands.add(cheese(sequence + "_clear_ng", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(showGround(true));
		commands.add(cheese(sequence + "_clear", new File(target.getAbsolutePath() + "\\" + "stills")));

		superscripts.put(sequence, commands.stream().toList());

		commands.clear();
		commands.addAll(setup());
		//------------------------------------------------------------------------------------------------------------------------------------------------
		sequence = "0009";
		commands.addAll(showConstellations(List.of(Constellation.Aur, Constellation.Tau, Constellation.CMi, Constellation.Gem, Constellation.Ori)));
		commands.addAll(showGround(false));
		commands.addAll(slideTo(new File(target + "\\" + sequence), LocalDateTime.of(2024, 12, 15, 23, 30, 0), steps, new Snapshot3D(166.3988, 57.5968, 70), new Snapshot3D(171.5850, 43.6764, 35), null));

		commands.addAll(move(LocalDateTime.of(2024, 12, 15, 23, 30, 0), new Snapshot3D(171.5850, 43.6764, 35)));

		commands.addAll(showConstellations(List.of(Constellation.Aur, Constellation.Tau, Constellation.CMi, Constellation.Gem)));
		commands.add(cheese(sequence + "_c_other", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(showConstellations(List.of(Constellation.Ori)));
		commands.add(cheese(sequence + "_c_ori", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(clearConstellations());
		commands.add(cheese(sequence + "_clear", new File(target.getAbsolutePath() + "\\" + "stills")));

		commands.addAll(showGround(true));
		superscripts.put(sequence, commands.stream().toList());

		commands.clear();
		commands.addAll(setup());
		//------------------------------------------------------------------------------------------------------------------------------------------------
		sequence = "0010";
		commands.addAll(showConstellations(List.of(Constellation.Aur, Constellation.Tau, Constellation.CMi, Constellation.Gem, Constellation.Ori)));
		commands.addAll(showGround(false));
		commands.addAll(slideTo(new File(target + "\\" + sequence), LocalDateTime.of(2024, 12, 15, 23, 30, 0), steps, new Snapshot3D(171.5850, 43.6764, 35), new Snapshot3D(190, 30, 65), null));

		commands.addAll(move(LocalDateTime.of(2024, 12, 15, 23, 30, 0), new Snapshot3D(190, 30, 65)));

		commands.addAll(showConstellations(List.of(Constellation.Aur, Constellation.Tau, Constellation.CMi, Constellation.Gem, Constellation.Ori)));
		commands.add(cheese(sequence + "_c_other", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(showConstellations(List.of(Constellation.Eri)));
		commands.add(cheese(sequence + "_c_eri", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(clearConstellations());
		commands.add(cheese(sequence + "_clear", new File(target.getAbsolutePath() + "\\" + "stills")));

		commands.addAll(showGround(true));
		superscripts.put(sequence, commands.stream().toList());

		commands.clear();
		commands.addAll(setup());
		//------------------------------------------------------------------------------------------------------------------------------------------------
		sequence = "0011";
		commands.addAll(showConstellations(List.of(Constellation.Aur, Constellation.Tau, Constellation.CMi, Constellation.Gem, Constellation.Ori, Constellation.Eri)));
		commands.addAll(showGround(false));
		commands.addAll(slideTo(new File(target + "\\" + sequence), LocalDateTime.of(2024, 12, 15, 23, 30, 0), steps, new Snapshot3D(190, 30, 65), new Snapshot3D(190, 15, 100), null));

		commands.addAll(move(LocalDateTime.of(2024, 12, 15, 23, 30, 0), new Snapshot3D(190, 15, 100)));

		commands.addAll(showConstellations(List.of(Constellation.Aur, Constellation.Tau, Constellation.CMi, Constellation.Gem, Constellation.Ori)));
		commands.add(cheese(sequence + "_c_other", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(showConstellations(List.of(Constellation.Eri)));
		commands.add(cheese(sequence + "_c_eri", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(showConstellations(List.of(Constellation.CMa)));
		commands.add(cheese(sequence + "_c_cma", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(clearConstellations());
		commands.add(cheese(sequence + "_clear", new File(target.getAbsolutePath() + "\\" + "stills")));

		commands.addAll(showGround(true));
		superscripts.put(sequence, commands.stream().toList());

		commands.clear();
		commands.addAll(setup());
		//------------------------------------------------------------------------------------------------------------------------------------------------
		sequence = "0012";
		commands.addAll(clearConstellations());
		commands.addAll(showGround(false));
		commands.addAll(slideTo(new File(target + "\\" + sequence + "_ng"), LocalDateTime.of(2024, 12, 15, 23, 30, 0), LocalDateTime.of(2024, 12, 16, 7, 0, 0), steps, new Snapshot3D(190, 15, 100), new Snapshot3D(260, 34, 60), null));
		commands.addAll(showConstellations(List.of()));
		commands.addAll(showGround(true));
		commands.addAll(slideTo(new File(target + "\\" + sequence), LocalDateTime.of(2024, 12, 15, 23, 30, 0), LocalDateTime.of(2024, 12, 16, 7, 0, 0), steps, new Snapshot3D(190, 15, 100), new Snapshot3D(260, 34, 60), null));

		commands.addAll(move(LocalDateTime.of(2024, 12, 16, 7, 0, 0), new Snapshot3D(260, 34, 60)));

		commands.addAll(showConstellations(List.of(Constellation.Cnc)));
		commands.add(cheese(sequence + "c_cnc", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(clearConstellations());
		commands.add(cheese(sequence + "_clear", new File(target.getAbsolutePath() + "\\" + "stills")));

		superscripts.put(sequence, commands.stream().toList());

		commands.clear();
		commands.addAll(setup());
		//------------------------------------------------------------------------------------------------------------------------------------------------
		sequence = "0013";
		commands.addAll(showConstellations(List.of(Constellation.Cnc)));
		commands.addAll(slideTo(new File(target + "\\" + sequence), LocalDateTime.of(2024, 12, 16, 7, 0, 0), steps, new Snapshot3D(260, 34, 60), new Snapshot3D(204,69,85), null));

		commands.addAll(move(LocalDateTime.of(2024, 12, 16, 7, 0, 0), new Snapshot3D(204,69,85)));

		commands.addAll(showConstellations(List.of(Constellation.Leo)));
		commands.add(cheese(sequence + "_c_leo", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(showConstellations(List.of(Constellation.Vir)));
		commands.add(cheese(sequence + "_c_vir", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(showConstellations(List.of(Constellation.Boo)));
		commands.add(cheese(sequence + "_c_boo", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(showConstellations(List.of(Constellation.UMa)));
		commands.add(cheese(sequence + "_c_uma", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(showConstellations(List.of(Constellation.Com)));
		commands.add(cheese(sequence + "_c_com", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(showConstellations(List.of(Constellation.CVn)));
		commands.add(cheese(sequence + "_c_cvn", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(clearConstellations());
		commands.add(cheese(sequence + "_clear", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(showGround(false));
		commands.addAll(clearConstellations());
		commands.add(cheese(sequence + "_clear_ng", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(showGround(true));
		superscripts.put(sequence, commands.stream().toList());

		commands.clear();
		commands.addAll(setup());
		//------------------------------------------------------------------------------------------------------------------------------------------------
		sequence = "geminids";
		commands.addAll(move(LocalDateTime.of(2024, 12, 14, 22, 0, 0), new Snapshot3D(89,44,100)));

		commands.addAll(showConstellations(List.of(Constellation.Gem)));
		commands.add(cheese(sequence + "_c_gem", new File(target.getAbsolutePath() + "\\" + "stills")));
		commands.addAll(clearConstellations());
		commands.add(cheese(sequence + "_clear", new File(target.getAbsolutePath() + "\\" + "stills")));
		superscripts.put(sequence, commands.stream().toList());

		commands.clear();
		commands.addAll(setup());
	}

	private static Collection<String> showGround(boolean enabled) {
		return List.of("LandscapeMgr.setFlagLandscape(%b);".formatted(enabled), "core.wait(3);");
	}
	private static Collection<String> showAtmo(boolean enabled) {
		return List.of("LandscapeMgr.setFlagAtmosphere(%b);".formatted(enabled), "core.wait(3);");
	}

	private static List<String> setup() {
		return List.of("core.clear(\"natural\");",
				"ConstellationMgr.setArtFadeDuration(0.01);",
				"SolarSystem.setFlagPlanets(true);",
				"core.setGuiVisible(false);",
				"core.setTimeRate(0);",
				"ConstellationMgr.setFlagLines(false);",
				"ConstellationMgr.setFlagArt(false);",
				"ConstellationMgr.setFlagIsolateSelected(true);",
				"LandscapeMgr.setAtmosphereModel(\"ShowMySky\");",
				"LandscapeMgr.setAtmosphereModelPath(\"D:/Program Files/Stellarium/atmosphere/default\");",
				"LandscapeMgr.setAtmosphereShowMySkyStoppedWithError(true);",
				"core.wait(2);",
				"var label = LandscapeMgr.getAtmosphereShowMySkyStatusText();",
				"LabelMgr.labelScreen(label,10, 10, true, 32.0, \"#ffffff\");",
				"core.wait(1);",
				"LabelMgr.deleteAllLabels();",
				"StelSkyDrawer.setFlagLuminanceAdaptation(false);");
	}

	private static List<String> showConstellations(List<Constellation> constellations) {
		ArrayList<String> commands = new ArrayList<>();
		commands.add("ConstellationMgr.setFlagLines(true);");
		commands.add("ConstellationMgr.setFlagArt(true);");
		commands.add("ConstellationMgr.deselectConstellations();");
		commands.add("core.wait(3);");
		for (Constellation constellation : constellations) {
			commands.add("core.selectConstellationByName(\"%s\");".formatted(constellation.name));
		}
		commands.add("core.wait(3);");
		return commands;
	}

	private static List<String> clearConstellations() {
		return List.of("ConstellationMgr.deselectConstellations()", "ConstellationMgr.setFlagLines(false);", "ConstellationMgr.setFlagArt(false);", "core.wait(3);");
	}

//    private static boolean isScriptRunning() {
//        JSONObject o;
//        try(Scanner sc = new Scanner(new URL("http://192.168.99.64:8090/api/scripts/status").openStream())) {
//            StringJoiner js = new StringJoiner("\n");
//            while (sc.hasNextLine()) js.add(sc.nextLine());
//
//            o = new JSONObject(js.toString());
//            return o.getBoolean("scriptIsRunning");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

//    private static void moveAllCapturesToAFolder(File in,File dest) {
//        dest.mkdirs();
//        int i = 0;
//        for (File file : Objects.requireNonNull(in.listFiles())) {
//            try {
//                Files.move(file.toPath(), Path.of(dest.getAbsolutePath() + "\\" + i + ".tif"));
//                i++;
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }



	private static Collection<String> slideTrack(File target, LocalDateTime start, LocalDateTime end, long steps, String targetObject, double zoom) {
		ArrayList<String> output = new ArrayList<>();
		System.out.println(target.getAbsolutePath());
		target.mkdirs();

		output.add("""
				function lerpsmooth(start, end, t)
				{
				    var t2 = lerp(Math.pow(t,2), 1 - Math.pow(1-t,2), t);
					return lerp(start,end,t2);
				}
				
				function lerp(a, b, t)
				{
				    return (1 - t) * a + t * b;
				}
				
				function int(a)
				{
				    return Math.floor(a);
				}
				
				function frac(a) {
				    return a - int(a);
				}
				
				""");

		output.add("""
				var object = "%s";
				""".formatted(targetObject));

		output.add("""
				var fov = %f;
				""".formatted(zoom));

		output.add("""
				var startTime = core.jdFromDateString("%s", "utc");
				var endTime = core.jdFromDateString("%s", "utc");
				""".formatted(timeToString(start), timeToString(end)));

		output.add("""
				
				var startDay = int(startTime);
				var endDay = int(endTime);
				
				var startHour = frac(startTime);
				var endHour = frac(endTime);
				""");

		output.add("""
				var steps = %d;
				""".formatted(steps));

		output.add("""
				core.moveToObject(object, 0.);
				StelMovementMgr.zoomTo(fov,0);
				core.setJDay(startTime);
				core.wait(3);
				
				for(var i = 0; i < steps; i++) {
					var t = i/steps;
					var day = Math.floor(lerpsmooth(startDay+.5, endDay+.5, t));
					var hour = lerpsmooth(startHour, endHour, t);
					core.setJDay(day+hour);
					core.moveToObject(object, 0.);
					core.wait(0.01);
				""");
		output.add("""
					core.screenshot((""+i).padStart(4,"0"),false,"%s",true,"jpeg");
				}
				""".formatted(target.getAbsolutePath().replace("\\", "\\\\")));
		return output;
	}

	private static String timeToString(LocalDateTime time) {
		return ZonedDateTime.of(time,ZoneId.of("Europe/Prague")).withZoneSameInstant(ZoneId.of("UTC")).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
	}

	private static Collection<String> travelTrack(File target, LocalDateTime start, LocalDateTime end, long steps, String sourceObject, String targetObject, double startZoom, double endZoom) {
		ArrayList<String> output = new ArrayList<>();
		System.out.println(target.getAbsolutePath());
		target.mkdirs();

		output.add("""
				function lerpsmooth(start, end, t)
				{
				    var t2 = lerp(Math.pow(t,2), 1 - Math.pow(1-t,2), t);
					return lerp(start,end,t2);
				}
				
				function lerp(a, b, t)
				{
				    return (1 - t) * a + t * b;
				}
				
				function int(a)
				{
				    return Math.floor(a);
				}
				
				function frac(a) {
				    return a - int(a);
				}
				
				""");

		output.add("""
				var startObject = "%s";
				var endObject = "%s";
				""".formatted(sourceObject,targetObject));

		output.add("""
				var startFov = %f;
				var endFov = %f;
				""".formatted(startZoom,endZoom));


		output.add("""
				var startTime = core.jdFromDateString("%s", "utc");
				var endTime = core.jdFromDateString("%s", "utc");
				""".formatted(timeToString(start), timeToString(end)));

		output.add("""
				
				var startDay = int(startTime);
				var endDay = int(endTime);
				
				var startHour = frac(startTime);
				var endHour = frac(endTime);
				
				core.setJDay(startTime);
				core.wait(0.5);
				
				var startalt = core.getObjectInfo(startObject).altitude
				var startaz = core.getObjectInfo(startObject).azimuth
				
				core.setJDay(endTime);
				core.wait(0.5);
				
				var endalt = core.getObjectInfo(endObject).altitude
				var endaz = core.getObjectInfo(endObject).azimuth
				""");

		output.add("""
				var steps = %d;
				""".formatted(steps));

		output.add("""
				core.moveToObject(startObject, 0.);
				StelMovementMgr.zoomTo(startFov,0);
				core.setJDay(startTime);
				core.wait(3);
				
				for(var i = 0; i < steps; i++) {
					var t = i/steps;
					var alt = lerpsmooth(startalt, endalt, t);
					var az = lerpsmooth(startaz, endaz, t);
					var zoom = lerpsmooth(startFov, endFov, t);
					var day = Math.floor(lerpsmooth(startDay+.5, endDay+.5, t));
					var hour = lerpsmooth(startHour, endHour, t);
					core.setJDay(day+hour);
					core.moveToAltAzi(alt, az, 0.);
					StelMovementMgr.zoomTo(zoom,0);
					core.wait(0.01);
				""");
		output.add("""
					core.screenshot((""+i).padStart(4,"0"),false,"%s",true,"jpeg");
				}
				""".formatted(target.getAbsolutePath().replace("\\", "\\\\")));
		return output;
	}

	public static ArrayList<String> slideTo(File target, LocalDateTime start, long steps, Snapshot3D in, Snapshot3D out, Double midZoom) {
		ArrayList<String> output = new ArrayList<>();
		target.mkdirs();
		output.add(setTime(start));
		output.addAll(move(in.alt(), in.azi(), in.fov()));
		output.add("core.wait(3);");
		for (long step = 0; step <= steps; step++) {
			Snapshot3D snapshot5D;
			if (midZoom == null) {
				snapshot5D = Lerp3D.interpolateDirect(in, out, step / (double) steps);
			} else {
				snapshot5D = Lerp3D.interpolateMidzoom(in, out, step / (double) steps, midZoom);
			}
			//captureTimestamp(new File(target + "/timestamp/"), unJulian(snapshot5D.day() + snapshot5D.hour()), step);
			output.addAll(move(snapshot5D.alt(), snapshot5D.azi(), snapshot5D.fov()));
			output.add(cheese("%04d".formatted(step), target));
			output.add("core.wait(0.01);");
		}
		return output;
	}

	private static String setTime(LocalDateTime time) {
		return "core.setDate(\"%s\");".formatted(timeToString(time));
	}

	public static ArrayList<String> slideTo(File target, LocalDateTime start, LocalDateTime end, long steps, Snapshot3D in, Snapshot3D out, Double midZoom) {
		ArrayList<String> output = new ArrayList<>();
		System.out.println(target.getAbsolutePath());
		target.mkdirs();

		output.add(setTime(start));
		output.addAll(move(in.alt(), in.azi(), in.fov()));
		output.add("core.wait(3);");

		LocalDate startDays = start.toLocalDate();
		LocalDate endDays = end.toLocalDate();

		LocalTime startHours = start.toLocalTime();
		LocalTime endHours = end.toLocalTime();
		for (long step = 0; step <= steps; step++) {
			Snapshot5D snapshot5D;
			if (midZoom == null) {
				snapshot5D = Lerp5D.interpolateDirect(in.convertTo5D(startDays, startHours), out.convertTo5D(endDays, endHours), step / (double) steps);
			} else {
				snapshot5D = Lerp5D.interpolateMidzoom(in.convertTo5D(startDays, startHours), out.convertTo5D(endDays, endHours), step / (double) steps, midZoom);
			}
			//captureTimestamp(new File(target + "/timestamp/"), unJulian(snapshot5D.day() + snapshot5D.hour()), step);
			output.addAll(move(LocalDateTime.of(snapshot5D.day(), snapshot5D.hour()), snapshot5D.stripTime()));
			output.add(cheese("%04d".formatted(step), target));
			output.add("core.wait(0.001);");
		}
		return output;
	}

	public static String cheese(String filename, File target) {
		if (!target.exists()) target.mkdirs();
		return "core.screenshot(\"%s\",false,\"%s\",true,\"jpeg\");".formatted(filename, target.getAbsolutePath().replace("\\", "\\\\"));
	}

//	public static void midpoint(LocalDateTime start, LocalDateTime end) throws IOException {
//		long startDays = integerPart(julian(start));
//		long endDays = integerPart(julian(end));
//
//		double startHours = fractionalPart(julian(start));
//		double endHours = fractionalPart(julian(end));
//
//		Snapshot5D snapshot5D = Lerp5D.interpolateDirect(
//				new Snapshot5D(145.9676, 9.1711, 16, startDays, startHours),
//				new Snapshot5D(216.4507, 11.0995, 40, endDays, endHours),
//				.5
//		);
//		setJD(snapshot5D.day() + snapshot5D.hour());
//		//move(snapshot5D.alt(), snapshot5D.azi(), );
//		zoom(snapshot5D.fov());
//	}

	public static ArrayList<String> move(double alt, double az, double fov) {
		ArrayList<String> out = new ArrayList<>();
		out.add("core.moveToAltAzi(%f, %f, 0.);".formatted(alt, az));
		out.add("StelMovementMgr.zoomTo(%f,0);".formatted(fov));
		return out;
	}

	public static ArrayList<String> move(LocalDateTime time, Snapshot3D orientation) {
		ArrayList<String> out = new ArrayList<>();
		out.add("core.moveToAltAzi(%f, %f, 0.);".formatted(orientation.alt(), orientation.azi()));
		out.add("StelMovementMgr.zoomTo(%f,0);".formatted(orientation.fov()));
		out.add("core.setDate(\"%s\");".formatted(time.minusHours(1).format(DateTimeFormatter.ISO_DATE_TIME)));
		return out;
	}

	public static double julian(LocalDateTime date) {
		ZonedDateTime d = date.atZone(ZoneId.of("Europe/Prague"));
		ZonedDateTime jul = ZonedDateTime.of(-4712, 1, 1, 12, 0, 0, 0, ZoneId.of("UTC"));
		long secs = ChronoUnit.SECONDS.between(jul, d);
		return (secs / 86400d) + 38;
	}
}