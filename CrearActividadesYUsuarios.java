import java.io.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Arrays;
//programa para crear los dato donde vamos a guardar los usuarios y actividades
//se crea para verificar eficacia y que ya se tenga usuarios creados
import java.util.HashMap;
import java.util.Map;

public class CrearActividadesYUsuarios {
    
    public static void main(String[] args) {
        System.out.println("hola");
        
        // creo datos o sea los usuarios y las actividaes
        List<Usuario> listaUsuarios = Arrays.asList(
            new Usuario("sjaimes333", "U1", "Contrasena", 1000, "Ingeniería de Sistemas", 3, "Simon", true),
            new Usuario("usuario", "U2", "Contrasena", 2000, "Administración de Empresa", 7, "Pepito perez", false),
            new Usuario("gabriela1", "U3", "Contrasena", 3000, "Ingeniería de Sistemas", 3, "Gabriela", false)
        );
        
        List<Actividad> listaActividades = Arrays.asList(
            new Actividad("Festival de comida", "muchos stands...", LocalDateTime.of(2025, 11, 11, 19, 0), 2000, 300, 36361, "plazoleta principal"),
            new Actividad("Película en Francés", "peliculas romanticas...", LocalDateTime.of(2025, 11, 20, 19, 0), 9000, 20, 36362, "Plazoleta Banu")
        );

        // los vuelvo a mapas con un for. la clave es el username
        
        Map<String, Usuario> mapaUsuarios = new HashMap<>();
        for (Usuario user : listaUsuarios) {
            mapaUsuarios.put(user.getUsername(), user);
        }

        Map<Integer, Actividad> mapaActividades = new HashMap<>();
        for (Actividad acti : listaActividades) {
            mapaActividades.put(acti.getCodigo(), acti);
        }

        // escribo el nombre de los archivos
        String archivoUsuarios = "Usuarios.dat";
        String archivoActividades = "Actividades.dat";

        guardarMapaEnArchivo(mapaUsuarios, archivoUsuarios);
        guardarMapaEnArchivo(mapaActividades, archivoActividades);

        // activo la funcion de guardar el mapa
        
        System.out.println("\n--- INICIO DE CARGA Y VERIFICACIÓN ---");
        
        // Cargar Usuarios
        Object objetoUsuariosCargados = cargarMapaDeArchivo(archivoUsuarios);
        // Cast: Se espera Map<String, Usuario>
        Map<String, Usuario> usuariosCargados = (Map<String, Usuario>) objetoUsuariosCargados;
        
        if (usuariosCargados != null) {
            System.out.println("Carga de Usuarios exitosa. Total: " + usuariosCargados.size());
            // se verifica existencia de usuario y que proceso fue hecho efectivmante
            System.out.println("Usuario 'sjaimes333': " + usuariosCargados.get("sjaimes333"));
        }
        
        // Cargar actividades
        Object objetoActividadesCargadas = cargarMapaDeArchivo(archivoActividades);
        // Cast: Se espera Map<Integer, Actividad>
        Map<Integer, Actividad> actividadesCargadas = (Map<Integer, Actividad>) objetoActividadesCargadas;
        
        if (actividadesCargadas != null) {
            System.out.println("Carga de Actividades exitosa. Total: " + actividadesCargadas.size());
            // verificar que la lectura de la actividad fue hecha perfecgtamente
            Actividad acti3 = actividadesCargadas.get(36361);
            System.out.println("Actividad 36361: " + acti3.getNombre());
        }

    }

    // metodo de guardar el mapa
    public static void guardarMapaEnArchivo(Map<?, ?> mapa, String nombreArchivo) {
        try (FileOutputStream fileOut = new FileOutputStream(nombreArchivo);
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {
            objectOut.writeObject(mapa);
            System.out.println("✅ Mapa guardado exitosamente en " + nombreArchivo);
        } catch (IOException i) {
            System.err.println("❌ ERROR al guardar " + nombreArchivo + ": " + i.getMessage());
        }
    }

    // cargar el mapa del .dat
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
}