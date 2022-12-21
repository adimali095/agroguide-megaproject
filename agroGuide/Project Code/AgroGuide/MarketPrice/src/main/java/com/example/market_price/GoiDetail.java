package com.example.market_price;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GoiDetail {


    private String GroceryName,GroceryPlace,GroceryPrice;
    private Long GroceryTime;

    /*
    public GoiDetail(String groceryName, String groceryPlace, String groceryPrice, String groceryTime) {
        GroceryName = groceryName;
        GroceryPlace = groceryPlace;
        GroceryPrice = groceryPrice;
        GroceryTime = groceryTime;
    }
    */

    public GoiDetail() {

    }

    public void setGroceryName(String groceryName) {
        GroceryName = groceryName;
    }

    public String getGroceryName() {
        return GroceryName;
    }

    public void setGroceryPlace(String groceryPlace) {
        GroceryPlace = groceryPlace;
    }

    public String getGroceryPlace() {
        return GroceryPlace;
    }

    public void setGroceryPrice(String groceryPrice) {
        GroceryPrice = groceryPrice;
    }

    public String getGroceryPrice() {
        return GroceryPrice;
    }

    public void setGroceryTime(String groceryTime) {
        //GroceryTime = groceryTime;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = sdf.parse(groceryTime);
            GroceryTime = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Long getGroceryTime() {
        return GroceryTime;
    }
}
