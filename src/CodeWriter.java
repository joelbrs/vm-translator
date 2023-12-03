import enums.Segments;
import utils.WriteUtils;

public class CodeWriter implements WriteUtils {

    private final StringBuilder output = new StringBuilder();
    private final String moduleName = "Main";
    private String outputFileName;
    private Integer labelCount = 0, callCount = 0;

    public CodeWriter(String fileName) {
        outputFileName = fileName;
    }

    public String registerName(String segment, int index) {
        Segments seg = Segments.getSegment(segment.toLowerCase());

        if (seg != null && Segments.isMemorySegment(seg)) {
            return seg.registerName(index);
        }
        return moduleName + "." + index;
    }

    public void writeInit() {
        write(output, "@256");
        write(output, "D=A");
        write(output, "@SP");
        write(output, "M=D");
    }

    public void writePush(String segment, int index) {
        Segments seg = Segments.getSegment(segment.toLowerCase());

        if (seg != null && Segments.isVariableSegment(seg)) {
            seg.writePushSegment(output, index);
            return;
        }

        write(output,"@" + registerName(segment,0) + " // push " + seg + " " + index);
        write(output,"D=M");
        write(output,"@" + index);
        write(output,"A=D+A");
        write(output,"D=M");
        write(output,"@SP");
        write(output,"A=M");
        write(output,"M=D");
        write(output,"@SP");
        write(output,"M=M+1");
    }


    public void writePop(String segment, int index) {
        Segments seg = Segments.getSegment(segment.toLowerCase());

        if (seg != null && Segments.isDataSegment(seg)) {
            seg.writePopSegment(output, index);
            return;
        }

        write(output, "@" + registerName(segment, 0) + " // pop " + seg + " " + index);
        write(output, "D=M");
        write(output, "@" + index);
        write(output, "D=D+A");
        write(output, "@R13");
        write(output, "M=D");
        write(output, "@SP");
        write(output, "M=M-1");
        write(output, "A=M");
        write(output, "D=M");
        write(output, "@R13");
        write(output, "A=M");
        write(output, "M=D");
    }

    @Override
    public void write(StringBuilder output, String s) {
        output.append(String.format("%s\n", s));
    }
}
