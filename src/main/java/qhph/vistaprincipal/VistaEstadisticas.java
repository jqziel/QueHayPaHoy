package qhph.vistaprincipal;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import qhph.vertareas.GestorTareas;
import qhph.vertareas.Tarea;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VistaEstadisticas extends JPanel {
    private GestorTareas gestorTareas;
    private ChartPanel panelGraficaEstado;
    private ChartPanel panelGraficaPrioridad;
    private static final Color COLOR_FONDO_OSCURO = new Color(43, 43, 43);
    private static final Color COLOR_TEXTO = new Color(220, 220, 220);
    private static final Color COLOR_CUADRICULA = new Color(80, 80, 80);

    public VistaEstadisticas(GestorTareas gestorTareas) {
        // Configuración de la ventana
        this.gestorTareas = gestorTareas;
        setLayout(new GridLayout(1, 2, 15, 15));
        setBackground(COLOR_FONDO_OSCURO);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mostrarGraficas();
    }
    // Muestra la gráfica
    private void mostrarGraficas() {
        panelGraficaEstado = new ChartPanel(mostrarGraficaEstado());
        add(panelGraficaEstado);

        panelGraficaPrioridad = new ChartPanel(mostrarGraficaPrioridad());
        add(panelGraficaPrioridad);
    }
    // Aplicar tema oscuro a JFreeChart para mantener cohesión en la estética de la App
    private void aplicarTemaOscuro(JFreeChart chart) {
        chart.setBackgroundPaint(COLOR_FONDO_OSCURO);
        chart.getTitle().setPaint(COLOR_TEXTO);
        if (chart.getLegend() != null) {
            chart.getLegend().setBackgroundPaint(COLOR_FONDO_OSCURO);
            chart.getLegend().setItemPaint(COLOR_TEXTO);
        }
        Plot plot = chart.getPlot();
        plot.setBackgroundPaint(COLOR_FONDO_OSCURO);
        plot.setOutlinePaint(null);
        if (plot instanceof CategoryPlot) {
            CategoryPlot categoryPlot = (CategoryPlot) plot;
            categoryPlot.setRangeGridlinePaint(COLOR_CUADRICULA);
            categoryPlot.getDomainAxis().setLabelPaint(COLOR_TEXTO);
            categoryPlot.getDomainAxis().setTickLabelPaint(COLOR_TEXTO);
            categoryPlot.getRangeAxis().setLabelPaint(COLOR_TEXTO);
            categoryPlot.getRangeAxis().setTickLabelPaint(COLOR_TEXTO);
        } else if (plot instanceof PiePlot) {
            PiePlot piePlot = (PiePlot) plot;
            piePlot.setLabelPaint(COLOR_TEXTO);
            piePlot.setLabelBackgroundPaint(null);
            piePlot.setLabelOutlinePaint(null);
            piePlot.setLabelShadowPaint(null);
        }
    }
    // Muestra la gráfica circular usando los estados de las tareas (completado o pendiente)
    private JFreeChart mostrarGraficaEstado() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        List<Tarea> tareas = gestorTareas.obtenerTareas();

        long tareasCompletadas = tareas.stream().filter(Tarea::isCompletada).count();
        long tareasPendientes = tareas.size() - tareasCompletadas;

        double porcentajeProductividad = (tareas.isEmpty()) ? 0 :
                ((double) tareasCompletadas / tareas.size()) * 100;

        String etiquetaCompletadas = String.format("Completadas (%.1f%%)", porcentajeProductividad);

        dataset.setValue(etiquetaCompletadas, tareasCompletadas);
        dataset.setValue("Pendientes", tareasPendientes);

        JFreeChart chart = ChartFactory.createPieChart(
                "Estado de Tareas",
                dataset,
                true,
                true,
                false
        );

        aplicarTemaOscuro(chart);

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionPaint(etiquetaCompletadas, new Color(66, 133, 244));
        plot.setSectionPaint("Pendientes", new Color(234, 67, 53));
        plot.setExplodePercent("Pendientes", 0.05);

        return chart;
    }

    // Muestra la gráfica de barras usando la prioridad de las tareas (Alta, media, baja)
    private JFreeChart mostrarGraficaPrioridad() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<Tarea> tareas = gestorTareas.obtenerTareas();

        if (tareas != null) {
            Map<String, Long> contadorPrioridades = tareas.stream()
                    .collect(Collectors.groupingBy(
                            Tarea::getPrioridad,
                            Collectors.counting()
                    ));

            dataset.addValue(contadorPrioridades.getOrDefault("Alta", 0L), "Tareas", "Alta");
            dataset.addValue(contadorPrioridades.getOrDefault("Media", 0L), "Tareas", "Media");
            dataset.addValue(contadorPrioridades.getOrDefault("Baja", 0L), "Tareas", "Baja");
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Tareas por Prioridad",
                "Prioridad",
                "Cantidad",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
        );
        aplicarTemaOscuro(chart);
        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(52, 168, 83));
        renderer.setBarPainter(new StandardBarPainter());

        return chart;
    }
    // Actualiza las gráficas según se van actualizando los estados de las tareas
    public void actualizarGraficas() {
        panelGraficaEstado.setChart(mostrarGraficaEstado());
        panelGraficaPrioridad.setChart(mostrarGraficaPrioridad());
    }
}