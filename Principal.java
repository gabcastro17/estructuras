import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.TitledBorder;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Principal extends JFrame {
    private static Integer dunabsFull=10000;

    // metodo de traer al objeto de Personita.dat
    public static Usuario cargarPersonita() {
        String archivo3 = "Personita.dat";
        Usuario personitaCargada = null;
        try (FileInputStream fileIn = new FileInputStream(archivo3);
                ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {
            Object objetoLeido = objectIn.readObject();
            personitaCargada = (Usuario) objetoLeido;

            System.out.println("Objeto 'Personita' cargado exitosamente de: " + archivo3);

        } catch (FileNotFoundException e) {
            System.err.println("ERROR: El archivo " + archivo3 + " no existe. Se devolverá null.");
        } catch (IOException i) {
            System.err.println("ERROR de I/O al cargar el archivo: " + i.getMessage());
        } catch (ClassNotFoundException c) {
            System.err.println(" ERROR: La clase Usuario no fue encontrada.");
        } catch (ClassCastException cce) {
            System.err.println(" ERROR: El objeto en el archivo no es de tipo Usuario.");
        }

        return personitaCargada;
    }

    // aqui se globaliza el objeto usuairo guardandolo en el de estudiante
    Usuario estudiante = cargarPersonita();

    // --- 1. DEFINICIÓN DE COLORES DE TEMA ---

    // Colores del Tema CLARO (DEFAULT)
    private static final Color CLARO_MORADO = new Color(75, 0, 130);
    private static final Color CLARO_HEADER = Color.WHITE;
    private static final Color CLARO_FONDO = new Color(245, 245, 245);
    private static final Color CLARO_TEXTO_PRIMARIO = Color.BLACK;
    private static final Color CLARO_PANEL_CONTENIDO = Color.WHITE;
    private static final Color CLARO_BOTON_FONDO = new Color(230, 230, 230);

    // Colores del Tema OSCURO
    private static final Color OSCURO_FONDO_PRINCIPAL = new Color(30, 30, 30);
    private static final Color OSCURO_HEADER_Y_PANEL = new Color(45, 45, 45);
    private static final Color OSCURO_MORADO = new Color(50, 0, 90);
    private static final Color OSCURO_TEXTO = Color.WHITE;
    private static final Color OSCURO_BOTON_FONDO = new Color(70, 70, 70);

    private boolean temaOscuroActivo = false;

    // Referencias a los paneles principales para el cambio de tema
    private JPanel headerPanel;
    private JPanel sidebarPanel;
    private JPanel datosPanel;

    // --- CONSTRUCTOR ---
    public Principal() {
        setTitle("DUNAB Hub - Panel Principal - Estudiantes");
        setSize(1280, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // 1. HEADER SUPERIOR (NORTH)
        headerPanel = crearHeader();
        add(headerPanel, BorderLayout.NORTH);

        // 2. CONTENIDO PRINCIPAL (CENTER)
        JPanel contenidoPrincipal = new JPanel(new BorderLayout());

        // 2.1 Columna Morada (WEST) - Sidebar
        sidebarPanel = crearSidebar();
        contenidoPrincipal.add(sidebarPanel, BorderLayout.WEST);

        // 2.2 Panel de Datos y Gráficos (CENTER)
        datosPanel = crearPanelDatos();
        contenidoPrincipal.add(datosPanel, BorderLayout.CENTER);

        add(contenidoPrincipal, BorderLayout.CENTER);

        // Aplica el tema inicial (Claro)
        aplicarTema(false);

        setVisible(true);
    }

    // --- MÉTODOS DE ESTRUCTURA Y COMPONENTES ---

    private JPanel crearHeader() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        panel.setPreferredSize(new Dimension(this.getWidth(), 60));

        JButton bCalculadora = new JButton("🧮");
        bCalculadora.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        bCalculadora.setToolTipText("Calculadora de Promedios");
        panel.add(bCalculadora);
        JButton bHorario = new JButton("⏱️");
        bHorario.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        bHorario.setToolTipText("Horario");
        panel.add(bHorario);
        JButton bActividades = new JButton("Actividades");
        bActividades.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        bActividades.setToolTipText("Horario");
        panel.add(bActividades);


        JButton bTema = new JButton("🌙");
        bTema.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        bTema.setToolTipText("Alternar tema oscuro");
        panel.add(bTema);

        // LÓGICA DE TEMA OSCURO
        bTema.addActionListener(e -> {
            temaOscuroActivo = !temaOscuroActivo;
            aplicarTema(temaOscuroActivo);
            bTema.setText(temaOscuroActivo ? "☀️" : "🌙");
        });
        //logica botones
        bCalculadora.addActionListener(e -> {
            PruebaCalculadora ventanaprincipal = new PruebaCalculadora();
            ventanaprincipal.setVisible(true);
        });
        bHorario.addActionListener(e -> {
            CalendarioClases ventanaprincipal = new CalendarioClases();
            ventanaprincipal.setVisible(true);
        });
        bActividades.addActionListener(e -> {
            //toca primero hacer el metodo de llamar las actividades del .dat y ponerlas en un mapa
            //por eso se llama al mapa externo de activides guardada en base de datos
            //ahora si el objeto esta listo para hacer presencia
            VentanaActividades ventanaprincipal = new VentanaActividades(ActividadesEnBaseDeDatos);
            ventanaprincipal.setVisible(true);
        });


        return panel;
    }

    private JPanel crearSidebar() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setPreferredSize(new Dimension(300, this.getHeight()));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 15, 5, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.10;
        int row = 0;

        // 1. Logo
        JLabel logo = new JLabel("DUNAB Hub", SwingConstants.CENTER);
        logo.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridy = row++;
        panel.add(logo, gbc);

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
        gbc.gridy = row++;
        panel.add(lblLogo,gbc);

        // 2. Foto de Usuario (Placeholder)
        String rutaImagen = estudiante.getLinkImagen();
        ImageIcon iconoOriginal = new ImageIcon(rutaImagen);

        // 2. Obtener la imagen y escalarla a 120x120
        Image imagen = iconoOriginal.getImage();
        // SCALED_SMOOTH proporciona una mejor calidad de escalado
        Image imagenEscalada = imagen.getScaledInstance(120, 120, Image.SCALE_SMOOTH);

        // 3. Crear el nuevo ImageIcon con la imagen escalada
        ImageIcon iconoEscalado = new ImageIcon(imagenEscalada);

        // 4. Crear el JLabel y asignarle el icono escalado
        JLabel fotoUsuario = new JLabel(iconoEscalado, SwingConstants.CENTER);

        // No es necesario setPreferredSize si ya asignas un icono de 120x120
        // fotoUsuario.setPreferredSize(new Dimension(120, 120));
        fotoUsuario.setOpaque(true);
        fotoUsuario.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        fotoUsuario.setOpaque(true);
        fotoUsuario.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        gbc.fill = GridBagConstraints.NONE;
        gbc.weighty = 0.0;
        gbc.gridy = row++;
        panel.add(fotoUsuario, gbc);

        // 3. Título "Información Personal"
        // Configuramos el relleno horizontal para que las etiquetas ocupen el ancho de
        // la columna
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0.0; 
        // ---  "INFORMACIÓN" ---
        JLabel lblInformacion = new JLabel("INFORMACIÓN", SwingConstants.CENTER);
        lblInformacion.setFont(new Font("Arial", Font.BOLD, 18)); 
        lblInformacion.setForeground(new Color(123, 44, 191)); 
        gbc.gridy = row++; 
        panel.add(lblInformacion, gbc);
        // --- "PERSONAL" ---
        JLabel lblPersonal = new JLabel("PERSONAL", SwingConstants.CENTER);
        lblPersonal.setFont(new Font("Arial", Font.BOLD, 18)); // Mantenemos el mismo tamaño
        lblPersonal.setForeground(new Color(90, 24, 154)); // Opcional: añade otro color
        gbc.gridy = row++; // Ubicación en la siguiente fila, luego incrementamos 'row'
        panel.add(lblPersonal, gbc);

        // 4. Información de la Persona
        JTextArea infoPersona = new JTextArea("Nombre: " + estudiante.getNombreCompleto() + "\n"
                + "Correo: " + estudiante.getCorreo() + "\n"
                + "Usuario: " + estudiante.getUsername() + "\n"
                + "ID UNAB: " + estudiante.getID() + "\n" + "Carrera: " + estudiante.getCarrera() + "\n" +
                "Semestre: " + estudiante.getSemestre());
        infoPersona.setEditable(false);
        gbc.gridy = row++;
        panel.add(infoPersona, gbc);

        

        return panel;
    }

    private JPanel crearPanelDatos() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(CLARO_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 1. Plata Disponible (NORTH)
        JPanel panelPlata = crearPanelPlata();
        panel.add(panelPlata, BorderLayout.NORTH);

        // 2. Contenedor para la tabla y el calendario (CENTER)
        JPanel panelInferior = new JPanel();
        panelInferior.setLayout(new BoxLayout(panelInferior, BoxLayout.Y_AXIS));
        panelInferior.setBackground(CLARO_FONDO);

        // Espacio para separar la sección Plata del resto
        panelInferior.add(Box.createVerticalStrut(10));

        // 2.1 Tabla de Promedios
        JPanel panelTabla = crearPanelTabla();
        panelInferior.add(panelTabla);

        // Espacio para acercar la tabla y el calendario
        panelInferior.add(Box.createVerticalStrut(15));

        // 2.2 Calendario
        JPanel panelCalendario = crearPanelCalendario();
        panelInferior.add(panelCalendario);


        panel.add(panelInferior, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelPlata() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Cantidad de Plata Disponible"));

        panel.add(new JLabel("  Total DUNABs: "+estudiante.getDunabs()+" dunabs"));
        panel.add(new JLabel("  DUNABs para completar meta:"));
        panel.add(new JLabel("   Fracción: "+estudiante.getDunabs()+"/"+dunabsFull));
        double resultadoDecimal = ((double) estudiante.getDunabs() / dunabsFull) * 100;
        String porcentajeFormateado = String.format("%.2f", resultadoDecimal);
        panel.add(new JLabel("   Porcentaje: "+porcentajeFormateado+"%"));
        Integer resta = dunabsFull-estudiante.getDunabs();
        panel.add(new JLabel("   Faltante: "+resta+" dunabs"));

        return panel;
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columnas = { "Período", "Promedio Recibido de Dunabs" };
        Object[][] datos = {
                { "Semana", "0" },
                { "Mes", "0" },
                { "Semestre", "0" },
                { "Año", "0" }
        };

        JTable tabla = new JTable(new DefaultTableModel(datos, columnas));
        tabla.setFillsViewportHeight(true);
        tabla.setRowHeight(25); // Filas más compactas

        JScrollPane scrollPane = new JScrollPane(tabla);
        // Ajustamos la altura preferida del JScrollPane para compactar la tabla
        scrollPane.setPreferredSize(new Dimension(scrollPane.getPreferredSize().width, 150));
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createTitledBorder("Promedio de Dunabs Recibido"));

        // Ajuste para BoxLayout
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        return panel;
    }

    // Método que simula la funcionalidad de JCalendar (requiere librería externa)
    private JPanel crearPanelCalendario() {
        JPanel panel = new JPanel(new BorderLayout());

        panel.setBorder(BorderFactory.createTitledBorder("Calendario: Noviembre 2025"));

        // Usamos un placeholder que simula JCalendar (con las celdas más grandes)
        JPanel placeholder = crearPlaceholderJCalendar();
        panel.add(placeholder, BorderLayout.CENTER);

        // Ajuste para BoxLayout
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        return panel;
    }

    // Placeholder que se usa si no se tiene la librería JCalendar
    private JPanel crearPlaceholderJCalendar() {
        JPanel panel = new JPanel(new BorderLayout());

        // Título que indica la necesidad de flechas funcionales
        JLabel tituloMes = new JLabel(" Noviembre 2025", SwingConstants.CENTER);
        tituloMes.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(tituloMes, BorderLayout.NORTH);

        // SIMULACIÓN DE CALENDARIO USANDO JTABLE
        String[] diasSemana = { "Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb" };
        Object[][] fechas = new Object[6][7];

        JTable calendario = new JTable(fechas, diasSemana);
        calendario.setRowHeight(40); // Celdas más altas
        calendario.setEnabled(false);

        JScrollPane scrollPaneCalendario = new JScrollPane(calendario);

        // Eliminamos las restricciones de tamaño para que ocupe el espacio vertical
        // restante
        scrollPaneCalendario.setPreferredSize(null);
        scrollPaneCalendario.setMaximumSize(null);

        panel.add(scrollPaneCalendario, BorderLayout.CENTER);

        return panel;
    }

    // --- LÓGICA DE APLICACIÓN DE TEMA ---

    private void aplicarTema(boolean oscuro) {
        // Selecciona los colores según el tema
        Color fondoPrincipal = oscuro ? OSCURO_FONDO_PRINCIPAL : CLARO_FONDO;
        Color panelColor = oscuro ? OSCURO_HEADER_Y_PANEL : CLARO_PANEL_CONTENIDO;
        Color sidebarColor = oscuro ? OSCURO_MORADO : CLARO_MORADO;
        Color textoGeneral = oscuro ? OSCURO_TEXTO : CLARO_TEXTO_PRIMARIO;

        // 1. Aplica color al Frame
        getContentPane().setBackground(fondoPrincipal);

        // 2. Aplica colores a los paneles principales
        headerPanel.setBackground(panelColor);
        sidebarPanel.setBackground(sidebarColor);
        datosPanel.setBackground(fondoPrincipal);

        // 3. Método recursivo para cambiar componentes internos
        cambiarColoresRecursivamente(headerPanel, panelColor, textoGeneral, oscuro);
        cambiarColoresRecursivamente(sidebarPanel, sidebarColor, textoGeneral, oscuro);
        cambiarColoresRecursivamente(datosPanel, fondoPrincipal, textoGeneral, oscuro);

        repaint();
        revalidate();
    }

    private void cambiarColoresRecursivamente(Container container, Color fondo, Color textoGeneral, boolean oscuro) {
        for (Component comp : container.getComponents()) {
            Color colorFondoComp = fondo;
            Color colorTextoComp = textoGeneral;

            // CORRECCIÓN: Si estamos en el SIDEBAR y el tema es CLARO, forzar texto BLANCO
            if (container == sidebarPanel && !oscuro) {
                colorTextoComp = Color.WHITE;
            }

            if (comp instanceof JPanel) {
                if (comp.getParent() == sidebarPanel) {
                    // Columna Morada
                    colorFondoComp = oscuro ? OSCURO_MORADO : CLARO_MORADO;
                } else if (comp.getParent() == datosPanel && comp != headerPanel) {
                    // Paneles de datos (Plata Disponible, Tabla, Calendario)
                    colorFondoComp = oscuro ? OSCURO_HEADER_Y_PANEL : CLARO_PANEL_CONTENIDO;
                } else {
                    colorFondoComp = fondo;
                }
                comp.setBackground(colorFondoComp);

                // Manejo de TitledBorder
                if (((JPanel) comp).getBorder() instanceof TitledBorder) {
                    ((TitledBorder) ((JPanel) comp).getBorder()).setTitleColor(colorTextoComp);
                }

            } else if (comp instanceof JScrollPane) {
                comp.setBackground(fondo);
            } else if (comp instanceof JLabel) {
                comp.setForeground(colorTextoComp);
            } else if (comp instanceof JButton) {
                comp.setBackground(oscuro ? OSCURO_BOTON_FONDO : CLARO_BOTON_FONDO);
                comp.setForeground(colorTextoComp);
            } else if (comp instanceof JTextArea) {
                // Info personal en sidebar
                comp.setBackground(oscuro ? OSCURO_MORADO.darker() : CLARO_MORADO.darker());
                comp.setForeground(colorTextoComp);
            } else if (comp instanceof JTable) {
                // Configuración de Tablas
                ((JTable) comp).setBackground(oscuro ? new Color(60, 60, 60) : Color.WHITE);
                ((JTable) comp).setForeground(colorTextoComp);
                ((JTable) comp).getTableHeader()
                        .setBackground(oscuro ? new Color(50, 50, 50) : new Color(230, 230, 230));
                ((JTable) comp).getTableHeader().setForeground(colorTextoComp);
            }

            if (comp instanceof Container) {
                cambiarColoresRecursivamente((Container) comp, colorFondoComp, colorTextoComp, oscuro);
            }
        }
    }

    // --- PUNTO DE ENTRADA DEL PROGRAMA ---
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Principal());
    }

    //metodo de poner en un mapa los datos del Actividades.dat
    public static Map<Integer, Actividad> leerMapaDesdeArchivo(String nombreArchivo) {
        Map<Integer, Actividad> mapaActividades = new HashMap<>();
        try (FileInputStream fis = new FileInputStream(nombreArchivo);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            Object objetoLeido = ois.readObject();
            if (objetoLeido instanceof Map) {
                mapaActividades = (Map<Integer, Actividad>) objetoLeido;
            } else {
                System.err.println("El objeto leído no es un Map.");
            }

        } catch (IOException e) {
            System.err.println("Error de entrada/salida al leer el archivo: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Clase Actividad no encontrada durante la deserialización: " + e.getMessage());
            e.printStackTrace();
        }

        return mapaActividades;
    }
    private static Map<Integer, Actividad> ActividadesEnBaseDeDatos = leerMapaDesdeArchivo("Actividades.dat");
}