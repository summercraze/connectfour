import java.util.Scanner;
/*
 * This is a code that mimic the game connect four
 * It prompts two users to drop colored disks into sever-column,six-row vertically suspended grid
 * Whoever connect four same colored disks in a row,a column,or diagonal wins the game
 * Also check the major diagonal and sub-diagonal
 */

public class connectFourRewrite {
	//this is the main code, it set up the game board and also a score board a
	//ask user question,and check whether the player wins
	public static void main(String[] args)
	{
		//this the different variables needed for the code
		int columnNumber = 10;
		boolean score = true;
		int counter = 0;
		
		//print the board and set up score board(which simulate the connect four array)
		String [][] gameBoard = createBoard();
		int [][] scoreBoard = new int[6][7];
		printMatrix(gameBoard);
		
		//fill up scoreBoard matrix
		scoreBoard = fillBoard(scoreBoard);
		String [][] newGameBoard;		

		//this is part where user will be asked to put down a disk,
		//the score board would be updated and checked to see who is winning.
		while(score == true)
		{
			if (counter % 2 == 0)
			{
				columnNumber = askUserQuestion(1);
				newGameBoard = updateBoard(columnNumber,gameBoard,"R");
				printMatrix(newGameBoard);
				scoreBoard  = updateScoreBoard(scoreBoard,columnNumber,"R");
				score = checkWin(scoreBoard,columnNumber);
			}
			else
			{
				columnNumber = askUserQuestion(0);	
				newGameBoard = updateBoard(columnNumber,gameBoard,"Y");
				printMatrix(newGameBoard);
				scoreBoard  = updateScoreBoard(scoreBoard,columnNumber,"Y");
				score = checkWin(scoreBoard,columnNumber);
			}
			counter+=1;
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
		//some varibales
		int columnNumber = 100;
		String diskColor;
		boolean needToAsk = true;
		
		//this is while loop which would continually ask the user for a number between 0 and 6
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

	//this is checking columns,rows and diagonal to see if one of the user is winning,
	//every check would return a score,it will be compile and will return a boolean to stop the game
	public static boolean checkWin(int[][] scoreBoard,int columnNumber)
	{
		//one variable
		int actualRowNumber = 0;

		//this locate the actual disk location
		for(int row = 0;row < scoreBoard.length;row++)
		{
			if(scoreBoard[row][columnNumber] == 1 || scoreBoard[row][columnNumber] == 0)
			{

				actualRowNumber = row;
				break;
			}
		}

		//get the row of this disk and check it(<>)
		int[] currentRow = extractRow(scoreBoard,actualRowNumber);
		int rowScore = checkRow(currentRow);

		//get the column of this disk and check it (|)
		int[] currentColumn= extractColumn(scoreBoard,columnNumber);
		int columnScore = checkColumn(currentColumn);

		//get the major diagonal and check it(/)
		int [] majorDiagonalArray = extractMajorDiagonalMatrix(scoreBoard,columnNumber,actualRowNumber);
		int majorDiagonalScore = checkDiagonal(majorDiagonalArray);

		//get the minor diagonal and check it(\)
		int [] minorDiagonalArray = extractMinorDiagonalMatrix(scoreBoard,columnNumber,actualRowNumber);
		int minorDiagonalScore = checkDiagonal(minorDiagonalArray);

		//every check would return a score,if one has connected four it would return 3 point else it will return 2
		int totalScore = rowScore + columnScore + majorDiagonalScore + minorDiagonalScore ;

		//print out who is the winner and return false to stop the game
		if(totalScore == 9)
		{
			if(scoreBoard[actualRowNumber][columnNumber] == 1)
			{
				System.out.println("The red player won! Game over!");

			}
			else
			{
				System.out.println("The yellow player won! Game over!");

			}
			return false;
		}
		return true;
	}
	
//this section starts a series of fuction to seperate the extract things to check same
	
	//divide the rows into sections and check the section
	public static int checkRow(int[] row)
	{
		//this is the connect four array and some variable
		int[] rowToCheck = new int[4];
		int win = 2;
		
		//will add 4 to the first four number,take the the array and check it will be the same to 
		//prevent the number in the middle of connect four
		for (int i = 0; i < 4 ; i++)
		{
			int counter = 0;
			
			for (int j = i; j < i + 4; j++)
			{
				rowToCheck[counter] = row[j];
				counter+=1;
			}
			
			if(checkSame(rowToCheck))
			{
				win += 1;
			}
			else
			{
				win += 0;
			}
		}
		
		return win;
	}

	//divide the column into sections and check the section
	public static int checkColumn(int[] column)
	{
		//this is the connect four array and some variable
		int[] columnToCheck = new int[4];
		int win = 2;
		
		//will add 4 to the first 3 number,take the the array and check it will be the same to 
		//prevent the number in the middle of connect four
		for (int i = 0; i < 3 ; i++)
		{
			int counter = 0;
			
			for (int j = i; j < i + 4; j++)
			{
				columnToCheck[counter] = column[j];
				counter+=1;
			}
			
			if(checkSame(columnToCheck))
			{
				win += 1;
			}
			else
			{
				win += 0;
			}
		}
		
		return win;
	}

	//check diagonal divide the rows into sections and check the section
	//the sections depend on the length of the array input into this function
	public static int checkDiagonal(int[] diagonal)
	{
		// this is array with some variable
		int[] diagonalToCheck = new int[4];
		int win = 2;
		
		//this is the calculation of how many set of 4 do we need to check
		int set = diagonal.length - 4 + 1;
		
		//if there is less than 1 set no need to do check else will do check
		if(set < 1)
		{
			win += 0;
		}
		else
		{
			for (int i = 0; i < set ; i++)
			{
				int counter = 0;
				
				for (int j = i; j < i + 4; j++)
				{
					diagonalToCheck[counter] = diagonal[j];
					counter+=1;
				}

				if(checkSame(diagonalToCheck))
				{
					win += 1;
				}
				else
				{
					win += 0;
				}
			}
		}
		
		return win;
	}

//this section starts a series of helper code to extract rows,column,diagonal
	
	// helper method to extract the row of the disk
	public static int[] extractRow(int[][] scoreBoard,int row)
	{
		//first figure out the length and create a 1-d array to hold the row extracted
		int rowLength = scoreBoard[0].length;
		int [] rowArray = new int[rowLength]; 
		
		//extract the row
		for(int column = 0;column < rowLength;column++)
		{
			rowArray[column] = scoreBoard[row][column];
		}
		
		return rowArray;
	}
	
	// helper method to extract the column of the pawn
	public static int[] extractColumn(int[][] scoreBoard,int column)
	{
		//first figure out the length and create a 1-d array to hold the column extracted
		int columnLength = scoreBoard.length;
		int [] columnArray = new int[columnLength]; 
		
		//extract the column
		for(int row = 0;row < columnLength;row++)
		{
			columnArray[row] = scoreBoard[row][column];
		}

		return columnArray;
	}
	
	// helper method to extract the diagonal
	//here is the idea:a matrix will be extract,the matrix is determined by a smaller number of either the row or column
	//then extract the diagonal
	public static int[] extractMajorDiagonalMatrix(int[][] scoreBoard,int column,int row)
	{
		//some variables used
		int sizeToLeft = 100;
		int sizeToRight = 100;

		//this is fixed variable of the length of column and row for some calculation later
		int rowLength = scoreBoard.length;
		int columnLength = scoreBoard[0].length;
	
		//actual row and column length (since matrix start at 0) used for calculation later
		int actualRow = rowLength - 1;
		int actualColumn = columnLength - 1;
		
		//the smaller of the two pair of number will determine the size of matrix to the right or left of the disk
		int rowBelow = actualRow - row ;
		int columnToTheRight = actualColumn -column;
		int rowAbove = row;		
		int columnToTheLeft = column;	

		//determine matrix row column to the below and left
		if(rowBelow < columnToTheLeft )
		{
			sizeToLeft = rowBelow;
		}
		//determine matrix row column to the above and right
		else
		{
			sizeToLeft = columnToTheLeft;
		}
		
		if(rowAbove < columnToTheRight )
		{
			sizeToRight = rowAbove;
		}
		else
		{
			sizeToRight = columnToTheRight;
		}

		//this determine the last row on the left most column disk
		int rowDown = row + sizeToLeft;
		int columnToLeft = column - sizeToLeft;
		//this determine the first row right most column disk(use this and above disk to determine size of the matrix
		int rowUp = row - sizeToRight;
		int columnToRight = column + sizeToRight;

		//this is the matrix that would be extracted to check for diagonal
		int [][] majorDiagonalMatrix = new int[rowDown - rowUp + 1][columnToRight - columnToLeft + 1];
	
		//fill the matrix with disk extracted from the score board matrix
		for(int i = rowUp;i <= rowDown;i++)
		{
			for(int j = columnToLeft;j <= columnToRight;j++)
			{
				int currentRow = i - rowUp;
				int currentColumn = j - columnToLeft;
				majorDiagonalMatrix[currentRow][currentColumn] = scoreBoard[i][j];
			}
		}
		
		//this is the array that would check by the checkDiagonal function and what would be returned
		int [] majorDiagonalArray = new int[majorDiagonalMatrix.length]; 
		
        //this is the diagonal array extracted from the matrix 
		for(int matrixRow = 0 ; matrixRow < majorDiagonalMatrix.length ; matrixRow++)
		{
			for(int matrixColumn = 0 ; matrixColumn < majorDiagonalMatrix[matrixRow].length ; matrixColumn ++)
			{
				//number used to determine which number to extract from matrix
				int magicNumber =  majorDiagonalMatrix.length - 1 - matrixRow;
				
				if (matrixColumn == magicNumber)
				{
					majorDiagonalArray[matrixRow] = majorDiagonalMatrix[matrixRow][matrixColumn];
				}
			}
		}
		return majorDiagonalArray ;
	}
	
	// helper method to extract the inverse diagonal
	public static int[] extractMinorDiagonalMatrix(int[][] scoreBoard,int column,int row)
	{
		//this is the variable for the size of the matrix
		int sizeToLeft = 100;
		int sizeToRight = 100;
		
		//length of matrix(used for calculation later)
		int rowLength = scoreBoard.length;
		int columnLength = scoreBoard[0].length;
        //actual length of matrix with matrix count start at 0(used for calculation later)
		int actualRow = rowLength - 1;
		int actualColumn = columnLength - 1;

		//the smaller of the two pair of number will determine the size of matrix to the right or left of the disk
		int rowBelow = actualRow - row ;
		int columnToTheRight = actualColumn -column  ;
		int rowAbove = row;
		int columnToTheLeft = column;
	
		//the smaller number will determine the row below and column to the right
		if(rowBelow < columnToTheRight )
		{
			sizeToLeft = rowBelow;
		}
		else
		{
			sizeToLeft = columnToTheRight;
		}
		//the smaller number will determine the row above and column to the left
		if(rowAbove < columnToTheLeft  )
		{
			sizeToRight = rowAbove;
		}
		else
		{
			sizeToRight = columnToTheLeft;
		}
        
		//this will determine the last row and right most column disk
		int rowDown = row + sizeToLeft;
		int columnToRight = column + sizeToLeft;		

		//this will determine the first row and left most column disk
		int rowUp = row - sizeToRight;
		int columnToLeft = column - sizeToRight;

		//this is the matrix that we would extract diagonal from
		int [][] minorDiagonalMatrix = new int[rowDown - rowUp + 1][columnToRight - columnToLeft + 1];
	
		//this will fill up the minor diagonal matrix with number from scoreboard
		for(int i = rowUp;i <= rowDown;i++)
		{
			for(int j = columnToLeft;j <= columnToRight;j++)
			{
				int currentRow = i - rowUp;
				int currentColumn = j - columnToLeft;
				minorDiagonalMatrix[currentRow][currentColumn] = scoreBoard[i][j];
			}
		}
		
		//this is the array that the minor diagonal will be extracted to
		int [] minorDiagonalArray = new int[minorDiagonalMatrix.length]; 
		
		//fill the minor diagonal array with the number in the matrix
		for(int matrixRow = 0 ; matrixRow < minorDiagonalMatrix.length ; matrixRow++)
		{
			for(int matrixColumn = 0 ; matrixColumn < minorDiagonalMatrix[matrixRow].length ; matrixColumn ++)
			{
				if (matrixColumn == matrixRow)
				{
					minorDiagonalArray[matrixRow] = minorDiagonalMatrix[matrixRow][matrixColumn];
				}
			}
		}
		return minorDiagonalArray ;
	}
	
	//takes an array and check if the array has the same number
	public static boolean checkSame(int[] a)
	{
		//take a number to check
		int keyNumber = a[0];

		//check all the number, if any of the number equal to 2,we will return false
		if(keyNumber == 2)
		{
			return false;
		}
		else
		{
			for (int i = 1;i < a.length;i++)
			{
				if (a[i] != keyNumber ||  a[i] == 2)
				{
					return false;
				}
			}
		}
		return true;
	}
}
