import javafx.util.Pair;
import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.Random;

public class TestSingleMatcher {
    @Test
    public void simpleTest() {
        tested("a", "abracadabra");
        tested("ra", "abracadabra");
        tested("aba", "ababahalamaha");
    }

    @Test
    public void simpleRandomTest() {
        for (int i = 21; i < 100; ++i) {
            tested(randString(5, 2), randString(i, 2));
            prependTests(randString(5, 2), randString(i, 2));
            appendTests(randString(5, 2), randString(i, 2));
            tested(randString(i - 20, 2), randString(i, 2));
            prependTests(randString(i - 20, 2), randString(i, 2));
            appendTests(randString(i - 20, 2), randString(i, 2));
        }
    }

    private void prependTests(String template, String text) {
        TSingleMatcher single = new TSingleMatcher();
        single.AddTemplate("a" + template);
        TSingleMatcher prepend = new TSingleMatcher();
        prepend.AddTemplate(template);
        prepend.PrependCharToTemplate('a');
        checkAnswer(single.MatchStream(new CharStream(text)), prepend.MatchStream(new CharStream(text)));
    }

    private void appendTests(String template, String text) {
        TSingleMatcher single = new TSingleMatcher();
        single.AddTemplate(template + 'a');
        TSingleMatcher append = new TSingleMatcher();
        append.AddTemplate(template);
        append.PrependCharToTemplate('a');
        checkAnswer(single.MatchStream(new CharStream(text)), append.MatchStream(new CharStream(text)));
    }

    @Test
    public void largeRandomTest() {
        for (int i = 0; i < 10; ++i) {
            tested(randString(30, 20), randString(10000, 20));
            appprepTests(randString(30, 20), randString(10000, 20));
        }
    }

    private void appprepTests(String template, String text) {
        TSingleMatcher single = new TSingleMatcher();
        single.AddTemplate(template);
        TSingleMatcher test = new TSingleMatcher();
        test.AddTemplate(template);
        for (int i = 0; i < 10; ++i) {
            template = 'a' + template + 'b';
            single.AddTemplate(template);
            test.AppendCharToTemplate('b');
            test.PrependCharToTemplate('a');
            checkAnswer(single.MatchStream(new CharStream(text)), test.MatchStream(new CharStream(text)));
        }
    }

    @Test
    public void TimeTest() {
        TSingleMatcher single = new TSingleMatcher();
        String text = randString(10000, 20);
        long timer = System.currentTimeMillis();
        for (int i = 0; i < 1000; ++i) {
            single.AddTemplate(randString(30, 10));
            single.MatchStream(new CharStream(text));
        }
        timer = System.currentTimeMillis() - timer;
        assertTrue(timer < (long) 5000);
    }

    private void tested(String template, String text) {
        TNaiveTemplateMatcher naive = new TNaiveTemplateMatcher();
        TSingleMatcher single = new TSingleMatcher();
        naive.AddTemplate(template);
        single.AddTemplate(template);
        checkAnswer(naive.MatchStream(new CharStream(text)), single.MatchStream(new CharStream(text)));
    }

    private void checkAnswer(ArrayList<Pair<Integer, Integer>> naiveAnswer, ArrayList<Pair<Integer, Integer>> singleAnswer) {
        assertEquals(naiveAnswer.size(), singleAnswer.size());
        for (int i = 0; i < naiveAnswer.size(); ++i) {
            assertEquals(naiveAnswer.get(i), singleAnswer.get(i));
        }
    }

    private String randString(int length, int numberOfDiffLetters) {
        String string = "";
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < length; ++i) {
            int p = Math.abs(random.nextInt(numberOfDiffLetters)) + 37;
            string += (char) p;
        }
        return string;
    }
}