package tictactoe.myprojects;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.Random;

public class TicTacToe extends JFrame {
	private JButton tl, tm, tr, ml, mm, mr, bl, bm, br;
	private JPanel topPanel, middlePanel, bottomPanel;
	private final Color PLAYER_COLOR = Color.BLUE;
	private final Color COMPUTER_COLOR = Color.RED;
	private final String PLAYER_SYMBOL = "X";
	private final String COMPUTER_SYMBOL = "O";
	private Random computerMove;

	public TicTacToe() {
		setTitle("Tic-Tac-Toe");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		buildButton();
		buildPanel();
		add(topPanel, BorderLayout.NORTH);
		add(middlePanel, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
		getContentPane().setBackground(Color.WHITE);
		setLocationRelativeTo(null);
		pack();
		setVisible(true);
	}
	
	private void buildButton() {
		tl = new JButton();
		tm = new JButton();
		tr = new JButton();
		ml = new JButton();
		mm = new JButton();
		mr = new JButton();
		bl = new JButton();
		bm = new JButton();
		br = new JButton();
		
		tl.addActionListener(new ButtonListener());
		tm.addActionListener(new ButtonListener());
		tr.addActionListener(new ButtonListener());
		ml.addActionListener(new ButtonListener());
		mm.addActionListener(new ButtonListener());
		mr.addActionListener(new ButtonListener());
		bl.addActionListener(new ButtonListener());
		bm.addActionListener(new ButtonListener());
		br.addActionListener(new ButtonListener());
	}
	
	private void buildPanel() {
		topPanel = new JPanel();
		middlePanel = new JPanel();
		bottomPanel = new JPanel();
		
		topPanel.add(tl);
		topPanel.add(tm);
		topPanel.add(tr);
		middlePanel.add(ml);
		middlePanel.add(mm);
		middlePanel.add(mr);
		bottomPanel.add(bl);
		bottomPanel.add(bm);
		bottomPanel.add(br);
		
		topPanel.setBackground(Color.BLACK);
		middlePanel.setBackground(Color.BLACK);
		bottomPanel.setBackground(Color.BLACK);		
	}
	
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JButton buttonClicked = (JButton) e.getSource();
			
			setColor(buttonClicked, PLAYER_COLOR, PLAYER_SYMBOL);
			buttonClicked.setBorderPainted(false);
			getComputerMove();
		}
	}
	
	private void getComputerMove() {
		JButton button = null;
		computerMove = new Random();
		int move = 0;
		
		while (button == null) {
		move = computerMove.nextInt(9) + 1;
		System.out.println(move);

		switch (move) {
			case 1: 
				if (tl.isBorderPainted()) {
					button = tl;
					tl.setBorderPainted(false);
					break;
				}
			case 2:
				if (tm.isBorderPainted()) {
					button = tm;
					tm.setBorderPainted(false);
					break;
				}
			case 3:
				if (tr.isBorderPainted()) {
					button = tr;
					tr.setBorderPainted(false);
					break;
				}
			case 4:
				if (ml.isBorderPainted()) {
					button = ml;
					ml.setBorderPainted(false);
					break;
				}
			case 5:
				if (mm.isBorderPainted()) {
					button = mm;
					mm.setBorderPainted(false);
					break;
				}
			case 6:
				if (mr.isBorderPainted()) {
					button = mr;
					mr.setBorderPainted(false);
					break;
				}
			case 7:
				if (bl.isBorderPainted()) {
					button = bl;
					bl.setBorderPainted(false);
					break;
				}
			case 8:
				if (bm.isBorderPainted()) {
					button = bm;
					bm.setBorderPainted(false);
					break;
				}
			case 9:
				if (br.isBorderPainted()) {
					button = br;
					br.setBorderPainted(false);
					break;
				}
			default: {
				button = new JButton("END OF MOVES");
				break;
				}
			
			}

		}
		System.out.println(button);
		setColor(button, COMPUTER_COLOR, COMPUTER_SYMBOL);
	}
	
	private void setColor(JButton button, Color color, String symbol) {
			button.setText(symbol);
			button.setOpaque(true);
			button.setBackground(color);
	}

	public static void main(String[] args) {
		new TicTacToe();
	}
}