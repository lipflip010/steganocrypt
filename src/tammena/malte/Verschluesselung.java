package tammena.malte;

import com.tozny.crypto.AesCbcWithIntegrity;

public class Verschluesselung {

	public static String encode(String t, String k) {
		if (k.length() == 0 && t.length() == 0) {
			return("Missing values!");
		} else if (k.length() == 0) {
			return("Missing key!");
		} else if (t.length() == 0) {
			return("Missing text!");
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
		}
		return t;
	}

	public static void steganography() {
	}
}
