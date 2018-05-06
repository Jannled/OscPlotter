package com.github.jannled.osplotter;

import java.io.File;
import java.util.Arrays;

import com.github.jannled.lib.FileUtils;
import com.github.jannled.lib.LogListener;
import com.github.jannled.lib.Print;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application implements LogListener
{
	private final int channelCount = 4;
	
	Plotter plotter = new Plotter(this, channelCount);
	FileChooser fileChooser = new FileChooser();
	
	BorderPane pnlPlotter = new BorderPane();
	
	static String[] args;
	
	Spinner<Integer> spnStart = new Spinner<Integer>(0, 0, 0);
	Spinner<Integer> spnLength = new Spinner<Integer>(0, 0, 0);
	
	CheckMenuItem[] chkChannels;
	
	TextArea txtLog = new TextArea(">> OscPlotter <<");
	
	public static void main(String[] args)
	{
		Print.m("Oscilloscpe Plotter launched with arguments " + Arrays.toString(args));
		Main.args = args;
		Print.setOutputLevel(Print.ALL);
		launch(args);
		Print.m("Exiting OscPlotter");
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		//Setup
		pnlPlotter.setCenter(plotter);
		
		Scene scene = new Scene(pnlPlotter);
		
		//Menu bar
		MenuBar menuBar = new MenuBar();
		
		Menu menuFile = new Menu("File");
		
		//File Dialog
		MenuItem openFileItem = new MenuItem("Open File");
		openFileItem.setOnAction(new EventHandler<ActionEvent>() 
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
		
		//Log
		CheckMenuItem showLog = new CheckMenuItem("Show log");
		Print.registerListener(this);
		txtLog.setEditable(false);
		for(String s : Print.getLog())
			txtLog.setText(txtLog.getText() + '\n' + s);
		ScrollPane sp = new ScrollPane();
		sp.setContent(txtLog);
		sp.setFitToWidth(true);
		
		showLog.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event)
			{
				if(showLog.isSelected())
				{
					pnlPlotter.setBottom(sp);
					sp.setMinViewportHeight(50);
					sp.setMinHeight(100);
				}
				else
					pnlPlotter.setBottom(null);
			}
		});
		
		//Probes
		Menu menuProbes = new Menu("Probes");
		
		chkChannels = new CheckMenuItem[channelCount];
		for(int i=0; i<channelCount; i++)
		{
			CheckMenuItem checkBox = new CheckMenuItem("Channel " + (i+1));
			checkBox.setSelected(true);
			checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> plotter.plot());
			menuProbes.getItems().add(checkBox);
			chkChannels[i] = checkBox;
		}
		
		//Range
		spnLength.valueProperty().addListener((obs, oldValue, newValue) -> plotter.plot(plotter.getStart(), newValue));
		spnStart.valueProperty().addListener((obs, oldValue, newValue) -> plotter.plot(newValue, plotter.getLength()));
		
		spnLength.setEditable(true);
		spnStart.setEditable(true);
		
		//pnlControls.getChildren().add(spnStart);
		//pnlControls.getChildren().add(spnLength);
		
		//Build Menu
		menuFile.getItems().addAll(openFileItem, showLog);
		menuBar.getMenus().addAll(menuFile, menuProbes);
		pnlPlotter.setTop(menuBar);
		
		//Show stage
		primaryStage.setScene(scene);
		primaryStage.show();
		
		//Check if a file is in the parameters
		
	}
	
	public Spinner<Integer> getSpnStart()
	{
		return spnStart;
	}
	
	public Spinner<Integer> getSpnLength()
	{
		return spnLength;
	}
	
	public CheckMenuItem[] getSelectedChannels()
	{
		return chkChannels;
	}

	@Override
	public void notifyLog(String message, int level)
	{
		if(level == Print.ALL && Print.getOutputLevel() != Print.ALL)
			return;
		txtLog.setText(txtLog.getText() + '\n' + message);
	}
}
