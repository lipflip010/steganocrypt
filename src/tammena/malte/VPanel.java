package tammena.malte;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import tammena.malte.Verschluesselung;

public class VPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final String LABEL_JFRAME_TITLE = "Verschluesselung";
	private static final String LABEL_BUTTON_ENCODE = "Encode";
	private static final String LABEL_BUTTON_DECODE = "Decode";
	private static final String LABEL_BUTTON_STEGANOGRAPHY = "Steganography";

	private JTextArea input;
	private JTextArea output;
	private JTextField key;
	private JButton encode;
	private JButton decode;
	private JButton steganography;

	private boolean encode_was_last;

	public VPanel() {
		setLayout(new BorderLayout());

		encode_was_last = true;

		ActionHandler ah = new ActionHandler();
		KeyHandler kh = new KeyHandler();

		input = new JTextArea();
		input.addKeyListener(kh);
		output = new JTextArea();
		output.setEditable(false);
		key = new JTextField();
		key.addKeyListener(kh);
		steganography = new JButton(LABEL_BUTTON_STEGANOGRAPHY);
		steganography.addActionListener(ah);
		encode = new JButton(LABEL_BUTTON_ENCODE);
		encode.addActionListener(ah);
		decode = new JButton(LABEL_BUTTON_DECODE);
		decode.addActionListener(ah);

		JPanel buttons = new JPanel(new GridLayout(1, 0));
		buttons.add(decode);
		buttons.add(key);
		buttons.add(encode);

		JPanel inputpanel = new JPanel(new BorderLayout());
		inputpanel.add(new JScrollPane(input), BorderLayout.CENTER);
		inputpanel.add(buttons, BorderLayout.SOUTH);

		JPanel center = new JPanel(new GridLayout(0, 1));
		center.add(inputpanel);

		center.add(new JScrollPane(output));

		add(center, BorderLayout.CENTER);
		add(steganography, BorderLayout.SOUTH);
	}

	private class ActionHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if ((JButton) e.getSource() == encode)
				output.setText(Verschluesselung.encode(input.getText(), key.getText()));
			else if ((JButton) e.getSource() == decode)
				output.setText(Verschluesselung.decode(input.getText(), key.getText()));
			else if ((JButton) e.getSource() == steganography)
				Verschluesselung.steganography();
		}
	}

	private class KeyHandler extends KeyAdapter {
		public void keyReleased(KeyEvent e) {
			if (encode_was_last) {
				output.setText(Verschluesselung.encode(input.getText(), key.getText()));
			} else {
				output.setText(Verschluesselung.decode(input.getText(), key.getText()));
			}
		}
	}

	public static void build() {
		JFrame f = new JFrame(LABEL_JFRAME_TITLE);
		f.add(new VPanel());
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setMinimumSize(new Dimension(600, 700));
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}

	public static void main(String[] args) {
		VPanel.build();
	}
}
