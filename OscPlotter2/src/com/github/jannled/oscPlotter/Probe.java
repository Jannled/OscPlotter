package com.github.jannled.oscPlotter;

import java.io.File;

import javafx.scene.paint.Color;

public class Probe 
{
	private double[] values;
	private double xzoom, yzoom;
	
	private static int probeIDC = 1;
	private int probeID;
	private Color[] colors = {Color.YELLOW, Color.CYAN, Color.PINK, Color.GREEN, Color.FIREBRICK, Color.BLUEVIOLET};
	private Color color;
	
	public Probe(File path, int column)
	{
		this(CSVLoader.loadCSV(CSVLoader.loadString(path), column));
	}
	
	public Probe(File path, int column, double xzoom, double yzoom)
	{
		this(CSVLoader.loadCSV(CSVLoader.loadString(path), column), xzoom, yzoom);
	}
	
	public Probe(double[] values)
	{
		this(values, 1, 1);
	}
	
	public Probe(double[] values, double xzoom, double yzoom)
	{
		this.values = values;
		this.xzoom = xzoom;
		this.yzoom = yzoom;
		
		probeID = probeIDC++;
		color = colors[(probeID-1)%colors.length];
		
		double ymin = 0;
		double ymax = 0;
		for(int i=0; i<values.length; i++)
		{
			if(values[i] < ymin)
				ymin = values[i];
			if(values[i] > ymax)
				ymax = values[i];
		}
		System.out.println("Min: " + ymin + ", Max: " + ymax);
	}
	
	public double[] getValues()
	{
		return values;
	}
	
	public int getID()
	{
		return probeID;
	}
	
	public double getValue(int index)
	{
		return values[index];
	}
	
	public double getXZoom()
	{
		return xzoom;
	}
	
	public double getYZoom()
	{
		return yzoom;
	}
	
	public Color getColor()
	{
		return color;
	}
	
	public void setColor(Color color)
	{
		this.color = color;
	}
	
	public void setZoom(double xzoom, double yzoom)
	{
		this.xzoom = xzoom;
		this.yzoom = yzoom;
	}
}
