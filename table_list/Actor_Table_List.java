package table_list;

import actor.Actor;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;

public class Actor_Table_List {

	public Actor_Table_List() {
		// TODO Auto-generated constructor stub
	}	
	
	/*******************************************************/
	//-----------------------TABLE-------------------------//
	
	 //* createTable is static to allow Army to define a table without having any Actor objects present. */
	  @SuppressWarnings( "unchecked" )
	public static TableView<Actor> createTable() {
	      // Code to create the basic TableView framework . . . but NO Actor objects are added here. The addition of Actor objects is the responsibility of the Army class.
	        TableView<Actor> table = new TableView<Actor>();
	    final double PREF_WIDTH_DOUBLE = 50.0;
	    table.setPrefWidth(PREF_WIDTH_DOUBLE*8.0); // based on 1 double-width column (name) and 3 single-width columns (strength, speed, health)
	        table.setEditable(true);
	        TableColumn<Actor, String>   nameCol      = new TableColumn<Actor, String>("Name");      nameCol.setCellValueFactory     (new PropertyValueFactory<Actor, String>("name"));      nameCol.setPrefWidth(PREF_WIDTH_DOUBLE*2.0);
	        TableColumn<Actor, Double>   strengthCol  = new TableColumn<Actor, Double>("Strength");  strengthCol.setCellValueFactory (new PropertyValueFactory<Actor, Double>("strength"));  strengthCol.setPrefWidth(PREF_WIDTH_DOUBLE * 1.5);
	        TableColumn<Actor, Double>   speedCol     = new TableColumn<Actor, Double>("Speed");     speedCol.setCellValueFactory    (new PropertyValueFactory<Actor, Double>("speed"));     speedCol.setPrefWidth(PREF_WIDTH_DOUBLE * 1.5);
	        TableColumn<Actor, Double>   healthCol    = new TableColumn<Actor, Double>("Health");    healthCol.setCellValueFactory   (new PropertyValueFactory<Actor, Double>("health"));    healthCol.setPrefWidth(PREF_WIDTH_DOUBLE * 1.5);

	    table.getColumns().addAll(nameCol, strengthCol, speedCol, healthCol);

	    // Code required to capture edited entries in the TableView and communicate back to target Actor object.
	    nameCol.setCellFactory(TextFieldTableCell.<Actor>forTableColumn());
	    nameCol.setOnEditCommit(new EventHandler<CellEditEvent<Actor, String>>()      { @Override public void handle(CellEditEvent<Actor, String> t) { Actor a = (t.getTableView().getItems().get(t.getTablePosition().getRow())); a.setName(t.getNewValue()); }}); // end setOnEditCommit()

	    strengthCol.setCellFactory(TextFieldTableCell.<Actor,Double>forTableColumn(new DoubleStringConverter()));
	    strengthCol.setOnEditCommit(new EventHandler<CellEditEvent<Actor, Double>>()     { @Override public void handle(CellEditEvent<Actor, Double> t) { Actor a = (t.getTableView().getItems().get(t.getTablePosition().getRow())); a.setStrength(t.getNewValue()); }}); // end setOnEditCommit()

	    speedCol.setCellFactory(TextFieldTableCell.<Actor,Double>forTableColumn(new DoubleStringConverter()));
	    speedCol.setOnEditCommit(new EventHandler<CellEditEvent<Actor, Double>>()     { @Override public void handle(CellEditEvent<Actor, Double> t) { Actor a = (t.getTableView().getItems().get(t.getTablePosition().getRow())); a.setSpeed(t.getNewValue()); }}); // end setOnEditCommit()

	    healthCol.setCellFactory(TextFieldTableCell.<Actor,Double>forTableColumn(new DoubleStringConverter()));
	    healthCol.setOnEditCommit(new EventHandler<CellEditEvent<Actor, Double>>()    { @Override public void handle(CellEditEvent<Actor, Double> t) { Actor a = (t.getTableView().getItems().get(t.getTablePosition().getRow())); a.setHealth(t.getNewValue()); }}); // end setOnEditCommit()
		return table;
    } // END TABLE
	  
	/*******************************************************/
	//------------------------LIST-------------------------//	  
	
    public static ListView<Actor> createList() {
		ListView<Actor> list = new ListView<Actor>();
		list.setPrefWidth( 400.0 );
		return list;
	} // END LIST

}

