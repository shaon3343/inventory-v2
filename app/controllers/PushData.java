package controllers;

import models.Product;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import util.AppConst;
import util.ParseSpreadsheet;
import views.html.setInventory.uploadproduct;

import java.io.File;
import java.util.ArrayList;

public class PushData extends Controller {

    public Result uploadPage() {

        return ok(uploadproduct.render());

    }

    public Result xlsupload() {
        MultipartFormData body = request().body().asMultipartFormData();
        FilePart uploadedFile = body.getFile("uploaded_file");

        //if (uploaded_file == null || !uploaded_file.getContentType().equals("application/vnd.ms-excel")) {
        String fileName = "";

        if (uploadedFile == null) {
            flash("FLASH_ERROR_UPLOAD", "Sorry, there was some problem. Please try again after some time");
            return redirect(routes.PushData.uploadPage());

        } else {
            String filename = uploadedFile.getFilename();
            String extension = filename.substring(filename.lastIndexOf(".") + 1, filename.length());

            String excel = "xlsx";
            if (!extension.equals(excel)) {
                flash("FLASH_ERROR_UPLOAD", "Sorry, please upload .xlsx file");
                return redirect(routes.PushData.uploadPage());
            }

            File fileXls = (File) uploadedFile.getFile();
            String fileNameXls = uploadedFile.getFilename();
            String path = AppConst.EXCEL_UPLOAD_DIR + fileNameXls;

            ArrayList<ArrayList<String>> spContents = new ParseSpreadsheet().getDataFromSpreadsheet(fileXls);

            System.out.println("PARSED EXCEL Total Content: " + spContents.size());
            boolean isParsed = false;
            if (!spContents.isEmpty())
                isParsed = true;
            try {
                for (ArrayList<String> content : spContents) {
                    Product p = new Product();

                    p.productName = content.get(0);
                    p.productCode = content.get(1);
                    p.productPrice = Float.parseFloat(content.get(2));
                    p.productQty = Float.parseFloat(content.get(3));
                    Product pr = Product.getProduct(p);
                    if (pr != null) {
                        p.id = pr.id;
                        Product.update(p);
                    } else {
                        Product.create(p);
                    }
                }
            } catch (Exception e) {
                isParsed = false;
            }


            if (isParsed) {
                flash("FLASH_SUCCESS_UPLOAD", "Uploaded and saved to Database");
                return redirect(routes.PushData.uploadPage());
            } else {
                flash("FLASH_ERROR_UPLOAD", "EXCEL Format is not right");
                return redirect(routes.PushData.uploadPage());
            }
        }

    }
}
