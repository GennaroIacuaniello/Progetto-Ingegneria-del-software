package gui;

import javax.swing.table.TableCellRenderer;
import javax.swing.*;
import java.awt.*;

class IconCellRenderer extends IconButton implements TableCellRenderer {

    public IconCellRenderer(String url, int width, int height) {

        super(url, width, height);

        setBackGround();
    }

    private void setBackGround() {

        this.setContentAreaFilled(false);
        this.setBorderPainted(false);
        this.setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        /*if (isSelected) {
            setBackground(table.getSelectionBackground());
        } else {
            setBackground(table.getBackground());
        }*/

        return this;
    }
}
