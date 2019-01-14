package util;
import javax.swing.JOptionPane;// for simple GUI Input/Output

/**
 * The InputGUI class implements simple I/O console operations. It provides methods for reading from the keyboard and displaying on the screen.
 * 
 * @author Rex Woollard
 * @version 1.9
 */
public final class InputGUI { // final prevents creation of subclasses using extends Input
	private InputGUI() { } // prevents the creation of a InputGUI object, since all methods are static (that is, no object is required to call the method).

	/**
	 * Inputs (reads) a line from a dialog box, capturing input as a <i>String</i>.
	 * @param prompt  Text in the message that is to be displayed
	 * @see javax.swing.JOptionPane
	 */
	public static String getString(String prompt) {
		return JOptionPane.showInputDialog(prompt);
	}

	/**
	 * Displays a message in a dialog box.
	 * @param message  <i>String</i> that is to be displayed. If <i>null</i> no message is displayed.
	 * @see javax.swing.JOptionPane
	 */
	public static void showMessageGUI(String message) {
		JOptionPane.showMessageDialog(null, message);
	}

	/**
	 * Displays a message in a dialog box.
	 * @param message  <i>String</i> that is to be displayed. If <i>null</i> no message is displayed.
	 * @see javax.swing.JOptionPane
	 */
	public static void showErrorGUI(String message) {
		JOptionPane.showMessageDialog(null, message, "ERROR", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Displays a prompt and attempts to retrieve <i>int</i> input; if no valid <i>int</i> input user is prompted to try again a limited number of times before
	 * @param prompt  <i>String</i> that is to be displayed. If <i>null</i> no message is displayed.
	 * @return value as an <i>int</i>
	 * @see javax.swing.JOptionPane
	 */
	public static int getInt(String prompt, int low, int high) {
		final int ALLOWABLE_INPUT_FAILURES = 3; // Avoid leaving a user stuck in here infinitely. If they fail to enter an int after 3 tries, bail out anyway.
		String response;
		int inputFailureCount = 0;
		while (true) { // use an infinite loop. Exit will occur on return if they enter a valid <i>int</i> or after exhausting the attempt limit.
			// try-block used to manage an interaction where there may be a failure: in particular, a user might enter something other than a parseable <i>int</i> value
			try {
				response = JOptionPane.showInputDialog(prompt); // no possible failure here, since we can pick up any pattern of characters
				// Possible failure on following line, since non-<i>int</i> characters will generate an exception in the <i>Integer.parseInt()</i> method
				// If a failure occurs in the <i>Integer.parseInt()</i> method call, then program execution will <i>throw</i> an exception that will immediately 
				//   abandon the <i>try</i>-block and jump to the closest matching <i>catch</i>-block. 
				int convertedResponse = Integer.parseInt(response);
				// we can only arrive here if the <i>Integer.parseInt()</i> method returned normally, that is, it did not <i>throw</i> an exception.
				if (convertedResponse < low || convertedResponse > high) {
					if (++inputFailureCount > ALLOWABLE_INPUT_FAILURES) {
						showErrorGUI("Too many unsuccessful attempts. Setting to 0");
						return 0;	// exits on failure to complete input operation
					}
					showErrorGUI("Number must be between " + low + " and " + high + " (inclusive).");
				} else
					return convertedResponse; // exits on successful completion of input operation
			} catch (NumberFormatException | NullPointerException exception) { // only arrive here if there was a failure in the <i>Integer.parseInt()</i> method or <Esc> at dialog
				if (++inputFailureCount < ALLOWABLE_INPUT_FAILURES) {
					showErrorGUI("You must enter an integer.");
				} else {
					showErrorGUI("Too many unsuccessful attempts. Setting to 0");
					return 0;	// exits on failure to complete input operation
				}
			}
		}
	} // end public static int getInt()

	/**
	 * Displays a prompt and attempts to retrieve <i>int</i> input; if no valid <i>int</i> input user is prompted to try again a limited number of times before
	 * @param prompt  <i>String</i> that is to be displayed. If <i>null</i> no message is displayed.
	 * @return value as an <i>int</i>
	 * @see javax.swing.JOptionPane
	 */
	public static double getDouble(String prompt, double low, double high) {
		final int ALLOWABLE_INPUT_FAILURES = 3; // Avoid leaving a user stuck in here infinitely. If they fail to enter an int after 3 tries, bail out anyway.
		String response;
		int inputFailureCount = 0;
		while (true) { // use an infinite loop. Exit will occur on return if they enter a valid <i>int</i> or after exhausting the attempt limit.
			// try-block used to manage an interaction where there may be a failure: in particular, a user might enter something other than a parseable <i>int</i> value
			try {
				response = JOptionPane.showInputDialog(prompt); // no possible failure here, since we can pick up any pattern of characters
				// Possible failure on following line, since non-<i>double</i> characters will generate an exception in the <i>Double.parseDouble()</i> method
				// If a failure occurs in the <i>Double.parseDouble()</i> method call, then program execution will <i>throw</i> an exception that will immediately 
				//   abandon the <i>try</i>-block and jump to the closest matching <i>catch</i>-block. 
				double convertedResponse = Double.parseDouble(response);
				// we can only arrive here if the <i>Integer.parseInt()</i> method returned normally, that is, it did not <i>throw</i> an exception.
				if (convertedResponse < low || convertedResponse > high) {
					if (++inputFailureCount > ALLOWABLE_INPUT_FAILURES) {
						showErrorGUI("Too many unsuccessful attempts. Setting to 0");
						return 0.0;	// exits on failure to complete input operation
					}
					showErrorGUI("Number must be between " + low + " and " + high + " (inclusive).");
				} else
					return convertedResponse; // exits on successful completion of input operation
			} catch (NumberFormatException | NullPointerException exception) { // only arrive here if there was a failure in the <i>Double.parseInt()</i> method or <Esc> at dialog
				if (++inputFailureCount < ALLOWABLE_INPUT_FAILURES) {
					showErrorGUI("You must enter an integer.");
				} else {
					showErrorGUI("Too many unsuccessful attempts. Setting to 0");
					return 0.0;	// exits on failure to complete input operation
				}
			}
		}
	} // end public static double getDouble()

	/**
	 * Displays a choice information panel with the options "Yes" / "No"
	 * @param prompt  <i>String</i> that is to be displayed. If <i>null</i> no message is displayed.
	 * @return boolean
	 * @see javax.swing.JOptionPane
	 */
	public static boolean getBooleanGUI(String prompt) {
		return (JOptionPane.showConfirmDialog(null, prompt, "Yes / No", JOptionPane.YES_NO_OPTION) == 0);
	}

	/**
	 * Displays a choice information panel where the programmer-defined options are laid out in an array of references to <i>String</i> objects. System auto-detects the length of the array.
	 * @param prompt  <i>String</i> that is to be displayed. If <i>null</i> no message is displayed.
	 * @param stringArrayOptions  Reference to an array of references to <i>String</i> objects.
	 * @return an <i>int</i> value: 0 if Yes is clicked, 1 if No is clicked, -1 if closed
	 * @see javax.swing.JOptionPane
	 */
	public static int getChoiceGUI(String prompt, String[] stringArrayOptions) {
		return JOptionPane.showOptionDialog(null, prompt, "Select One", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, stringArrayOptions, stringArrayOptions[0]);
	}
} // end Input class