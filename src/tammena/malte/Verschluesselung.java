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

	public Verschluesselung() {
		setLayout(new BorderLayout());

		ActionHandler ah = new ActionHandler();
		

		input = new JTextArea();
		input.addKeyListener(new KeyHandler());
		output = new JTextArea();
		key = new JTextField();
		philipp = new JButton("Philipp");
		philipp.addActionListener(ah);
		encode = new JButton("Verschlüsseln");
		encode.addActionListener(ah);
		decode = new JButton("Entschlüsseln");
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
			if (input.getText().trim().length() == 0)
				return;

			if ((JButton) e.getSource() == encode)
				output.setText(encode(input.getText()));
			else if ((JButton) e.getSource() == decode)
				output.setText(decode(input.getText()));
			else if ((JButton) e.getSource() == philipp)
				philipp();
		}
	}
	
	private class KeyHandler extends KeyAdapter {
		public void keyReleased(KeyEvent e) {
			super.keyReleased(e);
			output.setText(encode(input.getText().trim()));
		}
	}

	public static void build() {
		JFrame f = new JFrame("RSA - Verschlüsselung");
		f.add(new Verschluesselung());
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setMinimumSize(new Dimension(400, 600));
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}

	public void philipp() {
		KeyPairGenerator kpg = null;
		try {
			kpg = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		}
		KeyPair kp = kpg.generateKeyPair();
		System.out.println(kp.getPublic());
		System.out.println(kp.getPrivate());

	}

	public String encode(String text) {
		if (text.contains("Hallo"))
			text = text.replaceAll("Hallo", "Fick dich");
		if (text.contains("Philipp"))
			text = text.replaceAll("Philipp", "Nutte");
		if (text.contains("Malte"))
			text = text.replaceAll("Malte", "Lord Malte");
		
		return text;
	}

	public String decode(String text) {
		return text;
	}

	public static void main(String[] args) {
		Verschluesselung.build();
	}
}
