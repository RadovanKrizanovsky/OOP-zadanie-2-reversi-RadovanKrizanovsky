package sk.stuba.fei.uim.oop.logic;

import sk.stuba.fei.uim.oop.gui.board.GameBoard;
import sk.stuba.fei.uim.oop.gui.board.GridElement;
import sk.stuba.fei.uim.oop.gui.menu.Menu;

import java.awt.*;
import java.util.Objects;

public class GameLogic {
    private final GameBoard gameBoard;

    public GameLogic(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    private int[] directionChange(int x , int y ,String direction) {
        switch (direction) {
            case "leftup":
                x--;
                y--;
                break;
            case "up":
                y--;
                break;
            case "rightup":
                x++;
                y--;
                break;
            case "left":
                x--;
                break;
            case "right":
                x++;
                break;
            case "leftdown":
                x--;
                y++;
                break;
            case "down":
                y++;
                break;
            case "rightdown":
                x++;
                y++;
                break;
        }
        return new int[]{x, y};
    }

    private int linearCheck(int x, int y, boolean flip, String direction, String myColor) {
        String enemyColor = enemyColor(myColor);
        int numOfEnemies = 0;
        boolean isLastMine = false;
        boolean lastWasFlip = false;
        do {
            if (!lastWasFlip) {
                int[] newDirections = this.directionChange(x, y , direction);
                x = newDirections[0];
                y = newDirections[1];
            }
            lastWasFlip = false;
            if (this.isInsideGameBoard(x, y) && this.isColorAt(x, y, enemyColor)) {
                numOfEnemies++;
                if (flip){
                    this.removeIconOf(x, y);
                    this.changeStateOf(x, y, myColor);
                    int[] newDirections = this.directionChange(x, y , direction);
                    x = newDirections[0];
                    y = newDirections[1];
                    lastWasFlip = true;
                }
            } else if (this.isInsideGameBoard(x, y) && this.isColorAt(x, y, myColor)){
                isLastMine = true;
            }
        } while(this.isInsideGameBoard(x, y) && this.isColorAt(x, y, enemyColor));
        this.refreshGameBoard();
        if (isLastMine) {
            return numOfEnemies;
        } else {
            return 0;
        }
    }

    private String enemyColor(String myColor) {
        if (Objects.equals(myColor, "white")) {
            return "black";
        } else {
            return "white";
        }
    }

    public int allDirectionsCheck(int x, int y, boolean flip, String myColor) {
        String[] directions = {"leftup","up","rightup","left","right","leftdown","down","rightdown"};
        boolean[] validDirection = {false, false, false, false, false, false, false, false};
        int directionIndex = 0;
        int numberOfEnemies = 0;
        for (String direction : directions) {
            if (this.linearCheck(x, y, false, direction, myColor) > 0) {
                numberOfEnemies = numberOfEnemies + (this.linearCheck(x, y, false, direction, myColor));
                validDirection[directionIndex] = true;
            }
            directionIndex++;
        }
        if (flip) {
            directionIndex = 0;
            for (String direction : directions) {
                if (validDirection[directionIndex]) {
                    this.linearCheck(x, y, true, direction, myColor);
                }
                directionIndex++;
            }
        }
        this.refreshGameBoard();
        return numberOfEnemies;
    }

    public void enemyMove() {
        this.gameDidntEnded();
        this.markClickableFor("black");
        do {
            GridElement bestPlace = this.gameBoard.getElement(0,0);
            for (Component c : this.gameBoard.getComponents()) {
                if (Objects.equals(((GridElement) c).getCurrentState(), "clickable")) {
                    bestPlace = ((GridElement) c);
                    break;
                }
            }
            int bestPlaceEnemies = this.allDirectionsCheck(0,0, false, "black");
            ((Menu)this.gameBoard.getParent().getComponent(0)).getGameStatus().setText("Turn: BLACK");
            this.markClickableFor("black");
            for (Component c : this.gameBoard.getComponents()) {
                if (Objects.equals(((GridElement) c).getCurrentState(), "clickable")) {
                    int newPlaceEnemies = this.allDirectionsCheck(((GridElement)c).returnX(), ((GridElement)c).returnY(), false, "black");
                    if (newPlaceEnemies > bestPlaceEnemies) {
                        bestPlace = ((GridElement)c);
                        bestPlaceEnemies = newPlaceEnemies;
                    }
                }
            }
            bestPlace.changeState("black");
            this.allDirectionsCheck(bestPlace.returnX(), bestPlace.returnY(), true, "black");
            this.refreshGameBoard();
        } while (this.gameDidntEnded() && !(this.markClickableFor("white")));
        int[] finalCount = this.gameBoard.numWhiteBlack();
        String stat = ("W:" + finalCount[0] + " B:" + finalCount[1] + ")");
        ((Menu)this.gameBoard.getParent().getComponent(0)).getGameStatus().setText("Turn: WHITE (" + stat);
    }
    
    private boolean isColorAt(int x, int y, String which) {
        return Objects.equals(this.getCurrentStateOF(x, y), which);
    }

    private String getCurrentStateOF(int x, int y) {
        return this.gameBoard.getElement(x, y).getCurrentState();
    }

    private void changeStateOf(int x, int y, String to) {
        this.gameBoard.getElement(x, y).changeState(to);
    }

    private void removeIconOf(int x, int y) {
        this.gameBoard.getElement(x, y).remove(0);
    }

    private boolean isInsideGameBoard(int x, int y) {
        return this.gameBoard.isInsideGameBoard(x, y);
    }

    private void refreshGameBoard() {
        this.gameBoard.refreshGame();
    }

    private boolean gameDidntEnded() {
        return  !(this.gameBoard.gameEnded());
    }

    private boolean markClickableFor(String color) {
        return this.gameBoard.markClickable(color);
    }
}

