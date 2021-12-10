package gestorAplicacion.apuestas;

import java.io.Serializable;

public abstract class Apostador implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected int identificacion;
    protected String nombre;
    protected int saldo;
    
    protected Apostador(int identificacion, String nombre, int saldo) {
		this.identificacion = identificacion;
		this.nombre = nombre;
		this.saldo = saldo;
	}

	public void aumentarSaldo(double dinero) {
    	this.saldo += dinero;
    }
    
    public void reducirSaldo(double dinero) {
    	this.saldo -= dinero;
    	
    }
    
    public abstract String toString();

	public int getIdentificacion() {return identificacion;	}
	public void setIdentificacion(int identificacion) {	this.identificacion = identificacion;}

	public String getNombre() {	return nombre;}
	public void setNombre(String nombre) {this.nombre = nombre;}

	public int getSaldo() {	return saldo;}
	public void setSaldo(int saldo) {this.saldo = saldo;}    
    
}
