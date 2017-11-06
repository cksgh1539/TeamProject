package com.example.hp.teamproject;

//메뉴의 이미지,이름,가격,평점을 받는 어댑터클래스

public class FoodItem {
    private int Food;
    private String Name;
    private String Price;
    private String Score;

    FoodItem(int Food, String Name, String Price, String Score){
        this.Food = Food;
        this.Name = Name;
        this.Price = Price;
        this.Score = Score;
    }

    public int getFood(){return  Food;}
    public String getName(){
        return  Name;
    }
    public String getPrice(){
        return  Price;
    }
    public String getScore(){return Score;}

}
