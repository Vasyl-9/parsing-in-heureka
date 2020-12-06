package parsing;

import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class WorkWithExcel {
    private static final String[] columns = {"Item", "Price"};

    private WorkWithExcel() {
    }

    protected static XSSFSheet open(String fileName) {
        File myFile = new File(fileName);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(myFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // Finds the workbook instance for XLSX file
        XSSFWorkbook myWorkBook = null;
        try {
            myWorkBook = new XSSFWorkbook(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myWorkBook.getSheetAt(0);
    }

    protected static void save(List<Item> data, String fileName) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Items");
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }
        for (int i = 1; i < data.size(); i++) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(data.get(i).getTitle());
            row.createCell(1).setCellValue(data.get(i).getPrice());
        }
        FileOutputStream fileOut = new FileOutputStream(fileName);
        workbook.write(fileOut);
        fileOut.close();
    }


}
