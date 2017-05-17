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
package edu.umass.cs.sase.cluster;


import java.awt.Color;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;



import java.util.Random;

import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.filters.api.FilterController;
import org.gephi.filters.api.Query;
import org.gephi.filters.api.Range;
import org.gephi.filters.plugin.graph.DegreeRangeBuilder.DegreeRangeFilter;
import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.GraphView;
import org.gephi.graph.api.Node;
import org.gephi.graph.api.UndirectedGraph;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.importer.api.ImportController;
import org.gephi.layout.plugin.force.StepDisplacement;
import org.gephi.layout.plugin.force.yifanHu.YifanHuLayout;
import org.gephi.layout.plugin.forceAtlas.ForceAtlasLayout;
import org.gephi.layout.plugin.forceAtlas2.ForceAtlas2;
import org.gephi.layout.plugin.fruchterman.FruchtermanReingold;
import org.gephi.layout.plugin.scale.ScaleLayout;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.types.EdgeColor;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.gephi.ranking.api.Ranking;
import org.gephi.ranking.api.RankingController;
import org.gephi.ranking.api.Transformer;
import org.gephi.ranking.plugin.transformer.AbstractColorTransformer;
import org.gephi.ranking.plugin.transformer.AbstractSizeTransformer;
import org.gephi.statistics.plugin.GraphDistance;
import org.openide.util.Lookup;

import edu.umass.cs.sase.explanation.distancefunction.TimeSeriesFeaturePair;
import au.com.bytecode.opencsv.CSVReader;


/**
 * Reference: https://github.com/gephi/gephi/wiki/How-to-code-with-the-Toolkit
 * https://github.com/gephi/gephi/wiki/Headless-Example
 * @author haopeng
 *
 */
public class GephiCluster {
	String featureCSVPath;
	String correlationCSVPath;
	
	ArrayList<String> features;
	HashMap<String, Double> correlationIndex;
	public GephiCluster(String featureCSV, String correlationCSV) throws IOException {
		this.featureCSVPath = featureCSV;
		this.correlationCSVPath = correlationCSV;
		
		this.readFeatures();
		this.readCorrelations();
	}
	
	public void readFeatures() throws IOException {
		this.features = new ArrayList<String>();
		
		CSVReader reader = new CSVReader(new FileReader(this.featureCSVPath));
		String[] line = null;
		while ((line = reader.readNext()) != null) {
			features.add(line[1]);
		}
		reader.close();
	}
	
	
	
	public void readCorrelations() throws IOException {
		this.correlationIndex = new HashMap<String, Double>();
		
		CSVReader reader = new CSVReader(new FileReader(this.correlationCSVPath));
		String[] line = null;
	
		while ((line = reader.readNext()) != null) {
			String key = line[0];
			double value = Double.parseDouble(line[1]);
			this.correlationIndex.put(key, value);
		}
		reader.close();
	}
	
	public void visualizeCluster(int numOfFeatures, double correlationThreshold, String savePath, int loop, double scale) {
		System.out.println("Visualizing cluster:" + numOfFeatures + "\t features " + correlationThreshold);
		
		//Init a project - and therefore a workspace
		ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
		pc.newProject();
		Workspace workspace = pc.getCurrentWorkspace();

		//Get models and controllers for this new workspace - will be useful later
		AttributeModel attributeModel = Lookup.getDefault().lookup(AttributeController.class).getModel();
		GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
		PreviewModel model = Lookup.getDefault().lookup(PreviewController.class).getModel();
		ImportController importController = Lookup.getDefault().lookup(ImportController.class);
		FilterController filterController = Lookup.getDefault().lookup(FilterController.class);
		RankingController rankingController = Lookup.getDefault().lookup(RankingController.class);

		//Import file      
		/*
		Container container;
		try {
		    File file = new File("/Users/haopeng/Dropbox/code/libraries/gephi-toolkit-0.8.7-all/gephi-toolkit-demos/src/org/gephi/toolkit/demos/resources/polblogs.gml");
		    container = importController.importFile(file);
		    container.getLoader().setEdgeDefault(EdgeDefault.DIRECTED);   //Force DIRECTED
		} catch (Exception ex) {
		    ex.printStackTrace();
		    return;
		}

		//Append imported data to GraphAPI
		importController.process(container, new DefaultProcessor(), workspace);
		*/
		
		//See if graph is well imported
		DirectedGraph graph = graphModel.getDirectedGraph();
		//UndirectedGraph graph = graphModel.getUndirectedGraph();
		
		HashMap<String, Node> nodeIndex = new HashMap<String, Node>();
		
		
		//add nodes
		for (int i = 0; i < numOfFeatures; i ++) {
			String f = this.features.get(i);
			Node node = graphModel.factory().newNode(f);
			node.getNodeData().setLabel(f);
			nodeIndex.put(f, node);
			
			graph.addNode(node);
		}
		
		
		
		//add edges
		//add edges
		for (int i = 0; i < numOfFeatures; i ++) {
			String fi = this.features.get(i);
			Node nodeI = nodeIndex.get(fi);
			for (int j = i + 1; j < numOfFeatures; j ++) {
				String fj = this.features.get(j);
				Node nodeJ = nodeIndex.get(fj);
				
				String key = fi + "-" + fj;
				if (Math.abs(this.correlationIndex.get(key)) >= correlationThreshold) {
					double corr = Math.abs(this.correlationIndex.get(key));
					float f = (float) corr;
					Edge e = graphModel.factory().newEdge(nodeI, nodeJ, f, true);
					e.getEdgeData().setLabel("" + f);
					graph.addEdge(e);
				}
			}
		}
				
		System.out.println("Nodes: " + graph.getNodeCount());
		System.out.println("Edges: " + graph.getEdgeCount());

		//Filter      
		/*
		DegreeRangeFilter degreeFilter = new DegreeRangeFilter();
		degreeFilter.init(graph);
		degreeFilter.setRange(new Range(30, Integer.MAX_VALUE));     //Remove nodes with degree < 30
		Query query = filterController.createQuery(degreeFilter);
		GraphView view = filterController.filter(query);
		graphModel.setVisibleView(view);    //Set the filter result as the visible view
		*/
		
		//See visible graph stats
		UndirectedGraph graphVisible = graphModel.getUndirectedGraphVisible();
		//DirectedGraph graphVisible = graphModel.getDirectedGraphVisible();
		System.out.println("Nodes: " + graphVisible.getNodeCount());
		System.out.println("Edges: " + graphVisible.getEdgeCount());

		//Run YifanHuLayout for 100 passes - The layout always takes the current visible view
		YifanHuLayout layout = new YifanHuLayout(null, new StepDisplacement(1f));
		
		layout.setGraphModel(graphModel);
		layout.resetPropertiesValues();
		layout.setOptimalDistance(200f);
		//layout.setOptimalDistance(opDistance);
		layout.initAlgo();

		for (int i = 0; i < loop && layout.canAlgo(); i++) {
		    layout.goAlgo();
		}
		layout.endAlgo();

		//scale
		
		ScaleLayout layout2 = new ScaleLayout(null, scale);
		
		layout2.setGraphModel(graphModel);
		layout2.resetPropertiesValues();
		//layout.setOptimalDistance(200f);
		//layout.setOptimalDistance(opDistance);
		layout2.initAlgo();
		layout2.goAlgo();
		layout2.endAlgo();
		
		
		//Get Centrality
		GraphDistance distance = new GraphDistance();
		distance.setDirected(true);
		distance.execute(graphModel, attributeModel);

		//Rank color by Degree
		Ranking degreeRanking = rankingController.getModel().getRanking(Ranking.NODE_ELEMENT, Ranking.DEGREE_RANKING);
		AbstractColorTransformer colorTransformer = (AbstractColorTransformer) rankingController.getModel().getTransformer(Ranking.NODE_ELEMENT, Transformer.RENDERABLE_COLOR);
		colorTransformer.setColors(new Color[]{new Color(0xFEF0D9), new Color(0xB30000)});
		rankingController.transform(degreeRanking,colorTransformer);

		//Rank size by centrality
		
		AttributeColumn centralityColumn = attributeModel.getNodeTable().getColumn(GraphDistance.BETWEENNESS);
		Ranking centralityRanking = rankingController.getModel().getRanking(Ranking.NODE_ELEMENT, centralityColumn.getId());
		AbstractSizeTransformer sizeTransformer = (AbstractSizeTransformer) rankingController.getModel().getTransformer(Ranking.NODE_ELEMENT, Transformer.RENDERABLE_SIZE);
		sizeTransformer.setMinSize(3);
		sizeTransformer.setMaxSize(10);
		rankingController.transform(centralityRanking,sizeTransformer);
		
		
		//Preview
		model.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE);
		model.getProperties().putValue(PreviewProperty.SHOW_EDGE_LABELS, Boolean.TRUE);
		model.getProperties().putValue(PreviewProperty.EDGE_COLOR, new EdgeColor(Color.GRAY));
		model.getProperties().putValue(PreviewProperty.EDGE_THICKNESS, new Float(0.1f));
		model.getProperties().putValue(PreviewProperty.NODE_LABEL_FONT, model.getProperties().getFontValue(PreviewProperty.NODE_LABEL_FONT).deriveFont(8));

		//Export
		//Simple PDF export
		ExportController ec = Lookup.getDefault().lookup(ExportController.class);
		try {
		   //ec.exportFile(new File("/Users/haopeng/Desktop/simple5.pdf"));
		   ec.exportFile(new File(savePath));
		} catch (IOException ex) {
		   ex.printStackTrace();
		   return;
		}

		
		
	}
	
	
	public void visualizeClusterInForeAtlasLayout(int numOfFeatures, double correlationThreshold, String savePath, int loop, double scale) {
		System.out.println("Visualizing cluster:" + numOfFeatures + "\t features " + correlationThreshold);
		
		//Init a project - and therefore a workspace
		ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
		pc.newProject();
		Workspace workspace = pc.getCurrentWorkspace();

		//Get models and controllers for this new workspace - will be useful later
		AttributeModel attributeModel = Lookup.getDefault().lookup(AttributeController.class).getModel();
		GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
		PreviewModel model = Lookup.getDefault().lookup(PreviewController.class).getModel();
		ImportController importController = Lookup.getDefault().lookup(ImportController.class);
		FilterController filterController = Lookup.getDefault().lookup(FilterController.class);
		RankingController rankingController = Lookup.getDefault().lookup(RankingController.class);

		//Import file      
		/*
		Container container;
		try {
		    File file = new File("/Users/haopeng/Dropbox/code/libraries/gephi-toolkit-0.8.7-all/gephi-toolkit-demos/src/org/gephi/toolkit/demos/resources/polblogs.gml");
		    container = importController.importFile(file);
		    container.getLoader().setEdgeDefault(EdgeDefault.DIRECTED);   //Force DIRECTED
		} catch (Exception ex) {
		    ex.printStackTrace();
		    return;
		}

		//Append imported data to GraphAPI
		importController.process(container, new DefaultProcessor(), workspace);
		*/
		
		//See if graph is well imported
		DirectedGraph graph = graphModel.getDirectedGraph();
		//UndirectedGraph graph = graphModel.getUndirectedGraph();
		
		HashMap<String, Node> nodeIndex = new HashMap<String, Node>();
		
		
		//add nodes
		for (int i = 0; i < numOfFeatures; i ++) {
			String f = this.features.get(i);
			Node node = graphModel.factory().newNode(f);
			node.getNodeData().setLabel(f);
			nodeIndex.put(f, node);
			
			graph.addNode(node);
		}
		
		
		
		//add edges
		//add edges
		for (int i = 0; i < numOfFeatures; i ++) {
			String fi = this.features.get(i);
			Node nodeI = nodeIndex.get(fi);
			for (int j = i + 1; j < numOfFeatures; j ++) {
				String fj = this.features.get(j);
				Node nodeJ = nodeIndex.get(fj);
				
				String key = fi + "-" + fj;
				if (Math.abs(this.correlationIndex.get(key)) >= correlationThreshold) {
					double corr = Math.abs(this.correlationIndex.get(key));
					float f = (float) corr;
					Edge e = graphModel.factory().newEdge(nodeI, nodeJ, f, true);
					e.getEdgeData().setLabel("" + f);
					graph.addEdge(e);
				}
			}
		}
				
		System.out.println("Nodes: " + graph.getNodeCount());
		System.out.println("Edges: " + graph.getEdgeCount());

		//Filter      
		/*
		DegreeRangeFilter degreeFilter = new DegreeRangeFilter();
		degreeFilter.init(graph);
		degreeFilter.setRange(new Range(30, Integer.MAX_VALUE));     //Remove nodes with degree < 30
		Query query = filterController.createQuery(degreeFilter);
		GraphView view = filterController.filter(query);
		graphModel.setVisibleView(view);    //Set the filter result as the visible view
		*/
		
		//See visible graph stats
		UndirectedGraph graphVisible = graphModel.getUndirectedGraphVisible();
		//DirectedGraph graphVisible = graphModel.getDirectedGraphVisible();
		System.out.println("Nodes: " + graphVisible.getNodeCount());
		System.out.println("Edges: " + graphVisible.getEdgeCount());

		//Run YifanHuLayout for 100 passes - The layout always takes the current visible view
		//YifanHuLayout layout = new YifanHuLayout(null, new StepDisplacement(1f));
		//forceatlas layout
		//ForceAtlasLayout layout = new ForceAtlasLayout(null);
		//ForceAtlas2 layout = new ForceAtlas2(null);
		FruchtermanReingold layout = new FruchtermanReingold(null);
		
		layout.setGraphModel(graphModel);
		layout.resetPropertiesValues();
		//layout.setOptimalDistance(200f);
		//layout.setOptimalDistance(opDistance);
		layout.initAlgo();

		for (int i = 0; i < loop && layout.canAlgo(); i++) {
		    layout.goAlgo();
		}
		layout.endAlgo();
		
		
		ScaleLayout layout2 = new ScaleLayout(null, scale);
		
		layout2.setGraphModel(graphModel);
		layout2.resetPropertiesValues();
		//layout.setOptimalDistance(200f);
		//layout.setOptimalDistance(opDistance);
		layout2.initAlgo();
		layout2.goAlgo();
		layout2.endAlgo();

		
		
		
		//Get Centrality
		GraphDistance distance = new GraphDistance();
		distance.setDirected(true);
		distance.execute(graphModel, attributeModel);

		//Rank color by Degree
		Ranking degreeRanking = rankingController.getModel().getRanking(Ranking.NODE_ELEMENT, Ranking.DEGREE_RANKING);
		AbstractColorTransformer colorTransformer = (AbstractColorTransformer) rankingController.getModel().getTransformer(Ranking.NODE_ELEMENT, Transformer.RENDERABLE_COLOR);
		colorTransformer.setColors(new Color[]{new Color(0xFEF0D9), new Color(0xB30000)});
		rankingController.transform(degreeRanking,colorTransformer);

		//Rank size by centrality
		
		AttributeColumn centralityColumn = attributeModel.getNodeTable().getColumn(GraphDistance.BETWEENNESS);
		Ranking centralityRanking = rankingController.getModel().getRanking(Ranking.NODE_ELEMENT, centralityColumn.getId());
		AbstractSizeTransformer sizeTransformer = (AbstractSizeTransformer) rankingController.getModel().getTransformer(Ranking.NODE_ELEMENT, Transformer.RENDERABLE_SIZE);
		sizeTransformer.setMinSize(3);
		sizeTransformer.setMaxSize(10);
		rankingController.transform(centralityRanking,sizeTransformer);
		
		
		//Preview
		model.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE);
		//model.getProperties().putValue(PreviewProperty.SHOW_EDGE_LABELS, Boolean.TRUE);
		model.getProperties().putValue(PreviewProperty.EDGE_COLOR, new EdgeColor(Color.GRAY));
		model.getProperties().putValue(PreviewProperty.EDGE_THICKNESS, new Float(0.5f));
		model.getProperties().putValue(PreviewProperty.NODE_LABEL_FONT, model.getProperties().getFontValue(PreviewProperty.NODE_LABEL_FONT).deriveFont(8));

		//Export
		//Simple PDF export
		ExportController ec = Lookup.getDefault().lookup(ExportController.class);
		try {
		   //ec.exportFile(new File("/Users/haopeng/Desktop/simple5.pdf"));
		   ec.exportFile(new File(savePath));
		} catch (IOException ex) {
		   ex.printStackTrace();
		   return;
		}

		
		
	}
	
	
	
	public static void main(String args[]) throws IOException {
		//gFilter.writePreSelectedFeaturesToCSV("/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/11. Feature selection/cluster/features.csv");
		//gFilter.writeCorrelationIndexToCSV("/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/11. Feature selection/cluster/correlationindex.csv");

		
		String featureCSV = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/11. Feature selection/cluster/features.csv";
		String correlationCSV = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/11. Feature selection/cluster/correlationindex.csv";
		//String saveToPath = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/11. Feature selection/cluster/visualize/features-threshold/";
		//String saveToPath = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/11. Feature selection/cluster/visualize/layouts/yifanhu/";
		String saveToPath = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/11. Feature selection/cluster/visualize/layouts/reingold-scale/";
		GephiCluster cluster = new GephiCluster(featureCSV, correlationCSV);
		
		/*
		int numOfFeatures = 60;
		double threshold = 0.2;
		
		cluster.visualizeCluster(numOfFeatures, threshold, saveToPath + numOfFeatures + "-" + threshold + ".pdf", 100);
		*/
		/*
		for (int i = 10; i <= 160; i += 10) {
			for (double j = 0.1; j <1; j += 0.1) {
				cluster.visualizeCluster(i, j, saveToPath + i + "-" + j + ".pdf", 100);
			}
		}
		*/
		/*
		for (int i = 0; i < 200; i += 10) {
			//cluster.visualizeCluster(10, 0.75, saveToPath + "10-0.75-" + i + ".pdf", i);
		}
		*/
		
		//test YifanHuLayout optimal distance
		/*
		for (float i = 1.0f; i <= 10000f; i += 100.0f) {
			cluster.visualizeCluster(10, 0.75, saveToPath + "10-0.75-" + i + "-.pdf", 100, i);
		}
		*/
		/*
		//loop on loop times
		for (int i = 10; i <= 200; i += 10) {
			cluster.visualizeClusterInForeAtlasLayout(10, 0.75, saveToPath + "10-0.75-" + i + "-0.1scale.pdf", i);
		}
		*/
		
		//loop on scale
		
		
		saveToPath = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/11. Feature selection/cluster/visualize/layouts/reingold-scale/";
		
		for (double scale = 0.1; scale <= 2.5; scale += 0.1) {
			cluster.visualizeClusterInForeAtlasLayout(29, 0.75, saveToPath + "30-0.75-7000-" + scale +".pdf", 7000, scale);			
		}
		
		/*
		for (int i = 1000; i <= 10000; i += 1000) {
			cluster.visualizeClusterInForeAtlasLayout(30, 0.75, saveToPath + "30-0.75-"+i +"-1.pdf", i, 1);
		}
		*/
		//cluster.visualizeClusterInForeAtlasLayout(30, 0.75, saveToPath + "30-0.75-500-0.2.pdf", 500, 0.2);
		
		
		
		//yifanhu + scale
		/*
		saveToPath = "/Users/haopeng/Dropbox/research/3rd/TechnicalComponents/11. Feature selection/cluster/visualize/layouts/yifanhu-scale/";
		for (double scale = 0.1; scale <= 10.5; scale += 0.1) {
			cluster.visualizeCluster(30, 0.75, saveToPath + "30-0.75-100-" + scale + ".pdf", 8000, scale);
			//cluster.visualizeClusterInForeAtlasLayout(10, 0.75, saveToPath + "10-0.75-" + 100 + "-0.1-" + scale +".pdf", 100, scale);			
		}
		*/
		
	}

}
