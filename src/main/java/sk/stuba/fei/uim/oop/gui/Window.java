package sk.stuba.fei.uim.oop.gui;

import sk.stuba.fei.uim.oop.gui.board.GameBoard;
import sk.stuba.fei.uim.oop.gui.menu.Menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Window extends JFrame implements KeyListener {
    private GameBoard gameBoard;
    private Menu menu;

    public Window() {
        super();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(900, 950);
        this.setResizable(false);
        this.setLayout(new BorderLayout());
        this.setFocusable(true);
        this.addKeyListener(this);
        this.windowSetup(false);
    }

    private void windowSetup(boolean restart) {
        if (!restart) {
            this.gameBoard = new GameBoard(6);
            this.menu = new Menu(6);
        }
        this.add(menu, BorderLayout.PAGE_START);
        this.add(gameBoard, BorderLayout.CENTER);
        this.requestFocus();
        this.setVisible(true);
    }

    public void restartGame(int gridSizeAfter) {
        this.remove(gameBoard);
        this.remove(menu);
        this.gameBoard = new GameBoard(gridSizeAfter);
        this.menu = new Menu(gridSizeAfter);
        this.windowSetup(true);
    }

    public int getGridSize() {
        return gameBoard.getGridSize();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == 'r' || e.getKeyChar() == 'R') {
            this.restartGame(this.gameBoard.getGridSize());
        } else if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
            this.dispose();
            System.exit(0);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
