package actor;

import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import army.Army;
import util.InputGUI;
import util.SingletonRandom;

/*
 * Hobbits have a stealth attribute, run away from battle, and are tiny. If stealth is over 50%, change of being unseen
 */

public class Hobbit extends Actor {

	public static final double STEALTH_MIN = 0.0;
	public static final double STEALH_MAX = 100.0;

	private static int hobbitCount = 0;

	private double originalSpeed;
	private double stealth;
	private boolean isVisible;
	private boolean hasMount;
	private static Color fillColor = Color.BLUE;

	public Hobbit( Army armyAllegiance ) {
		super( ++hobbitCount, armyAllegiance );
		setStrength( getStrength() / 1.4 );
		setStealth( SingletonRandom.instance.getNormalDistribution( STEALTH_MIN, STEALH_MAX, 2.0 ) );
		this.originalSpeed = getSpeed();
		setMount( Math.random() < 0.5 );
	}

	@Override
	public void inputAllFields() {
		super.inputAllFields();
		setStealth( InputGUI.getDouble( "Enter Stealth: (Between: " + STEALTH_MIN + " and " + STEALH_MAX + ")", STEALTH_MIN, STEALH_MAX ) );
		setMount( InputGUI.getBooleanGUI( "Mounted?" ) );
	}

	/*******************************************************/
	//--------------STEALTH AND VISIBILITY-----------------//

	public double getStealth() {
		return this.stealth;
	}

	/** @param stealth the stealth to set */
	public void setStealth( double stealth ) {
		if ( stealth < STEALTH_MIN ) {
			stealth = STEALTH_MIN;
		} else if ( stealth > STEALH_MAX ) {
			stealth = STEALH_MAX;
		}
		setVisiblity( stealth );
		this.stealth = stealth;
	} // END STEALTH

	public boolean isVisible() {
		return this.isVisible;
	}

	public void setVisiblity( double stealth ) {
		// TODO implement this in a useful way somewhere
		if ( stealth >= 50 && stealth < 60 ) {
			isVisible = Math.random() < 0.5;
		} else if ( stealth >= 60 && stealth < 70 ) {
			isVisible = Math.random() > 0.6;
		} else if ( stealth >= 70 && stealth < 80 ) {
			isVisible = Math.random() > 0.7;
		} else if ( stealth >= 80 && stealth < 90 ) {
			isVisible = Math.random() > 0.8;
		} else if ( stealth >= 90 ) {
			isVisible = Math.random() > 0.9;
		} else {
			isVisible = true;
		}

	} // END VISIBILITY

	/*******************************************************/
	//-----------------------MOUNT-------------------------//

	@Override
	public boolean hasMount() {
		return this.hasMount;
	}

	@Override
	public void setMount( boolean hasMount ) {
		final double SPEED_MOD = 1.10;

		this.hasMount = hasMount;
		if ( hasMount ) {
			setSpeed( this.originalSpeed * SPEED_MOD );
		} else {
			setSpeed( this.originalSpeed );
		}

	} // END MOUNT
	

	@Override
	protected Node buildShape() {
		Rectangle battlefieldAvatar = new Rectangle( 12.0, 8.0, fillColor );
		battlefieldAvatar.setStrokeWidth( 2.0 );
		battlefieldAvatar.setFill( fillColor );
		battlefieldAvatar.contains( battlefieldAvatar.getHeight(), battlefieldAvatar.getWidth() );
		return battlefieldAvatar;
	}
	
	@Override
	public Point2D getSubclassSpecificNewLocation( Actor nearestOpposingActor ) {
		
		double actorXValue = this.getBattleFieldAvatar().getTranslateX();
		double actorYValue = this.getBattleFieldAvatar().getTranslateY();
		double opposingXValue = nearestOpposingActor.getBattleFieldAvatar().getTranslateX();
		double opposingYValue = nearestOpposingActor.getBattleFieldAvatar().getTranslateY();
		double sceneWidth = nearestOpposingActor.getBattleFieldAvatar().getScene().getWidth();
		double sceneHeight = nearestOpposingActor.getBattleFieldAvatar().getScene().getHeight();
		double differenceInXValues = Math.abs( nearestOpposingActor.getBattleFieldAvatar().getTranslateX() - this.getBattleFieldAvatar().getTranslateX() );
		double differenceInYValues = Math.abs( nearestOpposingActor.getBattleFieldAvatar().getTranslateY() - this.getBattleFieldAvatar().getTranslateY() );
		double xValue = 0.0;
		double yValue = 0.0;
		
		if (this.getBattleFieldAvatar().intersects( nearestOpposingActor.getBattleFieldAvatar().getBoundsInParent())){
			this.setHealth( this.getHealth() - nearestOpposingActor.getStrength() / 3 );
		}
		
		if ( differenceInXValues <= 10.0 || differenceInYValues <= 10.0 ) {
			if ( opposingXValue <= actorXValue && opposingYValue <= actorYValue ) {
				xValue = sceneWidth * 0.10;
				yValue = sceneHeight * 0.10;
			} else if ( opposingXValue >= actorXValue && opposingYValue >= actorYValue ) {
				xValue = -sceneWidth * 0.10;
				yValue = -sceneHeight * 0.10;
			} else if ( opposingXValue <= actorXValue && opposingYValue >= actorYValue ) {
				xValue = sceneWidth * 0.10;
				yValue = -sceneHeight * 0.10;
			} else if ( opposingXValue >= actorXValue && opposingYValue <= actorYValue ) {
				xValue = -sceneWidth * 0.10;
				yValue = sceneHeight * 0.10;
			}
			return new Point2D( xValue, yValue );
		} else if ( actorXValue != sceneWidth ) {
			xValue = actorXValue + ( actorXValue * 0.10 );
			yValue = actorYValue - ( actorYValue * 0.30 );
		}
		return new Point2D( xValue, yValue );
	}

	@Override
	public String toString() {
		return String.format( "%s Stealth: %-9.2f Visible: %-9b Mount: %-9b", super.toString(), getStealth(), isVisible(), hasMount() );
	}
}
