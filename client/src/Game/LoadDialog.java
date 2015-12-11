package Game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;

public class LoadDialog extends JDialog implements ActionListener {

	ArrayList<Person> scoreData;
	JLabel noticeLable;

	JButton refreshBtn;
	JButton cancelBtn;
	JPanel buttonPan;
	JPanel listPan;
	JPanel titlePan;
	private ArrayList<JButton> PageBtnList;
	
	LoadThread loadThread;
	
	int curPage = 1;
	int totalDataNum = 0;
	
	public LoadDialog(JFrame parentFrame, String title, boolean modal) {
		super(parentFrame, title, modal);
		setSize(620, 460);
		setLayout(new BorderLayout());
		init();
		setResizable(false);
	}
	

	private void init() {
		titlePan = new JPanel(new BorderLayout());
		titlePan.setBackground(Game.defaultBackground);
		
		noticeLable = new JLabel("Top Rank " + ((curPage-1)*10 + 1) + "~" + (curPage)*10);
		noticeLable.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 35));
		noticeLable.setForeground(Color.WHITE);
		JPanel centering = new JPanel();
		centering.add(noticeLable);
		centering.setBackground(Game.defaultBackground);
		titlePan.add(centering);
		
		add(titlePan, BorderLayout.NORTH);
		setListPanel();
		setButtonPanel();
	}
		
	private void setListPanel() {
		if (listPan != null) {
			listPan.removeAll();
		} else {
			listPan = new JPanel();
			listPan.setBackground(Game.defaultBackground);
			add(listPan, BorderLayout.CENTER);
			return;
		}
		listPan = new JPanel(new GridLayout(12,1,5,0));
		listPan.setBorder(BorderFactory.createEmptyBorder(0, 7, 5, 7));
		
		if (scoreData != null) {
			for (int i=-1; i<scoreData.size(); i++) {
				listPan.add(CreateListItemPanel(i));
			}
		}
		
		listPan.add(CreatePageBtn());
		listPan.setBackground(Game.defaultBackground);
		
		//remove(listPan);
		add(listPan, BorderLayout.CENTER);
	}
	
	private JPanel CreatePageBtn() {
		PageBtnList = new ArrayList<JButton>();
		
//		for (int i=0; i<10; i++) {
//			PageBtnList.add(CreatePageNumBtn());
//		}

		JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
		p.setBackground(Game.defaultBackground);
		
		int maxPageLimit = totalDataNum/10;
		maxPageLimit = (totalDataNum % 10) == 0 ? maxPageLimit : maxPageLimit+1; 

		if (curPage > 5) {
			PageBtnList.add(CreatePageNumBtn(" 1 "));
			PageBtnList.add(CreatePageNumBtn("..."));
		}
		
		for (int idx=PageBtnList.size(), pg=curPage-4; pg<curPage+5; pg++, idx++) {
//			PageBtnList.get(idx).setText(" " + pg + " ");
			PageBtnList.add(CreatePageNumBtn(" " + pg + " "));
			if (pg == curPage) {
				PageBtnList.get(idx).setForeground(new Color(206,242,121));
				PageBtnList.get(idx).setBorder(BorderFactory.createLineBorder(new Color(80,80,80), 2));
			}
		}
		
		if (curPage < maxPageLimit-5) {
			PageBtnList.add(CreatePageNumBtn("..."));
			PageBtnList.add(CreatePageNumBtn(" " + maxPageLimit + " "));
		}
		

		for (int i=0; i<PageBtnList.size(); i++) {
			if (!PageBtnList.get(i).getText().equals("...")) {
				int tmp = Integer.parseInt(PageBtnList.get(i).getText().trim());
				if (tmp <= 0 || tmp > maxPageLimit) {
					continue;
				}
			} else {
				PageBtnList.get(i).setEnabled(false);
			}
			p.add(PageBtnList.get(i));
		}
		return p;
	}
	
	private JButton CreatePageNumBtn(String str) {
		JButton btn = new JButton(str);
		btn.setBackground(Game.defaultBackground);
		btn.setForeground(Color.WHITE);
		btn.setBorder(BorderFactory.createLineBorder(Game.defaultBackground, 2));
		btn.setFocusable(false);
		btn.addActionListener(this);
		return btn;
	}
	
	private JButton CreatePageNumBtn() {
		return CreatePageNumBtn("");
	}
	
	private void setButtonPanel(){
		if (buttonPan != null) {
			buttonPan.removeAll();
		}
		buttonPan = new JPanel(new GridLayout(1, 2, 5, 5));
		buttonPan.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
		refreshBtn = CreateDesignedBtn("Refresh");
		cancelBtn = CreateDesignedBtn("Cancel");

		buttonPan.add(refreshBtn);
		buttonPan.add(cancelBtn);
		buttonPan.setBackground(Game.defaultBackground);
		
		add(buttonPan, BorderLayout.SOUTH);
	}
	
	private JButton CreateDesignedBtn(String text) {
		JButton btn = new JButton(text);
		btn.setBackground(Game.defaultBackground);
		btn.setForeground(Color.WHITE);
		btn.setFocusable(false);
		btn.setBorder(BorderFactory.createLineBorder(new Color(200,200,200), 2));
		btn.addActionListener(this);
		return btn;
	}
	
	private JTextField CreateDesignedTextField(String str, int widthLength, boolean isTitle){
		JTextField tf = new JTextField(str, widthLength);
		if (isTitle) {
			tf.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 12));
			tf.setForeground(new Color(200,200,0));

		}
		else {
			tf.setFont(new Font("¸¼Àº °íµñ",Font.PLAIN, 12));
			tf.setForeground(Color.WHITE);
		}
		tf.setBorder(null);
		tf.setBackground(Game.defaultBackground);
		tf.setEditable(false);
		
		return tf;
	}
	
	private JPanel CreateListItemPanel(int idx) {
		JPanel jp = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		jp.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		boolean isTitle = idx < 0 ? true : false;
		if (isTitle) {
			jp.add(CreateDesignedTextField("¼øÀ§", 3, isTitle));
			jp.add(CreateDesignedTextField("ÀÌ ¸§", 9, isTitle));
			jp.add(CreateDesignedTextField("±â ·Ï", 4, isTitle));
			jp.add(CreateDesignedTextField("ÄÚ ¸à Æ®", 23, isTitle));
			jp.add(CreateDesignedTextField("³¯ Â¥", 11, isTitle));
		} else {
			jp.add(CreateDesignedTextField("" + (idx+((curPage-1)*10)+1), 3, isTitle));
			jp.add(CreateDesignedTextField(scoreData.get(idx).getName(), 9, isTitle));
			jp.add(CreateDesignedTextField(scoreData.get(idx).getScore(), 4, isTitle));
			jp.add(CreateDesignedTextField(scoreData.get(idx).getComment(), 23, isTitle));
			jp.add(CreateDesignedTextField(scoreData.get(idx).getDate(), 11, isTitle));
		}
		jp.setBackground(Game.defaultBackground);
		
		return jp;
	}
	
	
	public void Execute(int page) {
		if (loadThread == null || loadThread.isRun == false) {
			loadThread = new LoadThread(page);
			loadThread.start();
			setLocation(getParent().getX() + (getParent().getWidth()/2) - (getWidth()/2),
					getParent().getY() +  (getParent().getHeight()/2) - (getHeight()/2));
			setVisible(true);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == refreshBtn) {
			Execute(curPage);
		} else if (e.getSource() == cancelBtn) {
			loadThread.isRun = false;
			setVisible(false);
		} else {
			if(PageBtnList.contains(e.getSource()) && !((JButton)e.getSource()).getText().equals("...")) {
				Execute(Integer.parseInt(e.getActionCommand().trim()));
			}
			
		}
	}
	
	
	class LoadThread extends Thread {
		boolean isRun = true;
		int page = 0;
		
		public LoadThread(int page) {
			curPage = page;
			this.page = page;
		}
		
		public void run() {
			refreshBtn.setEnabled(false);
			StringBuilder waitText = new StringBuilder("wait..");
			refreshBtn.setText(waitText.toString());
			
			ServerConnector post = new ServerConnector();
			XMLParser parser;
			try {
				ArrayList<String[]> data = new ArrayList<String[]>();
				data.add(new String[]{"PAGE", "" + (page-1)});
				parser = new XMLParser(post.send("LOAD", data));

				//System.out.println("[Log] result parser : \n" + parser);
				scoreData = parser.getScoreData();
				totalDataNum = parser.getPageCount();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(getContentPane(), "µ¥ÀÌÅÍ¸¦ Àü¼ÛÇÒ ¼ö ¾ø½À´Ï´Ù.\n(" + e + ")");
				e.printStackTrace();
			}
			
			setListPanel();
			noticeLable.setText("Top Rank " + ((curPage-1)*10 + 1) + "~" + (curPage)*10);
			
			while (isRun) {
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} 
				refreshBtn.setText("Refresh");
				refreshBtn.setEnabled(true);
				isRun = false;
				break;
				
			}
		}
	};
	
}
