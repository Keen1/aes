package aes;

import aes.Key;
import aes.CipherBlock;
//cipher body
public class AESCipher {
	private String plaintext;
	private String ciphertext;
	private String key;
	CipherBlock stateBlock = new CipherBlock();
	Key k_Store;
	private int[][] sBox = {
			{99, 124, 119, 123, 242, 107, 111, 197, 48, 1, 103, 43, 254, 215, 171, 118},
			{202, 130, 201, 125, 250, 89, 71, 240, 173, 212, 162, 175, 156, 164, 114, 192},
			{183, 253, 147, 38, 54, 63, 247, 204, 52, 165, 229, 241, 113, 216, 49, 21},
			{4, 199, 35, 195, 24, 150, 5, 154, 7, 18, 128, 226, 235, 39, 178, 117},
			{9, 131, 44, 26, 27, 110, 90, 160, 82, 59, 214, 179, 41, 227, 47, 132},
			{83, 209, 0, 237, 32, 252, 177, 91, 106, 203, 190, 57, 74, 76, 88, 207},
			{208, 239, 170, 251, 67, 77, 51, 133, 69, 249, 2, 127, 80, 60, 159, 168},
			{81, 163, 64, 143, 146, 157, 56, 245, 188, 182, 218, 33, 16, 255, 243, 210},
			{205, 12, 19, 236, 95, 151, 68, 23, 196, 167, 126, 61, 100, 93, 25, 115},
			{96, 129, 79, 220, 34, 42, 144, 136, 70, 238, 184, 20, 222, 94, 11, 219},
			{224, 50, 58, 10, 73, 6, 36, 92, 194, 211, 172, 98, 145, 149, 228, 121},
			{231, 200, 55, 109, 141, 213, 78, 169, 108, 86, 244, 234, 101, 122, 174, 8},
			{186, 120, 37, 46, 28, 166, 180, 198, 232, 221, 116, 31, 75, 189, 139, 138},
			{112, 62, 181, 102, 72, 3, 246, 14, 97, 53, 87, 185, 134, 193, 29, 158},
			{225, 248, 152, 17, 105, 217, 142, 148, 155, 30, 135, 233, 206, 85, 40, 223},
			{140, 161, 137, 13, 191, 230, 66, 104, 65, 153, 45, 15, 176, 84, 187, 22}};
	private int[][] isBox = {
			{82, 9, 106, 213, 48, 54, 165, 56, 191, 64, 163, 158, 129, 243, 215, 251},
			{124, 227, 57, 130, 155, 47, 255, 135, 52, 142, 67, 68, 196, 222, 233, 203},
			{84, 123, 148, 50, 166, 194, 35, 61, 238, 76, 149, 11, 66, 250, 195, 78},
			{8, 46, 161, 102, 40, 217, 36, 178, 118, 91, 162, 73, 109, 139, 209, 37},
			{114, 248, 246, 100, 134, 104, 152, 22, 212, 164, 92, 204, 93, 101, 182, 146},
			{108, 112, 72, 80, 253, 237, 185, 218, 94, 21, 70, 87, 167, 141, 157, 132},
			{144, 216, 171, 0, 140, 188, 211, 10, 247, 228, 88, 5, 184, 179, 69, 6},
			{208, 44, 30, 143, 202, 63, 15, 2, 193, 175, 189, 3, 1, 19, 138, 107},
			{58, 145, 17, 65, 79, 103, 220, 234, 151, 242, 207, 206, 240, 180, 230, 115},
			{150, 172, 116, 34, 231, 173, 53, 133, 226, 249, 55, 232, 28, 117, 223, 110},
			{71, 241, 26, 113, 29, 41, 197, 137, 111, 183, 98, 14, 170, 24, 190, 27},
			{252, 86, 62, 75, 198, 210, 121, 32, 154, 219, 192, 254, 120, 205, 90, 244},
			{31, 221, 168, 51, 136, 7, 199, 49, 177, 18, 16, 89, 39, 128, 236, 95},
			{96, 81, 127, 169, 25, 181, 74, 13, 45, 229, 122, 159, 147, 201, 156, 239},
			{160, 224, 59, 77, 174, 42, 245, 176, 200, 235, 187, 60, 131, 83, 153, 97},
			{23, 43, 4, 126, 186, 119, 214, 38, 225, 105, 20, 99, 85, 33, 12, 125}};
	
	//no param init
	AESCipher(){
		
		this.plaintext = "testgkvn12388kln";
		this.ciphertext = "";
		this.key = "";
		this.k_Store = new Key("abvdo10alznl>8?m");
		
	}
	
	//encrypt method
	//builds a key and a cipher block
	
	public void encrypt() {
		
		}
	
	//getters and setters
	public void set_Plaintext(String plaintext) {
		this.plaintext = plaintext;
	}
	public String get_Plaintext() {
		return this.plaintext;
	}
		
	public void set_Ciphertext(String ciphertext) {
		this.ciphertext = ciphertext;
	}
	public String get_Ciphertext() {
		return this.ciphertext;
	}
		
	public void set_Key(String key) {
		this.key = key;
	}
	public String get_Key() {
		return this.key;
	}
	
	
	
	
	
	
	
	
	/*
	 * test() methods
	 * Methods intended for testing and verifying results
	 * */
	public void test_Cipher_Block(boolean testInput) {
		
		if(testInput) {
			System.out.println("\nCurrent Block State: " + "\nTest input : abcdefghijklmnop" + 
				"\n___________________________________________");
		
			this.stateBlock.test_input("abcdefghijklmnop");
			this.stateBlock.test_Block();
			
		}else {
			this.stateBlock.test_Block();
		}
		this.stateBlock = new CipherBlock();
		
	}
	
	public void test_Mix_Columns() {
		System.out.println("\n");
		System.out.println("\nCurrentBlock State: " + "\n___________________________________________");
		
		
		this.stateBlock.test_input("YNJ>?-.&OP@!#KSL");
		this.stateBlock.test_Block();
		System.out.println("\n\nPerform mix columns operation");
		this.stateBlock.mix_Columns();
		System.out.println("\n");
		System.out.println("\nCurrentBlock State: " + "\n___________________________________________");
		this.stateBlock.test_Block();
		
	}
	
	public void test_Shift_Rows() {
		this.stateBlock.test_input("123456789ABCDEFG");
		
		System.out.println("\nCurrentBlock State: " + "\n___________________________________________");
		this.stateBlock.test_Block();
		System.out.println("\nPerform shift rows operations");
		this.stateBlock.shift_Rows();
		System.out.println("\nCurrentBlock State: " + "\n___________________________________________");
		this.stateBlock.test_Block();
		
		this.stateBlock = new CipherBlock();
	}
	
	public void test_SBoxes() {
		System.out.println("S-BOX HEXADECIMAL VALUES");
		System.out.println("___________________________________________");
		for(int i = 0; i < this.sBox.length; i++) {
			System.out.println();
			for(int k = 0; k < this.sBox[i].length; k++) {
				String hexS = Integer.toHexString(this.sBox[i][k]);
				if(hexS.length() == 1) {
					String hexFull = "0";
					hexFull += hexS;
					hexS = hexFull;
				}
				hexS = hexS.toUpperCase();
				System.out.print(hexS);
				System.out.print(" ");
			}
			
		}
		System.out.println();
		System.out.println("___________________________________________");
		System.out.println();
		
		System.out.println("INVERSE-S-BOX HEXADECIMAL VALUES");
		System.out.println("___________________________________________");
		
		for(int i = 0; i < this.isBox.length; i++) {
			System.out.println();
			for(int k = 0; k < this.isBox[i].length; k++) {
				String hexS = Integer.toHexString(this.isBox[i][k]);
				if(hexS.length() == 1) {
					String hexFull = "0";
					hexFull += hexS;
					hexS = hexFull;
				}
				hexS = hexS.toUpperCase();
				System.out.print(hexS);
				System.out.print(" ");
			}
		}
		this.stateBlock = new CipherBlock();
	}
	
}
