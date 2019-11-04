package com.github.jannled.oscPlotter;

import java.io.File;

public interface FileInterface 
{	
	public double[] loadCSV(String[] file, int column);
	
	public String[] loadString(String path);
	
	public String[] loadString(File path);
}
