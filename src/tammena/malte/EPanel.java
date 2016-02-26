package tammena.malte;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class EPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final String LABEL_JFRAME_TITLE = "Verschluesselung";
	private static final String LABEL_BUTTON_ENCODE = "Encode";
	private static final String LABEL_BUTTON_DECODE = "Decode";
	private static final String LABEL_BUTTON_CHOOSE = "Choose File";
	private static final String LABEL_BUTTON_LOAD ="Load";
	private static final String LABEL_BUTTON_HIDE ="Hide";


	private JTextArea _crypto;
	private JTextArea _log_output;
	private JTextField _key;
	private JButton _encode;
	private JButton _decode;
	private JButton _load;
	private JButton _hide;
	private JButton _choose;


	private Cryptography cry;

	public EPanel() {
		setLayout(new BorderLayout());

		cry = new Cryptography();

		ActionHandler ah = new ActionHandler();

		_crypto = new JTextArea();
		_crypto.setLineWrap(true);
	
		_log_output = new JTextArea();
		_log_output.setEditable(false);
		_log_output.setLineWrap(true);
		_key = new JTextField();
		
		_hide = new JButton(LABEL_BUTTON_HIDE);
		_load = new JButton(LABEL_BUTTON_LOAD);
		_choose = new JButton(LABEL_BUTTON_CHOOSE);
		_hide.addActionListener(ah);
		_load.addActionListener(ah);
		_choose.addActionListener(ah);
		_encode = new JButton(LABEL_BUTTON_ENCODE);
		_encode.addActionListener(ah);
		_decode = new JButton(LABEL_BUTTON_DECODE);
		_decode.addActionListener(ah);

		JPanel buttons = new JPanel(new GridLayout(1, 3));
		buttons.add(_decode);
		buttons.add(_key);
		buttons.add(_encode);//test

		JPanel input_panel = new JPanel(new BorderLayout());
		input_panel.add(new JScrollPane(_crypto), BorderLayout.CENTER);
		input_panel.add(buttons, BorderLayout.SOUTH);

		JPanel center = new JPanel(new GridLayout(0, 1));
		center.add(input_panel);
		center.add(new JScrollPane(_log_output));

		JPanel steganography = new JPanel(new GridLayout(1, 3));
		steganography.add(_load);
		steganography.add(_hide);
		steganography.add(_choose);

		add(center, BorderLayout.CENTER);
		add(steganography, BorderLayout.SOUTH);

	}

	private class ActionHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if ((JButton) e.getSource() == _encode) {
				_crypto.setText(cry.encode(_crypto.getText(), _key.getText()));
				_log_output.setText(cry.getLog());
				
			} else if ((JButton) e.getSource() == _decode) {
				_crypto.setText(cry.decode(_crypto.getText(), _key.getText()));
				_log_output.setText(cry.getLog());
			} else if((JButton) e.getSource() == _hide){
				cry.hide(_log_output.getText());
				_log_output.setText(cry.getLog());
			}
			else if((JButton) e.getSource() == _load){
				_crypto.setText(cry.load());
				_log_output.setText(cry.getLog());
			}
			else if((JButton) e.getSource() == _choose){
				cry.chooseMedium();
				_log_output.setText(cry.getLog());
			}
		}
	}


	public static void build() {
		JFrame f = new JFrame(LABEL_JFRAME_TITLE);
		f.add(new EPanel());
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setMinimumSize(new Dimension(800, 500));
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}

	public static void main(String[] args) {
		EPanel.build();
	}
}
