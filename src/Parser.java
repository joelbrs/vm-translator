import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser {

    private List<String[]> commands = new ArrayList<>();

    public Parser(String input) {
        final String EOL = System.getProperty("line.separator");
        String[] output = input.split(EOL);

        commands = Arrays.stream(output)
                    .map(String::strip)
                    .filter(s -> s.indexOf("//") != 0 && !s.isEmpty())
                    .map(s -> s.split(" "))
                    .toList();
    }

    public Boolean hasMoreCommands() {
        return !commands.isEmpty();
    }

    //TODO: nextCommand
}
