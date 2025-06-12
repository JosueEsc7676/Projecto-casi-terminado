package esfe.presentacion.GraficForm;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import esfe.persistencia.MovimientoDAO;

public class GastosMensualesChart extends JPanel {
    public GastosMensualesChart() {
        setLayout(new BorderLayout());

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<String, Double> gastos = MovimientoDAO.obtenerGastosPorMes();
        Map<String, Double> ingresos = MovimientoDAO.obtenerIngresosPorMes();

        for (Map.Entry<String, Double> entry : gastos.entrySet()) {
            dataset.addValue(entry.getValue(), "Egresos", entry.getKey());
        }

        for (Map.Entry<String, Double> entry : ingresos.entrySet()) {
            dataset.addValue(entry.getValue(), "Ingresos", entry.getKey());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Gastos e Ingresos Mensuales",
                "Mes",
                "Monto ($)",
                dataset
        );

        // ✅ Aplicar modo oscuro
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(30, 30, 30)); // Fondo oscuro
        plot.setDomainGridlinePaint(Color.GRAY); // Líneas de cuadrícula en gris
        plot.setRangeGridlinePaint(Color.GRAY);

        BarRenderer renderer = new BarRenderer();
        renderer.setSeriesPaint(0, new Color(255, 99, 71));  // Egresos en rojo
        renderer.setSeriesPaint(1, new Color(60, 179, 113)); // Ingresos en verde

        plot.setRenderer(renderer);

        // ✅ Ajustar colores de los ejes y el texto
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setTickLabelPaint(Color.WHITE);
        domainAxis.setLabelPaint(Color.WHITE);

        ValueAxis rangeAxis = plot.getRangeAxis();
        rangeAxis.setTickLabelPaint(Color.WHITE);
        rangeAxis.setLabelPaint(Color.WHITE);

        chart.setBackgroundPaint(new Color(20, 20, 20)); // Fondo general oscuro
        chart.getTitle().setPaint(Color.WHITE); // Color del título

        ChartPanel chartPanel = new ChartPanel(chart);
        add(chartPanel, BorderLayout.CENTER);
    }
}
