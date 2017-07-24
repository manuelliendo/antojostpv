package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class Estadisticas {

	// TODO: obtener el producto mas vendido del dia
	// TODO: obtener el producto menos vendido del dia

	DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	String savedDate = "";
	static double totalIngresosEfectivo = 0;
	static double totalIngresosTarjeta = 0;
	static double totalSalidas = 0;
	static double totalNeto = 0;
	static double totalClientes = 0;
	List<String> listaStats = FXCollections.observableArrayList();
	static List<String> listaProducto = FXCollections.observableArrayList();
	static List<Integer> listaCantidadProducto = FXCollections
			.observableArrayList();
	Connection conn = null;
	ResultSet rs = null;
	PreparedStatement pst = null;

	public void GuardarEstadisticas(int opcion, String totalOrden,
			ObservableList<ProductoSimple> orden) throws IOException, SQLException {
		File f = new File("Stats.ser");
		if (f.exists() && !f.isDirectory()) {
			Read();
			ResetDia();
		}
		double itotalOrden = Double.valueOf(totalOrden);
		switch (opcion) {
		case 0:
			EntradaCajaEfectivo(itotalOrden);
			break;
		case 1:
			EntradaCajaTarjeta(itotalOrden);
			break;
		default:
			break;
		}

		EntradaClientes();
		RegistroProducto(orden);

		Write();
		
	}
	
	public boolean Dianuevo() {

			LocalDateTime now = LocalDateTime.now();
//			savedDate = "";
		boolean aux = false;
		 System.out.println("Guardado: " + savedDate);
		 System.out.println("Actual: " + dateFormat.format(now));


		if (!(savedDate.equals(dateFormat.format(now)))) {
			aux = true;
			System.out.println("fecha cambio");
			savedDate = dateFormat.format(now);
			System.out.println(savedDate);
		}

		return aux;
	}

	public void ResetDia() throws IOException, SQLException {
		if (Dianuevo()) {
			System.out.println("resetear dia");
			WriteDataBase();
			WriteDataBaseProductos();
			totalNeto = 0;
			totalIngresosEfectivo = 0;
			totalIngresosTarjeta = 0;
			totalSalidas = 0;
			totalClientes = 0;
		}
	}

	

	public static double getTotalNeto() {
		totalNeto = (totalIngresosEfectivo + totalIngresosTarjeta)
				- totalSalidas;
		return totalNeto;
	}

	public static void EntradaCajaEfectivo(double totalOrden) {
		totalIngresosEfectivo += totalOrden;
	}

	public static void EntradaCajaTarjeta(double totalOrden) {
		totalIngresosTarjeta += totalOrden;
	}

	public static void SalidaCaja(double salidaCaja) {
		totalSalidas += salidaCaja;
	}

	public static void EntradaClientes() {
		totalClientes++;

	}

	public static void RegistroProducto(ObservableList<ProductoSimple> orden) {
		
		for (int i=0; i<orden.size();i++){
			
//			System.out.println(listaProducto.indexOf(orden.get(i).getNombre()));
			if(listaProducto.indexOf(orden.get(i).getNombre())<0){
				listaProducto.add(orden.get(i).getNombre());
				listaCantidadProducto.add(orden.get(i).getCantidad());
			}else{
				listaCantidadProducto.set(i, (listaCantidadProducto.get(i)+ orden.get(i).getCantidad()));
			}
//			System.out.println("Nombre: " + orden.get(i).getNombre());
//			System.out.println("Cantidad: " + orden.get(i).getCantidad());
			
		}

	}

	public void Read() throws IOException {
		// TODO: leer toda la info del archivo
		FileInputStream fin;
		try {
			fin = new FileInputStream(new File("Stats.ser"));
			ObjectInputStream oi = new ObjectInputStream(fin);
			listaStats.clear();
			ArrayList<String> list = (ArrayList<String>) oi.readObject();
			listaStats = FXCollections.observableList(list);
			list = (ArrayList<String>) oi.readObject();
			listaProducto = FXCollections.observableList(list);
			ArrayList<Integer> listI = (ArrayList<Integer>) oi.readObject();
			listaCantidadProducto = FXCollections.observableList(listI);
			totalNeto = Double.valueOf(listaStats.get(0));
			totalIngresosEfectivo = Double.valueOf(listaStats.get(1));
			totalIngresosTarjeta = Double.valueOf(listaStats.get(2));
			totalSalidas = Double.valueOf(listaStats.get(3));
			totalClientes = Double.valueOf(listaStats.get(4));
			savedDate = listaStats.get(5);

			oi.close();
			fin.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void Write() throws IOException {
		// TODO: guardar toda la informacion en un archivo
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;
		try {
			// write object to file
			listaStats.clear();
			// totalNeto 0;
			// totalIngresosEfectivo 1;
			// totalIngresosTarjeta 2;
			// totalSalidas 3;
			// totalClientes 4;
			// fecha 5
			getTotalNeto();
			listaStats.add(String.valueOf(getTotalNeto()));
			listaStats.add(String.valueOf(totalIngresosEfectivo));
			listaStats.add(String.valueOf(totalIngresosTarjeta));
			listaStats.add(String.valueOf(totalSalidas));
			listaStats.add(String.valueOf(totalClientes));
			listaStats.add(savedDate);
//			for (String s : listaStats) {
//				System.out.println("valor: " + s);
//			}
			fout = new FileOutputStream("Stats.ser");
			oos = new ObjectOutputStream(fout);
			oos.writeObject(new ArrayList<String>(listaStats));
			oos.writeObject(new ArrayList<String>(listaProducto));
			oos.writeObject(new ArrayList<Integer>(listaCantidadProducto));
			fout.close();
			oos.close();
			// System.out.println("Done");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void WriteDataBase() throws IOException, SQLException {
		pst = null;
		rs = null;
		conn = null;
		conn = ConnectionDB.Conectar();
		String query = "INSERT INTO Stats (Ingresos_efectivo,Ingresos_tarjeta,Salidas,Ingresos_neto,Clientes,Date) VALUES(?,?,?,?,?,?)";
		try {

			pst = conn.prepareStatement(query);
			pst.setString(1, listaStats.get(1).toString());
			pst.setString(2, listaStats.get(2).toString());
			pst.setString(3, listaStats.get(3).toString());
			pst.setString(4, listaStats.get(0).toString());
			pst.setString(5, listaStats.get(4).toString());
			pst.setString(6, listaStats.get(5).toString());
			pst.executeUpdate();
			
			System.out.println("info guardada");
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
		pst.close();
		conn.close();
		}

	}
	
	public void WriteDataBaseProductos() throws IOException {
		pst = null;
		rs = null;
		conn = null;
		conn = ConnectionDB.Conectar();
		
		String query = "INSERT INTO Estadisticas (Fecha_compra,Producto_nombre,Cantidad_comprada) VALUES(?,?,?)";
		try {

			for(int i=0;i<listaCantidadProducto.size();i++){
				pst = conn.prepareStatement(query);
				pst.setString(1, savedDate);
				pst.setString(2, listaProducto.get(i));
				pst.setInt(3, listaCantidadProducto.get(i));
				pst.executeUpdate();
			}
			pst.close();
			conn.close();
//			System.out.println("info guardada");
		}

		catch (Exception e) {
			e.printStackTrace();
		}

	}
}
