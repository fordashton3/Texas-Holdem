import java.io.File;

public class Test {
	public static void main(String[] args) {
		String[] pathnames;
		File f = new File("profiles");
		pathnames = f.list();
		for (int i = 0; i < pathnames.length; i++){
			System.out.println(pathnames[i]);
		}
		//TODO - Only interpret .txt files(If possible)

	}
}
