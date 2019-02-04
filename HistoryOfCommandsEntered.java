import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class HistoryOfCommandsEntered {
    private List<String> commands;
    final private String os;

    // constructor
    public HistoryOfCommandsEntered() {
        commands = new ArrayList<>();
        os = System.getProperty("os.name");
    }

    public boolean hasPreviousCommands(){
        return commands.size() > 0;
    }

    public void addCommand(String command){
        commands.add(command);
    }

    public String getLastCommand() {
        return commands.get(commands.size()-1);
    }

    // return the command number specified as the argument
    public String getCommand(int index) {
        return commands.get(index);
    }

    public Integer getCurrentCommandNumber() {
        return commands.size() - 1;
    }

    public boolean isWindows(){
        return os.contains("Windows");
    }

    //  print out the contents of the history of commands that have been entered into the shell,
    //  along with the command numbers
    public void printAllCommands(){
        IntStream.range(0, commands.size()).mapToObj(i -> i + " " + commands.get(i)).forEach(System.out::println);
    }
}