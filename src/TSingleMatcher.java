import javafx.util.Pair;
import java.util.ArrayList;

public class TSingleMatcher implements IMetaTemplateMatcher {
    ArrayList<Integer> templatesPrefixf = new ArrayList<Integer>();
    String template = null;
    String prepend = "";

    @Override
    public int AddTemplate(String templateTmp) {
        if (templateTmp == null || templateTmp.isEmpty()) {
            System.err.println("empty template");
            throw new ExceptionInInitializerError();
        }
        template = templateTmp + '$';
        return template.hashCode();
    }

    ArrayList<Pair<Integer, Integer>> answer = new ArrayList<Pair<Integer, Integer>>();

    @Override
    public ArrayList<Pair<Integer, Integer>> MatchStream(ICharStream stream) {
        if (template == null) {
            System.err.println("no template");
            throw new ExceptionInInitializerError();
        }
        String current = "";
        int pref = 0, n = 0;
        while (!stream.IsEmpty()) {
            char c = stream.GetChar();
            n++;
            //System.out.println(template);
            if(n < template.length() + prepend.length() - 1) { //пока тестировать не надо
                current += c;
            } else if (n == template.length() + prepend.length() - 1) { //уже может встречаться подстрока
                template = (new StringBuilder(prepend).reverse().toString()) + template;
                prepend = "";
                templatesPrefixf.add(0, 0);
                for (int i = 1; i < template.length(); ++i) { //префикс функция для образца
                    int j = templatesPrefixf.get(i - 1);
                    while (j > 0 && template.charAt(i) != template.charAt(j)) {
                        j = templatesPrefixf.get(j - 1);
                    }
                    if (template.charAt(i) == template.charAt(j)) {
                        j++;
                    }
                    templatesPrefixf.add(i, j);
                }
                for (int i = 0; i < current.length(); ++i) { //поиск максимальной подстроки среди первых длины образца
                    while (pref > 0 && current.charAt(i) != template.charAt(pref)) {
                        pref = templatesPrefixf.get(pref - 1);
                    }
                    if (current.charAt(i) == template.charAt(pref)) {
                        pref++;
                    }
                }
            }
            if (n >= template.length() + prepend.length() - 1) { // поиск в оставшемся тексте
                while (pref > 0 && c != template.charAt(pref)) {
                    pref = templatesPrefixf.get(pref - 1);
                }
                if (c == template.charAt(pref)) {
                    pref++;
                }
                if (pref == template.length() - 1) {
                    answer.add(new Pair<Integer, Integer>(n - 1, 0));
                }
            }
        }
        //write_ans(answer);
        return answer;
    }

    protected void write(ArrayList<Integer> arrayList) {
        for (Integer i : arrayList) {
            System.out.print(i + " ");
        }
    }

    private void write_ans(ArrayList<Pair<Integer, Integer>> answer) {
        for (Pair<Integer, Integer> anAnswer : answer) {
            System.out.println(anAnswer.getKey() + " " + anAnswer.getValue());
        }
    }

    public void AppendCharToTemplate(char c) {
        if (c < 32 || c > 255) {
            throw new ExceptionInInitializerError();
        }
        if (template == null) {
            System.err.println("no template");
            throw new ExceptionInInitializerError();
        }
        template = template.replace('$', c);
        template = template + '$';
    }

    public void PrependCharToTemplate(char c) {
        if (c < 32 || c > 255) {
            throw new ExceptionInInitializerError();
        }
        if (template == null) {
            System.err.println("no template");
            throw new ExceptionInInitializerError();
        }
        prepend = prepend + c;
    }
}
