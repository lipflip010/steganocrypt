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

	private String _medium; // Datei, in der Daten versteckt werden sollen.
							// Getestete Dateitypen: .jpg, .ai, .png, .pdf
	private String _message; // Daten, die uebertragen werden sollen
	private String _temp; // Temporaere Datei, in der der GZIP Abschnitt
							// zwischengepeichert wird
	private String _message_decompressed; // Datei, in der das entpackte
											// Ergebnis gespeichert wird
	private boolean _carrier_valid;
	public Archive(String medium, String message, String temp, String message_decompressed) {
		_medium = medium;
		_message = message;
		_temp = temp;
		_message_decompressed = message_decompressed;
		_carrier_valid = this.isCarrierValid();
	}

	/*
	 * Diese Methode verpackt eine Datei nach dem GZIP Format und haengt das
	 * Ergebnis an die Quelldatei.
	 */
	public boolean isCarrierValid() {
		byte[] buffer = new byte[1024];
		boolean gzip_magic = false;
		String[] lastBytes = { "", "", "", "" };
		int len;
		int gzip_position = 0;

		try {

			InputStream in = new BufferedInputStream(new FileInputStream(_medium));

			ByteArrayOutputStream bout = new ByteArrayOutputStream();

			while ((len = in.read(buffer)) > 0) {
				bout.write(buffer, 0, len);
			}
			byte[] file = bout.toByteArray();

			for (int i = (int) Math.round(file.length * 0.9); i < file.length; i++) {
				lastBytes[0] = String.format("%02x", file[i]);
				if (lastBytes[3].equals("1f") && lastBytes[2].equals("8b") && lastBytes[1].equals("08")
						&& lastBytes[0].equals("00")) {
					gzip_magic = true;
					gzip_position = i - 3;
				}
				lastBytes[3] = lastBytes[2];
				lastBytes[2] = lastBytes[1];
				lastBytes[1] = lastBytes[0];
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		String log = (gzip_magic) ? "GZIP-Header found" : "No GZIP found";
		//DEBUG System.out.println(log);
		return !gzip_magic;
	}

	public void compress() {

		if (_carrier_valid) {
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

				//DEBUG System.out.println("Compressed " + _message + "and appended it to: " + _medium);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/*
	 * Diese Methode sucht in der angegebenen Datei nach einem GZIP Abschnitt
	 * und schreibt diesen in eine temporaere .gz Datei. Zudem wird der GZIP
	 * Abschnitt von der Quelldatei entfernt.
	 */
	public void extractGZIP() {
		byte[] buffer = new byte[1024];
		boolean gzip_magic = false;
		String[] lastBytes = { "", "", "", "" };
		int len;
		int gzip_position = 0;

		try {

			InputStream in = new BufferedInputStream(new FileInputStream(_medium));
			FileOutputStream out = new FileOutputStream(_temp);
			ByteArrayOutputStream bout = new ByteArrayOutputStream();

			while ((len = in.read(buffer)) > 0) {
				bout.write(buffer, 0, len);
			}
			byte[] file = bout.toByteArray();

			for (int i = (int) Math.round(file.length * 0.9); i < file.length; i++) {
				lastBytes[0] = String.format("%02x", file[i]);
				if (lastBytes[3].equals("1f") && lastBytes[2].equals("8b") && lastBytes[1].equals("08")
						&& lastBytes[0].equals("00")) {
					out.write(file[i - 3]);
					out.write(file[i - 2]);
					out.write(file[i - 1]);
					gzip_magic = true;
					gzip_position = i - 3;
				}

				if (gzip_magic) {
					out.write(file[i]);
				}
				lastBytes[3] = lastBytes[2];
				lastBytes[2] = lastBytes[1];
				lastBytes[1] = lastBytes[0];
			}

			int last_complete_byte_array = (gzip_position - gzip_position % buffer.length) / buffer.length;
			int second_loop_start = gzip_position - gzip_position % buffer.length;
			// //DEBUG System.out.println(last_complete_byte_array);

			if (gzip_magic) {
				FileOutputStream in_restored = new FileOutputStream(_medium);//
				ByteArrayInputStream bin = new ByteArrayInputStream(file);
				for (int i = 0; (len = bin.read(buffer)) > 0 && i < last_complete_byte_array; i++) {
					in_restored.write(buffer, 0, len);
				}
				// FileOutputStream in_restored = new FileOutputStream(_medium);
				for (int i = second_loop_start; i < gzip_position; i++) {
					in_restored.write(file[i]);
				}
			}
			String log = (gzip_magic) ? "GZIP found and extracted to: " + _temp : "No GZIP found";
			//DEBUG System.out.println(log);

			in.close();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Diese Methode entpackt die temporaere .gz Datei nach MESSAGE_DECOMPRESSED
	 * und l�scht die temporaere .gz Datei.
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
			//DEBUG System.out.println("Decompressed to: " + _message_decompressed);

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
	 * Diese Funktion diente zum Testen einer Suche in einem Bytearray nach
	 * einer bestimmten byte Folge
	 */
	public void bytetest() {
		byte[] test = { -1, 31, -117, 64, 1, -26 };
		String ashex_old = "";
		for (int i = 0; i < test.length; i++) {
			//DEBUG System.out.println(String.format("%02x", test[i]));
			String ashex = String.format("%02x", test[i]);
			if (ashex_old.equals("1f") && ashex.equals("8b")) {
				//DEBUG System.out.println("Hans");
			}
			ashex_old = ashex;
		}
	}
}
