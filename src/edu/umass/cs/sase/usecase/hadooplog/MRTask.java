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
package edu.umass.cs.sase.usecase.hadooplog;


// borrowed from Ed, didn't use it.
public class MRTask implements Comparable<MRTask> {

	public enum Type {MAP, REDUCE};
	public enum Status {SUCCESS, KILLED};
	
	private int id;
	private Type type;
	private Status status;
	private double start, finish;
	
	public MRTask(Type type, int id) {
		this.type = type;
		this.id = id;
	}

	public int compareTo(MRTask t) {
		if(start < t.getStart()) {
			return -1;
		}
		else if(start > t.getStart()) {
			return 1;
		}
		else {
			return 0;
		}
	}
	
	public boolean equals(Object o) {
		MRTask t = (MRTask)o;
		return getType().equals(t.getType()) && id == t.getId();
	}
	
	public String getType() {
		switch(type) {
		case MAP:
			return "map";
		case REDUCE:
			return "reduce";
		}
		System.out.println("Something went wrong with getType()");
		return null; // Should never reach here
	}
	
	public int getId() {
		return id;
	}
	
	public String getStatus() {
		switch(status) {
		case SUCCESS:
			return "success";
		case KILLED:
			return "killed";
		}
		System.out.println("Something went wrong with getStatus()");
		return null; // Should never reach here
	}
	
	public double getStart() {
		return start;
	}
	
	public double getFinish() {
		return finish;
	}
	
	public void setStatus(MRTask.Status status) {
		this.status = status;
	}
	
	public void setStart(double start) {
		this.start = start;
	}
	
	public void setFinish(double finish) {
		this.finish = finish;
	}
	
}