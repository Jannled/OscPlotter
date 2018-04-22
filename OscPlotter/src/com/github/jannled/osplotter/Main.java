package com.github.jannled.osplotter;

import java.io.File;
import java.util.Arrays;

import com.github.jannled.lib.FileUtils;
import com.github.jannled.lib.Print;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.*;

public class Main extends Application
{
	private final int channelCount = 4;
	
	Plotter plotter = new Plotter(this, channelCount);
	FileChooser fileChooser = new FileChooser();
	
	HBox pnlControls = new HBox();
	BorderPane pnlPlotter = new BorderPane();
	
	Spinner<Integer> spnStart = new Spinner<Integer>(0, 0, 0);
	Spinner<Integer> spnLength = new Spinner<Integer>(0, 0, 0);
	
	CheckBox[] chkChannels;
	
	public static void main(String[] args)
	{
		Print.m("Oscilloscpe Plotter launched with arguments " + Arrays.toString(args));
		Print.setOutputLevel(Print.ALL);
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		//Setup
		pnlPlotter.setCenter(plotter);
		
		//Control bar
		pnlControls.setStyle("-fx-background-color: white");
		pnlControls.setPadding(new Insets(10));
		pnlControls.setSpacing(10);
		pnlPlotter.setTop(pnlControls);
		
		Scene scene = new Scene(pnlPlotter);
		
		//File Dialog
		Button btnOpen = new Button("Open file");
		btnOpen.setOnAction(new EventHandler<ActionEvent>() 
		{
			@Override
			public void handle(ActionEvent event)
			{
				File file = fileChooser.showOpenDialog(primaryStage);
				if(file != null)
				{
					Print.m("Opening file " + file.getAbsolutePath());
					plotter.parse(FileUtils.readTextFileN(file));
				}
				else
					Print.e("The selected file is null.");
			}
		});
		pnlControls.getChildren().add(btnOpen);
		
		//Probes
		chkChannels = new CheckBox[channelCount];
		for(int i=0; i<channelCount; i++)
		{
			CheckBox checkBox = new CheckBox("Channel " + (i+1));
			checkBox.setSelected(true);
			checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> plotter.plot());
			pnlControls.getChildren().add(checkBox);
			chkChannels[i] = checkBox;
		}
		
		//Range
		spnLength.valueProperty().addListener((obs, oldValue, newValue) -> plotter.plot(plotter.getStart(), newValue));
		spnStart.valueProperty().addListener((obs, oldValue, newValue) -> plotter.plot(newValue, plotter.getLength()));
		
		spnLength.setEditable(true);
		spnStart.setEditable(true);
		
		pnlControls.getChildren().add(spnStart);
		pnlControls.getChildren().add(spnLength);
		
		//Show stage
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public Spinner<Integer> getSpnStart()
	{
		return spnStart;
	}
	
	public Spinner<Integer> getSpnLength()
	{
		return spnLength;
	}
	
	public CheckBox[] getSelectedChannels()
	{
		return chkChannels;
	}
}
