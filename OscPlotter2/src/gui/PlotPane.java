package gui;

import java.util.List;

import com.github.jannled.oscPlotter.Probe;

import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class PlotPane extends Canvas implements EventHandler<MouseEvent>
{
	public Color backgroundColor = Color.BLACK;
	public Color gridColor = Color.GRAY;
	
	public PlotPane()
	{
		super();
	}
	
	public PlotPane(double width, double height)
	{
		super(width, height);
	}
	
	public void plot(List<Probe> probes)
	{
		GraphicsContext gc = getGraphicsContext2D();
		
		gc.setFill(backgroundColor);
		gc.fillRect(0, 0, getWidth(), getHeight());
		
		gc.setStroke(gridColor);
		gc.strokeLine(0, getHeight()/2, getWidth(), getHeight()/2);
		
		for(Probe probe : probes)
		{
			gc.beginPath();
			gc.setStroke(probe.getColor());
			
			for(double i=0; i<probe.getValues().length; i+=probe.getXZoom())
			{
				double average = probe.getValue((int) i);
				
				if(probe.getXZoom() < 0)
				{
					//Calculate average
					for(int j=0; j<probe.getXZoom(); j++)
						average += probe.getValue((int) i+j);
					average /= probe.getXZoom();
				} 
				
				double onScreenY = average*probe.getYZoom() + getHeight()/2;
				
				
				gc.lineTo(i, onScreenY);
			}
			
			gc.stroke();
			gc.closePath();
		}
	}
	
	@Override
	public void handle(MouseEvent event) 
	{
		System.out.println("Halllo");
	}
	
	@Override
	public double prefWidth(double height) 
	{
		return getWidth();
	}
	
	@Override
	public double prefHeight(double width)
	{
		return getHeight();
	}
	
	@Override
	public boolean isResizable() 
	{
		return true;
	}
}
