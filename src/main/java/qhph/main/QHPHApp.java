package qhph.main;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import qhph.vistaprincipal.Bienvenida;

import javax.swing.*;
import java.awt.*;

public class QHPHApp extends JFrame {

    public static void main(String[] args) {
        //Instalación de fuentes utilizándolos .properties
        FlatRobotoFont.install();
        FlatLaf.registerCustomDefaultsSource("qhph.temas");
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        FlatMacDarkLaf.setup();
        EventQueue.invokeLater(() -> new Bienvenida().setVisible(true)); //Mostrar la Bienvenida
    }
}
