package aes;

public class Main {
	public static void main(String[] args) {
		AESCipher testCipher = new AESCipher();
		testCipher.test_SBoxes();
		System.out.println();
		testCipher.k_Store.test_KeyBlock();
		System.out.println();
		testCipher.test_Cipher_Block(true);
		
		
		
		
		
	}

}
