package log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import webscraping.RegistryCNMV;

/**
 *
 * The LogManager has two functions:
 * 
 * <ol>
 * <li>It works as a historical record.
 * It means that LogManager keeps information about each execution in ScrapingHistory.log file.</li>
 * 
 * <li>The class catch exceptions and errors.
 * After that, it saves them in ErrorHistory.log file.</li>
 * </ol>
 * 
 * @author	Enrique Morales Montero (design, development, documentation)
 * @author	Javier Mora Gonzálbez (mentor and requirements analyst)
 * @author	Carlos Cano Ladera (mentor, guiding with design, development and documentation)
 * @since 	5/4/2019
 * @version	9/4/2019
 */
public class LogManager {
	
	private static final String FILE_PATH = "ScrapingHistory.log";
	private static final String EXCEPTIONS_FILE_PATH = "ErrorHistory.log";
	
	/**
	 * This procedure creates the history and log files.
	 * 
	 * @param PATH, This is the file path in a String.
	 * @return File
	 * @throws IOException
	 * @see LogManager#writeAttentionInLog() writeLog() - Method that uses this function.
	 */
	private static File getFileLog(final String PATH) throws IOException {
		
		File file = new File(PATH);
		
		if (!file.exists()) {
			file.createNewFile();
		}
		
		return file;
	}
	
	/**
	 * A public procedure  stores the following data in ErrorHistory.log file:
	 * The set of information includes 
	 * <ul>
	 * <li>		Date.							</li>
	 * <li>		Time .						</li>
	 * <li>		Type of exception.								</li>
	 * <li>		the code that throws the exception.	</li>
	 * </ul>
	 */
	public static void writeExceptionInLog(Exception exception, String message, String classAndMethod) {

		writeAttentionInLog();
		
		final LogDate thisDate = new LogDate();
		
		try {

			FileWriter fileWriter = new FileWriter(getFileLog(EXCEPTIONS_FILE_PATH), true);
			PrintWriter printWriter = new PrintWriter(fileWriter);

			printWriter.println("EXCEPTION!");
			printWriter.println("Date: " + thisDate.getDayMonthYear());
			printWriter.println("Hour: " + thisDate.getClockTime());
			printWriter.println("Loc.: " + classAndMethod);
			printWriter.println("Type: " + exception.getClass().getSimpleName() + ".");
			printWriter.println("Info: " + message);
			try {
				printWriter.println(exception.getCause().getLocalizedMessage());
			} catch (NullPointerException e) {}
			printWriter.println();
			printWriter.println();
			
			printWriter.close();
			fileWriter.close();
			
		} catch (IOException e) {e.printStackTrace();}
	}
	
	/**
	 * A public procedure  stores the following data in ErrorHistory.log file:
	 * The set of information includes 
	 * <ul>
	 * <li>		DAte							</li>
	 * <li>		Time						</li>
	 * <li>		Number of new records.						</li>	
	 * <li>		List of new entityNames inserted (company names).				</li>
	 * </ul>
	 */
	public static void writeListInLog(ArrayList<RegistryCNMV> list) {
		
		final LogDate thisDate = new LogDate();
		
		try {

			FileWriter fileWriter = new FileWriter(getFileLog(FILE_PATH), true);
			PrintWriter printWriter = new PrintWriter(fileWriter);

			printWriter.println("NEW SCRAPING ACTION!");
			printWriter.println("Date: " + thisDate.getDayMonthYear());
			printWriter.println("Hour: " + thisDate.getClockTime());
			printWriter.println("New registries: " + list.size() + ".");
			
			if (list.size() > 0) {
				
				for (RegistryCNMV registryCNMV : list) {
					printWriter.println("\t- " + registryCNMV.getEntity_name());
				}
				
			}
			
			printWriter.println();
			printWriter.println();
			
			printWriter.close();
			fileWriter.close();
			
		} catch (IOException e) {e.printStackTrace();}
		
	}
	
	/**
	 * A public procedure that stores the following data in the file:
	 * <ul>
	 * <li>		Error message.									</li>
	 * <li>		Date							</li>
	 * <li>		Time 						</li>
	 * </ul>
	 */
	public static void writeAttentionInLog() {
		
		final LogDate thisDate = new LogDate();
		
		try {

			FileWriter fileWriter = new FileWriter(getFileLog(FILE_PATH), true);
			PrintWriter printWriter = new PrintWriter(fileWriter);

			printWriter.println("ATTENTION!");
			printWriter.println("NEW SCRAPING ACTION WITH ERRORS!");
			printWriter.println("Date: " + thisDate.getDayMonthYear());
			printWriter.println("Hour: " + thisDate.getClockTime());			
			printWriter.println();
			printWriter.println();
			
			printWriter.close();
			fileWriter.close();
			
		} catch (IOException e) {e.printStackTrace();}
	}
}