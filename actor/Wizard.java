package actor;

import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import army.Army;
import util.InputGUI;

public class Wizard extends Actor {

	private static int wizardCount = 0;
	public boolean hasStaff;
	public boolean hasMount;
	private double originalStrength;
	private double originalSpeed;
	private static Color fillColor;

	public Wizard(Army armyAllegiance) {
		super( ++wizardCount, armyAllegiance );
		this.originalStrength = getStrength();
		this.originalSpeed = getSpeed();
		setStaff( Math.random() < 0.5 );
		setMount( Math.random() < 0.5 );
		setFillColor();
	}

	@Override
	public void inputAllFields() {
		super.inputAllFields();
		setStaff( InputGUI.getBooleanGUI( "Has Staff?" ) );
		setMount( InputGUI.getBooleanGUI( "Mounted?" ) );
	}

	/*******************************************************/
	//-----------------------STAFF-------------------------//

	public boolean hasStaff() {
		return this.hasStaff;
	}

	public void setStaff( boolean hasStaff ) {
		final double STRENGTH_MOD = 1.17;

		this.hasStaff = hasStaff;
		if ( hasStaff ) {
			setStrength( this.originalStrength * STRENGTH_MOD );
		} else {
			setStrength( this.originalStrength );
		}

	} // END STAFF

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
	
	public Color getFillColor(){
		return Wizard.fillColor;
	}
	public void setFillColor(){
		if(Math.random() < 0.5){
			Wizard.fillColor = Color.WHITE;
		}else{
			Wizard.fillColor = Color.GREY;
		}
	}	

	@Override
	protected Node buildShape() {
		Polygon battlefieldAvatar = new Polygon( 0.0,0.0, 5.0,0.0,  8.0,-20.0, 15.0,-30.0, 
												 20.0,-28.0, 23.0,-26.0,  31.0,-18.0, 24.0,-21.0, 
												 20.0,-15.0, 25.0,0.0,  30.0,0.0, 28.0,2.0, 
												 2.0,2.0 );
        battlefieldAvatar.setStrokeWidth(2.0);
        battlefieldAvatar.setFill(getFillColor());
        return battlefieldAvatar;
	}

	@Override
	public String toString() {
		return String.format( "%s Staff: %-9b Mount: %-9b", super.toString(), hasStaff(), hasMount() );
	}

	@Override
	public Point2D getSubclassSpecificNewLocation( Actor nearestOpposingActor ) {
		if (this.getBattleFieldAvatar().intersects( nearestOpposingActor.getBattleFieldAvatar().getBoundsInParent())){
			this.setHealth( this.getHealth() - nearestOpposingActor.getStrength() / 3 );
		}
		// TODO Auto-generated method stub
		return new Point2D( nearestOpposingActor.getBattleFieldAvatar().getTranslateX(), nearestOpposingActor.getBattleFieldAvatar().getTranslateY() );
	}
}
