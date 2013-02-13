package shell;

import java.io.BufferedReader;
import java.io.File;
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
	 * workingDir: The current working directory.
	 */
	private static String workingDir;
	
	/**
	 * LAUNCH_DIR: The directory that Jsh launches in.
	 */
	private static final String LAUNCH_DIR = System.getProperty("user.dir");
	
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
		// Set the current working directory.
		workingDir = System.getProperty("user.dir");
		
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
						if (Pattern.matches("(^cd){1}.*", nextline)) {
							// Change working directory to launch directory
							if (nextline.equals("cd")) {
								workingDir = LAUNCH_DIR;
							}
							// Change working directory to the specified directory
							else {
								String[] tokens = nextline.split("[ \t\n]+");
								if (tokens.length > 2) {
									System.err.print("Syntax error\n");
								}
								else if (tokens.length == 2) {
									File path = new File(tokens[1]);
									String absolutePath = path.getCanonicalPath();
									File abspath = new File(absolutePath);
									if (abspath.exists()) {
										if (abspath.isDirectory()) {
											workingDir = abspath.getCanonicalPath();
										}
										else {
											System.err.print(tokens[1] + ": Not a directory.\n");
										}
									}
									else {
										// TODO: Current bug: I can cd /etc/network, but not cd network from /etc.
										System.err.print(tokens[1] + ": No such directory.\n");
									}
								}
							}
						}
						// Print working directory.
						else if (nextline.equals("pwd")) {
							System.out.print(workingDir + "\n");
						}
						// Execute the command from the current working directory.
						else {
							handler = new CommandHandler(nextline, workingDir);
							handler.runProcess();
						}
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
