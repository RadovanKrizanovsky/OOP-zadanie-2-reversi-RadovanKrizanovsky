package sk.stuba.fei.uim.oop.gui.board;

import sk.stuba.fei.uim.oop.gui.menu.Menu;
import sk.stuba.fei.uim.oop.logic.GameLogic;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class GameBoard extends JPanel{
   private final int gridSize;
   private final GameLogic gameLogic;

    public GameBoard(int gridSize) {
        super();
        this.gridSize = gridSize;
        this.gameLogic = new GameLogic(this);
        this.setLayout(new GridLayout(gridSize, gridSize));

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                GridElement field = new GridElement(j, i);
                field.setLayout(new BorderLayout());
                field.setBorder(BorderFactory.createSoftBevelBorder(1));
                if (((j+i) % 2) == 0){
                    field.setBackground(new Color(0,108,0));
                } else {
                    field.setBackground(new Color(0,92,0));
                }
                this.add(field);
            }
        }
        this.prepareFirstRocks();
        this.markClickable("white");
    }

    private void prepareFirstRocks() {
        int x = gridSize/2-1;
        int y = gridSize/2-1;
        this.changeElementState(x,y, "black");
        x++;
        this.changeElementState(x,y, "white");
        y++;
        this.changeElementState(x,y, "black");
        x--;
        this.changeElementState(x,y, "white");
        this.refreshGame();
    }

    private void changeElementState(int x, int y, String state) {
        this.getElement(x, y).changeState(state);
    }

    private Menu getCurrentMenu() {
        return ((Menu)this.getParent().getComponent(0));
    }

    private String getStateOf(int x, int y) {
        return this.getElement(x, y).getCurrentState();
    }

    public boolean markClickable(String myColor){
        int x;
        int y;
        boolean atLeastOneClickable = false;
        for (Component c : this.getComponents()) {
            x = (((GridElement)c).returnX());
            y = (((GridElement)c).returnY());
            if (Objects.equals(this.getStateOf(x, y), "clickable")) {
                this.changeElementState(x, y, "empty");
            }
            if (this.gameLogic.allDirectionsCheck(x, y, false, myColor) > 0 && Objects.equals(this.getStateOf(x, y), "empty")) {
                this.changeElementState(x, y, "clickable");
                atLeastOneClickable = true;
            }
        }
        this.refreshGame();
        return atLeastOneClickable;
    }

    public GridElement getElement(int x, int y) {
        for (Component c : this.getComponents()) {
            if (((GridElement)c).returnX() == x && ((GridElement)c).returnY() == y) {
                return ((GridElement)c);
            }
        }
        return ((GridElement)this.getComponent(0));
    }

    public boolean gameEnded() {
        boolean end = (!(this.markClickable("black")) && !(this.markClickable("white")));
        if (end){
            int[] finalCount = this.numWhiteBlack();
            String stat = ("W:" + finalCount[0] + " B:" + finalCount[1] + ")");
            if (finalCount[0] > finalCount[1]) {
                this.getCurrentMenu().getGameStatus().setText("Winner: WHITE (" + stat);
            } else if (finalCount[0] < finalCount[1]) {
                this.getCurrentMenu().getGameStatus().setText("Winner: BLACK (" + stat);
            } else {
                this.getCurrentMenu().getGameStatus().setText("DRAW (" + stat);
            }
        }
        this.markClickable("white");
        return end;
    }

    public int[] numWhiteBlack() {
        int white = 0;
        int black = 0;
        for (Component c : this.getComponents()) {
            if (Objects.equals(((GridElement) c).getCurrentState(), "white")) {
                white++;
            } else if (Objects.equals(((GridElement) c).getCurrentState(), "black")) {
                black++;
            }
        }
        return new int[] {white, black};
    }

    public GameLogic getGameLogic() {
        return gameLogic;
    }

    public int getGridSize() {
        return gridSize;
    }

    public boolean isInsideGameBoard(int x, int y) {
        return (x >= 0 && x < this.gridSize) && (y >= 0 && y < this.gridSize);
    }

    public void refreshGame() {
        this.repaint();
        this.revalidate();
    }

}
