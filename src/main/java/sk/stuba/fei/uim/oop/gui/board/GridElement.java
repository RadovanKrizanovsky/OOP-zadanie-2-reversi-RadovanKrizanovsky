package sk.stuba.fei.uim.oop.gui.board;

import lombok.SneakyThrows;
import sk.stuba.fei.uim.oop.logic.GameLogic;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class GridElement extends JPanel implements MouseListener {
    private final int x;
    private final int y;
    private String currentState;
    private JLabel iconAdder;

    public GridElement(int x, int y) {
        super();
        this.x = x;
        this.y = y;
        this.currentState = "empty";
        this.addMouseListener(this);
        this.iconAdder = null;
    }

    private GameLogic useGameLogic() {
        return ((GameBoard)this.getParent()).getGameLogic();
    }

    private GameBoard getCurrentGameBoard() {
        return ((GameBoard)this.getParent());
    }

    private void refreshGridElement() {
        this.repaint();
        this.revalidate();
    }

    private boolean isCurrentStateNot(String which) {
        return (!Objects.equals(this.getCurrentState(), which));
    }

    public void changeState(String which) {
        if (this.iconAdder != null) {
            this.remove(this.iconAdder);
        }
        if (Objects.equals(which, "black") && this.isCurrentStateNot("black")) {
            this.changeIcon("/black.png");
        } else if (Objects.equals(which, "white") && this.isCurrentStateNot("white")) {
            this.changeIcon("/white.png");
        } else if (Objects.equals(which, "clickable" ) && this.isCurrentStateNot("clickable")) {
            this.changeIcon("/clickable.png");
        } else if (Objects.equals(which, "option" ) && this.isCurrentStateNot("option")) {
            this.changeIcon("/option.png");
        }
        this.currentState = which;
        this.refreshGridElement();
    }

    public String getCurrentState() {
        return currentState;
    }

    public int returnX() {
        return x;
    }

    public int returnY() {
        return y;
    }

    @SneakyThrows
    private void changeIcon(String which) {
        int gridSize = this.getCurrentGameBoard().getGridSize();
        BufferedImage icon;
        icon = ImageIO.read(Objects.requireNonNull(GridElement.class.getResourceAsStream(which)));
        assert icon != null;
        Image scaled = icon;
        switch (gridSize) {
            case 6:
                scaled = icon.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                break;
            case 8:
                scaled = icon.getScaledInstance(65, 65, Image.SCALE_SMOOTH);
                break;
            case 10:
                scaled = icon.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                break;
            case 12:
                scaled = icon.getScaledInstance(35, 35, Image.SCALE_SMOOTH);
                break;
        }
        this.iconAdder = new JLabel(new ImageIcon(scaled));
        this.add(iconAdder);
        this.refreshGridElement();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        this.getTopLevelAncestor().requestFocus();
        if (Objects.equals(this.currentState, "option")) {
            this.remove(0);
            this.changeState("white");
            this.useGameLogic().allDirectionsCheck(this.x,this.y,true, "white");

            if (this.getCurrentGameBoard().markClickable("black")) {
                this.useGameLogic().enemyMove();
            }
            this.getCurrentGameBoard().gameEnded();
            this.refreshGridElement();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (Objects.equals(this.currentState, "clickable")) {
            this.remove(0);
            this.changeState("option");
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (Objects.equals(this.currentState, "option")) {
            this.remove(0);
            this.changeState("clickable");
        }
    }
}
