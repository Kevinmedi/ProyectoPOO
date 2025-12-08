package com.tuempresa.sistema_de_Control_de_Asistencia_para_Empleados.acciones;

import com.tuempresa.sistema_de_Control_de_Asistencia_para_Empleados.modelo.Asistencia;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.openxava.actions.JasperReportBaseAction;
import org.openxava.jpa.XPersistence;

import java.util.List;
import java.util.Map;

public class ImprimirAsistenciaAction extends JasperReportBaseAction {

    @Override
    protected JRDataSource getDataSource() throws Exception {

        // Trae TODAS las asistencias de la base de datos
        List<Asistencia> asistencias = XPersistence.getManager()
                .createQuery("select a from Asistencia a", Asistencia.class)
                .getResultList();

        return new JRBeanCollectionDataSource(asistencias);
    }

    @Override
    protected String getJRXML() {
        // Ruta relativa dentro de src/main/resources
        return "reports/ReporteAsistencia.jrxml";
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected Map getParameters() throws Exception {
        // No necesitamos parámetros extra
        return null;
    }
}
