package hello;

public class Recommendation {

    private final long id;
    private final String content;

    public Recommendation(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}