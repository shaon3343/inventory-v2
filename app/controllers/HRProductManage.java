
package controllers;

import models.Product;
import models.SalesMen;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import util.AppConst;
import views.html.setInventory.*;

import javax.inject.Inject;
import java.util.List;

public class HRProductManage extends Controller {

    private final FormFactory formFactory;

    @Inject
    public HRProductManage(final FormFactory formFactory) {
        this.formFactory = formFactory;
    }


    /*salesMan CRUD Start */

    public Result createSalesMan() {
        Form<SalesMen> salesManForm = formFactory.form(SalesMen.class);
        return ok(insertSalesMan.render(salesManForm));
    }

    public Result saveSalesMan() {
        Form<SalesMen> salesManForm = formFactory.form(SalesMen.class);
        Form<SalesMen> filledForm = salesManForm.bindFromRequest();
        SalesMen salesMan = filledForm.get();

        SalesMen.create(salesMan);
        flash("success", AppConst.SUCCESS_MESSAGE);
        //return ok("");
        return redirect(controllers.routes.HRProductManage.salesMenList());
    }

    public Result salesMenList() {
        List<SalesMen> sMen = SalesMen.all();
        return ok(salesMenList.render(sMen));
    }


    public Result salesManShow(Long id) {
        SalesMen s = SalesMen.findById(id);

        if (s == null) {
            flash("error", AppConst.ERROR_MESSAGE_ID_NOT_FOUND);
//				return ok("");
            return redirect(controllers.routes.HRProductManage.salesMenList());
        } else
            return ok(salesManShow.render(s));
    }

    public Result salesManEdit(Long id) {
        Form<SalesMen> salesManForm = formFactory.form(SalesMen.class);
        SalesMen s = SalesMen.findById(id);

        if (s == null) {
            flash("error", AppConst.ERROR_MESSAGE_ID_NOT_FOUND);
            //return ok("");
            return redirect(controllers.routes.HRProductManage.show(id));
        } else
            return ok(salesManEdit.render(salesManForm.fill(s)));
    }


    public Result salesManupdate() {
        Form<SalesMen> salesManForm = formFactory.form(SalesMen.class);
        Form<SalesMen> filledForm = salesManForm.bindFromRequest();
        if (filledForm.hasErrors()) {
            return badRequest(salesManEdit.render(filledForm));
        } else {

            SalesMen s = filledForm.get();

            Long sId = s.id;

            SalesMen man = SalesMen.findById(sId);
            man.salesManName = s.salesManName;
            man.salesManAddress = s.salesManAddress;
            man.salesManContact = s.salesManContact;
            man.salesManTotalDue = s.salesManTotalDue;

            SalesMen.update(man);

            return ok(salesMenList.render(SalesMen.all()));
        }

    }

    public Result deleteSalesMan(Long id) {
        SalesMen.delete(id);
        flash("success", AppConst.SUCCESSFUL_DELETE_MESSAGE);
        return ok(salesMenList.render(SalesMen.all()));
    }

    /* CRUD SalesMan END */

    public Result create() {

        final Form<Product> ProductForm = formFactory.form(Product.class);
        return ok(create.render(ProductForm));
    }

    public Result save() {
        final Form<Product> ProductForm = formFactory.form(Product.class);
        Form<Product> filledForm = ProductForm.bindFromRequest();
        Product product = filledForm.get();

        Product.create(product);
        flash("success", AppConst.SUCCESS_MESSAGE);
        //return ok("");
        return redirect(controllers.routes.HRProductManage.list());
    }

    public Result list() {
        List<Product> Products = Product.all();
        return ok(list.render(Products));
    }


    public Result show(Long id) {
        Product prod = Product.findById(id);

        if (prod == null) {
            flash("error", AppConst.ERROR_MESSAGE_ID_NOT_FOUND);
//				return ok("");
            return redirect(controllers.routes.HRProductManage.list());
        } else
            return ok(show.render(prod));
    }

    public Result edit(Long id) {
        final Form<Product> ProductForm = formFactory.form(Product.class);
        Product prod = Product.findById(id);

        if (prod == null) {
            flash("error", AppConst.ERROR_MESSAGE_ID_NOT_FOUND);
            //return ok("");
            return redirect(controllers.routes.HRProductManage.show(id));
        } else
            return ok(edit.render(ProductForm.fill(prod)));
    }


    public Result update() {
        final Form<Product> ProductForm = formFactory.form(Product.class);
        Form<Product> filledForm = ProductForm.bindFromRequest();
        if (filledForm.hasErrors()) {
            return badRequest(edit.render(filledForm));
        } else {

            Product p = filledForm.get();

            Long ProductId = p.id;

            Product prod = Product.findById(ProductId);
            prod.productName = p.productName;
            prod.productCode = p.productCode;
            prod.productPrice = p.productPrice;
            prod.productQty = p.productQty;

            Product.update(prod);

            return ok(list.render(Product.all()));
        }

    }

    public Result delete(Long id) {
        Product.delete(id);
        flash("success", AppConst.SUCCESSFUL_DELETE_MESSAGE);
        return ok(list.render(Product.all()));
    }


}


