package webscraping.extracteddata.objects;

/**
 * Class with all the data collected from the website.
 * It corresponds to a registry of the financial information table of the CNMV.
 * 
 * @author Enrique Morales Montero
 * @since 28/3/2019
 * @version 1/4/2019
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