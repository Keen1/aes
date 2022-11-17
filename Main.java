package aes;

public class Main {
	public static void main(String[] args) {
		int elementOne = 135;
		int elementTwo = 110;
		int elementThree = 70;
		int elementFour = 166;
		//times by two
		elementOne = (elementOne << 1)^283;
		
		int temp = elementTwo;
		elementTwo = ((elementTwo << 1)^temp)^283;
		int finalResult = (elementOne^elementTwo^elementThree^elementFour)^283;
		System.out.println(finalResult);
		
		
		int[][] matrixFactor = {
				{2, 3, 1, 1},
				{1, 2, 3, 1},
				{1, 1, 2, 3},
				{3, 1, 1, 2}};
		
		int[] row = matrixFactor[0];
		for(int i = 0; i < row.length; i++) {
			System.out.println(row[i]);
		}
		
		
		
		
	}

}
