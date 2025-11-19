import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.SwingUtilities;

public class Main {

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