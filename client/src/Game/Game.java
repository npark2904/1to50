package Game;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.*;
import javax.swing.border.BevelBorder;

enum game_state { Ready, Play, End, exit };

public class Game extends JFrame implements ActionListener, WindowListener {
	
	public static Color defaultBackground = new Color(40,40,40);  
	/**
	 * I LOVE GNU
	 */
	private static final long serialVersionUID = 1L;

	private CardLayout cl;
	
	private JButton Start_Button;
	private JButton reStart_Button;
	private JButton rankUpdateBtn;
	private JButton rankShowBtn;
	private JButton NumButtonList[];
	private JButton resetBtn;
	private JButton exitBtn;
	
	private JLabel counterLabel;
	private JLabel ending_label;
	
	private JPanel Menu_Panel;
	private JPanel Game_Panel;
	private JPanel Counter_Panel;
	private JPanel Num_Board;
	private JPanel Ending_Board;
	private JPanel Ui_Btn_Panel;
	
	private int rnum[];	
	private int next_num;
	private int Time;
	private JButton nextNumBtn;
	
	private PostDialog rankPostDialog;
	private LoadDialog rankLoadDialog;

//	private boolean design_flag = true;

	
	
	TimeThread thread;
	
	class TimeThread extends Thread {
		private int checkTimer = 0;
		private int colorSelector = 255;
		private boolean colorFlag = true;
		private game_state state = game_state.Ready;
		private boolean game_run_flag = true;
		private Game game;
		
		public TimeThread(Game game){
			this.game = game; 
		}
		
		@Override
		public void run() {
			while(game_run_flag)
			{
				switch(state)
				{
					case Ready :
	
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						} finally {
							if (state != game_state.Ready) continue;
							if(game_run_flag==false) return;
						}
						
						if(Time < 3)
						{
							counterLabel.setText("" + (3-Time));
							Time += 1;
						}
						else if(Time >= 3 && Time < 4)
						{
							counterLabel.setText("S t a r t !!");
							counterLabel.setForeground(Color.RED);
							Time += 1;
						}
						else if(Time >= 4)
						{
							Set_ActivateButton();
							state = game_state.Play;
							counterLabel.setForeground(Color.WHITE);
							counterLabel.setHorizontalAlignment(JLabel.LEFT);
							counterLabel.setBorder(BorderFactory.createEmptyBorder(10, game.getBounds().width/2-80, 10, 0));
							Time = 0;
						}
						
						break;
						
					case Play :
						
						Time += 1;
						counterLabel.setText("" + (float)Time/1000);
						checkTimer += 1;
						
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							e.printStackTrace();
						} finally {
							if (state != game_state.Play) continue;
							if (game_run_flag==false) return;
						}
						
						if(checkTimer > 2000) {
							if (checkTimer % 20 == 0){
								if(colorSelector >= 255) {
									colorFlag = false;
								} else if (colorSelector <= 150) {
									colorFlag = true;
								}
								
								if (colorFlag == true) {
									colorSelector++;
								} else {
									colorSelector--;
								}
								
								nextNumBtn.setBackground(new Color(255, 255, colorSelector));
							}
						}
						break;
						
					case End :
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						ending_label.setText((float)Time/1000 + "");
						cl.show(game.getContentPane(), "Ending");
						FlagStop();
						break;
						
					default :

						break;
				}
				
	
			}
		}
	
		public void Checked(){
			checkTimer = 0;
			colorSelector = 255;
		}
		
		public void FlagStop() {
			game_run_flag = false;
		}
		
		public void FlagStart() {
			game_run_flag = true;
		}
		
		public boolean getFlag(){
			return game_run_flag;
		}
		
		public void setState(game_state state){
			this.state = state;
		}
	}

	Game()
	{
		next_num = 1;
		Time = 0;
		
		setBackground(defaultBackground);
		cl = new CardLayout();
		setVisible(true);
		
		setLayout(cl);

		Set_MenuBoard();
		Set_GameBoard();
		Set_EndBoard();
		cl.show(this.getContentPane(), "Menu");
		
		addWindowListener(this);
		setBounds(500, 200, 500, 500);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		rankPostDialog = new PostDialog(this, "Player Á¤º¸ ÀÔ·Â", true); 
		rankLoadDialog = new LoadDialog(this, "Top Ranker", true);

	}
	

	private void StartGame(){
		if ( thread != null && thread.getFlag() == true) {
			thread.FlagStop();
		}
		Time = 0;
		next_num = 1;
		rankPostDialog.RefreshedCurScore();
		
	    remove(Game_Panel);
	    remove(Menu_Panel);
	    remove(Ending_Board);
		Set_GameBoard();
		Set_MenuBoard();
		Set_EndBoard();
		
		for(int i=0; i<25; i++) {
			if (NumButtonList[i].getText().equals(""+next_num)) {
				nextNumBtn = NumButtonList[i];
				break;
			}
		}
		
		cl.show(this.getContentPane(), "Game");
		thread = new TimeThread(this);
		thread.start();
	}
	
	private void Set_EndBoard()
	{
		JLabel j = new JLabel("Your Score is", JLabel.CENTER);
		ending_label = new JLabel((float)Time/100 + "", JLabel.CENTER);
		JLabel j3 = new JLabel("·©Å·¼­¹ö Å×½ºÆ® Áß..", JLabel.CENTER);
		
		j.setFont(new Font("verdana",Font.BOLD, 35));
		j.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
		j.setForeground(new Color(200,200,200));
		
		ending_label.setFont(new Font("verdana",Font.BOLD, 70));
		ending_label.setForeground(Color.WHITE);
		j3.setFont(new Font("¸¼Àº °íµñ",Font.BOLD, 12));
		j3.setForeground(new Color(200,200,200));
		
		reStart_Button = new JButton("ReGame");
		reStart_Button.setBackground(defaultBackground);
		reStart_Button.setForeground(Color.WHITE);
		reStart_Button.setBorder(BorderFactory.createLineBorder(new Color(200,200,200), 2));
		reStart_Button.setFont(new Font("verdana",Font.BOLD, 20));
		reStart_Button.setFocusable(false);
		reStart_Button.addActionListener(this);
		
		rankUpdateBtn = new JButton("·©Å· µî·Ï");
		rankUpdateBtn.setBackground(defaultBackground);
		rankUpdateBtn.setForeground(Color.WHITE);
		rankUpdateBtn.setBorder(BorderFactory.createLineBorder(new Color(200,200,200), 2));
		rankUpdateBtn.setFont(new Font("¸¼Àº °íµñ",Font.BOLD, 20));
		rankUpdateBtn.setFocusable(false);
		rankUpdateBtn.addActionListener(this);
		
		rankShowBtn = new JButton("·©Å· È®ÀÎ");
		rankShowBtn.setBackground(defaultBackground);
		rankShowBtn.setForeground(Color.WHITE);
		rankShowBtn.setBorder(BorderFactory.createLineBorder(new Color(200,200,200), 2));
		rankShowBtn.setFont(new Font("¸¼Àº °íµñ",Font.BOLD, 20));
		rankShowBtn.setFocusable(false);
		rankShowBtn.addActionListener(this);
		
		Ending_Board = new JPanel(new GridLayout(7,1,15,10)); //¹öÆ°¸Þ´º °³¼ö+2, °¡·Î¹è¿­, °£°Ý,°£°Ý
		Ending_Board.setBorder(BorderFactory.createEmptyBorder(0,80,0,80));
		Ending_Board.add(j);
		Ending_Board.add(ending_label);
		Ending_Board.add(new JLabel());
		Ending_Board.add(reStart_Button);
		Ending_Board.add(rankUpdateBtn);
		Ending_Board.add(rankShowBtn);
		Ending_Board.add(j3);
		Ending_Board.setBackground(defaultBackground);

		add("Ending", Ending_Board);
	}

	private void Set_MenuBoard()
	{
		Start_Button = new JButton("S T A R T");
		Start_Button.setBackground(defaultBackground);
		Start_Button.setForeground(Color.WHITE);
		Start_Button.setBorder(BorderFactory.createLineBorder(new Color(200,200,200), 2));
		Start_Button.setFont(new Font("verdana",Font.BOLD, 20));
		Start_Button.setFocusable(false);
		Start_Button.addActionListener(this);
		
		rankShowBtn = new JButton("·©Å· È®ÀÎ");
		rankShowBtn.setBackground(defaultBackground);
		rankShowBtn.setForeground(Color.WHITE);
		rankShowBtn.setBorder(BorderFactory.createLineBorder(new Color(200,200,200), 2));
		rankShowBtn.setFont(new Font("¸¼Àº °íµñ",Font.BOLD, 20));
		rankShowBtn.setFocusable(false);
		rankShowBtn.addActionListener(this);
		
		JLabel title = new JLabel("1 TO 50", JLabel.CENTER);
		title.setForeground(Color.WHITE);
		title.setFont(new Font("verdana",Font.BOLD, 75));
		
		Menu_Panel = new JPanel();
		Menu_Panel.setBorder(BorderFactory.createEmptyBorder(0, 80, 0, 80));
		Menu_Panel.setLayout(new GridLayout(6,1,20,20));
		Menu_Panel.add(new JLabel());
		Menu_Panel.add(title);
		Menu_Panel.add(new JLabel());
		Menu_Panel.add(Start_Button);
		Menu_Panel.add(rankShowBtn);
		Menu_Panel.setBackground(defaultBackground);
		
		add("Menu", Menu_Panel);
	}
	
	private void Set_GameBoard()
	{
		Game_Panel = new JPanel();
		Game_Panel.setLayout(new BorderLayout());
		
		Num_Board = new JPanel(new GridLayout(5,5,3,3));
		Num_Board.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
		Num_Board.setBackground(new Color(240,240,240));
		
		Counter_Panel = new JPanel(new BorderLayout());
		Counter_Panel.setBackground(defaultBackground);
		
		Mix();
		CreateCounter();
		Create_NumButton();
		
		Game_Panel.add(Counter_Panel, BorderLayout.NORTH);
		Game_Panel.add(Num_Board, BorderLayout.CENTER);
		Game_Panel.setBackground(defaultBackground);
		
		add("Game", Game_Panel);
		
	}//ÃÖÁ¾ º¸µå ·¹ÀÌ¾Æ¿ô ¼³Á¤

	private void Set_ActivateButton()
	{
		for(int i=0; i<25; i++)
		{
			NumButtonList[i].setBackground(Color.WHITE);
			NumButtonList[i].setForeground(Color.BLACK);
			NumButtonList[i].setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED,
					new Color(245,245,245), new Color(230,230,220), new Color(180,180,200), new Color(210,210,210)));
			NumButtonList[i].setFont(new Font("verdana",Font.PLAIN, 23));
			NumButtonList[i].setEnabled(true);
		}
	}
	
	private void Create_NumButton()
	{
		for(int i=0; i<25; i++) {
			NumButtonList[i] = new JButton(""+ (rnum[i]+1));
			NumButtonList[i].setBackground(new Color(240,240,240));
			NumButtonList[i].setForeground(new Color(220,220,220));
			NumButtonList[i].setBorder(BorderFactory.createLineBorder(new Color(230,230,230),1));
			NumButtonList[i].setFont(new Font("verdana",Font.PLAIN, 22));
			NumButtonList[i].setEnabled(false);
			NumButtonList[i].setFocusable(false);
			NumButtonList[i].addActionListener(this);
		}
		for(int i=0; i<25; i++) {
			Num_Board.add(NumButtonList[i]);
		}
	}//¹öÆ° °´Ã¼ »ý¼º

	private void Mix()
	{
		rnum = new int[50];
		for(int k=0; k<50; k++)
			rnum[k] = 9999;
		
		NumButtonList = new JButton[25];
		
		int i, count = 0;

		while(true)
		{
			i = (int)(Math.random() * 25);
			
			if(rnum[i] == 9999)
			{
				rnum[i] = count;
				count++;
			}
			
			if(count >= 25)
				break;
		}

		while(true)
		{
			i = (int)(Math.random() * 25) + 25;
			
			if(rnum[i] == 9999)
			{
				rnum[i] = count;
				count++;
			}
			
			if(count >= 50)
				break;
		}
	}
	
	private void CreateCounter()
	{
		counterLabel = new JLabel("R e a d y", JLabel.CENTER);
		counterLabel.setForeground(Color.YELLOW);
		counterLabel.setFont(new Font("verdana", Font.BOLD, 50));
		counterLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		
		Counter_Panel.add(counterLabel, BorderLayout.CENTER);
		
		exitBtn = new JButton("    X    ");
		exitBtn.setBackground(defaultBackground);
		exitBtn.setBorder(BorderFactory.createLineBorder(new Color(80,80,80), 2));
		exitBtn.setForeground(new Color(200,200,200));
		exitBtn.setFocusable(false);
		exitBtn.addActionListener(this);
		
		resetBtn = new JButton("    R    ");
		resetBtn.setBackground(defaultBackground);
		resetBtn.setBorder(BorderFactory.createLineBorder(new Color(80,80,80), 2));
		resetBtn.setForeground(new Color(200,200,200));
		resetBtn.setFocusable(false);
		resetBtn.addActionListener(this);
		
		Ui_Btn_Panel = new JPanel(new GridLayout(2,1,2,2));
		Ui_Btn_Panel.setBackground(defaultBackground);
		Ui_Btn_Panel.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 3));
		Ui_Btn_Panel.add(exitBtn);
		Ui_Btn_Panel.add(resetBtn);
		
		Counter_Panel.add(Ui_Btn_Panel, BorderLayout.EAST);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String s = e.getActionCommand();
		
		if(s.equals("" + next_num)) {
			if(next_num >= 50) {
				 thread.setState(game_state.End);
				 return;
			}
			for (int i=0; i<25; i++) {
				if (NumButtonList[i].getText() == e.getActionCommand()) {
					for (int next=0; next<25; next++) {
						if (NumButtonList[next].getText().equals("" + (Integer.parseInt(e.getActionCommand()) + 1))) {
							NumButtonList[i].setBackground(Color.WHITE);
							nextNumBtn = NumButtonList[next];
							thread.Checked();
						}
					}
					if (next_num <= 25) {

						NumButtonList[i].setText("" + (rnum[i+25] + 1));
						//NumButtonList[i].setForeground(new Color(100,100,150));
					} else {
						NumButtonList[i].setVisible(false);
					}
					break;
				}
			}
			next_num++;
		} else if (e.getSource() == Start_Button || e.getSource() == reStart_Button || e.getSource() == resetBtn) {
			StartGame();
		} else if(e.getSource() == exitBtn) {
			if ( thread != null && thread.getFlag() == true) {
				thread.FlagStop();
			}
			
		    remove(Game_Panel);
		    remove(Menu_Panel);
		    remove(Ending_Board);
			Set_GameBoard();
			Set_MenuBoard();
			Set_EndBoard();
			
			cl.show(this.getContentPane(), "Menu");
		} else if(e.getSource() == rankUpdateBtn) {
			rankPostDialog.Execute(Time);
			
		} else if(s.equals("·©Å· È®ÀÎ")) {
			// web data load
			rankLoadDialog.Execute(1);
		}
	}
	
	
	@Override
	public void windowActivated(WindowEvent arg0) {	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		if (thread != null && thread.getFlag() == true){
			thread.FlagStop();
		}
	}

	@Override
	public void windowClosing(WindowEvent arg0) {	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {	}

	@Override
	public void windowIconified(WindowEvent arg0) {	}

	@Override
	public void windowOpened(WindowEvent arg0) {	}

}
