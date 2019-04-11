package log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple class to get the date and time.
 * 
 * @author	Enrique Morales Montero (design, development, documentation)
 * @author	Javier Mora Gonzálbez (mentor and requirements analyst)
 * @author	Carlos Cano Ladera (mentor, guiding with design, development and documentation)
 * @since 	5/4/2019
 * @version	5/4/2019
 */
public class LogDate extends Date {

	private static final long serialVersionUID = 1L;

	public LogDate() {super();}
	
	/**
	 * @return String. A date in a dd/MMM/yyyy format.
	 */
	public String getDayMonthYear() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy");
		return dateFormat.format(this);
	}
	
	/**
	 * @return String. Time in HH:mm:ss format.
	 */
	public String getClockTime() {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		return dateFormat.format(this);
	}
	
}