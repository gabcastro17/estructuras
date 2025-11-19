import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class PruebaCalculadora extends JFrame {

    private List<JTextField> camposNota = new ArrayList<>();
    private List<JTextField> camposPeso = new ArrayList<>();
    private List<JPanel> panelesFila = new ArrayList<>();

    private JPanel panelNotas;
    private int contadorNotas = 1;

    // ===== PALETA DE COLORES =====
    private final Color COLOR_FONDO = new Color(250, 248, 255);
    private final Color MORADO = new Color(123, 44, 191);
    private final Color MORADO_OSCURO = new Color(90, 24, 154);
    private final Color NARANJA = new Color(255, 133, 0);
    private final Color ROJO_CERRAR = new Color(220, 50, 50); // Color para el botón de cerrar
    private final Color BLANCO = Color.WHITE;
    private boolean modoOscuro = false;
    // Colores oscuros
    private final Color FONDO_OSCURO = new Color(30, 30, 30);
    private final Color PANEL_OSCURO = new Color(45, 45, 45);
    private final Color TEXTO_CLARO = new Color(230, 230, 230);
    private final Color BORDE_OSCURO = new Color(80, 80, 80);
    private final Color CERRAR_OSCURO = new Color(150, 30, 30); // Color de cerrar en modo oscuro


    public PruebaCalculadora() {
        setTitle("Calculadora de Promedio de Notas ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Esto termina la aplicación al cerrar con la 'X'
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(COLOR_FONDO);

        // ===== TÍTULO BONITO =====
        JLabel titulo = new JLabel("✨ Calculadora de Notas ✨", SwingConstants.CENTER);
        titulo.setFont(new Font("Helvetica", Font.BOLD, 26));
        titulo.setForeground(MORADO_OSCURO);
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(titulo, BorderLayout.NORTH);

        // ===== PANEL DE NOTAS =====
        panelNotas = new JPanel();
        panelNotas.setLayout(new BoxLayout(panelNotas, BoxLayout.Y_AXIS));
        panelNotas.setBackground(COLOR_FONDO);

        JScrollPane scroll = new JScrollPane(panelNotas);
        scroll.setBorder(BorderFactory.createLineBorder(MORADO, 3));
        scroll.getViewport().setBackground(COLOR_FONDO);

        add(scroll, BorderLayout.CENTER);

        // ===== PANEL INFERIOR (Botones) =====
        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(COLOR_FONDO);
        panelInferior.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JButton botonAgregar = crearBoton("➕ Agregar nota", Color.BLACK);
        botonAgregar.setForeground(Color.WHITE);
        JButton botonCalcular = crearBoton("⭐ Calcular promedio", NARANJA);
        botonCalcular.setForeground(Color.WHITE);
        JButton botonTema = crearBoton("🌙 Modo oscuro", MORADO);
        botonTema.setForeground(Color.WHITE);
        
        // **********************************************
        // NUEVO BOTÓN DE CERRAR CON DISPOSE
        JButton botonCerrar = crearBoton("❌ Cerrar", ROJO_CERRAR);
        botonCerrar.setForeground(BLANCO); 
        botonCerrar.setName("BotonCerrar"); // Nombre para gestionar el tema oscuro
        botonCerrar.addActionListener(e -> {
            // Llama a dispose() para cerrar solo esta ventana.
            this.dispose(); 
        });
        // **********************************************

        botonTema.addActionListener(e -> cambiarTema(botonTema));
        
        botonAgregar.addActionListener(e -> agregarFilaNota(null, null));
        botonCalcular.addActionListener(e -> calcularPromedio());

        panelInferior.add(botonAgregar);
        panelInferior.add(botonCalcular);
        panelInferior.add(botonTema);
        panelInferior.add(botonCerrar); // Se añade el nuevo botón

        add(panelInferior, BorderLayout.SOUTH);

        // ===== INICIAR CON 2 FILAS =====
        agregarFilaNota(null, null);
        agregarFilaNota(null, null);

        pack();
        setSize(650, 650);
        setLocationRelativeTo(null);
    }

    // ===== BOTÓN ESTILIZADO =====
    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Helvetica", Font.BOLD, 14));
        boton.setForeground(Color.WHITE);
        boton.setBackground(color);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return boton;
    }

    // ===== AGREGAR FILA =====
    private void agregarFilaNota(Double notaInicial, Double pesoInicial) {
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 8));
        
        // Aplicar estilos según el tema actual
        Color colorFila = modoOscuro ? PANEL_OSCURO : BLANCO;
        Color colorBorde = modoOscuro ? BORDE_OSCURO : MORADO_OSCURO;
        Color colorTexto = modoOscuro ? TEXTO_CLARO : MORADO_OSCURO;

        fila.setBackground(colorFila);
        fila.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorBorde, 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel lbl = new JLabel("Nota " + contadorNotas);
        lbl.setFont(new Font("Helvetica", Font.BOLD, 14));
        lbl.setForeground(colorTexto);

        JTextField campoNota = new JTextField(5);
        campoNota.setFont(new Font("Helvetica", Font.PLAIN, 14));
        campoNota.setBorder(BorderFactory.createLineBorder(MORADO, 2));
        campoNota.setBackground(BLANCO);
        campoNota.setForeground(Color.BLACK);
        if (notaInicial != null) campoNota.setText(String.valueOf(notaInicial));

        JTextField campoPeso = new JTextField(3);
        campoPeso.setFont(new Font("Helvetica", Font.PLAIN, 14));
        campoPeso.setBorder(BorderFactory.createLineBorder(NARANJA, 2));
        campoPeso.setBackground(BLANCO);
        campoPeso.setForeground(Color.BLACK);
        if (pesoInicial != null) campoPeso.setText(String.valueOf(pesoInicial));

        JLabel lblPorcentaje = new JLabel("%");
        lblPorcentaje.setFont(new Font("Helvetica", Font.BOLD, 14));
        lblPorcentaje.setForeground(colorTexto);

        JButton btnEliminar = crearBoton("❌", NARANJA);
        btnEliminar.setPreferredSize(new Dimension(55, 30));
        btnEliminar.addActionListener(e -> eliminarFilaNota(fila, campoNota, campoPeso));

        fila.add(lbl);
        fila.add(campoNota);
        fila.add(campoPeso);
        fila.add(lblPorcentaje);
        fila.add(btnEliminar);

        camposNota.add(campoNota);
        camposPeso.add(campoPeso);
        panelesFila.add(fila);

        panelNotas.add(fila);
        contadorNotas++;

        renumerarNotas();
        panelNotas.revalidate();
        panelNotas.repaint();
    }

    // ===== ELIMINAR FILA =====
    private void eliminarFilaNota(JPanel fila, JTextField nota, JTextField peso) {
        panelNotas.remove(fila);
        camposNota.remove(nota);
        camposPeso.remove(peso);
        panelesFila.remove(fila);

        contadorNotas--;
        renumerarNotas();

        panelNotas.revalidate();
        panelNotas.repaint();
    }

    // ===== RE-NUMERAR FILAS =====
    private void renumerarNotas() {
        Color colorTexto = modoOscuro ? TEXTO_CLARO : MORADO_OSCURO;
        for (int i = 0; i < panelesFila.size(); i++) {
            JPanel fila = panelesFila.get(i);
            // El JLabel de la nota está en la posición 0 de la fila
            JLabel lbl = (JLabel) fila.getComponent(0); 
            lbl.setText("Nota " + (i + 1));
            // Asegurarse que el texto mantenga el color del tema
            lbl.setForeground(colorTexto); 
            
            // El JLabel de porcentaje está en la posición 3
            JLabel lblPorcentaje = (JLabel) fila.getComponent(3); 
            lblPorcentaje.setForeground(colorTexto);
        }
    }

    // ===== CAMBIAR TEMA =====
    private void cambiarTema(JButton botonTema) {
        modoOscuro = !modoOscuro;

        Color fondo;
        Color panel;
        Color texto;
        Color borde;
        Color colorCerrar;

        if (modoOscuro) {
            fondo = FONDO_OSCURO;
            panel = PANEL_OSCURO;
            texto = TEXTO_CLARO;
            borde = BORDE_OSCURO;
            colorCerrar = CERRAR_OSCURO;
            botonTema.setText("☀️ Modo claro");
            // Cambiar color del título
            ((JLabel) getContentPane().getComponent(0)).setForeground(TEXTO_CLARO);
        } else {
            fondo = COLOR_FONDO;
            panel = BLANCO;
            texto = MORADO_OSCURO; // Usamos MORADO_OSCURO para los títulos y labels en modo claro
            borde = MORADO_OSCURO;
            colorCerrar = ROJO_CERRAR;
            botonTema.setText("🌙 Modo oscuro");
            // Cambiar color del título
            ((JLabel) getContentPane().getComponent(0)).setForeground(MORADO_OSCURO);
        }
        
        // Buscar el botón de cerrar en el panel inferior y actualizar su color
        for (Component c : ((JPanel) getContentPane().getComponent(2)).getComponents()) {
            if (c instanceof JButton && "BotonCerrar".equals(c.getName())) {
                c.setBackground(colorCerrar);
            }
        }
        
        // Fondo general
        getContentPane().setBackground(fondo);
        
        // ScrollPane
        ((JScrollPane) getContentPane().getComponent(1)).getViewport().setBackground(fondo);
        
        // Panel de notas
        panelNotas.setBackground(fondo);
        
        // Panel inferior (botones)
        ((JPanel) getContentPane().getComponent(2)).setBackground(fondo);
        
        // Cambiar tema a cada fila
        for (JPanel fila : panelesFila) {
            fila.setBackground(panel);
            fila.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(borde, 2),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));

            // Actualizar colores de los componentes internos de la fila
            for (Component c : fila.getComponents()) {
                if (c instanceof JLabel) c.setForeground(texto);
                if (c instanceof JTextField) {
                    c.setBackground(BLANCO);
                    c.setForeground(Color.BLACK);
                }
            }
        }
        
        // Reapintar todo
        SwingUtilities.updateComponentTreeUI(this);
        repaint();
    }


    // ===== CÁLCULO DEL PROMEDIO =====
    private void calcularPromedio() {
        double suma = 0;
        double pesos = 0;

        try {
            for (int i = 0; i < camposNota.size(); i++) {
                double nota = Double.parseDouble(camposNota.get(i).getText());
                double peso = Double.parseDouble(camposPeso.get(i).getText());

                if (nota < 0 || nota > 100 || peso < 0) {
                    JOptionPane.showMessageDialog(this,
                        "Las notas deben estar entre 0 y 100, y los pesos no pueden ser negativos.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                suma += nota * peso;
                pesos += peso;
            }

            if (pesos == 0) {
                JOptionPane.showMessageDialog(this,
                        "La suma de los pesos debe ser mayor que 0.",
                        "Error",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            // 

            double promedio = suma / pesos;

            JOptionPane.showMessageDialog(this,
                "⭐ Tu promedio es: " + String.format("%.2f", promedio),
                "Resultado",
                JOptionPane.INFORMATION_MESSAGE
            );

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Por favor ingresa solo números.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    // Método main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PruebaCalculadora().setVisible(true));
    }
}