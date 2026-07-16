package qhph.vertareas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent; //Para el check box.
import java.awt.event.ItemListener;

/**
 EditorCeldaCompletado: Editor de celda para JTable que usa un JCheckBox.
 Permite marcar tareas como completadas/no completadas directamente en la tabla.
 */
public class EditorCeldaCompletado extends DefaultCellEditor implements ItemListener {

    private JCheckBox checkBox;


    public EditorCeldaCompletado() {
        super(new JCheckBox());
        checkBox = (JCheckBox) getComponent();
        checkBox.setHorizontalAlignment(JLabel.CENTER);
        checkBox.addItemListener(this); // Escucha cambios de estado.
    }


     // Retorna el componente (component, que tiene una config especifica) visual para la edición de la celda.

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        checkBox.setSelected((Boolean) value); // Marca/desmarca el checkbox.
        checkBox.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground()); // Ajusta fondo.
        return checkBox;
    }


     //Retorna el valor final de la celda editada (true/false).

    @Override
    public Object getCellEditorValue() {
        return checkBox.isSelected();
    }


    @Override
    public void itemStateChanged(ItemEvent e) {
        fireEditingStopped();
    }
}
