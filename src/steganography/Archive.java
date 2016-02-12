package steganography;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
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

	private String _medium;
	private String _message;
	private String _temp;
	private	String _message_decompressed;
	
	
	public Archive(String medium,String message, String temp, String message_decompressed){
		_medium = medium;
		_message = message;
		_temp = temp;
		_message_decompressed = message_decompressed;
	}
	/*
	 * 	Diese Methode verpackt eine Datei nach dem GZIP Format
	 * 	und h�ngt das Ergebnis an die Quelldatei.
	 */
	public void compress() {

		byte[] buffer = new byte[1024];

		try {

			GZIPOutputStream gzos = new GZIPOutputStream(new FileOutputStream(_medium, true));

			FileInputStream in = new FileInputStream(_message);

			int len;

			while ((len = in.read(buffer)) > 0) {
				gzos.write(buffer, 0, len);
			}

			in.close();

			gzos.finish();
			gzos.close();

			System.out.println("Compressed "+_message+"and appended it to: "+_medium);

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	/*	
	 * 	Diese Methode sucht in der angegebenen Datei nach einem GZIP Abschnitt
	 * 	und schreibt diesen in eine tempor�re .gz Datei. Zudem wird der  GZIP
	 * 	Abschnitt von der Quelldatei entfernt.
	 */

	public void extractGZIP() {
		byte[] buffer = new byte[1024];
		boolean gzip_magic = false;
		String[] lastBytes = {"","","",""};
		int len;
		int gzip_position=0;
	
		try {

			InputStream in = new BufferedInputStream(new FileInputStream(_medium));
			FileOutputStream out = new FileOutputStream(_temp);
			
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
			int last_complete_byte_array = (gzip_position-gzip_position%buffer.length)/buffer.length;
			System.out.println(last_complete_byte_array);

			if(gzip_magic){
//				FileOutputStream in_restored = new FileOutputStream(_medium);
//				ByteArrayInputStream bin = new ByteArrayInputStream(file);
//				for (int i = 0;(len = bin.read(buffer))>0 ; i++) {
//					
//					in_restored.write(buffer, 0, len);
//				}
				FileOutputStream in_restored = new FileOutputStream(_medium);
				for (int i = 0; i < gzip_position; i++) {
					in_restored.write(file[i]);
				}
			}
			String log = (gzip_magic) ? "GZIP found and extracted to: " + _temp : "No GZIP found";
			System.out.println(log);
			
			in.close();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	/*	Diese Methode entpackt die tempor�re .gz Datei nach
	 * 	MESSAGE_DECOMPRESSED und l�scht die tempor�re .gz
	 * 	Datei. 
	 */


	public void decompress() {
		byte[] buffer = new byte[1024];
		int len;
		try {
			GZIPInputStream gzis = new GZIPInputStream(new FileInputStream(_temp));
			FileOutputStream out = new FileOutputStream(_message_decompressed);

			while ((len = gzis.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}
			System.out.println("Decompressed to: " + _message_decompressed);

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
			Files.deleteIfExists(FileSystems.getDefault().getPath(_temp));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/*
	 * 	Diese Funktion diente zum Testen einer Suche
	 * 	in einem Bytearray nach einer bestimmten by Folge
	 */
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


}
// if (byte_old.equals("1f") && byte_current.equals("8b")) {
