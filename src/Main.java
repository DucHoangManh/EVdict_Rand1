package application;

import java.util.Scanner;

public class Main {	
	private static Scanner input;

	public void process(String n) {
		Manage app = new Manage();
		input = new Scanner(System.in);
		if (!n.equals("exit"))
			switch (n) {
			case "1": {
				System.out.println("Nhap vao tu can tra cuu: ");
				String word = input.nextLine();
				word.toLowerCase();
				System.out.println(app.explain(word));
				break;
			}
			case "2": {
				System.out.println("Tu ban can tim bat dau bang: ");
				String word = input.next();
				word.toLowerCase();
				app.findWords(word);
				break;
			}
			case "3": {
				System.out.println("Nhap vao tu can them: ");
				String word = "";
				word = input.nextLine();
				System.out.println("Nhap vao thong tin cua tu: ");
				String info = "";
				info = input.nextLine();
				word.toLowerCase();
				app.insertWord(word, info);
				System.out.println("Tu '" + word + "' da duoc them vao tu dien!");
				break;
			}
			case "4": {
				System.out.println("Tu can xoa khoi tu dien la: ");
				String word = input.nextLine();
				word.toLowerCase();
				try {
					app.deleteWord(word);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				System.out.println("Tu '" + word + "' da duoc xoa khoi tu dien!");
				break;
			}
			case "5": {
				System.out.println("Nhap vao tu can sua: ");
				String word = " ";
				word = input.nextLine();
				System.out.println("Nhap vao thong tin cua tu: ");
				String info = " ";
				info = input.nextLine();
				word.toLowerCase();
				app.editWord(word, info);
				System.out.println("Tu '" + word + "' da sua thong tin!");
				break;
			}
			default: {
				System.out.println("Ky tu nhap vao khong dung");
			}
			}
	}

	public static void main (String[] args) {
		System.setProperty("file.encoding", "UTF-8");
		System.out.println("EV dict app, Group Rand1");
		String check = "";
		input = new Scanner(System.in);
		while (!check.equals("exit")) {
			System.out.println("Hay lua chon hanh dong:");
			System.out.println("1. Tra tu.");
			System.out.println("2. Tim tu bat dau bang...");
			System.out.println("3. Them tu");
			System.out.println("4. Xoa tu");
			System.out.println("5. Sua nghia cua tu");
			System.out.println("Nhap 'exit' de thoat");
			input = new Scanner(System.in);
			check = input.nextLine();
			Main manage = new Main();
			manage.process(check);
		}

	}
}
