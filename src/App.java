import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class App {
    private static String fromFile(File file) {
        byte[] bytes;

        try {
            bytes = Files.readAllBytes(file.toPath());
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static void translateFile (File file, CodeWriter code) {
        String input = fromFile(file);
        Parser p = new Parser(input);

        while (p.hasMoreCommands()) {
            Command command = p.nextCommand();
            switch (command.getType()) {
                case ADD:
                    code.writeArithmeticAdd();
                    break;
                case SUB:
                    code.writeArithmeticSub();
                    break;
                case NEG:
                    code.writeArithmeticNeg();
                    break;
                case NOT:
                    code.writeArithmeticNot();
                    break;
                case EQ:
                    code.writeArithmeticEq();
                    break;
                case LT:
                    code.writeArithmeticLt();
                    break;
                case GT:
                    code.writeArithmeticGt();
                    break;
                case AND:
                    code.writeArithmeticAnd();
                    break;
                case OR:
                    code.writeArithmeticOr();
                    break;
                case PUSH:
                    code.writePush(command.getArgs().get(0), Integer.parseInt(command.getArgs().get(1)));
                    break;
                case POP:
                    code.writePop(command.getArgs().get(0), Integer.parseInt(command.getArgs().get(1)));
                    break;
                default:
                    System.out.println(command.getType().toString() + " not implemented");
            }
        }
    }
}