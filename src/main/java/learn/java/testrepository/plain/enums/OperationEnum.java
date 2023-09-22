package learn.java.testrepository.plain.enums;

public final class OperationEnum {

    public static final OperationEnum PLUS = new OperationEnum("+");
    public static final OperationEnum MINUS = new OperationEnum("-");
    public static final OperationEnum TIMES = new OperationEnum("*");
    public static final OperationEnum DIVIDE = new OperationEnum("/");

    private final String symbol;

    private OperationEnum(String symbol) {
        this.symbol = symbol;
    }
}
