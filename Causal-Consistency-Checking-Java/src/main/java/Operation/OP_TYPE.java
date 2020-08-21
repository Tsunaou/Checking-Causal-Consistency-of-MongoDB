package Operation;

public enum OP_TYPE {

    WRITE(":write"), READ(":read"), UNDEFINED("undefined");
    private String opType;
    private final int types = 2;

    OP_TYPE(String opType) {
        this.opType = opType;
    }

}
