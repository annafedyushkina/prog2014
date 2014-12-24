import org.junit.Test;
import javax.management.openmbean.KeyAlreadyExistsException;

public class TestException {
    @Test(expected = NullPointerException.class)
    public void testNaiveEmpty() {
        TNaiveTemplateMatcher a = new TNaiveTemplateMatcher();
        a.AddTemplate("");
    }

    @Test(expected = KeyAlreadyExistsException.class)
    public void testNaiveSame() {
        TNaiveTemplateMatcher a = new TNaiveTemplateMatcher();
        a.AddTemplate("a");
        a.AddTemplate("a");
    }

    @Test(expected = ExceptionInInitializerError.class)
    public void testSingleEmpty() {
        TSingleMatcher a = new TSingleMatcher();
        a.AddTemplate("");
    }
    @Test(expected = ExceptionInInitializerError.class)
    public void testSingleLetter() {
        TSingleMatcher a = new TSingleMatcher();
        a.AppendCharToTemplate((char) 20);
    }

    @Test(expected = KeyAlreadyExistsException.class)
    public void testStaticSame() {
        TStaticTemplateMatcher a = new TStaticTemplateMatcher();
        a.AddTemplate("a");
        a.AddTemplate("a");
    }

    @Test(expected = NullPointerException.class)
    public void testStaticEmpty() {
        TStaticTemplateMatcher a = new TStaticTemplateMatcher();
        a.AddTemplate("");
    }
}