package application;

public class Almacen {

	private String nombre;
	private int stock;
	private int capacidadMinima;
	private int capacidadDeseada;
	private int capacidadMaxima;
	private double precio;
	
	public Almacen(String nombre, int stock, int capacidadMinima,
			int capacidadDeseada, int capacidadMaxima, double precio) {
		super();
		this.nombre = nombre;
		this.stock = stock;
		this.capacidadMinima = capacidadMinima;
		this.capacidadDeseada = capacidadDeseada;
		this.capacidadMaxima = capacidadMaxima;
		this.precio = precio;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public int getCapacidadMinima() {
		return capacidadMinima;
	}
	public void setCapacidadMinima(int capacidadMinima) {
		this.capacidadMinima = capacidadMinima;
	}
	public int getCapacidadDeseada() {
		return capacidadDeseada;
	}
	public void setCapacidadDeseada(int capacidadDeseada) {
		this.capacidadDeseada = capacidadDeseada;
	}
	public int getCapacidadMaxima() {
		return capacidadMaxima;
	}
	public void setCapacidadMaxima(int capacidadMaxima) {
		this.capacidadMaxima = capacidadMaxima;
	}
	public double getPrecio() {
		return precio;
	}
	public void setPrecio(double precio) {
		this.precio = precio;
	}
	
	
	
}
