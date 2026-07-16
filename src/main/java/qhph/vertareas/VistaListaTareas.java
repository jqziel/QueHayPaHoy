package qhph.vertareas; // Paquete de clases de gestión de tareas.

import net.miginfocom.swing.MigLayout; // Gestor de diseño.
import javax.swing.*; // Clases Swing.
import java.awt.datatransfer.DataFlavor; // Para Drag and Drop.
import java.awt.datatransfer.Transferable; // Para Drag and Drop.
import java.awt.dnd.DnDConstants; // Constantes de Drag and Drop.
import java.awt.event.MouseAdapter; // Manejo de eventos de ratón.
import java.awt.event.MouseEvent; // Eventos de ratón.
import java.util.Objects; // Utilidades para objetos.

// VistaListaTareas: Panel principal que muestra y permite interactuar con la lista de tareas en una tabla.
// Implementa funcionalidad de Drag and Drop para reordenar filas.

public class VistaListaTareas extends JPanel {
    private GestorTareas gestorTareas;
    private ModeloTablaTareas modeloTablaTareas;
    private JTable tablaTareas;
    private JComboBox<String> comboBoxCriterioOrdenamiento;
    private JPopupMenu menuContextual;

    // Inicializa componentes y carga datos de tareas.

    public VistaListaTareas(GestorTareas gestorTareas) {
        this.gestorTareas = gestorTareas;
        inicializarComponentes();
        actualizarDatosTabla();
    }

    // Configura el diseño, añade elementos de UI y sus funcionalidades.

    private void inicializarComponentes() {
        setLayout(new MigLayout("fill, insets 15", "[grow]", "[][grow][]")); // Configuración del layout.

        // Panel de Control (ordenamiento)
        JPanel panelControl = new JPanel(new MigLayout("insets 0", "[grow]", "[]"));
        panelControl.setOpaque(false);

        // JComboBox para seleccionar criterio de ordenamiento.
        comboBoxCriterioOrdenamiento = new JComboBox<>(new String[]{
                "Personalizado (arrastrar y soltar)",
                "Por fecha de vencimiento",
                "Por fecha de modificación (más recientes)",
                "Por prioridad"
        });
        comboBoxCriterioOrdenamiento.addActionListener(e -> { // Listener para cambiar ordenamiento.
            boolean habilitarArrastre = Objects.equals(comboBoxCriterioOrdenamiento.getSelectedItem(), "Personalizado (arrastrar y soltar)");
            tablaTareas.setDragEnabled(habilitarArrastre); // Habilita/deshabilita D&D.
            tablaTareas.setDropMode(habilitarArrastre ? DropMode.INSERT_ROWS : DropMode.USE_SELECTION);
            actualizarDatosTabla(); // Recarga y reordena la tabla.
        });
        panelControl.add(new JLabel("Ordenar por:"), "split 2");
        panelControl.add(comboBoxCriterioOrdenamiento, "growx, wrap");
        add(panelControl, "growx, wrap"); // Añade el panel de control a la vista principal.

        // Configuración de la JTable
        modeloTablaTareas = new ModeloTablaTareas(gestorTareas);
        tablaTareas = new JTable(modeloTablaTareas);
        tablaTareas.setFillsViewportHeight(true);
        tablaTareas.setRowHeight(25);

        // Configuración de la columna "Completado" (índice 0) con renderizador y editor personalizados.
        tablaTareas.getColumnModel().getColumn(0).setCellRenderer(new RenderizadorCeldaCompletado());
        tablaTareas.getColumnModel().getColumn(0).setCellEditor(new EditorCeldaCompletado());
        tablaTareas.getColumnModel().getColumn(0).setPreferredWidth(80);

        tablaTareas.getTableHeader().setReorderingAllowed(false); // Impide reordenar columnas.

        // Configuración de Drag and Drop.
        tablaTareas.setDragEnabled(true);
        tablaTareas.setDropMode(DropMode.INSERT_ROWS);
        tablaTareas.setTransferHandler(new ManejadorArrastreFilas(tablaTareas, modeloTablaTareas, comboBoxCriterioOrdenamiento));

        // JScrollPane para la tabla.
        JScrollPane scrollPane = new JScrollPane(tablaTareas);
        add(scrollPane, "grow, wrap");

        // Configuración del Menú Contextual (clic derecho).
        configurarMenuContextual();
        tablaTareas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) { // Detecta clic derecho.
                    int row = tablaTareas.rowAtPoint(e.getPoint());
                    if (row >= 0) {
                        tablaTareas.setRowSelectionInterval(row, row); // Selecciona la fila.
                        Tarea tareaSeleccionada = modeloTablaTareas.getTareaEn(row);
                        if (tareaSeleccionada != null) {
                            // Actualiza texto del menú según estado de la tarea.
                            ((JMenuItem) menuContextual.getComponent(0)).setText(tareaSeleccionada.isCompletada() ? "Marcar como Pendiente" : "Marcar como Completada");
                            menuContextual.show(e.getComponent(), e.getX(), e.getY()); // Muestra el menú.
                        }
                    }
                }
            }
        });
    }

    // Crea y define las acciones para el menú contextual (clic derecho).

    private void configurarMenuContextual() {
        menuContextual = new JPopupMenu();

        // Opción: Marcar como Completada/Pendiente.
        JMenuItem alternarEstadoItem = new JMenuItem("Marcar como Completada");
        alternarEstadoItem.addActionListener(e -> {
            int filaSeleccionada = tablaTareas.getSelectedRow();
            if (filaSeleccionada != -1) {
                Tarea tarea = modeloTablaTareas.getTareaEn(filaSeleccionada);
                if (tarea != null) {
                    gestorTareas.alternarEstadoCompletado(tarea);
                    modeloTablaTareas.fireTableCellUpdated(filaSeleccionada, 0); // Notifica actualización de celda.
                }
            }
        });
        menuContextual.add(alternarEstadoItem);

        // Opción: Editar Tarea.
        JMenuItem editarItem = new JMenuItem("Editar Tarea");
        editarItem.addActionListener(e -> {
            int filaSeleccionada = tablaTareas.getSelectedRow();
            if (filaSeleccionada != -1) {
                Tarea tarea = modeloTablaTareas.getTareaEn(filaSeleccionada);
                if (tarea != null) {
                    DialogoEditarTarea dialogo = new DialogoEditarTarea(SwingUtilities.getWindowAncestor(this), tarea, gestorTareas);
                    dialogo.setVisible(true); // Muestra diálogo modal.

                    if (dialogo.esTareaGuardada()) {
                        gestorTareas.actualizarTarea(tarea);
                        actualizarDatosTabla(); // Recarga y reordena la tabla.
                    }
                }
            }
        });
        menuContextual.add(editarItem);

        // Opción: Eliminar Tarea.
        JMenuItem eliminarItem = new JMenuItem("Eliminar Tarea");
        eliminarItem.addActionListener(e -> {
            int filaSeleccionada = tablaTareas.getSelectedRow();
            if (filaSeleccionada != -1) {
                Tarea tarea = modeloTablaTareas.getTareaEn(filaSeleccionada);
                if (tarea != null) {
                    // Pide confirmación.
                    int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que quieres eliminar la tarea '" + tarea.getNombre() + "'?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (confirm == JOptionPane.YES_OPTION) { // Si confirma, elimina y actualiza tabla.
                        gestorTareas.eliminarTarea(tarea);
                        actualizarDatosTabla();
                    }
                }
            }
        });
        menuContextual.add(eliminarItem);
    }

    // Recarga los datos de la tabla, obtiene y ordena las tareas más recientes.

    public void actualizarDatosTabla() {
        modeloTablaTareas.actualizarDatos((String) comboBoxCriterioOrdenamiento.getSelectedItem());
    }

    //ManejadorArrastreFilas: Clase interna para Drag and Drop de filas.
    //Permite al usuario reordenar tareas arrastrando y soltando filas en la JTable.

    private static class ManejadorArrastreFilas extends TransferHandler {
        private final JTable tabla;
        private final ModeloTablaTareas modelo;
        private final JComboBox<String> comboBoxCriterioOrdenamiento;
        private int[] indices = null;

        //Constructor del manejador D&D.
        public ManejadorArrastreFilas(JTable tabla, ModeloTablaTareas modelo, JComboBox<String> comboBoxCriterioOrdenamiento) {
            this.tabla = tabla;
            this.modelo = modelo;
            this.comboBoxCriterioOrdenamiento = comboBoxCriterioOrdenamiento;
        }

        //Crea el objeto Transferable cuando se inicia el arrastre.
        //Solo permite arrastrar en modo "Personalizado".

        @Override
        protected Transferable createTransferable(JComponent c) {
            if (Objects.equals(comboBoxCriterioOrdenamiento.getSelectedItem(), "Personalizado (arrastrar y soltar)")) {
                return new Transferable() {
                    @Override public DataFlavor[] getTransferDataFlavors() { return new DataFlavor[]{DataFlavor.javaFileListFlavor}; }
                    @Override public boolean isDataFlavorSupported(DataFlavor flavor) { return flavor.equals(DataFlavor.javaFileListFlavor); }
                    @Override public Object getTransferData(DataFlavor flavor) { return null; }
                };
            }
            return null;
        }

        //Define las acciones permitidas (MOVER). Solo si es en modo "Personalizado".

        @Override
        public int getSourceActions(JComponent c) {
            return Objects.equals(comboBoxCriterioOrdenamiento.getSelectedItem(), "Personalizado (arrastrar y soltar)") ? DnDConstants.ACTION_MOVE : TransferHandler.NONE;
        }

        //Indica si se pueden importar los datos arrastrados en la ubicación actual.
        //Verifica tipo de datos y si el modo es "Personalizado".

        @Override
        public boolean canImport(TransferSupport support) {
            return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor) &&
                    (support.getDropLocation() instanceof JTable.DropLocation) &&
                    Objects.equals(comboBoxCriterioOrdenamiento.getSelectedItem(), "Personalizado (arrastrar y soltar)");
        }

        //Implementa la lógica de reordenamiento cuando los datos son soltados.

        @Override
        public boolean importData(TransferSupport support) {
            if (!canImport(support)) return false;
            JTable.DropLocation dl = (JTable.DropLocation) support.getDropLocation();
            int filaDestino = dl.getRow();

            indices = tabla.getSelectedRows();
            if (indices == null || indices.length == 0) return false;

            int filaOrigenEnModelo = tabla.convertRowIndexToModel(indices[0]); // Convierte índice de vista a modelo.
            int filaDestinoEnModelo = tabla.convertRowIndexToModel(filaDestino);

            if (filaOrigenEnModelo < filaDestinoEnModelo) { filaDestinoEnModelo--; } // Ajusta destino si se mueve hacia abajo.
            if (filaDestinoEnModelo < 0) { filaDestinoEnModelo = 0; }

            modelo.moverFila(filaOrigenEnModelo, filaDestinoEnModelo); // Realiza el movimiento en el modelo.

            tabla.getSelectionModel().clearSelection(); // Limpia y selecciona nueva posición.
            tabla.getSelectionModel().addSelectionInterval(filaDestinoEnModelo, filaDestinoEnModelo);

            return true;
        }

        //Limpieza después de que la operación de D&D ha terminado.

        @Override
        protected void exportDone(JComponent c, Transferable data, int action) {
            indices = null; // Resetea índices.
        }
    }
}