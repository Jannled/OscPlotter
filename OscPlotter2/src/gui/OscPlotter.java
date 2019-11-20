package gui;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import com.github.jannled.oscPlotter.CSVLoader;
import com.github.jannled.oscPlotter.Main;
import com.github.jannled.oscPlotter.Probe;

import javafx.scene.control.ColorPicker;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
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
	private ColorPicker csB;
	private ColorPicker csG;
	private TitledPane pTitled;
	
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
		Menu mFile = new Menu("_File");
		mFile.getItems().addAll(miOpen, miClear);
		
		//Menu Item Screenshot
		MenuItem miScreenshot = new MenuItem("Take Screenshot");
		miScreenshot.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
		miScreenshot.setOnAction(e -> {
			FileOutput.writeImage(plotPane.snapshot(null, null));
		});
				
		//Menu Screenshot
		Menu mScreenshot = new Menu("_Screenshot");
		mScreenshot.getItems().add(miScreenshot);
		
		//Menu Bar
		menuBar = new MenuBar(mFile, mScreenshot);
		this.setTop(menuBar);
		
		//Probe Menus
		probeMenu = new HBox(10);
		this.setBottom(probeMenu);
		
		//Bottom Menu
		VBox bottomMenu = new VBox(10);
		
		//Color Picker Background
		csB = new ColorPicker(plotPane.backgroundColor);
		csB.setOnAction(e -> {
			plotPane.backgroundColor = csB.getValue();
			plotPane.plot(probes);
		});
		
		//Color Picker Grid
		csG = new ColorPicker(plotPane.gridColor);
		csG.setOnAction(e -> {
			plotPane.gridColor = csG.getValue();
			plotPane.plot(probes);
		});
		
		pTitled = new TitledPane("Background", bottomMenu);
		pTitled.setExpanded(false);
		
		bottomMenu.getChildren().addAll(csB, csG);
		probeMenu.getChildren().add(pTitled);
		
		//CSV-Menu
		csvMenu = new CSVMenu();
	}
	
	public void addProbe(Probe probe)
	{
		probes.add(probe);
		plotPane.plot(probes);
		
		ProbeMenu pm = new ProbeMenu("Probe " + probe.getID(), probe, this);
		pm.expandedProperty().bindBidirectional(pTitled.expandedProperty());
		
		probeMenu.getChildren().add(pm);
	}
	
	public void plot()
	{
		plotPane.plot(probes);
	}
	
	public void clearProbes()
	{
		System.out.println("Remove all Probes!");
		probes.clear();
		probeMenu.getChildren().clear();
		probeMenu.getChildren().add(pTitled);
	}
	
	public List<Probe> getProbes()
	{
		return probes;
	}
}
