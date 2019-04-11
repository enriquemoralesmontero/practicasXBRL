package webscraping;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * A web crawler (also called a web-spider or spiderbot).
 * This is a program that accedes to any quarterly report individual link (page) and extracts all the necessary data for each CNMV record we need to store and secure in our database.
 * 
 * @author	Enrique Morales Montero (design, development, documentation)
 * @author	Javier Mora Gonzálbez (mentor and requirements analyst)
 * @author	Carlos Cano Ladera (mentor, guiding with design, development and documentation)
 * @since 	28/3/2019
 * @version 9/4/2019
 */
public class Crawler {
	
	// Fields.
	
	/**
	 * The JSoup object references a company completed report.
	 * The web spider will extract data from this web page.
	 */
	private Document docHTML;
	
	// Constructor.
	
	/**
	 * Class constructor.
	 * It is only used to instantiate the object.
	 */
	public Crawler() {}
	
	// Setters and getters.
	
	/**
	 * The procedure fixes the targeted HTML document.
	 * The web spider uses the HTML DOM tree to gather the financial data.
	 * We use the JSoup library to go through the HTML DOM Tree.
	 * 
	 * @param doc - JSoup object with the web page HTML code.
	 */
	public void setDocHMTL(final Document doc) {this.docHTML = doc;}
	
	/**
	 * <p>	This function collects the information we pass as a parameter from the document.		</p>
	 * <p>	Consequently, it extracts the plain text that is placed between the tags whose attribute "id" is "ID".</p>
	 * <p>	Example:																				</p>
	 * <p>		HTML 									= <tag id="Parameter">31/01/2019</tag>		</p>
	 * <p>		Results of getPlainText("Parameter") 	= "31/01/2019"								</p>
	 * 
	 * @param ID -  targeted tag identifier attribute (String).
	 * @return The text between the targeted tags (String).
	 * @see Crawler#docHTML			docHTML			- HTML document.
	 * @see Crawler#getAttrValue()	getAttrValue	- Analogous method.
	 */
	public String getPlainText(final String ID) {
						
		Element element = docHTML.getElementById(ID);						// Getting the tag with id="ID"...
		
		if (element == null) {return "NULL";}
		
		String value = element.getElementsByAttributeValue("id", ID).text();// Getting the plain text between the searched tags.
		return value;
	}
	
	/**
	 * <p>	This function collects information from the document.												</p>
	 * <p>	This method is similar to getPlainText(), but it has its own features:								</p>
	 * <p>	It extracts the value from the attribute "ATTRIBUTE" located in the HTML tag with id equals to "ID".</p>
	 * <p>	Example:																							</p>
	 * <p>		HTML 											= <tag id="Parameter" href="page.com"></tag>	</p>
	 * <p>		Results of getAttrValue("Parameter", "href") 	= "page.com"									</p>
	 * 
	 * @param ID - Tag identifier (String).
	 * @param ATTRIBUTE - This attribute contains the value we are looking for.
	 * @return String. The attribute "ATTRIBUTE" value.
	 * @see Crawler#docHTML			docHTML			- HTML document.
	 * @see Crawler#getPlainText()	getPlainText	- Analogous method.
	 */
	public String getAttrValue(final String ID, final String ATTRIBUTE) {
					
		// Get the tag with id="ID"...
		Element element = docHTML.getElementById(ID);	
		
		if (element == null) {return "NULL";}
		
		// Get the attribute value passed as a parameter("ATTRIBUTE"). (ATTRIBUTE="value")...
		String value = element.attr(ATTRIBUTE);			
		return value;
	}
}