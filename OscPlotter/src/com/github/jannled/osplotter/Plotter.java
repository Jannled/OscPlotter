package com.github.jannled.osplotter;

import com.github.jannled.lib.Print;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.collections.*;
import javafx.beans.value.*;

public class Plotter extends Canvas
{
	public double[][] channels;
	public int channelCount;
	
	int start = 0;
	int length = 100;
	
	private final static int WIDTH = 1280;
	private final static int HEIGHT = 720;
	
	public Plotter(int channelCount)
	{
		super(WIDTH, HEIGHT);
		this.channelCount = channelCount;
		plot(0, 100);
		
		widthProperty().addListener(evt -> plot(start, length));
		heightProperty().addListener(evt -> plot(start, length));
	}
	
	public void parse(String file)
	{
		int errorFlag = 0;
	
		String[] samples = file.split("\n");
		channels = new double[channelCount][samples.length-1];
				
		for(int i=1; i<samples.length; i++)
		{
			String[] sample = samples[i].trim().split(",");
			if(sample.length < 1 || sample.length > channelCount)
				errorFlag++;
			
			for(int j=0; j<sample.length; j++)
			{
				try
				{
					channels[j][i-1] = Double.parseDouble(sample[j]);
				}
				catch(NumberFormatException | ArrayIndexOutOfBoundsException e)
				{
					errorFlag++;
				}
			}
		}
		if(errorFlag==0) Print.m("Parsed file without errors.");
		else Print.e(errorFlag + " errors occured while parsing file.");
		
		plot(0, 100);
	}
	
	public void plot(int start, int length)
	{
		this.start = start;
		this.length = length;
		
		//Background color
		GraphicsContext gc = getGraphicsContext2D();
		gc.setFill(new Color(0.03, 0.03, 0.03, 1));
		gc.fillRect(0, 0, getWidth(), getHeight());
		
		if(channels == null)
		{
			gc.setStroke(Color.DEEPSKYBLUE);
			gc.setFill(Color.DEEPSKYBLUE);
			gc.fillText("Please select a file!", 100, 100);
		}
		
		Print.d("Draw!");
	}
	
	@Override
	public void resize(double width, double height)
	{
		super.setWidth(width);
		super.setHeight(height);
		plot(start, length);
	}
	
	@Override
	public double minWidth(double height)
	{
		return HEIGHT;
	}
	
	@Override
	public double minHeight(double width)
	{
		return HEIGHT;
	}
	
	@Override
	public boolean isResizable()
	{
		return true;
	}
}
