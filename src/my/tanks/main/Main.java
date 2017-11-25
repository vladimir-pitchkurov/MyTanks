package my.tanks.main;

import my.tanks.display.Display;
import my.tanks.game.Game;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class Main {
    public static void main(String[] args) {
        Game newGame = new Game();
        newGame.start();
    }
}
