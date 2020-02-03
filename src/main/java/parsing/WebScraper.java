package parsing;

import java.io.IOException;
import java.util.List;

public class WebScraper {

    public static void main(String[] args) throws IOException {
        XlsxToArray xlsxToArray = new XlsxToArray();
        List<StringBuilder> searchArray = xlsxToArray.getArrayParts();
        ParsingHtml parsingHtml = new ParsingHtml(searchArray);
        parsingHtml.parsePrices();
    }
}
