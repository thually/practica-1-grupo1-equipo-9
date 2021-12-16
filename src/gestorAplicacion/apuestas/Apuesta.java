/*
 * La clase Apuesta tiene como finalidad realizar todos los procesos que una apuesta comun involucra:
 * - Ingresar a los participantes.
 * - Repartir premios a los ganadores.
 * 
 * Cada apuesta se asigna a su respectiva Pelea.
 * En una Apuesta puede participar Apostadores los cuales son en concreto Guardias o Prisioneros.
 * En ella participan las clases Pelea y Apostador.
 * Tiene los siguientes atributos:
 * - codigo (int): Permite la identificacion de cada apuesta (es el mismo que posee la clase Pelea).
 * - apostadores (ArrayList<Object[3]): Recibe una lista de listas, donde cada lista interna tiene:
 *     - apostador (Apostador): La persona que quiere apostar.
 *     - prisionero (Prisionero): Peleador (luchador) a quien se le apuesta.
 *     - apuesta (int): Monto que desea apostar.
 * - pelea (Pelea): La pelea a la cual esta dirigida la apuesta.
 * - montoTotal (double): Suma total de dinero recogida entre todos los apostadores.
 * - montoTotalGanadores (double): Suma total recogida entre los ganadores.
 * - estadisticas (ArrayList<String>): Lista de Strings, la cual servira para conocer los resultados de la apuesta.  
 */

package gestorAplicacion.apuestas;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

import gestorAplicacion.carcel.Prisionero;

public class Apuesta implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private static Hashtable<Integer,Apuesta> apuestas= new Hashtable<Integer,Apuesta>();

	private int codigo; //Cada pelea tiene el mismo codigo que su respectiva apuesta.
    private ArrayList<Object[]> apostadores= new ArrayList<Object[]>(); 
    private Pelea pelea;
    private double montoTotal;
    private double montoTotalGanadores;
    private ArrayList<String> estadisticas;
    
    /*
     * Las apuestas se crean automaticamente cuando se crea una pelea.
     * Cada vez que es creado una apuesta, esta es agragada a una lista general de apuestas.
     */
    public Apuesta(int codigo, Pelea pelea) {	
		this.codigo = codigo;
		this.pelea = pelea;
		apuestas.put(codigo, this);
	}

    /*
     * Retorna un resumen general de las estadisticas de las apuestas:
     * - Muestra el monto total apostado que se repartira entre los ganadores.
     * - Si la pelea aun no tiene ganador.
     * - Si la pelea no tuvo apostadores.
     */
	public String resultadoApuesta() {
		if (pelea.getGanador() == null) {return "La pelea aun no tiene ganador";}
		if (getApostadores().isEmpty()) {return "La pelea con codigo " + getCodigo() + "no tuvo apuestas";}
		
		String resultadoMonto1 = "El monto total recogido en la apuesta fue: " + montoTotal + "\n" ;
		String resultadoMonto2 = "El dinero total apostado por los ganadores de esta apuesta fue: " + montoTotalGanadores + "\n\n" ;
		
		String resulta3 = "Las estadisticas de esta apuesta son las siguientes: \n";
		String resulta4 = "";
		for (String string : estadisticas) {
			resulta4 += string + "\n"; 
		}
		
    	return resultadoMonto1 + resultadoMonto2 + resulta3 + resulta4;
    }
    
	/*
	 * El metodo resolverApuesta funciona con la siguiente logica:
	 * - Primero se encuentra cual es el monto total recogido entre los apostadores y los ganadores.
	 * - Luego se les agrega al saldo de los ganadores una cantidad proporcional a su apuesta en relacion al 
	 *   monto total apostado.
	 * 
	 * Nota: Se hace uso del resultado que tuvo la pela para definir a los ganadores y tambien del casteo 
	 * explicito.
	 */
    public void resolverApuesta() {
    	if (apostadores.isEmpty()) {return;}
    	
    	double montoTotal = 0;
    	double totalGanadores = 0;
    	
    	for (Object[] objects : apostadores) {
    		Prisionero prisionero = (Prisionero) objects[1];
    		double apuesta = (Integer) objects[2];
    		montoTotal += apuesta;
    		if (pelea.getGanador().equals(prisionero)) {totalGanadores += apuesta;}
		}
    	
    	ArrayList<String> estadisticas = new ArrayList<>();
    	
    	for (Object[] objects : apostadores) {

    		Apostador apostador = (Apostador) objects[0];
    		Prisionero prisionero = (Prisionero) objects[1];
    		double apuesta = (Integer) objects[2];
    		
    		if (pelea.getGanador().equals(prisionero)) {
    			double k = apuesta/totalGanadores;
    			double ganancia = k*montoTotal;
    			apostador.aumentarSaldo(ganancia);
    			estadisticas.add("ID Apostador: " + apostador.getIdentificacion() + "\t\t+" + (ganancia - apuesta) + "\t\t" + "Saldo actual: " + apostador.getSaldo());
    			
    		} else {
    			estadisticas.add("ID Apostador: " + apostador.getIdentificacion() + "\t\t-" + (apuesta) + "\t\t" + "Saldo actual: " + apostador.getSaldo());
    		}
		}
    	
    	this.montoTotal = montoTotal;
    	this.montoTotalGanadores = totalGanadores;
    	this.estadisticas = estadisticas;
    }
    
    /*
     * Tiene como finalidad agregar un Apostador a la lista de apostadores de una Apuesta en particular.
     * Recibe tres parametros:
     * - apostador: Apostador que ingresa a this Apuesta.
     * - prisionero: luchador por el que apostador va a apostar
     * - apuesta: cantidad a apostar
     * 
     * El metodo comprueba si el apostador que desea ingresar a la apuesta tiene suficiente saldo.
     * Si no posee suficiente saldo, no es agregado a la lista de apostadores.
     */
    public void agregarApostador(Apostador apostador, Prisionero prisionero, Integer apuesta) {
    	if (apuesta > apostador.getSaldo()) {return;}
    	
    	apostador.reducirSaldo(apuesta);
    	Object[] agregando = {apostador, prisionero, apuesta};
    	apostadores.add(agregando);
    }

	public int getCodigo() {return codigo;}
	public void setCodigo(int codigo) {	this.codigo = codigo;}

	public Pelea getPelea() {return pelea;}
	public void setPelea(Pelea pelea) {	this.pelea = pelea;}

	public ArrayList<Object[]> getApostadores() {return apostadores;}
    
	public static Hashtable<Integer, Apuesta> getApuestas() {return apuestas;}
	public static void setApuestas(Hashtable<Integer, Apuesta> apuestas) {Apuesta.apuestas = apuestas;}
    
}
