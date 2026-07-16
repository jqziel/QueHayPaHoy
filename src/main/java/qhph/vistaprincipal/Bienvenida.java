package qhph.vistaprincipal;

import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL; // Necesario para cargar recursos de imagen

public class Bienvenida extends JFrame {

    public Bienvenida() {
        // --- Configuración general de la ventana ---
        setTitle("¿Qué Hay Pa' Hoy?");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(20, 20, 25));

        // --- Configuración de MigLayout para el JFrame principal ---
        String layoutConstraints = "fill, insets 0";
        String columnConstraints = "[push, grow][pref!, center][push, grow]";
        String rowConstraints = "[push, grow][pref!][pref!][pref!][push, grow][pref!][push, grow]";
        setLayout(new MigLayout(layoutConstraints, columnConstraints, rowConstraints));

        // --- Componentes ---
        // 1. Panel de Logos (Contiene los tres logos horizontalmente centrados)
        JPanel panelLogos = mostrarPanelDeLogos();
        add(panelLogos, "cell 1 1, alignx center");

        // 2. Título principal (Centrado)
        JLabel titulo = new JLabel("¿Qué Hay Pa' Hoy?");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 36));
        add(titulo, "cell 1 2, alignx center, gaptop 20");

        // 3. Panel de Texto (Contiene el subtítulo y los datos del grupo)
        JPanel panelTexto = mostrarPanelDeTexto();
        add(mostrarPanelDeTexto(), "cell 1 3, alignx center, gaptop 20");

        // 4. Botón "Empezar" (Centrado y en la parte inferior)
        JButton empezarBtn = new JButton("Empezar");
        empezarBtn.setFont(new Font("SansSerif", Font.BOLD, 18));
        empezarBtn.setPreferredSize(new Dimension(200, 50));
        empezarBtn.setBackground(new Color(60, 60, 70));
        empezarBtn.setForeground(Color.WHITE);
        empezarBtn.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        empezarBtn.setFocusPainted(false);

        // Efecto de hover para el botón
        empezarBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                empezarBtn.setBackground(new Color(80, 80, 90));
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                empezarBtn.setBackground(new Color(60, 60, 70));
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        add(empezarBtn, "cell 1 5, alignx center, gaptop 50"); // gaptop para espacio desde el texto

        // Acción al hacer clic en el botón "Empezar"
        empezarBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirVistaPrincipal();
            }
        });
    }
    
     //Crea un panel que contiene los tres logos alineados horizontalmente.

    private JPanel mostrarPanelDeLogos() {
        JPanel panelLogos = new JPanel(new MigLayout("insets 0", "[grow][pref!][40][pref!][40][pref!][grow]", "pref!"));
        panelLogos.setOpaque(false);
        int alturaLogo = 120;

        // Cargar y escalar el logo de la App
        ImageIcon iconoApp = cargarYEscalarIcono("/qhph/assets/logo.png", alturaLogo);
        JLabel logoApp = new JLabel(iconoApp);
        
        panelLogos.add(logoApp, "cell 3 0");

        return panelLogos;
    }
    
     // Crea un panel que contiene el subtítulo y los datos del grupo,
     // ambos alineados al centro dentro de este panel.

    private JPanel mostrarPanelDeTexto() {
        JPanel panelTexto = new JPanel();
        panelTexto.setLayout(new BoxLayout(panelTexto, BoxLayout.Y_AXIS)); // BoxLayout para apilar verticalmente
        panelTexto.setOpaque(false); 

        // Subtítulo
        JLabel subtituloLinea1 = new JLabel("Organiza tu día.");
        subtituloLinea1.setForeground(new Color(180, 180, 180));
        subtituloLinea1.setFont(new Font("SansSerif", Font.PLAIN, 18));
        subtituloLinea1.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel subtituloLinea2 = new JLabel("Maximiza tu enfoque.");
        subtituloLinea2.setForeground(new Color(180, 180, 180));
        subtituloLinea2.setFont(new Font("SansSerif", Font.PLAIN, 18));
        subtituloLinea2.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelTexto.add(subtituloLinea1);
        panelTexto.add(subtituloLinea2);
        panelTexto.add(Box.createVerticalStrut(20));

        // Datos institucionales del grupo
        JLabel datos = new JLabel("<html><div style='text-align: center;'>" 
                + "Proyecto académico desarrollado en Java. <br><br>"
                + "Versión 1.0. <br><br>"
                + "</div></html>");
        datos.setForeground(new Color(180, 180, 180));
        datos.setFont(new Font("SansSerif", Font.PLAIN, 14));
        datos.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelTexto.add(datos);

        return panelTexto;
    }
    
     //Carga y escala un icono desde una URL.
    private ImageIcon cargarYEscalarIcono(String path, int altura) {
        ImageIcon iconoEscalado = null;
        try {
            URL url = getClass().getResource(path);
            if (url != null) {
                ImageIcon iconoOriginal = new ImageIcon(url);
                Image imagenOriginal = iconoOriginal.getImage();
                Image scaledImage = imagenOriginal.getScaledInstance(-1, altura, Image.SCALE_SMOOTH);
                iconoEscalado = new ImageIcon(scaledImage);
            } else {
                System.err.println("Error: No se encontró la imagen en " + path);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar o escalar la imagen " + path + ": " + e.getMessage());
        }
        return iconoEscalado;
    }
    // Método para abrir la ventana de la vista principal y cerrar la de bienvenida
    private void abrirVistaPrincipal() {
        dispose();
        JFrame vistaPrincipal = new JFrame("¿Qué Hay Pa' Hoy?");
        vistaPrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        vistaPrincipal.setExtendedState(JFrame.MAXIMIZED_BOTH);
        vistaPrincipal.setContentPane(new VistaPrincipal());
        vistaPrincipal.setVisible(true);
    }
}