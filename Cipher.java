package aes;
import java.util.Scanner;



public class Cipher {
	private String ciphertext;
	private String plaintext;
	private Key keyStore;
	private CipherBlock blockState;
	
	
	public Cipher() {
		this.ciphertext = null;
		this.plaintext = null;
		this.keyStore = new Key("DontStopTheRock!");
		
	}
	
	public void encrypt() {
		
	}
	public void decrypt() {
		
	}
	
	public void menu() {
		Scanner input = new Scanner(System.in);
		System.out.println("**************************************************");
		System.out.println("AES 16 BIT CIPHER");
		System.out.println("**************************************************");
		System.out.println("**************************************************");
		int uIn = -1;
		do {
			System.out.println("Please select one of the following options.");
			System.out.println("___________________________________________");
			System.out.println("1) Enter plaintext");
			System.out.println("2) Encrypt plaintext.");
			System.out.println("3) Show ciphertext.");
			System.out.println("4) Decrypt ciphertext.");
			System.out.println("5) Show plaintext.");
			System.out.println("6) Clear cipher.");
			System.out.println("7) Quit.");
			uIn = input.nextInt();
			input.nextLine();
			
			switch(uIn) {
			case 1 :
				System.out.println("Enter some plaintext.");
				String pText = input.nextLine();
				this.set_Plaintext(pText);
				break;
			case 2:
				System.out.println("Encrypting plaintext.");
				System.out.println("~~~~~~~~~~~~~~~~~~~~~");
				this.encrypt();
				System.out.println("Encryption complete. Plaintext wiped.");
				System.out.println("~~~~~~~~~~~~~~~~~~~~~");
				break;
			case 3: 
				System.out.println("Ciphertext : " + this.get_Ciphertext());
				break;
			case 4:
				System.out.println("Decrypting ciphertext.");
				System.out.println("~~~~~~~~~~~~~~~~~~~~~");
				this.decrypt();
				
				System.out.println("Decryption complete. Ciphertext wiped.");
				System.out.println("~~~~~~~~~~~~~~~~~~~~~");
				break;
			case 5:
				System.out.println("Plaintext: " + this.get_Plaintext());
				break;
			case 6: 
				this.set_Plaintext(null);
				this.set_Ciphertext(null);
				break;
			case 7:
				System.out.println("Goodbye.");
				break;
				
			}
			
		}while(uIn != 7);
		
	}
	
	
	
	
	
	public void set_Ciphertext(String ciphertext) {
		this.ciphertext = ciphertext;
	}
	
	public String get_Ciphertext() {
		return this.ciphertext;
	}
	
	public void set_Plaintext(String plaintext){
		this.plaintext = plaintext;
	}
	public String get_Plaintext() {
		return this.plaintext;
	}
	
	

}
