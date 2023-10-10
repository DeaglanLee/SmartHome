package Application;

import java.util.Scanner;

public class Menu {
	private String items[];
	private String title;
	private Scanner input;

	public Menu(String title, String data[]) {
		this.title = title;
		this.items = data;
		this.input = new Scanner(System.in);
	}

	public byte getUserChoice() {
		byte value = 0;
		boolean ok =false;
		display();
		do{
			System.out.print("Enter Selection: ");
			try{
				value = input.nextByte();
				if(value >=1 && value <= items.length){
					ok = true;
				}else{
					System.out.println("not valid");
				}
			}catch (Exception e){
				System.out.println("Not valid");
				input.nextLine();
			}
		}while (!ok);
		return value;
	}

	private void display() {
		System.out.println("\n\n"+title);
		for (short i = 0; i < title.length(); i++) {
			System.out.print("+");
		}
		System.out.println();
		for (short opt = 1; opt <= items.length; opt++) {
			System.out.println(opt + ". " + items[opt - 1]);
		}
		System.out.println();
	}

}
