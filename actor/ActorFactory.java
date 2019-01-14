package actor;

import army.Army;

public final class ActorFactory {
	
	/** enum is a special data type that enables for a variable to be a set of predefined constants. The variable must be equal to one of the values
	 * that have been predefined for it. */
	public enum Type {
		HOBBIT() { @Override public Actor create( Army armyAllegiance ) { return new Hobbit( armyAllegiance ); } }, // HOBBIT is a constant, thus all UPPERCASE letters 
		WIZARD() { @Override public Actor create( Army armyAllegiance ) { return new Wizard( armyAllegiance ); } }, 
		ORC()    { @Override public Actor create( Army armyAllegiance ) { return new Orc   ( armyAllegiance ); } }, 
		ELF()    { @Override public Actor create( Army armyAllegiance ) { return new Elf   ( armyAllegiance ); } },
		RANDOM() { @Override public Actor create( Army armyAllegiance ) { return createActorRandomSelection( armyAllegiance ); } };
		
		/** Polymorphic method that will bind to the specific create() method for the actual named type (e.g. HOBBIT); create an object of that type
		 * and return a reference-to it.   
		 * 
		 * This is required to use a method inside an enum type
		 * 
		 * @param armyAllegiance Used to define the <i>Army</i> allegiance of the <i>Actor</i>.
		 * @return reference-to <i>Actor</i> object created through random number selection. */
		public abstract Actor create( Army armyAllegiance ); // supports polymorphic call where actual subclass objects are created. 
	
	} // end enum Type 

	public final static int numTypes = Type.values().length; // Auto detects the number of CONSTANTS that have been defined "public" is acceptable because it is a CONSTANT "final" 

	/** Method that will select one of the available <i>Actor</i> types, create an object of that type and return a reference-to that object.
	 * 
	 * @param armyAllegiance Used to define the <i>Army</i> allegiance of the <i>Actor</i>.
	 * @return reference-to <i>Actor</i> object created through random number selection. allegiance */
	public final static Actor createActorRandomSelection( Army armyAllegiance ) {
		return Type.values()[ (int) ( Math.random() * ( numTypes - 1 ) ) ].create( armyAllegiance ); // subtract last because one enum type is RANDOM 
	} // end createActorRandomSelection() 

} // end class ActoryFactory