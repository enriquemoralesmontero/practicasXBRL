package cryptography;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
import java.security.Security;
import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.jcajce.provider.digest.SHA3.DigestSHA3;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

/**
 * Class to generate the hash code.
 * 
 * @author Enrique Morales Monteroç
 * @since 2/4/2019
 * @version 3/4/2019 
 */
public class Algorithm {
	
	final static String tempFolder = "./"; 
	
	/**
	 * Function to generate the hash code.
	 * 
	 * @param url_ixbrl
	 * @return hash_code (String)
	 */
	public static String generateSHA3_256(String url_ixbrl) {
		
		String hash_code = "";
		
		try {
			
			URL url = new URL(url_ixbrl);					// Url.
			
			// Connection.

			URLConnection urlCon = url.openConnection();	// Connection: open.
			
			InputStream is = urlCon.getInputStream();		// Getting InputStream.
			FileOutputStream fos = new FileOutputStream(tempFolder + "/temp.xml"); // Opening the file in the local system.
			
			// R/W in local.

			byte[] array = new byte[10000]; 				// Temporal buffer.

			int leido = is.read(array);

			while (leido > 0) {								// Reading file.
				fos.write(array, 0, leido);
				leido = is.read(array);
			}

			// Closing objects.

			is.close();
			fos.close();

			// Calculate the SHA-3 256 hash code.
			
			File file = new File(tempFolder + "/temp.xml");		// XBRL report file.

			// Getting bytes.

			Path filePath = Paths.get(file.getAbsolutePath());	// Path.
			byte[] fileBytes = Files.readAllBytes(filePath);	// Bytes.

			/*
			MessageDigest md = MessageDigest.getInstance("SHA3-256");	// This code does not work. 
			md.update(fileBytes);										// It requires a later version of Java.
			byte sha3Bytes[] = md.digest();
			hash_code = getHexadecimal(sha3Bytes);
			*/
			
			Security.addProvider(new BouncyCastleProvider());	// BouncyCastle hash generator.
			DigestSHA3 digestSHA3 = new SHA3.Digest256();		// SHA3 256.
			digestSHA3.update(fileBytes);						// Getting the hash code.
			hash_code = Hex.toHexString(digestSHA3.digest());	// To string...

			// Errors controls.
			
			if (hash_code.equals("a7ffc6f8bf1ed76651c14756a061d662f580ff4de43b49fa82d80a4b80f8434a")) {
				System.out.print(" - Attention: Empty XML file! - ");
			}
			
		} catch (MalformedURLException e) {e.printStackTrace();
		} catch (UnknownHostException e) {e.printStackTrace();
		} catch (IOException e) {e.printStackTrace();
		//} catch (NoSuchAlgorithmException e) {e.printStackTrace();
		}

		return hash_code;
	}

	/**
	 * Method used to calculate the hexadecimal of an array of bytes.
	 * It has been replaced by the library method.
	 * It could be reused in the future.
	 * 
	 * @deprecated
	 * @param sha3Bytes (byte[])
	 * @return Hexadecimal (String)
	 */
	@SuppressWarnings("unused")
	private static String getHexadecimal(byte[] sha3Bytes) {
		
		String hex = "";
		
		for (byte b : sha3Bytes) {
			String h = Integer.toHexString(b & 0xFF);
			if (h.length() == 1) {hex += "0";}
			
			hex += h;
		}
		
		return hex.toUpperCase();
	}
}