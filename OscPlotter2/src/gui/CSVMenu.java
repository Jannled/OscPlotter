package gui;

import com.github.jannled.oscPlotter.CSVLoader;
import com.github.jannled.oscPlotter.Probe;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

public class CSVMenu extends Stage
{
	private BorderPane pBorder;
	private TableView<ObservableList<StringProperty>> tableView;
	private Button bOk;
	
	private int selectedColumn = -1;
	
	public static final String CSVSEP = ","; 
	
	@SuppressWarnings("unchecked")
	public CSVMenu() 
	{	
		//Main Pane
		pBorder = new BorderPane();
		pBorder.setPadding(new Insets(10));
		
		//Table View
		tableView = new TableView<ObservableList<StringProperty>>();
		pBorder.setCenter(tableView);
		
		//Select Column
		tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		tableView.getSelectionModel().setCellSelectionEnabled(true);
		tableView.getFocusModel().focusedCellProperty().addListener((obs, oldVal, newVal) -> {

		    if(newVal.getTableColumn() != null){
		    	selectedColumn = newVal.getColumn();
		    		
		    	tableView.getSelectionModel().selectRange(0, newVal.getTableColumn(), tableView.getItems().size(), newVal.getTableColumn());
		    }
		});
		
		//Bottom Menu
		HBox pHBox = new HBox();
		pBorder.setTop(pHBox);
		
		Scene s = new Scene(pBorder);
		this.setScene(s);
		
		//Ok Button
		bOk = new Button("Okay");
		BorderPane.setAlignment(bOk, Pos.CENTER);
		BorderPane.setMargin(bOk, new Insets(10));
		pBorder.setBottom(bOk);
		bOk.setDefaultButton(true);
	}
	
	public void show(String[] csvFile, OscPlotter oscPlotter)
	{	
		//Remove all Columns
		tableView.getColumns().clear();
		
		//Split CSV into columns
		String[][] file = new String[csvFile.length][csvFile[0].split(CSVSEP).length];
		for(int i=0; i<file.length; i++)
		{
			file[i] = csvFile[i].split(CSVSEP);
		}
		
		//Add Columns to TableView
		for(int i=0; i<file[0].length; i++)
		{
			tableView.getColumns().add(createColumn(i, null));
		}
		
		//Populate table	
		for(String[] sarray : file)
		{
			ObservableList<StringProperty> observableList = FXCollections.observableArrayList();
			
			for(String s : sarray)
				observableList.add(new SimpleStringProperty(s));
			
			tableView.getItems().add(observableList);
		}
		
		//Button Ok
		bOk.setOnAction(e -> {
			System.out.println("row.getSelectedIndex() " + selectedColumn);
			oscPlotter.addProbe(new Probe(CSVLoader.loadCSV(csvFile, selectedColumn), 1, 10));
			close();
		});
		
		//Show Dialog
		super.show();
	}
	
	/**
	 * Source: https://community.oracle.com/message/10731570
	 * @param columnIndex
	 * @param columnTitle
	 * @return
	 */
	private TableColumn<ObservableList<StringProperty>, String> createColumn(final int columnIndex, String columnTitle)
	{
		TableColumn<ObservableList<StringProperty>, String> column = new TableColumn<>();
		String title;
		if (columnTitle == null || columnTitle.trim().length() == 0)
		{
			title = "Column " + (columnIndex + 1);
		} else
		{
			title = columnTitle;
		}
		column.setText(title);
		column.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ObservableList<StringProperty>, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(
							CellDataFeatures<ObservableList<StringProperty>, String> cellDataFeatures)
					{
						ObservableList<StringProperty> values = cellDataFeatures.getValue();
						if (columnIndex >= values.size())
						{
							return new SimpleStringProperty("");
						} else
						{
							return cellDataFeatures.getValue().get(columnIndex);
						}
					}
				});
		return column;
	}
}
