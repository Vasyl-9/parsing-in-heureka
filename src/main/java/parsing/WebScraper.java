package parsing;

import java.io.IOException;
import java.util.List;

public class WebScraper {
    public static void main(final String[] args) throws IOException {
        long start = System.nanoTime();
        List<StringBuilder> searchArray = ExcelUtils.writeItemNameToArray("input.xlsx");
        ParsingHtml parsingHtml = new ParsingHtml(searchArray);
        List<Item> item = parsingHtml.parsePrices();
        ExcelUtils.save(item, "output.xlsx");
        long duration = (System.nanoTime() - start) / 1_000_000_000L;
        System.out.println("The parsing was successfully completed and saved to file \"output.xlsx\" for " + duration +
                " sec");
    }
}
