package junit.cnmvtests;

import static org.junit.Assert.*;
import org.junit.Test;

import cryptography.Algorithm;

public class AlgorithmCNMVTest {
	
	@Test
	public void testGenerateSHA3_256() {
		
		/**
		 * From CNMV web (Telepizza report).
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