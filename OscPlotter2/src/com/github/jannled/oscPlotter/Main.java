package com.github.jannled.oscPlotter;
import gui.OscPlotter;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application 
{	
	public static FileChooser fileChooser = new FileChooser();
	
	public MenuBar menuBar;
	
	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception 
	{
		stage.setTitle("OscPlotter");
		
		OscPlotter osp = new OscPlotter();
		
		Scene scene = new Scene(osp);
		stage.setScene(scene);
		stage.sizeToScene();
		stage.show();
	}
}
