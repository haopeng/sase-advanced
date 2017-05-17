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

package edu.umass.cs.sase.otherengine.xseq;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Map.Entry;

/**
 * This class is used to 
 * @author haopeng
 *
 */
public class StockXmlConverter {
	String xmlFilePath;
	ArrayList<SaxTuple> tuples;
	public StockXmlConverter(String xmlFilePath){
		this.xmlFilePath = xmlFilePath;
		this.tuples = new ArrayList<SaxTuple>();
	}
	
	/**
	 * convert xml to sax tuples
	 */
	public void parseFile(){
		 try {
				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser saxParser = factory.newSAXParser();
				DefaultHandler handler = new DefaultHandler() {
				boolean addNewTuple = true;
				
				public void startElement(String uri, String localName,String qName, 
			                Attributes attributes) throws SAXException {
					
					addNewTuple = false;
					SaxTuple newTuple = new SaxTuple("open", qName, "", "<" + qName + ">", 0);
					tuples.add(newTuple);
					if(qName.equals("AllStockTransaction") || qName.equals("StockTransaction")){
						addNewTuple = false;
					}else{
						addNewTuple = true;
					}
					//process the attributes
					if(attributes.getLength() > 0){
						for(int i = 0; i < attributes.getLength(); i ++){
							String attName = attributes.getQName(i);
							String attValue = attributes.getValue(i);
							
							System.out.println(attName + "=:" + attValue);
							SaxTuple newAttTuple = new SaxTuple("attribute", attName, attValue, "", 0);
							tuples.add(newAttTuple);

						}

					}

				}
			 	public void endElement(String uri, String localName,
					String qName) throws SAXException {
					addNewTuple = false;
					SaxTuple newTuple = new SaxTuple("close", qName, "", "</" + qName + ">", 0);
					tuples.add(newTuple);
				}
			 
				public void characters(char ch[], int start, int length) throws SAXException {
					if(addNewTuple){
						String text = new String(ch, start, length);
						SaxTuple newTuple = new SaxTuple("text","", text, "", 0);
						tuples.add(newTuple);
					}
				}
			  };
			       saxParser.parse(this.xmlFilePath, handler);
			     } catch (Exception e) {
			       e.printStackTrace();
			     }
	}
	
	/**
	 * Add the skip numbers to tuples
	 */
	public void addSkipNumber(){
		HashMap<String, Integer> counters = new HashMap<String, Integer>();
		for(int i = this.tuples.size() - 1; i >= 0; i --){
			SaxTuple currentTuple = this.tuples.get(i);
			if(currentTuple.getType().equalsIgnoreCase("open")){
				int count = counters.get(currentTuple.getToken());
				currentTuple.setSkip(count);
				//everthing + 1
				for(Entry<String, Integer> entry : counters.entrySet()){
					int oldCount = entry.getValue();
					entry.setValue(oldCount + 1);
				}
				
			}else if(currentTuple.getType().equalsIgnoreCase("close")){
				for(Entry<String, Integer> entry : counters.entrySet()){
					int oldCount = entry.getValue();
					entry.setValue(oldCount + 1);
				}
				//reset its own tag to 1
				String token = currentTuple.getToken();
				counters.put(token, 1);
			}else{
				//everthing + 1
				for(Entry<String, Integer> entry : counters.entrySet()){
					int oldCount = entry.getValue();
					entry.setValue(oldCount + 1);
				}
			}
			
		}
	}
	/**
	 * Output result
	 */
	public void outputSax(String outputFile){
		try {
			File file = new File(outputFile);
 			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
			for(int i = 0; i < this.tuples.size(); i ++){
				System.out.println(this.tuples.get(i));
				bw.write(this.tuples.get(i).toString());
				bw.newLine();
			}
			bw.close();
			System.out.println("Done");
 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String xmlFilePath = "F:\\Dropbox\\code\\xseq\\myquery2\\stocks30.xml";
		String saxFilePath = "F:\\Dropbox\\code\\xseq\\myquery2\\sax30.txt";
		StockXmlConverter converter = new StockXmlConverter(xmlFilePath);
		converter.parseFile();
		converter.addSkipNumber();
		converter.outputSax(saxFilePath);
	}

}
