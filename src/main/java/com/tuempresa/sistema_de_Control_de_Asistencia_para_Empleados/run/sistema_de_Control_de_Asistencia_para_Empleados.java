package com.tuempresa.sistema_de_Control_de_Asistencia_para_Empleados.run;

import org.openxava.util.*;

/**
 * Ejecuta esta clase para arrancar la aplicación.
 *
 * Con OpenXava Studio/Eclipse: Botón derecho del ratón > Run As > Java Application
 */

public class sistema_de_Control_de_Asistencia_para_Empleados {

	public static void main(String[] args) throws Exception {
		//DBServer.start("sistema_de_Control_de_Asistencia_para_Empleados-db"); // Para usar tu propia base de datos comenta esta línea y configura src/main/webapp/META-INF/context.xml
		AppServer.run("CreativaAsistencia"); // Usa AppServer.run("") para funcionar en el contexto raíz
	}

}
