import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Box;

public class Metodos {

    public Metodos() {
    }
    //cargar personita
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

    //cargar todos los usuarios
    public static Map<String, Usuario> cargarMapaUsuarios() {
        String archivo = "Usuarios.dat";
        
        Map<String, Usuario> mapaCargado = new HashMap<>(); 
        
        try (FileInputStream fileIn = new FileInputStream(archivo);
             ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {
            
            
            Object objetoLeido = objectIn.readObject();
            mapaCargado = (Map<String, Usuario>) objetoLeido;

            System.out.println("✅ Mapa de Usuarios cargado exitosamente de: " + archivo);

        } catch (FileNotFoundException e) {
            System.err.println("⚠️ ERROR: El archivo " + archivo + " no fue encontrado. Devolviendo mapa vacío.");
        } catch (IOException i) {
            System.err.println("⚠️ ERROR de I/O al cargar el archivo " + archivo + ": " + i.getMessage());
        } catch (ClassNotFoundException c) {
            System.err.println("⚠️ ERROR: Una o más clases (Usuario) no fueron encontradas en el archivo.");
        } catch (ClassCastException cce) {
             System.err.println("⚠️ ERROR: El objeto en el archivo " + archivo + " no es de tipo Map<String, Usuario>.");
        }

        return mapaCargado;
    }

    //cargar todas las actividades
    public static Map<Integer, Actividad> cargarMapaActividades() {
        String archivo = "Actividades.dat";
       
        Map<Integer, Actividad> mapaCargado = new HashMap<>();

        try (FileInputStream fileIn = new FileInputStream(archivo);
             ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {
            
            
            Object objetoLeido = objectIn.readObject();
            mapaCargado = (Map<Integer, Actividad>) objetoLeido;

            System.out.println("✅ Mapa de Actividades cargado exitosamente de: " + archivo);

        } catch (FileNotFoundException e) {
            System.err.println("⚠️ ERROR: El archivo " + archivo + " no fue encontrado. Devolviendo mapa vacío.");
        } catch (IOException i) {
            System.err.println("⚠️ ERROR de I/O al cargar el archivo " + archivo + ": " + i.getMessage());
        } catch (ClassNotFoundException c) {
            System.err.println("⚠️ ERROR: Una o más clases (Actividad) no fueron encontradas en el archivo.");
        } catch (ClassCastException cce) {
             System.err.println("⚠️ ERROR: El objeto en el archivo " + archivo + " no es de tipo Map<Integer, Actividad>.");
        }

        return mapaCargado;
    }

    //metodo para guardar la informacion de la persona
    public static void guardarMapaUsuarios(
            Map<String, Usuario> mapaUsuarios, 
            Usuario usuarioModificado, 
            String nombreArchivo) 
    {
        if (usuarioModificado == null || usuarioModificado.getUsername() == null) {
            System.err.println("ERROR: El Usuario modificado es nulo o no tiene nombre de usuario. Operación de guardado abortada.");
            return; // Sale del método si los datos son inválidos
        }
        String username = usuarioModificado.getUsername();
        // remplazo
        if (mapaUsuarios.containsKey(username)) {
            mapaUsuarios.put(username, usuarioModificado);
            System.out.println("🔑 Usuario '" + username + "' actualizado en el mapa.");
        } else {
            System.err.println("⚠️ Advertencia: El Usuario '" + username + "' no se encontró en el mapa. Agregando nuevo usuario.");
            mapaUsuarios.put(username, usuarioModificado);
        }

        // 2. Serializar 
        try (FileOutputStream fileOut = new FileOutputStream(nombreArchivo);
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {

            objectOut.writeObject(mapaUsuarios);
            System.out.println("💾 Éxito: El mapa completo de Usuarios se guardó en: " + nombreArchivo);

        } catch (IOException i) {
            System.err.println("❌ ERROR de I/O al guardar el archivo " + nombreArchivo + ". El mapa NO se guardó.");
            i.printStackTrace(); // Imprime el stack trace para depuración
        }
    }

    //metodo para guardar actividad modificada
    public static void guardarMapaActividades(
            Map<Integer, Actividad> mapaActividades, 
            Actividad actividadModificada, 
            String nombreArchivo) 
    {
        if (actividadModificada == null || actividadModificada.getCodigo() == null) {
            System.err.println("❌ ERROR: La Actividad modificada es nula o no tiene código. Operación de guardado abortada.");
            return; 
        }
        Integer codigo = actividadModificada.getCodigo();
        // 1. Reemplazar 
        if (mapaActividades.containsKey(codigo)) {
            mapaActividades.put(codigo, actividadModificada);
            System.out.println("🔑 Actividad con código '" + codigo + "' actualizada en el mapa.");
        } else {
            System.err.println("⚠️ Advertencia: La Actividad '" + codigo + "' no se encontró en el mapa. Agregando nueva actividad.");
            mapaActividades.put(codigo, actividadModificada);
        }
        // 2. Serializar 
        try (FileOutputStream fileOut = new FileOutputStream(nombreArchivo);
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {
            objectOut.writeObject(mapaActividades);
            System.out.println("💾 Éxito: El mapa completo de Actividades se guardó en: " + nombreArchivo);
        } catch (IOException i) {
            System.err.println("❌ ERROR de I/O al guardar el archivo " + nombreArchivo + ". El mapa NO se guardó.");
            i.printStackTrace(); 
        }
    }

    public static void guardarMapaActividades(
            Map<Integer, Actividad> mapaActividades, 
            String nombreArchivo) 
{
    // 1. Validar 
    if (mapaActividades == null) {
        System.err.println("❌ ERROR: El mapa de Actividades es nulo. Operación de guardado abortada.");
        return; 
    }

    // 2. Serializar 
    try (FileOutputStream fileOut = new FileOutputStream(nombreArchivo);
         ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {

        objectOut.writeObject(mapaActividades);
        System.out.println("💾 Éxito: El mapa completo de Actividades se guardó en: " + nombreArchivo);

    } catch (IOException i) {
        System.err.println("❌ ERROR de I/O al guardar el archivo " + nombreArchivo + ". El mapa NO se guardó.");
        i.printStackTrace(); 
    }
}


//crear metodo para guardar lo que es el Personita.dat
public static void guardarUsuario(
            Usuario usuario, 
            String nombreArchivo) 
{
    // 1. Validar que el objeto a guardar no sea nulo
    if (usuario == null) {
        System.err.println("❌ ERROR: El objeto Usuario es nulo. Operación de guardado abortada.");
        return; 
    }

    // 2. Serializar el objeto Usuario en el archivo
    try (FileOutputStream fileOut = new FileOutputStream(nombreArchivo);
         ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {

        objectOut.writeObject(usuario);
        System.out.println("💾 Éxito: El objeto Usuario se guardó en: " + nombreArchivo);

    } catch (IOException i) {
        System.err.println("❌ ERROR de I/O al guardar el archivo " + nombreArchivo + ". El Usuario NO se guardó.");
        i.printStackTrace(); 
    }
} 

//hacer un mapa a una lista
public static List<Actividad> convertirMapaALista(Map<Integer, Actividad> mapaActividades) {
    return new ArrayList<>(mapaActividades.values());
}

//metodo para verificar que una actividad nueva no tenga un codigo ya usado 
public static boolean desocupado(Map<Integer, Actividad> mapaActividades, Actividad actividad) {
    Integer codigoActividad = actividad.getCodigo();
    return !mapaActividades.containsKey(codigoActividad);
}


}