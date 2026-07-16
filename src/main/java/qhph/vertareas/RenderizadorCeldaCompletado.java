package qhph.vertareas;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

// Renderiza celdas de tabla como checkboxes visuales (solo presentación)
public class RenderizadorCeldaCompletado extends JCheckBox implements TableCellRenderer {

    // Centra el checkbox y asegura que el fondo se renderice correctamente
    public RenderizadorCeldaCompletado() {
        setHorizontalAlignment(JLabel.CENTER);
        setOpaque(true);  // Fundamental para el pintado del fondo
    }

    // Configura el checkbox para reflejar el valor y estado de selección de la celda
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {

        setSelected((Boolean) value);  // Establece el estado marcado/no marcado

        // Ajusta colores según si la fila está seleccionada o no
        setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());

        return this;  // Retorna este componente como renderizador
    }
}