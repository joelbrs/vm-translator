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
                case GOTO:
                    code.writeGoto(command.getArgs().get(0));
                    break;
                case IF:
                    code.writeIf(command.getArgs().get(0));
                    break;
                case LABEL:
                    code.writeLabel(command.getArgs().get(0));
                    break;
                default:
                    System.out.println(command.getType().toString() + " not implemented");
            }
        }
    }

    public static void main(String[] args) {
        String path = "projects\\07\\MemoryAccess\\BasicTest";

        File file = new File(path);

        if (!file.exists()) {
            System.err.println("The file doesn't exist.");
            System.exit(1);
        }

        // we need to compile every file in the directory
        if (file.isDirectory()) {

            var outputFileName = file.getAbsolutePath() + "/" + file.getName() + ".asm";
            System.out.println(outputFileName);
            CodeWriter code = new CodeWriter(outputFileName);

            //code.writeInit();

            for (File f : file.listFiles()) {
                if (f.isFile() && f.getName().endsWith(".vm")) {

                    var inputFileName = f.getAbsolutePath();
                    var pos = inputFileName.indexOf('.');

                    System.out.println("compiling " + inputFileName);
                    translateFile(f, code);

                }

            }
            code.save();
            // we only compile the single file
        } else if (file.isFile()) {
            if (!file.getName().endsWith(".vm")) {
                System.err.println("Please provide a file name ending with .vm");
                System.exit(1);
            } else {
                var inputFileName = file.getAbsolutePath();
                var pos = inputFileName.indexOf('.');
                var outputFileName = inputFileName.substring(0, pos) + ".asm";
                CodeWriter code = new CodeWriter(outputFileName);
                System.out.println("compiling " + inputFileName);
                //code.writeInit();
                translateFile(file, code);
                code.save();
            }
        }
    }
}