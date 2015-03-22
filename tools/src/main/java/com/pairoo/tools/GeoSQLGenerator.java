package com.pairoo.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.util.Scanner;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;

/**
 * Hello world!
 *
 */
public class GeoSQLGenerator {
	private static StringBuffer sbCityEnum = new StringBuffer();
	private static StringBuffer sbTranslationXML = new StringBuffer();
	private static StringBuffer sbCountryEnum = new StringBuffer();
	private static String lastCountryVar = "";

	private final File csvDirectory;
	private Vector<String> subdivisionNames = null;
	private BufferedWriter bufferedWriterCities = null;
	private BufferedWriter bufferedWriterSubdivisions = null;
	private int subdivisionCounter = 0;

	public static void main(String... aArgs) throws Exception {
		GeoSQLGenerator parser = new GeoSQLGenerator(
				"/home/ralf/Desktop/WORKSPACES/Workspace-JavaPro/com.pairoo--parent/backend/src/main/resources/com/pairoo/backend/db/flyway/");
		File[] files = parser.csvDirectory.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".csv");
			}
		});
		FileWriter fstreamCities = new FileWriter("db-insert-cities.sql");
		FileWriter fstreamSubdivisions = new FileWriter(
				"db-insert-subdivisions.sql");
		parser.bufferedWriterCities = new BufferedWriter(fstreamCities);
		parser.bufferedWriterSubdivisions = new BufferedWriter(
				fstreamSubdivisions);

		try {
			for (File file : files) {
				parser.processLineByLine(file);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		parser.bufferedWriterCities.close();
		parser.bufferedWriterSubdivisions.close();
		log("Done.");
		// log(sbCityEnum.toString());
		// log(sbTranslationXML.toString());
		// log(sbCountryEnum.append("} )").toString());
	}

	/**
	 * Constructor.
	 *
	 * @param aFileName
	 *            full name of an existing, readable file.
	 */
	public GeoSQLGenerator(String pDirectory) {
		csvDirectory = new File(pDirectory);
		if (!csvDirectory.isDirectory()) {
			throw new IllegalArgumentException(csvDirectory.getAbsolutePath()
					+ " is no directory!");
		}
	}

	/**
	 * Template method that calls {@link #processLine(String)}.
	 *
	 * @param file
	 *            file to be parsed
	 */
	public final void processLineByLine(File file) throws Exception {
		subdivisionNames = new Vector<String>();

		// Note that FileReader is used, not File, since File is not Closeable
		Scanner scanner = new Scanner(new FileReader(file));
		try {
			// first use a Scanner to get each line
			while (scanner.hasNextLine()) {
				processLine(scanner.nextLine());
			}
		} finally {
			// ensure the underlying stream is always closed
			// this only has any effect if the item passed to the Scanner
			// constructor implements Closeable (which it does in this case).
			scanner.close();
		}
	}

	/**
	 * Overridable method for processing lines in different ways. e.g.:
	 * "Countrycode"
	 * ,"Country","City/Town","Population 2010","latitude","longitude"
	 * ,"Province/State","name variants",
	 * "AF","Afghanistan","Ačīn","12 687","34.12°N"
	 * ,"70.70°E","Nangarhār","Achīn, Achin",
	 *
	 * @throws Exception
	 */
	protected void processLine(String aLine) throws Exception {
		if (!aLine.contains("gazetteer.com") && !aLine.contains("Countrycode")) {
			String[] tokens = StringUtils
					.splitByWholeSeparatorPreserveAllTokens(aLine, "|");
			String countryCode = clean(tokens[0]);
			String countryName = clean(tokens[1]);
			String cityName = clean(tokens[2]);
			String populationStr = clean(tokens[3]);
			String latitudeStr = clean(tokens[4]);
			String longitudeStr = clean(tokens[5]);
			String subdivisionName = clean(tokens[6]);

			int population = -1;
			// population
			if (StringUtils.isNotBlank(populationStr)) {
				populationStr = populationStr.replaceAll("\\D", "");
				population = Integer.parseInt(populationStr);
			}

			// 4748N 00937E
			double latitude = 0;
			double longitude = 0;

			if (StringUtils.isNotBlank(latitudeStr)) {
				int factor = 1;
				if (latitudeStr.endsWith("S")) {
					factor = -1;
				}
				try {
					latitudeStr = latitudeStr.substring(0, latitudeStr
							.indexOf("°"));
				} catch (Exception e) {
					System.out.println(latitudeStr);
					// TODO Auto-generated catch block
					throw (e);
				}
				latitude = factor * Double.parseDouble(latitudeStr);

				factor = 1;
				if (longitudeStr.endsWith("W")) {
					factor = -1;
				}
				try {
					longitudeStr = longitudeStr.substring(0, longitudeStr
							.indexOf("°"));
				} catch (Exception e) {
					System.out.println(longitudeStr);
					// TODO Auto-generated catch block
					throw (e);
				}
				longitude = factor * Double.parseDouble(longitudeStr);
			}

			// log("country code is : " + countryCode + ", country name is : "
			// + countryName + ", city name is : " + cityName
			// + ", population is : " + population + ", latitude is : "
			// + latitude + ", longitude is : " + longitude
			// + ", subdivision code is : " + subdivisionName);

			// create SQL statements
			if (subdivisionName != null
					&& StringUtils.isNotBlank(subdivisionName)
					&& !subdivisionNames.contains(subdivisionName)) {
				subdivisionNames.add(subdivisionName);
				++subdivisionCounter;

				subdivisionName = subdivisionName.replaceAll("'", "''");
				String sql = "insert into SUBDIVISION(id, name, country) values("
						+ subdivisionCounter
						+ ", '"
						+ subdivisionName
						+ "', '"
						+ countryCode + "');";
				log(sql);

				bufferedWriterSubdivisions.newLine();
				bufferedWriterSubdivisions.write(sql);
			}
			// log("insert into CITY(name, latitude, longitude, subdivision) values('"
			// + cityName
			// + "', "
			// + latitude
			// + ", "
			// + longitude
			// + ", '"
			// + subdivisionName + "');");
		} else {
			// no city line
		}
	}

	private String clean(String string) {
		String result = null;
		if (StringUtils.isNotBlank(string)) {
			result = string.trim();
			result = result.replaceAll("\"", "");
		}
		return result;
	}

	private double deg2dec(int degree, int minutes, int seconds) {
		double dec = (double) degree + ((double) minutes / 60)
				+ ((double) seconds / (60 * 60));
		return dec;
	}

	private static void log(Object aObject) {
		System.out.println(String.valueOf(aObject));
	}

	private String quote(String aText) {
		String QUOTE = "'";
		return QUOTE + convertToAscii(aText) + QUOTE;
	}

	private String convertToAscii(String text) {
		text = text.replaceAll("Ā", "A");
		text = text.replaceAll("Ā", "A");
		text = text.replaceAll("À", "A");
		text = text.replaceAll("Á", "A");
		text = text.replaceAll("Ä", "AE");
		text = text.replaceAll("Ç", "C");
		text = text.replaceAll("ç", "c");
		text = text.replaceAll("Č", "C");
		text = text.replaceAll("Đ", "C");
		text = text.replaceAll("E", "E");
		text = text.replaceAll("Ë", "E");
		text = text.replaceAll("É", "E");
		text = text.replaceAll("Ə", "E");
		text = text.replaceAll("ə", "e");
		text = text.replaceAll("Ġ", "G");
		text = text.replaceAll("Ğ", "G");
		text = text.replaceAll("ğ", "g");
		text = text.replaceAll("H̱", "H");
		text = text.replaceAll("İ", "I");
		text = text.replaceAll("Ī", "I");
		text = text.replaceAll("Í", "I");
		text = text.replaceAll("ı", "i");
		text = text.replaceAll("M", "M");
		text = text.replaceAll("Ñ", "N");
		text = text.replaceAll("Ó", "O");
		text = text.replaceAll("Ò", "O");
		text = text.replaceAll("Ö", "OE");
		text = text.replaceAll("Ş", "S");
		text = text.replaceAll("Š", "S");
		text = text.replaceAll("ş", "s");
		text = text.replaceAll("Ţ", "T");
		text = text.replaceAll("Ū", "U");
		text = text.replaceAll("Ú", "U");
		text = text.replaceAll("Ü", "UE");

		text = text.replaceAll("'", "_");
		text = text.replaceAll("-", "_");
		text = text.replaceAll("\\.", "");
		text = text.replaceAll("\\(", "");
		text = text.replaceAll("\\)", "");

		return text;
	}
}
