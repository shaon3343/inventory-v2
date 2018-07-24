package dummy;

import util.AppConst;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HTMLGenerator {

    public static String generateROw(List<Map<String, String>> prodList) {
        List<Integer> prodIdList = new ArrayList<Integer>();
        for (Map<String, String> mp : prodList) {
            prodIdList.add(Integer.parseInt(mp.get(AppConst.productId)));
        }

        String rowWithSelectTag = "<tr class=\"item-row\">"
                + "<td class=\"item-name\"><div class=\"delete-wpr\">"
                + "<input id=\"prodSuggest\" class=\"prodSuggest\" >"
                + "<input type=\"hidden\" id=\"prodSel\"/>"
			  
		      /*  +"<select id=\"prod\" name=\"product.id\" onchange=\"setPriceAndName(this)\">"
			  +"<option class=\"blank\" value=\"\">-- Choose a Product --</option>"
		       for(Map.Entry<String,String> mp:Product.getProductsWithoutThese(prodIdList).entrySet()){
		    	  rowWithSelectTag = rowWithSelectTag		    	
		    			  +"<option value="+mp.getKey()+">"+mp.getValue()+"</option>";
		      }
		      rowWithSelectTag = rowWithSelectTag+"</select>" */
                + "<a class=\"delete\" title=\"Remove row\">X</a></div></td>"
                + "<td class=\"description\"><textarea id=\"description\"> </textarea></td>"
                + "<td><textarea class=\"cost\" id=\"cost\">$0</textarea></td>"
                + "<td><textarea class=\"qty\" id=\"qty\" onblur=\"checkQty(this)\">1</textarea></td>"
                + "<td><span class=\"price\">$0</span></td>"
                + "</tr>";

        return rowWithSelectTag;

    }
}
