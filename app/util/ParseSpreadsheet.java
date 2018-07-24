package util;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.Locale;

public class ParseSpreadsheet {
    public static boolean isEmptyRow(XSSFRow row) {

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

    public ArrayList<ArrayList<String>> getDataFromSpreadsheet(File fileXls) {


        ArrayList<ArrayList<String>> contents = new ArrayList<ArrayList<String>>();

        try {

            //FileInputStream fileInput;
            //fileInput = new FileInputStream(path);
            //BufferedInputStream bufferInput = new BufferedInputStream(fileInput);
            //POIFSFileSystem poiFileSystem;
            //poiFileSystem = new POIFSFileSystem(bufferInput);
            //Biff8EncryptionKey.setCurrentUserPassword(password);
            //HSSFWorkbook wb = new HSSFWorkbook(poiFileSystem, true);

            InputStream ExcelFileToRead = new FileInputStream(fileXls);
            XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);


            XSSFSheet sheet1 = wb.getSheetAt(0);

            DataFormatter formatter = new DataFormatter(Locale.getDefault());


            String flag = "no error";
            for (int i = 1; i <= sheet1.getLastRowNum(); i++) {

                ArrayList<String> saveRow = new ArrayList<String>();

                //System.out.println("-----loop-------");
                XSSFRow row = sheet1.getRow(i);
                if (isEmptyRow(row)) {
                    continue;
                }
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    //System.out.println("-----loop2-------");
                    XSSFCell cell = null;
                    if (row != null) {
                        cell = row.getCell(j);
                    } else {
                        break;
                    }

                    if (cell != null) {

                        //System.out.println("cell vall.."+i+"   "+cell.getRichStringCellValue().getString().trim());
                        //System.out.println("cell vall..");

                        switch (cell.getCellType()) {
                            case XSSFCell.CELL_TYPE_STRING:
                                saveRow.add(cell.getRichStringCellValue().getString().trim());
                                break;
                            case XSSFCell.CELL_TYPE_NUMERIC:
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    /*System.out.println("  "+formatter.formatCellValue(cell));*/
                                    saveRow.add(formatter.formatCellValue(cell).trim());
                                } else {
                                    //System.out.print("  "+String.valueOf((long)cell.getNumericCellValue()));
                                    saveRow.add(String.valueOf((long) cell.getNumericCellValue()));
                                    //System.out.println(cell.getNumericCellValue());
                                }
                                break;

                            default:
                                //System.out.println("DEFAULT");
                        }


                        //	System.out.println(">>>>>>>>>>>>>>"+cell.getRichStringCellValue());
                        //	cell.getRichStringCellValue().getString().trim();

                    }
                }
                contents.add(saveRow);
                //System.out.println("###### ROW SEPARATOR ###########");

            }


            return contents;
        } catch (FileNotFoundException e) {
            e.printStackTrace();

            return contents;

        } catch (IOException e) {
            e.printStackTrace();

            return contents;

        }

    }
}
