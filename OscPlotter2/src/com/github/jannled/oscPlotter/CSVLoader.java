package com.github.jannled.oscPlotter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class CSVLoader
{
	public static String[] loadString(File file) 
	{
		try(BufferedReader br = new BufferedReader(new FileReader(file))) 
		{
			List<String> lines = new LinkedList<String>();
			String line = null;
			while((line = br.readLine()) != null)
			{
				lines.add(line);
			}
			br.close();
			return lines.toArray(new String[lines.size()]);
		} 
		catch(IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String[] loadString(String path) 
	{
		return loadString(new File(path));
	}
	
	public static double[] loadCSV(String[] file, int column) 
	{
		int valcount = 0;
		
		double[] valueBuffer = new double[file.length];
		
		for(String line : file)
		{
			String[] vals = line.split(",");
			double d = 0; 
			try {
				d = Double.parseDouble(vals[column]);
				valueBuffer[valcount++] = d;
			} catch (NumberFormatException e) {
				
			}
		}
		
		double[] values = new double[valcount];
		System.arraycopy(valueBuffer, 0, values, 0, valcount);
		return values;
	}
	
}
