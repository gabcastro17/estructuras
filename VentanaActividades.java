import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeSet;
import java.util.HashMap;
import java.time.LocalDateTime;

public class VentanaActividades extends JFrame {

    // Simulación de carga de datos (deberías usar CargadorDatos.java)
    public static Usuario estudiante = Metodos.cargarPersonita();
    public static Map<String, Usuario> Estudiantes = Metodos.cargarMapaUsuarios();
    public static Map<Integer, Actividad> ActividadesCargadasMap = Metodos.cargarMapaActividades();

    private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd 'de' MMMM, yyyy - HH:mm");

    // Colores para el tema claro
    private final Color LIGHT_BACKGROUND = new Color(204, 204, 255);
    private final Color LIGHT_CARD_BACKGROUND = Color.WHITE;
    private final Color LIGHT_TEXT_COLOR = Color.BLACK;
    private final Color LIGHT_TITLE_COLOR = Color.WHITE;
    private final Color LIGHT_BUTTON_BACKGROUND = new Color(102, 102, 255);
    private final Color LIGHT_CLOSE_BUTTON = new Color(230, 0, 0); // Cerrar
    private final Color LIGHT_BAJA_BUTTON = new Color(255, 165, 0); // Darse de baja (Naranja)

    // Colores para el tema oscuro
    private final Color DARK_BACKGROUND = new Color(30, 30, 30);
    private final Color DARK_CARD_BACKGROUND = new Color(45, 45, 45);
    private final Color DARK_TEXT_COLOR = new Color(200, 200, 200);
    private final Color DARK_TITLE_COLOR = new Color(240, 240, 240);
    private final Color DARK_BUTTON_BACKGROUND = new Color(80, 80, 150);
    private final Color DARK_CLOSE_BUTTON = new Color(180, 0, 0); // Cerrar
    private final Color DARK_BAJA_BUTTON = new Color(180, 100, 0); // Darse de baja (Naranja Oscuro)

    private boolean isDarkMode = false;

    private JPanel contentPane;
    private JLabel tituloPrincipal;
    private JScrollPane scrollPane;
    private JButton temaOscuroButton;
    private JButton cerrarButton;

    private Map<Integer, JPanel> tarjetasActividad = new HashMap<>();

    public VentanaActividades(Map<Integer, Actividad> actividadesMap) {
        
        // Es crucial que ActividadesCargadasMap use la referencia que le pasamos al constructor
        // para que refreshCard() funcione correctamente al obtener la actividad modificada.
        ActividadesCargadasMap = actividadesMap; 
        
        setTitle("Actividades Próximas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // --- Contenedor Principal (contentPane) ---
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- Panel Superior (Título y Botones) ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        tituloPrincipal = new JLabel("Actividades Próximas", SwingConstants.CENTER);
        tituloPrincipal.setFont(new Font("SansSerif", Font.BOLD, 30));
        topPanel.add(tituloPrincipal, BorderLayout.CENTER);

        // --- Panel de Botones (Derecha) ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);

        temaOscuroButton = new JButton("Tema Oscuro");
        temaOscuroButton.setFocusPainted(false);
        temaOscuroButton.addActionListener(e -> toggleTheme());
        buttonPanel.add(temaOscuroButton);

        cerrarButton = crearBotonCerrar();
        buttonPanel.add(cerrarButton);

        topPanel.add(buttonPanel, BorderLayout.EAST);
        contentPane.add(topPanel, BorderLayout.NORTH);

        // --- Panel para Apilar las Tarjetas (listaPanel) ---
        JPanel listaPanel = new JPanel();
        listaPanel.setLayout(new BoxLayout(listaPanel, BoxLayout.Y_AXIS));
        listaPanel.setOpaque(false);

        for (Map.Entry<Integer, Actividad> entry : actividadesMap.entrySet()) {
            Actividad actividad = entry.getValue();
            JPanel tarjeta = crearTarjetaActividad(actividad);
            tarjetasActividad.put(actividad.getCodigo(), tarjeta);

            listaPanel.add(tarjeta);
            listaPanel.add(Box.createVerticalStrut(20));
        }

        scrollPane = new JScrollPane(listaPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        contentPane.add(scrollPane, BorderLayout.CENTER);

        setContentPane(contentPane);
        pack();
        setMinimumSize(new Dimension(850, 500));
        setLocationRelativeTo(null);

        applyTheme(isDarkMode);
    }

    private JButton crearBotonCerrar() {
        JButton boton = new JButton("❌");
        boton.setFocusPainted(false);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("SansSerif", Font.BOLD, 14));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        boton.addActionListener(e -> this.dispose());
        boton.setName("CloseButton");
        Metodos.guardarMapaUsuarios(Estudiantes,estudiante,"Usuarios.dat");
        Metodos.guardarMapaActividades(ActividadesCargadasMap, "Actividades.dat");
        Metodos.guardarUsuario(estudiante, "Personita.dat");
        return boton;
    }

    // =================================================================
    // refresher y botones simon
    
    //crear botones con estilo
    private JButton crearBotonEstilo(String texto) {
        JButton boton = new JButton(texto);
        boton.setOpaque(true);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setForeground(Color.WHITE);
        // El color de fondo se aplica en applyTheme
        return boton;
    }
    
    /**
     * Implementa la lógica condicional y la acción del botón.
     */
    private Component crearBotonAccion(Actividad actividad, boolean estaInscrito, boolean estaLleno) {
        Integer codigo = actividad.getCodigo();

        if (estaLleno) {
            // Caso 1: La actividad está llena
            JLabel avisoJLabel = new JLabel("ACTIVIDAD LLENA");
            // El color se aplica en updateComponentColors
            avisoJLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
            avisoJLabel.setName("AvisoLleno");
            return avisoJLabel;
            
        } else if (estaInscrito) {
            // Caso 2: Inscrito, puede darse de baja
            JButton darseDeBajaBoton = crearBotonEstilo("Darse de baja");
            darseDeBajaBoton.setName("BotonDarseDeBaja-" + codigo);

            darseDeBajaBoton.addActionListener(e -> {
                // Lógica de confirmación de baja (Tú requerimiento)
                Object[] opciones = { "Sí, darme de baja", "No, cancelar" };
                int seleccion = JOptionPane.showOptionDialog(null, 
                    "Ya estás inscrito en esta actividad.\n¿Quieres darte de baja?", "Confirmar Baja", 
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[1]);

                if (seleccion == JOptionPane.YES_OPTION) {
                    // 1. Actualización de datos 
                    estudiante.getActividadesInscritas().remove(codigo);
                    actividad.setAsistentesActuales(actividad.getAsistentesActuales() - 1);
                    estudiante.setDunabs(estudiante.getDunabs() - actividad.getCantidadDunabs());
                    

                    
                    // 2. Persistencia
                     Metodos.guardarMapaUsuarios(Estudiantes, estudiante, "Usuarios.dat");
                     Metodos.guardarMapaActividades(ActividadesCargadasMap, actividad, "Actividades.dat");
                    
                    JOptionPane.showMessageDialog(this, "¡Baja procesada con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    Metodos.guardarUsuario(estudiante, "Personita.dat");
                    // 3. REFRESCAR LA TARJETA
                    refreshCard(codigo); 
                }
                
            });
            return darseDeBajaBoton;
            
        } else {
            // Caso 3: Ni lleno, ni inscrito. Puede registrarse
            JButton registrarseBoton = crearBotonEstilo("Inscribirse");
            registrarseBoton.setName("BotonRegistrarse-" + codigo);
            
            registrarseBoton.addActionListener(e -> {
                // 1. Actualización de datos en memoria (añadiendo la inscripción)
                estudiante.getActividadesInscritas().add(codigo);
                actividad.setAsistentesActuales(actividad.getAsistentesActuales() + 1);
                estudiante.setDunabs(estudiante.getDunabs() + actividad.getCantidadDunabs());


                // 2. Persistencia 
                Metodos.guardarMapaUsuarios(Estudiantes, estudiante, "Usuarios.dat");
                 Metodos.guardarMapaActividades(ActividadesCargadasMap, actividad, "Actividades.dat");
                
                JOptionPane.showMessageDialog(this, 
                    "Te pudiste inscribir a la actividad: " + actividad.getNombre(), "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
                Metodos.guardarUsuario(estudiante, "Personita.dat");
                // 3. REFRESCAR LA TARJETA
                refreshCard(codigo);
            });
            return registrarseBoton;
        }
    }

    /**
     * Método crucial para refrescar una tarjeta específica después de un cambio.
     */
    private void refreshCard(Integer codigoActividad) {
        // Obtenemos el panel que contiene todas las tarjetas
        JPanel listaPanel = (JPanel) scrollPane.getViewport().getView();
        
        JPanel oldCard = tarjetasActividad.get(codigoActividad);
        Actividad actividadActualizada = ActividadesCargadasMap.get(codigoActividad);

        if (oldCard != null && actividadActualizada != null) {
            
            // 1. Encontrar la posición de la tarjeta antigua para mantener el orden
            int index = -1;
            for (int i = 0; i < listaPanel.getComponentCount(); i++) {
                if (listaPanel.getComponent(i) == oldCard) {
                    index = i;
                    break;
                }
            }
            
            if (index != -1) {
                // 2. Crear la nueva tarjeta con el estado de datos actual
                JPanel newCard = crearTarjetaActividad(actividadActualizada);
                
                // 3. Reemplazar
                listaPanel.remove(oldCard);
                listaPanel.add(newCard, index); 
                
                // 4. Actualizar referencias
                tarjetasActividad.put(codigoActividad, newCard); 
                applyTheme(isDarkMode); 

                // 5. Redibujar
                listaPanel.revalidate();
                listaPanel.repaint();
            }
        }
    }

    /**
     * Crea un JPanel que representa una tarjeta de actividad (Ahora solo crea la estructura).
     */
    private JPanel crearTarjetaActividad(Actividad actividad) {
        JPanel tarjetaPanel = new JPanel();
        tarjetaPanel.setLayout(new BoxLayout(tarjetaPanel, BoxLayout.Y_AXIS));
        tarjetaPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        tarjetaPanel.setBackground(Color.WHITE);
        tarjetaPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        tarjetaPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        // --- Contenedor Superior (Título y Descripción) ---
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        infoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel tituloLabel = new JLabel(actividad.getNombre());
        tituloLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        tituloLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(tituloLabel);

        JLabel descLabel = new JLabel("<html><p style=\"width:600px;\">" + actividad.getDescripcion() + "</p></html>");
        descLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(descLabel);

        tarjetaPanel.add(infoPanel);
        tarjetaPanel.add(Box.createVerticalStrut(10));

        // --- Contenedor Inferior (Datos y Botón) ---
        JPanel footerPanel = new JPanel(new GridBagLayout());
        footerPanel.setOpaque(false);
        footerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // --- Columna 0: Datos (Fecha, Código, Puntos) ---
        JPanel datosTopPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        datosTopPanel.setOpaque(false);
        String fechaFormateada = actividad.getFecha().format(DATE_FORMATTER);

        datosTopPanel.add(new JLabel("📅 Fecha: " + fechaFormateada));
        datosTopPanel.add(new JLabel("Código: " + actividad.getCodigo()));
        datosTopPanel.add(new JLabel("Puntos: " + actividad.getCantidadDunabs() + " Dunabs"));

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0;
        footerPanel.add(datosTopPanel, gbc);

        // --- Columna 0, Fila 1: Datos adicionales (Capacidad, Lugar) ---
        JPanel datosBottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        datosBottomPanel.setOpaque(false);

        String capacidadStr = actividad.getAsistentesActuales() + "/" + actividad.getCapacidadMax();
        datosBottomPanel.add(new JLabel("Capacidad: " + capacidadStr));
        datosBottomPanel.add(new JLabel("Lugar: " + actividad.getLugar()));

        gbc.gridx = 0; gbc.gridy = 1; gbc.insets = new Insets(0, 0, 0, 10);
        footerPanel.add(datosBottomPanel, gbc);

        // --- Columna 1: Botón (Fila 0 y Fila 1) ---
        Integer codigo = actividad.getCodigo();
        TreeSet<Integer> actividadesInscritas = estudiante.getActividadesInscritas();
        boolean estaInscrito = actividadesInscritas.contains(codigo);
        boolean estaLleno = (actividad.getAsistentesActuales() == actividad.getCapacidadMax());
        
        // ** INTEGRACIÓN DE LA LÓGICA CONDICIONAL **
        Component accionComponent = crearBotonAccion(actividad, estaInscrito, estaLleno);

        gbc.gridx = 1; gbc.gridy = 0; gbc.gridheight = 2; gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.VERTICAL; gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 10, 0, 0);

        footerPanel.add(accionComponent, gbc);
        tarjetaPanel.add(footerPanel);

        // Nombres para el cambio de tema (resto)
        tituloLabel.setName("TituloActividad-" + actividad.getCodigo());
        descLabel.setName("DescripcionActividad-" + actividad.getCodigo());
        // ... (Tu lógica de nombres para otros componentes) ...
        
        return tarjetaPanel;
    }

    private void toggleTheme() {
        isDarkMode = !isDarkMode;
        applyTheme(isDarkMode);
        temaOscuroButton.setText(isDarkMode ? "Tema Claro" : "Tema Oscuro");
    }

    private void applyTheme(boolean dark) {
        Color currentBackground = dark ? DARK_BACKGROUND : LIGHT_BACKGROUND;
        Color currentTitleColor = dark ? DARK_TITLE_COLOR : LIGHT_TITLE_COLOR;
        Color currentCardBackground = dark ? DARK_CARD_BACKGROUND : LIGHT_CARD_BACKGROUND;
        Color currentTextColor = dark ? DARK_TEXT_COLOR : LIGHT_TEXT_COLOR;
        Color currentRegistrarseButton = dark ? DARK_BUTTON_BACKGROUND : LIGHT_BUTTON_BACKGROUND;
        Color currentBajaButton = dark ? DARK_BAJA_BUTTON : LIGHT_BAJA_BUTTON;
        Color currentCloseButtonBackground = dark ? DARK_CLOSE_BUTTON : LIGHT_CLOSE_BUTTON;

        contentPane.setBackground(currentBackground);
        tituloPrincipal.setForeground(currentTitleColor);
        scrollPane.getViewport().setBackground(currentBackground);

        temaOscuroButton.setBackground(currentRegistrarseButton);
        temaOscuroButton.setForeground(currentTitleColor);

        if (cerrarButton != null) {
            cerrarButton.setBackground(currentCloseButtonBackground);
        }

        for (JPanel tarjeta : tarjetasActividad.values()) {
            tarjeta.setBackground(currentCardBackground);
            // Actualizamos para pasar el color de baja también
            updateComponentColors(tarjeta, currentTextColor, currentRegistrarseButton, currentBajaButton, dark);
        }

        SwingUtilities.updateComponentTreeUI(this);
        repaint();
    }
    
    // Se modificó para recibir el color de baja y el estado oscuro
    private void updateComponentColors(Container container, Color textColor, Color registrarseColor, Color bajaColor, boolean dark) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                if ("AvisoLleno".equals(label.getName())) {
                    // El color del aviso LLENO es fijo (rojo/más claro en oscuro)
                    label.setForeground(dark ? new Color(255, 100, 100) : new Color(200, 50, 50));
                } else {
                    label.setForeground(textColor);
                }
            } else if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                String name = button.getName();
                
                if (name != null) {
                    if (name.startsWith("BotonRegistrarse")) {
                        button.setBackground(registrarseColor);
                    } else if (name.startsWith("BotonDarseDeBaja")) {
                        button.setBackground(bajaColor);
                    }
                    button.setForeground(Color.WHITE); 
                }
            } else if (comp instanceof Container) {
                updateComponentColors((Container) comp, textColor, registrarseColor, bajaColor, dark);
            }
        }
    }


    public static void main(String[] args) {
        // Inicialización de datos de prueba, simulando la carga
        ActividadesCargadasMap = Metodos.cargarMapaActividades();
        
        // Simular que el usuario ya está inscrito en la actividad 303 (ver clase Usuario simulada)
        // La actividad 202 está llena (ver Actividad simulada)
        
        SwingUtilities.invokeLater(() -> {
            new VentanaActividades(ActividadesCargadasMap).setVisible(true);
        });
    }
}
