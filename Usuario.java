import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;//este se usa para creare el mapa de actividades
import java.util.TreeSet;
import java.util.Collection;
import java.io.Serializable; // Importación necesaria



public class Usuario implements Serializable{
    private String username;// el nombre del correo antes del arroba
    private String correo;// este se crea a partir del username
    private String ID;
    private String password;
    private int dunabs;// duanbs que tien actualmente
    private String carrera;
    private int semestre;
    private String nombreCompleto;
    private boolean administrador; // todo usuario creado no va a ser administrador excepto el que nosotros creemos manualmente
    //voy a poner los codigos de las actividades registradas por el estudiante
    private TreeSet<Integer> actividadesInscritas;
    //aca el link de la foto como atributo
    private String linkImagen;

    // constructor
    public Usuario(String username, String iD, String password, int dunabs, String carrera, int semestre,
            String nombreCompleto, boolean administrador) {
        this.username = username;
        this.correo = username+"@unab.edu.co";
        this.ID = iD;
        this.password = password;
        this.dunabs = dunabs;
        this.carrera = carrera;
        this.semestre = semestre;
        this.nombreCompleto = nombreCompleto;
        this.administrador = administrador;
        this.actividadesInscritas = new TreeSet<>();
        this.linkImagen = "src_fotos_perfil/imagenSinRostro.jpg";
    }

    //getters and setters tradicionales

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getID() {
        return ID;
    }

    public void setID(String iD) {
        ID = iD;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getDunabs() {
        return dunabs;
    }

    public void setDunabs(int dunabs) {
        this.dunabs = dunabs;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public boolean isAdministrador() {
        return administrador;
    }

    public void setAdministrador(boolean administrador) {
        this.administrador = administrador;
    }

    public TreeSet<Integer> getActividadesInscritas() {
        return actividadesInscritas;
    }

    public void setActividadesInscritas(TreeSet<Integer> actividadesInscritas) {
        this.actividadesInscritas = actividadesInscritas;
    }

    public void addActividadU(Integer codigo){
        actividadesInscritas.add(codigo);
    }

    public String getLinkImagen() {
        return linkImagen;
    }

    public void setLinkImagen(String linkImagen) {
        this.linkImagen = linkImagen;
    }

    

}
