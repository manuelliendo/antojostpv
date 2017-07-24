package application;


import java.io.Serializable;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Producto{

//	private Integer id;
	private String nombre = "";
	private ImageView imagen = null;
	private Float precio = null;
	private Integer cantidad=1;
	private Float precioTotal = null;
	private String especial = "";
	
	
	public Producto(String nombre, ImageView imagen, Float precio) {
		
//		this.id = id;
		this.nombre = nombre;
		this.imagen = imagen;
		this.precio = precio;
		
	}
	public Producto(String nombre, Float precio) {
		
//		this.id = id;
		this.nombre = nombre;
		this.imagen = new ImageView(new Image(
				"img/logoAntojos5.png", 100, 100, true, true));
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
	public ImageView getImagen() {
		return imagen;
	}
	public void setImagen(ImageView imagen) {
		this.imagen = imagen;
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

