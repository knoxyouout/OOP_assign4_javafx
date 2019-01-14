package army;

import table_list.Actor_Table_List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.effect.DropShadowBuilder;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;
import actor.Actor;
import actor.ActorFactory.Type;

/* To hold a collection of Actor objects */

public class Army {

	private Army opposingArmy;

	private String armyName;

	public ListView<Actor> listView;
	public TableView<Actor> tableView;
	public ObservableList<Actor> army;

	private Effect effectsToApplyToActorObjectsInArmy;

	public Army( String armyName, Color color ) {
		setArmyName( armyName );
		effectsToApplyToActorObjectsInArmy = DropShadowBuilder.create().spread( 0.6 ).radius( 10.0 ).color( color ).build();
		init();
	}

	private void init() { // INITIALIZER BLOCK: Start: Executed for every
		// possible constructor / deserialization
		army = FXCollections.observableArrayList(); // not serializable
		tableView = Actor_Table_List.createTable();
		tableView.setItems( army );
		listView = Actor_Table_List.createList();
		listView.setItems( army );
	} // INITIALIZER BLOCK: End

	public void populate( Type type, int actorCount, ObservableList<Node> observableList ) {
		for ( int i = 0; i < actorCount; ++i ) {
			Actor newActor = type.create( this );
			army.add( newActor );
			observableList.add( newActor.getBattleFieldAvatar() );
		}
	}

	public void editArmy( int index ) {
		for ( int i = index - 1; i < army.size(); ++i ) {
			army.get( i ).inputAllFields();
		}
	}

	public void display() {
		System.out.println( getArmyName() );
		for ( Actor actor : army ) {
			System.out.printf( "%d. %s\n", army.indexOf( actor ) + 1, actor );
		}
		System.out.println();
	}

	public String getArmyName() {
		return this.armyName;
	}

	public void setArmyName( String armyName ) {
		this.armyName = armyName;
	}

	public int getSize() {
		return army.size();
	}

	public ListView<Actor> getListViewOfActors() {
		return listView;
	}

	public TableView<Actor> getTableViewOfActors() {
		return tableView;
	}

	public Effect getEffectToApplyToActorObjectsInArmy() {
		return effectsToApplyToActorObjectsInArmy;
	}

	public void run() {
		for ( Actor actor : army ) {
			actor.move();
			if (actor.getHealth() == 0.0){
				actor.getBattleFieldAvatar().setVisible( false );				
			}
		}
	}

	public void suspend() {
		for ( Actor actor : army ) {
			actor.suspend();
		}
	}
	
	public void setDuration( double duration ){
		for (Actor actor : army){
			actor.setDuration( actor.getDuration() * duration );
		}		
	}

	public Actor findNearestActorInOpposingArmy( Actor actor ) {
		// receiving an actor in to the army, use distance to
		double distanceToClosestSoFar = Double.MAX_VALUE;
		Actor closestActor = null;

		Point2D myLocation = new Point2D( actor.getBattleFieldAvatar().getTranslateX(), actor.getBattleFieldAvatar().getTranslateY() );

		for ( Actor opposingActor : opposingArmy.army ) { // cycles through opposing army, finds x,y coordinates, and then compares to
			// initial actor's x,y
			Point2D opposingLocation = new Point2D( opposingActor.getBattleFieldAvatar().getTranslateX(), opposingActor.getBattleFieldAvatar().getTranslateY() );
			double distanceToNewestCandidateOpponent = myLocation.distance( opposingLocation );
			if ( distanceToNewestCandidateOpponent < distanceToClosestSoFar ) {
				distanceToClosestSoFar = distanceToNewestCandidateOpponent;
				closestActor = opposingActor;
			}
		}
		return closestActor;
	}
	
    
	public void setOpposingArmy( Army opposingArmy ) {
		this.opposingArmy = opposingArmy;
	}

}
