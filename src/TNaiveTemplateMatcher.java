import javafx.util.Pair;
import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.ArrayList;

public class TNaiveTemplateMatcher implements IMetaTemplateMatcher {
    ArrayList<String> templates = new ArrayList<String>();

    @Override
    public int AddTemplate(String template) {
        if (template == null || template.isEmpty()) {
            throw new NullPointerException();
        }
        if (templates.contains(template)) {
            throw new KeyAlreadyExistsException();
        } else {
            templates.add(template);
        }
        return template.hashCode();
    }

    @Override
    public ArrayList<Pair<Integer, Integer>> MatchStream(ICharStream stream) {
        ArrayList<Pair<Integer, Integer>> answer = new ArrayList<Pair<Integer, Integer>>();
        if (templates.size() != 0) {
            StringBuilder[] templatesBuilder = new StringBuilder[templates.size()];
            for(int i = 0; i < templatesBuilder.length; ++i) {
                templatesBuilder[i] = new StringBuilder();
            }
            int read = 0;
            while (!stream.IsEmpty()) {
                char nextCharFromStream = stream.GetChar();
                ++read;
                for (int i = 0; i < templatesBuilder.length; ++i) {
                    if (templatesBuilder[i].length() == templates.get(i).length()) {
                        templatesBuilder[i].deleteCharAt(0);
                    }
                    templatesBuilder[i].append(nextCharFromStream);
                    String check = templatesBuilder[i].toString();
                    if (templates.get(i).equals(check)) {
                        answer.add(new Pair<Integer, Integer>(read - 1, i));
                    }
                }
            }
        }
        //write(answer);
        return answer;
    }

    private void write(ArrayList<Pair<Integer, Integer>> answer) {
        for (Pair<Integer, Integer> anAnswer : answer) {
            System.out.println(anAnswer.getKey() + " " + anAnswer.getValue());
        }
    }
}