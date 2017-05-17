package utils;

import java.io.FileReader;
import java.text.NumberFormat;

import au.com.bytecode.opencsv.CSVReader;

public class CSVExport {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			CSVReader reader = new CSVReader(new FileReader("stats.csv"));
			String [] nextLine;
			int count = 0;
			while ((nextLine = reader.readNext()) != null) {
				// nextLine[] is an array of values from the line
				for (int i = 1; i < nextLine.length; i++) {
					if (nextLine[i] == null || nextLine[i].length() == 0) {
						nextLine[i] = "-";
					}
					NumberFormat nf = NumberFormat.getInstance();
					nf.setMaximumFractionDigits(3);
					try {
						double value = Double.valueOf(nextLine[i]);
						nextLine[i] = nf.format(value);
					}
					catch (NumberFormatException e) {
						// don't need to do anything
					}
				}
				if (++count < 40) {
					//*
					System.out.println(nextLine[0] + " & " 
						+ nextLine[7] + " & " + nextLine[8] + " & "
						+ nextLine[9] + " & " + nextLine[10] + " & "
						+ nextLine[11] + " & " + nextLine[12] + " & "
						+ nextLine[14] + " & " + nextLine[13] + " & "
						+ nextLine[15] + " & " + nextLine[16] + " & "
						+ nextLine[17] + " & " + nextLine[18] + " & "
						+ nextLine[19] + " & " + nextLine[20] + "\\\\");
					//*/
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
