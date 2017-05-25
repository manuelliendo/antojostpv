package application;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Producto {

//	private Integer id;
	private String nombre;
	private ImageView imagen;
	private Float precio;
	private Integer cantidad;
	
	
	public Producto(String nombre, ImageView imagen, Float precio) {
		super();
//		this.id = id;
		this.nombre = nombre;
		this.imagen = imagen;
		this.precio = precio;
		this.cantidad = 0;
	}
	public Producto(String nombre, Float precio) {
		super();
//		this.id = id;
		this.nombre = nombre;
		this.imagen = new ImageView(new Image(
				"img/logoAntojos5.jpg", 100, 100, true, true));
		this.precio = precio;
		this.cantidad = 0;
	}
	
//	public Integer getId() {
//		return id;
//	}
//	public void setId(Integer id) {
//		this.id = id;
//	}
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
	}
	public Float getPrecioTotal(){
		return (precio*cantidad);
	}
	
}

