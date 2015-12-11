package Game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

public class PostDialog extends JDialog implements ActionListener {
	int Time;
	
	JButton okBtn;
	JButton cancelBtn;
	JPanel buttonPan;
	
	JLabel noticeLable;

	JTextField nameField;
	JTextField commentField;
	JPanel textPan;
	
	public PostDialog(JFrame parentFrame, String title, boolean modal) {
		super(parentFrame, title, modal);
		setSize(300, 165);
		setLayout(new BorderLayout());
		init();
		setResizable(false);
	}
	
	private JButton CreateDesignedBtn(String text) {
		JButton btn = new JButton(text);
		btn.setBackground(Game.defaultBackground);
		btn.setFont(new Font("맑은 고딕",Font.PLAIN, 13));
		btn.setForeground(Color.WHITE);
		btn.setFocusable(false);
		btn.setBorder(BorderFactory.createLineBorder(new Color(200,200,200), 2));
		btn.addActionListener(this);
		return btn;
	}
	
	private JPanel CreateInputPenel(String text, JTextField input) {
		JPanel jp = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		jp.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
		JLabel item = new JLabel(text);
		item.setFont(new Font("맑은 고딕",Font.PLAIN, 12));
		item.setForeground(Color.WHITE);
		
		input.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.WHITE));
		input.setHorizontalAlignment(JTextField.CENTER);
		input.setBackground(Game.defaultBackground);
		input.setForeground(Color.WHITE);
		
		jp.add(item);
		jp.add(input);
		jp.setBackground(Game.defaultBackground);
		
		return jp;
	}
	
	private void init() {
		buttonPan = new JPanel(new GridLayout(1, 2, 5, 5));
		buttonPan.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		okBtn = CreateDesignedBtn("등 록");
		cancelBtn = CreateDesignedBtn("취 소");
		buttonPan.add(okBtn);
		buttonPan.add(cancelBtn);
		buttonPan.setBackground(Game.defaultBackground);
		
		nameField = new JTextField(18);
		nameField.setDocument(new BoundDocument(24, nameField));
		
		commentField = new JTextField(18);
		commentField.setDocument(new BoundDocument(50, commentField));

		textPan = new JPanel(new GridLayout(3,1,5,5));
		textPan.setBackground(Game.defaultBackground);
		noticeLable = new JLabel("Player 정보를 입력 해 주세요");
		noticeLable.setFont(new Font("맑은 고딕", Font.BOLD, 17));
		noticeLable.setForeground(Color.WHITE);
		JPanel j = new JPanel();
		j.add(noticeLable);
		j.setBackground(Game.defaultBackground);
		textPan.add(add(j));
		textPan.add(CreateInputPenel("이 름  :", nameField));
		textPan.add(CreateInputPenel("코멘트  :", commentField));
		
		add(buttonPan, BorderLayout.SOUTH);
		add(textPan, BorderLayout.CENTER);
		
	}
	
	class BoundDocument extends PlainDocument {
		protected int charLimit;
		protected JTextComponent textComp;
		public BoundDocument(int charLimit) { this.charLimit = charLimit; }
		public BoundDocument(int charLimit, JTextComponent textComp) { this.charLimit = charLimit; this.textComp = textComp; }

		public void insertString (int offs, String str, AttributeSet a) throws BadLocationException {
			if (textComp.getText().getBytes().length + str.getBytes().length <= charLimit) {
				super.insertString(offs, str, a);
			}
		}
	}

	public void Execute(int time) {
		this.Time = time;
		setLocation(getParent().getX() + (getParent().getWidth()/2) - (getWidth()/2),
				getParent().getY() +  (getParent().getHeight()/2) - (getHeight()/2));
		setVisible(true);
	}
	
	public void RefreshedCurScore() {
		okBtn.setText("등 록");
		okBtn.setEnabled(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == okBtn) {
			
			if ((nameField.getText() != null && nameField.getText().length()<=0)){
				JOptionPane.showMessageDialog(getContentPane(), "이름은 반드시 입력해주세요.");
			} else {
				// data post to web
				new Thread() {
					public void run() {
						StringBuilder result = new StringBuilder();
						ServerConnector post = new ServerConnector();
						ArrayList<String[]> data = new ArrayList<String[]>();
						data.add(new String[]{"NAME", nameField.getText()});
						data.add(new String[]{"COMMENT", commentField.getText()});
						data.add(new String[]{"SCORE", "" + (float)Time/1000});
						try {
							result.append(post.send("SAVE", data).trim());
							if (result.toString() != null && result.toString().equals("#complete")) {
								JOptionPane.showMessageDialog(getContentPane(), "등록 되었습니다");
								setVisible(false);
								okBtn.setEnabled(false);
								okBtn.setText("이미 등록되었습니다");
							} else {
								throw new IOException(result.toString());
							}
						} catch (IOException e) {
							JOptionPane.showMessageDialog(getContentPane(), "데이터를 전송할 수 없습니다.\n(" + e + ")");
							e.printStackTrace();
						}
					}
				}.start();
			}

		} else if (e.getSource() == cancelBtn) {
			setVisible(false);
		}
	}
}
