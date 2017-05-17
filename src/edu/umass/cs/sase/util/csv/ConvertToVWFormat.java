/*
 * Copyright (c) 2017, Regents of the University of Massachusetts Amherst 
 * All rights reserved.

 * Redistribution and use in source and binary forms, with or without modification, are permitted 
 * provided that the following conditions are met:

 *   * Redistributions of source code must retain the above copyright notice, this list of conditions 
 * 		and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright notice, this list of conditions 
 * 		and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *   * Neither the name of the University of Massachusetts Amherst nor the names of its contributors 
 * 		may be used to endorse or promote products derived from this software without specific prior written 
 * 		permission.

 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR 
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS 
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE 
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, 
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF 
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package edu.umass.cs.sase.util.csv;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

/**
 * Make the last attribute as label
 * @author haopeng
 *
 */
public class ConvertToVWFormat {
	public void convertToVWFormat(String inputFilePath, String outputFilePath) throws IOException{
		CSVReader reader = new CSVReader(new FileReader(inputFilePath));
		CSVWriter writer = new CSVWriter(new FileWriter(outputFilePath), ' ',CSVWriter.NO_QUOTE_CHARACTER);
		String attributes[] = reader.readNext();
		String[] tupleValues;
		int lineCount = 0;
		while((tupleValues = reader.readNext()) != null){
			String outputValues[] = new String[attributes.length];
			outputValues[0] = tupleValues[tupleValues.length - 1] + " |";
			for(int i = 1; i < outputValues.length; i ++){
				outputValues[i] = attributes[i-1] + ":" + tupleValues[i-1];
			}
			writer.writeNext(outputValues);
			System.out.println("linecount:" + lineCount);
			lineCount ++;
		}
		
		reader.close();
		writer.flush();
		writer.close();
	}
	
	public static void main(String args[]) throws IOException{
		String inputFilePath = "I:\\Copy\\Data\\2013\\merged\\ordered-all-numeric-sliding-chopped-labeled.csv";
		String outputFilePath = "g:\\Data\\ordered-all-numeric-sliding-chopped-labeled.vw";
		ConvertToVWFormat converter = new ConvertToVWFormat();
		converter.convertToVWFormat(inputFilePath, outputFilePath);
	}

}
