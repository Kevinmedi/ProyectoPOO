package com.tuempresa.sistema_de_Control_de_Asistencia_para_Empleados.acciones;

import com.tuempresa.sistema_de_Control_de_Asistencia_para_Empleados.modelo.Asistencia;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.openxava.actions.JasperReportBaseAction;
import org.openxava.model.MapFacade;
import org.openxava.tab.Tab;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ImprimirAsistenciaRangoFechasAction extends JasperReportBaseAction {

    @Inject
    private Tab tab;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Solo guarda la lista que se usará como datasource
    private List<Asistencia> asistencias;

    @Override
    protected JRDataSource getDataSource() throws Exception {

        asistencias = new ArrayList<>();

        Map[] claves = tab.getSelectedKeys().length > 0
                ? tab.getSelectedKeys()
                : tab.getAllKeys();

        for (Map key : claves) {
            Asistencia a = (Asistencia) MapFacade.findEntity(getModelName(), key);
            asistencias.add(a);
        }

        return new JRBeanCollectionDataSource(asistencias);
    }

    @Override
    protected String getJRXML() {
        return "reports/AsistenciaRangoFechas.jrxml";
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected Map getParameters() throws Exception {

        // ?? Recalculamos el rango AQUÍ para no depender del orden
        LocalDateTime fechaMin = null;
        LocalDateTime fechaMax = null;


        if (asistencias == null) {
            asistencias = new ArrayList<>();
            Map[] claves = tab.getSelectedKeys().length > 0
                    ? tab.getSelectedKeys()
                    : tab.getAllKeys();

            for (Map key : claves) {
                Asistencia a = (Asistencia) MapFacade.findEntity(getModelName(), key);
                asistencias.add(a);
            }
        }

        for (Asistencia a : asistencias) {
            LocalDateTime fh = a.getFechaHora();
            if (fh != null) {
                if (fechaMin == null || fh.isBefore(fechaMin)) fechaMin = fh;
                if (fechaMax == null || fh.isAfter(fechaMax)) fechaMax = fh;
            }
        }

        String fechaDesdeStr = fechaMin == null ? "" : fechaMin.toLocalDate().format(FORMATTER);
        String fechaHastaStr = fechaMax == null ? "" : fechaMax.toLocalDate().format(FORMATTER);

        Map params = new HashMap();
        params.put("fechaDesde", fechaDesdeStr);
        params.put("fechaHasta", fechaHastaStr);

        return params;
    }
}
