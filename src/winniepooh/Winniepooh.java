package winniepooh;
public class Winniepooh {
    //Atributos
    public String nombre="gaby";
    public int edad=17;
    public String genero="femenino";
    public int dianacimiento=17;
    public String nacionalidad="colombiana";
     //Constructor
    public Winniepooh(){
}       //get y set de nombre
        public String getNombre(){
            return nombre;
}
        public void setNombre(String nombre){
            this.nombre=nombre;
        }
        // get y set de edad
        public int getEdad(){
            return edad;
        }
        public void setEdad(int edad){
            this.edad=edad;
        }
        //get y set de genero
        public String getGenero(){
            return genero;
        }
        public void setGenero(String genero){
            this.genero=genero;
        }
        //get y set de dia de nacimiento
        public int getDianacimiento(){
           return dianacimiento;
        }
        public void setDianacimiento(int dianacimiento){
            this.dianacimiento=dianacimiento;
        }
        //get y set de nacionalidad
        public String getNacionalidad(){
            return nacionalidad;
        }
       public void setNacionalidad(String nacionalidad){
           this.nacionalidad=nacionalidad;
       }
    //Metodo
    public void toStudy(){
        System.out.print("Les gusta la miel");
    }
    }
    

