
package winniepooh;

public class winnie {
   public static void main(String[] args){
       Winniepooh w1 = new Winniepooh();
       System.out.println("El winnie 1 se llama: " + w1.getNombre());
       
       Winniepooh w2 = new Winniepooh();
       w2.setNombre("aleja");
       System.out.println("El winnie 2 se llama: " + w2.getNombre());
       
       Winniepooh w3 = new Winniepooh();
       w3.toStudy();
   } 
}
