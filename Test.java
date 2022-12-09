
public class Test {
	public static void main(String[] args) {
		String[] rank = new String[1];
		for (int i = 0; i < 13; i++) {
			for (int j = 0; j < 3; j++) {
				if (i == 0) {
					rank[0] = "A";
				} else if (i == 10) {
					rank[0] = "J";
				} else if (i == 11) {
					rank[0] = "Q";
				} else if (i == 12) {
					rank[0] = "K";
				} else {
					rank[0] = Integer.toString(i + 1);
				}
				System.out.print(rank[0]);
			}

		}

	}
}
