package com.github.jannled.oscPlotter;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class PlotPane extends Canvas 
{
	public PlotPane()
	{
		super();
	}
	
	public PlotPane(double width, double height)
	{
		super(width, height);
	}
	
	/**
	 * 
	 * @param values
	 * @param ymin
	 * @param ymax
	 * @param vpp The values to render per pixel
	 */
	public void plot(double values[], double xzoom, double yzoom)
	{
		GraphicsContext gc = getGraphicsContext2D();
		
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, getWidth(), getHeight());
		
		gc.setStroke(Color.AQUAMARINE);
		gc.beginPath();
		
		for(double i=0; i<values.length; i+=xzoom)
		{
			double average = values[(int) i];
			
			if(xzoom < 0)
			{
				//Calculate average
				for(int j=0; j<xzoom; j++)
					average += values[(int) i+j];
				average /= xzoom;
			} 
			
			double onScreenY = average*yzoom + getHeight()/2;
			
			gc.lineTo(i, onScreenY);
		}
		
		gc.stroke();
		gc.closePath();
	}
	
	public void plot(double[] values, double xzoom)
	{
		double size = 0;
		
		for(double d : values)
		{
			d = Math.abs(d);
			if(d > size)
				size = d;
		}
		
		plot(values, size, xzoom);
	}
}
