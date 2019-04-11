package webscraping;

import static log.LogManager.writeExceptionInLog;
import static cryptography.Algorithm.generateSHA3_256;
import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * <p>The class that collects the information from the requested web page.
 * This program has to accede to two levels of a HyperText document.</p>
 * 
 * <p>In the main website, we find rows in a table.
 * Each row summarizes information for a company quarterly report.
 * Anytime a report is given to the CNMV, the CNMV website manager adds this new report to his website.
 * This is an individual record we partially store in the RegistryCNMV object.
 * 
 * To see details about this information you can redirect to the RegistryCNMV commentaries section.</p>
 * 
 * <p>Each record in the HTML information has four fields and a link to the full report.
 * This new page (link) contains all the full report information.
 * Using the full report, we complete the data in the RegistryCNMV.
 * Therefore, the object is entirely filled in, and finally, it is ready to be stored at the database</p>
 * 
 * @author	Enrique Morales Montero (design, development, documentation)
 * @author	Javier Mora Gonzálbez (mentor and requirements analyst)
 * @author	Carlos Cano Ladera (mentor, guiding with design, development and documentation)
 * 
 * @since 	29/3/2019
 * @version 9/4/2019
 * 
 * @see RegistryCNMV RegistryCNMV object
 */
public class Scraper {
	
	/**
	 * This is the main method in the scraper.   
	 * It follows these steps:
	 * 
	 * <ol>
	 * <li>	Instance a list.													</li>
	 * <li>	Check that the connection code to the web page is correct. (200).	</li>
	 * <li>	Get the HTML document.												</li>
	 * <li>	Treats a row of the HTML table (a full report summary). And get the full report link.</li>
	 * <li>	It goes on getting an instance of a web crawler to enter the full report link.</li>
	 * <li> Put all the information in a new RegistryCNMV object. And then we add the object to the ArrayList.</li>
	 * <li>	It continues with the next row until the row information match with the most recent record in the database.</li>
	 * <li> As a result, as the loop finishes, we have an ArrayList that gathers all the new reports.</li>
	 * </ol>
	 * 
	 * @param webURL - Text string with the URL of the web page.
	 * @param lastUrlInfoContext 
	 * 
	 * @return An ArrayList<RegistryCNMV> (a list of all the extracted data).
	 * 
	 * @see Scraper#getStatusConnectionCode() getStatusConnectionCode() - Method to check the connection code.
	 * @see <a href="http://cnmv.es/Portal/Consultas/IFI/ListaIFI.aspx?XBRL=S">CNMV website</a>
	 */
	public static ArrayList<RegistryCNMV> getListOfInfoCNMV(final String webURL, final String lastUrlInfoContext) {
		
		// 1 - Instanced list.
		
		ArrayList<RegistryCNMV> list = new ArrayList<RegistryCNMV>();		// List of scraped data.
			
		
		// 2 - Checking the connection code.
		//
		// First, we check whether the request returns the code 200 (Ok code).
		// If the URL connection fails, it returns an error code (400, 404...)
		
		System.out.println("\n\tThis task may take a while.");
		
		if (getStatusConnectionCode(webURL) == 200) {		

			
			// 3 - It gets the HTML document.
			//
			// We assign the HTML Code (DOM tree) to the HTML document variable.
			
			Document document = getHtmlDocument(webURL); 	

			
			// 4 - We deal with each row.
			// 
			// JSoup extracts the summary report info (including each link to the full report).
            		
            Elements rows = document.select("tr"); 	// Rows obtained.
            System.out.println("\n\tElements: " + rows.size() + " registries.");
            
            
            // 5 - Instantiate the web crawler.
            //
            // The web spider gets into each individual hypertext links.
            // As a reminder, each link contains the full information associated with this CNMV company quarter financial information.
            // This step completes the data for each registry recorded in the collection.
            
            Crawler spiderBot = new Crawler();
            
            
            // 6 - Go through all the rows in the tabla to get data from each report .
            //
            // We use the JSoup library to recover information from the DOM tree.
            // It is not mandatory to inspect each row.
            // The loop finishes as soon as the record presently extracted matches the most recent record in the database.
            // Finally, the program adds each new gathered record in an ArrayList.
            
            for (int i = 1; i < rows.size(); i++) {
            	
            	System.out.print("\n\t\t- Scanning... " + i + "/" + rows.size());
    			
            	Element element = rows.get(i);						// Each element contains the row of the HTML table with its HTML information.
    			
    			Element hyperlink = element.select("a").first();	// It contains the url_info_context. The spider will use it later.
    			
    			
    			// JSoup data extraction:
    			//	- url_info_context
    			//	- entityName
    			
    			String url_info_context = hyperlink.attr("href").replace("../..", "http://cnmv.es/Portal");
    			String entityName = element.getElementsByAttributeValue("data-th", "Nombre del emisor").text();
    			
    			System.out.print("\t" + entityName);
    			
    			
    			// Improved performance control:
    			//		If the last gathered information matches with the most recent record in the database, it gets out of the loop.
    			//		As it does so, it is not mandatory to have a check for changes and new records of the CNMV's web.
    			
    			if (url_info_context.equals(lastUrlInfoContext)) {
    				System.out.println("\n\t\t\t\t\t- This registry (" + i + "/" + rows.size() + ") matches the last one in the database.");
    				System.out.println("\t\t\t\t\t- Finishing the scraping...");
    				break;
    			}
    			
    			
    			// It requests the spiderbot to deal with a singular web link (full report page).
    			// The crawler retrieves the full information we need for each CNMV entry.
    			 
    			spiderBot.setDocHMTL(getHtmlDocument(url_info_context));
    			
    			
    			// The spider extracts the following data fields::
    			//	- url_ixbrl
    			//	- entityCode
    			//	- period_end
    			//	- form
    			
    			String url_ixbrl = spiderBot.getAttrValue("ctl00_ContentPrincipal_ctl11_hlDescargaInforme", "href").replace("..", "http://cnmv.es/Portal");
    			String entityCode = spiderBot.getPlainText("ctl00_ContentPrincipal_ctl10_lblNIFCont");  			
    			String period_end = spiderBot.getPlainText("ctl00_ContentPrincipal_ctl10_lblFinPeriodoCont");
    			String form = spiderBot.getPlainText("ctl00_ContentPrincipal_ctl11_txtInfoXBRL").replaceAll("El informe ha sido elaborado basándose en la taxonomía ", "").replace(".", "");
    			
    			
    			// Some entities that are not yet n the database could have been modified lately due to some changes in quarterly company reports, before being treated by our program.
    			// The consequence of an update is that the attribute names change.
                // As a result, we need to take this into consideration.
    			// Our program should control these attribute changes before introducing the new records in our database.
    			// It barely happens, but it could occur.
    			
    			for (int j = 11; j < 20 && url_ixbrl == "NULL"; j++) {

    				System.err.print(" (new updates)");	// A text is shown to indicate that this registry was modified by the entity.
					url_ixbrl = spiderBot.getAttrValue("ctl00_ContentPrincipal_ctl" + String.valueOf(j + 1) + "_hlDescargaInforme", "href").replace("..", "http://cnmv.es/Portal");
					entityCode = spiderBot.getPlainText("ctl00_ContentPrincipal_ctl" + String.valueOf(j) + "_lblNIFCont");
					period_end = spiderBot.getPlainText("ctl00_ContentPrincipal_ctl" + String.valueOf(j) + "_lblFinPeriodoCont");
					form = spiderBot.getPlainText("ctl00_ContentPrincipal_ctl" + String.valueOf(j + 1) + "_txtInfoXBRL").replaceAll("El informe ha sido elaborado basándose en la taxonomía ", "").replace(".", "");
				}
    			
    			
    			// Other data:
    			//	- format
    			//	- oam
    			//	- country
    			
    			final String format = "XBRL";
    			final String oam = "CNMV";
    			final String country = "ES";
    			
    			
    			// The last data:
    			//	- hash_code
    			
    			String hash_code = generateSHA3_256(url_ixbrl);
    			
    			
    		    // Storing data in a list.
    		    
    		    RegistryCNMV infoCNMV = new RegistryCNMV(url_ixbrl, url_info_context, entityName, entityCode, period_end, form, format, hash_code, oam, country);
    		    list.add(infoCNMV);
    		}
            
        } else {
        	int errorCode = getStatusConnectionCode(webURL);        	
        	System.err.println("Critical status code: " + errorCode + " - " + getMessageFromCode(errorCode));
        	System.err.println("The webpage (" + webURL + ") cannot be accessed...");
        	writeExceptionInLog(new Exception(), "The webpage (" + webURL + ") cannot be accessed...", "Scraper.getListOfInfoCNMV()");
        	System.exit(1);
        }
		
		return list;
	}
		
	/**
	 * We need to check the website availability when we connect to it.
	 * This function gets the response status code.
	 * 
	 * <ul>
	 * 		<li>	200 = OK.						</li>
	 * 		<li>	300 = Multiple choices.			</li>
	 * 		<li>	301 = Moved permanently.		</li>
	 * 		<li>	305 = Use proxy.				</li>
	 * 		<li>	400 = Bad request.				</li>
	 * 		<li>	403 = Forbidden.				</li>
	 * 		<li>	404 = Not found.				</li>
	 * 		<li>	500 = Internal server error.	</li>
	 * 		<li>	502 = Bad gateway.				</li>
	 * 		<li>	503 = Service unavailable.		</li>
	 * </ul>
	 * 
	 * @param webURL - Text string with the URL of the web page.
	 * @return Status Code (int).
	 * 
	 * @throws IOException
	 */
	private static int getStatusConnectionCode(final String webURL) {
		
	    Response response = null;
	    
	    try {
	    	
	    	response = Jsoup.connect(webURL).userAgent("Mozilla/5.0").timeout(100000).ignoreHttpErrors(true).execute();
	    	
	    } catch (IOException ex) {
	    	System.err.println("Exception when getting the status code: " + ex.getMessage());
	    	writeExceptionInLog(ex, "IO exception when getting the status code...", "Scraper.getStatusConnectionCode()");
	    }
	    
	    return response.statusCode();
	}
	
	/**
	 * Returns the meaning of the connection code.
	 * 
	 * @param connectionCode (int)
	 * 
	 * @return String. Connection code text.
	 * 
	 * @throws IOException
	 */
	private static String getMessageFromCode(int connectionCode) {
		
		switch (connectionCode) {
		case 300:
			return "Multiple choices";
		case 301:
			return "Moved permanently";
		case 305:
			return "Use proxy";
		case 400:
			return "Bad request";
		case 403:
			return "Forbidden";
		case 404:
			return "Not found";
		case 500:
			return "Internal server error";
		case 502:
			return "Bad gateway";
		case 503:
			return "Service unavailable";
		default:
			return "Unknown error code";
		}
	}
	
	/**
	 * The method returns a DOM tree Document.
	 * It uses the JSoup library on it.
	 * Attention!
	 * The response expiration time is a hundred of seconds.
	 * After that, the connection must be double-checked!
	 * 
	 * @param webURL -Web page URL.
	 * @return Document HTML.
	 * 
	 * @throws IOException
	 */
	private static Document getHtmlDocument(final String webURL) {

	    Document doc = null;
	    
		try {
		    doc = Jsoup.connect(webURL).userAgent("Mozilla/5.0").timeout(100000).get();
		} catch (IOException ex) {
			System.err.println("IO exception when getting the HTML of the page: " + ex.getMessage());
			writeExceptionInLog(ex, "IO exception when getting the HTML of the page...", "Scraper.getHtmlDocument()");
			System.exit(1);
		}
		
	    return doc;
	}
	
}