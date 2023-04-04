package ct.model;

import javax.swing.event.DocumentEvent;
import java.io.Serializable;

public class ChangedText implements Serializable {
    public static final int INSERT = 1;
    public static final int REMOVE = 2;

    private String guid;
    private int line;
    private int offset;
    private int length;
    private int type;
    private String str;

    public ChangedText(int line, int offset, int length, int type, String str) {
        super();
        this.line = line;
        this.offset = offset;
        this.length = length;
        this.type = type;
        this.str = str;
    }

    public ChangedText(String guid, int line, int offset, int length, int type, String str) {
        super();
        this.guid = guid;
        this.line = line;
        this.offset = offset;
        this.length = length;
        this.type = type;
        this.str = str;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        if (type == REMOVE) {
            return "remove " + line + ", " + offset + ", " + length;
        }
        if (type == INSERT) {
            return "insert " + line + ", " + offset + ", " + length + "\n++" + str;
        }
        return "event null";
    }
}
