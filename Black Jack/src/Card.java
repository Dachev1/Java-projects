public class Card {
    private String value;
    private String type;

    public Card(String value, String type) {
        this.value = value;
        this.type = type;
    }

    @Override
    public String toString() {
        // Adding dash, because images have the same name
        return value + "-" + type;
    }

    public int getValue() {
        if ("AJKQ".contains(this.value)) {
            if ("A".equals(this.value)) {
                return 11;
            }

            return 10;
        }
        // 2 - 10;
        return Integer.parseInt(this.value);
    }

    public boolean isAce() {
        return "A".equals(this.value);
    }

    public String getImagePath() {
        return "./cards/" + toString() + ".png";
    }
}
