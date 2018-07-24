package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dummy.DummyReceipt;
import dummy.HTMLGenerator;
import dummy.Jutility;
import models.PaymentHistory;
import models.Product;
import models.Receipts;
import models.SalesMen;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import util.AppConst;
import views.html.index;
import views.html.listReceipts;
import views.html.testingAugoSuggest;
import views.html.viewReceipt;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/*import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;*/
/*import dummy.DummyProduct;
import dummy.DummyReceipt;*/

public class Application extends Controller {
    //static Form<DummyReceipt> dummyReceipt = Form.form(DummyReceipt.class);
    public Result testAutoSuggest() {
        return ok(testingAugoSuggest.render());

    }

    public Result index() {
        return ok(index.render());
    }

    public Result getProductById() {
        try {
            String pId = request().getQueryString("productId");
            Product prod = Product.findByIdAndQty(Integer.parseInt(pId));
            ObjectNode jsonResp = toJSONProduct(prod);
            return ok(jsonResp);
        } catch (Exception e) {
            Logger.info("EXCEPTION", e);
            ObjectNode jsonResp = Json.newObject();
            jsonResp.put("prodName", "");
            jsonResp.put("prodQty", 0);
            jsonResp.put("prodPrice", 0);
            jsonResp.put("prodCode", "");
            return ok(jsonResp);
        }

    }

    public Result getProductList() {

        //System.out.println(request().getQueryString("productId"));
        try {
            //	String pCode = request().getQueryString("term");

            final Map<String, String[]> data = request().body().asFormUrlEncoded();
            String pCode = data.get("term")[0];
            String exclude[] = data.get("exclude");
            //	exclude
            System.out.println("### TERM:" + pCode);

            //JsonNode json = request().body().asJson();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(exclude[0]);
            List<Map<String, String>> existProdList = new ArrayList<>();
            if (json != null) {
                existProdList = Jutility.processJSON(json);
            }


            List<Product> prodList = Product.suggestProdList(pCode, existProdList);
            //	List<DummyProduct> dumProd = new ArrayList<DummyProduct>();
			
			/*for(Product p : prodList){
				DummyProduct dum = new DummyProduct();
				dum.id = p.id;
				dum.label=p.productName;
				dum.value = ""+p.productPrice;
				dumProd.add(dum);
			}*/

            final StringWriter sw = new StringWriter();
            mapper = new ObjectMapper();
            mapper.writeValue(sw, prodList);
            String toJsonList = sw.toString();//use toString() to convert to JSON
            sw.close();
            return ok(toJsonList);
        } catch (Exception e) {
            Logger.info("EXCEPTION", e);
            return ok("");
        }

    }

    private ObjectNode toJSONProduct(Product prod) {
        //JsonNode json = request().body().asJson();
        ObjectNode result = Json.newObject();
        if (prod == null) {
            result.put("prodName", "");
            result.put("prodQty", 0);
            result.put("prodPrice", 0);
            result.put("prodCode", "");
        } else {
            result.put("prodName", prod.productName);
            result.put("prodQty", prod.productQty);
            result.put("prodPrice", prod.productPrice);
            result.put("prodCode", prod.productCode);
        }
        return result;
    }

    private ObjectNode toJSONSalesMan(SalesMen salesMen) {
        //JsonNode json = request().body().asJson();
        ObjectNode result = Json.newObject();
        if (salesMen == null) {
            result.put(AppConst.salesManId, 0);
            result.put(AppConst.salesManName, "");
            result.put(AppConst.salesManAddress, "");
            result.put(AppConst.salesManTotalDue, 0);
            result.put(AppConst.salesManContact, "");
        } else {
            result.put(AppConst.salesManId, salesMen.id);
            result.put(AppConst.salesManName, salesMen.salesManName);
            result.put(AppConst.salesManAddress, salesMen.salesManAddress);
            result.put(AppConst.salesManTotalDue, salesMen.salesManTotalDue);
            result.put(AppConst.salesManContact, salesMen.salesManContact);
        }
        return result;
    }

    public Result getHTML() {

        JsonNode json = request().body().asJson();
        //System.out.println("JSON REQUEST: "+json);

        if (json == null) {
            return badRequest("Expecting Json data");
        } else {

            List<Map<String, String>> prodList = Jutility.processJSON(json);
            return ok(HTMLGenerator.generateROw(prodList));
        }


    }

    public Result checkProdQty() {
        int prodId = -1;
        int qty = -1;
        ObjectNode result = Json.newObject();
        try {
            prodId = Integer.parseInt(request().getQueryString("prodId"));
            qty = Integer.parseInt(request().getQueryString("prodQty"));
            List<Product> prod = Product.checkProdAndQty(prodId, qty);
            if (prod == null) {
                result.put("isAvailable", false);
                result.put("prodName", "");
                result.put("prodPrice", 0);

            } else if (prod.isEmpty())
                result.put("isAvailable", false);
            else {
                result.put("prodName", prod.get(0).productName);
                result.put("prodPrice", prod.get(0).productPrice);
                result.put("isAvailable", true);
            }
        } catch (Exception e) {
            Logger.info("Exception ", e);
            result.put("prodName", "");
            result.put("prodPrice", 0);
            result.put("isAvailable", false);
        }
        return ok(result);

    }

    public Result submitReceipt() {

        List<Product> prod = new ArrayList<Product>();
        DummyReceipt dumRec = new DummyReceipt();
        dumRec.prodList = prod;
        JsonNode json = request().body().asJson();

        boolean savedRec = false;
        if (json == null) {
            return badRequest("Expecting Json data");
        } else {

            List<Map<String, String>> prodList = Jutility.processJSON(json);
            boolean checkAllProd = Product.checkAllProdQty(prodList);

            if (checkAllProd) {
                savedRec = Receipts.saveReceipts(prodList);
                //   dumRec = genReceipt(prodList);
            }


        }
        String ret = "OK";
        ret = ((savedRec == true) ? "OK" : "NOT OK");

        if (ret.equals("NOT OK"))
            flash("receiptSaved", "Failed to save receipt");
        else
            flash("receiptSaved", "RECEIPT SAVED SUCCESSFULLY");
        //return ok(viewReceipt.render(ret,dumRec));
        return ok(ret);
    }

    private DummyReceipt genReceipt(List<Map<String, String>> prodList) {
        DummyReceipt rec = new DummyReceipt();
        float totPrice = 0.0f;
        List<Product> pordListRec = new ArrayList<Product>();
        for (Map<String, String> prod : prodList) {
            Product p = Product.findById(Integer.parseInt(prod.get(AppConst.productId)));
            int qty = Integer.parseInt(prod.get(AppConst.productQty));
            totPrice = totPrice + (p.productPrice * qty);
            pordListRec.add(p);
        }
        rec.prodList = pordListRec;
        if (!prodList.isEmpty()) {

            rec.receiptId = prodList.get(0).get(AppConst.receiptId);
            rec.receiptDate = new Date();
            String paid = prodList.get(0).get(AppConst.paidAmount).replaceAll("\\$", "");
            if (!paid.contains(".")) {
                paid = paid + ".00";
            }
            Float paidAmount = Float.parseFloat(paid);
            rec.amountPaid = paidAmount;
            SalesMen man = SalesMen.findById(Integer.parseInt(prodList.get(0).get(AppConst.salesManId)));
            rec.customerName = man.salesManName;
            rec.customerAddress = man.salesManAddress;
            rec.customerContact = man.salesManContact;
            rec.prevDue = man.salesManTotalDue;

            Float due = totPrice + man.salesManTotalDue;

            due = due - paidAmount;

            rec.dueNow = due;
            man.salesManTotalDue = due;
            //man.update();
            SalesMen.update(man);

        }
        return rec;
    }

    public Result listReceipt() {
        List<PaymentHistory> listPayHist = PaymentHistory.all();

        return ok(listReceipts.render("List Of Receipts", listPayHist));
    }

    public Result createReceipt(String rec) {


        String req[] = rec.split("\\|");
        String receiptId = req[0];
        String custId = req[1];
        System.out.println("##### Receipt: " + receiptId + " Customer: " + custId);

        List<Receipts> receiptList = Receipts.findByReceiptId(receiptId);
        PaymentHistory payHist = PaymentHistory.findbyCustAndReceipt(receiptId, custId);
        DummyReceipt dumRec = new DummyReceipt();
        dumRec = genReceipt(receiptList, payHist);
		/*List<Product> productList = new ArrayList<Product>();
		
		rec.prodList = productList;*/
        return ok(viewReceipt.render("Print Receipt", dumRec));
        //	return TODO;
    }

    private DummyReceipt genReceipt(List<Receipts> receiptList, PaymentHistory payHist) {

        DummyReceipt rec = new DummyReceipt();
        float totPrice = 0.0f;
        List<Product> prodList = new ArrayList<Product>();
        for (Receipts r : receiptList) {
            Product p = r.productId;
            float qty = r.productqty;
            Product pr = new Product();
            pr.id = p.id;
            pr.productCode = p.productCode;
            pr.productName = p.productName;
            pr.productPrice = p.productPrice;
            pr.productQty = qty;
            totPrice = totPrice + (p.productPrice * qty);
            prodList.add(pr);
        }
        rec.prodList = prodList;

        if (!prodList.isEmpty()) {

            rec.receiptId = payHist.receiptId;
            rec.receiptDate = payHist.salesDate;

            rec.amountPaid = payHist.paid;
            SalesMen man = SalesMen.findById(payHist.salesMan);
            rec.customerName = man.salesManName;
            rec.customerAddress = man.salesManAddress;
            rec.customerContact = man.salesManContact;
            rec.prevDue = payHist.prevDue;
            rec.totalPrice = totPrice;

            rec.dueNow = payHist.currentDue;


        }
        return rec;
    }

    public Result getCustomerbyId() {
        try {
            String id = request().getQueryString("salesMenId");

            SalesMen man = SalesMen.findById(Integer.parseInt(id));
            ObjectNode jsonResp = toJSONSalesMan(man);
            return ok(jsonResp);
        } catch (Exception e) {
            Logger.info("EXCEPTION", e);
            ObjectNode result = Json.newObject();
            result.put(AppConst.salesManId, 0);
            result.put(AppConst.salesManName, "");
            result.put(AppConst.salesManAddress, "");
            result.put(AppConst.salesManTotalDue, 0);
            result.put(AppConst.salesManContact, "");
            return ok(result);
        }
    }

}
