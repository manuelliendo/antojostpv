package application;

public class Usuario {

	private static int permiso = 0;
	private static String nombreUsuario = "";
	
	public static int getPermiso() {
		return permiso;
	}
	public static void setPermiso(int permiso) {
		Usuario.permiso = permiso;
	}
	public static String getNombreUsuario() {
		return nombreUsuario;
	}
	public static void setNombreUsuario(String nombreUsuario) {
		Usuario.nombreUsuario = nombreUsuario;
	}
	
	
}
