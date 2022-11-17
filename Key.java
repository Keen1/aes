package aes;

public class Key {
	private int[][] keyBlock;
	private String keyPhrase;
	
	public Key() {
		this.keyBlock = new int[4][4];
		
	}
	
	public void set_KeyPhrase(String phrase) {
		this.keyPhrase = phrase;
	}
	public String get_KeyPhrase() {
		return this.keyPhrase;
	}
	
	public int get_Byte(int row, int column) {
		return this.keyBlock[row][column];
	}
	public void set_Byte(int value, int row, int column) {
		this.keyBlock[row][column] = value;
	}
	
	
}
