import enums.CommandTypes;

import java.util.ArrayList;
import java.util.List;

public class Command {
    private final CommandTypes type;
    private final List<String> args = new ArrayList<>();

    public List<String> getArgs() {
        return args;
    }

    public CommandTypes getType() {
        return type;
    }

    public Command(String[] commands) {
        if (commands[0].equals("if-goto")) {
            type = CommandTypes.IF;
        } else {
            type = CommandTypes.valueOf(commands[0].toUpperCase());
        }

        for (int i = 1; i < commands.length; i++) {
            var arg = commands[i];
            var pos = arg.indexOf("//");

            if (pos != -1) {
                arg = arg.substring(0, pos);
            }
            args.add(arg.strip());
        }
    }
}
