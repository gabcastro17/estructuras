import java.io.File;
import java.io.IOException;
//creee este programa para cuando en terminos de prueba, poder eliminar los dos archivos de .dat
public class LimpiarTodo {

    //metodo void para limpiar los archivos como funcion
    public static void limpiarArchivo(String nombreArchivo) {
        //crear variable de archivo
        File archivo = new File(nombreArchivo);

        System.out.println("-> Intentando limpiar: " + nombreArchivo);

        // verifica existencia del archivo
        if (archivo.exists()) {
            // si efectivamente se pudo eliminar el archivo
            if (archivo.delete()) {
                System.out.println("  ✅ ÉXITO: Archivo '" + nombreArchivo + "' limpiado (eliminado).");
            } else {
                // si hay fallo en la eliminacion o limpieza del archivo
                System.err.println("  ❌ ERROR: No se pudo eliminar el archivo '" + nombreArchivo + "'.");
                System.err.println("  Asegúrese de que la aplicación principal no esté ejecutándose.");
            }
        } else {
            // si el archivo no existe
            System.out.println("  ℹ️ INFO: El archivo '" + nombreArchivo + "' no existía. No se requiere limpieza.");
        }
    }
    
    // --- Método Main para Ejecutar la Limpieza ---
    
    public static void main(String[] args) {
        
        System.out.println("  INICIANDO ELIMINACIÓN DE DATOS");
        
        
        // Ejecutar la limpieza para ambos archivos
        limpiarArchivo("Usuarios.dat");
        limpiarArchivo("Actividades.dat");
        
        System.out.println("\n--- se pudo limpiar los dos archivos ---\n");
    }
}