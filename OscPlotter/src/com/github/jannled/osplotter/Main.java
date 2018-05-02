package com.github.jannled.osplotter;

import java.io.File;
import java.util.Arrays;

import com.github.jannled.lib.FileUtils;
import com.github.jannled.lib.LogListener;
import com.github.jannled.lib.Print;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application implements LogListener
{
	private final int channelCount = 4;
	
	private boolean showLog = false;
	
	Plotter plotter = new Plotter(this, channelCount);
	FileChooser fileChooser = new FileChooser();
	
	BorderPane pnlRoot = new BorderPane();
	HBox pnlControls = new HBox();
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
		
		//Control bar
		pnlControls.setStyle("-fx-background-color: white");
		pnlControls.setPadding(new Insets(10));
		pnlControls.setSpacing(10);
		pnlPlotter.setTop(pnlControls);
		
		Scene scene = new Scene(pnlRoot);
		
		//Menu bar
		MenuBar menuBar = new MenuBar();
		
		Menu fileMenu = new Menu("File");
		
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
		Print.registerListener(this);
		txtLog.setEditable(false);
		for(String s : Print.getLog())
			txtLog.setText(txtLog.getText() + '\n' + s);
		ScrollPane sp = new ScrollPane();
		sp.setContent(txtLog);
		sp.setFitToWidth(true);
		
		Button showLog = new Button("Show Log file");
		showLog.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event)
			{
				pnlPlotter.setBottom(sp);
			}
		});
		pnlPlotter.setBottom(showLog);
		
		//Probes
		Menu menuProbes = new Menu("Probes");
		
		chkChannels = new CheckMenuItem[channelCount];
		for(int i=0; i<channelCount; i++)
		{
			CheckMenuItem checkBox = new CheckMenuItem("Channel " + (i+1));
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
		
		pnlRoot.setCenter(pnlPlotter);
		
		//Build Menu
		fileMenu.getItems().addAll(openFileItem);
		menuBar.getMenus().addAll(fileMenu);
		pnlRoot.setTop(menuBar);
		
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
	
	public CheckBox[] getSelectedChannels()
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
