package com.vinade_app.rollerio;

public class Product {
    private String nameProduct, price, img, company, details, id;

    public Product() {
    }

    public Product(String nameProduct, String price, String img, String company, String details, String id) {
        this.nameProduct = nameProduct;
        this.price = price;
        this.img = img;
        this.company = company;
        this.details = details;
        this.id = id;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
