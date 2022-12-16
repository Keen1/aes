package aes;
import java.util.HashMap;
import java.util.Scanner;
/*
 * AES 10 ROUND CIPHER IMPLEMENTATION 
 * TO DO: 
 * Implement the mix columns and the inverse mix columns using the mathematical principles of the GF(2^8) with m(x) = 100011011
 * s-box arrays source provided in top comment in subBoxes class
 * 
 * testing via block printing can be done by changing the boolean values for encrypt() and decrypt() to true*/
public class Cipher {
	//attributes
	private int[][] stateBlock = new int[4][4];
	private SubBoxes sBoxes = new SubBoxes();
	private char[] rc_Values = {1,2,4,8,16,32,64,128,27,54};
	private HashMap<Integer, int[][]> keySpace = new HashMap<>();
	
	private String ciphertext, plaintext, key;
	
	//constructor
	public Cipher() {
		ciphertext = "";
		plaintext = "";
		key = "abcdefghijklmnop";
		this.init_Key_State();
		this.gen_Key_Space();
	}
	
	/*
	 * ENCRYPT AND DECRYPT METHODS
	 * 
	 * */
	private void encrypt(boolean test) {
		
		if(test) {
			System.out.println("**************************************" + "\nENCRYPTING");
		}
		
		//get a temp string of plaintext so as not to delete plaintext
		String tempPlain = this.get_Plaintext();
		//while there is plaintext
		while(tempPlain.length() > 0) {
			//if plaintext is 16 chars or less just write it to the block since it wont be out of bounds
			if(tempPlain.length() <= 16) {
				this.init_StateBlock(tempPlain);
				//set the plaintext to empty to end the loop.
				tempPlain = "";
				
			}else {
				//if the string is longer than 16 chars get the first 16 chars and then remove them from the temp string.
				this.init_StateBlock(tempPlain.substring(0, 16));
				tempPlain = tempPlain.substring(16);
			}
			if(test) {
				System.out.println("**************************************" + "\nINITIAL BLOCK STATE");
				this.test_Block();
			}
			//block is written add key state 0
			this.add_Round_Key(this.keySpace.get(0));
			
			if(test) {
				System.out.println("**************************************" + "\nBLOCK STATE AFTER ADDING INITIAL ROUND KEY");
				this.test_Block();
				System.out.println("**************************************" + "\nROUND KEY: ");
				this.test_Key(0);
			}
			
			
			
			//encrypt the block
			for(int i = 1; i < 11; i++) {
				this.encrypt_Round(i, test);
				if(test) {
					System.out.println("**************************************" + "\nBLOCK STATE ROUND " + i + " RESULT");
					this.test_Block();
				}
				
			}
			
			//write the block to cipher text
			this.ciphertext += this.out_stateBlock();
		}
	}
	//encrypt round helper
	private void encrypt_Round(int round, boolean test) {
		//if its the last round do not do the full round
		if(test) {
			System.out.println("**************************************" + "\nENCRYPT ROUND " + round);
		}
		
		if(round == 10) {
			this.sub_Bytes();
			
			if(test) {
				System.out.println("**************************************" + "\nFINAL ROUND POST SUB BYTE");
				this.test_Block();
			}
			this.shift_Rows();
			
			if(test) {
				System.out.println("**************************************" + "\nFINAL ROUND POST SHIFT ROWS");
				this.test_Block();
			}
			
			this.add_Round_Key(this.keySpace.get(round));
			
			if(test) {
				System.out.println("**************************************" + "\nFINAL ROUND POST ADD KEY");
				this.test_Block();
				System.out.println("Key : ");
				this.test_Key(round);
			}
			
			
		}else {
			
			this.sub_Bytes();
			if(test) {
				System.out.println("**************************************" + "\n " + round + "th ROUND POST SUB BYTES");
				this.test_Block();
			}
			
			this.shift_Rows();
			if(test) {
				System.out.println("**************************************" + "\n " + round + "th ROUND POST SHIFT ROWS");
				this.test_Block();
			}
			
			this.mix_Columns();
			if(test) {
				System.out.println("**************************************" + "\n " + round + "th ROUND POST MIX COLUMNS");
				this.test_Block();
			}
			
			this.add_Round_Key(this.keySpace.get(round));
			if(test) {
				System.out.println("**************************************" + "\n " + round + "th ROUND POST ADD KEY");
				this.test_Block();
				System.out.println("Key : ");
				this.test_Key(round);
			}
		}
		
		
	}
	/*returns a string for ouput.
	 * functionally identical to encrypt save inverse operations are used in decrypt round and we count backwards through 
	 * the key space hashmap 9 to 0*/
	public String decrypt(boolean test) {
		if(test) {
			System.out.println("**************************************" + "\nDECRYPTING");
		}
		String tempCiph = this.get_Ciphertext();
		String msg = "";
		while(tempCiph.length() > 0) {
			if(tempCiph.length() <= 16) {
				this.init_StateBlock(tempCiph);
				tempCiph = "";
			}else {
				this.init_StateBlock(tempCiph.substring(0,16));
				tempCiph = tempCiph.substring(16);
			}
			
			if(test) {
				System.out.println("**************************************" + "\nINITIAL CIPHER BLOCK STATE");
				this.test_Block();
			}
			//block is written, add key state 10
			this.add_Round_Key(this.keySpace.get(10));
			if(test) {
				System.out.println("**************************************" + "\nCIPHER BLOCK POST ADD ROUND 10 KEY");
				this.test_Block();
				System.out.println("Key: ");
				this.test_Key(10);
			}
			//decrypt the block
			for(int i = 9; i > -1; i--) {
				this.decrypt_Round(i, test);
				if(test) {
					System.out.println("**************************************" + "\nDECRYPT ROUND " + i + " RESULT");
					this.test_Block();
				}
			}
			msg += this.out_stateBlock();
		}
		return msg;
	}
	
	public void decrypt_Round(int round, boolean test) {
		if(test) {
			System.out.println("**************************************" + "\nDECRYPT ROUND " + round);
		}
		//if its the last round, dont do the full round.
		if(round == 0) {
			this.invshift_Rows();
			if(test) {
				System.out.println("**************************************" + "\nFINAL ROUND POST INVERSE SHIFT ROWS");
				this.test_Block();
			}
			
			this.invsub_Bytes();
			if(test) {
				System.out.println("**************************************" + "\nFINAL ROUND POST INVERSE SUB BYTE");
				this.test_Block();
			}
			this.add_Round_Key(this.keySpace.get(round));
			if(test) {
				System.out.println("**************************************" + "\nFINAL ROUND POST ADD ROUND KEY");
				this.test_Block();
			}
		}else {
			this.invshift_Rows();
			if(test) {
				System.out.println("**************************************" + "\n " + round + "TH ROUND POST INVERSE SHIFT ROWS");
				this.test_Block();
			}
			
			this.invsub_Bytes();
			if(test) {
				System.out.println("**************************************" + "\n " + round + "TH ROUND POST INVERSE SUB BYTES");
				this.test_Block();
			}
			
			this.add_Round_Key(this.keySpace.get(round));
			if(test) {
				System.out.println("**************************************" + "\n " + round + "TH ROUND POST ADD ROUND KEY ");
				this.test_Block();
				System.out.println("Key: ");
				this.test_Key(round);
			}
			
			this.invmix_Columns();
			if(test) {
				System.out.println("**************************************" + "\n " + round + "TH ROUND POST ADD ROUND KEY ");
				this.test_Block();
			}
		}
		
	}
	
	
	
	
	/*
	 * KEY OPERATIONS
	 * generate methods used in constructor to initialize entire keyspace as a hashmap
	 * */
	
	//write the key to the key block as the initial state
	private void init_Key_State() {
		
		int count = 0;
		int[][] keyState = new int[4][4];
		for(int i = 0; i < keyState.length; i++) {
			for(int k = 0; k < keyState[i].length; k++) {
				keyState[k][i] = this.key.charAt(count);
				count++;
				
			}
		}
		keySpace.put(0,keyState);
	}
	//generate each round block of the key
	private void gen_Key_Space() {
		for(int i = 1; i < 11; i++) {
			this.generate_Key_Block(i);
		}
	}
	//generate a key block given a integer round
	private void generate_Key_Block(int round) {
		int[][] roundBlock = new int[4][4];
		
		int[] currentWord = this.keySpace.get(round - 1)[3];
		currentWord = this.gen_G(currentWord, round);
		roundBlock[0] = currentWord;
		
		for(int i = 1; i < roundBlock.length; i++) {
			roundBlock[i] = this.generate_Word_Bytes(roundBlock, i, round);
		}
		keySpace.put(round, roundBlock);
	}
	
	//generate the word of the given round in the ith row
	private int[] generate_Word_Bytes(int[][] roundBlock, int blockRow, int round) {
		int[] newWord = new int[4];
		int[] wordDiff1 = roundBlock[blockRow - 1];
		int[] wordDiff4 = this.keySpace.get(round - 1)[blockRow];
		for(int i = 0; i < newWord.length; i++) {
			newWord[i] = wordDiff1[i]^wordDiff4[i];
		}
		return newWord;
	}
	//special generate g function used to generate any word whose index in the key space % 4 == 0
	//this turns out to be the first word of every block per round 
	public int[] gen_G(int[] currentWord, int round) {
		//1 byte left circ shift
		int temp = currentWord[0];
		currentWord[0] = currentWord[1];
		currentWord[1] = currentWord[2];
		currentWord[2] = currentWord[3];
		currentWord[3] = temp;
		
		for(int i = 0; i < currentWord.length; i++) {
			String hexString = Integer.toHexString(currentWord[i]);
			String bufferedString = "0";
			if(hexString.length() < 2) {
				bufferedString += hexString;
				hexString = bufferedString;
			}
			
			String hexRow = String.valueOf(hexString.charAt(0));
			int matrixRow = Integer.parseInt(hexRow, 16);
			
			String hexCol = String.valueOf(hexString.charAt(1));
			int matrixCol = Integer.parseInt(hexCol, 16);
			
			currentWord[i] = this.sBoxes.get_S_Byte(matrixRow, matrixCol);
		}
		int[] rcWord = new int[4];
		rcWord[0] = this.rc_Values[round - 1];
		for(int i = 0; i < currentWord.length; i++) {
			currentWord[i] = currentWord[i]^rcWord[i];
		}
		return currentWord;
	}
	
	
	
	/*
	 * BLOCK OPERATIONS 
	 * 
	 * 
	 * */
	
	//initialize the state block given a string
	private void init_StateBlock(String bytes) {
		if(bytes.length() == 0 || bytes == null) {
			return;
		}else {
			int cnt = 0;
			for(int i = 0; i < this.stateBlock.length; i++) {
				for(int k = 0; k < this.stateBlock[i].length; k++) {
					if(cnt < bytes.length()) {
						this.stateBlock[k][i] = bytes.charAt(cnt);
						cnt++;
					}else {
						this.stateBlock[k][i] = 0;
					}
				}
			}
			
		}
	}
	//write out the result of the block
	private String out_stateBlock() {
		String blockString = "";
		for(int i = 0; i < this.stateBlock.length; i++) {
			for(int k = 0; k < this.stateBlock[i].length; k++) {
				blockString += (char)this.stateBlock[k][i];
			}
		}
		this.stateBlock = new int[4][4];
		return blockString;
	}
	//add the round key to the block
	private void add_Round_Key(int[][] roundKey) {
		for(int i = 0; i < this.stateBlock.length; i++) {
			for(int k = 0; k < this.stateBlock[i].length; k++) {
				this.stateBlock[i][k] = (stateBlock[i][k]^roundKey[i][k]);
			}
		}
	}
	/*
	 * CURRENT MIX COLUMNS OP STATUS:
	 * doing look up tables in subBoxes class given scalar multiple, want to implement the actual 
	 * gf math but wasnt working with bit shifting and xoring*/
	//mix columns operation over galois field (2^8) given m(x) = x^8 + x^4 + x^3 +x + 1 or 100011011 or 11b or 283
	private void mix_Columns() {
		//matrix factor that premultiples the stateBlock over gf(2^8)
		int[][] matrixFactor = {
				{2, 3, 1, 1},
				{1, 2, 3, 1},
				{1, 1, 2, 3},
				{3, 1, 1, 2}};
		
		//resulting mixed block
		int[][] mixedBlock = new int[4][4];
		//scalar row per column
		int[] scalarRow;
		
		for(int i = 0; i < this.stateBlock.length; i++) {
			//get the current row
			scalarRow = matrixFactor[i];
			for(int k = 0; k < this.stateBlock[i].length; k++) {
				
				//get the current state column to be multiplied 
				int[] stateColumn = {this.stateBlock[0][k], this.stateBlock[1][k], this.stateBlock[2][k], this.stateBlock[3][k]};
				
				
				//get the mixed block index of the state after being multiplied
				mixedBlock[i][k] = this.matrix_GF_Mult_Op(scalarRow, stateColumn);
			}
		}
		this.stateBlock = mixedBlock;
	}
	
	//inverse mix columns operation
	private void invmix_Columns() {
		//inverse matrix factor
		
		int[][] matrixFactor = {
				{14, 11, 13, 9},
				{9, 14, 11, 13},
				{13, 9, 14, 11},
				{11, 13, 9, 14}};
		
		//resulting mixed block
		int[][] mixedBlock = new int[4][4];
		int[] scalarRow;
		
		for(int i = 0; i < this.stateBlock.length; i++) {
			//get the current scalar row
			scalarRow = matrixFactor[i];
			
			for(int k = 0; k < this.stateBlock[i].length; k++) {
				//get the current state column
				int[] stateColumn = {this.stateBlock[0][k], this.stateBlock[1][k], this.stateBlock[2][k], this.stateBlock[3][k]};
				//get the mixedBlock index after completing the matrix mult operation over gf(2^8)
				mixedBlock[i][k] = this.matrix_GF_Mult_Op(scalarRow, stateColumn);
			}
		}
		this.stateBlock = mixedBlock;
				
		
	}
	
	//matrix multiplication operations
	//was previously attempting to math this out but had to do lookup tables for the scalars as i was exceeding the bounds of the field
	private int matrix_GF_Mult_Op(int[] scalarRow, int[] stateColumn) {
		for(int i = 0; i < scalarRow.length; i++) {
			if(scalarRow[i] == 2) {
				String hexString = Integer.toHexString(stateColumn[i]);
				int[] rc = this.hexTransHelper(hexString);
				stateColumn[i] = this.sBoxes.get_Mult2(rc[0], rc[1]);
				
			}
			if(scalarRow[i] == 3) {
				String hexString = Integer.toHexString(stateColumn[i]);
				int[] rc = this.hexTransHelper(hexString);
				stateColumn[i] = this.sBoxes.get_Mult3(rc[0], rc[1]);
				
			}
			if(scalarRow[i] == 9) {
				String hexString = Integer.toHexString(stateColumn[i]);
				int[] rc = this.hexTransHelper(hexString);
				stateColumn[i] = this.sBoxes.get_Mult9(rc[0], rc[1]);
			}
			if(scalarRow[i] == 11) {
				String hexString = Integer.toHexString(stateColumn[i]);
				int[] rc = this.hexTransHelper(hexString);
				stateColumn[i] = this.sBoxes.get_Mult11(rc[0], rc[1]);
			}
			if(scalarRow[i] == 13) {
				String hexString = Integer.toHexString(stateColumn[i]);
				int[] rc = this.hexTransHelper(hexString);
				stateColumn[i] = this.sBoxes.get_Mult13(rc[0], rc[1]);
			}
			if(scalarRow[i] == 14) {
				String hexString = Integer.toHexString(stateColumn[i]);
				int[] rc = this.hexTransHelper(hexString);
				stateColumn[i] = this.sBoxes.get_Mult14(rc[0], rc[1]);
			}
		}
		return (stateColumn[0]^stateColumn[1]^stateColumn[2]^stateColumn[3]);
	}
	//hex translate helper
	private int[] hexTransHelper(String hexString) {
		int[] matrix_RowCol = new int[2];
		String bufferedString = "0";
		if(hexString.length() < 2) {
			bufferedString += hexString;
			hexString = bufferedString;
		}
		
		String hexRow = String.valueOf(hexString.charAt(0));
		matrix_RowCol[0] = Integer.parseInt(hexRow, 16);
		
		String hexCol = String.valueOf(hexString.charAt(1));
		
		matrix_RowCol[1] = Integer.parseInt(hexCol, 16);
		return matrix_RowCol;
	}
	//any operation that results in an 8th bit gets xored with the prime poly 0b100011011
	/*NOT IN USE
	 * leaving this here for further attempts, block values expanding to 3hex digits when attempting to bit shift and xor
	 * in the inverse mix col function
	 * private int mod_Prime_Poly(int value) {
		if(value >= 256) {
			return value^283;
		}
		else {
			return value;
		}
	}*/
	
	//sub-bytes operation
	private void sub_Bytes() {
		
		for(int i = 0; i < this.stateBlock.length; i++) {
			for(int k = 0; k < this.stateBlock[i].length; k++) {
				//convert the int index to a hexString and then get the row and column
				String hexString = Integer.toHexString(this.stateBlock[i][k]);
				
				String bufferedString = "0";
				
				if(hexString.length() < 2) {
					bufferedString += hexString;
					hexString = bufferedString;
				}
				String hexRow = String.valueOf(hexString.charAt(0));
				int matrixRow = Integer.parseInt(hexRow, 16);
				
				String hexCol = String.valueOf(hexString.charAt(1));
				int matrixCol = Integer.parseInt(hexCol, 16);
				
				this.stateBlock[i][k] = this.sBoxes.get_S_Byte(matrixRow, matrixCol);
			}
		}
	}
	//inverse-sub-bytes operation, carbon copy of sub_Bytes but pulls sub from invers sbox
	private void invsub_Bytes() {
		
		for(int i = 0; i < this.stateBlock.length; i++) {
			for(int k = 0; k < this.stateBlock[i].length; k++) {
				String hexString = Integer.toHexString(this.stateBlock[i][k]);
				
				String bufferedString = "0";
				if(hexString.length() < 2) {
					bufferedString += hexString;
					hexString = bufferedString;
				}
				
				String hexRow = String.valueOf(hexString.charAt(0));
				int matrixRow = Integer.parseInt(hexRow, 16);
				
				String hexCol = String.valueOf(hexString.charAt(1));
				int matrixCol = Integer.parseInt(hexCol, 16);
				
				this.stateBlock[i][k] = (char)this.sBoxes.get_IS_Byte(matrixRow, matrixCol);
			}
		}
	}
	
	//shift rows operation
	private void shift_Rows() {
		//first row left alone
		
		//second row gets 1 byte left circ shift
		int temp = this.stateBlock[1][0];
		this.stateBlock[1][0] = this.stateBlock[1][1];
		this.stateBlock[1][1] = this.stateBlock[1][2];
		this.stateBlock[1][2] = this.stateBlock[1][3];
		this.stateBlock[1][3] = temp;
		
		//third row gets a 2 bytes left circ shift
		temp = this.stateBlock[2][3];
		int temp2 = this.stateBlock[2][2];
		this.stateBlock[2][3] = this.stateBlock[2][1];
		this.stateBlock[2][2] = this.stateBlock[2][0];
		this.stateBlock[2][1] = temp;
		this.stateBlock[2][0] = temp2;
		
		//fourth row gets 1 byte right circ shift
		temp = this.stateBlock[3][3];
		this.stateBlock[3][3] = this.stateBlock[3][2];
		this.stateBlock[3][2] = this.stateBlock[3][1];
		this.stateBlock[3][1] = this.stateBlock[3][0];
		this.stateBlock[3][0] = temp;
	}
	
	//inverse shift rows operation
	private void invshift_Rows() {
		//leave first row alone
		
		//second row inverse of shift_Rows, 1 byte right circ shift
		int temp = this.stateBlock[1][3];
		this.stateBlock[1][3] = this.stateBlock[1][2];
		this.stateBlock[1][2] = this.stateBlock[1][1];
		this.stateBlock[1][1] = this.stateBlock[1][0];
		this.stateBlock[1][0] = temp;
		
		//third row inverse is two byte right cir shift
		
		temp = this.stateBlock[2][0];
		int temp2 = this.stateBlock[2][1];
		
		this.stateBlock[2][0] = this.stateBlock[2][2];
		this.stateBlock[2][1] = this.stateBlock[2][3];
		this.stateBlock[2][2] = temp;
		this.stateBlock[2][3] = temp2;
		
		//fourth row inverse is 1 byte left shift
		temp = this.stateBlock[3][0];
		this.stateBlock[3][0] = this.stateBlock[3][1];
		this.stateBlock[3][1] = this.stateBlock[3][2];
		this.stateBlock[3][2] = this.stateBlock[3][3];
		this.stateBlock[3][3] = temp;
		
	}
	
	//menu
	public void menu() {
		
		Scanner input = new Scanner(System.in);
		int uIn = -1;
		System.out.println("************");
		System.out.println("************");
		System.out.println("AES CIPHER");
		System.out.println("************");
		System.out.println("************");
		do {
			System.out.println("___________________________________________");
			System.out.println("Please select one of the following: ");
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
			/*NOTE:
			 * test values of the key and block state when encrypting and decrypting 
			 * by changing boolean to true for encrypt(boolean) and decrypt(boolean)
			 * */
			switch(uIn) {
			case 1:
				System.out.print("Enter some plaintext: ");
				String pText = input.nextLine();
				this.set_Plaintext(pText);
				
				break;
			case 2:
				System.out.println("Encrypting plaintext.\n...");
				this.encrypt(false);
				System.out.println("Plaintext encrypted.");
				break;
			case 3:
				System.out.println("Ciphertext: " + this.get_Ciphertext());
				break;
			case 4:
				System.out.println("Decrypting...");
				String msg = this.decrypt(false);
				System.out.println("Message: " + msg);
				break;
			case 5:
				System.out.println("Plaintext: " + this.get_Plaintext());
				break;
			case 6:
				this.set_Plaintext("");
				this.set_Ciphertext("");
				System.out.println("Cipher cleared.");
				break;
			case 7:
				System.out.println("Goodbye.");
				input.close();
				break;
			}
		}while(uIn != 7);
	}
	
	//getters and setters as needed
	public String get_Plaintext() {
		return this.plaintext;
	}
	public String get_Ciphertext() {
		return this.ciphertext;
	}
	
	public void set_Ciphertext(String ciphertext) {
		this.ciphertext = ciphertext;
	}
	
	public void set_Plaintext(String plaintext) {
		this.plaintext = plaintext;
	}
	public void set_Key(String key) {
		if(key.length() < 16) {
			return;
		}
		this.key = key;
	}
	
	//test block function
	private void test_Block() {
		for(int i = 0; i < this.stateBlock.length; i++) {
			System.out.println();
			for(int k = 0; k < this.stateBlock[i].length; k++) {
				String hexS = Integer.toHexString(this.stateBlock[i][k]);
				if(hexS.length() < 2) {
					String hexFull = "0";
					hexFull += hexS;
					hexS = hexFull;
				}
				hexS = hexS.toUpperCase();
				System.out.print( hexS + " ");
			}
		}
		System.out.println();
	}
	//test function to print the key state given the round
	private void test_Key(int round) {
		int[][] roundKey = this.keySpace.get(round);
		for(int i = 0; i < roundKey.length; i++) {
			System.out.println();
			for(int k = 0; k < roundKey[i].length; k++) {
				
				String hexS = Integer.toHexString(roundKey[i][k]);
				if(hexS.length() < 2) {
					
					String hexFull = "0";
					hexFull += hexS;
					hexS = hexFull;
				}
				hexS = hexS.toUpperCase();
				System.out.print(hexS + " ");
			}
		}
		System.out.println();
	}
}