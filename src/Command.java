import enums.CommandTypes;

import java.util.ArrayList;
import java.util.List;

public class Command {
    private final CommandTypes type;
    private final List<String> args = new ArrayList<>();

    public Command(String[] commands) {
        if (commands[0].equals("if-goto")) {
            type = CommandTypes.IF;
        } else {
            type = CommandTypes.valueOf(commands[0].toUpperCase());
        }

        for (String command : commands) {
            int pos = args.indexOf("//");

            if (pos != -1) {
                command = command.substring(0, pos);
            }
            args.add(command.strip());
        }
    }

    public List<String> getArgs() {
        return args;
    }

    public CommandTypes getType() {
        return type;
    }
}
