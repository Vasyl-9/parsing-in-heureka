package parsing;

class Item {
    private final String title;
    private final double price;

    Item(final String title, final double price) {
        this.title = title;
        this.price = price;
    }

    String getTitle() {
        return title;
    }

    double getPrice() {
        return price;
    }
}
