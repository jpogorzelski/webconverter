package pl.pogorzelski.webconverter.controller;

/**
 * @author Kuba
 */
public class CompilationException extends Throwable {
    String s;

    public CompilationException(String s) {
        this.s = s;
    }

    @Override
    public String getMessage() {
        return s + " :: " + super.getMessage();
    }
}
