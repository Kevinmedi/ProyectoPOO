package com.tuempresa.reporte_y_administrador.run;

import org.openxava.util.*;

/**
 * Ejecuta esta clase para arrancar la aplicación.
 *
 * Con OpenXava Studio/Eclipse: Botón derecho del ratón > Run As > Java Application
 */

public class reporte_y_administrador {

	public static void main(String[] args) throws Exception {
		//DBServer.start("reporte_y_administrador-db"); // Para usar tu propia base de datos comenta esta línea y configura src/main/webapp/META-INF/context.xml
		AppServer.run("reporte_y_administrador"); // Usa AppServer.run("") para funcionar en el contexto raíz
	}

}
