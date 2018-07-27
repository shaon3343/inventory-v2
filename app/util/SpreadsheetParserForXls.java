package util;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class SpreadsheetParserForXls implements SpreadsheetParser {
    @Override
    public ArrayList<ArrayList<String>> parseSpreadsheet(File spreadsheet) throws IOException {
        ArrayList<ArrayList<String>> contents = new ArrayList<>();
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(spreadsheet));
        DataFormatter formatter = new DataFormatter(Locale.getDefault());
        // Read the Sheet
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            // Read the Row
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                ArrayList<String> saveRow = new ArrayList<>();
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                if (isEmptyRow(hssfRow)) {
                    continue;
                }

                for (int j = 0; j < hssfRow.getLastCellNum(); j++) {
                    HSSFCell cell = hssfRow.getCell(j);
                    if (cell != null) {
                        switch (cell.getCellType()) {
                            case HSSFCell.CELL_TYPE_STRING:
                                saveRow.add(cell.getRichStringCellValue().getString().trim());
                                break;
                            case HSSFCell.CELL_TYPE_NUMERIC:
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    saveRow.add(formatter.formatCellValue(cell).trim());
                                } else {
                                    saveRow.add(String.valueOf((long) cell.getNumericCellValue()));
                                }
                                break;
                            default:
                                //System.out.println("DEFAULT");
                        }
                    }
                }
                contents.add(saveRow);
            }
        }
        return contents;
    }

    private static boolean isEmptyRow(HSSFRow row) {

        boolean isEmptyRow = true;
        if (row == null) {
            return true;
        }
        if (row.getLastCellNum() <= 0) {
            return true;
        }

        for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
            HSSFCell cell = row.getCell(cellNum);
            if (cell != null && cell.getCellType() != HSSFCell.CELL_TYPE_BLANK && StringUtils.isNotBlank(cell.toString())) {
                isEmptyRow = false;
            }
        }
        return isEmptyRow;
    }
}
