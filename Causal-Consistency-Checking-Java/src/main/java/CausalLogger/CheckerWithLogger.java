package CausalLogger;

public interface CheckerWithLogger {
    boolean LOGGER = false;
    boolean PRINT = true;
    void checkLoggerInfo(String message);
    void checkLoggerWarning(String message);
}
