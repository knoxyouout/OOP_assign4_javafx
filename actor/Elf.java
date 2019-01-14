package actor;

import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import army.Army;
import util.InputGUI;

/* Elves can have a bow. */
public class Elf extends Actor {

	private static int elfCount = 0;
	private boolean hasBow;
	private boolean hasMount;
	private double originalSpeed;
	private double originalStrength;
	private static Color fillColor = Color.GOLD;

	public Elf(Army armyAllegiance) {
		super( ++elfCount, armyAllegiance );
		this.originalStrength = getStrength();
		this.originalSpeed = getSpeed();
		setHasBow( Math.random() < 0.5 );
		setMount( Math.random() < 0.5 );
	}

	@Override
	public void inputAllFields() {
		super.inputAllFields();
		setHasBow( InputGUI.getBooleanGUI( "Has Bow?" ) );
		setMount( InputGUI.getBooleanGUI( "Mounted?" ) );
	}

	/*******************************************************/
	//-----------------------BOW---------------------------//

	/** @return the hasBow */
	public boolean hasBow() {
		return hasBow;
	}

	/** @param hasBow the hasBow to set */
	public void setHasBow( boolean hasBow ) {
		final double STRENGTH_MOD = 1.04;

		this.hasBow = hasBow;
		if ( hasBow ) {
			setStrength( this.originalStrength * STRENGTH_MOD );
		} else {
			setStrength( this.originalStrength );
		}
	} // END BOW

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
	public String toString() {
		return String.format( "%s Bow: %-13b Mount: %-9b", super.toString(), hasBow(), hasMount() );
	}

	@Override
	protected Node buildShape() {
		Rectangle battlefieldAvatar = new Rectangle( 12.0, 8.0, fillColor  );
		battlefieldAvatar.setStrokeWidth( 2.0 );
		battlefieldAvatar.setFill( fillColor );
		return battlefieldAvatar;
	}

	@Override
	public Point2D getSubclassSpecificNewLocation( Actor nearestOpposingActor ) {
		if (this.getBattleFieldAvatar().intersects( nearestOpposingActor.getBattleFieldAvatar().getBoundsInParent())){
			this.setHealth( this.getHealth() - nearestOpposingActor.getStrength() / 3 );
		}
		return new Point2D( nearestOpposingActor.getBattleFieldAvatar().getTranslateX(), nearestOpposingActor.getBattleFieldAvatar().getTranslateY() );
	}

}
