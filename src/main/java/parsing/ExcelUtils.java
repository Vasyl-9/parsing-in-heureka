package parsing;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class ExcelUtils {
    private static final String[] columns = {"Item", "Price"};

    private ExcelUtils() {
    }

    static XSSFSheet open(String fileName) {
        File myFile = new File(fileName);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(myFile);
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        }
        // Finds the workbook instance for XLSX file
        XSSFWorkbook myWorkBook = null;
        try {
            myWorkBook = new XSSFWorkbook(fis);
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return myWorkBook.getSheetAt(0);
    }

    static List<StringBuilder> writeItemNameToArray(final String fileName) {
        List<StringBuilder> arrayParts = new ArrayList<>();
        // Traversing over each row of XLSX file
        for (Row row : open(fileName)) {
            StringBuilder a = new StringBuilder();
            // For each row, iterate through each columns
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    a.append(cell.getStringCellValue()).append("\t");
                }
            }
            arrayParts.add(a);
        }
        return arrayParts;
    }

    static void save(final List<? extends Item> data, final String fileName) throws IOException {
        final Workbook workbook = new XSSFWorkbook();
        final Sheet sheet = workbook.createSheet("Items");
        final Row headerRow = sheet.createRow(0);
        for (int i = 0; i < columns.length; i++) {
            final Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }
        for (int i = 1; i < data.size(); i++) {
            final Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(data.get(i).getTitle());
            row.createCell(1).setCellValue(data.get(i).getPrice());
        }
        final FileOutputStream fileOut = new FileOutputStream(fileName);
        workbook.write(fileOut);
        fileOut.close();
    }


}
