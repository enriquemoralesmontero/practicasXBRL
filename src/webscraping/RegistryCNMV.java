package webscraping;

/**
 * <p>Class with all the data collected from the website.											</p>
 * <p>It corresponds to a registry of the quarterly report at the CNMV.								</p>
 * 
 * <p>We retrieve the information from two pages:													</p>
 * 
 * <ul>
 * <li>Main table (<a href="http://cnmv.es/Portal/Consultas/IFI/ListaIFI.aspx?XBRL=S">link here</a>)</li>
 * 
 * 		It contains a summary of all the quarterly reports from different companies.
 * 		From each report we select two fields from this summary report. These two fields are:
 *      <ul>
 *      <li>	entityName (the name of the company)												</li>  
 *      <li>	url_info_context (an url that corresponds to the individual complete report)		</li>
 *      </ul>
 * 
 * <li>Complete report (<a href="http://cnmv.es/Portal/AlDia/DetalleIFIAlDia.aspx?nreg=2019036710">example</a>)</li>
 * 
 * 		Each summary report at the main site table has a link to an complete and individual report.
 *      Most of the information we need to store is located in this link.
 *      From this field we take four fields:
 *      <ul>
 *      <li>	url_ixbrl (the XBRL report URL)							</li>  
 *      <li>	entityCode (the code of the company)					</li>
 *      <li>	period_end												</li>
 *      <li>	form (type of XBRL taxonomy that matches the report)	</li>
 *      </ul>
 * </ul>
 * 
 * <p>The fields "format", "oam" and "country" have constant values: "XBRL","CNMV" and "ES".</p>
 * 
 * We use this combine information to fill in each registry we create in an instance of this object (RegistryCNMV).
 * Actually, we insert this object as a row in our database.
 * 
 * @author	Enrique Morales Montero (design, development, documentation)
 * @author	Javier Mora Gonzálbez (mentor and requirements analyst)
 * @author	Carlos Cano Ladera (mentor, guiding with design, development and documentation)
 * @since 	28/3/2019
 * @version 9/4/2019
 */
public class RegistryCNMV {
	
	private String url_ixbrl;
	private String url_info_context;
	private String entity_name;
	private String entity_code;
	private String period_end;
	private String form;
	private String format;
	private String hash_code;
	private String oam;
	private String country;
	
	/**
	 * Constructor with all fields.
	 */
	public RegistryCNMV(String url_ixbrl, String url_info_context, String entity_name, String entity_code,
			String period_end, String form, String format, String hash_code, String oam, String country) {
		
		this.url_ixbrl = url_ixbrl;
		this.url_info_context = url_info_context;
		this.entity_name = entity_name;
		this.entity_code = entity_code;
		this.period_end = period_end;
		this.form = form;
		this.format = format;
		this.hash_code = hash_code;
		this.oam = oam;
		this.country = country;
	}

	// Getters.
	
	public String getUrl_ixbrl() {return url_ixbrl;}
	public String getUrl_info_context() {return url_info_context;}
	public String getEntity_name() {return entity_name;}
	public String getEntity_code() {return entity_code;}
	public String getPeriod_end() {return period_end;}
	public String getForm() {return form;}
	public String getFormat() {return format;}
	public String getHash_code() {return hash_code;}
	public String getOam() {return oam;}
	public String getCountry() {return country;}

	// Setters.
	
	public void setUrl_ixbrl(String url_ixbrl) {this.url_ixbrl = url_ixbrl;}
	public void setUrl_info_context(String url_info_context) {this.url_info_context = url_info_context;}
	public void setEntity_name(String entity_name) {this.entity_name = entity_name;}
	public void setEntity_code(String entity_code) {this.entity_code = entity_code;}
	public void setPeriod_end(String period_end) {this.period_end = period_end;}
	public void setForm(String form) {this.form = form;}
	public void setFormat(String format) {this.format = format;}
	public void setHash_code(String hash_code) {this.hash_code = hash_code;}
	public void setOam(String oam) {this.oam = oam;}
	public void setCountry(String country) {this.country = country;}

	// Other methods.
	
	@Override
	public String toString() {
		
		StringBuilder retorno = new StringBuilder();
		
		if (getUrl_ixbrl() != "")
			retorno.append("Url_ixbrl -------- " + getUrl_ixbrl() + "\n");
		
		if (url_info_context != "")
			retorno.append("url_info_context - " + url_info_context + "\n");
		
		if (entity_name != "")
			retorno.append("entity_name ------ " + entity_name + "\n");
								
		if (entity_code != "")
			retorno.append("entity_code ------ " + entity_code + "\n");
								
		if (period_end != "")
			retorno.append("period_end ------- " + period_end + "\n");
													
		if (form != "")
			retorno.append("form ------------- " + form + "\n");
		
		if (format != "")
			retorno.append("format ----------- " + format + "\n");
		
		if (hash_code != "")
			retorno.append("hash_code -------- " + hash_code + "\n");
		
		if (oam != "")
			retorno.append("oam -------------- " + oam + "\n");
		
		if (country != "")
			retorno.append("country ---------- " + country + "\n");
		
		return retorno.toString();
	}
	
	
}