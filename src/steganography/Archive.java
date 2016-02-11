package steganography;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;



import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;



public class Archive {

	private static final String SOURCE = "G:\\Dokumente\\Workspace\\stenography\\src\\ananas.ai";
	private static final String MESSAGE = "G:\\Dokumente\\Workspace\\stenography\\src\\Secret_Message.txt";
	private static final String GZIP_FILE = "G:\\Dokumente\\Workspace\\stenography\\src\\Secret_Message.gz";
	private static final String MESSAGE_DECOMPRESSED = "G:\\Dokumente\\Workspace\\stenography\\src\\Secret_Message_decompressed.txt";
	final protected static char[] hexArray = "0123456789abcdef".toCharArray();

	public void compress() {

		byte[] buffer = new byte[1024];

		try {

			GZIPOutputStream gzos = new GZIPOutputStream(new FileOutputStream(SOURCE, true));

			FileInputStream in = new FileInputStream(MESSAGE);

			int len;

			while ((len = in.read(buffer)) > 0) {
				gzos.write(buffer, 0, len);
			}

			in.close();

			gzos.finish();
			gzos.close();

			System.out.println("Compressed "+MESSAGE+"and appended it to: "+SOURCE);

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void extractGZIP() {
		byte[] buffer = new byte[1024];
		boolean gzip_magic = false;
		String[] lastBytes = {"","","",""};
		int len;
		int gzip_position=0;
	
		try {

			InputStream in = new BufferedInputStream(new FileInputStream(SOURCE));
			FileOutputStream out = new FileOutputStream(GZIP_FILE);
			
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			
			while ((len = in.read(buffer)) > 0) {
				bout.write(buffer, 0, len);
			}
		
			byte[] file = bout.toByteArray();
			
			
			for (int i = (int)Math.round(file.length*0.9); i < file.length; i++) {
				lastBytes[0] = String.format("%02x", file[i]);
				if (lastBytes[3].equals("1f") && lastBytes[2].equals("8b")&& lastBytes[1].equals("08")&& lastBytes[0].equals("00")) {
					out.write(file[i - 3]);
					out.write(file[i - 2]);
					out.write(file[i - 1]);
					gzip_magic = true;
					gzip_position = i -3;
				}
				
				if (gzip_magic) {
					out.write(file[i]);
				}
				// if (i%2 == 0) {
				// System.out.print(byte_current);
				// }
				// else
				// {
				// System.out.print(byte_current+" ");
				// }
				lastBytes[3] = lastBytes[2];
				lastBytes[2] = lastBytes[1];
				lastBytes[1] =lastBytes[0];
			
				
			}

			if(gzip_magic){
				FileOutputStream in_restored = new FileOutputStream(SOURCE);
				for (int i = 0; i < gzip_position; i++) {
					in_restored.write(file[i]);
				}
			}
			String log = (gzip_magic) ? "GZIP found and extracted to: " + GZIP_FILE : "No GZIP found";
			System.out.println(log);
			
			in.close();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void extractGZIP_old() {
		byte[] buffer = new byte[1024];
		boolean gzip_magic = false;
		String byte_old = "";
		String byte_current;
		
		try {

			FileInputStream in = new FileInputStream(SOURCE);
			FileOutputStream out = new FileOutputStream(GZIP_FILE);
			// GZIPInputStream gzis = new GZIPInputStream(new
			// FileInputStream(GZIP_FILE));
			int len;
		

			while ((len = in.read(buffer)) > 0) {

				for (int i = 0; i < len; i++) {
					byte_current = String.format("%02x", buffer[i]);
					if (byte_old.equals("1f") && byte_current.equals("8b")) {
						out.write(buffer[i - 1]);
						gzip_magic = true;
					}
					if (gzip_magic) {
						out.write(buffer[i]);
					}
					// if (i%2 == 0) {
					// System.out.print(byte_current);
					// }
					// else
					// {
					// System.out.print(byte_current+" ");
					// }
					byte_old = byte_current;
				}
			}

			String log = (gzip_magic) ? "GZIP found and extracted to: " + GZIP_FILE : "No GZIP found";
			System.out.println(log);

			in.close();
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void decompress() {
		byte[] buffer = new byte[1024];
		int len;
		try {
			GZIPInputStream gzis = new GZIPInputStream(new FileInputStream(GZIP_FILE));
			FileOutputStream out = new FileOutputStream(MESSAGE_DECOMPRESSED);

			while ((len = gzis.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}
			System.out.println("Decompressed to: " + MESSAGE_DECOMPRESSED);

			gzis.close();
			out.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Files.deleteIfExists(FileSystems.getDefault().getPath(GZIP_FILE));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void bytetest() {
		byte[] test = { -1, 31, -117, 64, 1, -26 };
		String ashex_old = "";
		for (int i = 0; i < test.length; i++) {
			System.out.println(String.format("%02x", test[i]));
			String ashex = String.format("%02x", test[i]);
			if (ashex_old.equals("1f") && ashex.equals("8b")) {
				System.out.println("Hans");
			}
			ashex_old = ashex;
		}

	}

	public static String bytesToHex(byte bytes) {
		char[] hexChars = new char[2];
		for (int j = 0; j < 1; j++) {
			int v = bytes & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}
}

// if (byte_old.equals("1f") && byte_current.equals("8b")) {
