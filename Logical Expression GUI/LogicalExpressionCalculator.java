/*Author: Nathan Zimmer
 * Date: 1/25/21
 * Class: COSC 314
 * Purpose: to take inputs for a logical expression such as "a and b or nc" and calculate the t/f value of that statement
 */
import java.util.Scanner;

public class LogicalExpressionCalculator {
	static Scanner scanner = new Scanner(System.in);
	
	public static void main(String[] args) {
		boolean answer = logicalExpression(setBools(numberInput()));
		System.out.println("The truth value for the given logical expression is " + answer);
	}

	//makes sure the user enters a number between 2 and 5 and returns that number
	static int numberInput() {
		int varNum = 0;
		
		System.out.println("Enter the number of variables (between 2 and 5): ");
		
		while (varNum < 2 || varNum > 5) {
			try {
				varNum = scanner.nextInt();
			} catch(Exception e) {
				scanner.next();
			}
			if (varNum < 2 || varNum > 5) {
				System.out.println("Invalid input. Enter an int between 2 and 5: ");
			}
		}
		return varNum;
	}
	
	//makes sure the user inputs true or false and returns an array of those values that is the length of varNum
	static boolean[] setBools(int varNum) {
		boolean[] bools = new boolean[varNum];
		char[] names = {'a','b','c','d','e'}; //array to make printing a-e easier
		
		for (int i = 0; i < varNum; i++) {
			System.out.println("Enter true/false value for " + names[i] +": ");
			
			while (!scanner.hasNextBoolean()) {
				System.out.println("Invalid input. Enter true or false");
				scanner.next();
			}
			bools[i] = scanner.nextBoolean();
		}
		return bools;
	}
	
	//takes in a logical expression in the format of "a and b or nc" and returns the value of that expression
	static boolean logicalExpression(boolean[] bools) {
		boolean valid = false; //user input will be taken until this value is true
		boolean answer = false;
		boolean isAnd = false; //when true, logical and is used in calculations. When false, logical or is used
		int varNum = bools.length;
		int ASCII_A = 97; //the ASCII value of 'a'. Used for determining if the letter inputs fall in the correct range
		
		System.out.println("Enter the logical expression: ");
		
		while (valid == false) {
			String[] input = scanner.nextLine().split(" ");
			for (int i = 0; i < input.length; i++) {
				if (i % 2 == 0) { //calculates the t/f values of the letters, and whether or not the input is valid
					if ((input[i].length() == 2) && (input[i].charAt(0) == 'n') && ((input[i].charAt(1) >= ASCII_A) && (input[i].charAt(1) < ASCII_A + varNum))) {
						valid = true;
						if (isAnd) {
							answer = answer && !bools[input[i].charAt(1) - ASCII_A];
						}
						else {
							answer = answer || !bools[input[i].charAt(1) - ASCII_A];
						}
					}
					else if ((input[i].length() == 1) && ((input[i].charAt(0) >= ASCII_A) && (input[i].charAt(0) < ASCII_A + varNum))) {
						valid = true;
						if (isAnd) {
							answer = answer && bools[input[i].charAt(0) - ASCII_A];
						}
						else {
							answer = answer || bools[input[i].charAt(0) - ASCII_A];
						}
					}
					else {
						valid = false;
						break;
					}
				}
				else if (i % 2 != 0) { //calculates the value of isAnd and whether or not the input is valid
					if (input[i].contentEquals("and")) {
						valid = true;
						isAnd = true;
					}
					else if (input[i].contentEquals("or")) {
						valid = true;
						isAnd = false;
					}
					else {
						valid = false;
						break;
					}
				}
			}
			if (((valid == false) || (input.length % 2 == 0)) && !input[0].isBlank()) {
				System.out.println("Invalid Input. Try again.");
				valid = false;
			}
		}	
		return answer;
	}
}