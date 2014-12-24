import javafx.util.Pair;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class TestStaticTemplateMatcher {
    @Test
    public void simpleRandomTest() {
        ArrayList<String> templates = new ArrayList<String>();
        for (int j = 0; j < 10; ++j) {
            String s = randString(j + 10, 3);
            if (!templates.contains(s)) {
                templates.add(s);
            }
        }
        testedManyTemplates(templates, randString(100, 3));
    }

    @Test
    public void largeRandomTest() {
        for (int i = 0; i < 10; ++i) {
            ArrayList<String> templates = new ArrayList<String>();
            for (int j = 0; j < 50; ++j) {
                String s = randString(300 + j, 20);
                if (!templates.contains(s)) {
                    templates.add(s);
                }
            }
            testedManyTemplates(templates, randString(100000, 20));
        }
    }

    @Test
    public void TimeTest() {
        TStaticTemplateMatcher staticT = new TStaticTemplateMatcher();
        ArrayList<String> templates = new ArrayList<String>();
        for (int i = 0; i < 1000; ++i) {
            String newTemplate = (randString(50, 15));
            if (!templates.contains(newTemplate)) {
                templates.add(newTemplate);
            }
        }
        long timer = System.currentTimeMillis();
        for (String template : templates) {
            staticT.AddTemplate(template);
        }
        timer = System.currentTimeMillis() - timer;
        assertTrue(timer < (long) 5000);
        String text = randString(100000, 50);
        timer = System.currentTimeMillis();
        staticT.MatchStream(new CharStream(text));
        timer = System.currentTimeMillis() - timer;
        assertTrue(timer < (long) 5000);
    }

    private void checkAns(ArrayList<Pair<Integer, Integer>> naiveAns, ArrayList<Pair<Integer, Integer>> staticAns) {
        Collections.sort(naiveAns, new ComparePairs());//чтобы проверить, надо упорядочить
        Collections.sort(staticAns, new ComparePairs());
        assertEquals(naiveAns.size(), staticAns.size());
        for (int i = 0; i < naiveAns.size(); ++i) {
            assertEquals(naiveAns.get(i), staticAns.get(i));
        }
    }

    private void testedManyTemplates(ArrayList<String> templates, String text) {
        TNaiveTemplateMatcher naive = new TNaiveTemplateMatcher();
        TStaticTemplateMatcher staticT = new TStaticTemplateMatcher();
        for (String s : templates) {
            naive.AddTemplate(s);
            staticT.AddTemplate(s);
        }
        checkAns(naive.MatchStream(new CharStream(text)), staticT.MatchStream(new CharStream(text)));
    }

    class ComparePairs implements Comparator<Pair<Integer, Integer>> {
        @Override
        public int compare(Pair<Integer, Integer> a, Pair<Integer, Integer> b) {
            if (a.getKey() > b.getKey())
                return 1;
            if (a.getKey() < b.getKey())
                return -1;
            if (a.getValue() > b.getValue())
                return 1;
            if (a.getValue() < b.getValue())
                return -1;
            return 0;
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
