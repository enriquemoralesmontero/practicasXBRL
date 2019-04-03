package webscraping.collectors;

import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import cryptography.Algorithm;
import webscraping.extracteddata.objects.RegistryCNMV;

/**
 * The class that collects the information from the requested web page.
 * 
 * @author Enrique Morales Montero
 * @since 29/3/2019
 * @version 3/4/2019
 */
public class Scraper {
	
	/**
	 * This is the most important function of the scraper.
	 * It is responsible for the following features:
	 * 
	 * <ol>
	 * <li>	Instance a list.													</li>
	 * <li>	Check that the connection code to the web page is correct (200).	</li>
	 * <li>	Get the HTML document.												</li>
	 * <li>	Treats the rows of the HTML table.									</li>
	 * <li>	Instance a web crawler to enter the hyperlinks.						</li>
	 * <li>	Loop to extract data from the rows and store them in the list.		</li>
	 * </ol>
	 * 
	 * @param webURL - Text string with the URL of the web page.
	 * @param lastUrlInfoContext 
	 * 
	 * @see Scraper#getStatusConnectionCode() getStatusConnectionCode() - Method for checking the connection code.
	 */
	public static ArrayList<RegistryCNMV> getListOfInfoCNMV(final String webURL, final String lastUrlInfoContext) {
		
		// 1 - Instanced list.
		
		ArrayList<RegistryCNMV> list = new ArrayList<RegistryCNMV>();		// List of scraped data.
			
		
		// 2 - Checking the connection code.
		//
		// It checks if the code is 200 when making the request.
		// If there is a bad connection to the website, it will give a code other than 200 (400, 404...)
		
		System.out.println("\n\tThis task may take a while.");
		
		if (getStatusConnectionCode(webURL) == 200) {		

			
			// 3 - Getting the HTML document.
			//
			// The HTML of the web is obtained in a Document object.
			
			Document document = getHtmlDocument(webURL); 	

			
			// 4 - Treats the rows of the HTML table.
			//
			// First essential work of web scraping!
			// The first elements to be treated are the rows of the HTML table of the web page.
			// Searching for data...
            		
            Elements rows = document.select("tr"); 	// Rows obtained.
            System.out.println("\n\tElements: " + rows.size() + " registries.");
            
            
            // 5 - Instantiate the web crawler.
            //
            // The web spider will be used to enter the hypertext links.
            // This will continue the data collection within them.
            
            Crawler spiderBot = new Crawler();
            
            
            // 6 - Loop to extract data from the rows.
            //
            // For each column taken, data is collected with the JSoup's sentences.
            // It is not necessary to control all the rows.
            // The loop will be exited if the registries found have already been previously inserted in the database.
            // The extracted data is stored in an ArrayList.
            
            for (int i = 1; i < rows.size(); i++) {
            	
            	System.out.print("\n\t\t- Scanning... " + i + "/" + rows.size());
    			Element element = rows.get(i);						// Each element contains the row of the HTML table with its HTML information.

    			// Starting of data collection with JSoup...:
    			
    			Element hyperlink = element.select("a").first();	// It contains the url_info_context. The spider will use it later.
    			
    			// JSoup data extraction:
    			//	- url_info_context
    			//	- entityName
    			
    			String url_info_context = hyperlink.attr("href").replace("../..", "http://cnmv.es/Portal");
    			String entityName = element.getElementsByAttributeValue("data-th", "Nombre del emisor").text();
    			
    			// Optimization control:
    			// 		If it matches the last record in the database, it exits the loop.
    			// 		Thus, it is not necessary to control all the records of the CNMV's web.
    			
    			if (url_info_context.equals(lastUrlInfoContext)) {
    				System.out.println(" [This record matches the last one in the database] - Finishing the scraping...");
    				break;
    			}
    			
    			// The spiderbot is asked to take care of this web link.
    			
    			spiderBot.setDocHMTL(getHtmlDocument(url_info_context));
    			
    			// JSoup data extraction (with the spider):
    			//	- url_ixbrl
    			//	- entityCode
    			//	- period_end
    			//	- form
    			//	- format
    			//	- oam
    			//	- country
    			
    			String url_ixbrl = spiderBot.getAttrValue("ctl00_ContentPrincipal_ctl11_hlDescargaInforme", "href").replace("..", "http://cnmv.es/Portal");
    			String entityCode = spiderBot.getPlainText("ctl00_ContentPrincipal_ctl10_lblNIFCont");  			
    			String period_end = spiderBot.getPlainText("ctl00_ContentPrincipal_ctl10_lblFinPeriodoCont");
    			String form = spiderBot.getPlainText("ctl00_ContentPrincipal_ctl11_txtInfoXBRL").replaceAll("El informe ha sido elaborado basándose en la taxonomía ", "").replace(".", "");
    			String format = "XBRL";
    			String oam = "CNMV";
    			String country = "ES";
    			
    			// Some entities have made modifications to the model previously registered with the CNMV.
    			// It must be controlled.
    			// It happens rarely, but it occurs regularly.
    			
    			if (url_ixbrl == "NULL") {
    				System.out.print(" (with modifications)");	// A text is shown to indicate that this record was modified by the entity.
    				url_ixbrl = spiderBot.getAttrValue("ctl00_ContentPrincipal_ctl12_hlDescargaInforme", "href").replace("..", "http://cnmv.es/Portal");
    				entityCode = spiderBot.getPlainText("ctl00_ContentPrincipal_ctl11_lblNIFCont");
    				period_end = spiderBot.getPlainText("ctl00_ContentPrincipal_ctl11_lblFinPeriodoCont");
    				form = spiderBot.getPlainText("ctl00_ContentPrincipal_ctl12_txtInfoXBRL").replaceAll("El informe ha sido elaborado basándose en la taxonomía ", "").replace(".", "");
    			}
    			
    			if (url_ixbrl == "NULL") {
    				System.out.print(" (2)");					// Second modification.
    				url_ixbrl = spiderBot.getAttrValue("ctl00_ContentPrincipal_ctl13_hlDescargaInforme", "href").replace("..", "http://cnmv.es/Portal");
    				entityCode = spiderBot.getPlainText("ctl00_ContentPrincipal_ctl12_lblNIFCont");
    				period_end = spiderBot.getPlainText("ctl00_ContentPrincipal_ctl12_lblFinPeriodoCont");
    				form = spiderBot.getPlainText("ctl00_ContentPrincipal_ctl13_txtInfoXBRL").replaceAll("El informe ha sido elaborado basándose en la taxonomía ", "").replace(".", "");
    			}
    			
    			// JSoup data extraction (with SHA3-256):
    			//	- hash_code
    			
    			String hash_code = Algorithm.generateSHA3_256(url_ixbrl);
    			
    		    // Storing data in a list.
    		    
    		    RegistryCNMV infoCNMV = new RegistryCNMV(url_ixbrl, url_info_context, entityName, entityCode, period_end, form, format, hash_code, oam, country);
    		    list.add(infoCNMV);
    		}
            
        } else {
        	int errorCode = getStatusConnectionCode(webURL);        	
        	System.err.println("Critical status code: " + errorCode + " - " + getMessageFromCode(errorCode));
        	System.exit(1);
        }
		
		return list;
	}
		
	/**
	 * Get the Status code of the response.
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
	 */
	private static int getStatusConnectionCode(final String webURL) {
		
	    Response response = null;
	    
	    try {
	    	
	    	response = Jsoup.connect(webURL).userAgent("Mozilla/5.0").timeout(100000).ignoreHttpErrors(true).execute();
	    	
	    } catch (IOException ex) {
	    	System.err.println("Exception when getting the Status Code: " + ex.getMessage());
	    }
	    
	    return response.statusCode();
	}
	
	/**
	 * Returns the meaning of the connection code.
	 * 
	 * @param connectionCode (int)
	 * @return The meaning of the connection code (String).
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
	 * Method that returns a Document with the HTML content of the web.
	 * It allows the application of JSoup methods on it.
	 * Attention! If in a hundred seconds HTML is not detected, the connection must be checked!
	 * 
	 * @param webURL - Text string with the URL of the web page.
	 * @return Document HTML.
	 */
	private static Document getHtmlDocument(final String webURL) {

	    Document doc = null;
	    
		try {
		    doc = Jsoup.connect(webURL).userAgent("Mozilla/5.0").timeout(100000).get();
		} catch (IOException ex) {
			System.err.println("Exception when getting the HTML of the page: " + ex.getMessage());
			System.exit(1);
		}
		
	    return doc;
	}
	
}

