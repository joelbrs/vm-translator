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
    CONSTANT,
    THIS,
    THAT,
    STATIC;

    private static final List<Segments> memorySegments  = List.of(LOCAL, ARGUMENT, THIS, THAT, POINTER, TEMP);
    private static final List<Segments> variablesSegments  = List.of(STATIC, POINTER, TEMP);
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
        return memorySegments.contains(segment);
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

    @Override
    public void write(StringBuilder output, String s) {
        output.append(String.format("%s\n", s));
    }
}
