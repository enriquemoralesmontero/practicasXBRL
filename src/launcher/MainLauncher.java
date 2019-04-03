package launcher;

import java.util.ArrayList;

import database.classes.DataBaseManager;
import webscraping.collectors.Scraper;
import webscraping.extracteddata.objects.RegistryCNMV;

/**
 * The main class that runs the entire project.
 * 
 * @author	Enrique Morales Montero
 * @author	Javier Mora Gonzálbez (project manager)
 * @author	Carlos Cano Ladera (collaborator, code reviewer)
 * @since	28/3/2019
 * @version	3/4/2019
 */
public class MainLauncher {
	
	/**
	 * The website that is going to be scraped.
	 */
	public static final String webURL = "http://cnmv.es/Portal/Consultas/IFI/ListaIFI.aspx?XBRL=S";
	
	/**
	 * Main method.
	 * Its main functions are the following:
	 * 
	 * <ol>
	 * <li>	Take the first registry in the database.	</li>
	 * <li>	Extract data from the Web (web scraping).	</li>
	 * <li>	List the extracted data.					</li>
	 * <li>	Store the extracted data in the database.	</li>
	 * </ol>
	 * 
	 * @param args - They are not necessary.
	 */
	public static void main(String[] args) {

		long milliseconds = System.currentTimeMillis();
		
		System.out.println(" · Starting...\n");

		
		// 1 - Getting the first registry in the database.
		//
		// It will serve to avoid having to review all the data on the website.
		// If the database is empty, ignore this step.
		
		DataBaseManager mysql = new DataBaseManager();						// MySQL database.
		String lastUrlInfoContext = "NULL";
		
		if (mysql.hasRegistries()) {							// The database is not empty.
			lastUrlInfoContext = mysql.getLastUrlInfoContext();	// Getting the last url_info_context...
			System.out.println(" · Last registry:\n\n\t" + lastUrlInfoContext);
		}
		
		
		// 2 - Web scraping.
		//
		// The data will be stored in a list of registries.
		// All data is collected until it matches the last record in the database.
		
		System.out.println("\n · Scraping data...");
		
		ArrayList<RegistryCNMV> list = Scraper.getListOfInfoCNMV(webURL, lastUrlInfoContext);	// Getting the list of data extracted by the web scraper.
		
		
		// 3 - Listing scraped data.
		//
		// The data extracted by the web scraper will be displayed.
		// They will be show by console.
		// If there is no new data, this list is omitted.
		
		System.out.println("\n\n · New registries in CNMV: " + list.size() + "\n");
		
		if (list.size() > 0) {						// If the list is not empty.
			
			for (int i = 0; i < list.size(); i++) {	// Loop to show all the data in the list.
				System.out.println(list.get(i));
			}
			
			
			// 4 - Storage in the MySQL database.
			//
			// The data is stored in order if it is not already in the database.
			// If there is no new data, this process is omitted.
			
			System.out.println("\nInserting in the database...\n");
			mysql.store(list);						// Storing extracted data...
		}	
		
		milliseconds = (System.currentTimeMillis() - milliseconds) / 1000;
		System.out.println("\n · Finish in " + milliseconds + " seconds!");
	}
}