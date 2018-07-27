package controllers;

import models.Product;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import util.AppConst;
import util.SpreadsheetParser;
import util.SpreadsheetParserForXls;
import util.SpreadsheetParserForXlsx;
import views.html.setInventory.uploadproduct;

import javax.inject.Inject;
import javax.transaction.NotSupportedException;
import java.io.File;
import java.util.ArrayList;

public class UploadData extends Controller {

    @Inject
    private Product product;

    public Result uploadPage() {

        return ok(uploadproduct.render());

    }

    public Result fileUpload() {
        MultipartFormData body = request().body().asMultipartFormData();
        FilePart uploadedFile = body.getFile("uploaded_file");
        if (!isSpreadsheet(uploadedFile)) {
            return redirect(routes.UploadData.uploadPage());
        } else {
            boolean isParsed = false;
            try {
                String filename = uploadedFile.getFilename();
                String extension = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
                File fileXls = (File) uploadedFile.getFile();
                SpreadsheetParser spreadsheetParser = getSpreadsheetParser(extension);
                ArrayList<ArrayList<String>> sheetContents = spreadsheetParser.parseSpreadsheet(fileXls);
                System.out.println("PARSED EXCEL Total Content: " + sheetContents.size());
                if (!sheetContents.isEmpty())
                    isParsed = true;
                for (ArrayList<String> content : sheetContents) {
                    product.insertProduct(content);
                }
            } catch (Exception e) {
                e.printStackTrace();
                isParsed = false;
            }
            if (isParsed) {
                flash("FLASH_SUCCESS_UPLOAD", "Uploaded and saved to Database");
            } else {
                flash("FLASH_ERROR_UPLOAD", "EXCEL Format is not right");
            }
            return redirect(routes.UploadData.uploadPage());
        }
    }

    private boolean isSpreadsheet(FilePart uploadedFile) {
        if (uploadedFile == null) {
            flash("FLASH_ERROR_UPLOAD", "there was some problem. Please try again after some time");
            return false;
        }
        String filename = uploadedFile.getFilename();
        String extension = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
        if (!extension.equals(AppConst.XLSX) && !extension.equals(AppConst.XLS)) {
            flash("FLASH_ERROR_UPLOAD", "please upload a valid spreadsheet/excel file");
            return false;
        }
        return true;
    }

    private SpreadsheetParser getSpreadsheetParser(String extension) throws NotSupportedException {
        if (extension.equals(AppConst.XLSX))
            return new SpreadsheetParserForXlsx();
        else if (extension.equals(AppConst.XLS)) {
            return new SpreadsheetParserForXls();
        }
        throw new NotSupportedException();
    }
}
