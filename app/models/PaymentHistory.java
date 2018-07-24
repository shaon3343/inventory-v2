package models;

import com.avaje.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;
/*import play.db.ebean.Model;
import play.db.ebean.Model.Finder;*/

@Entity
@Table(name = "payment_history")
public class PaymentHistory extends Model {
    private static Finder<Integer, PaymentHistory> find = new Finder<>(PaymentHistory.class);
    @Id
    public Long id;
    @Column(name = "receipt_id")
    public String receiptId;
    @Column(name = "sales_man_id")
    public Long salesMan;
    @Column(name = "sales_date")
    public Date salesDate;
    @Column(name = "prev_due")
    public float prevDue;
    @Column(name = "paid")
    public float paid;
    @Column(name = "current_due")
    public float currentDue;

    public static List<PaymentHistory> all() {
        return find.all();
    }

    public static PaymentHistory findById(int id) {
        return find.byId(id);

    }

    public static void create(PaymentHistory history) {
        history.save();

    }

    public static PaymentHistory findbyCustAndReceipt(String rId, String custId) {

        return find.where().eq("receiptId", rId).eq("salesMan", Long.parseLong(custId)).findUnique();
    }

}
