package aes;
//cipherBlock instance
public class CipherBlock {
	
	//block matrix
	private int[][] block = new int[4][4];;
	
	
	//cipherBlock constructor, a block is always a 4x4 matrix of bytes
	CipherBlock(){
		 
		 
	}
	
	/*
	 * mix_Columns() function 
	 * - diffuses the plaintext into the ciphertext
	 * 
	 * diffuses each new ciphertext element of the round by matrix multiplication in GF(2^8) where the operation takes the form:
	 * [u1(v1) XOR u2(v2) XOR u3(v3) XOR u4(v4)] over GF(2^8) m(x) as given for each entry in the matrix product. 
	 * 
	 * */
	
	public void mix_Columns() {
		int[][] matrixFactor = {
				{2, 3, 1, 1},
				{1, 2, 3, 1},
				{1, 1, 2, 3},
				{3, 1, 1, 2}};
		
		int[][] mixedBlock = new int[4][4];
		int[] scalarRow;
		
		
		
		for(int i = 0; i < this.block.length; i++) {
			scalarRow = matrixFactor[i];
			for(int k = 0; k < this.block.length; k++) {
				int[] stateColumn = {this.block[0][k], this.block[1][k], this.block[2][k], this.block[3][k]};
				mixedBlock[i][k] = this.matrix_GF_Mult_Op(scalarRow, stateColumn);
			}
		}
		this.block = mixedBlock;
		
	}
	
	/*
	 * Helper methods to compute the diffused matrix product of the state matrix and the matrix factor 
	 * operations are computed over a Galois Field of 2^8 where m(x) = prime polynomial x^8 + x^4 + x^3 + x + 1 or 100011011 base 2 
	 * or 283 base 10. 
	 * 
	 * takes a row of the matrix factor and XORs the products of the scalars and byte elements of the appropriate column
	 * to compute the entry in the product matrix. */
	
	private int matrix_GF_Mult_Op(int[] scalarRow, int[] stateColumn) {
		for(int i = 0; i < scalarRow.length; i++) {
			if(scalarRow[i] == 3) {
				int temp = stateColumn[i];
				stateColumn[i] = this.mod_Prime_Poly(stateColumn[i] << 1);
				stateColumn[i] = this.mod_Prime_Poly(stateColumn[i]^temp);
			}
			if(scalarRow[i] == 2) {
				stateColumn[i] = this.mod_Prime_Poly(stateColumn[i] << 1);
			}
		}
		return this.mod_Prime_Poly((stateColumn[0]^stateColumn[1]^stateColumn[2]^stateColumn[3]));
	}
	//every operation is computed over a GF(2^8) where m(x) = 283
	private int mod_Prime_Poly(int value) {
		if(value >= 256) {
			return value^283;
		}
		
		return value;
	}
	
	
	/*
	 * shift_Rows() function
	 * shift the appropriate rows by the appropriate circular shift per row in the state matrix
	 * */
	public void shift_Rows() {
		//leave first row alone
		
		/*
		 * second row shift
		 * one-byte left circular shift
		 * */
		//get first byte block in second row
		int temp = this.get_Byte(1, 0);
		//move second block to first block position.
		this.set_Byte(this.get_Byte(1, 1), 1, 0);
		//move third block to second block position
		this.set_Byte(this.get_Byte(1, 2), 1, 1);
		//move fourth block to third block position
		this.set_Byte(this.get_Byte(1,3), 1, 2);
		//move first block to fourth block position
		this.set_Byte(temp, 1, 3);
		
		/*
		 * third row shift
		 * two-byte left circular shift
		 * */
		//get temp copies of the first two elements in the byte row
		temp = this.get_Byte(2, 0);
		int temp2 = this.get_Byte(2, 1);
		//left shift the 3rd and 4th elements by two
		this.set_Byte(this.get_Byte(2, 2), 2, 0);
		this.set_Byte(this.get_Byte(2,3), 2, 1);
		//place the temp copies of the 1st and 2nd elements in the 3rd and 4th positions
		this.set_Byte(temp, 2, 2);
		this.set_Byte(temp2, 2, 3);
		
		/*
		 * fourth row shift
		 * three-byte left circular shift OR a one-byte right circular shift
		 * */
		
		//get temp copy of 4th element
		temp = this.get_Byte(3, 3);
		//move 3rd element to 4th position
		this.set_Byte(this.get_Byte(3,2), 3, 3);
		//move 2nd element to 3rd position
		this.set_Byte(this.get_Byte(3, 1), 3, 2);
		//move 1st element to 2nd position
		this.set_Byte(this.get_Byte(3, 0), 3, 1);
		//place copy of 4th element in 1st position
		this.set_Byte(temp, 3, 0);
		
	}
	
	
	//getter and setter for state matrix byte entries
	private int get_Byte(int row, int column){
		return this.block[row][column];
	}
	private void set_Byte(int newByte, int row, int column) {
		this.block[row][column] = newByte;
	}
	
	/*
	 * test() methods
	 * methods intended for testing and verifying results*/
	
	public void test_input(String inputPhrase) {
		int count = 0;
		for(int i = 0; i < this.block.length; i++) {
			for(int k = 0; k < this.block[i].length; k++) {
				this.block[k][i] = inputPhrase.charAt(count);
				count++;
			}
			
		}
		
	}
	
	public void test_Block() {
		for(int i = 0; i < this.block.length; i++) {
			System.out.println();
			for(int k = 0; k < this.block[i].length; k++) {
				String hexS = Integer.toHexString(this.block[i][k]);
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
}
