import java.util.StringTokenizer;
public class Card {
    public static enum Color {
        YELLOW, GREEN, BLUE, RED, __
    }

    public Color color;
    Card(Color color) {
        this.color = color;
    }
    public static class NumberedCard extends Card {
        NumberedCard(Color color, int number) {
            super(color);
            // this.color = color;
            this.number = number;
        }
        public int number;
    }

    public static enum SpecialCardFunction {
        PLUSTWO, SKIP, SUBWAY, PLUSFOUR, CHANGECOLOR
    }

    public static class SpecialCard extends Card {
        public SpecialCardFunction function;

        SpecialCard(Color color, SpecialCardFunction func) {
            super(color);
            // this.color = color;
            this.function = func;
        }
    }

    public String toString() {
        return "card-"
                + color.toString() + "-"
                + (this instanceof NumberedCard
                ? "num-" + Integer.toString(((NumberedCard)this).number)
                : "fun-" + ((SpecialCard)this).function.toString()
        )
                ;
    }
    public static Card stringToCard(String arg) {

        // String str = "card-GREEN-num-0";
        // String str = "card-BLUE-num-0";
        // String str = "card-__-fun-";

        StringTokenizer tokens = new StringTokenizer(arg, "-");
        String command = tokens.nextToken();
        // command == color
        String color = tokens.nextToken();
        String type = tokens.nextToken();
        int num = -1;
        String func = "__";
        switch (type) {
            case "num":
                num = Integer.parseInt(tokens.nextToken());
                return new Card.NumberedCard(Card.Color.valueOf(color), num);
            // break;
            //case "act":
            case "fun":
                func = tokens.nextToken();
                return new Card.SpecialCard(Card.Color.valueOf(color), Card.SpecialCardFunction.valueOf(func));
            // break;
            default:
                return null;
        }
    }

}