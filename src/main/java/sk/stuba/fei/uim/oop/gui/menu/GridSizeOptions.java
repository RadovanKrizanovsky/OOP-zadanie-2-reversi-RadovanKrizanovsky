package sk.stuba.fei.uim.oop.gui.menu;

import sk.stuba.fei.uim.oop.gui.Window;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class GridSizeOptions extends JComboBox<String> implements ActionListener {
    public GridSizeOptions(String[] options) {
        super(options);
        this.addActionListener(this);
    }

    public void actionPerformed(ActionEvent a) {
        int gridSize;
        if (Objects.requireNonNull(this.getSelectedItem()).toString().length() == 3) {
            gridSize = Integer.parseInt(this.getSelectedItem().toString().substring(0,1));
        } else {
            gridSize = Integer.parseInt(this.getSelectedItem().toString().substring(0,2));
        }
        if (((Window)this.getTopLevelAncestor()).getGridSize() != gridSize) {
            ((Window)this.getTopLevelAncestor()).restartGame(gridSize);
        } else {
            this.getTopLevelAncestor().requestFocus();
        }
    }
}
