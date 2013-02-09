package shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import shell.CommandHandler;

/**
 * Top-level class for a Java command shell for Linux.
 * 
 * @author Jordan Takayama
 * @since Java 1.6
 */
public class Jsh {
	/**
	 * The main class for the shell.
	 * 
	 * @param args	Passes in the command-line arguments to the shell.
	 */
	public static void main(String[] args) {
		// InputStreamReader for the BufferedReader.
		InputStreamReader shellInput = new InputStreamReader(System.in);
		// BufferedReader for the shell.
		BufferedReader shellBuffer = new BufferedReader(shellInput);
		// CommandHandler to parse and execute the input commands.
		CommandHandler commandshell = new CommandHandler();
		while(true) {
			System.out.print("jsh> ");
			try {
				String input = shellBuffer.readLine().trim();
				if(checkForEOF(input)) {
					System.exit(0);
				}
				else {
					System.out.println("\n" + commandshell.execute(input));
				}
			}
			catch (IOException ioe) {
				System.out.println("IOException: \n" + ioe.getMessage());
			}
			// TODO: How to print stderror instead of this?
			catch (Exception ex) {
				System.out.println("Error: " + ex.getMessage());
			}
		}
	}
	
	/**
	 * Checks whether input contains an EOF (^D in Linux), which is the signal
	 * to quit this shell.
	 * 
	 * @param input Terminal input.
	 * @return True if input contains a carriage return, false if not. 
	 */
	private static boolean checkForEOF(String input) {
		String eof_windows = "\r\n";
		String eof_linux = "\n";
		// TODO: method unimplemented.
		return input.matches("^" + eof_linux + "{1}+|^" 
				+ eof_windows + "{1}+");
	}

}
