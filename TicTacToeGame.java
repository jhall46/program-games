package myprojects.game.tictactoe;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
public class TicTacToeGame extends JFrame {
	private final String[] NORMAL = { 
			"Images/X.JPG", 
            "Images/O.JPG", 
			"Images/Blank.JPG" };
	private final String[] JACOB_AND_JESICA = { 
			"Images/SunflowerX.JPG", 
            "Images/CookieO.JPG", 
		    "Images/UsBlank.JPG" };
	private final String[] POKEMON = { 
			"Images/PokeX.jpg", 
            "Images/PokeO.jpg", 
		    "Images/PokeBlank.jpg" };
	private final String[] COMPUTER = {
			"Images/HumanX.jpg", 
			"Images/ComputerO.jpg", 
	        "Images/ComputerBlank.jpg"};
	private final String CATS_GAME = 
			"Images/CatsGame.jpg";
	 private static final String BUTTON_NOISE = "Music/Pop.aiff"; 
	 private static final String LOSING_NOISE = "Music/Funk.aiff"; 
	 private static final String WINNING_NOISE = "Music/Blow.aiff";
	private JMenuBar menuBar;
	private JMenu fileMenu, newGameMenu, stylesMenu, difficultyMenu;
	private JMenuItem[] menuItems;
	private JButton[] gameSquareButtons;
	private JPanel gameBoardPanel;
	private final int PLAYER_1_MOVE = -1, PLAYER_2_MOVE = 1, PLAYER_1_WIN = -3, 
			          PLAYER_2_WIN = 3, MAX_SQUARES = 9, MAX_HANDS = 8;
	private boolean player1Wins, player2Wins, isPlayer1Move, isSinglePlayer, isFirstMove;
	private int[] winningHands, gameboard;
	private final int WIDTH = 150, HEIGHT = 135, WINDOW_WIDTH = 500, WINDOW_HEIGHT = 500,
					  EASY = 0, MODERATE = 1, HARD = 2;
	private String xImage, oImage, startingImage, xText, oText;
	private int computerMove, gameMode, difficulty;
	private Timer timer;
    private Clip clip;
    private AudioInputStream audioInputStream; 
	
	public TicTacToeGame() throws UnsupportedAudioFileException, 
                                  IOException, LineUnavailableException  {
		buildVariables();
		buildButton();
		buildPanel();
		buildMenuItem();
		buildMenu();
		buildMenuBar();
		buildAudio();
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setJMenuBar(menuBar);
		setBackground(Color.YELLOW);
		add(gameBoardPanel);
		setTitle("Tic-Tac-Toe (Multiplayer Mode)");
		setLocationRelativeTo(null);
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setVisible(true);
	}
	private void buildVariables() {
		String x = NORMAL[0];
		String o = NORMAL[1];
		String s = NORMAL[2];
		setImage(x, o, s);
		xText = "X";
		oText = "O";
		isSinglePlayer = false;
		player1Wins = false;
		player2Wins = false;
		isPlayer1Move = true;
		isFirstMove = true;
		difficulty = EASY;
		gameboard = new int[MAX_SQUARES];
		winningHands = new int[MAX_HANDS];
		buildWinningHands();
	}
	private ImageIcon getIcon(String image) {
		return new ImageIcon(
				new ImageIcon(image).getImage().getScaledInstance(
				WIDTH, HEIGHT, java.awt.Image.SCALE_SMOOTH));
	}
	private void buildWinningHands() {
		// Rows 1-3
		winningHands[0] = gameboard[0] + gameboard[1] + gameboard[2];
		winningHands[1] = gameboard[3] + gameboard[4] + gameboard[5];
		winningHands[2] = gameboard[6] + gameboard[7] + gameboard[8];
		// Columns 1-3
		winningHands[3] = gameboard[0] + gameboard[3] + gameboard[6];
		winningHands[4] = gameboard[1] + gameboard[4] + gameboard[7];
		winningHands[5] = gameboard[2] + gameboard[5] + gameboard[8];
		// Diagonals 1-2
		winningHands[6] = gameboard[0] + gameboard[4] + gameboard[8];
		winningHands[7] = gameboard[2] + gameboard[4] + gameboard[6];
	}
	private void buildButton() {
		gameSquareButtons = new JButton[MAX_SQUARES];
		
		for (int i = 0; i < MAX_SQUARES; i++) {
			gameSquareButtons[i] = new JButton();
			setImage(startingImage, i);
			gameSquareButtons[i].addActionListener(new ButtonListener());
			gameSquareButtons[i].setBorder(new LineBorder(Color.BLACK));
		}
	}
	private void setImage(String file, int index) {
		gameSquareButtons[index].setIcon(getIcon(file));
	}
	private void buildPanel() {
		gameBoardPanel = new JPanel();
		gameBoardPanel.setBackground(Color.DARK_GRAY);
		gameBoardPanel.setLayout(new GridLayout(3, 3));
		
		for (int i = 0; i < MAX_SQUARES; i++) {
			gameBoardPanel.add(gameSquareButtons[i]);
		}
	}
	private void buildMenuItem() {
		menuItems = new JMenuItem[10];
		
		menuItems[0] = new JMenuItem("Single Player");
		menuItems[1] = new JMenuItem("Multiplayer");
		menuItems[2] = new JMenuItem("Normal");
		menuItems[3] = new JMenuItem("Jake & Jes");
		menuItems[4] = new JMenuItem("Pokemon");
		menuItems[5] = new JMenuItem("Cyber");
		menuItems[6] = new JMenuItem("Exit");
		menuItems[7] = new JMenuItem("Easy");
		menuItems[8] = new JMenuItem("Moderate");
		menuItems[9] = new JMenuItem("Hard");
		
		for (int i = 0; i < menuItems.length; i++) {
			menuItems[i].addActionListener(new MenuItemListener());
		}
	}
	private void buildMenu() {
		fileMenu = new JMenu("File");
		newGameMenu = new JMenu("New Game");
		stylesMenu = new JMenu("Styles");
		difficultyMenu = new JMenu("Difficulty");
		
		fileMenu.add(newGameMenu);
		fileMenu.add(stylesMenu);
		fileMenu.add(difficultyMenu);
		
		newGameMenu.add(menuItems[0]);
		newGameMenu.add(menuItems[1]);
		stylesMenu.add(menuItems[2]);
		stylesMenu.add(menuItems[3]);
		stylesMenu.add(menuItems[4]);
		stylesMenu.add(menuItems[5]);
		fileMenu.add(menuItems[6]);
		difficultyMenu.add(menuItems[7]);
		difficultyMenu.add(menuItems[8]);
		difficultyMenu.add(menuItems[9]);
		
		fileMenu.setBorder(BorderFactory.createEtchedBorder());
		newGameMenu.setBorder(BorderFactory.createEtchedBorder());
		stylesMenu.setBorder(BorderFactory.createEtchedBorder());
	}
	private void buildMenuBar() {
		menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		menuBar.setOpaque(true);
		menuBar.setBorder(BorderFactory.createEtchedBorder());
	}
	private void buildAudio() throws UnsupportedAudioFileException, 
    	IOException, LineUnavailableException{
		audioInputStream =  
				AudioSystem.getAudioInputStream(new File(BUTTON_NOISE).getAbsoluteFile());
	}
	private void playAudio(String noise) throws LineUnavailableException, IOException, 
		InterruptedException, UnsupportedAudioFileException {
		audioInputStream =  
				AudioSystem.getAudioInputStream(new File(noise).getAbsoluteFile());
		clip = AudioSystem.getClip(); 
		clip.open(audioInputStream); 
		clip.start();
	}
	private void setImage(String x, String o, String s) {
		xImage = x;
		oImage = o;
		startingImage = s;
	}
	private void resetGame() {
		setImage(xImage, oImage, startingImage);
		player1Wins = false;
		player2Wins = false;
		isPlayer1Move = true;
		isFirstMove = true;
		gameboard = new int[MAX_SQUARES];
		winningHands = new int[MAX_HANDS];
		buildWinningHands();
		
		for (int j = 0; j < MAX_SQUARES; j++) {
			setImage(startingImage, j);
		}
		
		for (int i = 0; i < MAX_SQUARES; i++) {
			gameSquareButtons[i].setBorder(new LineBorder(Color.BLACK));
		}
	}
	private boolean winner() {
		boolean winner = false;
		int i = 0; 
		
		buildWinningHands();
		
		while (i < MAX_HANDS && !player1Wins && !player2Wins) {
			if (winningHands[i] == PLAYER_1_WIN)
				player1Wins = true;
			else if (winningHands[i] == PLAYER_2_WIN)
				player2Wins = true;
			else
				i++;
		}
		if (player1Wins || player2Wins)
			winner = true;
		return winner;
	}
	private void setBorderColor(Color win, Color lose, Color tie) {
		int winner = player1Wins ? PLAYER_1_WIN : PLAYER_2_WIN;
		Color color = player1Wins ? win : lose, color2 = tie;
		
		if (winner()) {
			if (winningHands[0] == winner) {
				gameSquareButtons[0].setBorder(new LineBorder(color));
				gameSquareButtons[1].setBorder(new LineBorder(color));
				gameSquareButtons[2].setBorder(new LineBorder(color));
			}
			else if (winningHands[1] == winner) {
				gameSquareButtons[3].setBorder(new LineBorder(color));
				gameSquareButtons[4].setBorder(new LineBorder(color));
				gameSquareButtons[5].setBorder(new LineBorder(color));
			}
			else if (winningHands[2] == winner) {
				gameSquareButtons[6].setBorder(new LineBorder(color));
				gameSquareButtons[7].setBorder(new LineBorder(color));
				gameSquareButtons[8].setBorder(new LineBorder(color));
			}
			else if (winningHands[3] == winner) {
				gameSquareButtons[0].setBorder(new LineBorder(color));
				gameSquareButtons[3].setBorder(new LineBorder(color));
				gameSquareButtons[6].setBorder(new LineBorder(color));
			}
			else if (winningHands[4] == winner) {
				gameSquareButtons[1].setBorder(new LineBorder(color));
				gameSquareButtons[4].setBorder(new LineBorder(color));
				gameSquareButtons[7].setBorder(new LineBorder(color));
			}
			else if (winningHands[5] == winner) {
				gameSquareButtons[2].setBorder(new LineBorder(color));
				gameSquareButtons[5].setBorder(new LineBorder(color));
				gameSquareButtons[8].setBorder(new LineBorder(color));
			}
			else if (winningHands[6] == winner) {
				gameSquareButtons[0].setBorder(new LineBorder(color));
				gameSquareButtons[4].setBorder(new LineBorder(color));
				gameSquareButtons[8].setBorder(new LineBorder(color));
			}
			else if (winningHands[7] == winner) {
				gameSquareButtons[2].setBorder(new LineBorder(color));
				gameSquareButtons[4].setBorder(new LineBorder(color));
				gameSquareButtons[6].setBorder(new LineBorder(color));
			}
		}
		else {
			gameSquareButtons[0].setBorder(new LineBorder(color2));
			gameSquareButtons[1].setBorder(new LineBorder(color2));
			gameSquareButtons[2].setBorder(new LineBorder(color2));
			gameSquareButtons[3].setBorder(new LineBorder(color2));
			gameSquareButtons[4].setBorder(new LineBorder(color2));
			gameSquareButtons[5].setBorder(new LineBorder(color2));
			gameSquareButtons[6].setBorder(new LineBorder(color2));
			gameSquareButtons[7].setBorder(new LineBorder(color2));
			gameSquareButtons[8].setBorder(new LineBorder(color2));
		}
	}
	private boolean isGameboardFull() {
		int i = 0; 
		boolean isFull= true;
		
		while (i < MAX_SQUARES && isFull) {
			if (gameboard[i] == 0)
				isFull = false;
			else
				i++;
		}
		return isFull;
	}
	private void makePlayerMove(JButton square) {
		if (isSinglePlayer && !winner())
			playSinglePlayer(square);
		if (!isSinglePlayer && !winner())
			playMultiplayer(square);
		if (winner() || isGameboardFull())
			try {
				displayWinner();
			} catch (LineUnavailableException | IOException | 
					InterruptedException | UnsupportedAudioFileException e) {
				e.printStackTrace();
			}
	}
	private void playMultiplayer(JButton square) {
		int i = 0;
		boolean isButton = false;
		
		while (i < MAX_SQUARES && !isButton) {
			if (gameSquareButtons[i] == square) {
				if (gameboard[i] == 0) {
					if (isPlayer1Move) {
						gameboard[i] = PLAYER_1_MOVE;
						setImage(xImage, i);
						isButton = true;
						isPlayer1Move = false;
					}
					else {
						gameboard[i] = PLAYER_2_MOVE;
						setImage(oImage, i);
						isButton = true;
						isPlayer1Move = true;
					}
				}
			}
			i++;
		}
	}
	private void playSinglePlayer(JButton square) {
		int i = 0;
		boolean isButton = false;
		
		while (i < MAX_SQUARES && !isButton) {
			if (gameSquareButtons[i] == square) {
				if (gameboard[i] == 0) {
					gameboard[i] = PLAYER_1_MOVE;
					setImage(xImage, i);
					isButton = true;
					isPlayer1Move = false;
				}
			}
			i++;
		}
		if (!winner() && !isGameboardFull()) {
			makeComputerMove();
			isFirstMove = false;
		} else
			try {
				displayWinner();
			} catch (LineUnavailableException | IOException | 
					InterruptedException | UnsupportedAudioFileException e) {
				e.printStackTrace();
			}
	}
	private void getComputerMove() {
		boolean isFound = false;
		
		// Finding Winning Move
		if (isRowAvailable(0, 0, 2))
			isFound = true;
		else if (isColAvailable(3, 0, 2) && !isFound)
			isFound = true;
		else if (isDiaAvailable(6, 2, 0, 4, 8) && !isFound)
			isFound = true;
		else if (isDiaAvailable(7, 2, 2, 4, 6) && !isFound) 
			isFound = true;
		// Block Player Move
		else if (isRowAvailable(0, 0, -2) && difficulty != EASY && !isFound)
			isFound = true;
		else if (isColAvailable(3, 0, -2) && difficulty != EASY && !isFound)
			isFound = true;
		else if (isDiaAvailable(6, -2, 0, 4, 8) && difficulty != EASY && !isFound)
			isFound = true;
		else if (isDiaAvailable(7, -2, 2, 4, 6) && difficulty != EASY && !isFound) 
			isFound = true;
		// Find Move Closest to Computer Occupied Square
		else if (isRowAvailable(0, 0, 1) && !isFound)
			isFound = true;
		else if (isColAvailable(3, 0, 1) && !isFound)
			isFound = true;
		else if (isDiaAvailable(6, 1, 0, 4, 8) && !isFound)
			isFound = true;
		else if (isDiaAvailable(7, 1, 2, 4, 6) && !isFound) 
			isFound = true;
		// To Make a Strategic First Move
		else if (isFirstMove && difficulty == HARD && !isFound)
			makeFirstMove();
		// No Good Move, Make Random Move
		else
			makeRandomMove();
	}
	private boolean isRowAvailable(int i, int j, int points) {
		boolean isAvailable = false;
		
		while (i < 3 && !isAvailable) {
			if (winningHands[i] == points) {
				if (gameboard[j] == 0) {
					computerMove = j;
					isAvailable = true;
				}
				else if (gameboard[j + 1] == 0) {
					computerMove = j + 1;
					isAvailable = true;
				}
				else if (gameboard[j + 2] == 0) {
					computerMove = j + 2;
					isAvailable = true;
				}
			}
			j += 3;
			i++;
		}
		return isAvailable;
	}
	private boolean isColAvailable(int i, int j, int points) {
		boolean isAvailable = false;
		
		while (i < 6 && !isAvailable) {
			if (winningHands[i] == points) {
				if (gameboard[j] == 0) {
					computerMove = j;
					isAvailable = true;
				}
				else if (gameboard[j + 3] == 0) {
					computerMove = j + 3;
					isAvailable = true;
				}
				else if (gameboard[j + 6] == 0) {
					computerMove = j + 6;
					isAvailable = true;
				}
			}
			j++;
			i++;
		}
		return isAvailable;
	}
	private boolean isDiaAvailable(int i, int points, int a, int b, int c) {
		boolean isAvailable = false;
		
		if (winningHands[i] == points) {
			if (gameboard[a] == 0) {
				computerMove = a;
				isAvailable = true;
			}
			else if (gameboard[b] == 0) {
				computerMove = b;
				isAvailable = true;
			}
			else if (gameboard[c] == 0) {
				computerMove = c;
				isAvailable = true;
			}
		}
		
		return isAvailable;
	}
	private void makeFirstMove() {
		if (gameboard[0] == -1) {
			computerMove = 4;
		}
		else if (gameboard[8] == -1) {
			computerMove = 4;
		}
		else if (gameboard[2] == -1) {
			computerMove = 4;
		}
		else if (gameboard[6] == -1) {
			computerMove = 4;
		}
		else if (gameboard[4] == -1) {
			boolean isFound = false;
			int square;
			
			while (!isFound) {
				square = new Random().nextInt(9) + 1;
				if (square == 0 || square == 2 || square == 6 || square == 8) {
					computerMove = square;
					isFound = true;
				}
			}
		}
	}
	private void makeRandomMove() {
		boolean isFound = false;
		
		if (!isFound) {
			int square = new Random().nextInt(MAX_SQUARES - 1) + 1;
			while (!isFound) {
				square = new Random().nextInt(MAX_SQUARES - 1) + 1;
				if (gameboard[square] == 0) {
					computerMove = square;
					isFound = true;
				}
			}	
		}
	}
	private void makeComputerMove() {
		getComputerMove();
		int wait = (new Random().nextInt(3) + 1) * 1000;
		timer = new Timer();
		try {
			timer.schedule(new TimerListener(), wait);
		}
		catch (IllegalArgumentException | IllegalStateException | NullPointerException e) {
		}
	}
	private class TimerListener extends TimerTask {
		@Override
		public void run() {
			gameboard[computerMove] = PLAYER_2_MOVE;
			setImage(oImage, computerMove);
			isPlayer1Move = true;
			timer.cancel();
			timer.purge();
			if (winner() || isGameboardFull())
				try {
					displayWinner();
				} catch (LineUnavailableException | IOException | 
						InterruptedException| UnsupportedAudioFileException e) {
					e.printStackTrace();
				}
		}
	}
	private void displayWinner() throws LineUnavailableException, IOException, InterruptedException, UnsupportedAudioFileException {
		int option;
		String noise = WINNING_NOISE;

		if (isSinglePlayer) {
			if (player1Wins)
				noise = WINNING_NOISE;
			else
				noise = LOSING_NOISE;
		}
		
		setBorderColor();
		
		if (winner()) {
			playAudio(noise);
			option = JOptionPane.showConfirmDialog(null, (player1Wins ? xText + " Wins!" : oText + " Wins!"), 
					"Game Results", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, 
					(player1Wins ? getIcon(xImage) : getIcon(oImage)));
			if (option == 0)
				resetGame();
		}
		else if (isGameboardFull()) {
			playAudio(LOSING_NOISE);
			option = JOptionPane.showConfirmDialog(null, "CAT'S GAME!", "Game Results", 
					JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, getIcon(CATS_GAME));
			if (option == 0)
				resetGame();
		}
	}
	private void setBorderColor() {
		// Normal
		if (gameMode == 0)
			setBorderColor(Color.GREEN, Color.RED, Color.ORANGE);
		// Jacob and Jes
		else if (gameMode == 1)
			setBorderColor(Color.YELLOW, Color.GREEN, Color.PINK);
		// Pokemon
		else if (gameMode == 2) 
			setBorderColor(Color.GREEN, Color.RED, Color.ORANGE);
		// Cyber
		else if (gameMode == 3)
			setBorderColor(Color.WHITE, Color.CYAN, Color.YELLOW);
			
	}
	private class ButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				if (isPlayer1Move && isSinglePlayer) {
					playAudio(BUTTON_NOISE);
					makePlayerMove((JButton) e.getSource());
				}
				else if (!isSinglePlayer) {
					playAudio(BUTTON_NOISE);
					makePlayerMove((JButton) e.getSource());
				}
			} catch (LineUnavailableException | IOException | 
					 InterruptedException | UnsupportedAudioFileException e1) {
				System.out.println(e1.getMessage());
			}
		}
	}
	private class MenuItemListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {

			if (menuItems[0] == e.getSource()) {
				isSinglePlayer = true;
				setTitle("Tic-Tac-Toe (Single Player Mode: EASY)");
				resetGame();
			}
			else if (menuItems[1] == e.getSource()) {
				isSinglePlayer = false;
				setTitle("Tic-Tac-Toe (Multiplayer Mode)");
				resetGame();
			}
			else if (menuItems[2] == e.getSource()) {
				setGameMode(NORMAL, "X", "Y", Color.YELLOW, Color.DARK_GRAY);
				gameMode = 0;
			}
			else if (menuItems[3] == e.getSource()) {
				setGameMode(JACOB_AND_JESICA, "Jesica", "Jacob", Color.YELLOW, Color.DARK_GRAY);
				gameMode = 1;
			}
			else if (menuItems[4] == e.getSource()) {
				setGameMode(POKEMON, "Togepi", "Squirtle", Color.YELLOW, Color.DARK_GRAY);
				gameMode = 2;
			}
			else if (menuItems[5] == e.getSource()) {
				setGameMode(COMPUTER, "Hacker", "Computer", Color.BLACK, Color.BLACK);
				gameMode = 3;
			}
			else if (menuItems[6] == e.getSource()) {
				System.exit(0);
			}
			else if (menuItems[7] == e.getSource()) {
				setTitle("Tic-Tac-Toe (Single Player Mode: EASY)");
				difficulty = EASY;
			}
			else if (menuItems[8] == e.getSource()) {
				setTitle("Tic-Tac-Toe (Single Player Mode: MODERATE)");
				difficulty = MODERATE;
			}
			else if (menuItems[9] == e.getSource()) {
				setTitle("Tic-Tac-Toe (Single Player Mode: HARD)");
				difficulty = HARD;
			}
		}
	}
	private void setGameMode(String[] style, String xText, String oText, 
            Color frameColor, Color panelColor) {
		this.xText = xText;
		this.oText = oText;
		setBackground(frameColor);
		gameBoardPanel.setBackground(panelColor);
		setImage(style[0], style[1], style[2]);
		
		for (int i = 0; i < MAX_SQUARES; i++) {
			if (gameboard[i] == -1)
				setImage(xImage, i);
			else if (gameboard[i] == 1)
				setImage(oImage, i);
			else
				setImage(style[2], i);
		}
		//resetGame();
}
	public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		new TicTacToeGame();
	}
}