package qhph.vertareas; // Paquete de clases de gestión de tareas.
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import javax.swing.text.MaskFormatter; // Para campos de texto con formatos (fechas/horas).
import java.awt.*; // Clases básicas de AWT.
import java.text.ParseException; // Manejo de errores de formato.
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter; // Formato de fechas/horas.
import java.time.format.DateTimeParseException; // Errores al parsear fechas/horas.

public class DialogoEditarTarea extends JDialog {
    private Tarea tarea;
    private GestorTareas gestorTareas;
    private JTextField txtNombre;
    private JTextArea txtDescripcion;
    private JFormattedTextField txtFechaVencimiento;
    private JFormattedTextField txtHoraVencimiento;
    private JComboBox<String> comboPrioridad; // Usamos JComboBox para que se muestre como una lista desplegable.
    private JComboBox<String> comboMetodoEstudio;
    private boolean tareaGuardada = false; // Bandera para ver si se guardó o no la tarea.

    /**
     * Constructor del diálogo. Configura la ventana y carga los datos de la tarea.
     */
    public DialogoEditarTarea(Window owner, Tarea tarea, GestorTareas gestorTareas) {
        super(owner, "Editar Tarea", ModalityType.APPLICATION_MODAL); // Esto se usa para que no se pueda interactuar con otra ventana que no sea "editar tarea".
        this.tarea = tarea;
        this.gestorTareas = gestorTareas;
        inicializarComponentes(); // Inicializa elementos de la UI.
        llenarCampos(); // Rellena campos con datos de la tarea.
        pack(); // Ajusta tamaño del diálogo, para que se muestre del tamaño necesario.
        setLocationRelativeTo(owner); // Centra el diálogo, con referencia a la ventana padre.
    }


    // inicializa los componentes UI, usando el MIG.

    private void inicializarComponentes() {
        setLayout(new MigLayout("insets 15", "[][grow]", "[][][][][][]")); // Configura el layout del diálogo.

        // Fila 1: Nombre
        add(new JLabel("Nombre:"), "");
        txtNombre = new JTextField(20);
        add(txtNombre, "wrap, growx");

        // Fila 2: Descripción
        add(new JLabel("Descripción:"), "");
        txtDescripcion = new JTextArea(3, 20);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        JScrollPane scrollDescripcion = new JScrollPane(txtDescripcion);
        add(scrollDescripcion, "grow, wrap");

        // Fila 3: Fecha Vencimiento
        add(new JLabel("Fecha Vencimiento (DD/MM/AAAA):"), "");
        try {
            MaskFormatter mask = new MaskFormatter("##/##/####");
            mask.setPlaceholderCharacter(' ');
            txtFechaVencimiento = new JFormattedTextField(mask);
            txtFechaVencimiento.setColumns(10);
        } catch (ParseException e) {
            txtFechaVencimiento = new JFormattedTextField();
        }
        add(txtFechaVencimiento, "wrap, growx");

        // Fila 4: Hora Vencimiento
        add(new JLabel("Hora Vencimiento (HH:MM):"), "");
        try {
            MaskFormatter mask = new MaskFormatter("##:##");
            mask.setPlaceholderCharacter(' ');
            txtHoraVencimiento = new JFormattedTextField(mask);
            txtHoraVencimiento.setColumns(5);
        } catch (ParseException e) {
            txtHoraVencimiento = new JFormattedTextField();
        }
        add(txtHoraVencimiento, "wrap, growx");

        // Fila 5: Prioridad
        add(new JLabel("Prioridad:"), "");
        comboPrioridad = new JComboBox<>(new String[]{"Baja", "Media", "Alta", "Inferir (Beta)"});
        add(comboPrioridad, "wrap, growx");

        // Fila 6: Método de Estudio
        add(new JLabel("Usar Pomodoro:"), "");
        comboMetodoEstudio = new JComboBox<>(new String[]{"Sí", "No", "Inferir (Beta)"});
        add(comboMetodoEstudio, "wrap, growx");

        // Fila 7: Panel de Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> guardarTarea()); // Llama a guardarTarea al hacer clic.
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose()); // Cierra el diálogo al hacer clic.
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        add(panelBotones, "span 2, growx"); // Ocupa 2 columnas y se estira.
    }


     //Rellena los campos de la UI con los datos de la tarea.


    private void llenarCampos() {
        txtNombre.setText(tarea.getNombre());
        txtDescripcion.setText(tarea.getDescripcion());

        if (tarea.getFechaVencimiento() != null) {
            txtFechaVencimiento.setText(tarea.getFechaVencimiento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            txtHoraVencimiento.setText(tarea.getFechaVencimiento().format(DateTimeFormatter.ofPattern("HH:mm")));
        } else {
            txtHoraVencimiento.setText("12:00");
        }
        comboPrioridad.setSelectedItem(tarea.getPrioridad());
        comboMetodoEstudio.setSelectedItem("Pomodoro".equals(tarea.getMetodoEstudio()) ? "Sí" : "No");
    }

    /**
     * Valida los datos, infiere atributos si es necesario y actualiza la tarea.
     * Se llama al hacer clic en el botón "Guardar".
     */
    private void guardarTarea() {
        String nombre = txtNombre.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        String fechaStr = txtFechaVencimiento.getText().trim();
        String horaStr = txtHoraVencimiento.getText().trim();
        LocalDateTime fechaHoraVencimiento;

        if (nombre.isEmpty()) { // Validación de nombre vacío.
            JOptionPane.showMessageDialog(this, "El nombre de la tarea no puede estar vacío.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validación y parseo de fecha/hora.
        if (fechaStr.replace(" ", "").isEmpty()) {
            JOptionPane.showMessageDialog(this, "La fecha de vencimiento no puede estar vacía.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (horaStr.replace(" ", "").isEmpty()) {
            JOptionPane.showMessageDialog(this, "La hora de vencimiento no puede estar vacía.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }
        fechaHoraVencimiento = parseFechaHora(fechaStr, horaStr); // Intenta parsear y muestra errores.
        if (fechaHoraVencimiento == null) return; // Comprueba si hay error de formato

        String prioridadSeleccionada = (String) comboPrioridad.getSelectedItem();
        String metodoEstudioUI = (String) comboMetodoEstudio.getSelectedItem();
        String prioridadFinal;
        String metodoEstudioFinal;

        // Lógica de inferencia o uso manual de prioridad y método de estudio.
        boolean inferirPrioridad = "Inferir (Beta)".equals(prioridadSeleccionada);
        boolean inferirMetodoEstudio = "Inferir (Beta)".equals(metodoEstudioUI);

        if (inferirPrioridad || inferirMetodoEstudio) {
            GestorTareas.AtributosInferidos inferidos = gestorTareas.inferirAtributos(descripcion, fechaHoraVencimiento.toLocalDate());
            prioridadFinal = inferirPrioridad ? inferidos.prioridad() : prioridadSeleccionada;
            metodoEstudioFinal = inferirMetodoEstudio ? inferidos.metodoEstudio() : ("Sí".equals(metodoEstudioUI) ? "Pomodoro" : "Ninguno");
        } else {
            prioridadFinal = prioridadSeleccionada;
            metodoEstudioFinal = "Sí".equals(metodoEstudioUI) ? "Pomodoro" : "Ninguno";
        }

        if (metodoEstudioFinal == null || metodoEstudioFinal.isEmpty()) metodoEstudioFinal = "Ninguno";

        // Actualiza los atributos de la tarea.
        tarea.setNombre(nombre);
        tarea.setDescripcion(descripcion);
        tarea.setFechaVencimiento(fechaHoraVencimiento);
        tarea.setPrioridad(prioridadFinal);
        tarea.setMetodoEstudio(metodoEstudioFinal);

        tareaGuardada = true; // Indica que la tarea se guardó.
        dispose(); // Cierra el diálogo.
    }


     //Convierte cadenas de fecha/hora a un objeto LocalDateTime, mostrando errores de formato.

    private LocalDateTime parseFechaHora(String fechaStr, String horaStr) {
        fechaStr = fechaStr.replace(' ', '0'); // Rellena espacios de MaskFormatter.
        horaStr = horaStr.replace(' ', '0');

        LocalDate fecha;
        LocalTime hora;

        try {
            fecha = LocalDate.parse(fechaStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "El formato de la fecha de vencimiento es incorrecto. Use DD/MM/AAAA.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        try {
            hora = LocalTime.parse(horaStr, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "El formato de la hora de vencimiento es incorrecto. Use HH:MM.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return LocalDateTime.of(fecha, hora);
    }


     // Bandera que indica si los cambios en la tarea fueron guardados.

    public boolean esTareaGuardada() { return tareaGuardada; }
}