package aes;
//cipherBlock instance
public class CipherBlock {
	
	//block matrix
	private int[][] block = new int[4][4];
	
	private SubBoxes sBoxes = new SubBoxes();
	
	//cipherBlock constructor, a block is always a 4x4 matrix of bytes
	//a string passed 
	CipherBlock(String plainBytes){
		this.write_Block(plainBytes);
	}
	
	private void write_Block(String plainBytes) {
		
		
		int cnt = 0;
		for(int i = 0; i < this.block.length; i++) {
			for(int k = 0; k < this.block[i].length; k++) {
				if(cnt < plainBytes.length()) {
					this.block[k][i] = plainBytes.charAt(cnt);
					cnt++;
				}else {
					this.block[k][i] = 0;
				}
				
			}
		}
	}
	public String out_Block() {
		StringBuilder outStr = new StringBuilder();
		for(int i = 0; i < this.block.length; i++) {
			for(int k = 0; k < this.block[i].length; k++) {
				outStr.append((char)this.block[k][i]);
			}
		}
		return outStr.toString();
	}
	
	public boolean valid_Byte_Str(String plainBytes) {
		if(plainBytes.length() > 16) {
			return false;
		}else {
			return true;
		}
	}
	
	/*add_Round_Key() function
	 * XORs the roundkey with the current state
	 * */
	public void add_Round_Key(int[][] roundKey) {
		for(int i = 0; i < this.block.length; i++) {
			for(int k = 0; k < this.block[i].length; k++) {
				this.block[i][k] = roundKey[i][k]^this.block[i][k];
			}
		}
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
			for(int k = 0; k < this.block[i].length; k++) {
				int[] stateColumn = {this.block[0][k], this.block[1][k], this.block[2][k], this.block[3][k]};
				mixedBlock[i][k] = this.matrix_GF_Mult_Op(scalarRow, stateColumn);
			}
		}
		this.block = mixedBlock;
	}
	
	public void invmix_Columns() {
		int[][] inv_MatrixFactor = {
				{14,11,13,9},
				{9,14,11,13},
				{13,9,14,11},
				{11, 13, 9, 14}};
		
		int[][] mixedBlock = new int[4][4];
		int[] scalarRow;
		
		for(int i = 0; i < this.block.length; i++) {
			scalarRow = inv_MatrixFactor[i];
			for(int k = 0; k < this.block[i].length; k++) {
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
			if(scalarRow[i] == 14) {
				stateColumn[i] = this.mod_Prime_Poly((stateColumn[i] << 3)^(stateColumn[i] << 2)^(stateColumn[i] << 1));
			}
			if(scalarRow[i] == 13) {
				stateColumn[i] = this.mod_Prime_Poly((stateColumn[i] << 3)^((stateColumn[i] << 2)^stateColumn[i]));
			}
			if(scalarRow[i] == 11) {
				stateColumn[i] = this.mod_Prime_Poly((stateColumn[i] << 3)^((stateColumn[i] << 1)^stateColumn[i]));
			}
			if(scalarRow[i] == 9) {
				stateColumn[i] = this.mod_Prime_Poly((stateColumn[i] << 3)^stateColumn[i]);
			}
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
	
	public void invshift_Rows() {
		//leave first row alone
		
		/*
		 * second row inverse shift
		 * one-byte right circular shift
		 * */
		//get last byte of second row
		int temp = this.get_Byte(1, 3);
		//move 3rd element to 4th position
		this.set_Byte(this.get_Byte(1, 2), 1, 3);
		//move 2nd element to 3rd position
		this.set_Byte(this.get_Byte(1, 1), 1, 2);
		//move 1st element to 2nd position
		this.set_Byte(this.get_Byte(1, 0), 1, 1);
		//move 4th element to 1st position
		this.set_Byte(temp, 1, 0);
		
		/*
		 * third row shift
		 * two-byte right circular shift
		 * is functionally equivalent is two-byte left shift on 4 bytes
		 * */
		//get temp copies of two right vars
		temp = this.get_Byte(2, 2);
		int temp2 = this.get_Byte(2, 3);
		//shift first two elements right 2
		this.set_Byte(this.get_Byte(2, 1), 2, 3);
		this.set_Byte(this.get_Byte(2, 0), 2, 2);
		//set two temp copies according to 2 byte circ shift
		this.set_Byte(temp, 2, 0);
		this.set_Byte(temp2, 2, 1);
		
		/*
		 * fourth row shift
		 * three-byte right circular shift OR one-byte left circular shift
		 * */
		//get first element
		temp = this.get_Byte(3,0);
		//shift 2nd element to 1st position
		this.set_Byte(this.get_Byte(3, 1), 3, 0);
		//shift 3rd element to 2nd position
		this.set_Byte(this.get_Byte(3, 2), 3, 1);
		//shift 4th element to 3rd position
		this.set_Byte(this.get_Byte(3, 3), 3, 2);
		//set 1st element to 4th positioin
		this.set_Byte(temp, 3, 3);
		
	
	}
	
	public void sub_Bytes() {
		StringBuilder sb = new StringBuilder("0");
		
		for(int i = 0; i < this.block.length; i++) {
			for(int k = 0; k < this.block[i].length; k++) {
				
				String hexStr = Integer.toHexString(this.block[i][k]);
				
				if(hexStr.length() < 2) {
					sb.append(hexStr);
					hexStr = sb.toString();
					sb = new StringBuilder("0");
				}
				String row = String.valueOf(hexStr.charAt(0));
				int matrixRow = Integer.parseInt(row, 16);
				
				String column = String.valueOf(hexStr.charAt(1));
				int matrixCol = Integer.parseInt(column, 16);
				
				this.block[i][k] = this.sBoxes.get_S_Byte(matrixRow, matrixCol);
			}
		}
	}
	public void invsub_Bytes() {
		StringBuilder sb = new StringBuilder("0");
		
		for(int i = 0; i < this.block.length; i++) {
			for(int k = 0; k < this.block[i].length; k++) {
				String hexStr = Integer.toHexString(this.block[i][k]);
				
				if(hexStr.length() < 2 ) {
					sb.append(hexStr);
					hexStr = sb.toString();
					sb = new StringBuilder("0");
				}
				String row = String.valueOf(hexStr.charAt(0));
				int matrixRow = Integer.parseInt(row, 16);
				
				String column = String.valueOf(hexStr.charAt(1));
				int matrixCol = Integer.parseInt(column, 16);
				
				this.block[i][k] = this.sBoxes.get_IS_Byte(matrixRow, matrixCol);
				
			}
		}
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
	 * methods intended for testing and verifying results
	 * */
	
	public void test_Func() {
		
	}
	
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
		System.out.println("\n___________________________________________");
		System.out.println("\nCurrent Block state: ");
		
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
		System.out.println();
	}
}
