package main.cardgame.theme;

public class PictureTheme extends CardTheme {
    public PictureTheme() {
        super("Pictures", "images/picture_back.png");
    }
    
    @Override
    protected void initializeTheme() {
        // Add paths to picture images
        String[] imagePaths = {
            "images/animal1.png",
            "images/animal2.png",
            "images/animal3.png",
            "images/animal4.png",
            "images/animal5.png",
            "images/animal6.png",
            "images/animal7.png",
            "images/animal8.png",
            "images/animal9.png",
            "images/animal10.png",
            "images/animal11.png",
            "images/animal12.png",
            "images/animal13.png",
            "images/animal14.png",
            "images/animal15.png",
            "images/animal16.png"
        };
        
        for (String path : imagePaths) {
            cardImages.add(path);
        }
    }
}
