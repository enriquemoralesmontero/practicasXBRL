package junit.chaintests;

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
public class AlgorithmXBRLChainTest {
	
	@Test
	public void testGenerateSHA3_256() {
		
		/**
		 * The URL report origin is XBRLchain.
		 * We use http.
		 */
		final String url_ixbrl = "http://www.sec.gov/Archives/edgar/data/1613780/000119312519094497/dbvt-20181231.xml";
		
		final String generatedHash = Algorithm.generateSHA3_256(url_ixbrl);
		final String expectedHash = "4be099d13b312b10c3be3acc73cb0f079626e841993bd282f56568c32f661527";
		
		// Show preview.
		
		System.out.println(generatedHash);
		System.out.println(expectedHash);
		
		// Verification.
		
		assertEquals(expectedHash, generatedHash); // The codes are not the same!
	}
	
}