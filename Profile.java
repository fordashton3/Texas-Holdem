public class Profile {
	private String name;
	private int balance;
	private int wins;
	private int games;
	private boolean save;
	private double ratio;


	public Profile(String name, int balance, int wins, int games, boolean save) {
		this.name = name;
		this.balance = balance;
		this.wins = wins;
		this.games = games;
		this.save = save;
		this.ratio = wins/games;
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

	public double getRatio() {
		return ratio;
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

	public String toString() {
		return String.format("%s,%d,%d,%d\n", name, balance, wins, games);
	}
}
