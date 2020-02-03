package parsing;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

public class ParsingHtml {
    private static String[] columns = {"Item", "Price"};
    private List<StringBuilder> searchArray;

    public ParsingHtml(List<StringBuilder> searchArray1) {
        this.searchArray = searchArray1;
    }

    public void parsePrices() throws IOException {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Items");
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }
        int rowNum = 1;
        for (int i = 0; i < searchArray.size(); i++) {

            StringBuilder searchQuery = searchArray.get(i);
            String baseUrl = "https://heureka.sk";
            WebClient client = new WebClient();
            client.getOptions().setCssEnabled(false);
            client.getOptions().setJavaScriptEnabled(false);
            Row row = sheet.createRow(rowNum++);
            try {

                String searchUrl = baseUrl + "/?h%5Bfraze%5D=" + URLEncoder.encode(String.valueOf(searchQuery), "UTF-8") + "&min=&max=&gty=new&o=3";
                HtmlPage page = client.getPage(searchUrl);
                List<HtmlElement> items = (List<HtmlElement>) page.getByXPath("//div[@class='product']");

                if (items.isEmpty()) {
                    row.createCell(1).setCellValue("0");
                    row.createCell(0).setCellValue(String.valueOf(searchArray.get(i)));
                } else {
                    for (HtmlElement htmlItem : items) {

                        HtmlAnchor itemAnchor = (htmlItem.getFirstByXPath(".//div[@class='desc']/div/h2/a"));
                        if (itemAnchor == null) {
                            row.createCell(1).setCellValue("0");
                            row.createCell(0).setCellValue(String.valueOf(searchArray.get(i)));
                            break;
                        }
                        HtmlElement spanPrice = (htmlItem.getFirstByXPath(".//div/p[@class='price']/a"));
                        if (spanPrice == null) {
                            row.createCell(1).setCellValue("0");
                            row.createCell(0).setCellValue(String.valueOf(searchArray.get(i)));
                            break;
                        }
                        // It is possible that an item doesn't have any price, we set the price to 0.0 in this case
                        String itemPrice = (spanPrice.asText());

                        Item item = new Item();
                        item.setTitle(itemAnchor.asText());
                        itemPrice = itemPrice.replace(",", ".");
                        itemPrice = itemPrice.replace("€", "");
                        item.setPrice(Double.parseDouble(itemPrice));

                        row.createCell(1).setCellValue(item.getPrice());
                        row.createCell(0).setCellValue(item.getTitle());
                        break;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                client.close();
            }
        }
        FileOutputStream fileOut = new FileOutputStream("asd.xlsx");
        workbook.write(fileOut);
        fileOut.close();
    }
}
