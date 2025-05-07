package main.cardgame.theme;

import java.util.ArrayList;
import java.util.List;

public abstract class CardTheme {
    protected String name;
    protected List<String> cardImages;
    protected String backImage;
    
    public CardTheme(String name, String backImage) {
        this.name = name;
        this.backImage = backImage;
        this.cardImages = new ArrayList<>();
        initializeTheme();
    }
    
    protected abstract void initializeTheme();
    
    public String getName() {
        return name;
    }
    
    public String getCardImage(int index) {
        if (index < 0 || index >= cardImages.size()) {
            throw new IndexOutOfBoundsException("Invalid card image index");
        }
        return cardImages.get(index);
    }
    
    public String getBackImage() {
        return backImage;
    }
    
    public int getThemeSize() {
        return cardImages.size();
    }
}
