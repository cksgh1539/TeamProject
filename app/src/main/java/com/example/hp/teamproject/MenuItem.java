package com.example.hp.teamproject;

//메뉴의 이미지,이름,가격을 받는 어댑터 클래스

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