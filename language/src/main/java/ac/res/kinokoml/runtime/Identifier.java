package ac.res.kinokoml.runtime;

public class Identifier {
    private final String name;
    private static int i;

    public static final Identifier OUTER = new Identifier("$outer");

    public Identifier() {
        name = String.format("$%d", i++);
    }

    public Identifier(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("${%s}", name);
    }
}