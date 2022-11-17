package aes;




public class CipherBlock {
	
	private int[][] block;
	
	//test
	
	CipherBlock(){
		 this.block = new int[4][4];
		 
	}
	
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
	
	private int mod_Prime_Poly(int value) {
		if(value >= 256) {
			return value^283;
		}
		
		return value;
	}
	
	
	
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
	
	private int get_Byte(int row, int column){
		return this.block[row][column];
	}
	private void set_Byte(int newByte, int row, int column) {
		this.block[row][column] = newByte;
	}
	
	
	
}
