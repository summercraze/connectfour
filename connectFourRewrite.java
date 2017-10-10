import java.util.Scanner;

public class connectFourRewrite {
	public static void main(String[] args)
	{
		//print the board and set up score board(which simulate the connect four array)
		String [][] gameBoard = createBoard();
		int [][] scoreBoard = new int[6][7];
		printMatrix(gameBoard);
		//fill up scoreBoard matrix
		scoreBoard = fillBoard(scoreBoard);
		String [][] newGameBoard;
		int columnNumber = 10;
		boolean score = true;
		//counter for the turn
		int counter = 0;

		//
		while(score == true)
		{
			if (counter % 2 == 0)
			{

				columnNumber = askUserQuestion(1);
				newGameBoard = updateBoard(columnNumber,gameBoard,"R");
				printMatrix(newGameBoard);
				scoreBoard  = updateScoreBoard(scoreBoard,columnNumber,"R");
				printScoreMatrix(scoreBoard);
				score = checkWin(scoreBoard,columnNumber);
			}
			else
			{
				columnNumber = askUserQuestion(0);	
				newGameBoard = updateBoard(columnNumber,gameBoard,"Y");
				printMatrix(newGameBoard);
				scoreBoard  = updateScoreBoard(scoreBoard,columnNumber,"Y");
				printScoreMatrix(scoreBoard);
				score = checkWin(scoreBoard,columnNumber);
			}

			counter+=1;

		}
	}
	//print out the matrix
	public static void printScoreMatrix(int[][] m)
	{
		for(int row = 0;row < m.length;row++)
		{
			for(int coloumn = 0;coloumn < m[row].length;coloumn++)
			{
				System.out.print(m[row][coloumn] + " ");
			}
			System.out.println();
		}
	}


	//create the board of the game
	public static String [][] createBoard()
	{
		//array that is the 6*7 board with extra space in between and last row as the border
		String [][] board = new String[7][15];
		for (int row = 0;row < board.length;row++ )
		{
			for (int coloumn = 0;coloumn < board[row].length;coloumn++)
			{
				//fill the matrix with odd | and even " "
				if (coloumn % 2 != 0)
				{
					board[row][coloumn] = " ";
				}
				else
				{
					board[row][coloumn] = "|";
				}
				//last row "-"
				if (row == board.length - 1)
				{
					board[row][coloumn] = "-";
				}
			}
		}
		return board;
	}

	//print out the matrix
	public static void printMatrix(String [][] m)
	{
		for(int row = 0;row < m.length;row++)
		{
			for(int coloumn = 0;coloumn < m[row].length;coloumn++)
			{
				System.out.print(m[row][coloumn]);
			}
			System.out.println();
		}
	}

	//prompt user 1 or 2 depend on the key,will return the column number 
	public static int askUserQuestion(int key)
	{
		int columnNumber = 100;
		String diskColor;
		boolean needToAsk = true;
		while(needToAsk == true)
		{
			if (key == 1)
			{
				diskColor = "red";
				Scanner input = new Scanner(System.in);
				System.out.println("User 1: " + " Drop a " +  diskColor + " disk at column (0-6):");
				columnNumber = input.nextInt();
				if (columnNumber >= 7)
				{
					System.out.println("Error please enter a number between 0 to 6.");
					needToAsk = true;
				}
				else
				{
					needToAsk = false;
				}

			}
			else
			{
				diskColor = "yellow";
				Scanner input = new Scanner(System.in);
				System.out.print("User 2:" + " Drop a " +  diskColor + " disk at column (0-6):");
				columnNumber = input.nextInt();
				if (columnNumber >= 7)
				{
					System.out.println("Error please enter a number between 0 to 6.");
					needToAsk = true;
				}
				else
				{
					needToAsk = false;
				}
			}

		}
		return columnNumber;
	}

	//update board game when user enter the column number
	public static String [][] updateBoard(int columnNumber,String [][] gameBoard,String color)
	{
		//figure out the actual column which is twice + 1 of the number
		int actualColumnNumber = (columnNumber * 2) + 1;
		//fill the bottom of the column
		for(int row = gameBoard.length - 1;row > 0;row--)
		{
			if(gameBoard[row ][actualColumnNumber] == " ")
			{
				gameBoard[row ][actualColumnNumber] = color;
				break;
			}
		}
		return gameBoard;
	}
	//fill up score board with 2 which would be changed to 0 or 1 depend on the player
	public static int[][] fillBoard(int[][] scoreBoard)
	{
		for (int row = 0;row < scoreBoard.length;row++ )
		{
			for (int coloumn = 0;coloumn < scoreBoard[row].length;coloumn++)
			{
				scoreBoard[row][coloumn] = 2;
			}
		}
		return scoreBoard;
	}

	//update score board with whatever the user has enter:0 is yellow 1 is red
	public static int[][] updateScoreBoard(int[][] scoreBoard,int columnNumber,String color)
	{
		//fill the bottom of the column or stack up on top of someone else
		for(int row = scoreBoard.length - 1;row > 0;row--)
		{
			if(scoreBoard[row][columnNumber] == 2)
			{
				if(color == "R")
				{
					scoreBoard[row][columnNumber] = 1;
					break;
				}
				else
				{
					scoreBoard[row][columnNumber] = 0;
					break;
				}
			}
		}
		return scoreBoard;
	}
	/*
	 * the plan:
	 * extract a row, a column and a few matrix to check the side
	 */
	public static boolean checkWin(int[][] scoreBoard,int columnNumber)
	{
		//locate the actual position of the pawn
		int actualRowNumber = 0;
		int [][] matrixNumber = new int[7][15];

		for(int row = 0;row < scoreBoard.length;row++)
		{
			if(scoreBoard[row][columnNumber] == 1 || scoreBoard[row][columnNumber] == 0)
			{

				actualRowNumber = row;
				break;
			}
		}

		//get the row of this pawn 
		int[] currentRow = extractRow(scoreBoard,actualRowNumber);
		int rowScore = checkRow(currentRow);

		//get the column of this pawn
		int[] currentColumn= extractColumn(scoreBoard,columnNumber);
		int columnScore = checkColumn(currentColumn);

		//get the matrix of the pawn
		int [] majorDiagonalArray = extractMajorDiagonalMatrix(scoreBoard,columnNumber,actualRowNumber);
		int majorDiagonalScore = checkDiagonal(majorDiagonalArray);

		int [] minorDiagonalArray = extractMinorDiagonalMatrix(scoreBoard,columnNumber,actualRowNumber);
		int minorDiagonalScore = checkDiagonal(minorDiagonalArray);

		int totalScore = rowScore + columnScore + majorDiagonalScore + minorDiagonalScore ;
		System.out.println("The current score is:" + totalScore);

		if(totalScore == 9)
		{
			if(scoreBoard[actualRowNumber][columnNumber] == 1)
			{
				System.out.println("The red player won");

			}
			else
			{
				System.out.println("The yellow player won");

			}
			return false;
		}
		return true;
	}
	//divide the rows into sections and check the section
	public static int checkRow(int[] row)
	{
		int[] rowToCheck = new int[4];
		int win = 2;
		for (int i = 0; i < 4 ; i++)
		{
			int counter = 0;
			//			System.out.println("\nThis is the row array" + i + ":");
			for (int j = i; j < i + 4; j++)
			{
				rowToCheck[counter] = row[j];
				//				System.out.print(rowToCheck[counter]);
				counter+=1;

			}

			//				System.out.println("We are going in to check same");
			if(checkSame(rowToCheck))
			{
				win += 1;
			}
			else
			{
				win += 0;
			}


			//			System.out.println("");
			//			System.out.println("Win number in row:" + win);

		}
		return win;
	}

	//divide the column into sections and check the section
	public static int checkColumn(int[] column)
	{
		int[] columnToCheck = new int[4];
		int win = 2;
		for (int i = 0; i < 3 ; i++)
		{
			int counter = 0;
			//			System.out.println("\nThis is the column array" + i + ":");
			for (int j = i; j < i + 4; j++)
			{
				columnToCheck[counter] = column[j];
				//				System.out.print(columnToCheck[counter]);
				counter+=1;

			}

			//				System.out.println("We are going in to check same");
			if(checkSame(columnToCheck))
			{
				win += 1;
			}
			else
			{
				win += 0;
			}

			//			System.out.println("");
			//			System.out.println("Win number in column:" + win);


		}
		return win;
	}

	//check diagonal divide the rows into sections and check the section
	public static int checkDiagonal(int[] diagonal)
	{

		int[] diagonalToCheck = new int[4];
		int set = diagonal.length - 4 + 1;
		//			System.out.println("There is" + set + " of set of 4 we have");
		int win = 2;
		if(set < 1)
		{
			//				System.out.println("There is no array");
			win += 0;
		}
		else
		{
			for (int i = 0; i < set ; i++)
			{
				int counter = 0;
				//				System.out.println("\nThis is the diagonal array" + i + ":");
				for (int j = i; j < i + 4; j++)
				{
					diagonalToCheck[counter] = diagonal[j];
					//					System.out.print(diagonalToCheck[counter]);
					counter+=1;

				}

				//					System.out.println("We are going in to check same");
				if(checkSame(diagonalToCheck))
				{
					win += 1;
				}
				else
				{
					win += 0;
				}

				//				System.out.println("");
				//				System.out.println("Win number in column:" + win);


			}
		}
		return win;
	}

	// helper method to extract the row of the pawn
	public static int[] extractRow(int[][] scoreBoard,int row)
	{
		int rowLength = scoreBoard[0].length;
		int [] rowArray = new int[rowLength]; 
		//		System.out.println("This is the print out of the extraction of the rows:");
		for(int column = 0;column < rowLength;column++)
		{
			rowArray[column] = scoreBoard[row][column];
			//			System.out.print(rowArray[column]);
		}
		return rowArray;
	}
	// helper method to extract the column of the pawn
	public static int[] extractColumn(int[][] scoreBoard,int column)
	{
		int columnLength = scoreBoard.length;
		int [] columnArray = new int[columnLength]; 
		//		System.out.println("This is the print out of the extraction of the columns:");
		for(int row = 0;row < columnLength;row++)
		{
			columnArray[row] = scoreBoard[row][column];
			//			System.out.print(columnArray[row]);
		}
		//		System.out.println("");
		return columnArray;
	}
	// helper method to extract the matrix of the pawn
	public static int[] extractMajorDiagonalMatrix(int[][] scoreBoard,int column,int row)
	{
		//			System.out.println("ERROR CHECKING");
		int rowLength = scoreBoard.length;
		int columnLength = scoreBoard[0].length;
		//			System.out.println("This is total number of column" + columnLength);
		//			System.out.println("This is  total number of row" + rowLength);
		//			System.out.println("This is row for pawn:" + row + "column for pawn:" + column);

		int actualRow = rowLength - 1;
		int actualColumn = columnLength - 1;
		int rowBelow = actualRow - row ;
		//			System.out.println("This is number of  the row below" + rowBelow);
		int columnToTheLeft = column;
		//			System.out.println("This is number of column to the left" + columnToTheLeft);
		int rowAbove = row;
		//			System.out.println("This is number of row above" + rowAbove);
		int columnToTheRight = actualColumn -column  ;
		//			System.out.println("This is number of column to the right" + columnToTheRight);

		int sizeToLeft = 100;
		int sizeToRight = 100;

		if(rowBelow < columnToTheLeft )
		{
			sizeToLeft = rowBelow;
		}
		else
		{
			sizeToLeft = columnToTheLeft;
		}
		//			System.out.println("This is number of row below and column to the left" + sizeToLeft);
		if(rowAbove < columnToTheRight )
		{
			sizeToRight = rowAbove;
		}
		else
		{
			sizeToRight = columnToTheRight;
		}
		//			System.out.println("This is number of row above and column to the right" + sizeToRight);

		int rowDown = row + sizeToLeft;
		int columnToLeft = column - sizeToLeft;
		//			System.out.println("This is the last row of the matrix" + rowDown);
		//			System.out.println("This is the left most column of the matrix" + columnToLeft);

		int rowUp = row - sizeToRight;
		int columnToRight = column + sizeToRight;
		//			System.out.println("This is the first row of the matrix" + rowUp);
		//			System.out.println("This is the right most column of the matrix" + columnToRight);

		int [][] majorDiagonalMatrix = new int[rowDown - rowUp + 1][columnToRight - columnToLeft + 1];

		for(int i = rowUp;i <= rowDown;i++)
		{
			for(int j = columnToLeft;j <= columnToRight;j++)
			{
				int currentRow = i - rowUp;
				int currentColumn = j - columnToLeft;
				majorDiagonalMatrix[currentRow][currentColumn] = scoreBoard[i][j];
				//					System.out.println("This is the majorDiagonalMatrix[" + currentRow + "] [" + currentColumn + "] = " 
				//					+ majorDiagonalMatrix[currentRow][currentColumn]);
				//					System.out.println("This is the scoreBoard[" + i + "] [" + j + "] = " 
				//							+ scoreBoard[i][j] );

			}
		}

		//			System.out.println("This is the length of the majorDiagonal" + majorDiagonalMatrix.length);
		int [] majorDiagonalArray = new int[majorDiagonalMatrix.length]; 
		for(int matrixRow = 0 ; matrixRow < majorDiagonalMatrix.length ; matrixRow++)
		{
			for(int matrixColumn = 0 ; matrixColumn < majorDiagonalMatrix[matrixRow].length ; matrixColumn ++)
			{
				int magicNumber =  majorDiagonalMatrix.length - 1 - matrixRow;

				if (matrixColumn == magicNumber)
				{
					majorDiagonalArray[matrixRow] = majorDiagonalMatrix[matrixRow][matrixColumn];
					//						System.out.println("This is the majorDiagonalArray[" + matrixRow + "] = " 
					//								+ majorDiagonalMatrix[matrixRow][matrixColumn]);
				}
			}
		}
		return majorDiagonalArray ;
	}
	// helper method to extract the matrix of the pawn
	public static int[] extractMinorDiagonalMatrix(int[][] scoreBoard,int column,int row)
	{
		System.out.println("ERROR CHECKING FOR INVERSE MATRIX");
		int rowLength = scoreBoard.length;
		int columnLength = scoreBoard[0].length;
		//			System.out.println("This is total number of column" + columnLength);
		//			System.out.println("This is  total number of row" + rowLength);
		//			System.out.println("This is row for pawn:" + row + "column for pawn:" + column);

		int actualRow = rowLength - 1;
		int actualColumn = columnLength - 1;

		int rowBelow = actualRow - row ;
		System.out.println("This is number of  the row below" + rowBelow);
		int columnToTheLeft = column;
		System.out.println("This is number of column to the left" + columnToTheLeft);
		int rowAbove = row;
		System.out.println("This is number of row above" + rowAbove);
		int columnToTheRight = actualColumn -column  ;
		System.out.println("This is number of column to the right" + columnToTheRight);

		int sizeToLeft = 100;
		int sizeToRight = 100;

		if(rowBelow < columnToTheRight )
		{
			sizeToLeft = rowBelow;
		}
		else
		{
			sizeToLeft = columnToTheRight;
		}
		System.out.println("This is number of row below and column to the right" + sizeToLeft);
		if(rowAbove < columnToTheLeft  )
		{
			sizeToRight = rowAbove;
		}
		else
		{
			sizeToRight = columnToTheLeft;
		}
		System.out.println("This is number of row above and column to the left" + sizeToRight);

		int rowDown = row + sizeToLeft;
		int columnToRight = column + sizeToLeft;		
		System.out.println("This is the last row of the matrix" + rowDown);
		System.out.println("This is the right most column of the matrix" + columnToRight);

		int rowUp = row - sizeToRight;
		int columnToLeft = column - sizeToRight;
		System.out.println("This is the first row of the matrix" + rowUp);
		System.out.println("This is the left most column of the matrix" + columnToLeft);

		int [][] minorDiagonalMatrix = new int[rowDown - rowUp + 1][columnToRight - columnToLeft + 1];

		for(int i = rowUp;i <= rowDown;i++)
		{
			for(int j = columnToLeft;j <= columnToRight;j++)
			{
				int currentRow = i - rowUp;
				int currentColumn = j - columnToLeft;
				minorDiagonalMatrix[currentRow][currentColumn] = scoreBoard[i][j];
				System.out.println("This is the minorDiagonalMatrix[" + currentRow + "] [" + currentColumn + "] = " 
						+ minorDiagonalMatrix[currentRow][currentColumn]);
				System.out.println("This is the scoreBoard[" + i + "] [" + j + "] = " 
						+ scoreBoard[i][j] );

			}
		}

		System.out.println("This is the length of the minorDiagonal" + minorDiagonalMatrix.length);
		int [] minorDiagonalArray = new int[minorDiagonalMatrix.length]; 
		for(int matrixRow = 0 ; matrixRow < minorDiagonalMatrix.length ; matrixRow++)
		{
			for(int matrixColumn = 0 ; matrixColumn < minorDiagonalMatrix[matrixRow].length ; matrixColumn ++)
			{


				if (matrixColumn == matrixRow)
				{
					minorDiagonalArray[matrixRow] = minorDiagonalMatrix[matrixRow][matrixColumn];
					System.out.println("This is the minorDiagonalArray[" + matrixRow + "] = " 
							+ minorDiagonalMatrix[matrixRow][matrixColumn]);
				}
			}
		}
		return minorDiagonalArray ;
	}
	//takes an array and check if the array has the same number
	public static boolean checkSame(int[] a)
	{
		int keyNumber = a[0];

		//		System.out.println("This is the key number in checkSame:" + keyNumber);
		//		System.out.println("This is the rest of the array" );
		if(keyNumber == 2)
		{
			return false;
		}
		else
		{
			for (int i = 1;i < a.length;i++)
			{
				//			System.out.print(a[i] );
				if (a[i] != keyNumber ||  a[i] == 2)
				{
					return false;
				}
			}
		}
		return true;

	}


}
