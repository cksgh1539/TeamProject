package com.example.hp.teamproject;

public class MenuItem {
    private int Food;
    private String Name;
    private String Price;

    MenuItem(int Food, String Name, String Price){
        this.Food = Food;
        this.Name = Name;
        this.Price = Price;
    }

    public int getFood(){return  Food;}
    public String getName(){
        return  Name;
    }
    public String getPrice(){
        return  Price;
    }

}