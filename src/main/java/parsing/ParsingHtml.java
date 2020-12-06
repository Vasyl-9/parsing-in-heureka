package parsing;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class ParsingHtml {
    private final List<StringBuilder> searchArray;

    public ParsingHtml(List<StringBuilder> searchArray) {
        this.searchArray = searchArray;
    }

    public List<Item> parsePrices() {
        List<CompletableFuture<Item>> priceFuture = searchArray.stream()
                .map(searchField -> CompletableFuture.supplyAsync(() -> parseHTML(searchField, new WebClient()),
                        executor(searchArray.size())))
                .collect(Collectors.toList());
        return priceFuture.stream().map(CompletableFuture::join).collect(Collectors.toList());
    }

    private Executor executor(int size) {
        return Executors.newFixedThreadPool(Math.min(size, 100), (Runnable r) -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
    }

    private Item parseHTML(StringBuilder searchQuery, WebClient client) {
        // Webclient setting
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        String baseUrl = "https://heureka.sk";
        String searchUrl = null;
        try {
            searchUrl = baseUrl + "/?h%5Bfraze%5D=" + URLEncoder.encode(String.valueOf(searchQuery),
                    "UTF-8") + "&min=&max=&gty=new&o=3";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HtmlPage page = null;
        try {
            page = client.getPage(searchUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<HtmlElement> items = Objects.requireNonNull(page).getByXPath("//div[@class='product']");
        if (items.isEmpty()) {
            return new Item(String.valueOf(searchQuery), 0);
        } else {
            HtmlAnchor itemAnchor = null;
            String itemPrice = "";
            for (HtmlElement htmlItem : items) {
                itemAnchor = htmlItem.getFirstByXPath(".//div[@class='desc']/div/h2/a");
                if (itemAnchor == null) {
                    return new Item(String.valueOf(searchQuery), 0);
                }
                HtmlElement spanPrice = (htmlItem.getFirstByXPath(".//div/p[@class='price']/a"));
                if (spanPrice == null) {
                    return new Item(itemAnchor.asText(), 0);
                } else {
                    itemPrice = (spanPrice.asText());
                }
            }
            // It is possible that an item doesn't have any price, we set the price to 0.0 in this case
            itemPrice = itemPrice.replace(",", ".");
            itemPrice = itemPrice.replace("€", "");
            return new Item(itemAnchor.asText(), Double.parseDouble(itemPrice));
        }
    }
}

