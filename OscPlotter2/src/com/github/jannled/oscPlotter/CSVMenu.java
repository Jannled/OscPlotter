package com.github.jannled.oscPlotter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class CSVMenu extends Stage
{
	private BorderPane pBorder;
	private TableView<String> tableView;
	private ObservableList<String> observableList;
	private ComboBox<String> row;
	
	public static final String CSVSEP = ","; 
	
	public CSVMenu() 
	{
		pBorder = new BorderPane();
		tableView = new TableView<String>();
		pBorder.setCenter(tableView);
		
		tableView.setItems(observableList);
		
		//Combo Box
		HBox pHBox = new HBox();
		pBorder.setTop(pHBox);
		row = new ComboBox<String>();
		pHBox.getChildren().add(row);
		
		Scene s = new Scene(pBorder);
		this.setScene(s);
	}
	
	public void show(String[] csvFile, OscPlotter oscPlotter)
	{
		tableView.getColumns().remove(0, tableView.getColumns().size());
		
		String[][] file = new String[csvFile.length][csvFile[0].split(CSVSEP).length];
		for(int i=0; i<file.length; i++)
		{
			file[i] = csvFile[i].split(CSVSEP);
		}
		
		String[] colNames = new String[file[0].length];
		
		for(int i=0; i<file[0].length; i++)
		{
			colNames[i] = "Column " + (i+1);
			TableColumn<String, String> tc = new TableColumn<String, String>(colNames[i]);
			tableView.getColumns().add(tc);
		}
		
		ObservableList<String> rows = FXCollections.observableArrayList(colNames);
		row.getItems().addAll(rows);
		super.show();
		
		Button bOk = new Button("Okay");
		bOk.setOnAction(e -> {
			oscPlotter.addProbe(new Probe(CSVLoader.loadCSV(csvFile, colNum), 1, 10));
		});
	}
}
