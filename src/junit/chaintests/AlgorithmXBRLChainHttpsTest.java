package junit.chaintests;

import static org.junit.Assert.*;
import org.junit.Test;

import cryptography.Algorithm;

public class AlgorithmXBRLChainHttpsTest {
	
	@Test
	public void testGenerateSHA3_256() {
		
		/**
		 * From XBRLchain with https.
		 */
		final String url_ixbrl = "https://www.sec.gov/Archives/edgar/data/1613780/000119312519094497/dbvt-20181231.xml";
		
		final String generatedHash = Algorithm.generateSHA3_256(url_ixbrl);
		final String expectedHash = "4be099d13b312b10c3be3acc73cb0f079626e841993bd282f56568c32f661527";
		
		// Show preview.
		
		System.out.println(generatedHash);
		System.out.println(expectedHash);
		
		// Verification.
		
		assertEquals(expectedHash, generatedHash); // OK. generatedHash = correctHash
	}
	
}