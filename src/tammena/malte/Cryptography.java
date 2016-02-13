package tammena.malte;

import com.tozny.crypto.AesCbcWithIntegrity;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
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

	public static boolean writeFile(String file, String t) {
		File f = new File(file);
		File pa = new File(f.getParent());

		if (! pa.exists() && ! pa.mkdirs()) {
			System.out.println("Parent missing");
			return false;
		}
		if (f.exists()) {
			f.delete();
		}
		boolean created_a_new_file = false;
		try {
			created_a_new_file = f.createNewFile();
		} catch (IOException e) {}
		if (! created_a_new_file && f.canWrite()) {
			return false;
		} else {
			try {
				BufferedWriter bf = new BufferedWriter(new FileWriter(f));
				bf.write(t.toCharArray());
				bf.flush();
				bf.close();
			} catch (IOException e) {
				return false;
			}
		}
		return true;
	}

	public static String readFile(String file) {
		File f = new File(file);
		String s = "", line = null;
		if (! f.exists() || ! f.canRead())
			return null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			do {
				line = br.readLine();
				if (line == null)
					break;
				s += line;
			} while (true);
		} catch (IOException e) {
			return null;
		}
		return s;
	}

	public static void steganography(EPanel ep) {
		// Archive a = new Archive();
		String os = System.getProperty("os.name").toLowerCase();
		JFileChooser jfc = new JFileChooser();
		String stegano_file;
		String tempdir;
		String carrier;
		File f = null;

		if (os.indexOf("linux") >= 0) {
			System.out.println("LINUX");
			f = new File(tempdir = "/tmp/Cryptography/");
			if (! f.exists())
				f.mkdirs();
			f = new File(f.toPath() + "/encr_message");
			if (f.exists())
				if (! f.delete())
					System.err.println("No writing access to temp files");
		} else if (os.indexOf("win") >= 0) {
			System.out.println("WINDOWS");
			f = new File(tempdir = "C:\\Users\\" + System.getProperty("user.name") + "\\AppData\\Local\\Temp\\Cryptography\\");
			if (! f.exists())
				f.mkdirs();
			f = new File(f.toPath() + "\\encr_message");
			if (f.exists())
				if (! f.delete())
					System.err.println("No writing access to temp files");
		} else {
			return;
		}

		writeFile(f.toString(), "Hallo Malte, alles gut???");
		System.out.println(readFile(f.toString()));

		if (jfc.showOpenDialog(ep) == JFileChooser.APPROVE_OPTION) {
			stegano_file = jfc.getSelectedFile().toString();
		} else {
			return;
		}
		System.out.println(stegano_file);
	}
}
