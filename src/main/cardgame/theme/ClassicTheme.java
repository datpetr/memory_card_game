package main.cardgame.theme;

public class ClassicTheme extends CardTheme {
    public ClassicTheme() {
        super("Classic", "images/card_back.png");
    }
    
    @Override
    protected void initializeTheme() {
        // Add letters and numbers for classic cards
        for (char c = 'A'; c <= 'Z'; c++) {
            cardImages.add(String.valueOf(c));
        }
        for (int i = 0; i <= 9; i++) {
            cardImages.add(String.valueOf(i));
        }
    }
}
