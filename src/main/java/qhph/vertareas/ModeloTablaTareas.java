package qhph.vertareas;

import javax.swing.table.AbstractTableModel;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


 // Define cómo la JTable muestra y edita los datos de las tareas.
public class ModeloTablaTareas extends AbstractTableModel {
    private GestorTareas gestorTareas;
    private List<Tarea> tareasMostradasActualmente;
    private final String[] nombresColumnas = {"Completado", "Nombre", "Descripción", "Prioridad", "Vencimiento", "Estudio"};
    private final Class<?>[] clasesColumnas = {Boolean.class, String.class, String.class, String.class, String.class, String.class};


     // Inicializa el modelo y carga datos iniciales.

    public ModeloTablaTareas(GestorTareas gestorTareas) {
        this.gestorTareas = gestorTareas;
        this.tareasMostradasActualmente = new ArrayList<>();
        actualizarDatos("Personalizado (arrastrar y soltar)"); // Carga y ordena datos.
    }


     // Retorna el número de filas (tareas).

    @Override
    public int getRowCount() { return tareasMostradasActualmente.size(); }


//     Retorna el número de columnas.

    @Override
    public int getColumnCount() { return nombresColumnas.length; }


//      Retorna el nombre de la columna.

    @Override
    public String getColumnName(int columnIndex) { return nombresColumnas[columnIndex]; }


     // Retorna la clase de datos de la columna.

    @Override
    public Class<?> getColumnClass(int columnIndex) { return clasesColumnas[columnIndex]; }

     // Indica si una celda es editable (solo la columna "Completado").

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) { return columnIndex == 0; }


     // Retorna el valor de la celda para dibujar en la JTable.

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= tareasMostradasActualmente.size()) return null;
        Tarea tarea = tareasMostradasActualmente.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> tarea.isCompletada();
            case 1 -> tarea.getNombre();
            case 2 -> tarea.getDescripcion();
            case 3 -> tarea.getPrioridad();
            case 4 -> (tarea.getFechaVencimiento() != null) ?
                    tarea.getFechaVencimiento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "N/A";
            case 5 -> "Pomodoro".equals(tarea.getMetodoEstudio()) ? "Sí" : "No";
            default -> null;
        };
    }


     // Actualiza el modelo de datos subyacente cuando el usuario edita una celda.

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 0) { // Solo si es la columna "Completado".
            Tarea tarea = tareasMostradasActualmente.get(rowIndex);
            boolean nuevoEstadoCompletado = (Boolean) aValue;
            if (tarea.isCompletada() != nuevoEstadoCompletado) {
                gestorTareas.alternarEstadoCompletado(tarea);
                fireTableCellUpdated(rowIndex, columnIndex); // Notifica a la JTable para redibujar solo esa celda.
            }
        }
    }


     // Obtiene un objeto Tarea de una fila específica.

    public Tarea getTareaEn(int rowIndex) {
        return (rowIndex >= 0 && rowIndex < tareasMostradasActualmente.size()) ? tareasMostradasActualmente.get(rowIndex) : null;
    }


     // Refresca los datos de la tabla, obtiene y ordena las tareas más recientes.

    public void actualizarDatos(String criterioOrdenamientoActual) {
        this.tareasMostradasActualmente = gestorTareas.obtenerTareas();
        gestorTareas.ordenarTareas(tareasMostradasActualmente, criterioOrdenamientoActual);
        fireTableDataChanged(); // Notifica a la JTable que los datos han cambiado.
    }


     // Mueve una tarea dentro de la lista para soportar Drag & Drop.

    public void moverFila(int fromIndex, int toIndex) {
        if (fromIndex == toIndex) return;
        Tarea tareaMovida = tareasMostradasActualmente.remove(fromIndex);
        tareasMostradasActualmente.add(toIndex, tareaMovida);
        for (int i = 0; i < tareasMostradasActualmente.size(); i++) {
            tareasMostradasActualmente.get(i).setOrdenPersonalizado(i); // Actualiza órdenes personalizados.
        }
        fireTableRowsUpdated(Math.min(fromIndex, toIndex), Math.max(fromIndex, toIndex)); // Notifica a la JTable.
    }
}