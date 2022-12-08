public class Hand {
	private int cardOne;
	private int cardTwo;
	private Card[] hand;

	public Hand(Card[] hand){
		hand = new Card[7];
		this.hand = hand;
		cardOne = 0;
		cardTwo = 1;
	}

	public int getCardOne() {
		return cardOne;
	}

	public int getCardTwo() {
		return cardTwo;
	}

	public void setCardOne(int cardOne) {
		this.cardOne = cardOne;
	}

	public void setCardTwo(int cardTwo) {
		this.cardTwo = cardTwo;
	}

	public String toString() {
		return String.format("%s  %s",hand[cardOne], hand[cardTwo]);
	}
	public String toStringAll() {
		return String.format("%s  %s  %s  %s  %s  %s  %s", hand[0], hand[1], hand[2], hand[3], hand[4], hand[5], hand[6]);
	}
}
