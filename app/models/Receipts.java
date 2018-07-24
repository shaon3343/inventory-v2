package models;

import io.ebean.Ebean;
import io.ebean.Finder;
import io.ebean.Model;
import util.AppConst;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "receipts")
public class Receipts extends Model {

    private static final long serialVersionUID = 1L;
    private static Finder<Integer, Receipts> find = new Finder<>(Receipts.class);

    @Id
    public Long id;
    @Column(name = "receipt_id")
    public String receiptId;
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    public Product productId;
    @Column(name = "product_quantity")
    public float productqty;
    @Column(name = "sales_man_id")
    public Long salesMan;

    @Column(name = "sales_date")
    public Date salesDate;
    @Column(name = "total_price")
    public float totalPrice;

    public static List<Receipts> all() {
        return find.all();
    }

    public static Receipts findById(int id) {
        return find.byId(id);

    }

    public static boolean saveReceipts(List<Map<String, String>> prodList) {
        try {
            //String nowDate = new SimpleDateFormat(AppConst.DATE_TIME_FORMAT_MIL).format(new Date());

            float totPrice = 0.0f;
            for (Map<String, String> prod : prodList) {
                Product p = Product.findById(Integer.parseInt(prod.get(AppConst.productId)));
                Receipts r = new Receipts();
                r.productId = p;
                r.receiptId = prod.get(AppConst.receiptId);
                int qty = Integer.parseInt(prod.get(AppConst.productQty));
                r.productqty = qty;


                //r.totalDue = due;
                totPrice = totPrice + (p.productPrice * qty);
                r.totalPrice = p.productPrice * qty;
                r.salesDate = new Date();
                r.salesMan = Long.parseLong(prod.get(AppConst.salesManId));
                r.save();

                p.productQty = p.productQty - qty;
                p.update(p);

            }

            if (!prodList.isEmpty()) {
                String paid = prodList.get(0).get(AppConst.paidAmount).replaceAll("\\$", "");
                if (!paid.contains(".")) {
                    paid = paid + ".00";
                }
                Float paidAmount = Float.parseFloat(paid);
                SalesMen man = SalesMen.findById(Integer.parseInt(prodList.get(0).get(AppConst.salesManId)));
                PaymentHistory pHist = new PaymentHistory();
                pHist.prevDue = man.salesManTotalDue;

                Float due = totPrice + man.salesManTotalDue;
                due = due - paidAmount;
                man.salesManTotalDue = due;
                SalesMen.update(man);

                pHist.salesMan = man.id;
                pHist.currentDue = due;
                pHist.paid = paidAmount;
                pHist.salesDate = new Date();
                pHist.receiptId = prodList.get(0).get(AppConst.receiptId);

                PaymentHistory.create(pHist);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static List<Receipts> findByReceiptId(String rId) {
        return Ebean.find(Receipts.class).where().eq("receiptId", rId).findList();
    }
}
