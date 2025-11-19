import javax.swing.*;
import java.awt.*;
import java.util.*;

public class CalendarioClases extends JFrame {

    private Map<String, JPanel> panelesDias = new LinkedHashMap<>();
    private String[] dias = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"};

    public CalendarioClases() {

        UIManager.put("OptionPane.messageFont", new Font("SansSerif", Font.PLAIN, 15));
        UIManager.put("OptionPane.buttonFont", new Font("SansSerif", Font.BOLD, 14));

        setTitle("📅 Horario Semanal de Clases");
        setSize(900, 600);
        
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 240, 250));

        JPanel panelSemana = new JPanel(new GridLayout(1, 6, 10, 10));
        panelSemana.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelSemana.setBackground(new Color(240, 240, 250));

        for (String dia : dias) {
            JPanel panelDia = new JPanel();
            panelDia.setLayout(new BoxLayout(panelDia, BoxLayout.Y_AXIS));
            panelDia.setBackground(new Color(250, 250, 255));
            panelDia.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(new Color(170, 170, 210), 2),
                    dia,
                    0, 0,
                    new Font("SansSerif", Font.BOLD, 14)
            ));

            panelesDias.put(dia, panelDia);
            panelSemana.add(panelDia);
        }

        add(panelSemana, BorderLayout.CENTER);

        // --- Panel de Botones ---
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBackground(new Color(230, 230, 245));

        // Botón Agregar Clase
        JButton btnAgregarClase = crearBoton("➕ Agregar Clase", new Color(110, 170, 255));
        btnAgregarClase.addActionListener(e -> agregarClase());

        // *** CAMBIO CLAVE 2: Botón Cerrar con dispose() ***
        JButton btnCerrar = crearBoton("❌ Cerrar Horario", new Color(200, 60, 60)); // Color rojo para cerrar
        btnCerrar.addActionListener(e -> {
            // Llama al método dispose() para liberar los recursos de esta ventana
            this.dispose(); 
        });
        
        panelBotones.add(btnAgregarClase);
        panelBotones.add(btnCerrar); // Agregamos el nuevo botón
        add(panelBotones, BorderLayout.NORTH);

        setLocationRelativeTo(null);
    }

    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("SansSerif", Font.BOLD, 14));
        boton.setForeground(Color.WHITE);
        boton.setBackground(color);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setFocusPainted(false);
        return boton;
    }

    private void agregarClase() {

        JTextField campoNombre = new JTextField(15);

        Integer[] horas = new Integer[24];
        for (int i = 0; i < 24; i++) horas[i] = i;

        Integer[] minutos = {0, 15, 30, 45};

        JComboBox<Integer> comboHoraInicio = new JComboBox<>(horas);
        JComboBox<Integer> comboMinInicio = new JComboBox<>(minutos);
        JComboBox<Integer> comboHoraFin = new JComboBox<>(horas);
        JComboBox<Integer> comboMinFin = new JComboBox<>(minutos);

        JComboBox<String> comboDia = new JComboBox<>(dias);

        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.add(new JLabel("Nombre de la clase:"));
        panel.add(campoNombre);

        JPanel horaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        horaPanel.add(new JLabel("Inicio:"));
        horaPanel.add(comboHoraInicio);
        horaPanel.add(new JLabel(":"));
        horaPanel.add(comboMinInicio);

        JPanel horaPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        horaPanel2.add(new JLabel("Fin:"));
        horaPanel2.add(comboHoraFin);
        horaPanel2.add(new JLabel(":"));
        horaPanel2.add(comboMinFin);

        panel.add(horaPanel);
        panel.add(horaPanel2);

        panel.add(new JLabel("Día:"));
        panel.add(comboDia);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Agregar clase",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {

            String nombre = campoNombre.getText().trim();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete el nombre.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int h1 = (int) comboHoraInicio.getSelectedItem();
            int m1 = (int) comboMinInicio.getSelectedItem();
            int h2 = (int) comboHoraFin.getSelectedItem();
            int m2 = (int) comboMinFin.getSelectedItem();

            String hora = String.format("%02d:%02d - %02d:%02d", h1, m1, h2, m2);
            String dia = comboDia.getSelectedItem().toString();

            int inicioInt = h1 * 60 + m1;
            int finInt = h2 * 60 + m2;

            if (finInt <= inicioInt) {
                JOptionPane.showMessageDialog(this, "La hora final debe ser mayor.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (horaRepetida(dia, inicioInt, finInt)) {
                JOptionPane.showMessageDialog(this, "Ya existe una clase con la misma hora.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            agregarClaseAlPanel(dia, nombre, hora);
            ordenarClases(dia);
        }
    }

    private boolean horaRepetida(String dia, int ini, int fin) {
        JPanel panelDia = panelesDias.get(dia);
        for (Component c : panelDia.getComponents()) {
            if (c instanceof JPanel) {
                JPanel p = (JPanel) c;
                // El JLabel de la hora está en la posición 1
                JLabel lbl = (JLabel) p.getComponent(1); 
                String t = lbl.getText();

                String[] partes = t.split(" - ");
                int i = parseHora(partes[0]);
                int f = parseHora(partes[1]);

                // Verificación de solapamiento de tiempo
                // Si la nueva clase empieza antes de que termine la existente Y termina después de que empieza la existente
                if (ini < f && fin > i) return true;
            }
        }
        return false;
    }

    private int parseHora(String h) {
        String[] hm = h.split(":");
        return Integer.parseInt(hm[0].trim()) * 60 + Integer.parseInt(hm[1].trim());
    }

    private void ordenarClases(String dia) {
        JPanel panelDia = panelesDias.get(dia);

        java.util.List<JPanel> lista = new ArrayList<>();
        for (Component c : panelDia.getComponents()) {
            if (c instanceof JPanel) lista.add((JPanel) c);
        }

        // Comparator para ordenar por la hora de inicio (en minutos)
        lista.sort((a, b) -> {
            JLabel la = (JLabel) a.getComponent(1);
            JLabel lb = (JLabel) b.getComponent(1);
            
            // Extrae solo la hora de inicio antes del " - "
            String horaA = la.getText().split(" - ")[0];
            String horaB = lb.getText().split(" - ")[0];
            
            return parseHora(horaA) - parseHora(horaB);
        });

        panelDia.removeAll();
        for (JPanel p : lista) panelDia.add(p);
        panelDia.revalidate();
        panelDia.repaint();
    }

    private void agregarClaseAlPanel(String dia, String nombre, String hora) {

        JPanel panelDia = panelesDias.get(dia);

        JPanel clasePanel = new JPanel();
        clasePanel.setLayout(new BoxLayout(clasePanel, BoxLayout.Y_AXIS));
        clasePanel.setBackground(new Color(230, 245, 255));
        clasePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 170, 200), 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JLabel lblNombre = new JLabel(nombre);
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 14));

        JLabel lblHora = new JLabel(hora);
        lblHora.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblHora.setForeground(new Color(50, 50, 50)); // Color de hora más oscuro

        JButton btnEliminar = new JButton("❌");
        btnEliminar.setBackground(new Color(255, 120, 120));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Ajuste para que el botón no sea demasiado grande
        btnEliminar.setMaximumSize(new Dimension(80, 25)); 
        btnEliminar.setAlignmentX(Component.CENTER_ALIGNMENT);


        btnEliminar.addActionListener(e -> {
            panelDia.remove(clasePanel);
            panelDia.revalidate();
            panelDia.repaint();
        });

        // Asegurarse de que los componentes estén alineados al centro (X_AXIS)
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblHora.setAlignmentX(Component.CENTER_ALIGNMENT);

        clasePanel.add(lblNombre);
        clasePanel.add(Box.createRigidArea(new Dimension(0, 3))); // Espaciador
        clasePanel.add(lblHora);
        clasePanel.add(Box.createRigidArea(new Dimension(0, 5))); // Espaciador
        clasePanel.add(btnEliminar);
        clasePanel.add(Box.createRigidArea(new Dimension(0, 5))); // Espaciador final

        panelDia.add(clasePanel);
        panelDia.revalidate();
        panelDia.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CalendarioClases().setVisible(true));
    }
}