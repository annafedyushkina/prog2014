import javafx.util.Pair;
import java.util.ArrayList;

public interface IMetaTemplateMatcher {
    public int AddTemplate(String template);
    public ArrayList<Pair<Integer, Integer>> MatchStream(ICharStream stream);
}
