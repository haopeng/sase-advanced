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
package edu.umass.cs.sase.datacollect;

import java.util.ArrayList;
/**
 * 
 * @author haopeng
 *Used to generate the scripts to collect data on different nodes
 */
public class DataCollectScriptGenerator {

	public static void main(String[] args) {

		DataCollectScriptGenerator g = new DataCollectScriptGenerator();
		//g.obelix();
		
		/*
		System.out.println("#Compute-0-x: 29 machines");
		g.yeeha0();
		
		System.out.println("#Compute-1-x: 11 machines");
		g.yeeha1();
		*/
		
		System.out.println("#Compute-0-x: 7 matchines");
		g.dbcluster0();
		
		System.out.println("#Compute-1-x: 4 matchines");
		g.dbcluster1();
	}
	
	public void dbcluster1() {
		//Prepare template
		ArrayList<String> lines = new ArrayList<String>();
		String line1 = "#Machine = compute-1-%machine_num.dbcluster, Metrics = %metric_name, Aggregation = %agg_name";
		String line2 = "cd /srv/data1/users/haopeng/DBCluster/compute-1-%machine_num.dbcluster/";
		String line3 = "/opt/rocks/bin/rrdtool fetch %metric_name.rrd %agg_name -r 15 -s -1h >> /srv/data1/users/haopeng/GangliaDBCluster/compute-1-%machine_num.dbcluster/%metric_name_%agg_name.log";
		lines.add(line1);
		lines.add(line2);
		lines.add(line3);
		//Prepare variables
		int[] machines = new int[4];
		for (int i = 0; i < machines.length; i ++) {
			machines[i] = i;
		}
		
		String[] metrics = {"boottime", "bytes_in", "bytes_out", "cpu_aidle", "cpu_idle", "cpu_nice", "cpu_num", "cpu_speed", 
				"cpu_system", "cpu_user", "cpu_wio", "disk_free", "disk_total", "load_fifteen", "load_five", "load_one", 
				"mem_buffers", "mem_cached", "mem_free", "mem_shared", "mem_total", "part_max_used", "pkts_in", "pkts_out", 
				"proc_run", "proc_total", "swap_free", "swap_total"};
		String[] aggregations = {"AVERAGE"};
		
		String tempLine = null;
		
		//iterate
		for(int i = 0; i < machines.length; i ++){
			System.out.println();
			System.out.println("#============Machine:" + machines[i] + "=================");
			System.out.println();
			for(int j = 0; j < metrics.length; j ++){
				System.out.println();
				System.out.println("#~~~~~~Metrics:" + metrics[j] + "~~~~~~~~");
				System.out.println();
				for(int k = 0; k < aggregations.length; k ++){
					for(int l = 0; l < lines.size(); l ++){
						tempLine = lines.get(l);
						tempLine = tempLine.replaceAll("%machine_num", "" + machines[i]);
						tempLine = tempLine.replaceAll("%metric_name", metrics[j]);
						tempLine = tempLine.replaceAll("%agg_name", aggregations[k]);
						System.out.println(tempLine);
					}
				}
			}
		}
		
	}

	public void dbcluster0() {
		//Prepare template
		ArrayList<String> lines = new ArrayList<String>();
		String line1 = "#Machine = compute-0-%machine_num.dbcluster, Metrics = %metric_name, Aggregation = %agg_name";
		String line2 = "cd /srv/data1/users/haopeng/DBCluster/compute-0-%machine_num.dbcluster/";
		String line3 = "/opt/rocks/bin/rrdtool fetch %metric_name.rrd %agg_name -r 15 -s -1h >> /srv/data1/users/haopeng/GangliaDBCluster/compute-0-%machine_num.dbcluster/%metric_name_%agg_name.log";
		lines.add(line1);
		lines.add(line2);
		lines.add(line3);
		//Prepare variables
		int[] machines = new int[7];
		for (int i = 0; i < machines.length; i ++) {
			machines[i] = i;
		}
		
		String[] metrics = {"boottime", "bytes_in", "bytes_out", "cpu_aidle", "cpu_idle", "cpu_nice", "cpu_num", "cpu_speed", 
				"cpu_system", "cpu_user", "cpu_wio", "disk_free", "disk_total", "load_fifteen", "load_five", "load_one", 
				"mem_buffers", "mem_cached", "mem_free", "mem_shared", "mem_total", "part_max_used", "pkts_in", "pkts_out", 
				"proc_run", "proc_total", "swap_free", "swap_total"};
		String[] aggregations = {"AVERAGE"};
		
		String tempLine = null;
		
		//iterate
		for(int i = 0; i < machines.length; i ++){
			System.out.println();
			System.out.println("#============Machine:" + machines[i] + "=================");
			System.out.println();
			for(int j = 0; j < metrics.length; j ++){
				System.out.println();
				System.out.println("#~~~~~~Metrics:" + metrics[j] + "~~~~~~~~");
				System.out.println();
				for(int k = 0; k < aggregations.length; k ++){
					for(int l = 0; l < lines.size(); l ++){
						tempLine = lines.get(l);
						tempLine = tempLine.replaceAll("%machine_num", "" + machines[i]);
						tempLine = tempLine.replaceAll("%metric_name", metrics[j]);
						tempLine = tempLine.replaceAll("%agg_name", aggregations[k]);
						System.out.println(tempLine);
					}
				}
			}
		}
		
	}
	
	
	public void yeeha1() {
		//Prepare template
		ArrayList<String> lines = new ArrayList<String>();
		String line1 = "#Machine = compute-1-%machine_num.yeeha, Metrics = %metric_name, Aggregation = %agg_name";
		String line2 = "cd /var/lib/ganglia/rrds/yeeha/compute-1-%machine_num.yeeha/";
		String line3 = "/opt/rocks/bin/rrdtool fetch %metric_name.rrd %agg_name -r 15 -s -1h >> /srv/data1/users/haopeng/GangliaYeeha/compute-1-%machine_num.yeeha/%metric_name_%agg_name.log";
		lines.add(line1);
		lines.add(line2);
		lines.add(line3);
		//Prepare variables
		int[] machines = new int[11];
		for (int i = 0; i < machines.length; i ++) {
			machines[i] = i;
		}
		
		String[] metrics = {"boottime", "bytes_in", "bytes_out", "cpu_aidle", "cpu_idle", "cpu_nice", "cpu_num", "cpu_speed", 
				"cpu_system", "cpu_user", "cpu_wio", "disk_free", "disk_total", "load_fifteen", "load_five", "load_one", 
				"mem_buffers", "mem_cached", "mem_free", "mem_shared", "mem_total", "part_max_used", "pkts_in", "pkts_out", 
				"proc_run", "proc_total", "swap_free", "swap_total"};
		String[] aggregations = {"AVERAGE"};
		
		String tempLine = null;
		
		//iterate
		for(int i = 0; i < machines.length; i ++){
			System.out.println();
			System.out.println("#============Machine:" + machines[i] + "=================");
			System.out.println();
			for(int j = 0; j < metrics.length; j ++){
				System.out.println();
				System.out.println("#~~~~~~Metrics:" + metrics[j] + "~~~~~~~~");
				System.out.println();
				for(int k = 0; k < aggregations.length; k ++){
					for(int l = 0; l < lines.size(); l ++){
						tempLine = lines.get(l);
						tempLine = tempLine.replaceAll("%machine_num", "" + machines[i]);
						tempLine = tempLine.replaceAll("%metric_name", metrics[j]);
						tempLine = tempLine.replaceAll("%agg_name", aggregations[k]);
						System.out.println(tempLine);
					}
				}
			}
		}
		
	}
	
	public void yeeha0() {
		//Prepare template
		ArrayList<String> lines = new ArrayList<String>();
		String line1 = "#Machine = compute-0-%machine_num.yeeha, Metrics = %metric_name, Aggregation = %agg_name";
		String line2 = "cd /var/lib/ganglia/rrds/yeeha/compute-0-%machine_num.yeeha/";
		String line3 = "/opt/rocks/bin/rrdtool fetch %metric_name.rrd %agg_name -r 15 -s -1h >> /srv/data1/users/haopeng/GangliaYeeha/compute-0-%machine_num.yeeha/%metric_name_%agg_name.log";
		lines.add(line1);
		lines.add(line2);
		lines.add(line3);
		//Prepare variables
		int[] machines = new int[29];
		for (int i = 0; i < machines.length; i ++) {
			machines[i] = i;
		}
		
		String[] metrics = {"boottime", "bytes_in", "bytes_out", "cpu_aidle", "cpu_idle", "cpu_nice", "cpu_num", "cpu_speed", 
				"cpu_system", "cpu_user", "cpu_wio", "disk_free", "disk_total", "load_fifteen", "load_five", "load_one", 
				"mem_buffers", "mem_cached", "mem_free", "mem_shared", "mem_total", "part_max_used", "pkts_in", "pkts_out", 
				"proc_run", "proc_total", "swap_free", "swap_total"};
		String[] aggregations = {"AVERAGE"};
		
		String tempLine = null;
		
		//iterate
		for(int i = 0; i < machines.length; i ++){
			System.out.println();
			System.out.println("#============Machine:" + machines[i] + "=================");
			System.out.println();
			for(int j = 0; j < metrics.length; j ++){
				System.out.println();
				System.out.println("#~~~~~~Metrics:" + metrics[j] + "~~~~~~~~");
				System.out.println();
				for(int k = 0; k < aggregations.length; k ++){
					for(int l = 0; l < lines.size(); l ++){
						tempLine = lines.get(l);
						tempLine = tempLine.replaceAll("%machine_num", "" + machines[i]);
						tempLine = tempLine.replaceAll("%metric_name", metrics[j]);
						tempLine = tempLine.replaceAll("%agg_name", aggregations[k]);
						System.out.println(tempLine);
					}
				}
			}
		}
		
	}


	/**
	 * cd /var/lib/ganglia/rrds/unspecified/obelix105.local/ 
rrdtool fetch cpu_system.rrd AVERAGE -r 15 -s -1h >> //nfs/obelix8/scratch2/haopeng/testdata/obelix105.local.cpu_system
11 machines
metrics:boottime, bytes_in, bytes_out, cpu_aidle, cpu_idle, cpu_nice, cpu_num, cpu_speed, cpu_system, cpu_user, cpu_wio, disk_free, disk_total, load_fifteen, load_five, load_one, mem_buffers, mem_cached, mem_free, mem_shared, mem_total, part_max_used, pkts_in, pkts_out, proc_run, proc_total, swap_free, swap_total
Agg: average, max, min
	 */
	public void obelix() {
		//Prepare template
		ArrayList<String> lines = new ArrayList<String>();
		String line1 = "#Machine = obelix%machine_num, Metrics = %metric_name, Aggregation = %agg_name";
		String line2 = "cd /var/lib/ganglia/rrds/unspecified/obelix%machine_num.local/";
		String line3 = "rrdtool fetch %metric_name.rrd %agg_name -r 15 -s -1h >> /nfs/obelix8/scratch2/haopeng/hadoopdata/obelix%machine_num/%metric_name_%agg_name.log";
		lines.add(line1);
		lines.add(line2);
		lines.add(line3);
		//Prepare variables
		int[] machines = {105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115};
		String[] metrics = {"boottime", "bytes_in", "bytes_out", "cpu_aidle", "cpu_idle", "cpu_nice", "cpu_num", "cpu_speed", 
				"cpu_system", "cpu_user", "cpu_wio", "disk_free", "disk_total", "load_fifteen", "load_five", "load_one", 
				"mem_buffers", "mem_cached", "mem_free", "mem_shared", "mem_total", "part_max_used", "pkts_in", "pkts_out", 
				"proc_run", "proc_total", "swap_free", "swap_total"};
		String[] aggregations = {"AVERAGE"};
		
		String tempLine = null;
		
		//iterate
		for(int i = 0; i < machines.length; i ++){
			System.out.println();
			System.out.println("#============Machine:" + machines[i] + "=================");
			System.out.println();
			for(int j = 0; j < metrics.length; j ++){
				System.out.println();
				System.out.println("#~~~~~~Metrics:" + metrics[j] + "~~~~~~~~");
				System.out.println();
				for(int k = 0; k < aggregations.length; k ++){
					for(int l = 0; l < lines.size(); l ++){
						tempLine = lines.get(l);
						tempLine = tempLine.replaceAll("%machine_num", "" + machines[i]);
						tempLine = tempLine.replaceAll("%metric_name", metrics[j]);
						tempLine = tempLine.replaceAll("%agg_name", aggregations[k]);
						System.out.println(tempLine);
					}
				}
			}
		}
		
	}

}
