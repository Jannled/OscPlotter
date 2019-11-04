package com.github.jannled.oscPlotter;

import javafx.scene.control.ColorPicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

public class ProbeMenu extends TitledPane 
{
	public ProbeMenu(String title, Probe probe, OscPlotter oscPlotter)
	{
		super();
		super.setText(title);
		
		VBox vbox = new VBox(10);
		
		ColorPicker cs = new ColorPicker(probe.getColor());
		cs.setOnAction(e -> {
			probe.setColor(cs.getValue());
			oscPlotter.plot();
		});
		setExpanded(false);
		
		Spinner<Double> spinnerX = new Spinner<Double>();
		SpinnerValueFactory<Double> spvX = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.1, 10, 1, 0.1);
		spinnerX.setValueFactory(spvX);
		
		Spinner<Double> spinnerY = new Spinner<Double>();
		SpinnerValueFactory<Double> spvY = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.1, 10, 1, 0.1);
		spinnerY.setValueFactory(spvY);
		
		
		vbox.getChildren().addAll(cs, spinnerX, spinnerY);
		this.setContent(vbox);
	}
}
