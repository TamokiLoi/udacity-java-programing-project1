package UI;

import java.text.ParseException;

public class HotelApplication {
    public static void main(String[] args) {
        MainMenu mainMenu = new MainMenu();
        try {
            mainMenu.displayMainMenu();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
