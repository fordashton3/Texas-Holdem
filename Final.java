import javax.swing.*;
import java.io.*;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Final {
	public static void main(String[] args) {
		welcome();
		Scanner input = new Scanner(System.in);
		Card[] deck = new Card[52];
		Profile[] players = new Profile[6];
		runMenu(players, input);

		initDeck(deck);
		shuffle(deck);
		runGame(players, deck, input);

//TODO- Possibly implement globablly the ability for the program to continue even if something wrong happens.(unless critical)
	}

	public static void welcome() {
		System.out.println("Welcome to Texas Holdem!\n*****************************************");
		System.out.println("\t\tPlay against your friends!");
		System.out.println("1. Press C to call");
		System.out.println("2. Press R to raise");
		System.out.println("3. Press F to fold");
		System.out.println("4. Press X to check");
		System.out.println("5. Aces can only be high or low");
		System.out.println("6. Good luck, have fun!");
		System.out.println("*****************************************");
	}

	public static void initDeck(Card[] deck) {
		String rank;
		char suit;
		for (int i = 0; i < 13; i++) {
			for (int j = 0; j < 4; j++) {
				if (i == 0) {
					rank = "A";
				} else if (i == 10) {
					rank = "J";
				} else if (i == 11) {
					rank = "Q";
				} else if (i == 12) {
					rank = "K";
				} else {
					rank = Integer.toString(i + 1);
				}

				suit = switch (j) {
					case 0 -> '♥';
					case 1 -> '♦';
					case 2 -> '♣';
					default -> '♠';
				};
				deck[13 * j + i] = new Card(rank, suit, i + 1);
			}
		}

	}

	public static void printDeck(Card[] deck) {
		for (int i = 0; i < deck.length; i++) {
			if (deck[i] != null) {
				System.out.printf("%-3s ", deck[i].toString());
			} else {
				System.out.print("na  ");
			}
			if (i % 13 == 12) {
				System.out.println();
			}
		}
	}

	public static void swap(Card[] deck, int p1, int p2) {

		Card temp = deck[p1];
		deck[p1] = deck[p2];
		deck[p2] = temp;
	}

	public static void shuffle(Card[] deck) {
		for (int i = 0; i < deck.length; i++) {
			int card1 = (int) (Math.random() * deck.length - 1);
			int card2 = (int) (Math.random() * deck.length - 1);

			swap(deck, card1, card2);
		}
	}

	public static void parseProfile(File file, Profile[] players, int seat, boolean print) {
		try (Scanner reader = new Scanner(file)) {
			String line = reader.nextLine();
			reader.close();
			String[] data = line.split(",");
			players[seat] = new Profile(data[0], Integer.parseInt(data[1]), Integer.parseInt(data[2]), Integer.parseInt(data[3]), true);
			if (print) {
				System.out.println(players[seat].toString());
			}

		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		}
	}

	public static void writeProfiles(Profile player, String username) {
		File file = new File("profiles\\" + username);
		if (player.isSave()) {
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
				writer.write(player.toString());
			} catch (IOException e) {
				System.out.println("IO error occurred");
			}
		}
	}

	public static void runMenu(Profile[] players, Scanner input) {
		int menuInput;
		System.out.printf("1. Start game%n2. Quit to Desktop%n");
		try {
			menuInput = input.nextInt();
			if (menuInput == 1) {
				chooseSeat(players, input);
			} else if (menuInput == 2) {
				input.close();
				System.exit(1);
			} else {
				throw new Exception("Invalid input");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static void chooseSeat(Profile[] players, Scanner input) {

		System.out.println("Which seat would you like to interact with?");
		for (int i = 0; i < 6; i++) {
			if (players[i] != null) {
				System.out.printf("%d:\t%s%n", i + 1, players[i].getName());
			} else {
				System.out.printf("%d:\tEmpty%n", i + 1);
			}
		}
		int seat = 0;
		try {
			seat = input.nextInt();
			if (seat < 1 || seat > 6) {
				throw new Exception("Input out of specified range");
			}
			seat--;
		} catch (InputMismatchException e) {
			System.out.println("Input mismatch");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		runUserSetup(seat, players, input);
	}

	public static void runUserSetup(int seat, Profile[] players, Scanner input) {
		int menuInput;
		if (players[seat] != null) {
			System.out.printf("Seat %d is occupied by %s.%n\t1. Make %s leave the table%n\t2. Return to Menu%n",
					seat + 1, players[seat].getName(), players[seat].getName());
			try {
				menuInput = input.nextInt();
				if (menuInput == 1) {
					writeProfiles(players[seat], players[seat].getName());
					players[seat] = null;
					chooseSeat(players, input);
				} else if (menuInput == 2) {
					saveProfiles(players);
					runMenu(players, input);
				} else {
					throw new Exception("Invalid input");
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		} else {
			System.out.printf("Seat %d:%n\t1. Choose Profile%n\t2. New Profile%n\t3. Return to Menu%n", seat + 1);
			try {
				menuInput = input.nextInt();
				if (menuInput == 1) {
					chooseProfile(players, seat, input);
				} else if (menuInput == 2) {
					runNewProfile(players, seat, input);
				} else if (menuInput == 3) {
					runMenu(players, input);
				} else {
					throw new Exception("Invalid input");
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	public static boolean isDuplicate(Profile[] players, String fileName) {

		for (Profile player : players) {
			if (player != null) {
				if (player.getName().equalsIgnoreCase(fileName)) {
					return true;
				}
			}
		}
		return false;
	}

	public static void chooseProfile(Profile[] players, int seat, Scanner input) { //TODO - Restrict duplicate profile selections
		String[] filesNames;
		try {
			filesNames = fileList(players, true);
			if (filesNames[0].equals("-1")) {
				System.out.printf("1. New User Setup%n2. Exit to Menu");
				try {
					switch (input.nextInt()) {
						case 1:
							runNewProfile(players, seat, input);
						case 2:
							runMenu(players, input);
						default:
							input.next();
							throw new Exception("Invalid input - Please try again");
					}
				} catch (InputMismatchException e) {
					System.out.println("Input Mismatch");
				}
			}
			int profile;
			System.out.print("Enter the integer corresponding to your profile: ");
			profile = input.nextInt();
			if (profile < 1 || profile > filesNames.length) {
				throw new Exception("Input is out of range");
			} else if (isDuplicate(players, filesNames[profile - 1])) {
				throw new Exception("Profile is already in use");
			}
			profile--;
			File file = new File("profiles\\" + filesNames[profile]);
			parseProfile(file, players, seat, false);
		} catch (InputMismatchException e) {
			System.out.println("Input Mismatch");
		} catch (IOException e) {
			System.out.println("IO error occurred");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		getSeatOrPlay(players, input);

	}

	public static void runNewProfile(Profile[] players, int seat, Scanner input) {
		int userInput;
		System.out.printf("1. New user%n2. Sit as guest%n");
		try {
			userInput = input.nextInt();
			if (userInput == 1) {
				createProfile(players, seat, input);
			} else if (userInput == 2) {
				createGuest(players, seat, input);
			} else {
				throw new Exception("Invalid Input");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		getSeatOrPlay(players, input);
	}

	public static void createProfile(Profile[] players, int seat, Scanner input) {
		try {
			String[] fileNames = fileList(players, false);
			String username = null;
			System.out.print("Enter a Username: ");
			for (String fileName : fileNames) {
				username = input.nextLine();
				while (username.equalsIgnoreCase(fileName)) {
					System.out.printf("Username already in use.%nEnter a different Username: ");
					username = input.nextLine();
				}
				while (username.length() > 10) {
					System.out.printf("Username exceeds character count of 10%nEnter a different Username: ");
					username = input.nextLine();
				}
			}
			players[seat] = new Profile(username, 1000, 0, 1, true);
			try {
				assert username != null;
				try (BufferedWriter ignored = new BufferedWriter(new FileWriter(username))) {// TODO - Don't allow special characters due to file names
					writeProfiles(players[seat], username);
				}
			} catch (IOException e) {
				System.out.println("IO error occurred");
				System.exit(1);
			}
		} catch (NoSuchElementException e) {
			System.out.println("No lines to read from");
		}
		getSeatOrPlay(players, input);
	}

	public static void createGuest(Profile[] players, int seat, Scanner input) { //TODO - Make sure to clean scanner Buffer between methods. IT BUILDS!!!!
		try {
			String[] fileNames = fileList(players, false);
			String username = null;
			System.out.print("Enter a Username: ");
			for (String fileName : fileNames) {
				username = input.nextLine();
				while (username.equalsIgnoreCase(fileName)) {
					System.out.print("Username already in use\nEnter a different Username: ");
				}
			}
			players[seat] = new Profile(username, 1000, 0, 1, false);
		} catch (NoSuchElementException e) {
			System.out.println("No lines to read from");
		}
		getSeatOrPlay(players, input);
	}

	public static String[] fileList(Profile[] players, boolean print) {
		String[] pathnames;
		File file = new File("profiles");
		pathnames = file.list();
		try {
			if (print) {
				if (pathnames != null) {
					for (int i = 0; i < pathnames.length; i++) {
						if (isDuplicate(players, pathnames[i])) {
							System.out.printf("%d. %s\t(In Use)%n", i + 1, pathnames[i]);
						} else {
							System.out.printf("%d. %s%n", i + 1, pathnames[i]);
						}
					}
				} else {
					System.out.println("No profiles to choose from");
					pathnames = new String[]{"-1"};
				}
			}
		} catch (NullPointerException e) {
			System.out.println("Null Pointer");
		} finally {
			System.out.println();
		}
		return pathnames;
	}

	public static void saveProfiles(Profile[] players) {
		for (int i = 0; i < players.length; i++) {

			if (players[i] != null) {
				writeProfiles(players[i], players[i].getName());
				players[i] = null;
			}
		}
	}

	public static void getSeatOrPlay(Profile[] players, Scanner input) {
		int userInput;
		System.out.printf("1. Seat Selection%n2. Play Poker!%n3. Exit to menu%n"); //TODO - On menu exit, clear seats.
		try {
			userInput = input.nextInt();
			if (userInput == 1) {
				chooseSeat(players, input);
			} else if (userInput == 3) {
				saveProfiles(players);
				runMenu(players, input);
			} else if (userInput != 2) {
				throw new Exception("Invalid input");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static Card draw(Card[] deck) { // TODO - make it draw different cards
		Card temp = deck[0];
		for (int i = 0; i < deck.length - 1; i++) {
			deck[i] = deck[i + 1];
		}
		deck[51] = null;
		return temp;
	}

	public static void drawTable(Card[] table, Profile[] player, Card[] deck, int index) {
		table[index] = draw(deck);
		for (Profile profile : player) {
			if (profile != null) {
				profile.setHand(draw(deck), index + 2);
			}
		}
	}

	public static void runGame(Profile[] players, Card[] deck, Scanner input) { //TODO - Create gameplay loop
		Card[] table = new Card[5];
		int pot = 0;
		int[] participants = new int[]{0};
		boolean[] occupied = new boolean[6];
		for (int i = 0; i < players.length; i++) {
			if (players[i] != null){
				occupied[i] = true;
				participants[0]++;
				players[i].setHand(draw(deck),0);
				players[i].setHand(draw(deck),1);
			}
		}
		pot = runRound(players, table, occupied, participants, input);
		for (int i = 0; i < 5; i++) {
			drawTable(table, players, deck, i);
			if (participants[0] > 1 && i > 1){
				pot = runRound(players, table, occupied, participants, input);
			}
		}
		int bestPlayer = 0;
//        for (int i = 0; i < players.length; i++) {
//            if (occupied[i] && players[i].getScore() > bestPlayer) {
//                bestPlayer = i;
//            }
//        }
		System.out.printf("Congratulations %s! You won %d chips!%n", players[bestPlayer].getName(), pot);
	}

	public static int runRound(Profile[] players, Card[] table, boolean[] occupied, int[] participants, Scanner input) {
		int pot = 0;
		int callCounter = 0;
			int action = 0;
			int bet = 0;

			int activePlayer = 0;
			while (callCounter != participants[0]) {
				if (activePlayer >= 6) {
					activePlayer = 0;
				}
				if (occupied[activePlayer]) {
					printCards(players, table, activePlayer, participants[0], callCounter);
					if (players[activePlayer] != null) {
						action = chooseAction(players, bet, activePlayer, input);
					}
					if (action == -1) { // fold
						participants[0]--;
						occupied[activePlayer] = false;
					} else if (action > 0) { // bet
						callCounter = 1;
						pot += action;
					} else if (action == 0) { // check or call
						callCounter++;
					}
				}
				activePlayer++;
			}
		return pot;
	}

	//TODO - Create getScore method to give score based on value of hand
	public static int chooseAction(Profile[] players, int prevBet, int activePlayer, Scanner input) {
		int balance = players[activePlayer].getBalance();
		String name = players[activePlayer].getName();
		char action;
		int maxBet = 0;
		for (int i = 0; i < players.length; i++) {
			if (players[i] != null) {
				if (maxBet > players[i].getBalance()) {
					maxBet = i;
				}
			}
		}
		boolean loop = false;
		do {
			try {
				System.out.printf("Balance:\t%d%n(C) Call\t(R) Raise\t(X) Check\t(F) Fold%nChoose desired action: ", balance);
				action = Character.toLowerCase(input.next().charAt(0));

				switch (action) {
					case 'c' -> {
						if (prevBet < balance) {
							players[activePlayer].setBalance(balance - prevBet);
							System.out.printf("%s called for %d credits%n", name, prevBet);
							return 0;
						} else if (balance == prevBet) {
							players[activePlayer].setBalance(0);
							System.out.printf("%s is All In for %d credits%n", name, prevBet);
							return 0;
						} else {
							System.out.printf("Unable to call due to insufficient funds%n%s was forced to fold", name);
							return -1;
						}
					}
					case 'r' -> {
						System.out.print("Bet amount: ");
						prevBet = input.nextInt();
						if (prevBet > maxBet && prevBet <= balance) { // Balance insufficient and balance greater than max bet
							loop = true;
							System.out.printf("Raise over max betting amount%n");
						} else if (prevBet <= maxBet && prevBet < balance) {
							players[activePlayer].setBalance(balance - prevBet);
							System.out.printf("%s raised to %d credits%n", name, prevBet);
							return prevBet;
						} else if (prevBet <= maxBet && prevBet == balance) {
							players[activePlayer].setBalance(0);
							System.out.printf("%s is All In for %d credits", name, prevBet);
							return prevBet;
						} else {
							loop = true;
							System.out.printf("Insufficient funds to raise %d units%n", prevBet);
						}
					}
					case 'x' -> {
						if (prevBet != 0 && prevBet != -1) { // Unable to call behavior
							loop = true;
							System.out.printf("Unable to call due to ongoing bet%n");
						} else {
							System.out.printf("%s checked%n", name);
							return 0;
						}
					}
					case 'f' -> {
						System.out.printf("%s folded%n", name);
						return -1;
					}
					default -> {
						loop = true;
						System.out.printf("'%c' is not a valid option, try again%n", action);
					}
				}
			} catch (InputMismatchException e) {
				System.out.println("Input Mismatch");
			}
		} while (loop);
		return -1;
	}

	public static void printCards(Profile[] player, Card[] table, int activePlayer, int participants, int callCounter) {
		for (int i = 0; i < 5; i++) {
			if (table[i] != null) {
				System.out.printf("%s\t", table[i]);
			} else {
				System.out.print("__\t");
			}
		}
		System.out.println();
		for (int i = 0; i < 6; i++) {
			if (i == activePlayer && player[i] != null) {
				System.out.printf("%-10s:\t%s%n", player[i].getName(), player[i].handToString());
			} else if (player[i] != null) {
				System.out.printf("%-10s:\t--   --%n", player[i].getName());
			}

		}
		System.out.printf("People: %d\tCall Counter: %d%n", participants, callCounter);
		System.out.println();
	}

	public static int getScore(){
		return 0;
	}
}

/*
1. Ashton
2. Edward
3.
4. Guest1
5. Guest2
6.

 */