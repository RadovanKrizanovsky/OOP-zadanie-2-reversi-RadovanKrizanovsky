package sk.stuba.fei.uim.oop.gui.menu;

import sk.stuba.fei.uim.oop.gui.Window;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RestartButton extends JButton implements ActionListener {
    public RestartButton() {
        super("Restart Game");
        this.addActionListener(this);
    }

    private Window getCurrentWindow() {
        return ((Window)this.getTopLevelAncestor());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
       int gridSize = this.getCurrentWindow().getGridSize();
       this.getCurrentWindow().restartGame(gridSize);
    }
}
