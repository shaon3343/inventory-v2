package models;

import com.avaje.ebean.Expr;
import com.avaje.ebean.Model;
import util.AppConst;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/*import play.db.ebean.Model.Finder;
import play.db.ebean.Model;*/

@Entity
@Table(name = "product")
public class Product extends Model {
    // replaced to com.avaje.model from play.db.ebean.Model.Finder
    private static Finder<Integer, Product> find = new Finder<>(Product.class);
    @Id
    public Long id;
    @Column(name = "product_name")
    public String productName;
    @Column(name = "product_code")
    public String productCode;
    @Column(name = "product_price")
    public float productPrice;
    @Column(name = "product_quantity")
    public float productQty;

    public static List<Product> all() {
        return find.all();
    }

    public static Product findById(int id) {
        return find.byId(id);

    }

    public static Product findById(Long id) {
        return find.byId(Integer.parseInt(id + ""));

    }

    public static List<Product> checkProdNameAndQty(String prodName, int qty) {
        return find.where().eq("productName", prodName).ge("productQty", qty)
                .findList();
    }

    public static List<Product> checkProdAndQty(int prodId, int qty) {
        return find.where().eq("id", prodId).ge("productQty", qty).findList();
    }

    public static boolean checkProdAndQty(String prodId, String qty) {
        int id = Integer.parseInt(prodId);
        int qt = Integer.parseInt(qty);
        List<Product> prod = checkProdAndQty(id, qt);
        if (prod.isEmpty())
            return false;
        else
            return true;
    }

    public static Product findByIdAndQty(int id) {
        return find.where().eq("id", id).gt("productQty", 0).findUnique();

    }

    public static Map<String, String> getProductsAsMap() {
        LinkedHashMap<String, String> prodList = new LinkedHashMap<String, String>();
        for (Product prod : Product.find.orderBy("productCode").findList()) {
            prodList.put(prod.id.toString(), prod.productName);
        }

        return prodList;
    }

    public static boolean checkAllProdQty(List<Map<String, String>> prodList) {
        boolean flag = true;
        for (Map<String, String> prod : prodList) {
            if (!checkProdAndQty(prod.get(AppConst.productId),
                    prod.get(AppConst.productQty))) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    public static Map<String, String> getProductsWithoutThese(
            List<Integer> prodIdList) {

        LinkedHashMap<String, String> prodList = new LinkedHashMap<String, String>();

        List<Product> pList = find.where().not(Expr.in("id", prodIdList))
                .findList();

        for (Product prod : pList) {
            prodList.put(prod.id.toString(), prod.productName);
        }

        return prodList;
    }

    public static List<Product> suggestProdList(String prodCode) {

        List<Product> listProd = find.where()
                .ilike("productCode", "%" + prodCode + "%").ge("productQty", 1)
                .findList();

        return listProd;
    }

    public static List<Product> suggestProdList(String prodCode,
                                                List<Map<String, String>> existProdList) {
        List<Integer> prodIdList = new ArrayList<Integer>();
        for (Map<String, String> mp : existProdList) {
            try {
                prodIdList.add(Integer.parseInt(mp.get(AppConst.productId)));
            } catch (Exception e) {

            }
        }
        List<Product> listProd = new ArrayList<Product>();
        if (!prodIdList.isEmpty()) {
            listProd = find.where().not(Expr.in("id", prodIdList))
                    .ilike("productCode", "%" + prodCode + "%")
                    .ge("productQty", 1).findList();
        } else {
            listProd = find.where().ilike("productCode", "%" + prodCode + "%")
                    .ge("productQty", 1).findList();
        }
        return listProd;
    }

    public static void delete(Long id) {
        delete(id);
    }

    public static void create(Product product) {
        product.save();

    }

    public static void update(Product product) {
        product.update();

    }

    public static Product getProduct(Product pCode) {
        try {
            return find.where().eq("productName", pCode.productName).eq("productCode", pCode.productCode).findUnique();
        } catch (Exception e) {
            return null;
        }

    }
}
