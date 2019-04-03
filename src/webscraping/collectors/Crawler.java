package webscraping.collectors;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * A web crawler, sometimes called a web-spider or spiderbot.
 * The spider enters the hypertext links and continues to extract data from there.
 * 
 * @author Enrique Morales Montero
 * @since 28/3/2019
 * @version 3/4/2019
 */
public class Crawler {
	
	// Fields.
	
	/**
	 * JSoup object with the HTML of the web page.
	 * The web spider will extract data from this web page.
	 */
	private Document docHTML;
	
	// Constructor.
	
	/**
	 * Simple constructor.
	 * It is only used to instantiate the object.
	 */
	public Crawler() {}
	
	// Setters and getters.
	
	/**
	 * Procedure for establishing the HTML document.
	 * The web spider will extract data from this web page.
	 * 
	 * @param doc - JSoup object with the HTML of the web page.
	 */
	public void setDocHMTL(final Document doc) {this.docHTML = doc;}
	
	/*
	 * <p>	Function collects information from the document.										</p>
	 * <p>	It extracts the plain text that is placed between the tags whose attribute "id" is "ID".</p>
	 * <p>	Example:																				</p>
	 * <p>		HTML 									= <tag id="Parameter">31/01/2019</tag>		</p>
	 * <p>		Results of getPlainText("Parameter") 	= "31/01/2019"								</p>
	 * 
	 * @param ID - Identifier of the searched tag (String).
	 * @return The text that is between the searched tags (String).
	 */
	public String getPlainText(final String ID) {
						
		Element element = docHTML.getElementById(ID);						// Getting the tag with id="ID"...
		
		if (element == null) {return "NULL";}
		
		String value = element.getElementsByAttributeValue("id", ID).text();// Getting the plain text between the searched tags.
		return value;
	}
	
	/**
	 * <p>	Function collects information from the document.													</p>
	 * <p>	It is analogous to getPlainText(), but with a particularity:										</p>
	 * <p>	It extracts the value of the attribute "ATTRIBUTE" located on the label with id="ID".				</p>
	 * <p>	Example:																							</p>
	 * <p>		HTML 											= <tag id="Parameter" href="page.com"></tag>	</p>
	 * <p>		Results of getAttrValue("Parameter", "href") 	= "page.com"									</p>
	 * 
	 * @param ID - Identifier of the searched tag (String).
	 * @param ATTRIBUTE - Element from which the value will be collected (String).
	 * @return The value of the attribute called "ATTRIBUTE" (String).
	 */
	public String getAttrValue(final String ID, final String ATTRIBUTE) {
					
		Element element = docHTML.getElementById(ID);	// Getting the tag with id="ID"...
		
		if (element == null) {return "NULL";}
		
		String value = element.attr(ATTRIBUTE);			// Getting the value of the attribute called "ATTRIBUTE" (ATTRIBUTE="value")...
		return value;
	}
}