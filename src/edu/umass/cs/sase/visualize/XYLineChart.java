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

package edu.umass.cs.sase.visualize;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeriesCollection;


/**
 * This class is used to draw xy line charts like this one: http://www.java2s.com/Code/Java/Chart/JFreeChartLineChartDemo6.htm
 * Input: 
 * Output file path
 * Dataset
 * Chart title
 * X label
 * Y label
 * @author haopeng
 *
 */
public class XYLineChart {
	String outputFilePath;
	XYSeriesCollection dataset;
	String chartTitle;
	String xLabel;
	String yLabel;
	
	JFreeChart chart;
	public XYLineChart(String outputFilePath, XYSeriesCollection dataset, String chartTitle, String xLabel, String yLabel) {
		this.outputFilePath = outputFilePath;
		this.dataset = dataset;
		this.chartTitle = chartTitle;
		this.xLabel = xLabel;
		this.yLabel = yLabel;
	}
	
	public XYLineChart(String outputFilePath, ArrayList<XYSeriesCollection> datasetList, String chartTitle, String xLabel, String yLabel) {
		this.outputFilePath = outputFilePath;
		this.chartTitle = chartTitle;
		this.xLabel = xLabel;
		this.yLabel = yLabel;
		this.mergeDatasetList(datasetList);
	}
	
	public void mergeDatasetList(ArrayList<XYSeriesCollection> datasetList) {
		this.dataset = new XYSeriesCollection();
		
		for (XYSeriesCollection c : datasetList) {
			for (int i = 0; i < c.getSeriesCount(); i ++) {
				this.dataset.addSeries(c.getSeries(i));
			}
		}
	}

	
	public void visualize(){
		this.createPlot();
		try {
			this.saveToPNGFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	public void createPlot() {
		System.out.println("Creating plot ...");
		// create the chart...
        this.chart = ChartFactory.createXYLineChart(
            this.chartTitle,      // chart title
            this.xLabel,                      // x axis label
            this.yLabel,                      // y axis label
            this.dataset,                  // data
            PlotOrientation.VERTICAL,
            true,                     // include legend
            true,                     // tooltips
            false                     // urls
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);

        //        final StandardLegend legend = (StandardLegend) chart.getLegend();
        //      legend.setDisplaySeriesShapes(true);
        
        // get a reference to the plot for further customisation...
        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.lightGray);
        //    plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        
        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, false);
        renderer.setSeriesShapesVisible(1, false);
        plot.setRenderer(renderer);

        // change the auto tick unit selection to integer units only...
        //final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        //rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        
        // OPTIONAL CUSTOMISATION COMPLETED.
        System.out.println("Creating plot complete!");   
	}
	
	public void saveToPNGFile() throws IOException {
		BufferedImage image = this.chart.createBufferedImage(1600, 900);
		File f = new File(this.outputFilePath);
		ImageIO.write(image, "PNG", f);
		System.out.println("The visulization file has been saved to " + this.outputFilePath + "!!!!!!!!!");
	}
}
