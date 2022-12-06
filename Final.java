import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class Final {
	public static void main(String[] args) { // This is a test message
		welcome();
		Card[] deck = new Card[52];
		Profile[] possiblePlayers;
		Profile[] players;
		int numStored;
		//TODO - Implement edwards code "runMenu() - is start
		initDeck(deck);
		//runGame();
		File playerData = new File("player data.txt");
		try {
			checkFile(playerData);
			possiblePlayers = new Profile[getLines(playerData)];
			parseProfile(playerData, possiblePlayers);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

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

	public static int getLines(File file) {
		int counter = 0;
		try (Scanner lineCounter = new Scanner(file)) {
			for (int i = 0; lineCounter.hasNext("\n"); i++) {
				counter++;
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		}
		return counter;
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


	public static void parseProfile(File file, Profile[] players) throws Exception {
		try (Scanner reader = new Scanner(file)) {
			int counter = getLines(file);
			reader.close();
			Scanner readerNew = new Scanner(file);
			for (int i = 0; readerNew.hasNextLine(); i++) {
				String line = readerNew.nextLine();
				String[] data = line.split(",");
				players = new Profile[counter];
				players[i] = new Profile(data[i], Integer.parseInt(data[i + 1]), Integer.parseInt(data[i + 2]), Integer.parseInt(data[i + 3]));
				System.out.println(players[i].toString());
			}

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


	public static void getUserQuantity(Scanner input) {
		System.out.println("How many players are sitting in? (1-6)");
		int playerCount = 0;
		try {
			playerCount = input.nextInt();
			//TODO - Make it throw an exception, not a null output
			if (playerCount < 1 || playerCount > 6 || !input.hasNextInt()) {
				throw new Exception("Invalid Player Count");
			}
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
		runUserSetup(input, playerCount);
	}

	public static void runUserSetup(Scanner input, int playerCount){
		for(int i = 0; i < playerCount; i++) {
			int menuInput = 0;
			System.out.printf("Player %d:%n%n1. Login%n2. New Player%n3. Return to Menu%n", i+1);
			menuInput = input.nextInt();
			if (menuInput == 1) {
				runLogin(input);
			} else if (menuInput == 2) {
				runNewPlayer(input);
			} else if (menuInput == 3) {
				runMenu();
			}
		}
	}
	public static void runMenu() {
		int menuInput = 0;
		System.out.printf("1. Start game%n2. Quit to Desktop%n");
		try (Scanner input = new Scanner(System.in)){
			menuInput = input.nextInt();
			if (menuInput == 1) {
				getUserQuantity(input);
			} else if (menuInput == 2) {
				input.close();
				System.exit(1);
			} else{
				throw new Exception("Invalid input");
			}
		} catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	public static void runLogin(Scanner input) {
		File user;
		for(int i = 0; i < 1; i++){
			try {
				System.out.print("Username: ");
				String userID = input.nextLine();
				if (userID.contains(" ")) {
					input.nextLine();

					throw new Exception("Invalid Username - Cannot Contain Spaces");
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			user = new File("");
		}
	}
	public static void runNewPlayer(Scanner input){
		int userInput = 0;
		System.out.printf("1. New user%n2. Sit as guest");
		try {
			userInput = input.nextInt();
			if (userInput == 1) {
				//TODO - make player able to create profile
			} else if (userInput == 2) {
				//TODO - Allow for profiles to not be stored permanantly
			} else {
				throw new Exception("Invalid Input");
			}
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
	}
	public static void runGame(Scanner input){
		//TODO - Actual Poker Game Code
	}
}