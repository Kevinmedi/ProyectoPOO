package com.tuempresa.sistema_de_Control_de_Asistencia_para_Empleados.acciones;

import com.tuempresa.sistema_de_Control_de_Asistencia_para_Empleados.modelo.Empleado;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.openxava.actions.JasperReportBaseAction;
import org.openxava.model.MapFacade;
import org.openxava.tab.Tab;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ImprimirEmpleadosAction extends JasperReportBaseAction {

    @Inject
    private Tab tab;

    @Override
    protected JRDataSource getDataSource() throws Exception {

        List<Empleado> empleados = new ArrayList<>();

        // Si hay filas seleccionadas en la lista
        if (tab.getSelectedKeys().length > 0) {
            for (Map key : tab.getSelectedKeys()) {
                empleados.add((Empleado) MapFacade.findEntity("Empleado", key));
            }
        }
        // Si no hay selección, usamos todos los registros de la lista
        else {
            for (int i = 0; i < tab.getTableModel().getRowCount(); i++) {
                @SuppressWarnings("unchecked")
                Map key = (Map) tab.getTableModel().getObjectAt(i);
                empleados.add((Empleado) MapFacade.findEntity("Empleado", key));
            }
        }

        return new JRBeanCollectionDataSource(empleados);
    }

    @Override
    protected String getJRXML() {
        // OJO: nombre EXACTO de tu archivo .jrxml
        return "reports/Reporte.jrxml";
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected Map getParameters() {
        return null;
    }
}
