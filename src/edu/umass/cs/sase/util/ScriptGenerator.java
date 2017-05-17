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
package edu.umass.cs.sase.util;

import java.util.ArrayList;

/*
 *
 * selectivity template
 * # query 1
echo "selectivity, query 1, optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/1.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 1 >>selectivity-new-result/postponing-1-$(date +%Y%m%d-%T).result



echo "selectivity, query 1, no optimization"
java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/1.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 >>selectivity-new-result/nopostponing-1-$(date +%Y%m%d-%T).result

 */
public class ScriptGenerator {
	public static void main(String args[]){
		ScriptGenerator sg = new ScriptGenerator();
		//sg.generateScriptsForSelectivity(1, 10);
		//sg.generateScriptsForSelectivityZstream(21, 36);
		//sg.generateScriptsForTimewindowZstream(10, 100);
		//sg.generateScriptsForSelectivityFalse(1, 10);
		//sg.generateScriptsForSelectivityInconsistent(1, 10);
		//sg.generateScriptsForUncertaintyInterval(1, 10);
		
		ArrayList<String> lines = new ArrayList<String>();
		
		
		/*
		lines.add("echo \"selectivity, true-false, precise, baseline, query %%\"");
		lines.add("java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/%%.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 >>selectivity-true-false-precise-baseline/%%-$(date +%Y%m%d-%T).result");
		*/
		/*
		lines.add("echo \"selectivity, true-false, precise, baseline+postponing, query %%\"");
		lines.add("java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/%%.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 1 >>selectivity-true-false-precise-baseline-postponing/%%-$(date +%Y%m%d-%T).result");
		*/
		/*
		lines.add("echo \"selectivity, true-false, precise, baseline+postponing+collapsed format, query %%\"");
		lines.add("java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/%%.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 1 1 1>>selectivity-true-false-precise-baseline-postponing-collapsed/%%-$(date +%Y%m%d-%T).result");
		sg.generateScriptGeneral(lines, 1, 10, 1);
		*/
		
		/*
		lines.add("echo \"time window, true-false, precise, baseline, window = %%\"");
		lines.add("java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/%%.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 >>timewindow-true-false-precise-baseline/%%-$(date +%Y%m%d-%T).result");
		*/
		/*
		lines.add("echo \"time window, true-false, precise, baseline + postponing, window = %%\"");
		lines.add("java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/%%.nfa $HOME/sase2013/queries/timewindow/window.stream 0 1 >>timewindow-true-false-precise-baseline-postponing/%%-$(date +%Y%m%d-%T).result");
		*/

		/*
		lines.add("echo \"time window, true-false, precise, baseline + postponing, window = %%\"");
		lines.add("java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/%%.nfa $HOME/sase2013/queries/timewindow/window.stream 0 1 1 1>>timewindow-true-false-precise-baseline-postponing-collapsed/%%-$(date +%Y%m%d-%T).result");
		sg.generateScriptGeneral(lines, 10, 100, 10);
		*/
		
		
		/*
		lines.add("echo \"selectivity, false, precise, baseline, query %%\"");
		lines.add("java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false-precise/%%.nfa $HOME/sase2013/queries/selectivity-false-precise/selectivity10.stream 0 0 >>selectivity-false-precise-baseline/%%-$(date +%Y%m%d-%T).result");
		*/
		
		
		/*
		lines.add("echo \"selectivity, true-false, precise, baseline+postponing, query %%\"");
		lines.add("java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false-precise/%%.nfa $HOME/sase2013/queries/selectivity-false-precise/selectivity10.stream 0 1 >>selectivity-false-precise-baseline-postponing/%%-$(date +%Y%m%d-%T).result");
		*/
		
		
		/*
		lines.add("echo \"selectivity, true-false, precise, baseline+postponing+on-the-fly, query %%\"");
		lines.add("java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false-precise/%%.nfa $HOME/sase2013/queries/selectivity-false-precise/selectivity10.stream 0 1 1 0 >>selectivity-false-precise-baseline-postponing-onthefly/%%-$(date +%Y%m%d-%T).result");
		*/
		
		
		/*
		lines.add("echo \"selectivity, true-false, precise, baseline+postponing+on-the-fly+collapsed, query %%\"");
		lines.add("java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false-precise/%%.nfa $HOME/sase2013/queries/selectivity-false-precise/selectivity10.stream 0 1 1 1 >>selectivity-false-precise-baseline-postponing-onthefly-collapsed/%%-$(date +%Y%m%d-%T).result");
		
		sg.generateScriptGeneral(lines, 1, 8, 1);
		*/
		



		/*
		lines.add("echo \"time window, false, precise, baseline, window = %%\"");
		lines.add("java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-false-precise/%%.nfa $HOME/sase2013/queries/timewindow-false-precise/window.stream 0 0 >>timewindow-false-precise-baseline/%%-$(date +%Y%m%d-%T).result");
		*/
		
		
		/*
		lines.add("echo \"time window, false, precise, baseline + postponing, window = %%\"");
		lines.add("java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-false-precise/%%.nfa $HOME/sase2013/queries/timewindow-false-precise/window.stream 0 1 >>timewindow-false-precise-baseline-postponing/%%-$(date +%Y%m%d-%T).result");
		*/
		
		
		/*
		lines.add("echo \"time window, false, precise, baseline + postponing + onthefly, window = %%\"");
		lines.add("java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-false-precise/%%.nfa $HOME/sase2013/queries/timewindow-false-precise/window.stream 0 1 1 0 >>timewindow-false-precise-baseline-postponing-onthefly/%%-$(date +%Y%m%d-%T).result");
		*/
		
		
		/*
		lines.add("echo \"time window, false, precise, baseline + postponing + onthefly-collapsed, window = %%\"");
		lines.add("java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-false-precise/%%.nfa $HOME/sase2013/queries/timewindow-false-precise/window.stream 0 1 1 1 >>timewindow-false-precise-baseline-postponing-onthefly-collapsed/%%-$(date +%Y%m%d-%T).result");
		
		sg.generateScriptGeneral(lines, 10, 100, 10);
		*/
		
		
		
		/*
		lines.add("echo \"selectivity, true-false, precise, baseline + zstream, query %%\"");
		lines.add("java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/%%.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 0 0 10 1 >>selectivity-true-false-precise-baseline-zstream/%%-$(date +%Y%m%d-%T).result");
		*/
		
		
		/*
		lines.add("echo \"time window, true-false, precise, baseline + zstream, window = %%\"");
		lines.add("java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/%%.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 0 0 10 1 >>timewindow-true-false-precise-baseline-zstream/%%-$(date +%Y%m%d-%T).result");
		*/
		
		
		/*
		lines.add("echo \"selectivity, false, precise, baseline-zstream, query %%\"");
		lines.add("java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false-precise/%%.nfa $HOME/sase2013/queries/selectivity-false-precise/selectivity10.stream 0 0 0 0 10 1 >>selectivity-false-precise-baseline-zstream/%%-$(date +%Y%m%d-%T).result");
		sg.generateScriptGeneral(lines, 1, 8, 1);
		*/
		
		/*

		lines.add("echo \"time window, false, precise, baseline+zstream, window = %%\"");
		lines.add("java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-false-precise/%%.nfa $HOME/sase2013/queries/timewindow-false-precise/window.stream 0 0 0 0 10 1 >>timewindow-false-precise-baseline-zstream/%%-$(date +%Y%m%d-%T).result");
		*/
		
		/*
		lines.add("echo \"stream size, true-false, precise, baseline+postponing+collapsed format, stream size = %%0000\"");
		lines.add("java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/1.nfa $HOME/sase2013/queries/selectivity/selectivity%%.stream 0 1 1 1 >>streamsize-true-false-precise-baseline-postponing-collapsed/%%-$(date +%Y%m%d-%T).result");
		*/
		
		/*
		lines.add("echo \"time window, true-false, precise, baseline + postponing, window = %%\"");
		lines.add("java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/%%.nfa $HOME/sase2013/queries/timewindow/window.stream 0 1 1 1 >>largewindow-true-false-precise-baseline-postponing-collapsed/%%-$(date +%Y%m%d-%T).result");
		*/
		/*
		lines.add("echo \"selectivity, true-false, imprecise, baseline-postponing, query %%\"");
		lines.add("java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/selectivity-imprecise/%%.nfa $HOME/sase2013/queries/selectivity-imprecise/selectivity-imprecise.stream 0 1 1 1 10 0 0 0.5 5 >>selectivity-true-false-imprecise-baseline-postponing-collapsed/%%-$(date +%Y%m%d-%T).result");
		*/
		
		/*

		lines.add("echo \"time window, true-false, imprecise, baseline, window = %%\"");
		lines.add("java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-imprecise/%%.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 0 0 0 10 0 0 0.5 5 >>timewindow-true-false-imprecise-baseline/%%-$(date +%Y%m%d-%T).result");
		*/
		
		
		
		/*
		lines.add("echo \"uncertaintyinterval, true-false, imprecise, baseline-postponing-collapsed, half uncertainty interval = %%\"");
		lines.add("java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/uncertaintyinterval-true-false/1.nfa $HOME/sase2013/queries/uncertaintyinterval-true-false/imprecise.stream 0 1 1 1 10 0 0 0.5 %% >>uncertaintyinterval-true-false-imprecise-baseline-postponing-collapsed/%%-$(date +%Y%m%d-%T).result");
		*/
		
		
		

		/*
		lines.add("echo \"time window, true-false, imprecise, baseline, window = %%\"");
		lines.add("java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/%%.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 0 0 0 10 0 0 0.5 5 0 0 >>timewindow-true-false-imprecise-baseline/%%-$(date +%Y%m%d-%T).result");
		*/
		
		
		
		/*
		lines.add("echo \"time window, true-false, imprecise, baseline-postponing-dpc, window = %%\"");
		lines.add("java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/%%.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc/%%-$(date +%Y%m%d-%T).result");
		*/
		
		
		
		
		lines.add("echo \"time window, true-false, imprecise, baseline-postponing-collapsed, window = %%\"");
		lines.add("java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/%%.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 1 10 0 0 0.5 5 0 0 >>timewindow-true-false-imprecise-baseline-postponing-collapsed/%%-$(date +%Y%m%d-%T).result");
		
		
		
		
		/*
		lines.add("echo \"confidence, true-false, imprecise, baseline-postponing-dpc, window = 30, confidence = 0.%%\"");
		lines.add("java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/30.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.%% 5 1 0 >>timewindow-true-false-imprecise-baseline-postponing-dpc/0.%%-$(date +%Y%m%d-%T).result");
		*/
		
		
		/*
		lines.add("echo \"time window, true-false, precise, baseline + postponing, compare with imprecise, window = %%\"");
		lines.add("java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow-imprecise/%%.nfa $HOME/sase2013/queries/timewindow-imprecise/window.stream 0 1 >>timewindow-true-false-precise-baseline-postponing-compare-with-imprecise/%%-$(date +%Y%m%d-%T).result");
		*/
		
		/*
		echo "time window, true-false, imprecise, baseline, window = 40"
		java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/40.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 0 0 0 10 0 0 0.5 5 0 0 >>timewindow-true-false-imprecise-baseline/40-$(date +%Y%m%d-%T).result	
		*/
		
		
		
		/*
		lines.add("echo \"time window, true-false, imprecise, baseline-postponing, window = %%\"");
		lines.add("java -Xms100m -Xmx8000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/timewindow-imprecise/%%.nfa $HOME/sase2013/queries/timewindow-imprecise/window-imprecise.stream 0 1 1 0 10 0 0 0.5 5 >>timewindow-true-false-imprecise-baseline-postponing/%%-$(date +%Y%m%d-%T).result");
		*/
		
		sg.generateScriptGeneral(lines, 45, 100, 5);

		sg.generateScriptGeneral(lines, 110, 200, 10);
		
	}
	
	public void generateScriptGeneral(ArrayList<String> lines, int from, int to, int step){
		String tempLine;
		for(int i = from; i <= to; i += step){
			for(int j = 0; j < lines.size(); j ++){
				tempLine = lines.get(j).replaceAll("%%", "" + i);
				System.out.println(tempLine);
			}
		}
	}
	
	public void generateScriptsForUncertaintyInterval(int from, int to){
		ArrayList<String> lines = new ArrayList<String>();
		lines.add("# half uncertainty %%");
		lines.add("echo \"half uncertainty %%, optimization\"");
		lines.add("java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/uncertain-interval/uncertain.nfa $HOME/sase2012/queries/uncertain-interval/uncertain%%.stream 0 1 >>uncertain-interval-result/postponing-%%-$(date +%Y%m%d-%T).result");
		lines.add("");
		lines.add("echo \"half uncertainty %%, no optimization\"");
		lines.add("java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.UncertainEngineUI $HOME/sase2013/queries/uncertain-interval/uncertain.nfa $HOME/sase2012/queries/uncertain-interval/uncertain%%.stream 0 0 >>uncertain-interval-result/nopostponing-%%-$(date +%Y%m%d-%T).result");
		
		String tempLine;
		for(int i = from; i <= to; i++){
			for(int j = 0; j < lines.size(); j ++){
				tempLine = lines.get(j).replaceAll("%%", "" + i);
				System.out.println(tempLine);
			}
		}
		
		
	}
	public void generateScriptsForSelectivity(int from, int to){
		ArrayList<String> lines = new ArrayList<String>();
		lines.add("# query %%");
		lines.add("echo \"selectivity, query %%, optimization\"");
		lines.add("java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2012/queries/selectivity-new/%%.nfa $HOME/sase2012/queries/selectivity-new/selectivity10.stream 0 1 >>selectivity-new10-result/postponing-%%-$(date +%Y%m%d-%T).result");
		lines.add("");
		lines.add("echo \"selectivity, query %%, no optimization\"");
		lines.add("java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2012/queries/selectivity-new/%%.nfa $HOME/sase2012/queries/selectivity-new/selectivity10.stream 0 0 >>selectivity-new10-result/nopostponing-%%-$(date +%Y%m%d-%T).result");
		
		String tempLine;
		for(int i = from; i <= to; i++){
			for(int j = 0; j < lines.size(); j ++){
				tempLine = lines.get(j).replaceAll("%%", "" + i);
				System.out.println(tempLine);
			}
		}
		
		
	}
	
	public void generateScriptsForSelectivityZstream(int from, int to){
		ArrayList<String> lines = new ArrayList<String>();
		lines.add("# query %%");
		lines.add("echo \"zstream, selectivity, query %%, \"");
		lines.add("java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity/%%.nfa $HOME/sase2013/queries/selectivity/selectivity.stream 0 0 0 0 1 >>zstream/selectivity-%%-$(date +%Y%m%d-%T).result");
		
		String tempLine;
		for(int i = from; i <= to; i++){
			for(int j = 0; j < lines.size(); j ++){
				tempLine = lines.get(j).replaceAll("%%", "" + i);
				System.out.println(tempLine);
			}
		}
		
		
	}
	
	

	//# time window: 10
	//echo "time window, 10, optimization"
	//java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/10.nfa $HOME/sase2013/queries/timewindow/window.stream 0 1 >>window-result/postponing-10-$(date +%Y%m%d-%T).result

	public void generateScriptsForTimewindowZstream(int from, int to){
		ArrayList<String> lines = new ArrayList<String>();
		lines.add("# time window %%");
		lines.add("echo \"zstream, timewindow,  %%, \"");
		lines.add("java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/timewindow/%%.nfa $HOME/sase2013/queries/timewindow/window.stream 0 0 0 0 1 >>zstream/timewindow-%%-$(date +%Y%m%d-%T).result");
		
		String tempLine;
		for(int i = from; i <= to; i+=10){
			for(int j = 0; j < lines.size(); j ++){
				tempLine = lines.get(j).replaceAll("%%", "" + i);
				System.out.println(tempLine);
			}
		}
		
		
	}

	public void generateScriptsForSelectivityFalse(int from, int to){
		ArrayList<String> lines = new ArrayList<String>();
		lines.add("# query %%");
		lines.add("echo \"selectivity-false, query %%, optimization, no on the fly, no collapsed format\"");
		lines.add("java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false/%%.nfa $HOME/sase2012/queries/selectivity-false/selectivity10.stream 0 1 0 0>>selectivity-false/postponing-%%-$(date +%Y%m%d-%T).result");
		lines.add("");
		lines.add("echo \"selectivity-false, query %%, no optimization, no on the fly, no collapsed format\"");
		lines.add("java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false/%%.nfa $HOME/sase2012/queries/selectivity-false/selectivity10.stream 0 0 0 0>>selectivity-false/nopostponing-%%-$(date +%Y%m%d-%T).result");
		lines.add("");
		lines.add("echo \"selectivity-false, query %%, optimization,  on the fly, no collapsed format\"");
		lines.add("java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false/%%.nfa $HOME/sase2012/queries/selectivity-false/selectivity10.stream 0 1 1 0>>selectivity-false/onthefly-%%-$(date +%Y%m%d-%T).result");
		lines.add("");
		lines.add("echo \"selectivity-false, query %%, optimization,  on the fly, collapsed format\"");
		lines.add("java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-false/%%.nfa $HOME/sase2012/queries/selectivity-false/selectivity10.stream 0 1 1 1>>selectivity-false/collapsed-%%-$(date +%Y%m%d-%T).result");
		
		String tempLine;
		for(int i = from; i <= to; i++){
			for(int j = 0; j < lines.size(); j ++){
				tempLine = lines.get(j).replaceAll("%%", "" + i);
				System.out.println(tempLine);
			}
		}
		
		
	}
	public void generateScriptsForSelectivityInconsistent(int from, int to){
		ArrayList<String> lines = new ArrayList<String>();
		lines.add("# query %%");
		lines.add("echo \"selectivity-false, query %%, optimization, no on the fly, no collapsed format\"");
		lines.add("java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-inconsistent/%%.nfa $HOME/sase2012/queries/selectivity-inconsistent/selectivity10.stream 0 1 0 0>>selectivity-inconsistent/postponing-%%-$(date +%Y%m%d-%T).result");
		lines.add("");
		lines.add("echo \"selectivity-false, query %%, no optimization, no on the fly, no collapsed format\"");
		lines.add("java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-inconsistent/%%.nfa $HOME/sase2012/queries/selectivity-inconsistent/selectivity10.stream 0 0 0 0>>selectivity-inconsistent/nopostponing-%%-$(date +%Y%m%d-%T).result");
		lines.add("");
		lines.add("echo \"selectivity-false, query %%, optimization,  on the fly, no collapsed format\"");
		lines.add("java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-inconsistent/%%.nfa $HOME/sase2012/queries/selectivity-inconsistent/selectivity10.stream 0 1 1 0>>selectivity-inconsistent/onthefly-%%-$(date +%Y%m%d-%T).result");
		lines.add("");
		lines.add("echo \"selectivity-false, query %%, optimization,  on the fly, collapsed format\"");
		lines.add("java -Xms100m -Xmx3000m edu.umass.cs.sase.UI.CommandLineUI $HOME/sase2013/queries/selectivity-inconsistent/%%.nfa $HOME/sase2012/queries/selectivity-inconsistent/selectivity10.stream 0 1 1 1>>selectivity-inconsistent/collapsed-%%-$(date +%Y%m%d-%T).result");
		
		String tempLine;
		for(int i = from; i <= to; i++){
			for(int j = 0; j < lines.size(); j ++){
				tempLine = lines.get(j).replaceAll("%%", "" + i);
				System.out.println(tempLine);
			}
		}
		
		
	}
	
}
