public class ForestTracker {
	
	public char[][] trackForestTerrain(char[][] forest, int years) {
		return changeForest(forest, years);
	}
	
	
	public char[][] changeForest(char[][] forest, int years) {
		
		final int yrsChange = 10;
		
		if (years < yrsChange) return forest;

		int rows = forest.length;
		int cols = forest[0].length;
		char[][] result = fillWithZeros(forest, rows, cols);
		
		
		char[][] tempResult = new char[result.length][result[0].length];
		for (int i = 0; i < result.length; ++i) {
			for (int j = 0; j < result[0].length; ++j) {
				tempResult[i][j] = result[i][j];
			}
		}
		
		
		while (years >= yrsChange) {
			
			for (int i = 1; i <= rows; ++i) {
				for (int j = 1; j <= cols; ++j) {
					if (result[i][j] >= '1' && result[i][j] < '4') {
						result[i][j] += 1;
					}
					else if (result[i][j] == '4') {
						if (hasToChange(tempResult,  i, j)) {
							result[i][j] -= 1;
						}
					}
				}
			}
			
			years -= yrsChange;
		}
		
		
		return cleanFromZeros(result);
	}
	
	public char[][] fillWithZeros(char[][] forest, int rows, int cols) {
		
		char[][] result = new char[rows + 2][cols + 2];
		
		for (int i = 0; i <= cols + 1; ++i)
			result[0][i] = '0';
		
		for (int i = 1; i <= rows; ++i) {
			for (int j = 0; j <= cols + 1; ++j) {
				if (j == 0 || j == cols + 1) {
					result[i][j] = '0';
				}
				else {
					result[i][j] = forest[i - 1][j - 1];
				}
			}
		}
		
		for (int i = 0; i <= cols + 1; ++i) {
			result[rows + 1][i] = '0';
		}
		
		return result;
	}
	
	public char[][] cleanFromZeros(char[][] forest) {
		
		char[][] result = new char[forest.length - 2][forest[0].length - 2];
		
		for (int i = 0; i < forest.length - 2; ++i) {
			for (int j = 0; j < forest[0].length - 2; ++j) {
				result[i][j] = forest[i + 1][j + 1];
			}
		}
		
		return result;
	}
	
	public boolean hasToChange(char[][] forest, int row1, int col1) {
		
		int row = row1; 
		int col = col1;
		
		int counter = 0;
		
		for (int i = 0; i < 3; ++i) {
			if (forest[row - 1][col - 1 + i] == '4') {
				counter++;
			}
		}
		
		for (int i = 0; i < 3; ++i) {
			if (forest[row + 1][col - 1 + i] == '4') {
				counter++;
			}
		}
		
		
		
		if (forest[row][col - 1] == '4') {
			counter++;
		}
		
		if (forest[row][col + 1] == '4') {
			counter++;
		}
		
		if (counter >= 3) return true;
		return false;
	}
}