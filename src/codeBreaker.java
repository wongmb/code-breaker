/*===================================================================================================================================
Code Breaker
Michael Wong
June 3, 2023
Java
=====================================================================================================================================
Problem Definition – Required to make code breaker game in java GUI. 

Option 1 where the computer can set the code and provides hints for the user to guess the code.

Option 2 where the user could set the code, and the computer should be given the opportunity to guess the code and the user should provide the hints.

Input: Main menu options, different colours (as pegs), 

Output: Display AI guessing code, past guess and clues history, current colours input, display GUI, along with statistics and settings

Process: Varies depending on option

Option 1: The computer will set a code by randomly selecting colours from a colour array, then setting a 
code with those colours. The GUI will get input from buttons to determine what the user inputs as guesses, 
then the computer will determine if there are any black and white pegs.

Option 2: The user will think of a code in their head, and the AI will try to guess it based on the black 
and white pegs that the user inputs to what the AI guesses. Based on a algorithm, the AI will then remove 
all codes that don't fit accordingly, and then guess a code that remains and fits.

=====================================================================================================================================
List of Identifiers - Listed in methods
=====================================================================================================================================*/

import java.io.*;
import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class codeBreaker extends JFrame implements ActionListener {
	// Declare global variables
	static int totalGuessesMade = 0, totalGamesPlayed = 0, wins = 0, losses = 0, size = 4, tries = 10;
	static int numberOfColours = 6, tempSize = 4, tempNumColours = 6, tempTries = 10, numTries = 0;
	static int numBlackPegs = 0, numWhitePegs = 0, codeGuesser = 0, totalPegs = 0, currentGuessCounter = 0;

	static String code = "", codeGuess = "", colours = "", computerGuess = "";

	static int[] stats = new int[4], pegs = new int[2];
	static final String[] COLOURS = { "R", "G", "B", "O", "P", "Y" };

	static boolean tempShowLetters = true, validGuess = true, showLetters = true;
	static boolean deleteGuess = false, gaveUp = false, noMoreCodes = false, AICurrentlyGuessing = false;
	
	static int statShown = 0;

	static String[][] pastGuesses = new String[tries][size], displayColours = new String[tries][size],
			pastClues = new String[tries][size];
	static String[] codeGuessArray = new String[size], codeArray = new String[size],
			coloursArray = new String[numberOfColours], outcome = new String[size];

	static List<String> possibleCodes = generateAllPossibleCodes();
	static codeBreaker obj = new codeBreaker();
	static Random random = new Random();
	static FlowLayout flowLayout = new FlowLayout();
	static JButton plusNumTries, plusNumColours, plusCodeSize, minusNumTries, minusNumColours, minusCodeSize;
	static JPanel codeSetterPanel;
	static JFrame displayGameFrame;
	static Border blackLine = BorderFactory.createLineBorder(Color.black, 4, true);
	static JButton blueButton, greenButton, orangeButton, purpleButton, redButton, yellowButton, blackButton,
			whiteButton;

	/**
	 * displayGameFrame method: This procedural method creates and displays the
	 * JFrame that shows the entire game
	 *
	 */
	public static void displayGameFrame() {

		// Create frame
		displayGameFrame = new JFrame("");
		CardLayout cardLayout = new CardLayout();

		displayGameFrame.setSize(1200, 600);
		displayGameFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		displayGameFrame.setUndecorated(false);

		displayGameFrame.setTitle("Code Breaker");

		displayGameFrame.setLayout(cardLayout);

		// Display frame

		displayGameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		displayGameFrame.setVisible(true);

	}

	/**
	 * editDisplayGameFrame method: This procedural method will edit the
	 * displayGameFrame to display the GUI for the actual game (guessing
	 * colours, number of black and white pegs). It will change what is being
	 * displayed based on certain buttons entered.
	 *
	 */

	public static void editDisplayGameFrame() {
		writeFile(); // Calling the writeFile method

		// Creating the main panel for the entire game
		JPanel entireGamePanel = new JPanel();
		entireGamePanel.setLayout(new BoxLayout(entireGamePanel, BoxLayout.PAGE_AXIS));

		// Creating the panel for the end of the game message
		JPanel gameEndPanel = new JPanel();
		gameEndPanel.setLayout(new BoxLayout(gameEndPanel, BoxLayout.PAGE_AXIS));
		gameEndPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Creating the main game panel
		JPanel gamePanel = new JPanel();

		// Creating additional panels for colors, past clues, and number of
		// tries
		JPanel colours = new JPanel();
		JPanel pastCluesPanel = new JPanel();
		JPanel numTriesPanel = new JPanel();

		// Creating layouts for past clues and number of tries panels
		GridLayout pastCluesLayout = new GridLayout(tries, size);
		GridLayout numTriesLayout = new GridLayout(tries, 1);
		GridLayout pastGuessesLayout = new GridLayout(tries, size);

		// Creating a panel for the current set of colors
		JPanel currentColours = new JPanel();

		// Setting layouts for various panels
		gamePanel.setLayout(flowLayout);
		colours.setLayout(flowLayout);
		numTriesPanel.setLayout(numTriesLayout);
		currentColours.setLayout(pastGuessesLayout);
		pastCluesPanel.setLayout(pastCluesLayout);

		// Creating panels for the secret code and mini menu
		JPanel secretCode = new JPanel();
		JPanel miniMenu = new JPanel();

		// Creating labels for player and AI guessing
		JLabel playerGuessing = new JLabel("Player Guessing");
		JLabel computerGuessing = new JLabel("AI Guessing");

		playerGuessing.setAlignmentX(Component.CENTER_ALIGNMENT);
		computerGuessing.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Creating buttons for the mini menu
		JButton menuButton = new JButton("Menu");
		JButton giveUpButton = new JButton("Give Up");
		JButton howToPlayButton = new JButton("How To Play?");
		giveUpButton.setForeground(Color.WHITE);
		giveUpButton.setBackground(Color.RED);
		giveUpButton.setFont(new Font("Arial", Font.PLAIN, 15));
		howToPlayButton.setFont(new Font("Arial", Font.PLAIN, 15));

		if (gaveUp) {
			giveUpButton.setBackground(Color.gray);
		}

		JButton newGameButton = new JButton("New Game");
		JButton settingsButton = new JButton("Settings");
		JButton statsButton = new JButton("Statistics");

		// Adding buttons to the mini menu panel
		miniMenu.add(menuButton);
		miniMenu.add(newGameButton);
		miniMenu.add(settingsButton);
		miniMenu.add(statsButton);
		miniMenu.add(howToPlayButton);

		// Setting fonts for the buttons in the mini menu
		menuButton.setFont(new Font("Arial", Font.PLAIN, 15));
		newGameButton.setFont(new Font("Arial", Font.PLAIN, 15));
		settingsButton.setFont(new Font("Arial", Font.PLAIN, 15));
		statsButton.setFont(new Font("Arial", Font.PLAIN, 15));

		// Adding action listeners to the buttons in the mini menu
		menuButton.addActionListener(obj);
		newGameButton.addActionListener(obj);
		settingsButton.addActionListener(obj);
		statsButton.addActionListener(obj);
		howToPlayButton.addActionListener(obj);

		// Setting up the layout and adding components for the secret code panel
		secretCode.setLayout(flowLayout);

		JLabel secretCodeDisplay = new JLabel("Secret Code: ");

		secretCode.add(secretCodeDisplay);

		// Checking conditions for code guesser and displaying appropriate game
		// end
		// messages
		if (codeGuesser == 2) {
			// Checking if player wins
			if (numBlackPegs == size) {
				JLabel gameEndLabel = new JLabel("You Win! Click New Game To Play Again.");
				if (statShown == 0) {
					statShown = 1;
					totalGamesPlayed++;
					wins++;
				}

				System.out.println("total games played " + totalGamesPlayed);
				System.out.println("wins: " + wins);

				// Setting the font for the game end label
				gameEndLabel.setFont(new Font("Arial", Font.ITALIC, 15));

				// Adding the game end label to the game end panel
				gameEndPanel.add(gameEndLabel);

			}

			// Checking if the player loses due to running out of tries or
			// giving up
			else if (numTries == tries || gaveUp) {
				JLabel gameEndLabel = new JLabel("You Lose. Click New Game To Play Again.");

				// Setting the font for the game end label
				gameEndLabel.setFont(new Font("Arial", Font.ITALIC, 15));

				// Adding the game end label to the game end panel
				gameEndPanel.add(gameEndLabel);

				if (statShown == 0) {
					statShown = 1;
					losses++;
					totalGamesPlayed++;
				}
			}

			// Adding the give up button to the mini menu panel
			miniMenu.add(giveUpButton);
			giveUpButton.addActionListener(obj);

			// Creating and setting up buttons for colors and check/delete
			// actions
			if (showLetters) {
				blueButton = new JButton("B");
				greenButton = new JButton("G");
				orangeButton = new JButton("O");
				purpleButton = new JButton("P");
				redButton = new JButton("R");
				yellowButton = new JButton("Y");
			} else {
				blueButton = new JButton("   ");
				greenButton = new JButton("   ");
				orangeButton = new JButton("   ");
				purpleButton = new JButton("   ");
				redButton = new JButton("   ");
				yellowButton = new JButton("   ");
			}

			JButton check = new JButton("Check");
			JButton delete = new JButton("Delete");

			// Setting the background colors for the colour buttons
			blueButton.setBackground(Color.blue);
			greenButton.setBackground(Color.green);
			orangeButton.setBackground(Color.orange);
			purpleButton.setBackground(Color.MAGENTA);
			redButton.setBackground(Color.red);
			yellowButton.setBackground(Color.yellow);

			// Adding colour buttons to the colors panel
			for (int i = 0; i < numberOfColours; i++) {
				if (i == 0) {
					colours.add(blueButton);
				} else if (i == 1) {
					colours.add(greenButton);
				} else if (i == 2) {
					colours.add(orangeButton);
				} else if (i == 3) {
					colours.add(purpleButton);
				} else if (i == 4) {
					colours.add(redButton);
				} else if (i == 5) {
					colours.add(yellowButton);
				}
			}

			// Adding check and delete buttons to the colors panel
			colours.add(check);
			colours.add(delete);

			// Adding action listeners to the buttons
			if (numBlackPegs != size && gaveUp == false) {
				blueButton.addActionListener(obj);
				greenButton.addActionListener(obj);
				orangeButton.addActionListener(obj);
				purpleButton.addActionListener(obj);
				redButton.addActionListener(obj);
				yellowButton.addActionListener(obj);
			}
			check.addActionListener(obj);
			delete.addActionListener(obj);
			delete.setForeground(Color.WHITE);
			delete.setBackground(Color.RED);
		} else {
			// Creating and setting up buttons for black and white pegs, enter,
			// and delete
			// actions
			if (showLetters) {
				blackButton = new JButton("B");
				whiteButton = new JButton("W");
			} else {
				blackButton = new JButton("   ");
				whiteButton = new JButton("   ");
			}

			JButton enter = new JButton("Enter");
			JButton delete = new JButton("Delete");

			// Adding action listeners to the buttons
			blackButton.addActionListener(obj);
			whiteButton.addActionListener(obj);
			enter.addActionListener(obj);
			delete.addActionListener(obj);

			// Adding buttons to the colors panel
			colours.add(blackButton);
			colours.add(whiteButton);
			colours.add(enter);
			colours.add(delete);

			// Setting the background colors for the buttons
			blackButton.setBackground(Color.BLACK);
			blackButton.setForeground(Color.WHITE);
			whiteButton.setBackground(Color.WHITE);
			delete.setForeground(Color.WHITE);
			delete.setBackground(Color.RED);
		}

		// Creating labels for each colour
		JLabel redLabel;
		JLabel orangeLabel;
		JLabel greenLabel;
		JLabel purpleLabel;
		JLabel blueLabel;
		JLabel yellowLabel;

		if (gaveUp == true || numBlackPegs == size || numTries >= tries) {
			// Displaying the secret code when the game ends
			for (int i = 0; i < size; i++) {
				if (codeArray[i].equals("R")) {
					// Creating and setting up a red label for the secret code
					if (showLetters) {
						redLabel = new JLabel(" R ");
					} else {
						redLabel = new JLabel("     ");
					}

					redLabel.setOpaque(true);
					redLabel.setBackground(Color.red);
					secretCode.add(redLabel);
					redLabel.setBorder(blackLine);

				} else if (codeArray[i].equals("G")) {
					// Creating and setting up a green label for the secret code
					if (showLetters) {
						greenLabel = new JLabel(" G ");
					} else {
						greenLabel = new JLabel("     ");
					}

					greenLabel.setOpaque(true);
					greenLabel.setBackground(Color.green);
					secretCode.add(greenLabel);
					greenLabel.setBorder(blackLine);

				} else if (codeArray[i].equals("O")) {
					// Creating and setting up an orange label for the secret
					// code
					if (showLetters) {
						orangeLabel = new JLabel(" O ");
					} else {
						orangeLabel = new JLabel("     ");
					}

					orangeLabel.setOpaque(true);
					orangeLabel.setBackground(Color.orange);
					orangeLabel.setBorder(blackLine);
					secretCode.add(orangeLabel);

				} else if (codeArray[i].equals("P")) {
					// Creating and setting up a purple label for the secret
					// code
					if (showLetters) {
						purpleLabel = new JLabel(" P ");
					} else {
						purpleLabel = new JLabel("     ");
					}

					purpleLabel.setBackground(Color.MAGENTA);
					purpleLabel.setOpaque(true);
					purpleLabel.setBorder(blackLine);
					secretCode.add(purpleLabel);

				} else if (codeArray[i].equals("B")) {
					// Creating and setting up a blue label for the secret code
					if (showLetters) {
						blueLabel = new JLabel(" B ");
					} else {
						blueLabel = new JLabel("     ");
					}

					blueLabel.setBackground(Color.blue);
					secretCode.add(blueLabel);
					blueLabel.setOpaque(true);
					blueLabel.setBorder(blackLine);

				} else if (codeArray[i].equals("Y")) {
					// Creating and setting up a yellow label for the secret
					// code
					if (showLetters) {
						yellowLabel = new JLabel(" Y ");
					} else {
						yellowLabel = new JLabel("     ");
					}

					yellowLabel.setBackground(Color.yellow);
					secretCode.add(yellowLabel);
					yellowLabel.setOpaque(true);
					yellowLabel.setBorder(blackLine);

				}
			}
		} else {
			// Displaying question mark labels for the secret code during the
			// game
			for (int i = 0; i < size; i++) {
				JLabel questionMarkLabel = new JLabel(" ? ");
				questionMarkLabel.setBorder(blackLine);
				secretCode.add(questionMarkLabel);
			}
		}

		// Creating a label for the number of tries
		JLabel numTry;

		for (int i = 0; i < tries; i++) {
			// Creating a label for the number of tries
			if (tries >= 10) {
				numTry = new JLabel(" " + String.valueOf(i + 1));
			} else {
				numTry = new JLabel(" " + String.valueOf(i + 1) + " ");
			}

			// Adjusting the label for two-digit tries
			if (i >= 9) {
				numTry = new JLabel(String.valueOf(i + 1));
			}
			numTry.setBorder(blackLine);

			// Setting background colour for the current try in the player
			// guessing mode
			if (i <= numTries && codeGuesser == 2) {
				numTry.setBackground(Color.CYAN);
				numTry.setOpaque(true);

				// Clearing the background colour if the code has been correctly
				// guessed
				if (numBlackPegs == size && i == numTries) {
					numTry.setOpaque(false);
				}
			}

			// Setting background colour for previous tries in the AI guessing
			// mode
			if (codeGuesser == 1) {
				if (i < numTries) {
					numTry.setBackground(Color.CYAN);
					numTry.setOpaque(true);

					// Clearing the background colour if the code has been
					// correctly guessed
					if (numBlackPegs == size && i == numTries) {
						numTry.setOpaque(false);
					}
				}
			}

			numTriesPanel.add(numTry);

			JLabel whitePeg;
			JLabel blackPeg;
			for (int j = 0; j < size; j++) {
				if (pastClues[i][j].equals("w")) {
					// Creating and setting up a white peg label for a white
					// clue
					if (showLetters) {
						whitePeg = new JLabel(" W ");
					} else {
						whitePeg = new JLabel("     ");
					}

					whitePeg.setOpaque(true);
					whitePeg.setBorder(blackLine);
					whitePeg.setBackground(Color.WHITE);
					pastCluesPanel.add(whitePeg);

				} else if (pastClues[i][j].equals("b")) {
					// Creating and setting up a black peg label for a black
					// clue
					if (showLetters) {
						blackPeg = new JLabel(" B ");
					} else {
						blackPeg = new JLabel("     ");
					}

					blackPeg.setOpaque(true);
					blackPeg.setBorder(blackLine);
					blackPeg.setBackground(Color.BLACK);
					blackPeg.setForeground(Color.WHITE);
					pastCluesPanel.add(blackPeg);

				} else {
					// Adding a blank label for no clue
					JLabel blank = new JLabel("     ");
					pastCluesPanel.add(blank);
					blank.setBorder(blackLine);
				}
			}
		}

		// Updating the displayColours array with the current code guess
		if (!codeGuess.equals("")) {
			if (codeGuesser == 2) {
				// Updating for player guessing mode
				for (int i = 0; i < codeGuessArray.length; i++) {
					if (codeGuessArray[i].equals("R")) {
						displayColours[numTries][i] = "R";
					} else if (codeGuessArray[i].equals("G")) {
						displayColours[numTries][i] = "G";
					} else if (codeGuessArray[i].equals("O")) {
						displayColours[numTries][i] = "O";
					} else if (codeGuessArray[i].equals("B")) {
						displayColours[numTries][i] = "B";
					} else if (codeGuessArray[i].equals("P")) {
						displayColours[numTries][i] = "P";
					} else if (codeGuessArray[i].equals("Y")) {
						displayColours[numTries][i] = "Y";
					}
				}
			} else {
				// Updating for AI guessing mode
				for (int i = 0; i < codeGuessArray.length; i++) {
					if (codeGuessArray[i].equals("R")) {
						displayColours[numTries - 1][i] = "R";
					} else if (codeGuessArray[i].equals("G")) {
						displayColours[numTries - 1][i] = "G";
					} else if (codeGuessArray[i].equals("O")) {
						displayColours[numTries - 1][i] = "O";
					} else if (codeGuessArray[i].equals("B")) {
						displayColours[numTries - 1][i] = "B";
					} else if (codeGuessArray[i].equals("P")) {
						displayColours[numTries - 1][i] = "P";
					} else if (codeGuessArray[i].equals("Y")) {
						displayColours[numTries - 1][i] = "Y";
					}
				}
			}
		}

		// Clearing the current guess if delete button is pressed
		if (deleteGuess) {
			for (int i = 0; i < currentGuessCounter; i++) {
				displayColours[numTries][i] = "";
			}
			currentGuessCounter = 0;
		}

		for (int i = 0; i < displayColours.length; i++) {
			for (int j = 0; j < displayColours[i].length; j++) {
				if (displayColours[i][j].equals("R")) {
					// Creating and setting up a red label for the color "R"
					if (showLetters) {
						redLabel = new JLabel(" R ");
					} else {
						redLabel = new JLabel("     ");
					}

					redLabel.setOpaque(true);
					redLabel.setBackground(Color.red);
					currentColours.add(redLabel);
					redLabel.setBorder(blackLine);

				} else if (displayColours[i][j].equals("G")) {
					// Creating and setting up a green label for the color "G"
					if (showLetters) {
						greenLabel = new JLabel(" G ");
					} else {
						greenLabel = new JLabel("     ");
					}

					greenLabel.setOpaque(true);
					greenLabel.setBackground(Color.green);
					currentColours.add(greenLabel);
					greenLabel.setBorder(blackLine);

				} else if (displayColours[i][j].equals("O")) {
					// Creating and setting up an orange label for the color "O"
					if (showLetters) {
						orangeLabel = new JLabel(" O ");
					} else {
						orangeLabel = new JLabel("     ");
					}

					orangeLabel.setOpaque(true);
					orangeLabel.setBackground(Color.orange);
					orangeLabel.setBorder(blackLine);
					currentColours.add(orangeLabel);

				} else if (displayColours[i][j].equals("P")) {
					// Creating and setting up a purple label for the color "P"
					if (showLetters) {
						purpleLabel = new JLabel(" P ");
					} else {
						purpleLabel = new JLabel("     ");
					}

					purpleLabel.setBackground(Color.MAGENTA);
					purpleLabel.setOpaque(true);
					purpleLabel.setBorder(blackLine);
					currentColours.add(purpleLabel);

				} else if (displayColours[i][j].equals("B")) {
					// Creating and setting up a blue label for the color "B"
					if (showLetters) {
						blueLabel = new JLabel(" B ");
					} else {
						blueLabel = new JLabel("     ");
					}

					blueLabel.setBackground(Color.blue);
					currentColours.add(blueLabel);
					blueLabel.setOpaque(true);
					blueLabel.setBorder(blackLine);

				} else if (displayColours[i][j].equals("Y")) {
					// Creating and setting up a yellow label for the color "Y"
					if (showLetters) {
						yellowLabel = new JLabel(" Y ");
					} else {
						yellowLabel = new JLabel("     ");
					}

					yellowLabel.setBackground(Color.yellow);
					currentColours.add(yellowLabel);
					yellowLabel.setOpaque(true);
					yellowLabel.setBorder(blackLine);

				} else {
					// Adding a blank label for no color
					JLabel blank = new JLabel("     ");
					currentColours.add(blank);
					blank.setBorder(blackLine);
				}
			}
		}

		// Edit displayGameFrame to display different things
		displayGameFrame.getContentPane().removeAll();

		// Checking conditions for displaying the game end panel or invalid
		// guess message
		if (numBlackPegs == size || numTries >= tries || noMoreCodes || gaveUp) {
			entireGamePanel.add(gameEndPanel);
		} else if (validGuess == false) {
			JLabel guess = new JLabel("Please enter " + size + " colours.");
			guess.setFont(new Font("Arial", Font.ITALIC, 15));
			entireGamePanel.add(guess);
			guess.setAlignmentX(Component.CENTER_ALIGNMENT);
		}

		JLabel gameEndLabel;
		if (codeGuesser == 1 && AICurrentlyGuessing == false) {
			if (numBlackPegs == size) {
				gameEndLabel = new JLabel("The Computer Has Cracked The Code!");
				gameEndPanel.add(gameEndLabel);
			} else {
				gameEndLabel = new JLabel("There Are No More Codes Available.");
				gameEndPanel.add(gameEndLabel);
			}
			gameEndLabel.setFont(new Font("Arial", Font.ITALIC, 15));
		}

		gamePanel.add(numTriesPanel);
		gamePanel.add(currentColours);
		gamePanel.add(pastCluesPanel);

		entireGamePanel.add(miniMenu);

		if (codeGuesser == 1) {
			entireGamePanel.add(computerGuessing);
		} else {
			entireGamePanel.add(playerGuessing);
		}

		entireGamePanel.add(colours);
		entireGamePanel.add(gamePanel);

		if (codeGuesser == 2) {
			entireGamePanel.add(secretCode);
		}

		displayGameFrame.add(entireGamePanel);

		displayGameFrame.revalidate();

	}

	// Gets input from buttons
	public void actionPerformed(ActionEvent event) {
		String button = event.getActionCommand();

		// Play button pressed
		if (button.equals("Play")) {
			codeSetterPanel();
		}
		// Settings button pressed
		else if (button.equals("Settings")) {
			settingsPanel();
		}
		// Give Up button pressed and game is not already won
		else if (button.equals("Give Up") && numBlackPegs != size) {
			gaveUp = true;
			editDisplayGameFrame();
		}
		// Exit button pressed
		else if (button.equals("Exit")) {
			System.exit(0);
		}
		// AI Guesses button pressed
		else if (button.equals("AI Guesses")) {
			codeGuesser = 1;
			AICurrentlyGuessing = true;

			codeGuess = possibleCodes.get(random.nextInt(possibleCodes.size()));
			codeGuessArray = codeGuess.split("");

			findFullyCorrect(codeGuessArray, codeArray);
			display(outcome, codeGuessArray);

			editDisplayGameFrame();
		}
		// Menu button pressed
		else if (button.equals("Menu")) {
			menuPanel();
		}
		// New Game button pressed
		else if (button.equals("New Game")) {
			codeSetterPanel();
		}
		// Player Guesses button pressed
		else if (button.equals("Player Guesses")) {
			codeGuesser = 2;

			// Set the available colors based on the number of colors chosen
			if (numberOfColours == 2) {
				colours = "BG";
			} else if (numberOfColours == 3) {
				colours = "BGO";
			} else if (numberOfColours == 4) {
				colours = "BGOP";
			} else if (numberOfColours == 5) {
				colours = "BGOPR";
			} else if (numberOfColours == 6) {
				colours = "BGOPRY";
			}

			coloursArray = colours.split("");

			// Create the secret code
			codeArray = createCode(colours, colours.length(), codeArray, coloursArray);
			codeGuessArray = new String[coloursArray.length];
			codeArray = code.split("");

			editDisplayGameFrame();
		}
		// Color buttons pressed when player is guessing
		else if (codeGuesser == 2 && (codeGuess.length() < size)
				&& (event.getSource().equals(redButton) || event.getSource().equals(yellowButton)
						|| event.getSource().equals(purpleButton) || event.getSource().equals(orangeButton)
						|| event.getSource().equals(greenButton) || event.getSource().equals(blueButton))) {

			// Add the selected color to the code guess
			if (event.getSource().equals(redButton)) {
				codeGuess += "R";
			} else if (event.getSource().equals(blueButton)) {
				codeGuess += "B";
			} else if (event.getSource().equals(greenButton)) {
				codeGuess += "G";
			} else if (event.getSource().equals(orangeButton)) {
				codeGuess += "O";
			} else if (event.getSource().equals(purpleButton)) {
				codeGuess += "P";
			} else if (event.getSource().equals(yellowButton)) {
				codeGuess += "Y";
			}
			codeGuessArray = codeGuess.split("");

			currentGuessCounter++;
			editDisplayGameFrame();
		}
		// Check button pressed
		else if (button.equals("Check")) {
			if (codeGuessArray.length == size) {
				validGuess = true;
				currentGuessCounter = 0;
				totalGuessesMade++;

				for (int i = 0; i < outcome.length; i++) {
					outcome[i] = " ";
				}
				findFullyCorrect(codeGuessArray, codeArray);
				display(outcome, codeGuessArray);
				codeGuess = "";
				codeGuessArray = codeGuess.split("");
			} else {
				validGuess = false;
			}

			editDisplayGameFrame();
		}
		// Delete button pressed when player is guessing
		else if (button.equals("Delete") && codeGuesser == 2 && numBlackPegs != size) {
			deleteGuess = true;
			codeGuess = "";
			codeGuessArray = codeGuess.split("");
			editDisplayGameFrame();
			deleteGuess = false;
		}
		// Enter button pressed when AI is guessing
		else if (button.equals("Enter") && AICurrentlyGuessing == true && numBlackPegs != size && numTries < tries) {
			possibleCodes = narrowDownPossibilities(possibleCodes, codeGuess, numBlackPegs, numWhitePegs);

			try {
				codeGuess = possibleCodes.get(random.nextInt(possibleCodes.size()));
			} catch (IllegalArgumentException e) {
				System.out.println("No more codes found");
				AICurrentlyGuessing = false;
				noMoreCodes = true;
			}
			codeGuessArray = codeGuess.split("");

			totalPegs = 0;
			numBlackPegs = 0;
			numWhitePegs = 0;

			findFullyCorrect(codeGuessArray, codeArray);
			display(outcome, codeGuessArray);

			editDisplayGameFrame();
		}
		// Enter button pressed when AI has won or gave up
		else if (button.equals("Enter") && numBlackPegs == size && AICurrentlyGuessing) {
			AICurrentlyGuessing = false;
			editDisplayGameFrame();
		}
		// Black button pressed when AI is guessing
		else if (event.getSource().equals(blackButton) && AICurrentlyGuessing && totalPegs < size) {
			pastClues[numTries - 1][totalPegs] = "b";
			totalPegs++;
			numBlackPegs++;

			editDisplayGameFrame();
		}
		// White button pressed when AI is guessing
		else if (event.getSource().equals(whiteButton) && totalPegs < size && AICurrentlyGuessing) {
			pastClues[numTries - 1][totalPegs] = "w";
			totalPegs++;
			numWhitePegs++;

			editDisplayGameFrame();
		}
		// Plus button pressed for increasing tries in settings
		else if (event.getSource().equals(plusNumTries) && tempTries < 10) {
			tempTries++;
			settingsPanel();
		}
		// Minus button pressed for decreasing tries in settings
		else if (event.getSource().equals(minusNumTries) && tempTries > 1) {
			tempTries--;
			settingsPanel();
		}
		// Plus button pressed for increasing number of colors in settings
		else if (event.getSource().equals(plusNumColours) && tempNumColours < 6) {
			tempNumColours++;
			settingsPanel();
		}
		// Plus button pressed for increasing code size in settings
		else if (event.getSource().equals(plusCodeSize) && tempSize < 8) {
			tempSize++;
			settingsPanel();
		}
		// Minus button pressed for decreasing number of colors in settings
		else if (event.getSource().equals(minusNumColours) && tempNumColours > 2) {
			tempNumColours--;
			settingsPanel();
		}
		// Minus button pressed for decreasing code size in settings
		else if (event.getSource().equals(minusCodeSize) && tempSize > 1) {
			tempSize--;
			settingsPanel();
		}
		// Delete button pressed when AI is guessing
		else if (button.equals("Delete") && codeGuesser == 1 && AICurrentlyGuessing == true) {
			for (int i = 0; i < totalPegs; i++) {
				pastClues[numTries - 1][i] = "";
			}
			totalPegs = 0;
			numBlackPegs = 0;
			numWhitePegs = 0;

			editDisplayGameFrame();
		}
		// v/ button pressed for showing letters in settings
		else if (button.equals("v/")) {
			tempShowLetters = false;
			settingsPanel();
		}
		// X button pressed for showing colors in settings
		else if (button.equals("X")) {
			tempShowLetters = true;
			settingsPanel();
		}
		// Restore Default button pressed in settings
		else if (button.equals("Restore Default")) {
			tempShowLetters = true;
			tempSize = 4;
			tempNumColours = 6;
			tempTries = 10;
			settingsPanel();
		}
		// Cancel button pressed in settings
		else if (button.equals("Cancel")) {
			tempSize = size;
			tempTries = tries;
			tempNumColours = numberOfColours;
			tempShowLetters = showLetters;

			if (codeGuesser == 0) {
				menuPanel();
			} else {
				editDisplayGameFrame();
			}
		}
		// Back button pressed in settings or how to play panel
		else if (button.equals("Back")) {
			if (codeGuesser == 0) {
				menuPanel();
			} else {
				editDisplayGameFrame();
			}
		}
		// How To Play? button pressed in menu
		else if (button.equals("How To Play?")) {
			howToPlayPanel();
		}
		// Apply button pressed in settings
		else if (button.equals("Apply")) {
			size = tempSize;
			tries = tempTries;
			numberOfColours = tempNumColours;
			showLetters = tempShowLetters;

			pastClues = new String[tries][size];
			pastGuesses = new String[tries][size];
			codeGuessArray = new String[size];
			codeArray = new String[size];
			displayColours = new String[tries][size];
			outcome = new String[size];

			menuPanel();
		}
		// Statistics button pressed in menu/mini menu
		else if (button.equals("Statistics")) {
			statisticsPanel();
		}
		// Reset button pressed in statistics panel
		else if (button.equals("Reset")) {
			totalGuessesMade = 0;
			totalGamesPlayed = 0;
			wins = 0;
			losses = 0;
			statisticsPanel();
		}
		// Next button pressed in how to play panel
		else if (button.equals("Next")) {
			AICurrentlyGuessing = true;
			howToPlayPanel();
		}

	}

	/**
	 * codeSetterPanel method: This procedural method creates and displays the
	 * JPanel that shows the options of selecting whoever is guessing
	 *
	 */
	public static void codeSetterPanel() {
		// Create the codeSetterPanel
		codeSetterPanel = new JPanel();
		JButton playerSetsCode = new JButton("AI Guesses");
		JButton computerSetsCode = new JButton("Player Guesses");

		JLabel codeSetterPanelLabel = new JLabel("Who is guessing the code?", SwingConstants.CENTER);

		// Set the alignments for the label and buttons
		codeSetterPanelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		playerSetsCode.setAlignmentX(Component.CENTER_ALIGNMENT);
		computerSetsCode.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Set the layout for the codeSetterPanel
		BoxLayout boxLayout = new BoxLayout(codeSetterPanel, BoxLayout.Y_AXIS);
		codeSetterPanel.setLayout(boxLayout);

		// Add components to the codeSetterPanel
		codeSetterPanel.add(Box.createRigidArea(new Dimension(50, 50)));
		codeSetterPanel.add(codeSetterPanelLabel);
		codeSetterPanel.add(Box.createRigidArea(new Dimension(50, 150)));
		codeSetterPanel.add(playerSetsCode);
		codeSetterPanel.add(Box.createRigidArea(new Dimension(50, 150)));
		codeSetterPanel.add(computerSetsCode);

		// Associate ActionListener with the buttons
		playerSetsCode.addActionListener(obj);
		computerSetsCode.addActionListener(obj);

		// Reset values
		for (int i = 0; i < tries; i++) {
			for (int j = 0; j < size; j++) {
				displayColours[i][j] = "";
				pastClues[i][j] = "";
				codeArray[j] = "";
				outcome[j] = "";
			}
		}
		currentGuessCounter = 0;
		numTries = 0;
		code = "";
		codeGuess = "";
		colours = "";
		numBlackPegs = 0;
		numWhitePegs = 0;
		gaveUp = false;
		AICurrentlyGuessing = false;
		possibleCodes = generateAllPossibleCodes();
		totalPegs = 0;
		noMoreCodes = false;
		validGuess = true;
		statShown = 0;

		// Display the codeSetterPanel
		codeSetterPanel.setVisible(true);

		// Remove all components from the displayGameFrame and add the
		// codeSetterPanel
		displayGameFrame.getContentPane().removeAll();
		displayGameFrame.add(codeSetterPanel);
		displayGameFrame.revalidate();

	}

	/**
	 * menuPanel method: This procedural method creates and displays the JPanel
	 * that shows the options of selecting whoever is guessing
	 *
	 */
	public static void menuPanel() {
		// Create the menuPanel
		JPanel menuPanel = new JPanel();
		menuPanel.setSize(1200, 600);
		BoxLayout menuLayout = new BoxLayout(menuPanel, BoxLayout.Y_AXIS);
		menuPanel.setLayout(menuLayout);

		// Create the labels and buttons for the menuPanel
		JLabel by = new JLabel("By: Michael Wong");
		JLabel welcome = new JLabel("Welcome To:", SwingConstants.CENTER);
		JButton play = new JButton("Play");
		JButton options = new JButton("Settings");
		JButton statistics = new JButton("Statistics");
		JButton exit = new JButton("Exit");
		JLabel titleLabel = new JLabel("Code Breaker", SwingConstants.CENTER);
		JButton howToPlay = new JButton("How To Play?");

		// Set the alignments and fonts for the labels and buttons
		welcome.setAlignmentX(Component.CENTER_ALIGNMENT);
		by.setAlignmentX(Component.CENTER_ALIGNMENT);
		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		options.setAlignmentX(Component.CENTER_ALIGNMENT);
		play.setAlignmentX(Component.CENTER_ALIGNMENT);
		exit.setAlignmentX(Component.CENTER_ALIGNMENT);
		statistics.setAlignmentX(Component.CENTER_ALIGNMENT);
		howToPlay.setAlignmentX(Component.CENTER_ALIGNMENT);

		welcome.setFont(new Font("Arial", Font.PLAIN, 15));
		by.setFont(new Font("Arial", Font.PLAIN, 15));
		titleLabel.setFont(new Font("Arial", Font.BOLD, 70));

		// Add components to the menuPanel
		menuPanel.add(Box.createRigidArea(new Dimension(50, 50)));
		menuPanel.add(welcome);
		menuPanel.add(titleLabel);
		menuPanel.add(Box.createRigidArea(new Dimension(50, 50)));
		menuPanel.add(by);
		menuPanel.add(Box.createRigidArea(new Dimension(50, 50)));
		menuPanel.add(play);
		menuPanel.add(Box.createRigidArea(new Dimension(50, 50)));
		menuPanel.add(options);
		menuPanel.add(Box.createRigidArea(new Dimension(50, 50)));
		menuPanel.add(statistics);
		menuPanel.add(Box.createRigidArea(new Dimension(50, 50)));
		menuPanel.add(howToPlay);
		menuPanel.add(Box.createRigidArea(new Dimension(50, 50)));
		menuPanel.add(exit);

		codeGuesser = 0;
		AICurrentlyGuessing = false;

		// Associate ActionListener with the buttons
		options.addActionListener(obj);
		play.addActionListener(obj);
		exit.addActionListener(obj);
		statistics.addActionListener(obj);
		howToPlay.addActionListener(obj);

		// Remove all components from the displayGameFrame and add the menuPanel
		displayGameFrame.getContentPane().removeAll();
		displayGameFrame.add(menuPanel);
		menuPanel.setVisible(true);
		displayGameFrame.revalidate();

	}

	/**
	 * settingsPanel method: This procedural method creates and displays the
	 * JPanel that shows the configurable settings that allows the user to
	 * change the number of tries, the code size, number of colours available,
	 * and if letters are displayed on the colour.
	 *
	 */
	public static void settingsPanel() {
		// Create the settings panel and set its layout
		JPanel settingsPanel = new JPanel();
		BoxLayout settingsLayout = new BoxLayout(settingsPanel, BoxLayout.Y_AXIS);

		// Create panels for different settings
		JPanel showLettersPanel = new JPanel();
		JPanel guessesAvailable = new JPanel();
		JPanel codeSize = new JPanel();
		JPanel numberOfColoursPanel = new JPanel();

		// Create labels for the settings
		JLabel showLettersLabel = new JLabel("Show Letters On Colours");
		JLabel guessesAvailableLabel = new JLabel("Number of Guesses Available");
		JLabel codeSizeLabel = new JLabel("Code Size");
		JLabel numberOfColoursLabel = new JLabel("Number of Colours");

		// Create labels to display the current values of settings
		JLabel numberOfGuessesAvailable = new JLabel("" + tempTries);
		JLabel numberOfColoursAvailable = new JLabel("" + tempNumColours);
		JLabel codeSizeCurrent = new JLabel("" + tempSize);

		// Create buttons to adjust the settings
		plusNumTries.setFocusable(false);
		plusNumColours.setFocusable(false);
		plusCodeSize.setFocusable(false);
		minusNumTries.setFocusable(false);
		minusNumColours.setFocusable(false);
		minusCodeSize.setFocusable(false);

		// Add the labels and buttons to the corresponding panels
		guessesAvailable.add(guessesAvailableLabel);
		guessesAvailable.add(minusNumTries);
		guessesAvailable.add(numberOfGuessesAvailable);
		guessesAvailable.add(plusNumTries);

		codeSize.add(codeSizeLabel);
		codeSize.add(minusCodeSize);
		codeSize.add(codeSizeCurrent);
		codeSize.add(plusCodeSize);

		numberOfColoursPanel.add(numberOfColoursLabel);
		numberOfColoursPanel.add(minusNumColours);
		numberOfColoursPanel.add(numberOfColoursAvailable);
		numberOfColoursPanel.add(plusNumColours);

		// Set the layout for each panel
		showLettersPanel.setLayout(flowLayout);
		guessesAvailable.setLayout(flowLayout);
		codeSize.setLayout(flowLayout);
		numberOfColoursPanel.setLayout(flowLayout);

		// Create and configure the show letters button
		JButton showLettersButton;
		if (tempShowLetters) {
			showLettersButton = new JButton("v/");

		} else {
			showLettersButton = new JButton("X");
		}

		// Add the show letters label and button to the show letters panel
		showLettersPanel.add(showLettersLabel);
		showLettersPanel.add(showLettersButton);

		// Set the layout for the settings panel
		settingsPanel.setLayout(settingsLayout);

		// Create and configure the configure settings panel
		JPanel configureSettings = new JPanel();
		configureSettings.setLayout(flowLayout);

		// Create buttons for applying, canceling, and restoring default
		// settings
		JButton apply = new JButton("Apply");
		JButton cancel = new JButton("Cancel");
		JButton restoreDefault = new JButton("Restore Default");

		// Create a title label for the settings
		JLabel titleLabel = new JLabel("Settings", SwingConstants.CENTER);
		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Adjust the background color of plus/minus buttons based on the
		// current settings
		if (tempTries == 10) {
			plusNumTries.setBackground(Color.GRAY);
			minusNumTries.setBackground(null);
		} else if (tempTries == 1) {
			minusNumTries.setBackground(Color.GRAY);
			plusNumTries.setBackground(null);
		} else {
			plusNumTries.setBackground(null);
			minusNumTries.setBackground(null);
		}

		if (tempNumColours == 6) {
			plusNumColours.setBackground(Color.GRAY);
			minusNumColours.setBackground(null);
		} else if (tempNumColours == 2) {
			minusNumColours.setBackground(Color.GRAY);
			plusNumColours.setBackground(null);
		} else {
			plusNumColours.setBackground(null);
			minusNumColours.setBackground(null);
		}

		if (tempSize == 8) {
			plusCodeSize.setBackground(Color.GRAY);
			minusCodeSize.setBackground(null);
		} else if (tempSize == 1) {
			plusCodeSize.setBackground(null);
			minusCodeSize.setBackground(Color.GRAY);
		} else {
			plusCodeSize.setBackground(null);
			minusCodeSize.setBackground(null);
		}

		// Associate ActionListener with the buttons
		apply.addActionListener(obj);
		cancel.addActionListener(obj);
		restoreDefault.addActionListener(obj);
		showLettersButton.addActionListener(obj);

		// Remove all components from the displayGameFrame
		displayGameFrame.getContentPane().removeAll();

		// Add components to the settings panel
		settingsPanel.add(titleLabel);
		settingsPanel.add(Box.createRigidArea(new Dimension(50, 50)));
		settingsPanel.add(showLettersPanel);
		settingsPanel.add(guessesAvailable);
		settingsPanel.add(codeSize);
		settingsPanel.add(numberOfColoursPanel);
		settingsPanel.add(configureSettings);

		// Add buttons to the configure settings panel
		configureSettings.add(apply);
		configureSettings.add(cancel);
		configureSettings.add(restoreDefault);

		// Add the settings panel to the displayGameFrame
		displayGameFrame.add(settingsPanel);
		settingsPanel.setVisible(true);
		displayGameFrame.revalidate();
	}

	/**
	 * statisticsPanel method: This procedural method creates and displays the
	 * JPanel that shows the statistics of the player guesses. The variables
	 * used are read from a .txt file.
	 *
	 */
	public static void statisticsPanel() {
		// Create statistics panel
		JPanel statisticsPanel = new JPanel();
		BoxLayout statisticsLayout = new BoxLayout(statisticsPanel, BoxLayout.Y_AXIS);
		statisticsPanel.setLayout(statisticsLayout);

		// Create title label
		JLabel statsTitle = new JLabel("Game Statistics (Player Guesses)", SwingConstants.CENTER);
		statsTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Create buttons
		JButton backButton = new JButton("Back");
		backButton.setAlignmentX(Component.CENTER_ALIGNMENT);

		JButton resetButton = new JButton("Reset");
		resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Create statistic labels
		JLabel totalGuessesLabel = new JLabel("Total Guesses: " + totalGuessesMade);
		JLabel totalGamesLabel = new JLabel("Total Games: " + totalGamesPlayed);
		JLabel winsLabel = new JLabel("Wins: " + wins);
		JLabel lossesLabel = new JLabel("Losses: " + losses);

		totalGuessesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		totalGamesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		winsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		lossesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Associate ActionListener with the buttons
		backButton.addActionListener(obj);
		resetButton.addActionListener(obj);

		// Remove all components from displayGameFrame
		displayGameFrame.getContentPane().removeAll();

		// Add components to statisticsPanel
		statisticsPanel.add(Box.createRigidArea(new Dimension(50, 50)));
		statisticsPanel.add(statsTitle);
		statisticsPanel.add(Box.createRigidArea(new Dimension(50, 50)));
		statisticsPanel.add(totalGuessesLabel);
		statisticsPanel.add(Box.createRigidArea(new Dimension(50, 50)));
		statisticsPanel.add(totalGamesLabel);
		statisticsPanel.add(Box.createRigidArea(new Dimension(50, 50)));
		statisticsPanel.add(winsLabel);
		statisticsPanel.add(Box.createRigidArea(new Dimension(50, 50)));
		statisticsPanel.add(lossesLabel);
		statisticsPanel.add(Box.createRigidArea(new Dimension(50, 50)));
		statisticsPanel.add(backButton);
		statisticsPanel.add(Box.createRigidArea(new Dimension(50, 50)));
		statisticsPanel.add(resetButton);

		// Add statisticsPanel to displayGameFrame
		displayGameFrame.add(statisticsPanel);
		statisticsPanel.setVisible(true);
		displayGameFrame.revalidate();

	}

	/**
	 * howToPlayPanel method: This procedural method creates and displays the
	 * JPanel that shows the instructions on how to play the game. The JPanel
	 * will change its display depending on who may be currently guessing, or if
	 * called when during the main menu.
	 *
	 */
	public static void howToPlayPanel() {
		// Create howToPlayPanel and set its layout
		JPanel howToPlayPanel = new JPanel();
		BoxLayout howToPlayLayout = new BoxLayout(howToPlayPanel, BoxLayout.Y_AXIS);
		howToPlayPanel.setLayout(howToPlayLayout);

		// Create title label
		JLabel howToPlayTitle = new JLabel("How To Play", SwingConstants.CENTER);
		howToPlayTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Create text area to display instructions
		JTextArea howToPlayText = new JTextArea(2, 20);
		String text;
		JLabel whoGuess;

		// Create buttons
		JButton backButton = new JButton("Back");
		backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		JPanel howToPlayButtons = new JPanel();
		howToPlayButtons.setLayout(flowLayout);
		JButton nextButton = new JButton("Next");

		// Determine text and label based on the codeGuesser and
		// AICurrentlyGuessing values
		if (codeGuesser == 0) {
			howToPlayButtons.add(backButton);
			if (!AICurrentlyGuessing) {
				howToPlayButtons.add(nextButton);
			}
		} else {
			howToPlayButtons.add(backButton);
		}

		if (AICurrentlyGuessing) {
			whoGuess = new JLabel("AI GUESSING");
			text = ("You are the mastermind, and you must create a code in your head. \r\n" + "\r\n"
					+ "The computer will attempt to guess the code based on the hints that you give it.\r\n" + "\r\n"
					+ "For each guess, a black score peg indicates that one of their pegs is the right colour in the right position. A white score peg indicates that one of their pegs is the right colour in the wrong position.\r\n"
					+ "\r\n" + "Do note that these pegs DO NOT correspond to the location of the colour. \r\n" + "\r\n"
					+ "Determine how many black and white pegs are in the computer's guess and click the colours to enter them (you can delete your input by clicking the \"delete\" button).\r\n"
					+ "\r\n" + "Then click the \"Enter\" button.\r\n" + "\r\n"
					+ "The computer will use this to guide their next guess. If their guess scores all black score pegs within the given number of tries, they win.\r\n"
					+ "\r\n"
					+ "If they find that there are no more available codes, then you most likely inputted one of the pegs wrong."); // Text
																																	// for
																																	// AI
																																	// guessing
																																	// instructions
		} else {
			whoGuess = new JLabel("PLAYER GUESSING");
			text = ("The computer has selected a secret combination of colored pegs and you have to guess that combination within the given number of tries to win. \r\n"
					+ "\r\n"
					+ "To create your guess, click each apparent colored peg until you've filled the row with your combination (you can delete your guess by clicking the \"delete\" button).\r\n"
					+ "\r\n" + "Then click the \"Check\" button.\r\n" + "\r\n"
					+ "Each time you submit a guess the machine will use score pegs to let you know how close that guess is.\r\n"
					+ "\r\n"
					+ "For each guess, a black score peg indicates that one of your pegs is the right colour in the right position. A white score peg indicates that one of your pegs is the right colour in the wrong position.\r\n"
					+ "\r\n" + "Do note that these pegs DO NOT correspond to the location of the colour. \r\n" + "\r\n"
					+ "Use the score to guide your next guess. If your guess scores all black score pegs within the given number of tries, you win.\r\n"
					+ "\r\n" + "Good luck!"); // Text for player guessing
												// instructions
		}

		// Set text area properties
		howToPlayText.setText(text);
		howToPlayText.setWrapStyleWord(true);
		howToPlayText.setLineWrap(true);
		howToPlayText.setOpaque(false);
		howToPlayText.setEditable(false);
		howToPlayText.setFocusable(false);
		howToPlayText.setBackground(UIManager.getColor("Label.background"));
		howToPlayText.setFont(new Font("Aria", Font.PLAIN, 20));
		howToPlayText.setBorder(UIManager.getBorder("Label.border"));

		// Align labels
		howToPlayTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		whoGuess.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Associate ActionListener with the buttons
		backButton.addActionListener(obj);
		nextButton.addActionListener(obj);

		// Remove all components from displayGameFrame
		displayGameFrame.getContentPane().removeAll();

		// Add components to howToPlayPanel
		howToPlayPanel.add(Box.createRigidArea(new Dimension(50, 50)));
		howToPlayPanel.add(howToPlayTitle);
		howToPlayPanel.add(Box.createRigidArea(new Dimension(50, 50)));
		howToPlayPanel.add(whoGuess);
		howToPlayPanel.add(Box.createRigidArea(new Dimension(50, 50)));
		howToPlayPanel.add(howToPlayText);
		howToPlayPanel.add(Box.createRigidArea(new Dimension(50, 50)));
		howToPlayPanel.add(howToPlayButtons);
		howToPlayPanel.add(Box.createRigidArea(new Dimension(50, 50)));

		// Add howToPlayPanel to displayGameFrame
		displayGameFrame.add(howToPlayPanel);
		howToPlayPanel.setVisible(true);
		displayGameFrame.revalidate();

	}

	public static void main(String[] args) throws IOException {
		// Set the look and feel (ui) to NimbusLookAndFeel (theme)
		NimbusLookAndFeel ui = new NimbusLookAndFeel();
		try {
			UIManager.setLookAndFeel(ui);
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		// Customize the font for buttons and labels
		ui.getDefaults().put("Button.font", new Font("Arial", Font.PLAIN, 30));
		ui.getDefaults().put("Label.font", new Font("Arial", Font.BOLD, 40));

		// Create buttons for increasing/decreasing values
		plusNumTries = new JButton("+");
		minusNumTries = new JButton("-");
		minusNumColours = new JButton("-");
		plusNumColours = new JButton("+");
		minusCodeSize = new JButton("-");
		plusCodeSize = new JButton("+");

		// Associate ActionListener with the buttons
		plusNumTries.addActionListener(obj);
		plusCodeSize.addActionListener(obj);
		plusNumColours.addActionListener(obj);
		minusNumTries.addActionListener(obj);
		minusCodeSize.addActionListener(obj);
		minusNumColours.addActionListener(obj);

		// Read file for statistics
		readFile();

		// Display the game frame and menu panel
		displayGameFrame();
		menuPanel();
	}

	
	/**
	 * createCode method: This functional method creates a random code and returns the code array.
	 *
	 * @param colours - Holds the selected colour <type String>
	 *        coloursSize - Size of the colours available <type int>
	 *        codeArray - The arrray that stores the solution code <String[]>
	 *        coloursArray - The array that stores the possible colours that the user can use <String[]>
	 *
	 * @returns codeArray
	 */
	
	public static String[] createCode(String colours, int coloursSize, String[] codeArray, String[] coloursArray) {
		// Generate a random code by selecting random colors from the colors
		// array
		for (int i = 0; i < size; i++) {
			int randomNum = (int) Math.floor(Math.random() * (coloursSize - 1 + 1) + 1);
			codeArray[i] = coloursArray[randomNum - 1];
		}

		// Build the code string by concatenating the code array elements
		for (int i = 0; i < size; i++) {
			code += codeArray[i];
		}

		// Return the generated code array
		return codeArray;
	}
	
	/**
	 * findFullyCorrect method: This functional method determines the 
	 *
	 * @param 
	 *        codeArray - The arrray that stores the solution code <String[]>
	 *        codeGuessArray - The array that stores the player's guess  <String[]>
	 *
	 * @returns outcome
	 */
	

	public static String[] findFullyCorrect(String[] codeGuessArray, String[] codeArray) {
		// Check the code guess against the secret code and determine the
		// outcome (black and white pegs)
		if (codeGuesser == 2) {
			numBlackPegs = 0;
			numWhitePegs = 0;

			// Create temporary arrays for checking
			String[] codeGuessArrayCheck = new String[size];
			String[] codeArrayCheck = new String[size];

			// Copy the code guess and secret code arrays for checking
			for (int i = 0; i < size; i++) {
				codeGuessArrayCheck[i] = codeGuessArray[i];
				codeArrayCheck[i] = codeArray[i];
			}

			// Check for black pegs (correct color in the correct position)
			for (int i = 0; i < size; i++) {
				if (codeGuessArrayCheck[i].equals(codeArrayCheck[i])) {
					numBlackPegs++;
					codeGuessArrayCheck[i] = ""; // Mark the positions as
													// visited in both arrays
					codeArrayCheck[i] = "";
				}
			}

			// Check for white pegs (correct color in the wrong position)
			for (int i = 0; i < size; i++) {
				if (!codeGuessArrayCheck[i].equals("")) {
					for (int j = 0; j < size; j++) {
						if (!codeArrayCheck[j].equals("") && codeGuessArray[i].equals(codeArray[j])) {
							numWhitePegs++;
							codeArrayCheck[j] = ""; // Mark the position as
													// visited in the code array
							break;
						}
					}
				}
			}

			// Set the outcome array based on the number of black and white pegs
			for (int i = 0; i < numBlackPegs; i++) {
				outcome[i] = "b";
			}

			for (int i = 0; i < numWhitePegs; i++) {
				outcome[i + numBlackPegs] = "w";
			}
		} else {
			// Set the outcome array based on the number of black and white pegs
			for (int i = 0; i < numBlackPegs; i++) {
				outcome[i] = "b";
			}

			for (int i = 0; i < numWhitePegs; i++) {
				outcome[i + numBlackPegs] = "w";
			}
		}

		return outcome;
	}

	public static int getIndex(char c) {
		// Find the index of a given character in the COLOURS array
		for (int i = 0; i < COLOURS.length; i++) {
			if (COLOURS[i].equals(Character.toString(c))) {
				return i;
			}
		}

		// If the character is not found in the COLOURS array, throw an
		// exception
		throw new IllegalArgumentException("Invalid character: " + c);
	}

	// Store previous guesses
	/**
	 * display method: Procedural method to store the current code guess and outcome in the pastGuesses and pastClues array.
	 * 
	 * @param outcome
	 *            String array to store past clues
	 * @param codeGuessArray
	 *            String array to store past code guesses
	 */
	public static void display(String[] outcome, String[] codeGuessArray) {
		numTries++; // Add 1 to numTries counter

		// Store the current code guess and outcome in the pastGuesses and
		// pastClues arrays
		for (int i = 0; i < size; i++) {
			pastGuesses[numTries - 1][i] = codeGuessArray[i];
			pastClues[numTries - 1][i] = outcome[i];
			outcome[i] = " "; // Reset the outcome array for the next guess
		}
	}

	/**
	 * generateAllPossibleCodes method: Generates all possible codes based on the current size of the code.
	 * 
	 * @return a list of strings containing all possible codes
	 */
	public static List<String> generateAllPossibleCodes() {
		List<String> codes = new ArrayList<>();
		generateAllPossibleCodesHelper("", codes); // Start the recursive
													// generation with an empty
													// current code
		return codes;
	}

	/**
	 * generateAllPossibleCodesHelper method: Recursive helper method to generate all possible codes for the computer to guess.
	 * 
	 * @param currentCode
	 *            the current code being generated
	 * @param codes
	 *            the list to store the generated codes
	 */
	public static void generateAllPossibleCodesHelper(String currentCode, List<String> codes) {
		if (currentCode.length() == size) {
			codes.add(currentCode); // Add the completed code to the list
			return;
		}

		for (String colour : COLOURS) {
			generateAllPossibleCodesHelper(currentCode + colour, codes); // Recursively
																			// append
																			// each
																			// color
																			// to
																			// the
																			// current
																			// code
		}
	}

	/**
	 * narrowDownPossibilities method: Narrows down the list of possible codes based on the feedback received
	 * from the guess.
	 * 
	 * @param possibleCodes
	 *            the list of possible codes
	 * @param guessCode
	 *            the code that was guessed
	 * @param blackPegs
	 *            the number of black pegs in the feedback
	 * @param whitePegs
	 *            the number of white pegs in the feedback
	 * @return a list of narrowed down codes
	 */
	public static List<String> narrowDownPossibilities(List<String> possibleCodes, String guessCode, int blackPegs,
			int whitePegs) {
		List<String> narrowedCodes = new ArrayList<>();

		for (String code : possibleCodes) {
			int codeBlackPegs = countBlackPegs(code, guessCode); // Calculate
																	// the
																	// number of
																	// black
																	// pegs
			int codeWhitePegs = countWhitePegs(code, guessCode); // Calculate
																	// the
																	// number of
																	// white
																	// pegs

			if (codeBlackPegs == blackPegs && codeWhitePegs == whitePegs) {
				narrowedCodes.add(code); // Add the code to the narrowedCodes
											// list if the peg counts match
			}
		}
		return narrowedCodes;
	}

	/**
	 * countBlackPegs method: Counts the number of black pegs in the feedback by comparing the code and
	 * guess code.
	 * 
	 * @param code
	 *            the secret code
	 * @param guessCode
	 *            the guessed code
	 * @return the number of black pegs
	 */
	public static int countBlackPegs(String code, String guessCode) {
		int blackPegs = 0;

		for (int i = 0; i < size; i++) {
			if (code.charAt(i) == guessCode.charAt(i)) {
				blackPegs++; // Increment blackPegs if the characters at the
								// same position match
			}
		}

		return blackPegs;
	}

	/**
	 * countWhitePegs: Counts the number of white pegs in the feedback by comparing the code and
	 * guess code.
	 * 
	 * @param code
	 *            the secret code
	 * @param guessCode
	 *            the guessed code
	 * @return the number of white pegs
	 */
	public static int countWhitePegs(String code, String guessCode) {
		int whitePegs = 0;

		int[] codeCount = new int[COLOURS.length];
		int[] guessCodeCount = new int[COLOURS.length];

		for (int i = 0; i < size; i++) {
			if (code.charAt(i) != guessCode.charAt(i)) {
				codeCount[getIndex(code.charAt(i))]++; // Increment count for
														// the specific color in
														// the code
				guessCodeCount[getIndex(guessCode.charAt(i))]++; // Increment
																	// count for
																	// the
																	// specific
																	// color in
																	// the guess
																	// code
			}
		}

		for (int i = 0; i < COLOURS.length; i++) {
			whitePegs += Math.min(codeCount[i], guessCodeCount[i]); // Add the
																	// minimum
																	// count of
																	// a color
																	// to
																	// whitePegs
		}

		return whitePegs;
	}

	/**
	 * readFile method: Reads statistics from a file if it exists, or creates a new file if it
	 * doesn't.
	 */
	public static void readFile() {
		String fileName = "H:\\Statistics.txt";

		// Check if the file exists
		File file = new File(fileName);
		if (file.exists()) {
			// File exists, so read from it
			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				for (int i = 0; i < stats.length; i++) {
					stats[i] = Integer.parseInt(br.readLine()); // Read and
																// parse the
																// statistics
																// data from the
																// file
				}

				totalGuessesMade = stats[0];
				totalGamesPlayed = stats[1];
				wins = stats[2];
				losses = stats[3];

			} catch (IOException e) {
				// Handle the IOException if any
			}
		} else {
			// File doesn't exist, so create it
			try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
				String content = "0\n";
				for (int i = 0; i < stats.length; i++) {
					bw.write(content); // Write the initial statistics data to
										// the file
				}
				System.out.println("File created successfully.");
			} catch (IOException e) {
				System.out.println("Can't create file on the H drive.");
			}
		}
	}

	/**
	 * writeFile method: 
	 * Writes the current statistics data to a file.
	 */
	public static void writeFile() {
		String fileName = "H:\\Statistics.txt";

		stats[0] = totalGuessesMade;
		stats[1] = totalGamesPlayed;
		stats[2] = wins;
		stats[3] = losses;

		try (BufferedWriter br = new BufferedWriter(new FileWriter(fileName))) {
			for (int i = 0; i < stats.length; i++) {
				br.write(stats[i] + "\n"); // Write the updated statistics data
											// to the file
			}
		} catch (IOException e) {
			System.out.println("Error writing to the H Drive.");
		}
	}

}