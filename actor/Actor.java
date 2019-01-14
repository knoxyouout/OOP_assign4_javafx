package actor;

import army.Army;
import javafx.animation.Animation;
import javafx.animation.RotateTransitionBuilder;
import javafx.animation.Transition;
import javafx.animation.TranslateTransitionBuilder;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;
import util.InputGUI;
import util.SingletonRandom;

/*
 * Actor must contain a name, health, strength, and speed. This will be an abstract super class
 */

public abstract class Actor {

	private static final int NAME_LENGTH_MAX = 11;
	private static final double HEALTH_MIN = 1.0;
	private static final double HEALTH_MAX = 200.00;
	private static final double STRENGTH_MIN = 30.0;
	private static final double STRENGTH_MAX = 100.00;
	private static final double SPEED_MIN = 30.0;
	private static final double SPEED_MAX = 100.00;

	private static int objectCount = 0;

	public SimpleStringProperty name = new SimpleStringProperty();
	public SimpleDoubleProperty health = new SimpleDoubleProperty();
	public SimpleDoubleProperty strength = new SimpleDoubleProperty();
	public SimpleDoubleProperty speed = new SimpleDoubleProperty();

	private Tooltip toolTip = new Tooltip();
	private Army armyAllegiance; // Used to capture the Effect applied to the battefieldAvatar, and used to probe its relationship to other Actor objects when evaluating move()
	private Node battlefieldAvatar; // No need to tag as "transient" since I am overriding readObject() and writeObject()
	private Transition transition; // Retained for run-time control of animations. Need reference-to value to support suspend() call.
	private double duration = 200.0;

	public Actor(){}
	
	public Actor( int subClassCount, Army armyAllegiance ) {
		this.armyAllegiance = armyAllegiance;
		battlefieldAvatar = buildShape(); // abstract method MUST be implemented in each subclass
		setName( this.getClass().getSimpleName() + " " + ++objectCount + ":" + subClassCount );
		setHealth( SingletonRandom.instance.getNormalDistribution( HEALTH_MIN, HEALTH_MAX, 2.0 ) );
		setStrength( SingletonRandom.instance.getNormalDistribution( STRENGTH_MIN, STRENGTH_MAX, 2.0 ) );
		setSpeed( SingletonRandom.instance.getNormalDistribution( SPEED_MIN, SPEED_MAX, 2.0 ) );
		setRandomLocation( armyAllegiance );
		adjustAvatarBasedOnActorAttributes();
	}

	public void inputAllFields() {
		setName( InputGUI.getString( "Enter Name: " ) );
		setHealth( InputGUI.getDouble( "Enter Health Value (Between: " + HEALTH_MIN + " and " + HEALTH_MAX + ")", HEALTH_MIN, HEALTH_MAX ) );
		setStrength( InputGUI.getDouble( "Enter Strength Value (Between: " + STRENGTH_MIN + " and " + STRENGTH_MAX + ")", STRENGTH_MIN, STRENGTH_MAX ) );
		setSpeed( InputGUI.getDouble( "Enter Speed Value (Between: " + SPEED_MIN + " and " + SPEED_MAX + ")", SPEED_MIN, SPEED_MAX ) );
	}

	public abstract boolean hasMount();
	public abstract void setMount( boolean hasMount );

	/*******************************************************/
	//-----------------------NAME--------------------------//

	public String getName() {
		return name.get();
	}

	/** @param name the name to set */
	public void setName( String name ) {
		if ( name.length() > NAME_LENGTH_MAX )
			name = name.substring( 0, NAME_LENGTH_MAX );
		this.name.set( name );
	} // END NAME

	/*******************************************************/
	//---------------------HEALTH--------------------------//

	public double getHealth() {
		return health.get();
	}

	/** @param health the health to set */
	public void setHealth( double health ) {
		if ( health < HEALTH_MIN ) {
			health = 0.0;
		} else if ( health > HEALTH_MAX ) {
			health = HEALTH_MAX;
		}
		this.health.set( health );
	} // END HEALTH

	/*******************************************************/
	//--------------------STRENGTH-------------------------//

	public double getStrength() {
		return strength.get();
	}

	/** @param strength the strength to set */
	public void setStrength( double strength ) {
		if ( strength < STRENGTH_MIN ) {
			strength = STRENGTH_MIN;
		} else if ( strength > STRENGTH_MAX ) {
			strength = STRENGTH_MAX;
		}
		this.strength.set( strength );
	} // END STRENGTH

	/*******************************************************/
	//----------------------SPEED--------------------------//

	public double getSpeed() {
		return speed.get();
	}

	/** @param speed the speed to set */
	public void setSpeed( double speed ) {
		if ( speed < SPEED_MIN ) {
			speed = SPEED_MIN;
		} else if ( speed > SPEED_MAX ) {
			speed = SPEED_MAX;
		}
		this.speed.set( speed );
	} // END SPEED
	
	/*******************************************************/
	//---------------SHAPE SIZE AND EFFECT------------------/	

	protected abstract Node buildShape(); // Each subclass will have a different look.

	public Node getBattleFieldAvatar() {
		return battlefieldAvatar;
	}

	// created as separate method: called when a TableView edit has taken place and the change in attributes needs to be reflected in the scene graph
	private void adjustAvatarBasedOnActorAttributes() {
		battlefieldAvatar.setScaleX( getStrength() / 60.0 );
		battlefieldAvatar.setScaleY( getStrength() / 60.0 );
		battlefieldAvatar.setEffect( armyAllegiance.getEffectToApplyToActorObjectsInArmy() );
		toolTip.setText( this.toString() );
		toolTip.setOpacity( 0.80 );
		Tooltip.install( battlefieldAvatar, toolTip );
	}

	/*******************************************************/
	//---------------MOVEMENT AND LOCATION-----------------//

	public boolean isActive() {
		return !( ( transition == null ) || ( transition.getStatus() != Animation.Status.RUNNING ) );
	}
	
	/** Call once to begin motion; each call to move() invokes one short-term animation; an "onFinished" CALLBACK is set to call move() again; looks recursive BUT IT IS NOT (I call it chained). */
    public void move() {
    	adjustAvatarBasedOnActorAttributes();
    	Point2D ptNewLocation = findNewLocationToMoveTo(); // COULD RETURN NULL if not moving
    	if ( ptNewLocation == null || getHealth() <= 0.0 ) { // don't move . . . should be in a rotate mode
    		transition = RotateTransitionBuilder.create() // look around then invoke move() when finished looking
        	    .node( battlefieldAvatar )
        	    .fromAngle( 0.0 )
        	    .toAngle( 360.0 )
        	    .cycleCount( 2 ) // if the Actor isn't yet healthy enough to move, then this version of move() (a RotateTransition) will be reasserted again and again.
        	    .autoReverse( true )
        	    .duration( Duration.seconds( 500.0 / getSpeed() ) )
        	    .onFinished( new EventHandler<ActionEvent>() {
        	        @Override
        	        public void handle( ActionEvent arg0 ) {
        	            move();
        	        }
        	    } ) // Looks recursive, but not really . . . it is chained, where the termination of a transition calls move() to spawn another.
        	    .build();
    		transition.play();
    	} else { // found a new location . . . move towards
    		transition = TranslateTransitionBuilder.create()
        	    .node( battlefieldAvatar )
        		.delay( Duration.seconds( 150.0 / getHealth() ) )  // before transition starts
        		.duration( Duration.seconds( getDuration() / getSpeed() ) )  //
        		.toX( ptNewLocation.getX() )
        		.toY( ptNewLocation.getY() )
        		.onFinished( new EventHandler<ActionEvent>() {
        		    @Override
        		    public void handle( ActionEvent arg0 ) {
        		        move();
        		    }
        		} ) // Looks recursive, but not really . . . it is chained, where the termination of a transition calls move() to spawn another.
        		.build();
    		transition.play();
    	}
    }
    
    public double getDuration(){
    	return this.duration;
    }
    
    public void setDuration( double duration ){
      	this.duration  = duration;    	
    }

	/** Determines the motion of an Actor object. Internally it calls a polymorphic method to define the new target location of the Actor (based on
	 * subclass processing).
	 * 
	 * @return new coordinate of the Node */
	public Point2D findNewLocationToMoveTo() {
		Actor nearestOpposingActor = armyAllegiance.findNearestActorInOpposingArmy( this );
		if ( nearestOpposingActor == null )
			return null;
			// Don't move. A "null" return value is normal under specific circumstances, for example, when their are no opposing Actor
		Point2D ptNewPossiblePoint = getSubclassSpecificNewLocation( nearestOpposingActor ); // Point2D is immutable
		double x = ptNewPossiblePoint.getX();
		double y = ptNewPossiblePoint.getY();
		// Snap the proposed newPossiblePoint to the inside boundaries of the scene.
		double sceneWidth = battlefieldAvatar.getScene().getWidth();
		double sceneHeight = battlefieldAvatar.getScene().getHeight();
		final double edgeOffset = 20.0;
		if ( x < 0.0 )
			x = edgeOffset;
		else if ( x > sceneWidth )
			x = sceneWidth - edgeOffset;
		if ( y < 0.0 )
			y = edgeOffset;
		else if ( y > sceneHeight )
			y = sceneHeight - edgeOffset;
		if ( y < sceneHeight * 0.109 && x < ( sceneWidth * 0.378 ) )
			x = ( sceneWidth * 0.38 );
		else if( y > sceneHeight * 0.109 && y < sceneHeight * 0.388 && x < ( sceneWidth * 0.322 ) )
			x = ( sceneWidth * 0.33 );
		else if ( y > sceneHeight * 0.388 && y < sceneHeight * 0.623 && x < ( sceneWidth * 0.397 ) )
			x = ( sceneWidth * 0.4 );
		else if ( y > sceneHeight * 0.623 && x < ( sceneWidth * 0.57 ) )
			x = ( sceneWidth * 0.58 );
		
		return new Point2D( x, y ); // Since Point2D is immutable, we cannot change newPossiblePoint and may need to manufacture a new one. So,
		
		// currently, I manufacture a new Point2D in all cases rather than testing if I need to.
		
    }
   
	public abstract Point2D getSubclassSpecificNewLocation( Actor nearestOpposingActor );

	public void suspend() {
		if ( transition == null ) // not active, nothing to do
			return;
		transition.stop();
		transition = null;
	}

	public void setRandomLocation( Army armyAllegiance ) {
		final double range = 100.0 / 2.0;
		final double spread = 1.2;
		Point2D ptCenterOfDistribution = null;

		if ( armyAllegiance.getArmyName().compareTo( "Forces of Light" ) == 0 ) {
			ptCenterOfDistribution = new Point2D( 452.0, 150.0 );
		} else if ( armyAllegiance.getArmyName().compareTo( "Forces of Darkness" ) == 0 ) {
			ptCenterOfDistribution = new Point2D( 690.0, 400.0 );
		}

		battlefieldAvatar.setTranslateX( SingletonRandom.instance.getNormalDistribution( ptCenterOfDistribution.getX() - range, ptCenterOfDistribution.getX() + range, spread ) );
		battlefieldAvatar.setTranslateY( SingletonRandom.instance.getNormalDistribution( ptCenterOfDistribution.getY() - range, ptCenterOfDistribution.getY() + range, spread ) );
	} 

	@Override
	public String toString() {
		return String.format( "%-17s Health: %-9.2f Strength: %-9.2f Speed: %-9.2f", getName(), getHealth(), getStrength(), getSpeed() );
	}
}
