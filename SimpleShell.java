//  Mark Abramov
//  Shell Interface

/* The steps are:
 (1) parse the input to obtain the command and any parameters
 (2) create a ProcessBuilder object
 (3) start the process
 (4) obtain the output stream
 (5) output the contents returned by the command */


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class SimpleShell {

    public static void main(String[] args) throws IOException {
        // determine the directory we are starting from
        File homeDirectory = new File(System.getProperty("user.dir"));
        // create a ProcessBuilder object
        ProcessBuilder pb = new ProcessBuilder();
        pb.directory(homeDirectory);

        // create an object containing a history which includes all commands that have been entered by the user
        // since the shell was invoked
        HistoryOfCommandsEntered commandsEntered = new HistoryOfCommandsEntered();
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

        // we break out with <control><C>
        while (true) {
            System.out.print("jsh>");
            // read what the user entered
            String commandLine = console.readLine();
            boolean historyCommand = false; // tests if user entered a history command

            try {
                // this switch statement tests if commands should be switched out for history commands
                switch (commandLine) {
                    case "": // if the user entered a return, just loop again
                        continue;
                    case "!!":  // run the previous command in the history
                        if (commandsEntered.hasPreviousCommands()) {
                            commandLine = commandsEntered.getLastCommand();
                            commandsEntered.addCommand(commandLine);
                            historyCommand = true;
                            break;
                        } else { // if there is no previous command, output an appropriate error message.
                            System.out.println("Sorry, there are no previous commands in the history.");
                            commandsEntered.addCommand(commandLine);
                            continue;
                        }
                    default:
                        // when the user enters !<integer value i>, run the ith command in the history
                        if (commandLine.matches("!\\d*")) {
                            historyCommand = true;
                            //  get the number following the !
                            Integer numberEntered = Integer.parseInt(commandLine.substring(1));
                            //  ensure that the integer value is a valid number in the command history
                            if (numberEntered > commandsEntered.getCurrentCommandNumber()) {
                                System.out.println("Sorry, that command does not exist in the history. Please enter an integer value that is a valid number in the command history.");
                                commandsEntered.addCommand(commandLine);
                                continue;
                            }
                            commandLine = commandsEntered.getCommand(numberEntered);
                        }
                }
                switch (commandLine) {
                    // this switch statement executes special shell commands
                    case "history":
                        //  print out the contents of the history of command that have been entered into the shell,
                        //  along with the command numbers.
                        commandsEntered.printAllCommands();
                        if(!historyCommand) commandsEntered.addCommand(commandLine);
                        continue;
                    case "cd":  // change the current working directory to the user’s home directory
                        pb.directory(homeDirectory);
                        System.out.println(homeDirectory);
                        if(!historyCommand) commandsEntered.addCommand(commandLine);
                        continue;
                    case "cd..":  // change the current working directory to parent directory
                    case "cd ..":
                        pb.directory(new File(pb.directory().getParent()));
                        System.out.println(pb.directory());
                        if(!historyCommand) commandsEntered.addCommand(commandLine);
                        continue;
                    default:
                        // when the user enters "cd" followed by directory name, which would be the second parameter of the command
                        if (commandLine.contains("cd")) {
                            if(!historyCommand) commandsEntered.addCommand(commandLine);
                            File newDirectory = new File(pb.directory() + File.separator + commandLine.substring(3));
                            //  make sure the new path being specified is a valid directory
                            if (newDirectory.isDirectory()) {
                                pb.directory(newDirectory);
                                System.out.println(newDirectory);
                            } else
                                System.out.println("Sorry, " + newDirectory + " is not a valid file or directory. Please make sure the path being specified is a valid directory.");
                            continue;
                        }
                }

                // if program executes within Microsoft Windows, then it makes the first two elements
                // of the list passed to the ProcessBuilder constructor "cmd.exe" and "/c"
                pb.command((commandsEntered.isWindows() ? "cmd.exe /c" + commandLine : commandLine).split(" "));
                // start the process
                Process p = pb.start();
                // obtain the output stream
                BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                // output the contents returned by the command
                br.lines().forEach(System.out::println);
                br.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            if(!historyCommand) commandsEntered.addCommand(commandLine);
        }
    }
}
