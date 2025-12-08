package com.tuempresa.sistema_de_Control_de_Asistencia_para_Empleados.acciones;

import com.tuempresa.sistema_de_Control_de_Asistencia_para_Empleados.modelo.Asistencia;
import com.tuempresa.sistema_de_Control_de_Asistencia_para_Empleados.modelo.ClasificacionAsistencia;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.openxava.actions.JasperReportBaseAction;
import org.openxava.model.MapFacade;
import org.openxava.tab.Tab;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

public class ImprimirRetrasosAction extends JasperReportBaseAction {

    @Inject
    private Tab tab;

    private LocalDateTime fechaMin;
    private LocalDateTime fechaMax;

    @Override
    protected JRDataSource getDataSource() throws Exception {

        List<Asistencia> retrasos = new ArrayList<>();

        Map[] claves = tab.getSelectedKeys();
        if (claves.length == 0) {
            claves = tab.getAllKeys();
        }

        fechaMin = null;
        fechaMax = null;

        for (Map clave : claves) {
            Asistencia a = (Asistencia) MapFacade.findEntity(getModelName(), clave);

            if (a.getClasificacion() == ClasificacionAsistencia.RETRASO) {
                retrasos.add(a);

                LocalDateTime fh = a.getFechaHora();
                if (fh != null) {
                    if (fechaMin == null || fh.isBefore(fechaMin)) fechaMin = fh;
                    if (fechaMax == null || fh.isAfter(fechaMax)) fechaMax = fh;
                }
            }
        }

        return new JRBeanCollectionDataSource(retrasos);
    }

    @Override
    protected String getJRXML() {
        return "reports/RetrasosEmpleados.jrxml";
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected Map getParameters() {
        Map<String, Object> params = new HashMap<>();
        if (fechaMin != null) params.put("fechaDesde", Timestamp.valueOf(fechaMin));
        if (fechaMax != null) params.put("fechaHasta", Timestamp.valueOf(fechaMax));
        return params;
    }
}
