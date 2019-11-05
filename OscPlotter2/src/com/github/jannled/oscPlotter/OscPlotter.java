package com.github.jannled.oscPlotter;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javafx.scene.control.ColorPicker;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class OscPlotter extends BorderPane
{
	private List<Probe> probes = new LinkedList<Probe>();
	
	private PlotPane plotPane;
	private MenuBar menuBar;
	private HBox probeMenu;
	private CSVMenu csvMenu;
	
	public OscPlotter()
	{
		setMinWidth(720);
		setMinHeight(480);
		init();
	}
	
	private void init()
	{
		//Plot Pane
		plotPane = new PlotPane(720, 480);
		
		plotPane.widthProperty().bind(this.widthProperty()); //Resize with parent
		plotPane.heightProperty().bind(this.heightProperty()); //Resize with parent
		
		plotPane.widthProperty().addListener((e) -> plotPane.plot(probes));
		plotPane.heightProperty().addListener((e) -> plotPane.plot(probes));
		
		this.setCenter(plotPane);
		
		//Menu Item Open
		MenuItem miOpen = new MenuItem("Open");
		miOpen.setOnAction((e) -> {
			File file = Main.fileChooser.showOpenDialog(null);
			if(file != null)
			{
				String[] fileContent = CSVLoader.loadString(file);
				csvMenu.show(fileContent, this);
			}
		});
		
		//Menu Item Clear Probes
		MenuItem miClear = new MenuItem("Clear all Probes");
		miClear.setOnAction(e -> clearProbes());
		
		//Menu File
		Menu mFile = new Menu("File");
		mFile.getItems().addAll(miOpen, miClear);
		
		//Menu Bar
		menuBar = new MenuBar(mFile);
		this.setTop(menuBar);
		
		//Probe Menus
		probeMenu = new HBox(10);
		this.setBottom(probeMenu);
		
		//Background color
		VBox vbox = new VBox(10);
		
		//Color Picker Background
		ColorPicker csB = new ColorPicker(plotPane.backgroundColor);
		csB.setOnAction(e -> {
			plotPane.backgroundColor = csB.getValue();
			plotPane.plot(probes);
		});
		
		//Color Picker Grid
		ColorPicker csG = new ColorPicker(plotPane.gridColor);
		csG.setOnAction(e -> {
			plotPane.gridColor = csG.getValue();
			plotPane.plot(probes);
		});
		
		TitledPane pTitled = new TitledPane("Background", vbox);
		pTitled.setExpanded(false);
		
		vbox.getChildren().addAll(csB, csG);
		probeMenu.getChildren().add(pTitled);
		
		//CSV-Menu
		csvMenu = new CSVMenu();
	}
	
	public void addProbe(Probe probe)
	{
		probes.add(probe);
		plotPane.plot(probes);
		
		ProbeMenu pm = new ProbeMenu("Probe " + probe.getID(), probe, this);
		
		probeMenu.getChildren().add(pm);
	}
	
	public void plot()
	{
		plotPane.plot(probes);
	}
	
	public void clearProbes()
	{
		for(int i=0; i<probes.size(); i++)
			probes.remove(i);
		plotPane.plot(probes);
	}
	
	public List<Probe> getProbes()
	{
		return probes;
	}
}