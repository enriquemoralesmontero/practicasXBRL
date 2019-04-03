CREATE TABLE IF NOT EXISTS financialinfo (
	id bigint(20) NOT NULL AUTO_INCREMENT,
	url_ixbrl text NOT NULL,
	url_info_context text NOT NULL,
	entity_name text NOT NULL,
	entity_code text NOT NULL,
	period_end text NOT NULL,
	form text NOT NULL,
	format text NOT NULL,
	hash_code varchar(64) NOT NULL,
	oam text NOT NULL,
	country text NOT NULL,
	PRIMARY KEY (id)
);