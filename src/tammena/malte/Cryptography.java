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
import sun.rmi.runtime.Log;

public class Cryptography {

	private File medium, text, temp, decr;
	private Archive ar;
	private StringBuilder _log;

	public Cryptography() {
		_log = new StringBuilder();
		temp = findTempFile("temp");
		text = findTempFile("text");
		decr = findTempFile("decompressed");

		fileExists(temp, false, false, true);
		fileExists(text, false, false, true);
		fileExists(decr, false, false, true);
	}

	public String getLog() {
		return _log.toString();
	}

	public void chooseMedium() {
		medium = chooseFile();
		if (medium == null){
			_log.append("No File selected\n");
			return;
		}
		_log.append("Selected: "+medium.getName()+"\n");

		ar = new Archive(medium.toString(), text.toString(), temp.toString(), decr.toString());
	}

	public String encode(String t, String k) {
		if (k.length() == 0 && t.length() == 0) {
			_log.append("Missing values!\n");
		} else if (k.length() == 0) {
			_log.append("Missing key!\n");
		} else if (t.length() == 0) {
			_log.append("Missing text!\n");

		} else if (k.length() < 2) {
			_log.append("Key to short!\n");

		} else if (t.length() != 0 && k.length() >= 2) {
			try {
				AesCbcWithIntegrity.SecretKeys keys = AesCbcWithIntegrity.generateKeyFromPassword(k, k.substring(0, 2));
				AesCbcWithIntegrity.CipherTextIvMac cipher = AesCbcWithIntegrity.encrypt(t, keys);
				t = cipher.toString();
			} catch (UnsupportedEncodingException e) {
			} catch (GeneralSecurityException e) {
			}
		}
		return t;
	}

	public String decode(String t, String k) {
		if (k.length() == 0 && t.length() == 0) {
			_log.append("Missing values!\n");
		} else if (k.length() == 0) {
			_log.append("Missing key!\n");
		} else if (t.length() == 0) {
			_log.append("Missing text!\n");
		} else if (k.length() < 2) {
			_log.append("Key to short!\n");
		} else if (t.length() != 0 && k.length() >= 2) {
			try {
				AesCbcWithIntegrity.SecretKeys keys = AesCbcWithIntegrity.generateKeyFromPassword(k, k.substring(0, 2));
				AesCbcWithIntegrity.CipherTextIvMac cipher = new AesCbcWithIntegrity.CipherTextIvMac(t);
				t = AesCbcWithIntegrity.decryptString(cipher, keys);
			} catch (GeneralSecurityException e) {
			} catch (UnsupportedEncodingException e) {
			}
		}
		return t;
	}

	public boolean writeFile(String file, String t) {
		File f = new File(file);
		File pa = new File(f.getParent());

		fileExists(f, true, true, true);
		boolean created_a_new_file = false;
		try {
			BufferedWriter bf = new BufferedWriter(new FileWriter(f));
			bf.write(t.toCharArray());
			bf.flush();
			bf.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public String readFile(String file) {
		File f = new File(file);
		String s = "", line = null;
		if (!f.exists() || !f.canRead())
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

	public void createFile(File f) {
		try {
			f.createNewFile();
		} catch (IOException e) {
			System.err.println("Erstellen der Datei nicht moeglich:");
			e.printStackTrace();
		}
	}

	public boolean fileExists(File f, boolean mkdir, boolean delete, boolean create) {
		if (!f.getParentFile().exists()) {
			if (mkdir) {
				if (create) {
					createFile(f);
				} else {
					return false;
				}
			}
		} else {
			if (!f.exists()) {
				if (create) {
					createFile(f);
				} else {
					return false;
				}
			} else {
				if (delete) {
					if (create) {
						createFile(f);
					} else {
						return true;
					}
				}
			}
		}
		return true;
	}

	private File chooseFile() {
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle("Bitte Bild o.ae. auswaehlen!");
		if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			return jfc.getSelectedFile();
		}
		return null;
	}

	private File findTempFile(String s) {
		String os = System.getProperty("os.name").toLowerCase();
		File f;

		if (os.indexOf("linux") >= 0) {
			// DEBUG System.out.println("LINUX");
			f = new File("/tmp/Cryptography/");
			if (!f.exists())
				f.mkdirs();
			f = new File(f.toPath() + "/" + s);
			if (f.exists())
				if (!f.delete())
					System.err.println("No writing access to temp files");
		} else if (os.indexOf("win") >= 0) {
			// DEBUG System.out.println("WINDOWS");
			f = new File("C:\\Users\\" + System.getProperty("user.name") + "\\AppData\\Local\\Temp\\Cryptography\\");
			if (!f.exists())
				f.mkdirs();
			f = new File(f.toPath() + "\\" + s);
			if (f.exists())
				if (!f.delete())
					System.err.println("No writing access to temp files");
		} else {
			System.err.println("Unbekanntes Betriebssystem!");
			return null;
		}
		return f;
	}

	public void hide(String t) {
		if (medium != null) {
			writeFile(text.toString(), t);
			_log.append(ar.compress()+"\n");
		}
	}

	public String load() {
		if (medium != null) {
			_log.append(ar.extractGZIP()+"\n");
			ar.decompress();
			return readFile(decr.toString());
		}
		return "File not loaded!";
	}
}
