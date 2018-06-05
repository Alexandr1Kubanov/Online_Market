package sample;

import javafx.beans.property.*;

public class Product {

    private IntegerProperty idProduct;
    private StringProperty nameProduct;
    private DoubleProperty oldPrice;
    private IntegerProperty unit;
    private BooleanProperty presence;
    private BooleanProperty sale;

    public  Product(int idProduct, String nameProduct,
                    double oldPrice,int unit,boolean presence,boolean sale){

        this.idProduct = new SimpleIntegerProperty(idProduct);
        this.nameProduct = new SimpleStringProperty(nameProduct);
        this.oldPrice = new SimpleDoubleProperty(oldPrice);
        this.unit = new SimpleIntegerProperty(unit);
        this.presence = new SimpleBooleanProperty(presence);
        this.sale = new SimpleBooleanProperty(sale);

    }

    public IntegerProperty getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(IntegerProperty idProduct) {
        this.idProduct = idProduct;
    }

    public StringProperty getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(StringProperty nameProduct) {
        this.nameProduct = nameProduct;
    }

    public DoubleProperty getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(DoubleProperty oldPrice) {
        this.oldPrice = oldPrice;
    }

    public IntegerProperty getUnit() {
        return unit;
    }

    public void setUnit(IntegerProperty unit) {
        this.unit = unit;
    }

    public BooleanProperty getPresence() {
        return presence;
    }

    public void setPresence(BooleanProperty presence) {
        this.presence = presence;
    }

    public BooleanProperty getSale(){
        return sale;
    }

    public void setSale(BooleanProperty sale) {
        this.sale=sale;
    }
}

