package com.example.stockwatch;

public class Stock {

    String stockSymbol;
    String companyName;
    double price;
    double priceChange;
    double percentageChange;

    public Stock(String stockSymbol,String companyName,double price, double priceChange, double percentageChange){
        this.stockSymbol=stockSymbol;
        this.companyName=companyName;
        this.price=price;
        this.priceChange=priceChange;
        this.percentageChange=percentageChange;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(double priceChange) {
        this.priceChange = priceChange;
    }

    public double getPercentageChange() {
        return percentageChange;
    }

    public void setPercentageChange(double percentageChange) {
        this.percentageChange = percentageChange;
    }

    public Stock(){}

}

