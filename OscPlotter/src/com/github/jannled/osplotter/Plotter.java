package com.github.jannled.osplotter;

import com.github.jannled.lib.Print;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

public class Plotter extends BorderPane implements EventHandler<MouseEvent>
{
	Main main;
	PlottCanvas canvas;
	
	public static final Color[] channelColors = new Color[] {Color.YELLOW, Color.MEDIUMPURPLE, Color.CORAL, Color.FIREBRICK, Color.LIGHTGREEN};
	
	public double[][] channels;
	public int channelCount;
	
	int start = 0;
	int length = 100;
	
	final static int WIDTH = 1280;
	final static int HEIGHT = 720;
	
	ScrollBar scrollBra;
	
	public Plotter(Main main, int channelCount)
	{
		canvas = new PlottCanvas(WIDTH, HEIGHT);
		this.channelCount = channelCount;
		this.main = main;
		
		widthProperty().addListener(evt -> plot(start, length));
		heightProperty().addListener(evt -> plot(start, length));
		
		setOnMouseMoved(this);
		setCenter(canvas);
		
		 scrollBra = new ScrollBar();
		 scrollBra.setMin(0);
		 scrollBra.setMax(0);
		 scrollBra.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
			{
				plot(newValue.intValue(), length);
			}
		});
		 setBottom(scrollBra);
		
		setMinSize(WIDTH, HEIGHT);
		plot(0, 100);
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
		
		IntegerSpinnerValueFactory vstart = (IntegerSpinnerValueFactory) main.getSpnStart().getValueFactory();
		IntegerSpinnerValueFactory vlength = (IntegerSpinnerValueFactory) main.getSpnLength().getValueFactory();
		
		vstart.setMax(channels[0].length-1);
		vlength.setMax(channels[0].length);
		vlength.setValue(length);
		
		scrollBra.setMax(length);
		plot(0, 100);
	}
	
	public void plot(int start, int length, double cursorX, double cursorY)
	{
		this.start = start;
		this.length = length;
		
		//Background color
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.clearRect(0, 0, getWidth(), getHeight());
		gc.setFill(new Color(0.03, 0.03, 0.03, 1));
		gc.fillRect(0, 0, getWidth(), getHeight());
		
		if(channels == null)
		{
			gc.setStroke(Color.DEEPSKYBLUE);
			gc.setFill(Color.DEEPSKYBLUE);
			gc.fillText("Please select a file!", 100, 100);
			
			return;
		}
		
		//Prevent ArrayIndexOutOfBoundsException
		if(start+length > channels[0].length)
		{
			Print.e("Entered range exceeds the amount of measuered oscilloscope samples!");
			return;
		}
		
		final double segmentLength = getWidth()/length;
		
		for(int c=0; c<channelCount; c++)
		{
			if(!main.getSelectedChannels()[c].isSelected())
				continue;
			
			gc.setStroke(channelColors[c]);
			gc.setFill(channelColors[c]);
			gc.beginPath();
			for(int i=start; i<length+start; i++)
			{
				gc.lineTo((i-start) * segmentLength, channels[c][i] + getHeight()/2);
			}
			gc.moveTo(0, getHeight()/2);
			gc.stroke();
			gc.closePath();
		}
		
		//Draw Cursor
		if(cursorX > 0 && cursorY > 0)
		{
			gc.setStroke(Color.DEEPSKYBLUE);
			gc.strokeLine(cursorX, 0, cursorX, getHeight());
		}
	}
	
	public void plot()
	{
		plot(start, length);
	}
	
	public void plot(int start, int width)
	{
		plot(start, width, -1, -1);
	}
	
	public int getStart()
	{
		return start;
	}
	
	public int getLength()
	{
		return length;
	}

	@Override
	public void handle(MouseEvent e)
	{
		plot(start, length, e.getX(), e.getY());
	}
}

class PlottCanvas extends Canvas
{
	public PlottCanvas(int width, int height)
	{
		super(width, height);
	}
	
	@Override
	public void resize(double width, double height)
	{
		super.setWidth(width);
		super.setHeight(height);
		//plot(start, length);
	}
	
	@Override
	public double minWidth(double height)
	{
		return Plotter.WIDTH;
	}
	
	@Override
	public double minHeight(double width)
	{
		return Plotter.HEIGHT;
	}
	
	@Override
	public boolean isResizable()
	{
		return true;
	}
}
