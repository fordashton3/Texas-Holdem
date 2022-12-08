public class Card {
	private String rank;
	private char suit;
	private int value;

	public Card(String rank, char suit, int value){
		this.rank = rank;
		this.suit = suit;
		this.value = value;
	}

	public String getRank() {
		return rank;
	}

	public char getSuit() {
		return suit;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public void setSuit(char suit) {
		this.suit = suit;
	}

	public void setRank(String rank) {
		this.rank = rank;

	}

	public String toString(){
		return String.format("%s%c", rank, suit);
	}
}
