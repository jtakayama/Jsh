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
		CommandHandler handler = null;
		while(true) {
			System.out.print("jsh> ");
			try {
				// Note: As of Eclipse Juno / January 2013, Ctrl-D is
				// not registered by the Eclipse console in Windows.
				// See https://bugs.eclipse.org/bugs/show_bug.cgi?id=159803.
				String nextline = shellBuffer.readLine();
				if(nextline == null) {
					// If no input is read, assume a ^D / EOF was entered.
					System.exit(0);
				}
				else {
					// A BufferedReader strips out line feeds and carriage returns.
					// A line of input that is only line feeds and/or carriage returns
					// will have length 0.
					if (nextline.length() == 0) {
						continue;
					}
					else {
						handler = new CommandHandler(nextline);
						handler.runProcess();
						continue;
					}
				}
			}
			catch (IOException ioe) {
				System.out.println("IOException: \n" + ioe.getMessage());
			}
			catch (Exception ex) {
				System.out.println("Error: " + ex.getMessage());
			}
		}
	}
}
