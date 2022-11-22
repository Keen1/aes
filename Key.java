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
	private SubBoxes sBoxes = new SubBoxes();
	
	
	//constructors
	public Key() {
	}
	
	public Key(String keyPhrase) {
		this.set_KeyPhrase(keyPhrase);
		this.init_State();
		this.gen_Key_Space();
		
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
	private void gen_Key_Space() {
		for(int i = 1; i <= 10; i++) {
			this.generate_Key_Block(i);
		}
		
	}
	
	/*generate_KeyBlock() function
	 * generates the next keyblock according to the round
	 * and stores it in the hashmap
	 * */
	public void generate_Key_Block(int round) {
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
			currentWord[i] = this.sBoxes.get_S_Byte(iRow, iCol);
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
	
	public int[][] get_Key_State(int round) {
		return this.keyMap.get(Integer.valueOf(round));
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
			this.generate_Key_Block(i);
		}
	}
	
	
	
	
	
	
	
}
