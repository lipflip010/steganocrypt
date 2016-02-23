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
	private static final String LABEL_BUTTON_STEGANOGRAPHY = "Choose File";
	private static final String LABEL_BUTTON_LOAD ="Load";
	private static final String LABEL_BUTTON_HIDE ="Hide";
	

	private JTextArea _input;
	private JTextArea _output;
	private JTextField _key;
	private JButton _encode;
	private JButton _decode;
	private JButton _choose_carrier;
	private JButton _load;
	private JButton _hide;

	private boolean _encode_was_last;

	public EPanel() {
		setLayout(new BorderLayout());

		_encode_was_last = true;

		ActionHandler ah = new ActionHandler();
		KeyHandler kh = new KeyHandler();

		_input = new JTextArea();
		_input.setLineWrap(true);
		_input.addKeyListener(kh);
		_output = new JTextArea();
		_output.setEditable(false);
		_output.setLineWrap(true);
		_key = new JTextField();
		_key.addKeyListener(kh);
		_choose_carrier = new JButton(LABEL_BUTTON_STEGANOGRAPHY);
		_hide = new JButton(LABEL_BUTTON_HIDE);
		_load = new JButton(LABEL_BUTTON_LOAD);
		_hide.addActionListener(ah);
		_load.addActionListener(ah);
		_choose_carrier.addActionListener(ah);
		_encode = new JButton(LABEL_BUTTON_ENCODE);
		_encode.addActionListener(ah);
		_decode = new JButton(LABEL_BUTTON_DECODE);
		_decode.addActionListener(ah);

		JPanel buttons = new JPanel(new GridLayout(1, 0));
		buttons.add(_decode);
		buttons.add(_key);
		buttons.add(_encode);

		JPanel input_panel = new JPanel(new BorderLayout());
		input_panel.add(new JScrollPane(_input), BorderLayout.CENTER);
		input_panel.add(buttons, BorderLayout.SOUTH);

		JPanel center = new JPanel(new GridLayout(0, 1));
		center.add(input_panel);
		center.add(new JScrollPane(_output));
		
		JPanel steganography = new JPanel(new GridLayout(0,3));
		steganography.add(_choose_carrier);
		steganography.add(_load);
		steganography.add(_hide);

		add(center, BorderLayout.CENTER);
		add(steganography, BorderLayout.SOUTH);
		
	}

	private class ActionHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if ((JButton) e.getSource() == _encode) {
				_output.setText(Cryptography.encode(_input.getText(), _key.getText()));
				_encode_was_last = true;
			} else if ((JButton) e.getSource() == _decode) {
				_output.setText(Cryptography.decode(_input.getText(), _key.getText()));
				_encode_was_last = false;
			} else if ((JButton) e.getSource() == _choose_carrier){
				Cryptography.appendToFile(_output.getText());
			}else if((JButton) e.getSource() == _hide){
				System.out.println("Hide");
			}
			else if((JButton) e.getSource() == _load){
				System.out.println("Load");
			}
				
		}
	}

	private class KeyHandler extends KeyAdapter {
		public void keyReleased(KeyEvent e) {
			if (_encode_was_last) {
				_output.setText(Cryptography.encode(_input.getText(), _key.getText()));
			} else {
				_output.setText(Cryptography.decode(_input.getText(), _key.getText()));
			}
		}
	}

	public static void build() {
		JFrame f = new JFrame(LABEL_JFRAME_TITLE);
		f.add(new EPanel());
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setMinimumSize(new Dimension(600, 700));
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}

	public static void main(String[] args) {
		EPanel.build();
	}
}
