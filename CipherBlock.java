package aes;
//cipherBlock instance
public class CipherBlock {
	
	//block matrix
	private int[][] block = new int[4][4];;
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
	
	
	//cipherBlock constructor, a block is always a 4x4 matrix of bytes
	CipherBlock(){
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
				
				this.block[i][k] = this.sBox[matrixRow][matrixCol];
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
