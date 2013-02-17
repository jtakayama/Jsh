package shell;
import java.lang.Exception;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.ProcessBuilder;
import java.lang.InterruptedException;
import java.lang.Thread;
import java.util.regex.Pattern;

/**
 * Parses and executes command shell input.
 * 
 * @author Jordan Takayama
 * @since 1.7
 */
public class CommandHandler {
	/**
	 * command: The command passed as input, and its flags.
	 */
	private String command;
	
	/**
	 * workingDir: The working directory of the command.
	 */
	private String workingDir;
		
	/**
	 * Constructs a new CommandHandler.
	 * 
	 * @param userInput	The command passed as input, and its flags.
	 */
	public CommandHandler(String userInput, String wdir) {
		this.command = userInput;
		this.workingDir = wdir;
	}
	
	/**
	 * Executes the command shell input after sending it to the parse method.
	 * 
	 * @return Returns the result of command execution, or an error message.
	 */
	public void runProcess() {
		String[] tokens = this.command.split("[ \t\n]+");
		ProcessBuilder pb = null;
		Process p = null;
		
		pb = new ProcessBuilder(tokens);
		
		// Set working directory if not null
		if (this.workingDir != null) {
			File dirSetter = new File(workingDir);
			pb.directory(dirSetter);
		}
		try {
			p = pb.start();
		}
		catch (IOException e) {
			if (Pattern.matches("(^Cannot run program){1}.+", e.getMessage())) {
				System.out.println("Unknown command: \'" + tokens[0] + "\'");
				return;
			}
			else {
				System.err.println(e.getMessage());
				return;
			}
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
			return;
		}
		
		// Input/Output relative to this process
		//   getErrorStream() gives an InputStream because the current
		//        process reads input from the created process's stderr.
		//   getInputStream() gives an InputStream because the current
		//        process reads input from the created process's stdout.
		//   getOutputStream() gives an OutputStream because the current
		//        process writes output into the created process's stdin.
		InputStream sstderr = p.getErrorStream();
		InputStream sstdout = p.getInputStream();
		OutputStream sstdin = p.getOutputStream();
		
		// Create BufferedReader objects associated with the above streams
		BufferedReader stderr = new BufferedReader(new InputStreamReader(sstderr));
		BufferedReader stdout = new BufferedReader(new InputStreamReader(sstdout));
		BufferedWriter stdin = new BufferedWriter(new OutputStreamWriter(sstdin));
		
	    // Communication can now happen when necessary, e.g., reading one line
	    // from the process' stdout, and printing it out to stdout
	    try {
	      String outLine = null;
	      String err = null;
	      // Print stdout.
		  while ((outLine = stdout.readLine()) != null) {
		    System.out.println(outLine);
		  }
	      // Print stderr.
	      while ((err = stderr.readLine()) != null) {
	    		  System.out.println(err);
	      }
	    } catch (IOException e) {
	      System.err.println("IO Error");
	    }		
		
		// Wait for the process to terminate.
	    try {
	      int returnValue = p.waitFor();
	    } catch (InterruptedException e) {
	      // Do nothing in current version
	    }

	    // close streams
	    try {
	      sstderr.close();
	      sstdout.close();
	      sstdin.close();
	    } catch (IOException e) {
	      System.err.println("IO Error");
	    }
	 
	    // destroying the process if necessary
	    try {
	      // Sleep time reduced to make shell work at a faster speed
	      Thread.sleep(1000);
	    } catch (InterruptedException e) {
	      // Do nothing for now... this is about threads and will
	      // be explained later
	    }

	    p.destroy();
	    return;
	}
}
