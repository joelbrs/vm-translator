import enums.Segments;
import utils.WriteUtils;

import java.io.FileOutputStream;
import java.io.IOException;

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

        if (seg != null && (Segments.isVariableSegment(seg) || seg.equals(Segments.CONSTANT))) {
            if (Segments.isVariableSegment(seg)) {
                write(output, "@" + registerName(segment, index) + " // push " + segment + " " + index);
                write(output, "D=M");
            } else {
                write(output, "@" + index + " // push " + segment + " " + index);
                write(output, "D=A");
            }
        } else {
            write(output, "@" + registerName(segment, 0) + " // push " + segment + " " + index);
            write(output, "D=M");
            write(output, "@" + index);
            write(output, "A=D+A");
        }
        write(output, "D=M");
        write(output, "@SP");
        write(output, "A=M");
        write(output, "M=D");
        write(output, "@SP");
        write(output, "M=M+1");
    }


    public void writePop(String segment, int index) {
        Segments seg = Segments.getSegment(segment.toLowerCase());

        if (seg != null && Segments.isDataSegment(seg)) {
            write(output, "@SP // pop " + segment + " " + index);
            write(output, "M=M-1");
            write(output, "A=M");
            write(output, "D=M");
            write(output, "@" + registerName(segment, index));
            write(output, "M=D");
        } else {
            write(output, "@" + registerName(segment, 0) + " // pop " + segment + " " + index);
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
    }

    void writeArithmeticAdd() {
        write(output, "@SP // add");
        write(output, "M=M-1");
        write(output, "A=M");
        write(output, "D=M");
        write(output, "A=A-1");
        write(output, "M=D+M");
    }

    void writeArithmeticSub() {
        write(output, "@SP // sub");
        write(output, "M=M-1");
        write(output, "A=M");
        write(output, "D=M");
        write(output, "A=A-1");
        write(output, "M=M-D");
    }

    void writeArithmeticNeg() {
        write(output, "@SP // neg");
        write(output, "A=M");
        write(output, "A=A-1");
        write(output, "M=-M");
    }


    void writeArithmeticAnd() {
        write(output, "@SP // and");
        write(output, "AM=M-1");
        write(output, "D=M");
        write(output, "A=A-1");
        write(output, "M=D&M");
    }

    void writeArithmeticOr() {
        write(output, "@SP // or");
        write(output, "AM=M-1");
        write(output, "D=M");
        write(output, "A=A-1");
        write(output, "M=D|M");
    }

    void writeArithmeticNot() {

        write(output, "@SP // not");
        write(output, "A=M");
        write(output, "A=A-1");
        write(output, "M=!M");
    }


    void writeArithmeticEq() {
        String label = ("JEQ_" + moduleName + "_" + (labelCount));
        write(output, "@SP // eq");
        write(output, "AM=M-1");
        write(output, "D=M");
        write(output, "@SP");
        write(output, "AM=M-1");
        write(output, "D=M-D");
        write(output, "@" + label);
        write(output, "D;JEQ");
        write(output, "D=1");
        write(output, "(" + label + ")");
        write(output, "D=D-1");
        write(output, "@SP");
        write(output, "A=M");
        write(output, "M=D");
        write(output, "@SP");
        write(output, "M=M+1");

        labelCount++;
    }

    void writeArithmeticGt() {
        String labelTrue = ("JGT_TRUE_" + moduleName + "_" + (labelCount));
        String labelFalse = ("JGT_FALSE_" + moduleName + "_" + (labelCount));

        write(output, "@SP // gt");
        write(output, "AM=M-1");
        write(output, "D=M");
        write(output, "@SP");
        write(output, "AM=M-1");
        write(output, "D=M-D");
        write(output, "@" + labelTrue);
        write(output, "D;JGT");
        write(output, "D=0");
        write(output, "@" + labelFalse);
        write(output, "0;JMP");
        write(output, "(" + labelTrue + ")");
        write(output, "D=-1");
        write(output, "(" + labelFalse + ")");
        write(output, "@SP");
        write(output, "A=M");
        write(output, "M=D");
        write(output, "@SP");
        write(output, "M=M+1");

        labelCount++;
    }

    void writeArithmeticLt() {
        String labelTrue = ("JLT_TRUE_" + moduleName + "_" + (labelCount));
        String labelFalse = ("JLT_FALSE_" + moduleName + "_" + (labelCount));

        write(output, "@SP // lt");
        write(output, "AM=M-1");
        write(output, "D=M");
        write(output, "@SP");
        write(output, "AM=M-1");
        write(output, "D=M-D");
        write(output, "@" + labelTrue);
        write(output, "D;JLT");
        write(output, "D=0");
        write(output, "@" + labelFalse);
        write(output, "0;JMP");
        write(output, "(" + labelTrue + ")");
        write(output, "D=-1");
        write(output, "(" + labelFalse + ")");
        write(output, "@SP");
        write(output, "A=M");
        write(output, "M=D");
        write(output, "@SP");
        write(output, "M=M+1");

        labelCount++;
    }

    void writeLabel(String label) {
        write(output, "(" + label + ")");
    }

    void writeGoto(String label) {
        write(output, "@" + label);
        write(output, "0;JMP");
    }

    void writeIf(String label) {
        write(output, "@SP");
        write(output, "AM=M-1");
        write(output, "D=M");
        write(output, "M=0");
        write(output, "@" + label);
        write(output, "D;JNE");

    }

    void writeFunction(String funcName, int nLocals) {

        var loopLabel = funcName + "_INIT_LOCALS_LOOP";
        var loopEndLabel = funcName + "_INIT_LOCALS_END";


        write(output, "(" + funcName + ")" + "// initializa local variables");
        write(output, String.format("@%d", nLocals));
        write(output, "D=A");
        write(output, "@R13"); // temp
        write(output, "M=D");
        write(output, "(" + loopLabel + ")");
        write(output, "@" + loopEndLabel);
        write(output, "D;JEQ");
        write(output, "@0");
        write(output, "D=A");
        write(output, "@SP");
        write(output, "A=M");
        write(output, "M=D");
        write(output, "@SP");
        write(output, "M=M+1");
        write(output, "@R13");
        write(output, "MD=M-1");
        write(output, "@" + loopLabel);
        write(output, "0;JMP");
        write(output, "(" + loopEndLabel + ")");
    }

    void writeFramePush(String value) {
        write(output, "@" + value);
        write(output, "D=M");
        write(output, "@SP");
        write(output, "A=M");
        write(output, "M=D");
        write(output, "@SP");
        write(output, "M=M+1");
    }

    public String codeOutput() {
        return output.toString();
    }

    public void save() {
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(outputFileName);

            outputStream.write(output.toString().getBytes());

            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void write(StringBuilder output, String s) {
        output.append(String.format("%s\n", s));
    }
}
