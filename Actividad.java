import java.time.LocalDateTime;//esto es para mejor organizacion de las fechas de las actividades ya que la fecha es parte del objeto Actividad
import java.util.HashMap;
import java.util.Map;// este es para crear mapas
//se decidio usar mapas ya que los mapas son mas rapidos 
import java.io.Serializable; // Importación necesaria
import java.time.format.DateTimeFormatter;


public class Actividad implements Serializable {

    //atributos

    private String nombre; //nombre de la actividad
    private String descripcion;//descripcion de la actividad
    private LocalDateTime fecha;//fecha de la actividad usando LocalDateTime
    private int cantidadDunabs;//cantidad de dunabs que da
    private int capacidadMax; //cantidad maxima de gente para la actividad
    private int asistentesActuales;//cantidades de personas actuales que estan inscritos a la actividad
    private Integer codigo;//codigo de reconocimiento de la actividad
    private String lugar;
    private String linkImagen;//va a ser lo mismo que el usuario
    
    //constructor
    
    public Actividad(String nombre, String descripcion, LocalDateTime fecha, int cantidadDunabs, int capacidadMax, Integer codigo,String lugar) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.cantidadDunabs = cantidadDunabs;
        this.capacidadMax = capacidadMax;
        this.asistentesActuales = 0;
        this.codigo = codigo;
        this.lugar = lugar;
        this.linkImagen = "";
    }

    //getters and setters tradicionales

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public LocalDateTime getFecha() {
        return fecha;
    }
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
    public int getCantidadDunabs() {
        return cantidadDunabs;
    }
    public void setCantidadDunabs(int cantidadDunabs) {
        this.cantidadDunabs = cantidadDunabs;
    }
    public int getCapacidadMax() {
        return capacidadMax;
    }
    public void setCapacidadMax(int capacidadMax) {
        this.capacidadMax = capacidadMax;
    }
    public int getAsistentesActuales() {
        return asistentesActuales;
    }
    public void setAsistentesActuales(int asistentesActuales) {
        this.asistentesActuales = asistentesActuales;
    }
    public Integer getCodigo() {
        return codigo;
    }
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
    

    //creo los metodos de simon
    

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    
    
}