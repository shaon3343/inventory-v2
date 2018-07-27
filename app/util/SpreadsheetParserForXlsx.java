package util;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

public class SpreadsheetParserForXlsx implements SpreadsheetParser {
    private static boolean isEmptyRow(XSSFRow row) {

        boolean isEmptyRow = true;
        if (row == null) {
            return true;
        }
        if (row.getLastCellNum() <= 0) {
            return true;
        }

        for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
            XSSFCell cell = row.getCell(cellNum);
            if (cell != null && cell.getCellType() != XSSFCell.CELL_TYPE_BLANK && StringUtils.isNotBlank(cell.toString())) {
                isEmptyRow = false;
            }
        }
        return isEmptyRow;
    }

    @Override
    public ArrayList<ArrayList<String>> parseSpreadsheet(File fileXlsx) {
        ArrayList<ArrayList<String>> contents = new ArrayList<>();

        try {
            InputStream ExcelFileToRead = new FileInputStream(fileXlsx);
            XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);
            XSSFSheet sheet1 = wb.getSheetAt(0);
            for (int i = 1; i <= sheet1.getLastRowNum(); i++) {
                XSSFRow row = sheet1.getRow(i);
                if (isEmptyRow(row)) {
                    continue;
                }
                contents.add(extractRowContents(row));
            }
            return contents;
        } catch (Exception e) {
            e.printStackTrace();
            return contents;
        }

    }

    private ArrayList<String> extractRowContents(XSSFRow row) {
        DataFormatter formatter = new DataFormatter(Locale.getDefault());
        ArrayList<String> saveRow = new ArrayList<>();
        for (int j = 0; j < row.getLastCellNum(); j++) {
            XSSFCell cell = row.getCell(j);
            if (cell != null) {
                switch (cell.getCellType()) {
                    case XSSFCell.CELL_TYPE_STRING:
                        saveRow.add(cell.getRichStringCellValue().getString().trim());
                        break;
                    case XSSFCell.CELL_TYPE_NUMERIC:
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
        return saveRow;
    }
}
