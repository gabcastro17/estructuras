import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Comparator;

public class ventanaAdministrador extends JFrame {
    
    // Asumo que estas clases y métodos existen y funcionan correctamente
    public static Usuario estudiante = Metodos.cargarPersonita();
    public static Map<String, Usuario> Estudiantes = Metodos.cargarMapaUsuarios();
    public static Map<Integer, Actividad> mapaActividades = Metodos.cargarMapaActividades();
    public static List<Actividad> ListaActividades = Metodos.convertirMapaALista(mapaActividades); 

    private final JPanel panelContenedorActividades;
    private final JTextField txtBuscador;
    
    // Componentes que necesitan cambiar de color
    private JPanel panelIzquierdo;
    private JPanel panelDerecho;
    private JPanel panelControles; // Añadir referencia al panel de controles
    private JButton btnModoOscuro; // Se inicializará en crearPanelDerecho()

    // --- Datos del Administrador (se asume que Usuario tiene getID, getUsername)
    private static final String NOMBRE_COMPLETO_ADMIN = estudiante.getNombreCompleto();
    private static final String CODIGO_UNIVERSITARIO_ADMIN = estudiante.getID();
    private static final String USUARIO_ADMIN = estudiante.getUsername();

    // --- Constantes de Estilo ---
    // COLORES DEL MODO CLARO (PREDETERMINADO)
    private static final Color CLARO_PANEL_IZQUIERDO = new Color(103, 58, 183); // Morado
    private static final Color CLARO_FONDO_DERECHO = Color.WHITE;
    private static final Color CLARO_TEXTO_PRIMARIO = Color.BLACK;
    private static final Color CLARO_TEXTO_SECUNDARIO = new Color(200, 200, 200);
    private static final Color CLARO_TITULO_IZQ = Color.WHITE;

    // COLORES DEL MODO OSCURO
    private static final Color OSCURO_PANEL_IZQUIERDO = new Color(33, 33, 33); // Gris Oscuro
    private static final Color OSCURO_FONDO_DERECHO = new Color(18, 18, 18); // Negro muy oscuro
    private static final Color OSCURO_TEXTO_PRIMARIO = Color.WHITE;
    private static final Color OSCURO_TEXTO_SECUNDARIO = new Color(150, 150, 150);
    private static final Color OSCURO_TITULO_IZQ = new Color(179, 157, 219); // Morado claro para títulos

    // Colores de acciones que NO cambian
    private static final Color COLOR_PRIMARIO = new Color(76, 175, 80); // Verde para 'Añadir Actividad'
    private static final Color COLOR_MODIFICAR = new Color(66, 133, 244);
    private static final Color COLOR_ELIMINAR = new Color(244, 67, 54);
    
    private boolean modoOscuroActivado = false; // Estado inicial: Modo Claro

    // Constructor 
    public ventanaAdministrador() {
         
        this.panelContenedorActividades = new JPanel();
        this.txtBuscador = new JTextField(20);

        // ** Configuración de la Ventana **
        setTitle("Panel de Administración de Actividades");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(900, 600);
        setLayout(new BorderLayout());

        //aca al cerrar este ventana administrador, se guardan los datos
        this.addWindowListener(new java.awt.event.WindowAdapter() {
        @Override
        public void windowClosing(java.awt.event.WindowEvent windowEvent) {
            // Muestra el mensaje que solicitaste
            JOptionPane.showMessageDialog(
                ventanaAdministrador.this, 
                "Cerrando programa, adiós.", 
                "Cierre de Aplicación", 
                JOptionPane.INFORMATION_MESSAGE
            );
            //serializacion final
            Metodos.guardarUsuario(estudiante,"Personita.dat");
            Metodos.guardarMapaActividades(mapaActividades,"Actividades");

            
            // Termina la aplicación después de mostrar el mensaje
            System.exit(0); 
        }
    });

        

        // ** Crear Componentes **
        panelIzquierdo = crearPanelIzquierdo(); 
        panelDerecho = crearPanelDerecho(); // Aquí se inicializa btnModoOscuro

        add(panelIzquierdo, BorderLayout.WEST);
        add(panelDerecho, BorderLayout.CENTER);

        // Aplicar el tema inicial (Modo Claro)
        aplicarTema(false);

        refrescarPanelActividades(mapaActividades);
        
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // --- Métodos de Tema y Estilo ---
    
    private void aplicarTema(boolean oscuro) {
        Color fondoIzq = oscuro ? OSCURO_PANEL_IZQUIERDO : CLARO_PANEL_IZQUIERDO;
        Color fondoDer = oscuro ? OSCURO_FONDO_DERECHO : CLARO_FONDO_DERECHO;
        Color textoTit = oscuro ? OSCURO_TITULO_IZQ : CLARO_TITULO_IZQ;
        Color textoPrim = oscuro ? OSCURO_TEXTO_PRIMARIO : CLARO_TEXTO_PRIMARIO;
        Color textoSec = oscuro ? OSCURO_TEXTO_SECUNDARIO : CLARO_TEXTO_SECUNDARIO;
        
        // 1. Panel Izquierdo
        if (panelIzquierdo != null) {
            panelIzquierdo.setBackground(fondoIzq);
            for (Component comp : panelIzquierdo.getComponents()) {
                if (comp instanceof JLabel) {
                    JLabel lbl = (JLabel) comp;
                    // Solo cambia el color del título "Información del Administrador"
                    if (lbl.getText() != null && lbl.getText().contains("Información")) {
                        lbl.setForeground(textoTit);
                    } else if (lbl.getText() != null && lbl.getText().contains("Logo")) { 
                        // Mantiene el color del texto de la etiqueta si el ícono no carga
                        lbl.setForeground(textoTit);
                    }
                } else if (comp instanceof JPanel) {
                    JPanel subPanel = (JPanel) comp;
                    // Intenta cambiar el fondo de los subpaneles de información
                    if (subPanel.getLayout() instanceof BoxLayout) {
                         subPanel.setBackground(fondoIzq);
                    } else if (subPanel.getLayout() instanceof FlowLayout) {
                        // Cambia el fondo del panel que contiene el valor
                        subPanel.setBackground(fondoIzq.brighter()); 
                    }
                }
            }
        }
        
        // 2. Panel Derecho y Controles
        if (panelDerecho != null) {
            panelDerecho.setBackground(fondoDer);
        }
        if (panelControles != null) {
            panelControles.setBackground(fondoDer);
            for (Component comp : panelControles.getComponents()) {
                if (comp instanceof JLabel) {
                    comp.setForeground(textoPrim);
                }
            }
        }
        
        // 3. Botón de Modo Oscuro/Claro
        if (btnModoOscuro != null) {
            btnModoOscuro.setText(oscuro ? "☀️ Modo Claro" : "🌙 Modo Oscuro");
            btnModoOscuro.setBackground(oscuro ? new Color(50, 50, 50) : new Color(220, 220, 220));
            btnModoOscuro.setForeground(oscuro ? Color.WHITE : Color.BLACK);
        }

        // 4. Refrescar actividades (para cambiar el fondo y el texto de las tarjetas)
        refrescarPanelActividades(mapaActividades);
        
        // Revalidar para asegurar que todos los cambios se apliquen
        revalidate();
        repaint();
    }

    private void toggleModoOscuro() {
        modoOscuroActivado = !modoOscuroActivado;
        aplicarTema(modoOscuroActivado);
    }

    // --- Métodos de Construcción de GUI ---

    private JPanel crearPanelIzquierdo() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(250, 600));
        panel.setBackground(CLARO_PANEL_IZQUIERDO); // Inicial
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- 1. Espacio para el Logo ---
        JLabel lblLogo = new JLabel();
        try {
             // Intenta cargar la imagen que subiste
             ImageIcon icon = new ImageIcon(getClass().getResource("src\\logo java dunab.png"));
             Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
             lblLogo.setIcon(new ImageIcon(img));
        } catch (Exception e) {
             // si el logo no carga
             lblLogo.setText("UNAB Logo");
             lblLogo.setForeground(Color.WHITE);
             lblLogo.setFont(new Font("Arial", Font.BOLD, 18));
             
        }
        lblLogo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblLogo);
        
        panel.add(Box.createRigidArea(new Dimension(0, 10))); 

        // Título del panel
        JLabel lblTitulo = new JLabel("<html><b>Información del<br>Administrador</b></html>");
        lblTitulo.setForeground(Color.WHITE); 
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblTitulo);
        
        panel.add(Box.createRigidArea(new Dimension(0, 10))); 

        // Secciones de información usando las constantes
        Color colorFondoValor = CLARO_PANEL_IZQUIERDO.brighter(); // Color inicial
        
        panel.add(crearSeccionInfo("Nombre Completo", NOMBRE_COMPLETO_ADMIN, colorFondoValor));
        panel.add(crearSeccionInfo("Código UNAB", CODIGO_UNIVERSITARIO_ADMIN, colorFondoValor));
        panel.add(crearSeccionInfo("USUARIO UNAB", USUARIO_ADMIN, colorFondoValor));
        
        // Empuja el contenido hacia arriba (ya no se añade btnModoOscuro aquí)
        panel.add(Box.createVerticalGlue()); 
        
        return panel;
    }

    private JPanel crearSeccionInfo(String titulo, String valor, Color colorFondoValor) {
        JPanel panelSeccion = new JPanel();
        panelSeccion.setLayout(new BoxLayout(panelSeccion, BoxLayout.Y_AXIS));
        panelSeccion.setBackground(CLARO_PANEL_IZQUIERDO); // Color inicial
        panelSeccion.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        panelSeccion.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setForeground(new Color(200, 200, 200)); 
        lblTitulo.setFont(new Font("Arial", Font.PLAIN, 12));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelSeccion.add(lblTitulo);

        JPanel panelValor = new JPanel();
        panelValor.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panelValor.setBackground(colorFondoValor); // Color inicial
        panelValor.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        panelValor.setMaximumSize(new Dimension(250, 40));

        JLabel lblValor = new JLabel(valor);
        lblValor.setForeground(Color.WHITE);
        lblValor.setFont(new Font("Arial", Font.BOLD, 14));
        panelValor.add(lblValor);

        panelSeccion.add(panelValor);
        return panelSeccion;
    }
    
    // Método para crear el panel derecho
    private JPanel crearPanelDerecho() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(CLARO_FONDO_DERECHO); // Inicial
        this.panelDerecho = panel; // Guardar referencia

        // Título principal
        JLabel lblTitulo = new JLabel("Panel de Administración de Actividades");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        panel.add(lblTitulo, BorderLayout.NORTH);

        // Panel de Controles (Añadir y Buscar)
        panelControles = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelControles.setBackground(CLARO_FONDO_DERECHO); // Inicial
        panelControles.add(new JLabel("🌙")); 
        
        JButton btnAgregar = new JButton("+ Añadir Actividad");
        btnAgregar.setBackground(COLOR_PRIMARIO);
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFocusPainted(false);
        btnAgregar.addActionListener(this::mostrarDialogoAgregarActividad);
        panelControles.add(btnAgregar);
        
        panelControles.add(new JLabel("Buscar por Código:"));
        panelControles.add(txtBuscador);
        
        JButton btnBuscar = new JButton("🔍 Buscar");
        btnBuscar.addActionListener(this::buscarActividadPorCodigo);
        panelControles.add(btnBuscar);
        
        JButton btnMostrarTodos = new JButton("Mostrar Todos");
        btnMostrarTodos.addActionListener(e -> refrescarPanelActividades(mapaActividades));
        panelControles.add(btnMostrarTodos);

        // INICIALIZACIÓN DEL BOTÓN MODO OSCURO (CORREGIDO)
        btnModoOscuro = new JButton("🌙 Modo Oscuro");
        btnModoOscuro.setBackground(new Color(220, 220, 220));
        btnModoOscuro.setForeground(Color.BLACK);
        btnModoOscuro.setFocusPainted(false);
        btnModoOscuro.addActionListener(e -> toggleModoOscuro());
        panelControles.add(btnModoOscuro); // AÑADIDO AL PANEL DE CONTROLES

        // Panel de Actividades (donde se listan)
        panelContenedorActividades.setLayout(new BoxLayout(panelContenedorActividades, BoxLayout.Y_AXIS));
        panelContenedorActividades.setBackground(CLARO_FONDO_DERECHO); // Inicial
        
        JScrollPane scrollPane = new JScrollPane(panelContenedorActividades);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.add(panelControles, BorderLayout.NORTH);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);
        panelPrincipal.setBackground(CLARO_FONDO_DERECHO); // Inicial

        panel.add(panelPrincipal, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearTarjetaActividad(Actividad actividad) {
        // Colores dinámicos para el modo oscuro
        Color fondoTarjeta = modoOscuroActivado ? new Color(40, 40, 40) : Color.WHITE;
        Color colorTexto = modoOscuroActivado ? Color.WHITE : Color.BLACK;

        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BorderLayout(10, 0));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY.darker(), 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        tarjeta.setBackground(fondoTarjeta);
        tarjeta.setMaximumSize(new Dimension(800, 100));

        JPanel panelTexto = new JPanel(new GridLayout(3, 1));
        panelTexto.setBackground(fondoTarjeta);

        JLabel lblNombre = new JLabel("<html><b>" + actividad.getNombre() + "</b></html>");
        lblNombre.setFont(new Font("Arial", Font.BOLD, 16));
        lblNombre.setForeground(colorTexto);
        panelTexto.add(lblNombre);

        JLabel lblDescripcion = new JLabel(actividad.getDescripcion());
        lblDescripcion.setForeground(colorTexto);
        panelTexto.add(lblDescripcion);

        JPanel panelMetadatos = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panelMetadatos.setBackground(fondoTarjeta);
        
        JLabel lblFecha = new JLabel("🗓️ Fecha: " + actividad.getFecha().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))); 
        lblFecha.setForeground(colorTexto);
        panelMetadatos.add(lblFecha);
        
        JLabel lblCodigo = new JLabel("🚀 Código de Actividad: " + actividad.getCodigo());
        lblCodigo.setForeground(colorTexto);
        panelMetadatos.add(lblCodigo);
        
        JLabel lblPuntos = new JLabel("* " + actividad.getCantidadDunabs() + " Dunabs");
        lblPuntos.setForeground(colorTexto);
        panelMetadatos.add(lblPuntos);

        JLabel lblLugar = new JLabel("Lugar: " + actividad.getLugar() );
        lblLugar.setForeground(colorTexto);
        panelMetadatos.add(lblLugar);

        panelTexto.add(panelMetadatos);
        tarjeta.add(panelTexto, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBotones.setBackground(fondoTarjeta);

        JButton btnModificar = new JButton("Modificar");
        btnModificar.setBackground(COLOR_MODIFICAR);
        btnModificar.setForeground(Color.WHITE);
        btnModificar.setFocusPainted(false);
        btnModificar.addActionListener(e -> mostrarDialogoModificarActividad(actividad));
        panelBotones.add(btnModificar);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBackground(COLOR_ELIMINAR);
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.addActionListener(e -> eliminarActividad(actividad.getCodigo()));
        panelBotones.add(btnEliminar);

        tarjeta.add(panelBotones, BorderLayout.EAST);

        return tarjeta;
    }

    
    private void refrescarPanelActividades(Map<Integer, Actividad> actividadesAMostrar) {
        panelContenedorActividades.removeAll();
        
        // Ajustar el color de fondo del contenedor según el tema
        panelContenedorActividades.setBackground(modoOscuroActivado ? OSCURO_FONDO_DERECHO : CLARO_FONDO_DERECHO);

        List<Actividad> listaOrdenada = new ArrayList<>(actividadesAMostrar.values());
        // Se usa Comparator para ordenar por fecha (de más cercana a más lejana)
        Collections.sort(listaOrdenada, Comparator.comparing(Actividad::getFecha)); 
        
        for (Actividad actividad : listaOrdenada) {
            panelContenedorActividades.add(crearTarjetaActividad(actividad));
            panelContenedorActividades.add(Box.createRigidArea(new Dimension(0, 10))); 
        }

        panelContenedorActividades.revalidate();
        panelContenedorActividades.repaint();
    }
    
    // ... (Métodos auxiliares se mantienen igual)
    
    private void buscarActividadPorCodigo(ActionEvent e) {
        try {
            int codigoBuscado = Integer.parseInt(txtBuscador.getText().trim());
            Actividad actividadEncontrada = mapaActividades.get(codigoBuscado);

            if (actividadEncontrada != null) {
                Map<Integer, Actividad> resultado = new HashMap<>();
                resultado.put(codigoBuscado, actividadEncontrada);
                refrescarPanelActividades(resultado);
            } else {
                JOptionPane.showMessageDialog(this, "Actividad con código " + codigoBuscado + " no encontrada.", "Búsqueda Fallida", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un código numérico válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDialogoAgregarActividad(ActionEvent e) {
        JTextField txtNombre = new JTextField();
        JTextField txtDescripcion = new JTextField();
        JTextField txtFecha = new JTextField(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        JTextField txtPuntos = new JTextField();
        JTextField txtCapacidad = new JTextField();
        JTextField txtCodigo = new JTextField();
        JTextField txtLugar = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Nombre:")); panel.add(txtNombre);
        panel.add(new JLabel("Descripción:")); panel.add(txtDescripcion);
        panel.add(new JLabel("Fecha (yyyy-MM-dd HH:mm):")); panel.add(txtFecha);
        panel.add(new JLabel("Puntos (int):")); panel.add(txtPuntos);
        panel.add(new JLabel("Capacidad Máx (int):")); panel.add(txtCapacidad);
        panel.add(new JLabel("Código (Integer):")); panel.add(txtCodigo);
        panel.add(new JLabel("Lugar:")); panel.add(txtLugar);

        int result = JOptionPane.showConfirmDialog(this, panel, "Añadir Nueva Actividad",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                // Parsear los datos
                String nombre = txtNombre.getText();
                String descripcion = txtDescripcion.getText();
                LocalDateTime fecha = LocalDateTime.parse(txtFecha.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                int puntos = Integer.parseInt(txtPuntos.getText());
                int capacidad = Integer.parseInt(txtCapacidad.getText());
                Integer codigo = Integer.parseInt(txtCodigo.getText());
                String lugar = txtLugar.getText();

                // Validar código no repetido
                if (mapaActividades.containsKey(codigo)) {
                    JOptionPane.showMessageDialog(this, "Error: El código " + codigo + " ya existe. Intente con otro.", "Código Repetido", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Actividad nuevaActividad = new Actividad(nombre, descripcion, fecha, puntos, capacidad, codigo, lugar);
                mapaActividades.put(codigo, nuevaActividad);
                refrescarPanelActividades(mapaActividades);
                JOptionPane.showMessageDialog(this, "Actividad '" + nombre + "' agregada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            } catch (NumberFormatException | java.time.format.DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Error de formato en datos numéricos o fecha. Verifique el formato.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void mostrarDialogoModificarActividad(Actividad actividad) {
        JTextField txtNombre = new JTextField(actividad.getNombre());
        JTextField txtDescripcion = new JTextField(actividad.getDescripcion());
        JTextField txtFecha = new JTextField(actividad.getFecha().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        JTextField txtPuntos = new JTextField(String.valueOf(actividad.getCantidadDunabs()));
        JTextField txtCapacidad = new JTextField(String.valueOf(actividad.getCapacidadMax()));
        JTextField txtLugar = new JTextField(actividad.getLugar());
        JLabel lblCodigo = new JLabel(String.valueOf(actividad.getCodigo()));

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Código:")); panel.add(lblCodigo);
        panel.add(new JLabel("Nombre:")); panel.add(txtNombre);
        panel.add(new JLabel("Descripción:")); panel.add(txtDescripcion);
        panel.add(new JLabel("Fecha (yyyy-MM-dd HH:mm):")); panel.add(txtFecha);
        panel.add(new JLabel("Puntos (int):")); panel.add(txtPuntos);
        panel.add(new JLabel("Capacidad Máx (int):")); panel.add(txtCapacidad);
        panel.add(new JLabel("Lugar:")); panel.add(txtLugar);

        int result = JOptionPane.showConfirmDialog(this, panel, "Modificar Actividad: " + actividad.getNombre(),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                LocalDateTime nuevaFecha = LocalDateTime.parse(txtFecha.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                int nuevosPuntos = Integer.parseInt(txtPuntos.getText());
                int nuevaCapacidad = Integer.parseInt(txtCapacidad.getText());
                
                actividad.setNombre(txtNombre.getText());
                actividad.setDescripcion(txtDescripcion.getText());
                actividad.setFecha(nuevaFecha);
                actividad.setCantidadDunabs(nuevosPuntos);
                actividad.setCapacidadMax(nuevaCapacidad);
                actividad.setLugar(txtLugar.getText());
                
                mapaActividades.put(actividad.getCodigo(), actividad);
                refrescarPanelActividades(mapaActividades);
                JOptionPane.showMessageDialog(this, "Actividad modificada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            } catch (NumberFormatException | java.time.format.DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Error de formato en datos numéricos o fecha. Verifique el formato.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarActividad(Integer codigo) {
        int confirm = JOptionPane.showConfirmDialog(this, 
                "¿Está seguro de que desea eliminar la actividad con código " + codigo + "?", 
                "Confirmar Eliminación", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            Actividad actividadEliminada = mapaActividades.remove(codigo);
            if (actividadEliminada != null) {
                refrescarPanelActividades(mapaActividades);
                JOptionPane.showMessageDialog(this, "Actividad '" + actividadEliminada.getNombre() + "' eliminada.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error: No se encontró la actividad para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    

    // --- Main para Ejecución ---

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ventanaAdministrador());
    }
}