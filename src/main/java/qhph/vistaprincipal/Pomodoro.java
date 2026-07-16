package qhph.vistaprincipal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Pomodoro extends JFrame {

    private static final int TIEMPO_TRABAJO = 25 * 60;
    private static final int TIEMPO_DESCANSO = 5 * 60;
    private boolean estaCorriendo = false;
    private boolean enSesionDeTrabajo = true;
    private int tiempoRestante = TIEMPO_TRABAJO;
    private Timer temporizador;
    private JLabel lbTemporizador;
    private JButton btnIniciar;
    private JButton btnPausar;
    private JButton btnReiniciar;
    private JButton btnForzarDescanso;
    private JLabel lbSesion;

    public Pomodoro() {
        // Configuración de la ventana
        setTitle("Temporizador Pomodoro");
        setSize(450, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(30, 30, 40));

        // Etiqueta del temporizador
        lbTemporizador = new JLabel(formatearTiempo(tiempoRestante), SwingConstants.CENTER);
        lbTemporizador.setFont(new Font("SansSerif", Font.BOLD, 48));
        lbTemporizador.setForeground(Color.WHITE);
        add(lbTemporizador, BorderLayout.CENTER);

        // Etiqueta de la sesión
        lbSesion = new JLabel("Tiempo de trabajo", SwingConstants.CENTER);
        lbSesion.setFont(new Font("SansSerif", Font.PLAIN, 18));
        lbSesion.setForeground(Color.LIGHT_GRAY);
        add(lbSesion, BorderLayout.NORTH);

        // Panel de botones
        JPanel panelDeBotones = new JPanel();
        panelDeBotones.setBackground(new Color(30, 30, 40));
        btnIniciar = new JButton("Iniciar");
        btnPausar = new JButton("Pausar");
        btnReiniciar = new JButton("Reiniciar");
        btnForzarDescanso = new JButton("Forzar Descanso");
        panelDeBotones.add(btnIniciar);
        panelDeBotones.add(btnPausar);
        panelDeBotones.add(btnReiniciar);
        panelDeBotones.add(btnForzarDescanso);
        add(panelDeBotones, BorderLayout.SOUTH);

        // Acciones de los botones
        btnIniciar.addActionListener(e -> iniciarTemporizador());
        btnPausar.addActionListener(e -> pausarTemporizador());
        btnReiniciar.addActionListener(e -> reiniciarTemporizador());
        btnForzarDescanso.addActionListener(e -> forzarDescanso());

        // Lógica del temporizador
        temporizador = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tiempoRestante--;
                lbTemporizador.setText(formatearTiempo(tiempoRestante));

                if (tiempoRestante <= 0) {
                    temporizador.stop();
                    estaCorriendo = false;
                    Toolkit.getDefaultToolkit().beep();

                    if (enSesionDeTrabajo) {
                        enSesionDeTrabajo = false;
                        tiempoRestante = TIEMPO_DESCANSO;
                        lbSesion.setText("Tiempo de descansoo");
                        JOptionPane.showMessageDialog(null, "¡Hora del descanso!");
                    } else {
                        enSesionDeTrabajo = true;
                        tiempoRestante = TIEMPO_TRABAJO;
                        lbSesion.setText("Tiempo de trabajo");
                        JOptionPane.showMessageDialog(null, "¡Hora de trabajar!");
                    }
                    lbTemporizador.setText(formatearTiempo(tiempoRestante));
                }
            }
        });

        setVisible(true);
    }
    //Lógica del botón Iniciar
    private void iniciarTemporizador() {
        if (!estaCorriendo) {
            temporizador.start();
            estaCorriendo = true;
        }
    }
    //Lógica del botón Pausar
    private void pausarTemporizador() {
        if (estaCorriendo) {
            temporizador.stop();
            estaCorriendo = false;
        }
    }
    //Lógica del botón Reiniciar
    private void reiniciarTemporizador() {
        temporizador.stop();
        estaCorriendo = false;
        enSesionDeTrabajo = true;
        tiempoRestante = TIEMPO_TRABAJO;
        lbTemporizador.setText(formatearTiempo(tiempoRestante));
        lbSesion.setText("Tiempo de trabajo");
    }
    //Lógica del botón Forzar Descanso
    private void forzarDescanso() {
        temporizador.stop();
        enSesionDeTrabajo = false;
        tiempoRestante = TIEMPO_DESCANSO;
        estaCorriendo = true;
        lbSesion.setText("Tiempo de descanso");
        lbTemporizador.setText(formatearTiempo(tiempoRestante));
        temporizador.start();
    }
    //Formatear tiempo en minutos y segundos
    private String formatearTiempo(int totalSeconds) {
        int min = totalSeconds / 60;
        int sec = totalSeconds % 60;
        return String.format("%02d:%02d", min, sec);
    }
}