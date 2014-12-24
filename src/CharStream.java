import java.util.NoSuchElementException;

public class CharStream implements ICharStream {
    private int index;
    private String string;

    public CharStream(String str) {
        index = 0;
        string = str;
    }

    @Override
    public char GetChar() {
        if (index < string.length()) {
            if (string.charAt(index) < 32 || string.charAt(index) > 255) {
                throw new ExceptionInInitializerError();
            }
            index++;
            return string.charAt(index - 1);
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public boolean IsEmpty() {
        return (index >= string.length());
    }
}