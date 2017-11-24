import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tester {
    final String regex = "([\\s\\S]+)return([\\s\\S]+)";
    final String string = " x:=1; return x";

    final Pattern pattern = Pattern.compile(regex);
    final Matcher matcher = pattern.matcher(string);
    public static void main(String[] args) {
        Tester t = new Tester();
        t.doStuff();
    }
    public void doStuff()
    {
        while (matcher.find()) {
            System.out.println("Full match: " + matcher.group(0));
            for (int i = 1; i <= matcher.groupCount(); i++) {
                System.out.println("Group " + i + ": " + matcher.group(i));
            }
        }
    }
}
