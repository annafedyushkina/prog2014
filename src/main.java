public class main {
    public static void main(String[] args) {
        TSingleMatcher single = new TSingleMatcher();
        String a = "a";
        single.AddTemplate(a);
        single.PrependCharToTemplate('r');
        single.MatchStream(new CharStream("abracadabra"));
        TNaiveTemplateMatcher naive = new TNaiveTemplateMatcher();
        naive.AddTemplate(a);
        //naive.MatchStream(new CharStream("abracadabra"));
    }
}
