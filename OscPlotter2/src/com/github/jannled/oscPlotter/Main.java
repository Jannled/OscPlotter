package com.github.jannled.oscPlotter;
import java.io.File;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application 
{
	private static final String TESTPATH = "C:\\Users\\jannl\\OneDrive\\Dokumente\\FH\\Physik & Elektrotechnik\\Protokolle\\01 Messtechnik\\ALL0000\\F0000CH1.CSV";
	
	static FileChooser fs = new FileChooser();
	
	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception 
	{
		stage.setTitle("OscPlotter");
		
		BorderPane pMain = new BorderPane();
		
		//Plot Pane
		PlotPane plotPane = new PlotPane(720, 480);
		plotPane.plot(CSVLoader.loadCSV(CSVLoader.loadString(TESTPATH), 4), 0.5);
		pMain.setCenter(plotPane);
		
		//Menu File
		Menu mFile = new Menu("File");
		
		//Menu Item Open
		MenuItem miOpen = new MenuItem("Open");
		mFile.getItems().addAll(miOpen);
		miOpen.setOnAction((e) -> {
			File file = fs.showOpenDialog(stage);
			if(file != null)
				plotPane.plot(CSVLoader.loadCSV(CSVLoader.loadString(file), 4), 0.5);
		});
		
		//Menu Bar
		MenuBar menuBar = new MenuBar(mFile);
		pMain.setTop(menuBar);
		
		Scene scene = new Scene(pMain);
		stage.setScene(scene);
		stage.show();
	}
}
