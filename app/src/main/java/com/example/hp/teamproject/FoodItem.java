package com.example.hp.teamproject;

//메뉴의 이미지,이름,가격,평점을 받는 어댑터클래스

public class FoodItem {
  //  private int Food;
    private String Pict;
    private String Name;
    private String Price;
    private String Comment;

    FoodItem(String Picture, String Name, String Price){
        this.Pict = Picture;
        this.Name = Name;
        this.Price = Price;
      //  this.Comment = Comment;
    }

    public String getPicture(){return  Pict;}
    public String getName(){
        return  Name;
    }
    public String getPrice(){
        return  Price;
    }
   // public String getComment(){return Comment;}

}
