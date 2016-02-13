package tammena.malte;

import com.tozny.crypto.AesCbcWithIntegrity;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import javax.swing.JFileChooser;
import steganography.Archive;

public class Cryptography {

	public static String encode(String t, String k) {
		if (k.length() == 0 && t.length() == 0) {
			return("Missing values!");
		} else if (k.length() == 0) {
			return("Missing key!");
		} else if (t.length() == 0) {
			return("Missing text!");
		} else if (k.length() < 2) {
			return("Key to short!");
		}
		try {
			AesCbcWithIntegrity.SecretKeys keys = AesCbcWithIntegrity.generateKeyFromPassword(k, k.substring(0, 2));
			AesCbcWithIntegrity.CipherTextIvMac cipher = AesCbcWithIntegrity.encrypt(t, keys);
			t = cipher.toString();
		} catch (UnsupportedEncodingException e) {
		} catch (GeneralSecurityException e) {
		}
		return t;
	}

	public static String decode(String t, String k) {
		if (k.length() == 0 && t.length() == 0) {
			return("Missing values!");
		} else if (k.length() == 0) {
			return("Missing key!");
		} else if (t.length() == 0) {
			return("Missing text!");
		} else if (k.length() < 2) {
			return("Key to short!");
		}
		try {
			AesCbcWithIntegrity.SecretKeys keys = AesCbcWithIntegrity.generateKeyFromPassword(k, k.substring(0, 2));
			AesCbcWithIntegrity.CipherTextIvMac cipher = new AesCbcWithIntegrity.CipherTextIvMac(t);
			t = AesCbcWithIntegrity.decryptString(cipher, keys);
		} catch (GeneralSecurityException e) {
		} catch (UnsupportedEncodingException e) {
		}
		return t;
	}

	public static void steganography(EPanel ep) {
		// Archive a = new Archive();
		String os = System.getProperty("os.name").toLowerCase();
		JFileChooser jfc = new JFileChooser();
		String output;
		String tempdir;
		String input;
		File f;

		if (os.indexOf("linux") >= 0) {
			System.out.println("LINUX");
			f = new File("/tmp/Cryptography/");
			if (! f.exists())
				f.mkdirs();
		} else if (os.indexOf("win") >= 0) {
			System.out.println("WINDOWS");
			f = new File("C:\\Users\\" + System.getProperty("user.name") + "\\AppData\\Local\\Temp\\Cryptography\\");
			if (! f.exists())
				f.mkdirs();
		}
		
		if (jfc.showOpenDialog(ep) == JFileChooser.APPROVE_OPTION) {
			output = jfc.getSelectedFile().toString();
		} else {
			return;
		}
		System.out.println(output);
	}
}
