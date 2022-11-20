package aes;
import java.util.HashMap;
public class Key {
	
	/*Variables
	 * *keyBlock matrix to store current state
	 * *keyPhrase to store original key input
	 * 		-might delete this later, should just be able to generate the key 
	 * 		 from the Cipher object and then once the state 0 is set the og key 
	 * 		 doesnt matter
	 * *HashMap object to store previous round keys? might not need tihs depending 
	 * on how the decrpyt algo works when i get there
	 * *sbox and isBox matrices for sub bytes in key expansion
	 * */
	private int[][] keyBlock = new int[4][4];;
	private String keyPhrase;
	private HashMap<Integer, int[][]> keyMap = new HashMap<>();
	int[] rc_Values = { 1, 2, 4, 8, 16, 32, 64, 128, 27, 54};
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
	
	//constructors
	public Key() {
	}
	
	public Key(String keyPhrase) {
		this.set_KeyPhrase(keyPhrase);
		this.init_State();
		
	}
	
	
	/*init_State() function
	 * initiates state 0 of the key space
	 * */
	public void init_State() {
		int count = 0;
		for(int i = 0; i < this.keyBlock.length; i++) {
			for(int k = 0; k < this.keyBlock.length; k++) {
				this.keyBlock[k][i] = this.get_KeyPhrase().charAt(count);
				count++;
			}
		}
		keyMap.put(0, keyBlock);
		
	}
	
	/*generate_KeyBlock() function
	 * generates the next keyblock according to the round
	 * and stores it in the hashmap
	 * */
	private void generate_KeyBlock(int round) {
		int[][] newBlock = new int[4][4];
		int[] currentWord = this.get_Word(round - 1, 3);
		
		currentWord = this.gen_G(currentWord, round);
		newBlock[0] = currentWord;
		
		for(int i = 1; i < newBlock.length; i++) {
			newBlock[i] = this.generate_Word(newBlock, i);
		}
		keyMap.put(round, newBlock);
		this.keyBlock = newBlock;
		
	}
	
	/*generate_Word() function
	 * generates the next word of the keyBlock
	 * */
	private int[] generate_Word(int[][] buildState, int blockRow) {
		int[] newWord = new int[4];
		
		int[] wordDiff1 = buildState[blockRow -1];
		int[] wordDiff4 = this.keyBlock[blockRow];
		for(int i = 0; i < 4; i++) {
			newWord[i] = wordDiff1[i]^wordDiff4[i];
		}
		return newWord;
	}
	
	/*gen_G() function
	 * generates the first word of each keyBlock,
	 * that is each word in the keyspace such that w % 4 = 0;
	 * */
	private int[] gen_G(int[] currentWord, int round) {
		StringBuilder sb = new StringBuilder("0");
		
		int temp = currentWord[0];
		currentWord[0] = currentWord[1];
		currentWord[1] = currentWord[2];
		currentWord[2] = currentWord[3];
		currentWord[3] = temp;
		
		String hexStr;
		
		for(int i = 0; i < currentWord.length; i++) {
			hexStr = Integer.toHexString(currentWord[i]);
			if(hexStr.length() < 2) {
				sb.append(hexStr);
				hexStr = sb.toString();
				sb = new StringBuilder("0");
			}
			String row = String.valueOf(hexStr.charAt(0));
			int iRow = Integer.parseInt(row, 16);
			String column = String.valueOf(hexStr.charAt(1));
			int iCol = Integer.parseInt(column, 16);
			currentWord[i] = this.sBox[iRow][iCol];
		}
		int[] rcWord = new int[4];
		rcWord[0] = this.rc_Values[round - 1];
		
		for(int i = 0; i < currentWord.length; i++) {
			currentWord[i] = currentWord[i]^rcWord[i];
		}
		return currentWord;
	}
		
		
		
		
	
	
	//getters and setters
	private void set_KeyPhrase(String phrase) {
		this.keyPhrase = phrase;
	}
	private String get_KeyPhrase() {
		return this.keyPhrase;
	}
	
	private int get_Byte(int row, int column) {
		return this.keyBlock[row][column];
	}
	private void set_Byte(int value, int row, int column) {
		this.keyBlock[row][column] = value;
	}
	private int[] get_Word(int round, int row) {
		return this.keyMap.get(Integer.valueOf(round))[row];
	}
	private void set_Word(int[] newWord, int row) {
		this.keyBlock[row] = newWord;
	}
	
	
	
	/*testing functions*/
	public void test_KeyBlock() {
		System.out.println();
		System.out.println("Current Key Block State: ");
		System.out.println("___________________________________________");
		
		for(int i = 0; i < this.keyBlock.length; i++) {
			System.out.println();
			for(int k = 0; k < this.keyBlock[i].length; k++) {
				String hexS = Integer.toHexString(this.keyBlock[i][k]);
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
	}
	//general purpose test function***delete later
	public void test_Funct() {
		for(int i = 1; i <= 10; i++) {
			this.generate_KeyBlock(i);
		}
	}
	
	
	
	
	
	
	
}
