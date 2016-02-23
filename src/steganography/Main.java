package steganography;

import tammena.malte.*;

public class Main {

	public static void main(String[] args) {
		Archive gZip = new Archive("G:\\Dokumente\\Workspace\\steganography\\src\\test.pdf",
				"G:\\Dokumente\\Workspace\\steganography\\src\\Secret_Message.txt",
				"G:\\Dokumente\\Workspace\\steganography\\src\\Secret_Message.gz",
				"G:\\Dokumente\\Workspace\\steganography\\src\\Secret_Message_decompressed.txt");

		gZip.compress();
		gZip.extractGZIP();
		gZip.decompress();

	}

}
