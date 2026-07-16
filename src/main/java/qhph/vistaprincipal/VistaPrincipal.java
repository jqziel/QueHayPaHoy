package qhph.vistaprincipal;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import qhph.vertareas.GestorTareas;
import qhph.vertareas.Tarea;
import qhph.vertareas.VistaListaTareas;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class VistaPrincipal extends JPanel {
    private JPanel panelPrincipal;
    private JTextField txtNombreTarea;
    private JTextArea txtAreaDescripcionTarea;
    private JFormattedTextField txtVencimientoTarea;
    private JFormattedTextField txtHoraVencimientoTarea;
    private JComboBox<String> cbPrioridadTarea;
    private JComboBox<String> cbMetodoEstudio;
    private GestorTareas gestorTareas;
    private JPanel panelDeRegistro;
    private VistaListaTareas panelDeTareas;
    private VistaEstadisticas panelDeGraficas;
    private String COLOR = "#5b5b5b";

    public VistaPrincipal() {
        this.gestorTareas = new GestorTareas();
        this.panelDeRegistro = mostrarPanelRegistro();
        this.panelDeTareas = new VistaListaTareas(this.gestorTareas);
        this.panelDeGraficas = new VistaEstadisticas(this.gestorTareas);
        inicializarLayout();
    }

     //Configura el diseño principal de la ventana usando MigLayout.
     //Añade topbar, sidebar y el área de contenido principal.

    private void inicializarLayout() {
        // Layout general
        setLayout(new MigLayout(" fill, insets 20", "[250!][grow]", "[pref!][grow]"));

        // Panel Principal: Área de contenido dinámico.
        panelPrincipal = new JPanel(new MigLayout("fill", "[fill]", "[fill]"));
        panelPrincipal.putClientProperty(FlatClientProperties.STYLE, "arc:20; background:$Panel.background;");

        // Añade topbar
        add(mostrarTopbar(), "cell 0 0, span 2, growx, h 60!, wrap, gapbottom 20");

        // Sidebar: Contiene menú y panel Pomodoro.
        JPanel sidebar = new JPanel(new MigLayout("wrap, fillx, aligny top", "[fill]"));
        sidebar.setOpaque(false);

        // Menú: Menú lateral con botones de navegación.
        JPanel menu = new JPanel(new MigLayout("wrap, fillx, insets 15, gapy 10", "[fill]"));
        menu.putClientProperty(FlatClientProperties.STYLE, "arc:20; background:" + COLOR + ";");

        JButton btnInicio = new JButton("Inicio");
        JButton btnTodasTareas = new JButton("Todas las tareas");
        JButton btnEstadisticas = new JButton("Estadísticas");

        // Listener para los botones
        btnInicio.addActionListener(e -> mostrarPanelPrincipal(panelDeRegistro));
        btnTodasTareas.addActionListener(e -> mostrarPanelPrincipal(panelDeTareas));
        //Listener para "Estadísticas": Muestra el panel de estadísticas en panelPrincipal.
        btnEstadisticas.addActionListener(e -> {
            panelDeGraficas.actualizarGraficas(); // Actualiza las gráficas justo antes de mostrarlas
            mostrarPanelPrincipal(panelDeGraficas);
        });

        menu.add(btnInicio, "h 40!");
        menu.add(btnTodasTareas, "h 40!");
        menu.add(btnEstadisticas, "h 40!");
        sidebar.add(menu, "growx, wrap, gapbottom 20"); // Añade sidebar al panel izquierdo.

        // Panel Pomodoro
        JPanel panelPomodoro = mostrarPanelPomodoro();
        sidebar.add(panelPomodoro, "growx, h 180!"); // Añade panel Pomodoro.

        // Añade panelIzquierdo a la VistaPrincipal.
        add(sidebar, "cell 0 1, growy, gapright 20");
        add(panelPrincipal, "cell 1 1, grow");

        // --- Vista Inicial ---
        mostrarPanelPrincipal(panelDeRegistro); // Muestra el panel de registro al iniciar
    }

    private void mostrarPanelPrincipal(Component panel) {
        panelPrincipal.removeAll();
        panelPrincipal.add(panel, "grow");
        panelPrincipal.revalidate();
        panelPrincipal.repaint();
    }

     // Crea y configura la barra superior de la aplicación.

    private JPanel mostrarTopbar() {
        JPanel topbar = new JPanel(new MigLayout("fill, insets 0", "[fill]"));
        topbar.putClientProperty(FlatClientProperties.STYLE, "arc: 20; background: #4e4e4e;");

        JPanel contenidoTopbar = new JPanel(new MigLayout("insets 10 20 10 20, fillx", "[][grow]"));
        contenidoTopbar.setOpaque(false);

        JLabel logo = new JLabel();
        try {
            URL logoUrl = getClass().getResource("/qhph/assets/logo.png");
            ImageIcon scaledLogo = escalarIcono(logoUrl, 40);
            if (scaledLogo != null) { logo.setIcon(scaledLogo); }
            else { logo.setText("L"); System.err.println("Logo no encontrado."); }
        } catch (Exception e) { logo.setText("L"); System.err.println("Error al cargar el logo: " + e.getMessage()); }

        JLabel title = new JLabel("¿Qué Hay Pa' Hoy?");
        title.putClientProperty(FlatClientProperties.STYLE, "font:bold +8");
        title.setForeground(Color.WHITE);
        topbar.add(logo);
        topbar.add(title, "aligny center");
        topbar.add(contenidoTopbar, "growx");

        return topbar;
    }

    //Carga y escala un icono desde una URL.

    private ImageIcon escalarIcono(URL url, int altura) {
        if (url == null) return null;
        ImageIcon iconoOriginal = new ImageIcon(url);
        Image imagenOriginal = iconoOriginal.getImage();
        Image imagenEscalada = imagenOriginal.getScaledInstance(-1, altura, Image.SCALE_SMOOTH);
        return new ImageIcon(imagenEscalada);
    }

     //Crea y configura un panel placeholder para el timer Pomodoro.

    private JPanel mostrarPanelPomodoro() {
        JPanel panelPomodoro = new JPanel(new MigLayout("fill, insets 15", "[center]", "[center]"));
        panelPomodoro.putClientProperty(FlatClientProperties.STYLE, "arc:20; background:" + COLOR + ";");
        JButton btnPomodoro = new JButton("Abrir Pomodoro");
        panelPomodoro.add(btnPomodoro, "h 40!");

        btnPomodoro.addActionListener(e -> SwingUtilities.invokeLater(Pomodoro::new));

        return panelPomodoro;
    }

     // Crea y configura el formulario para registrar nuevas tareas.

    private JPanel mostrarPanelRegistro() {
        JPanel panelRegistro = new JPanel(new MigLayout("wrap, fillx, aligny top", "[fill]", "[]"));
        panelRegistro.setOpaque(false);

        txtNombreTarea = new JTextField();
        txtAreaDescripcionTarea = new JTextArea();
        JScrollPane scrollDescripcion = new JScrollPane(txtAreaDescripcionTarea);
        scrollDescripcion.setPreferredSize(new Dimension(0, 100));

        txtVencimientoTarea = crearCampoFormatoFecha("##/##/####");
        txtHoraVencimientoTarea = crearCampoFormatoFecha("##:##");
        txtHoraVencimientoTarea.setText("12:00");

        cbPrioridadTarea = new JComboBox<>(new String[]{"Alta", "Media", "Baja", "Inferir (Beta)"});
        cbPrioridadTarea.setSelectedIndex(0);

        cbMetodoEstudio = new JComboBox<>(new String[]{"Sí", "No", "Inferir (Beta)"});
        cbMetodoEstudio.setSelectedIndex(0);

        JLabel lbTitulo = new JLabel("Registrar nueva tarea");
        lbTitulo.putClientProperty(FlatClientProperties.STYLE, "font:bold +10");
        JLabel lbDescripcion = new JLabel("Ingresa los datos de la nueva tarea a continuación:");

        JButton btnRegistrar = new JButton("Registrar Tarea");
        btnRegistrar.addActionListener(e -> registrarTarea());

        // Añade componentes al panel con sus restricciones de MigLayout.
        panelRegistro.add(lbTitulo, "gapy 8");
        panelRegistro.add(lbDescripcion, "gapy 8");
        panelRegistro.add(new JLabel("Nombre:"), "gapy 8");
        panelRegistro.add(txtNombreTarea);
        panelRegistro.add(new JLabel("Descripción:"), "gapy 8");
        panelRegistro.add(scrollDescripcion, "growx, h 100::200");
        panelRegistro.add(new JLabel("Fecha de vencimiento (dd/MM/yyyy):"), "gapy 8");
        panelRegistro.add(txtVencimientoTarea);
        panelRegistro.add(new JLabel("Hora de vencimiento (HH:mm):"), "gapy 8");
        panelRegistro.add(txtHoraVencimientoTarea);
        panelRegistro.add(new JLabel("Prioridad:"), "gapy 8");
        panelRegistro.add(cbPrioridadTarea);
        panelRegistro.add(new JLabel("¿Recomendar Pomodoro?"), "gapy 8");
        panelRegistro.add(cbMetodoEstudio);
        panelRegistro.add(btnRegistrar, "gapy 15, h 40!");

        return panelRegistro;
    }

     //Crea un JFormattedTextField con la máscara especificada.

    private JFormattedTextField crearCampoFormatoFecha(String formato) {
        try {
            MaskFormatter mask = new MaskFormatter(formato);
            mask.setPlaceholderCharacter(' ');
            return new JFormattedTextField(mask);
        } catch (ParseException e) {
            return new JFormattedTextField(); // Retorna campo sin formato si hay error en máscara.
        }
    }

    //Valida datos del formulario, infiere atributos, crea y agrega la tarea, limpia el formulario.

    private void registrarTarea() {
        String nombre = txtNombreTarea.getText().trim();
        String descripcion = txtAreaDescripcionTarea.getText().trim();
        String fechaStr = txtVencimientoTarea.getText().trim();
        String horaStr = txtHoraVencimientoTarea.getText().trim();

        if (nombre.isEmpty()) { // Validación de nombre obligatorio.
            JOptionPane.showMessageDialog(this, "El nombre de la tarea es obligatorio.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validación y parseo de fecha y hora.
        boolean fechaVacia = fechaStr.replace(" ", "").isEmpty();
        boolean horaVacia = horaStr.replace(" ", "").isEmpty();

        if (fechaVacia) {
            JOptionPane.showMessageDialog(this, "La fecha de vencimiento no puede estar vacía.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (horaVacia) {
            JOptionPane.showMessageDialog(this, "La hora de vencimiento no puede estar vacía.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDateTime fechaHoraVencimiento = parseFechaHora(fechaStr, horaStr); // Parseo y manejo de errores de formato.
        if (fechaHoraVencimiento == null) { // Si hay error de formato, sale.
            return;
        }

        String prioridadSeleccionada = (String) cbPrioridadTarea.getSelectedItem();
        String metodoEstudioUI = (String) cbMetodoEstudio.getSelectedItem();
        String prioridadFinal, metodoEstudioFinal;

        // Lógica de inferencia o uso de valores seleccionados manualmente.
        boolean inferirPrioridad = "Inferir (Beta)".equals(prioridadSeleccionada);
        boolean inferirMetodoEstudio = "Inferir (Beta)".equals(metodoEstudioUI);

        if (inferirPrioridad || inferirMetodoEstudio) {
            LocalDate fechaVencimientoSoloFecha = fechaHoraVencimiento.toLocalDate();
            GestorTareas.AtributosInferidos inferidos = gestorTareas.inferirAtributos(descripcion, fechaVencimientoSoloFecha);
            prioridadFinal = inferirPrioridad ? inferidos.prioridad() : prioridadSeleccionada;
            metodoEstudioFinal = inferirMetodoEstudio ? inferidos.metodoEstudio() : ("Sí".equals(metodoEstudioUI) ? "Pomodoro" : "Ninguno");
        } else {
            prioridadFinal = prioridadSeleccionada;
            metodoEstudioFinal = "Sí".equals(metodoEstudioUI) ? "Pomodoro" : "Ninguno";
        }
        // Actualiza la tarea con los nuevos valores.
        gestorTareas.agregarTarea(new Tarea(nombre, descripcion, fechaHoraVencimiento, prioridadFinal, metodoEstudioFinal));

        panelDeTareas.actualizarDatosTabla();
        panelDeGraficas.actualizarGraficas();

        limpiarFormulario();

        JOptionPane.showMessageDialog(this,
                String.format("Tarea registrada con éxito:\n- Nombre: %s\n- Prioridad: %s\n- ¿Recomendar Pomodoro?: %s",
                        nombre, prioridadFinal, ("Pomodoro".equals(metodoEstudioFinal) ? "Sí" : "No")),
                "Tarea Registrada", JOptionPane.INFORMATION_MESSAGE);
    }

     //Parsea cadenas de fecha y hora a LocalDateTime; muestra errores si el formato es inválido.

    private LocalDateTime parseFechaHora(String fechaStr, String horaStr) { // Rellena espacios para parseo.
        fechaStr = fechaStr.replace(' ', '0');
        horaStr = horaStr.replace(' ', '0');
        LocalDate fecha;
        LocalTime hora;
        try {
            fecha = LocalDate.parse(fechaStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "El formato de la fecha es incorrecto. Use DD/MM/AAAA.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        try {
            hora = LocalTime.parse(horaStr, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "El formato de la hora es incorrecto. Use HH:MM.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return LocalDateTime.of(fecha, hora);
    }
    
     //Restablece todos los campos del formulario a sus valores iniciales.
    
    private void limpiarFormulario() {
        txtNombreTarea.setText("");
        txtAreaDescripcionTarea.setText("");
        txtVencimientoTarea.setValue(null);
        txtHoraVencimientoTarea.setText("12:00");
        cbPrioridadTarea.setSelectedIndex(0);
        cbMetodoEstudio.setSelectedIndex(0);
    }
}