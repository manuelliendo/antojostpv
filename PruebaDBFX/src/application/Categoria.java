package application;

import javafx.scene.image.Image;

public class Categoria {

	private String Nombre;
	private Integer id;
	private Image imagen;
	private Integer count;
	public Categoria(String nombre, Integer id, Image imagen,Integer count) {
		super();
		Nombre = nombre;
		this.id = id;
		this.imagen = imagen;
		this.count = count;
	}
	public Categoria(String nombre, Integer id, Integer count) {
		super();
		Nombre = nombre;
		this.id = id;
		this.imagen = new Image("img/logoAntojos5.jpg", 100, 100, true, true);
		this.count = count;
	}
	
	public String getNombre() {
		return Nombre;
	}
	public void setNombre(String nombre) {
		Nombre = nombre;
	}
	public Integer getId() {
		return id;
	}
	
	public Image getImagen() {
		return imagen;
	}
	public void setImagen(Image imagen) {
		this.imagen = imagen;
	}
	
	public void setCount(Integer count) {
		this.count = count;
	}
	public Integer getCount() {
		return count;
	}
	
	
	
}
