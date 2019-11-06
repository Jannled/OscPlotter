package gui;

import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

public class DropdownMenu<T> extends ComboBox<T> 
{
	public DropdownMenu()
	{
		super();
		setEditable(false);
	}
	
	public DropdownMenu(ObservableList<T> items)
	{
		super(items);
		setEditable(false);
	}
	
	public int getSelectedIndex()
	{
		return getSelectionModel().getSelectedIndex();
	}
}
