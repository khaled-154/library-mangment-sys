package exceptions;

public class bookisnotborrowedforthisuser extends Exception {
    public bookisnotborrowedforthisuser() {
        super("book is not borrowed for this user");
    }


}
