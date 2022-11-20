package aes;

public class Main {
	public static void main(String[] args) {
		
		
		AESCipher testCipher = new AESCipher();
		main_test(testCipher);
		
		
		
		
		
	}
	public static void main_test(AESCipher testCipher) {
		
		System.out.println("\n*****************************************");
		System.out.println("TESTING SBOX AND ISBOX");
		testCipher.test_SBoxes();
		
		System.out.println("\n*****************************************");
		System.out.println("TESTING KEY BLOCK");
		testCipher.k_Store.test_KeyBlock();
		System.out.println("\n*****************************************");
		System.out.println("TESTING KEY SPACE");
		testCipher.k_Store.test_Funct();
		System.out.println("\n*****************************************");
		System.out.println("TESTING CIHPER BLOCK TRANSCRIPTION.");
		testCipher.test_Cipher_Block(true);
		
		System.out.println("\n*****************************************");
		System.out.println("TESTING SHIFT ROWS FUNCTION");
		testCipher.test_Shift_Rows();
		
		System.out.println("\n*****************************************");
		System.out.println("TESTING MIX COLUMNS FUNCTION");
		testCipher.test_Mix_Columns();
		
		
	}

}
