package main.cardgame.model;

public class Card implements CardBehavior{
    private int id;
    private String imagePath;
    private boolean isMatched;
    private boolean isFaceUp;
    private static String backImagePath;


    public Card(int id, String imagePath) {
        this.id = id;
        this.imagePath = imagePath; // Corrected from 'imagePath' to 'value'
        this.isMatched = false;
        this.isFaceUp = false;
    }

    public static String getBackImagePath() {
        return backImagePath;
    }

    public static void setBackImagePath(String backImagePath) {
        Card.backImagePath = backImagePath;
    }

    public String getImagePath() {
        return imagePath;
        // will be added more code later
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }


    public boolean isMatched() {
        return isMatched;
        // will be added more code later
    }

    public void setMatched(boolean matched) {
        isMatched = matched;
        // will be added more code later
    }

    public boolean isFaceUp() {
        return isFaceUp;
        // will be added more code later
    }

    public void flip() {
        isFaceUp = !isFaceUp;
        // will be added more code later
    }

    public int getId() {
        return id;
        // will be added more code later
    }

    // Set the back image path for all cards
       static{Card.setBackImagePath("file:src/main/resources/images/back2.jpg");}
}
