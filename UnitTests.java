package aes;

public class UnitTests {
	public static void main(String [] args) {
		
		sub_Bytes_Test();
		
		
		
		
	}
	
	public static void encrypt_Test() {
		CipherBlock cipher = new CipherBlock("abcdefghijklmnop");
		Key cipherKey = new Key("mlp123jkzv-08((&");
		
		System.out.println("Block state init: \n");
		
		cipher.test_Block();
		System.out.println("Key state init: \n");
		cipherKey.test_KeyBlock(0);
		
		
		cipher.add_Round_Key(cipherKey.get_Key_State(0));
		System.out.println("\nAfter initial add key: \n");
		cipher.test_Block();
		
		for(int i = 1; i < 11; i++) {
			System.out.println("\n********************************************");
			System.out.println("\n********************************************");
			System.out.println("ROUND " + i);
			cipher.sub_Bytes();
			System.out.println("\n___________________________________________");
			System.out.println("AFTER SUB-BYTES ROUND " + i + "\n");
			cipher.test_Block();
			cipher.shift_Rows();
			System.out.println("\n___________________________________________");
			System.out.println("AFTER SHIFT ROWS ROUND " + i + "\n");
			cipher.test_Block();
			cipher.mix_Columns();
			System.out.println("\n___________________________________________");
			System.out.println("AFTER MIX COLUMNS ROUND  " + i + "\n"); 
			cipher.test_Block();
			cipher.add_Round_Key(cipherKey.get_Key_State(i));
			System.out.println("\n___________________________________________");
			System.out.println("AFTER ROUND KEY " + i + " ADD KEY\n");
			cipherKey.test_KeyBlock(i);
			cipher.test_Block();
			
		}
	}
	
	public static void mix_Columns_Test() {
		CipherBlock block = new_Test_Block();
		Key key = new_Test_Key();
		System.out.println(block.out_Block());
		System.out.println("********************************************");
		System.out.println("TESTING MIX COLUMNS");
		System.out.println("PRE-MIX STATES");
		block.test_Block();
		key.test_KeyBlock(0);
		System.out.println();
		System.out.println("********************************************");
		System.out.println("MIXING COLUMNS");
		block.mix_Columns();
		System.out.println("POST-MIX BLOCK STATE");
		block.test_Block();
		System.out.println(block.out_Block());
		System.out.println("********************************************");
		System.out.println("INVERSE MIXING");
		block.invmix_Columns();
		block.test_Block();
		System.out.println("out state: ");
		System.out.println(block.out_Block());
		
	}
	
	public static void shift_Rows_Test() {
		CipherBlock block = new_Test_Block();
		
		System.out.println("TESTING SHIFT ROWS");
		System.out.println("********************************************");
		
		block.test_Block();
		System.out.println("Out Block test : " + block.out_Block());
		System.out.println("SHIFTING ROWS");
		System.out.println("********************************************");
		block.shift_Rows();
		block.test_Block();
		System.out.println("Out Block test : " + block.out_Block());
		System.out.println("INVERSE SHIFTING");
		System.out.println("********************************************");
		block.invshift_Rows();
		block.test_Block();
		System.out.println("Out Block test : " + block.out_Block());
		
		
		
	}
	
	public static void add_Key_Test() {
		CipherBlock block = new_Test_Block();
		Key key = new_Test_Key();
		System.out.println("********************************************");
		System.out.println("TESTING ADD ROUND KEY");
		block.test_Block();
		System.out.println("Out Block test : " + block.out_Block());
		key.test_KeyBlock(0);
		System.out.println("********************************************");
		System.out.println("ADDING ROUND KEY");
		block.add_Round_Key(key.get_Key_State(0));
		System.out.println("********************************************");
		System.out.println("POST ROUND KEY BLOCK STATE: ");
		block.test_Block();
		System.out.println("Out Block test : " + block.out_Block());
		System.out.println("********************************************");
		System.out.println("INVERSE ADD ROUND KEY");
		block.add_Round_Key(key.get_Key_State(0));
		System.out.println("********************************************");
		System.out.println("BLOCK STATE");
		block.test_Block();
		System.out.println("Out Block test : " + block.out_Block());
		
		
		
		
	}
	
	public static void sub_Bytes_Test() {
		CipherBlock block = new_Test_Block();
		System.out.println("********************************************");
		System.out.println("TESTING SUB-BYTES");
		block.test_Block();
		System.out.println("Out Block test : " + block.out_Block());
		block.sub_Bytes();
		System.out.println("********************************************");
		System.out.println("POST SUB-BYTES");
		block.test_Block();
		System.out.println("Out Block test : " + block.out_Block());
		System.out.println("********************************************");
		System.out.println("TESTING INVERSE-SUB-BYTES");
		block.invsub_Bytes();
		block.test_Block();
		System.out.println("Out Block test : " + block.out_Block());
		
		
		
	}
	
	public static Key new_Test_Key() {
		return new Key("mlp123jkzv-08((&");
	}
	public static CipherBlock new_Test_Block() {
		return new CipherBlock("abcdefghijklmnop");
	}
	

}
