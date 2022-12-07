import java.io.*;
import java.sql.SQLOutput;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Final {
	public static void main(String[] args) {
		welcome();
		Card[] deck = new Card[52];
		Profile[] players = new Profile[6];
		runMenu(players);

		initDeck(deck);
		File playerData = new File("player data.txt");

		try (Scanner reader = new Scanner(playerData)) {
			String line = reader.nextLine();
			int counter = 0;
			String[] columns = line.split(",");

			players = new Profile[columns.length / 4];

		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (NumberFormatException e) {
			System.out.println("File not formatted correctly");
		}

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

				switch (j) {
					case 0:
						suit = '♥';
						break;
					case 1:
						suit = '♦';
						break;
					case 2:
						suit = '♣';
						break;
					default:
						suit = '♠';
						break;
				}
				deck[13 * j + i] = new Card(rank, suit, i + 1);
			}
		}

	}

	public static void printDeck(Card[] deck) {
		for (int i = 0; i < deck.length; i++) {
			System.out.printf(deck[i].toString());
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

	public static void checkFile(File file) throws Exception {
		int counter = 0;
		Scanner reader = new Scanner(file);
		while (reader.hasNext()) {
			counter = 0;
			String line = reader.nextLine();
			for (int i = 0; i < line.length(); i++) {
				if (line.charAt(i) == ',') {
					counter++;
				}
			}
			if (counter != 3) {
				throw new Exception("File not formatted correctly");
			}
		}
	}


	public static void parseProfile(File file, Profile[] players, int index) throws Exception {
		try (Scanner reader = new Scanner(file)) {
			String line = reader.nextLine();
			reader.close();
			String[] data = line.split(",");
			for (int i = 0; i < data.length; i++) {
				players[i] = new Profile(data[i], Integer.parseInt(data[i + 1]), Integer.parseInt(data[i + 2]), Integer.parseInt(data[i + 3]), true);
			}
			System.out.println(players[index].toString());

		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		}
	}

	// Use possible players
	public static void writeProfiles(File file, Profile[] players) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			for (int i = 0; i < players.length; i++) {
				writer.write(players[i].toString() + "\n");
			}
		} catch (IOException e) {
			System.out.println("IO error occurred");
		}
	}

	public static void runMenu(Profile[] players) {
		int menuInput = 0;
		System.out.printf("1. Start game%n2. Quit to Desktop%n");
		try (Scanner input = new Scanner(System.in)) {
			menuInput = input.nextInt();
			if (menuInput == 1) {
				chooseSeat(input, players);
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

	public static void chooseSeat(Scanner input, Profile[] players) {
		System.out.println("Which seat would you like to interact with?");
		for (int i = 0; i < 6; i++) {
			System.out.printf("\t%d", i + 1);
		}
		System.out.println();
		int seat = 0;
		try {
			seat = input.nextInt();
			if (seat < 1 || seat > 6) {
				throw new Exception("Input out of specified range");
			}
		} catch (InputMismatchException e) {
			System.out.println("Input mismatch");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		runUserSetup(input, seat, players);
	}

	public static void runUserSetup(Scanner input, int seat, Profile[] players) {
		int menuInput = 0;
		System.out.printf("Seat %d:%n\t1. Choose Profile%n\t2. New Profile%n\t3. Return to Menu%n", seat);
		try {
			menuInput = input.nextInt();
			if (menuInput == 1) {
				chooseProfile(input, seat);
			} else if (menuInput == 2) {
				runNewProfile(input, players, seat);
			} else if (menuInput == 3) {
				runMenu(players);
			} else {
				throw new Exception("Invalid input");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static void chooseProfile(Scanner input, int seat) {
		String[] filesNames;
		try {
			filesNames = fileList(true);
			int profile = 0;
			System.out.print("Enter the integer corresponding to your profile: ");
			profile = input.nextInt();
			if(profile < 1 || profile > filesNames.length){
				throw new Exception("Input is out of range");
			}
		} catch (InputMismatchException e){
			System.out.println("Input Mismatch");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}


	public static void runNewProfile(Scanner input, Profile[] players, int seat) {
		int userInput = 0;
		System.out.printf("1. New user%n2. Sit as guest");
		try {
			userInput = input.nextInt();
			if (userInput == 1) {
				createProfile(input, players, seat);
			} else if (userInput == 2) {
				createGuest(input, players, seat);
			} else {
				throw new Exception("Invalid Input");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static void createProfile(Scanner input, Profile[] players, int seat) {
		try {
			String[] fileNames = fileList(false);
			String username = null;
			for (String fileName : fileNames) {
				System.out.print("Enter a Username: ");
				username = input.nextLine();
				while (username.equalsIgnoreCase(fileName)) {
					System.out.print("Username already in use\nEnter a different Username: ");
				}
			}
			players[seat] = new Profile(username, 1000, 0, 1, true);
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(username))) {
				// TODO - Don't allow special characters due to file names
				//  if (userID.contains("Special Character")) {
				//  input.nextLine();
				//  throw new Exception("Invalid Username - Cannot Contain Spaces");
				//  }
				writer.write(players[seat].toString());
			} catch (IOException e) {
				System.out.println("IO error occurred");
				System.exit(1);
			}
		} catch (NoSuchElementException e) {
			System.out.println("No lines to read from");
		}
	}

	public static void createGuest(Scanner input, Profile[] players, int seat) {
		try {
			String[] fileNames = fileList(false);
			String username = null;
			for (String fileName : fileNames) {
				System.out.print("Enter a Username: ");
				username = input.nextLine();
				for (int i = 0; i < 6; i++) {
					if (username.equalsIgnoreCase(fileName) || username.equalsIgnoreCase(players[i].getName())) {
						while (username.equalsIgnoreCase(fileName) || username.equalsIgnoreCase(players[i].getName())) {
							System.out.print("Username already in use\nEnter a different Username: ");
						}
					}
				}
			}
			players[seat] = new Profile(username, 1000, 0, 1, false);
		} catch (NoSuchElementException e) {
			System.out.println("No lines to read from");
		}
	}

	public static String[] fileList(boolean print) {
		String[] pathnames;
		File file = new File("profiles");
		pathnames = file.list();
		try {
			if (print) {
				for (int i = 0; i < pathnames.length; i++) {
					System.out.printf("%d. %s%n", i + 1, pathnames[i]);
				}
			}
		} catch (NullPointerException e){
			System.out.println("Null pointer");
		}
		return pathnames;
	}

	public static void runGame(Scanner input) {
		//TODO - Actual Poker Game Code

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