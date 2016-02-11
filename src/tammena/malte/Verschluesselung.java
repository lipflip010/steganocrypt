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

public class Verschluesselung extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTextArea input;
	private JTextArea output;
	private JTextField key;
	private JButton encode;
	private JButton decode;
	private JButton philipp;

	private boolean encode_was_last;

	public Verschluesselung() {
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
		philipp = new JButton("Philipp");
		philipp.addActionListener(ah);
		encode = new JButton("Verschluesseln");
		encode.addActionListener(ah);
		decode = new JButton("Entschluesseln");
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
		add(philipp, BorderLayout.SOUTH);
	}

	private class ActionHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (input.getText().length() == 0)
				return;

			if ((JButton) e.getSource() == encode)
				output.setText(encode(input.getText(), key.getText()));
			else if ((JButton) e.getSource() == decode)
				output.setText(decode(input.getText(), key.getText()));
			else if ((JButton) e.getSource() == philipp)
				philipp();
		}
	}

	private class KeyHandler extends KeyAdapter {
		public void keyReleased(KeyEvent e) {
			super.keyReleased(e);
			if (encode_was_last) {
				output.setText(encode(input.getText(), key.getText()));
			} else {
				output.setText(decode(input.getText(), key.getText()));
			}
		}
	}

	public static void build() {
		JFrame f = new JFrame("Verschluesselung");
		f.add(new Verschluesselung());
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setMinimumSize(new Dimension(600, 700));
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}

	public void philipp() {
	}

	public String encode(String t, String k) {
		if (k.length() == 0 && t.length() == 0) {
			return("Missing values!");
		} else if (k.length() == 0) {
			return("Missing key!");
		} else if (t.length() == 0) {
			return("Missing text!");
		}
		return t;
	}

	public String decode(String t, String k) {
		if (k.length() == 0 && t.length() == 0) {
			return("Missing values!");
		} else if (k.length() == 0) {
			return("Missing key!");
		} else if (t.length() == 0) {
			return("Missing text!");
		}
		return t;
	}

	public static void main(String[] args) {
		Verschluesselung.build();
	}
}
