package aes;
import java.util.ArrayList;
public class Key {
	private int[][] keyBlock = new int[4][4];;
	private String keyPhrase;
	private ArrayList<int[]> keyStore;
	int[] rc_Values = { 1, 2, 4, 8, 16, 32, 64, 128, 27, 54};
	
	
	public Key() {
	}
	public Key(String keyPhrase) {
		this.set_KeyPhrase(keyPhrase);
		this.generate_KeyBlock();
		
	}
	
	public void generate_KeyBlock() {
		int count = 0;
		for(int i = 0; i < this.keyBlock.length; i++) {
			for(int k = 0; k < this.keyBlock.length; k++) {
				this.keyBlock[k][i] = this.get_KeyPhrase().charAt(count);
				count++;
			}
		}
	}
	
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
	
	
	
	
}
