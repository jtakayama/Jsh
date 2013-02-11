package shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

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
				// TODO: This section is untested, as Eclipse still can't read Ctrl-D
				// TODO: from its terminal. See https://bugs.eclipse.org/bugs/show_bug.cgi?id=159803.
				String nextline = shellBuffer.readLine();
				if(nextline == null) {
					// Exit program if Ctrl-D (^D), an EOF, is detected.
					// Not sure if this will cause it to return -1.
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
						System.out.println(commandshell.execute(nextline.trim()));
					}
				}
			}
			catch (IOException ioe) {
				System.out.println("IOException: \n" + ioe.getMessage());
			}
			// TODO: Later, this will print stderror messages from CommandHandler
			catch (Exception ex) {
				System.out.println("Error: " + ex.getMessage());
			}
		}
	}
}
