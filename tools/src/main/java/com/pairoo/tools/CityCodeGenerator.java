package com.pairoo.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import org.apache.commons.lang.StringUtils;

/**
 * Hello world!
 *
 */
public class CityCodeGenerator {
	private static StringBuffer sbCityEnum = new StringBuffer();
	private static StringBuffer sbTranslationXML = new StringBuffer();
	private static StringBuffer sbCountryEnum = new StringBuffer();
	private static String lastCountryVar = "";

	public static void main(String... aArgs) throws FileNotFoundException {
		// CityCodeGenerator parser = new
		// CityCodeGenerator("/home/ralf/Desktop/WORKSPACES/Workspace-JavaPro/com.pairoo--parent/resources/Data/cities-of-the-world.csv");
		CityCodeGenerator parser = new CityCodeGenerator(
				"/home/ralf/Desktop/WORKSPACES/Workspace-JavaPro/com.pairoo--parent/resources/Data/loc102ma/2010-2 UNLOCODE CodeList.txt");
		parser.processLineByLine();
		log("Done.");
		log(sbCityEnum.toString());
		log(sbTranslationXML.toString());
		log(sbCountryEnum.append("} )").toString());
	}

	/**
	 * Constructor.
	 *
	 * @param aFileName
	 *            full name of an existing, readable file.
	 */
	public CityCodeGenerator(String aFileName) {
		fFile = new File(aFileName);
	}

	/** Template method that calls {@link #processLine(String)}. */
	public final void processLineByLine() throws FileNotFoundException {
		// Note that FileReader is used, not File, since File is not Closeable
		Scanner scanner = new Scanner(new FileReader(fFile));
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
	 * Overridable method for processing lines in different ways.
	 *
	 * <P>
	 * This simple default implementation expects simple name-value pairs,
	 * separated by an '=' sign. Examples of valid input :
	 * <tt>height = 167cm</tt> <tt>mass =  65kg</tt>
	 * <tt>disposition =  "grumpy"</tt>
	 * <tt>this is the name = this is the value</tt>
	 */
	protected void processLine(String aLine) {
		// use a second Scanner to parse the content of each line
		if (aLine.length() > 100) {
			String countryCode = aLine.substring(3, 5);
			String locode = aLine.substring(6, 9);
			String cityName = aLine.substring(11, 43);

			if (!cityName.startsWith(".")) {
				String cityNameClean = aLine.substring(43, 75);
				String subdivisionCode = aLine.substring(75, 78);
				String status = aLine.substring(79, 81);
				String function = aLine.substring(82, 90);
				String date = aLine.substring(91, 95);
				String latitudeLongitude = aLine.substring(99, 111);

				// countryCode = countryCode.trim().replaceAll("\"", "");
				// locode = locode.trim().replaceAll("\"", "");
				// cityName = cityName.trim().replaceAll("\"", "");
				// cityNameClean = cityNameClean.trim().replaceAll("\"", "");
				// subdivisionCode = subdivisionCode.trim().replaceAll("\"",
				// "");
				// function = function.trim().replaceAll("\"", "");
				// status = status.trim().replaceAll("\"", "");
				// date = date.trim().replaceAll("\"", "");
				// latitudeLongitude = latitudeLongitude.trim().replaceAll("\"",
				// "");

				// 4748N 00937E
				String latitudeStr = "";
				String longitudeStr = "";
				double latitude = 0;
				double longitude = 0;

				if (StringUtils.isNotBlank(latitudeLongitude)) {
					String[] st2 = latitudeLongitude.split("\\s");
					latitudeStr = st2[0];
					longitudeStr = st2[1];

					int factor = 1;
					if (latitudeStr.endsWith("S")) {
						factor = -1;
					}
					int degrees = Integer.parseInt(latitudeStr.substring(0,2));
					int minutes = Integer.parseInt(latitudeStr.substring(2,4));
					latitude = factor * deg2dec(degrees, minutes, 0);

					factor = 1;
					if (longitudeStr.endsWith("W")) {
						factor = -1;
					}
					degrees = Integer.parseInt(longitudeStr.substring(1,3));
					minutes = Integer.parseInt(longitudeStr.substring(3,5));
					longitude = factor * deg2dec(degrees, minutes, 0);
				}
				// String countryVar = convertToAscii(locode.toUpperCase())
				// .replaceAll(" ", "_");
				// String cityNameVar = convertToAscii(cityName.toUpperCase())
				// .replaceAll(" ", "_");
				// String cityNameCleanVar = convertToAscii(
				// cityNameClean.toUpperCase()).replaceAll(" ", "_");
				// String latitudeVar =
				// convertToAscii(latitudeStr.toUpperCase())
				// .replaceAll(" ", "_");
				// String longitudeVar =
				// convertToAscii(longitudeStr.toUpperCase())
				// .replaceAll(" ", "_");

				log("country code is : " + quote(countryCode)
						+ ", locode is : " + quote(locode)
						+ ", city name is : " + quote(cityName)
						+ ", city name clean is : " + quote(cityNameClean)
						+ ", subdivision code is : " + quote(subdivisionCode)
						+ ", function is : " + quote(function)
						+ ", status is : " + quote(status) + ", date is : "
						+ quote(date) + ", latitude and longitude is : "
						+ quote(latitudeLongitude) + ", latitude is : "
						+ quote(latitudeStr) + "/" + latitude
						+ ", longitude is : " + quote(longitudeStr) + "/"
						+ longitude);

				// create SQL statement

				// create City-enum entry
				// e.g.: ARASJI(Country.ARUBA),
				// sbCityEnum.append(cityNameVar).append("(Country.").append(
				// countryVar).append("),");
				//
				// sbTranslationXML.append("<entry key=\"City.").append(
				// cityNameVar).append("\">").append(cityName).append(
				// "</entry>\n");
				//
				// if (!lastCountryVar.equals(countryVar)) {
				// sbCountryEnum.append("} ),\n").append(countryVar).append(
				// "(new City[] {");
				// lastCountryVar = countryVar;
				// }
				// sbCountryEnum.append("City.").append(cityNameVar).append(", ");
			} else {
				// no city line
			}
		}
	}

	private double deg2dec(int degree, int minutes, int seconds) {
		double dec = (double) degree + ((double) minutes / 60)
				+ ((double) seconds / (60 * 60));
		return dec;
	}

	// PRIVATE
	private final File fFile;

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
