package enums;

import jdk.jfr.ContentType;
import utils.WriteUtils;

import java.util.List;

public enum Segments implements WriteUtils {
    LOCAL {
        @Override
        public String registerName(int index) {
            return "LCL";
        }
    },
    ARGUMENT {
        @Override
        public String registerName(int index) {
            return "ARG";
        }
    },
    POINTER {
        @Override
        public String registerName(int index) {
            return "R" + (3 + index);
        }
    },
    TEMP {
        @Override
        public String registerName(int index) {
            return "R" + (5 + index);
        }
    },
    CONSTANT {
        @Override
        public void writePushSegment(StringBuilder output, int index) {
            write(output, "@" + index + " // push " + name() + " " + index);
            write(output, "D=A");
            write(output, "@SP");
            write(output, "A=M");
            write(output, "M=D");
            write(output, "@SP");
            write(output, "M=M+1");
        }
    },
    THIS,
    THAT,
    STATIC;

    private static final List<Segments> memorySegments  = List.of(LOCAL, ARGUMENT, THIS, THAT, POINTER, TEMP, STATIC);
    private static final List<Segments> variablesSegments  = List.of(STATIC, CONSTANT, POINTER, TEMP);
    private static final List<Segments> dataSegments = List.of(STATIC, TEMP, POINTER);

    public static Segments getSegment(String segment) {
        for (Segments s : values()) {
            if (s.toString().toLowerCase().equals(segment)) {
                return s;
            }
        }
        return null;
    }

    public static Boolean isMemorySegment(Segments segment) {
        return memorySegments .contains(segment);
    }

    public static Boolean isVariableSegment(Segments segment) {
        return variablesSegments.contains(segment);
    }

    public static Boolean isDataSegment(Segments segment) {
        return dataSegments.contains(segment);
    }

    public String registerName(int index) {
        return name().toUpperCase();
    };

    /**
     * STATIC,
     * TEMP,
     * POINTER
     */
    public void writePushSegment(StringBuilder output, int index) {
        write(output, "@" + registerName(index) + " // push " + name() + " " + index);
        write(output, "D=M");
        write(output, "@SP");
        write(output, "A=M");
        write(output, "M=D");
        write(output, "@SP");
        write(output, "M=M+1");
    }

    /**
     * STATIC,
     * TEMP,
     * POINTER
     * */
    public void writePopSegment(StringBuilder output, int index) {
        write(output, "@SP // pop " + name() + " " + index);
        write(output, "M=M-1");
        write(output, "A=M");
        write(output, "D=M");
        write(output, "@" + registerName(index));
        write(output, "M=D");
    }

    @Override
    public void write(StringBuilder output, String s) {
        output.append(String.format("%s\n", s));
    }
}
