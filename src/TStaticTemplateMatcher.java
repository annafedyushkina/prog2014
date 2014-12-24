import javax.management.openmbean.KeyAlreadyExistsException;
import javafx.util.Pair;
import java.util.ArrayList;

public class TStaticTemplateMatcher implements IMetaTemplateMatcher {
    private int numberOfTemplates = 0;
    ArrayList<String> templates = new ArrayList<String>();
    ArrayList<vertex> dka = new ArrayList<vertex>();
    int sizeOfDKA;
    private ArrayList<Pair<Integer, Integer>> answer = new ArrayList<Pair<Integer, Integer>>();

    public TStaticTemplateMatcher() {
        dka.add(new vertex(-1));
        sizeOfDKA = 1;
    }

    private class vertex {
        ArrayList<Integer> next = new ArrayList<Integer>();
        ArrayList<Integer> go = new ArrayList<Integer>();
        int numberOfTemplate = -1;
        boolean leaf;
        int suffLink = -1;
        int suffUpLink = -1;
        int parent;
        char parenChar;

        public vertex(Integer parentTmp) {
            parent = parentTmp;
            for (int i = 0; i < 255; ++i) {
                go.add(i, -1);
            }
            for (int i = 0; i < 255; ++i) {
                next.add(i, -1);
            }
        }
    }

    @Override
    public int AddTemplate(String template) {
        if (templates.contains(template)) {
            throw new KeyAlreadyExistsException();
        }
        templates.add(template);
        if (template == null || template.isEmpty()) {
            throw new NullPointerException();
        }
        int vertex = 0;
        for (int i = 0; i < template.length(); ++i) {
            char c = template.charAt(i);
            if (dka.get(vertex).next.get(c) == -1) {
                dka.add(new vertex(vertex));
                dka.get(sizeOfDKA).parenChar = c;
                dka.get(vertex).next.set(c, sizeOfDKA++);
            }
            vertex = dka.get(vertex).next.get(c);
        }
        dka.get(vertex).leaf = true;
        dka.get(vertex).numberOfTemplate = numberOfTemplates;
        ++numberOfTemplates;
        return template.hashCode();
    }

    @Override
    public ArrayList<Pair<Integer, Integer>> MatchStream(ICharStream stream) {
        answer.clear();
        numberOfLetter = 0;
        for (int i = 1; i < sizeOfDKA; ++i) {
            getSuffLink(i);
        }
        for (int i = 1; i < sizeOfDKA; ++i) {
            if (dka.get(i).suffUpLink == -1) {
                dka.get(i).suffUpLink = getUpLink(dka.get(i).suffLink);
            }
        }
        int vertex = 0;
        while (!stream.IsEmpty()) {
            vertex = doSomeMagic(stream.GetChar(), vertex);
            ++numberOfLetter;
        }
        return answer;
    }

    private int getSuffLink(int v) { //вычисление суффиксной ссылки, есди ее еще нет
        if (dka.get(v).suffLink == -1)
            if (v == 0 || dka.get(v).parent == 0)
                dka.get(v).suffLink = 0;
            else {
                dka.get(v).suffLink = getGo(getSuffLink(dka.get(v).parent), dka.get(v).parenChar);
            }
        return dka.get(v).suffLink;
    }

    private int getGo(int v, char c) { //вычисление перехода
        if (dka.get(v).go.get(c) == -1)
            if (dka.get(v).next.get(c) != -1)
                dka.get(v).go.set(c, dka.get(v).next.get(c));
            else if (v == 0) {
                dka.get(v).go.set(c, 0);
            } else {
                dka.get(v).go.set(c, getGo(getSuffLink(v), c));
            }
        return dka.get(v).go.get(c);
    }

    private int getUpLink(int v) { //вычисление сжатой ссылки
        if (dka.get(v).leaf) {
            return v;
        }
        if (v == 0 || dka.get(v).parent == 0) {
            return 0;
        }
        return getUpLink(dka.get(v).suffLink);
    }

    private int numberOfLetter;

    protected int doSomeMagic(char letter, int vertex) { //переход из нашей вершины по букве, которую нам дали
        vertex = getGo(vertex, letter);
        if (vertex != -1) {
            int check = vertex;
            while (check != 0 && check != -1) {
                if (dka.get(check).leaf) {
                    answer.add(new Pair<Integer, Integer>(numberOfLetter, dka.get(check).numberOfTemplate));
                }
                check = dka.get(check).suffUpLink;
            }
        } else {
            vertex = 0;
        }
        return vertex;
    }
}

