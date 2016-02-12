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

	private JTextArea _input;
	private JTextArea _output;
	private JTextField _key;
	private JButton _encode;
	private JButton _decode;
	private JButton _steganography;

	private boolean _encode_was_last;

	public VPanel() {
		setLayout(new BorderLayout());

		_encode_was_last = true;

		ActionHandler ah = new ActionHandler();
		KeyHandler kh = new KeyHandler();

		_output = new JTextArea();
		_input.addKeyListener(kh);
		_output = new JTextArea();
		_output.setEditable(false);
		_key = new JTextField();
		_key.addKeyListener(kh);
		_steganography = new JButton(LABEL_BUTTON_STEGANOGRAPHY);
		_steganography.addActionListener(ah);
		_encode = new JButton(LABEL_BUTTON_ENCODE);
		_encode.addActionListener(ah);
		_decode = new JButton(LABEL_BUTTON_DECODE);
		_decode.addActionListener(ah);

		JPanel buttons = new JPanel(new GridLayout(1, 0));
		buttons.add(_decode);
		buttons.add(_key);
		buttons.add(_encode);

		JPanel inputpanel = new JPanel(new BorderLayout());
		inputpanel.add(new JScrollPane(_input), BorderLayout.CENTER);
		inputpanel.add(buttons, BorderLayout.SOUTH);

		JPanel center = new JPanel(new GridLayout(0, 1));
		center.add(inputpanel);

		center.add(new JScrollPane(_output));

		add(center, BorderLayout.CENTER);
		add(_steganography, BorderLayout.SOUTH);
	}

	private class ActionHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if ((JButton) e.getSource() == _encode)
				_output.setText(Verschluesselung.encode(_input.getText(), _key.getText()));
			else if ((JButton) e.getSource() == _decode)
				_output.setText(Verschluesselung.decode(_input.getText(), _key.getText()));
			else if ((JButton) e.getSource() == _steganography)
				Verschluesselung.steganography();
		}
	}

	private class KeyHandler extends KeyAdapter {
		public void keyReleased(KeyEvent e) {
			if (_encode_was_last) {
				_output.setText(Verschluesselung.encode(_input.getText(), _key.getText()));
			} else {
				_output.setText(Verschluesselung.decode(_input.getText(), _key.getText()));
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
