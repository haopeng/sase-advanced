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
package edu.umass.cs.sase.engine;

import java.util.ArrayList;

/**
 * This class is used to manage all instances of Path
 * This class uses singleTon design pattern
 * Reference:http://www.javaworld.com/javaworld/jw-04-2003/jw-0425-designpatterns.html
 * 
 * @author haopeng
 *
 */
public class PathPool {
	private static PathPool instance = null;
	
	public static PathPool getInstance(){
		if(instance == null){
			instance = new PathPool();
		}
		return instance;
	}
	
	ArrayList<Path> idlePaths;
	int count = 0;
	
	protected PathPool(){
		this.idlePaths = new ArrayList<Path>();
	}
	
	public Path getPath(){
		Path p = null;
		if(idlePaths.size() == 0){
			p = new Path();
			count ++;
			//System.out.println("get a new path");
		}else{
			p = idlePaths.get(0);
			this.idlePaths.remove(0);
			//System.out.println("reuse a path");
		}
		
		
		return p;
	}
	
	public void returnPath(ArrayList<Path> paths){
		System.out.println("return paths:" + paths.size() + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		this.idlePaths.addAll(paths);
	}
	
	public void reset(){
		this.count = 0;
		//this.idlePaths = new ArrayList<Path>();
	}

	public ArrayList<Path> getIdlePaths() {
		return idlePaths;
	}

	public void setIdlePaths(ArrayList<Path> idlePaths) {
		this.idlePaths = idlePaths;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public static void setInstance(PathPool instance) {
		PathPool.instance = instance;
	}
	
}
