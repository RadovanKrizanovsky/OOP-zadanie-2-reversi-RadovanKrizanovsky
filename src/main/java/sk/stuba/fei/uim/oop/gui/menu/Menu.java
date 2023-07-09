package sk.stuba.fei.uim.oop.gui.menu;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Menu extends JPanel {
    private final JLabel gameStatus;

    public Menu(int gridSize) {
        super(new GridLayout(1,5));
        this.setBackground(Color.LIGHT_GRAY);
        this.add(new RestartButton());
        JLabel guide = new JLabel("You: WHITE", SwingConstants.CENTER);
        this.add(guide);
        this.gameStatus = new JLabel("Turn: WHITE (W:2 B:2)", SwingConstants.CENTER);
        this.add(gameStatus);
        JLabel gameBoardSize = new JLabel("Board Size: " + gridSize + "x" + gridSize, SwingConstants.CENTER);
        this.add(gameBoardSize);

        ArrayList<String> myList = new ArrayList<>();
        myList.add("6x6");
        myList.add("8x8");
        myList.add("10x10");
        myList.add("12x12");
        myList.remove(gridSize + "x" + gridSize);
        myList.add(0, gridSize + "x" + gridSize);
        this.add(new GridSizeOptions(myList.toArray(new String[0])));
    }

    public JLabel getGameStatus() {
        return gameStatus;
    }
}
