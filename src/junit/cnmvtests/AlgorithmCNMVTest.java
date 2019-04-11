package junit.cnmvtests;

import static org.junit.Assert.*;
import org.junit.Test;

import cryptography.Algorithm;

/**
 * JUnit unit test to check the hashcode generator.
 * 
 * @author	Enrique Morales Montero
 * @author	Javier Mora Gonzálbez (project manager)
 * @author	Carlos Cano Ladera (Designer, code reviewer)
 * @since	4/4/2019
 * @version	8/4/2019
 */
public class AlgorithmCNMVTest {
	
	@Test
	public void testGenerateSHA3_256() {
		
		/**
		 * Telepizza report example extracted from the CNMV web site.
		 * The URL is just in the url_ixbrl constant.
		 */
		final String url_ixbrl = "http://cnmv.es/Portal/Consultas/wuc/DescargaXBRLIPP.ashx?t={13b380b8-dc7a-4c0e-81d3-f80fafd904c8}";
		
		/**
		 * Generated hash code by https://emn178.github.io/online-tools/sha3_256_checksum.html
		 * from the XML (XBRL report).
		 */
		final String expectedHash = "08b8938a7e2f511a8496698818ac1a5533ef8ecee8a5a594ee29b286744140a7";
		
		final String generatedHash = Algorithm.generateSHA3_256(url_ixbrl);
		
		// Show preview.
		
		System.out.println(generatedHash);
		System.out.println(expectedHash);
		
		// Verification.
		
		assertEquals(expectedHash, generatedHash); // OK. generatedHash = correctHash
	}
	
}