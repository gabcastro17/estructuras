import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

import java.util.HashMap;
import java.util.Map;

public class InicioSesion extends JFrame {
    // creo un usuario objeto predeterminado
    private static Usuario personita;

    // crear el mapa a usar
    private static Object objetoUsuariosCargados = cargarMapaDeArchivo("Usuarios.dat"); // este para el cargado
    private static Map<String, Usuario> usuariosCargados = (Map<String, Usuario>) objetoUsuariosCargados; // ese
                                                                                                          // almacena l
                                                                                                          // oque el
                                                                                                          // objeto
                                                                                                          // estrajo del
                                                                                                          // .dat

    // llamo a el .dat de usuarios para crear el mapa
    // por tanto, solo vamos a leer
    public static Object cargarMapaDeArchivo(String nombreArchivo) {
        Object mapaCargado = null;
        try (FileInputStream fileIn = new FileInputStream(nombreArchivo);
                ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {
            mapaCargado = objectIn.readObject();
        } catch (FileNotFoundException e) {
            System.err.println("❌ ERROR: El archivo " + nombreArchivo + " no fue encontrado.");
        } catch (IOException i) {
            System.err.println("❌ ERROR de I/O al cargar " + nombreArchivo + ": " + i.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("❌ ERROR: Clase no encontrada durante la deserialización.");
        }
        return mapaCargado;
    }

    // creo metodo de verificacion de ingresar usuario
    public static boolean verificarUsuario(String username, String password) {
        if (usuariosCargados.containsKey(username)) {
            personita = usuariosCargados.get(username);
            if (personita.getPassword().equals(password)) {
                return true;
            } else {

                return false;
            }
        } else {
            return false;
        }

    }

    // metodo para poner la imagen de fondo
    private static BufferedImage cargarImagenFondo(String ruta) {
        try {
            return ImageIO.read(new File(ruta));
        } catch (IOException e) {
            System.err.println("error al subir la iamgen de fondo");
            System.err.println("Ruta: " + ruta + ". Mensaje: " + e.getMessage());
            return null; // Devuelve null si falla la carga
        }
    }

    // variables globales
    private JTextField campoUsername;
    private JPasswordField campoContrasena;
    private JLabel msjError;

    // crear la interfaz
    public InicioSesion() {
        // 1. se hizo la configuracion de la pagina tradicional
        setTitle("Dunab Hub - Iniciar Sesión");
        setSize(1280, 832);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel de fondo de la imagen para hacer fondo de imagen
        JPanel fondoPanel = new JPanel(new GridBagLayout()) {
            private final BufferedImage imagenFondo = cargarImagenFondo("src\\banuplazoleta.jpg");

            // se sobre escribe porque se añade algo al panel ya creado
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imagenFondo != null) {
                    // escala la imagen
                    g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
                } else {
                    // por is acaso, se pone un color en el fondo si falla
                    g.setColor(new Color(50, 50, 50));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };

        // ahora vamos a hacer el cuadrado blanco del centro
        JPanel formularioPanel = new JPanel();
        // se usa gridbaglayout para organizar los componentes
        formularioPanel.setLayout(new GridBagLayout());
        // tamaño fijo del formulario. talvez lo modifique mas tarde
        formularioPanel.setPreferredSize(new Dimension(500, 600));
        // se establece color blanco y semitransparente
        formularioPanel.setBackground(new Color(255, 255, 255, 220));
        // GridBagConstraints y contador de fila
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        int row = 0;

        // se pone logo

        JLabel logoLabel = new JLabel("LOGO", SwingConstants.CENTER);
        ImageIcon logoIcono = null;
        Image logoImagen = null;

        try {
            // se llama iamgen
            logoImagen = ImageIO.read(new File("src\\logo java dunab.png"));

            // se escala la imagen
            Image imagenEscalada = logoImagen.getScaledInstance(250, 250, Image.SCALE_SMOOTH);
            logoIcono = new ImageIcon(imagenEscalada);

            // hay un jholder el cual se cambia por el logo imagen
            logoLabel.setIcon(logoIcono);
            logoLabel.setText(null);

        } catch (IOException e) {
            System.err.println(" Error al cargar o escalar el logo. Usando texto placeholder.");
            // logoLabel ya está inicializado con "LOGO", por lo que no hay error de
            // compilación.
        }

        gbc.gridx = 0;
        gbc.gridy = row++; // Coloca el logo y avanza la fila
        gbc.gridwidth = 2; // Ocupa dos columnas
        gbc.fill = GridBagConstraints.NONE; // No expandir el logo
        gbc.insets = new Insets(20, 10, 5, 10); // Márgenes para el logo
        formularioPanel.add(logoLabel, gbc);

        gbc.insets = new Insets(10, 10, 10, 10); // Restaura los márgenes

        // ahora lo de poner en la interfaz del cuadrado blanco
        JLabel titulo = new JLabel("Iniciar Sesión", SwingConstants.CENTER);
        titulo.setFont(new Font("Serif", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formularioPanel.add(titulo, gbc);

        // aqui aparece el mensaje de error
        msjError = new JLabel("", SwingConstants.CENTER);
        msjError.setForeground(Color.RED);
        gbc.gridy = row++;
        formularioPanel.add(msjError, gbc);

        gbc.gridwidth = 1;

        // username
        // Label
        gbc.gridx = 0;
        gbc.gridy = row;
        formularioPanel.add(new JLabel("Usuario:"), gbc);

        // TextField
        gbc.gridx = 1;
        campoUsername = new JTextField(20);
        formularioPanel.add(campoUsername, gbc);
        row++;

        // password-
        // Label
        gbc.gridx = 0;
        gbc.gridy = row;
        formularioPanel.add(new JLabel("Contraseña:"), gbc);

        // PasswordField
        gbc.gridx = 1;
        campoContrasena = new JPasswordField(20);
        formularioPanel.add(campoContrasena, gbc);
        row++;

        // botones

        // boton ingresar
        JButton bAcceder = new JButton("Iniciar Sesion");
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        formularioPanel.add(bAcceder, gbc);
        row++;

        // Botón "Registrarme"
        JButton bRegistrarse = new JButton("Registrarme");
        gbc.gridy = row;
        formularioPanel.add(bRegistrarse, gbc);
        row++;

        // ultima parte, es cuadno se activa todo
        // se activa el cuadrado blanco
        fondoPanel.add(formularioPanel);
        // se activa el fondo DE PANTALLA DEL PANEL
        setContentPane(fondoPanel);
        // Muestra la ventana, ultima parte
        setVisible(true);

        // ahora los listeneres o sea que pasa cuando se activa ls botones
        bAcceder.addActionListener(e -> {
            String user = campoUsername.getText();
            // obtener lo que la persona puso en username y en paswaord
            String pass = new String(campoContrasena.getPassword());

            if (verificarUsuario(user, pass)) {
                msjError.setText("¡Acceso exitoso!");
                // se abre ventana administrador si es administrador
                // se abre ventana de estudiante si no es administrador
                if(personita.isAdministrador()){
                    dispose();
                    ventanaAdministrador ventanaAdministrador = new ventanaAdministrador();
                    ventanaAdministrador.setVisible(true); //lo tengo que cambiar para el administrador
                }else{
                    guardarPersonita(personita);
                    dispose();
                    Principal ventanaprincipal = new Principal();
                    ventanaprincipal.setVisible(true);
                }

            } else {
                msjError.setText("Usuario o contraseña incorrectos. No puedes pasar");
                campoUsername.setText("");
                campoContrasena.setText("");
            }
        });
        bRegistrarse.addActionListener(e -> {
            // se abre pestaña registro y se cierra inicio sesion
            dispose();
            VentanaRegistro ventanaRegistro = new VentanaRegistro();
            ventanaRegistro.setVisible(true);
        });

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InicioSesion().setVisible(true));
    }

    //metodo de guardado del punto dat
    public static void guardarPersonita(Usuario personita){
        String archivo3 = "Personita.dat";
        try (FileOutputStream fileOut = new FileOutputStream(archivo3);
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) 
        {
            
            objectOut.writeObject(personita);
            
            System.out.println(" Objeto '" + personita.getNombreCompleto() + "' guardado exitosamente en: " + archivo3);

        } catch (IOException i) {
            System.err.println("ERROR: No se pudo guardar el archivo " + archivo3);
            i.printStackTrace();
        }
    }
    
}
