package application;

import java.io.Serializable;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ProductoSimple implements Serializable{
	private String nombre = "";
	private Float precio = null;
	private Integer cantidad=1;
	private Float precioTotal = null;
	private String especial = "";
	
	public ProductoSimple(String nombre, Float precio) {
		super();
//		this.id = id;
		this.nombre = nombre;
		this.precio = precio;
		
	}
	
//	public Integer getId() {
//		return id;
//	}
//	public void setId(Integer id) {
//		this.id = id;
//	}
	public void setEspecial(String especial){
		this.especial = especial;
	}
	public String getEspecial(){
		return especial;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Float getPrecio() {
		return precio;
	}
	public Integer getCantidad() {
		return cantidad;
	}
	
	public void addCantidad(){
		cantidad++;
		precioTotal = precio*cantidad;
	}
	public Float getPrecioTotal(){
		return (precio*cantidad);
	}
	public void resetCantidad(){
		cantidad = 1;
	}
}
