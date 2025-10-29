package exceptions;

public class bookisalreadybrowed extends Exception {
    public bookisalreadybrowed() {
        super("book is browed already");
    }
}
