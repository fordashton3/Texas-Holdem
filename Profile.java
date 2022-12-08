@SuppressWarnings("unused")
public class Profile {
	private String name;
	private int balance;
	private int wins;
	private int games;
	private boolean save;
	private Card[] hand;
	private int cardOne;
	private int cardTwo;
	private double ratio;


	public Profile(String name, int balance, int wins, int games, boolean save) {
		this.name = name;
		this.balance = balance;
		this.wins = wins;
		this.games = games;
		this.save = save;
		this.hand = new Card[7];
		this.ratio = (double) wins / games;
		cardOne = 0;
		cardTwo = 1;
	}

	public String getName() {
		return name;
	}

	public int getBalance() {
		return balance;
	}

	public int getWins() {
		return wins;
	}

	public int getGames() {
		return games;
	}

	public boolean isSave() {
		return save;
	}

	public Card[] getHand() {
		return hand;
	}

	public Card getCard(int index) {
		return hand[index];
	}

	public double getRatio() {
		return ratio;
	}

	public int getCardOne() {
		return cardOne;
	}

	public int getCardTwo() {
		return cardTwo;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}

	public void setGames(int games) {
		this.games = games;
	}

	public void setSave(boolean save) {
		this.save = save;
	}

	public void setHand(Card deck, int index) {
		this.hand[index] = deck;
	}

	public void setCardOne(int cardOne) {
		this.cardOne = cardOne;
	}

	public void setCardTwo(int cardTwo) {
		this.cardTwo = cardTwo;
	}

	public String handToString() {
		return String.format("%-3s  %s", hand[cardOne], hand[cardTwo]);
	}

	public String toString() {
		return String.format("%s,%d,%d,%d,%b", name, balance, wins, games, save);
	}
}
