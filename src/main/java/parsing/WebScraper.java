package parsing;

import java.io.IOException;
import java.util.List;

public class WebScraper {
    public static void main(String[] args) throws IOException {
        long start = System.nanoTime();
        List<StringBuilder> searchArray = XlsxToArray.writeItemNameToArray("input.xlsx");
        ParsingHtml parsingHtml = new ParsingHtml(searchArray);
        List<Item> item = parsingHtml.parsePrices();
        WorkWithExcel.save(item, "output.xlsx");
        long duration = (System.nanoTime() - start) / 1_000_000_000;
        System.out.println("The parsing was successfully completed and saved to file \"output.xlsx\" for " + duration +
                " sec");
    }
}
