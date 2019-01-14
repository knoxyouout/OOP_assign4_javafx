package actor;

import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import util.InputGUI;
import army.Army;

/* Orcs go Berserk */
public class Orc extends Actor {

	private static int orcCount;
	private boolean isBerserk;
	private boolean hasMount;
	private double originalSpeed;
	private double originalStrength;
	private static Color fillColor = Color.OLIVEDRAB;

	public Orc(Army armyAllegiance) {
		super( ++orcCount, armyAllegiance );
		this.originalStrength = getStrength();
		this.originalSpeed = getSpeed();
		setBerserk( Math.random() < 0.5 );
		setMount( Math.random() < 0.5 );
	}

	@Override
	public void inputAllFields() {
		super.inputAllFields();
		setBerserk( InputGUI.getBooleanGUI( "Berserk?" ) );
		setMount( InputGUI.getBooleanGUI( "Mounted?" ) );
	}

	/*******************************************************/
	//----------------------Berserk-------------------------//

	/** @return the isBerserk */
	public boolean isBerserk() {
		return isBerserk;
	}

	/** @param isBerserk the isBerserk to set */
	public void setBerserk( boolean isBerserk ) {
		final double STRENGTH_MOD = 1.22;

		this.isBerserk = isBerserk;
		if ( isBerserk ) {
			setStrength( this.originalStrength * STRENGTH_MOD );
		} else {
			setStrength( this.originalStrength );
		}
	} // END Berserk

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
		return String.format( "%s Berserk: %-9b Mount: %-9b", super.toString(), isBerserk(), hasMount() );
	}

	@Override
	protected Node buildShape() {
		Circle battlefieldAvatar = new Circle( 8.0, fillColor  );
		battlefieldAvatar.setStrokeWidth( 2.0 );
		battlefieldAvatar.setFill( fillColor );
		return battlefieldAvatar;
	}

	@Override
	public Point2D getSubclassSpecificNewLocation( Actor nearestOpposingActor ) {
		if (this.getBattleFieldAvatar().intersects( nearestOpposingActor.getBattleFieldAvatar().getBoundsInParent())){
			this.setHealth( this.getHealth() - nearestOpposingActor.getStrength() / 3 );
		}
		if (nearestOpposingActor.getStrength() > 50.0){
			return new Point2D( nearestOpposingActor.getBattleFieldAvatar().getTranslateX(), nearestOpposingActor.getBattleFieldAvatar().getTranslateY() );
		}else {
			return new Point2D( this.getBattleFieldAvatar().getTranslateX() + 10, this.getBattleFieldAvatar().getTranslateY() + 10);
		}
	}

}
