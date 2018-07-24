package dummy;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import util.AppConst;

import java.text.SimpleDateFormat;
import java.util.*;

/*import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;*/

public class Jutility {

    public static String formatDate(Date date) {
        return new SimpleDateFormat("MMMM dd, yyyy").format(date);

    }

    public static List<Map<String, String>> processJSON(JsonNode json) {
        ArrayNode arr = (ArrayNode) json;
        Iterator<JsonNode> it = arr.iterator();

        List<Map<String, String>> retProdList = new ArrayList<>();
        while (it.hasNext()) {
            JsonNode obj = it.next();
            Map<String, String> productMap = new LinkedHashMap<String, String>();
            //   System.out.println("language: " + obj.findPath("language").getTextValue());

            String prodId = obj.findPath("prodId").textValue();
            String prodCost = obj.findPath("prodCost").textValue();
            String prodQty = obj.findPath("prodQty").textValue();
            String receiptId = obj.findPath("receiptId").textValue();
            String paidAmount = obj.findPath("paid").textValue();
            String custId = obj.findPath("salesManId").textValue();

            productMap.put(AppConst.productId, prodId);
            productMap.put(AppConst.productCost, prodCost);
            productMap.put(AppConst.productQty, prodQty);
            productMap.put(AppConst.receiptId, receiptId);
            productMap.put(AppConst.paidAmount, paidAmount);
            productMap.put(AppConst.salesManId, custId);
	        
    	   /* System.out.println("### PROD ID: "+obj.findPath("prodId").getTextValue()+
    	    		" ###PROD COST "+obj.findPath("prodCost").getTextValue()+
    	    		" ##Prod Quantity: "+obj.findPath("prodQty").getTextValue());*/
            retProdList.add(productMap);
        }
        return retProdList;
    }

    public static String getNowDate() {
        String nowDate = new SimpleDateFormat(AppConst.DATE_TIME_FORMAT_MIL).format(new Date());
        return nowDate;
    }
}
