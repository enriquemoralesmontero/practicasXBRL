package cryptography;

import static log.LogManager.writeExceptionInLog;
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
 * This class offers a set of algorithms to obtain the hash code.
 * 
 * @author	Enrique Morales Montero (design, development, documentation)
 * @author	Javier Mora Gonzálbez (mentor and requirements analyst)
 * @author	Carlos Cano Ladera (mentor, guiding with design, development and documentation)
 * @since	2/4/2019
 * @version 8/4/2019 
 */
public class Algorithm {
	
	final static String tempFolder = "./"; 
	
	/**
	 * The function that generates the hash code (SHA3 256) from the XML.
	 * As a parameter, a URl is provided.
	 * It uses the library of BouncyCastle.
	 * 
	 * @param url_ixbrl
	 * 
	 * @return hash_code (String)
	 * 
	 * @throws MalformedURLException 
	 * @throws UnknownHostException
	 * @throws IOException
	 * 
	 * @see <a href="https://www.bouncycastle.org/latest_releases.html">BouncyCastle Web (Latest releases)</a>
	 * @see <a href="https://www.bouncycastle.org/download/bcprov-jdk15on-161.jar">Library used</a>
	 */
	public static String generateSHA3_256(String url_ixbrl) {
		
		String hash_code = "";
		
		try {
			
			URL url = new URL(url_ixbrl);					// Url.
			
			// Connection.

			URLConnection urlCon = url.openConnection();	// Connection: open.
			
			InputStream is = urlCon.getInputStream();		// Getting InputStream.
			FileOutputStream fos = new FileOutputStream(tempFolder + "/temp.xml"); // Opening the file in the local system.
			
			// Local R/W.

			byte[] array = new byte[10000]; 				// Temporal buffer.

			int leido = is.read(array);

			while (leido > 0) {								// Reading the file.
				fos.write(array, 0, leido);
				leido = is.read(array);
			}

			is.close();
			fos.close();

			// Calculating the SHA-3 256 hash code.
			
			File file = new File(tempFolder + "/temp.xml");		// XBRL report file.

			// Getting bytes.

			Path filePath = Paths.get(file.getAbsolutePath());	// Path.
			byte[] fileBytes = Files.readAllBytes(filePath);	// Bytes.

			/*
			MessageDigest md = MessageDigest.getInstance("SHA3-256");	// This code does not work. 
			md.update(fileBytes);										// Later Java version required.
			byte sha3Bytes[] = md.digest();
			hash_code = getHexadecimal(sha3Bytes);
			*/
			
			Security.addProvider(new BouncyCastleProvider());	// BouncyCastle hash generator.
			DigestSHA3 digestSHA3 = new SHA3.Digest256();		// SHA3 256.
			digestSHA3.update(fileBytes);						// Getting the hash code.
			hash_code = Hex.toHexString(digestSHA3.digest());	// To string...

			// Error control.
			
			if (hash_code.equals("a7ffc6f8bf1ed76651c14756a061d662f580ff4de43b49fa82d80a4b80f8434a")) {
				System.out.print(" - Attention: Empty XML file! - ");
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			writeExceptionInLog(e, e.getMessage(), "Algorithm.generateSHA3_256()");
		} catch (UnknownHostException e) {
			e.printStackTrace();
			writeExceptionInLog(e, e.getMessage(), "Algorithm.generateSHA3_256()");
		} catch (IOException e) {
			e.printStackTrace();
			writeExceptionInLog(e, e.getMessage(), "Algorithm.generateSHA3_256()");
		//} catch (NoSuchAlgorithmException e) {e.printStackTrace();
		}

		return hash_code;
	}

	/**
	 * The method functionality is to calculate an array of bytes hexadecimal.
	 * It is deprecated because we use the library method.
	 * It should be reused in the future.
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