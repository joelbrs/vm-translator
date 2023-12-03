import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser {

    private final List<String[]> commands = new ArrayList<>();

    public Parser(String input) {
        String EOL = System.getProperty("line.separator");
        String[] output = input.split(EOL);

        for (String otp : output) {
            otp = otp.strip();

            if (otp.indexOf("//") != 0 && !otp.isEmpty()) {
                String[] parts = otp.split(" ");
                commands.add(parts);
            }
        }
    }

    public Boolean hasMoreCommands() {
        return !commands.isEmpty();
    }

    public Command nextCommand() {
        return new Command(commands.remove(0));
    }
}
