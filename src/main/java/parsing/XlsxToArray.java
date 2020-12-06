package parsing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class XlsxToArray {

    private XlsxToArray() {
    }

    public static List<StringBuilder> writeItemNameToArray(String fileName) {
        final List<StringBuilder> arrayParts = new ArrayList<>();
        // Traversing over each row of XLSX file
        for (Row row : WorkWithExcel.open(fileName)) {
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
}
