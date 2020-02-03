package parsing;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XlsxToArray {

    private List<StringBuilder> ArrayParts = new ArrayList<StringBuilder>();

    public List<StringBuilder> getArrayParts() {
        return ArrayParts;
    }

    public void setArrayParts(StringBuilder arrayParts) {
        ArrayParts.add(arrayParts);
    }

    public XlsxToArray() throws IOException {
        writeItemNameToArray();
    }

    public void writeItemNameToArray() {
        OpenXlsx openXlsx = new OpenXlsx();

        Iterator<Row> rowIterator = openXlsx.openXlsx().iterator();

        // Traversing over each row of XLSX file
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            StringBuilder a = new StringBuilder();
            // For each row, iterate through each columns
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {

                Cell cell = cellIterator.next();

                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                        a.append((cell.getStringCellValue() + ("\t")));
                        break;
                }
            }
            setArrayParts(a);
        }
    }
}
