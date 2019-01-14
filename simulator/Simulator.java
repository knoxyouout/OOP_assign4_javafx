package simulator;

import java.io.Serializable;

import javafx.scene.Scene;
import javafx.scene.control.LabelBuilder;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import table_list.Actor_Table_List;
import util.InputGUI;
import actor.ActorFactory;
import army.Army;

public class Simulator extends StackPane implements Serializable {
    /**
	 * 
	 */
    private static final long serialVersionUID = 6569177158645537576L;
    private Army forcesOfLight;
    private Army forcesOfDarkness;
    private transient Stage primaryStage;
    private transient Stage tableViewStage;
    private transient Stage stageListArmiesWindow;
    private boolean populated;

    public Simulator( Stage primaryStage ) {
	forcesOfLight = new Army( "Forces of Light", Color.SPRINGGREEN );
	forcesOfDarkness = new Army( "Forces of Darkness", Color.DARKRED );
	forcesOfLight.setOpposingArmy( forcesOfDarkness );
	forcesOfDarkness.setOpposingArmy( forcesOfLight );
	this.primaryStage = primaryStage;
	buildTableViewStage();
	buildListViewWindow();
	this.populated = false;

    } // end constructor

	/*******************************************************/
	//-----------------LIST VIEW WINDOW--------------------//
    
    public final void buildListViewWindow() { // final because of its use in the constructor
	VBox vBoxLightArmy = new VBox( 5.0 );
	vBoxLightArmy.getChildren().addAll( LabelBuilder.create().text( forcesOfLight.getArmyName() ).textAlignment( TextAlignment.CENTER ).build(), forcesOfLight.getListViewOfActors() );
	VBox vBoxDarkArmy = new VBox( 5.0 );
	vBoxDarkArmy.getChildren().addAll( LabelBuilder.create().text( forcesOfDarkness.getArmyName() ).textAlignment( TextAlignment.CENTER ).build(), forcesOfDarkness.getListViewOfActors() );
	HBox hBoxSceneGraphRoot = new HBox( 5.0 );

	hBoxSceneGraphRoot.getChildren().addAll( vBoxLightArmy, vBoxDarkArmy );

	if ( stageListArmiesWindow != null ) {
	    stageListArmiesWindow.close();
	    stageListArmiesWindow.setScene( null );
	}
	stageListArmiesWindow = new Stage( StageStyle.UTILITY );
	stageListArmiesWindow.setScene( new Scene( hBoxSceneGraphRoot ) );
    } // END LIST VIEW WINDOW
    
	/*******************************************************/
	//-----------------TABLE VIEW STAGE--------------------//


	public final void buildTableViewStage() { // final because of its use in the constructor
    Actor_Table_List.createTable();    
	VBox vBoxLightArmy = new VBox( 5.0 );
	vBoxLightArmy.getChildren().addAll( LabelBuilder.create().text( forcesOfLight.getArmyName() ).textAlignment( TextAlignment.CENTER ).build(), forcesOfLight.getTableViewOfActors() );
	VBox vBoxDarkArmy = new VBox( 5.0 );
	vBoxDarkArmy.getChildren().addAll( LabelBuilder.create().text( forcesOfDarkness.getArmyName() ).textAlignment( TextAlignment.CENTER ).build(), forcesOfDarkness.getTableViewOfActors() );
	HBox hBoxSceneGraphRoot = new HBox( 5.0 );

	hBoxSceneGraphRoot.getChildren().addAll( vBoxLightArmy, vBoxDarkArmy );

		if ( tableViewStage != null ) {
			tableViewStage.close();
			tableViewStage.setScene( null );
		}
		tableViewStage = new Stage( StageStyle.UTILITY );
		tableViewStage.initOwner( primaryStage );
		tableViewStage.setScene( new Scene( hBoxSceneGraphRoot ) );
	} // END TABLE VIEW STAGE

	public void populate() {
		forcesOfLight.populate( ActorFactory.Type.HOBBIT, 4, getChildren() );
		forcesOfLight.populate( ActorFactory.Type.ELF, 10, getChildren() );
		forcesOfLight.populate( ActorFactory.Type.WIZARD, 5, getChildren() );

		forcesOfDarkness.populate( ActorFactory.Type.ORC, 17, getChildren() );
		forcesOfDarkness.populate( ActorFactory.Type.WIZARD, 2, getChildren() );
		setPopulated( true );
	} // end populate()

	public void displayLightArmyToConsole() {
		forcesOfLight.display();
	}

	public void displayDarkArmyToConsole() {
		forcesOfDarkness.display();
	}

	public void editDarkArmy() {
		forcesOfDarkness.editArmy( InputGUI.getInt( "Index to Edit (Max: " + ( forcesOfDarkness.getSize() - 1 ) + ")", 0, forcesOfDarkness.getSize() - 1 ) );
	}

	public void editLightArmy() {
		forcesOfLight.editArmy( InputGUI.getInt( "Index to Edit (Max: " + ( forcesOfLight.getSize() - 1 ) + ")", 0, forcesOfLight.getSize() - 1 ) );
	}

	public void openListWindow() {
		stageListArmiesWindow.show();
	}

	public void closeListWindow() {
		stageListArmiesWindow.close();
	}

	public void closeTableWindow() {
		tableViewStage.close();
	}

	public void openTableWindow() {
		tableViewStage.show();

	}

	public void run() {
		forcesOfLight.run();
		forcesOfDarkness.run();
	}

	public void suspend() {
		forcesOfLight.suspend();
		forcesOfDarkness.suspend();

	}
    
    public void speedUp(){
    	final double TEN_PERCENT_FASTER = 0.90;
    	forcesOfLight.setDuration( TEN_PERCENT_FASTER );
    	forcesOfDarkness.setDuration( TEN_PERCENT_FASTER );
    }
    
    public void slowDown(){
    	final double TEN_PERCENT_SLOWER = 1.10;
    	forcesOfLight.setDuration( TEN_PERCENT_SLOWER );
    	forcesOfDarkness.setDuration( TEN_PERCENT_SLOWER );    	
    }
    
    public void resetSpeed(){
    	final double DEFAULT_DURATION = 200.0;
    	forcesOfLight.setDuration( DEFAULT_DURATION );
    	forcesOfDarkness.setDuration( DEFAULT_DURATION );
    }
    
    public boolean isPopulated(){
    	return this.populated;
    }
    public void setPopulated(boolean pop){
    	this.populated = pop;
    }   
    
} // end class Simulator