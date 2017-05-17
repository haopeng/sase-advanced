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
package edu.umass.cs.sase.stream;

import java.io.IOException;
import java.util.Random;
import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
 

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.umass.cs.sase.explanation.simulation.SimulationSettings;

/**
 * This class wraps the stream, specifies how to generate or import stream.
 * @author haopeng
 *
 */
public class StreamController {
	/**
	 * The stream
	 */
	Stream myStream;
	/**
	 * The size of the stream
	 */
	int size;
	
	/**
	 * event id
	 */
	int eventID;
	
	/**
	 * A random number generator
	 */
	Random randomGenerator;
	
	/**
	 * Default constructor
	 */
	public StreamController(){
		eventID = 0;
		randomGenerator = new Random(11);
	}
	
	/**
	 * Constructor, specified size and event type
	 * @param size
	 * @param eventType
	 */
	public StreamController(int size, String eventType){
		this.size = size;
		myStream = new Stream(size);
		if(eventType.equalsIgnoreCase("abcevent")){
			this.generateABCEvents();
		}
		if(eventType.equalsIgnoreCase("stockevent")){
			this.generateStockEvents();
		}
	}
	/**
	 * Generates a series of stock events
	 */
	public void generateStockEventsAsConfig(){
		Random r = new Random(StockStreamConfig.randomSeed);
		StockEvent events[] = new StockEvent[this.size];
		int id;
		int timestamp = 0;
		int symbol;
		int volume;
		int price = r.nextInt(100);
		for (int i = 0; i < size; i ++){
			id = i;
			timestamp = id;
			symbol = r.nextInt(StockStreamConfig.numOfSymbol) + 1; 
			price = r.nextInt(StockStreamConfig.maxPrice) + 1;
			volume = r.nextInt(StockStreamConfig.maxVolume) + 1;
			events[i] = new StockEvent(id, timestamp, symbol, price, volume);				
		}
		myStream.setEvents(events);
	}
	
	/**
	 * Generates a series of stock events
	 * it is here
	 */
	public void generateStockEventsAsConfigType(){
		if(StockStreamConfig.increaseProbability > 100){
		Random r = new Random(StockStreamConfig.randomSeed);
		StockEvent events[] = new StockEvent[this.size];
		int id;
		int timestamp = 0;
		int symbol;
		int volume;
		int price = r.nextInt(100);
		String eventType = "stock";
		
		
		
		for (int i = 0; i < size; i ++){
			id = i;
			timestamp = id;
			
			symbol = r.nextInt(StockStreamConfig.numOfSymbol) + 1; 
			price = r.nextInt(StockStreamConfig.maxPrice) + 1;
			volume = r.nextInt(StockStreamConfig.maxVolume) + 1;
			eventType = "stock" + symbol;
			events[i] = new StockEvent(id, timestamp, symbol, price, volume, eventType);				
		}
		myStream.setEvents(events);
		
		}else{
			this.generateStockEventsWithIncreaseProbability();
		}
	}
	
	/**
	 * Generates a series of stock events
	 * it is here
	 */
	public void generateSimulationStockEventsAsConfigType(){

		Random r = new Random(StockStreamConfig.randomSeed);
		StockEvent events[] = new StockEvent[this.size];
		int id;
		int timestamp = 0;
		int symbol;
		int volume;
		int price = r.nextInt(100);
		String eventType = "stock";
		
		
		
		for (int i = 0; i < size; i ++){
			id = i;
			timestamp = id / (int)SimulationSettings.streamRate;
			
			symbol = r.nextInt(StockStreamConfig.numOfSymbol) + 1; 
			price = r.nextInt(StockStreamConfig.maxPrice) + 1;
			volume = r.nextInt(StockStreamConfig.maxVolume) + 1;
			eventType = "stock" + symbol;
			//eventType = "stock";
			events[i] = new StockEvent(id, timestamp, symbol, price, volume, eventType);				
		}
		myStream.setEvents(events);

	}
	
	
	/**
	 * Generates a series of stock events
	 */
	public void generateUncertainStockEventsAsConfigType(){
		if(StockStreamConfig.increaseProbability > 100){
		Random r = new Random(StockStreamConfig.randomSeed);
		UncertainStockEvent events[] = new UncertainStockEvent[this.size];
		int id;
		int timestamp = 0;

		int symbol;
		int volume;
		int price = r.nextInt(100);
		
		String eventType = "stock";
		
		
		
		for (int i = 0; i < size; i ++){
			id = i;
			timestamp = id;
					
			symbol = r.nextInt(StockStreamConfig.numOfSymbol) + 1; 
			price = r.nextInt(StockStreamConfig.maxPrice) + 1;
			volume = r.nextInt(StockStreamConfig.maxVolume) + 1;
			eventType = "stock" + symbol;
			
			events[i] = new UncertainStockEvent(id, timestamp, symbol, price, volume, eventType, StockStreamConfig.uncertaintyInterval);				
		}
		myStream.setEvents(events);
		
		}else{
			this.generateUncertainStockEventsWithIncreaseProbability();
		}
	}
	
	/**
	 * Generates a series of stock events
	 */
	public void generateStockEventsWithIncreaseProbability(){
		
		Random r = new Random(StockStreamConfig.randomSeed);
		StockEvent events[] = new StockEvent[this.size];
		int id;
		int timestamp = 0;
		int symbol;
		int volume;
		int price[] = new int[StockStreamConfig.numOfSymbol];
		for(int i = 0; i < StockStreamConfig.numOfSymbol; i ++){
			//initializes the prices of each stock
			price[i] = r.nextInt(1000);
		}
		
		int random = 0;
		String eventType = "stock";
		
		
		
		for (int i = 0; i < size; i ++){
			id = i;
			timestamp = id;			
			symbol = r.nextInt(StockStreamConfig.numOfSymbol) + 1;
			random = r.nextInt(100) + 1;
			if(random <= StockStreamConfig.increaseProbability){
				//increase
				price[symbol - 1] += (r.nextInt(3) + 1);
			}else if(random > (100 + StockStreamConfig.increaseProbability) / 2){
				// decrease
				price[symbol - 1] -= (r.nextInt(3) + 1);				
			}
			
			
			volume = r.nextInt(StockStreamConfig.maxVolume) + 1;
			eventType = "stock";
			events[i] = new StockEvent(id, timestamp, symbol, price[symbol - 1], volume, eventType);				
		}
		myStream.setEvents(events);
		
		
	}
	
	public void generateUncertainStockEventsWithIncreaseProbability(){
		
		Random r = new Random(StockStreamConfig.randomSeed);
		UncertainStockEvent events[] = new UncertainStockEvent[this.size];
		int id;
		int timestamp = 0;
		int symbol;
		int volume;
		int price[] = new int[StockStreamConfig.numOfSymbol];
		for(int i = 0; i < StockStreamConfig.numOfSymbol; i ++){
			//initializes the prices of each stock
			price[i] = r.nextInt(1000);
		}
		
		int random = 0;
		String eventType = "stock";
		
		
		
		for (int i = 0; i < size; i ++){
			id = i;
			timestamp = id;			
			symbol = r.nextInt(StockStreamConfig.numOfSymbol) + 1;
			random = r.nextInt(100) + 1;
			if(random <= StockStreamConfig.increaseProbability){
				//increase
				price[symbol - 1] += (r.nextInt(3) + 1);
			}else if(random > (100 + StockStreamConfig.increaseProbability) / 2){
				// decrease
				price[symbol - 1] -= (r.nextInt(3) + 1);				
			}
			
			
			volume = r.nextInt(StockStreamConfig.maxVolume) + 1;
			eventType = "stock";
			events[i] = new UncertainStockEvent(id, timestamp, symbol, price[symbol - 1], volume, eventType, StockStreamConfig.uncertaintyInterval);				
		}
		myStream.setEvents(events);
		
		
	}
	
	/**
	 * Generates a series of stock events
	 */
	public void generateStockEvents(){
		Random r = new Random(11);
		StockEvent events[] = new StockEvent[this.size];
		int id;
		int timestamp = 0;
		int symbol;
		int volume;
		int price = r.nextInt(100);
		int random = 0;
		for (int i = 0; i < size; i ++){
			id = i;
			timestamp = id;
			symbol = r.nextInt(2); //0 or 1
			random = r.nextInt(100);
			if(random < 55){
				price += r.nextInt(5);
			}else if(random >= 55 && random < 77){
				price -= r.nextInt(5);
			}
			volume = r.nextInt(1000);
			events[i] = new StockEvent(id, timestamp, symbol, price, volume);	
		}
		myStream.setEvents(events);
	}
	
	/**
	 * Generates another batch of stock events
	 * @param number the size of the stream
	 */
	public void generateNextStockEvents(int number){
		
		StockEvent events[] = new StockEvent[number];
		int id;
		int timestamp = 0;
		int symbol;
		int volume;
		int price = this.randomGenerator.nextInt(100);
		int random = 0;
		for (int i = 0; i < number; i ++){
			id = this.eventID;
			timestamp = id;
			symbol = this.randomGenerator.nextInt(2); //0 or 1
			random = this.randomGenerator.nextInt(100);
			if(random < 55){
				price += this.randomGenerator.nextInt(5);
			}else if(random >= 55 && random < 77){
				price -= this.randomGenerator.nextInt(5);
			}
			volume = this.randomGenerator.nextInt(1000);
			
			events[i] = new StockEvent(id, timestamp, symbol, price, volume);	
			this.eventID ++;
			
			
		}
		this.myStream = new Stream(number);
		myStream.setEvents(events);
	}
	
	/**
	 * Generates ABCEvents for the stream
	 */
	public void generateABCEvents(){
		// this is for correctness test
		Random r = new Random(11);
		ABCEvent events[] = new ABCEvent[this.size];
		int id;
		int timestamp = 0;
		int random = 0;
		String eventType = "";
		int price = 50;
		for (int i = 0; i < size; i ++){
			id = i;
			random = r.nextInt(3);
			timestamp = i;
			switch(random){
			case 0:
				eventType = "a";
				break;
			case 1:
				eventType = "b";
				price += 1;
				break;
			case 2:
				eventType = "c";
				price += 2;
				break;
			case 3:
				eventType = "d";
				price += 3;
				break;
			
			}
			
			events[i] = new ABCEvent(id, timestamp, eventType, price);
			
		}
		
		myStream.setEvents(events);
	}

	/**
	 * @return the myStream
	 */
	public Stream getMyStream() {
		return myStream;
	}

	/**
	 * @param myStream the myStream to set
	 */
	public void setMyStream(Stream myStream) {
		this.myStream = myStream;
		this.setSize(myStream.getSize());
	}

	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}
	
	/**
	 * Outputs the events in the stream one by one in the console
	 */
	public void printStream(){
		int counter = 0;
		for(int i = 0; i < myStream.getSize(); i ++){
			
			//System.out.println("The "+ i + " event out of " + size + " events is: " + myStream.getEvents()[i].toString());
			System.out.println(myStream.getEvents()[i].toString());
			/*
			int price = myStream.getEvents()[i].getAttributeByName("price");
			if(price % 500 == 0){
				counter ++;
			}*/
		}
		System.out.println("Counter = " + counter);
	}
	
	public void convertToXMLFile(String xmlOutputPath) throws ParserConfigurationException, TransformerException{
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
 
		// root elements
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("AllStockTransaction");
		doc.appendChild(rootElement);
 
		for(int i = 0; i < myStream.getSize(); i ++){
			Event e = myStream.getEvents()[i];
			// staff elements
			Element staff = doc.createElement("StockTransaction");
			rootElement.appendChild(staff);
	 
			/*
			// set attribute to staff element
			Attr attr = doc.createAttribute("id");
			attr.setValue("" + i);
			staff.setAttributeNode(attr);
	 		*/
			// shorten way
			// staff.setAttribute("id", "1");
	 
			// ID elements
			Element idElement = doc.createElement("ID");
			idElement.appendChild(doc.createTextNode("" + e.getId()));
			staff.appendChild(idElement);
	 
			// Timestamp elements
			Element timestampElement = doc.createElement("Timestamp");
			timestampElement.appendChild(doc.createTextNode("" + e.getTimestamp()));
			staff.appendChild(timestampElement);
	 
			// Symbol elements
			Element symbolElement = doc.createElement("Symbol");
			symbolElement.appendChild(doc.createTextNode("" + e.getAttributeByName("symbol") ));
			staff.appendChild(symbolElement);
	 
			// Price elements
			Element priceElement = doc.createElement("Price");
			priceElement.appendChild(doc.createTextNode("" + e.getAttributeByName("price")));
			staff.appendChild(priceElement);

			// Price elements
			Element volumeElement = doc.createElement("Volume");
			volumeElement.appendChild(doc.createTextNode("" + e.getAttributeByName("volume")));
			staff.appendChild(volumeElement);

			
		}
 
		
		
		
		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(xmlOutputPath));
 
		// Output to console for testing
		// StreamResult result = new StreamResult(System.out);
 
		transformer.transform(source, result);
 
		System.out.println("File saved!");
	}
	public void convertToXMLFileWithAttributes(String xmlOutputPath) throws ParserConfigurationException, TransformerException{
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
 
		// root elements
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("AllStockTransaction");
		doc.appendChild(rootElement);
 
		for(int i = 0; i < myStream.getSize(); i ++){
			Event e = myStream.getEvents()[i];
			// staff elements
			Element transaction = doc.createElement("StockTransaction");
			rootElement.appendChild(transaction);
			
			Attr idAttr = doc.createAttribute("ID");
			idAttr.setValue("" + e.getId());
			transaction.setAttributeNode(idAttr);
			
			Attr timestampAttr = doc.createAttribute("Timestamp");
			timestampAttr.setValue("" + e.getTimestamp());
			transaction.setAttributeNode(timestampAttr);
			
			Attr symbolAttr = doc.createAttribute("Symbol");
			symbolAttr.setValue("" + e.getAttributeByName("symbol"));
			transaction.setAttributeNode(symbolAttr);
			
			Attr priceAttr = doc.createAttribute("Price");
			//note here, this change is to fit the xseq engine
			//because xseq doesn't support mode operation!! 
			if(e.getAttributeByName("price") % 500 == 0){
				priceAttr.setValue("500");
			}else{
				priceAttr.setValue("" + e.getAttributeByName("price"));
			}
			
			transaction.setAttributeNode(priceAttr);
			
			Attr volumeAttr = doc.createAttribute("Volume");
			volumeAttr.setValue("" + e.getAttributeByName("volume"));
			transaction.setAttributeNode(volumeAttr);
			
			
			/*
			// set attribute to staff element
			Attr attr = doc.createAttribute("id");
			attr.setValue("" + i);
			staff.setAttributeNode(attr);
	 		*/
			// shorten way
			// staff.setAttribute("id", "1");
	 
		}
 
		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(xmlOutputPath));
 
		// Output to console for testing
		// StreamResult result = new StreamResult(System.out);
 
		transformer.transform(source, result);
 
		System.out.println("File saved!");
	}
	
	public static void main(String args[]) throws IOException, ParserConfigurationException, TransformerException{
		System.out.println("haha");
		String streamConfigFile = "f:\\Dropbox\\code\\workspace\\sase-opensource\\queries\\selectivity10.stream";
		ParseStockStreamConfig.parseStockEventConfig(streamConfigFile);
		StockStreamConfig.printConfig();
		
		StreamController myStreamController = new StreamController(StockStreamConfig.streamSize,"StockEvent");
		myStreamController.generateStockEventsAsConfigType();	
		myStreamController.printStream();
		System.out.println("Writing to file...");
		//myStreamController.convertToXMLFile("f:\\Dropbox\\code\\xseq\\myquery\\stocks20000.xml");
		myStreamController.convertToXMLFileWithAttributes("f:\\Dropbox\\code\\xseq\\myquery2\\stocks30.xml");
	}
	
	
	
	
	

}
