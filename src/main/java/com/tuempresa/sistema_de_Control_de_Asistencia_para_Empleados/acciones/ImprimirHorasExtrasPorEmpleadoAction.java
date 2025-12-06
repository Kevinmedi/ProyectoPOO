package com.tuempresa.sistema_de_Control_de_Asistencia_para_Empleados.acciones;

import com.tuempresa.sistema_de_Control_de_Asistencia_para_Empleados.modelo.Asistencia;
import com.tuempresa.sistema_de_Control_de_Asistencia_para_Empleados.modelo.Empleado;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.openxava.actions.JasperReportBaseAction;
import org.openxava.model.MapFacade;
import org.openxava.tab.Tab;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

public class ImprimirHorasExtrasPorEmpleadoAction extends JasperReportBaseAction {

    @Inject
    private Tab tab;

    private LocalDateTime fechaMin;
    private LocalDateTime fechaMax;

    // DTO para el reporte
    public static class ResumenHorasExtras {
        private String codigo;
        private String nombreCompleto;
        private double totalHorasExtras;
        private int diasConExtras;   // aquí contamos filas con extras > 0

        public ResumenHorasExtras(String codigo, String nombreCompleto) {
            this.codigo = codigo;
            this.nombreCompleto = nombreCompleto;
        }

        public void agregarHoras(double horasExtras) {
            if (horasExtras > 0) {
                this.totalHorasExtras += horasExtras;
                this.diasConExtras++;
            }
        }

        // Getters para Jasper
        public String getCodigo() { return codigo; }
        public String getNombreCompleto() { return nombreCompleto; }
        public double getTotalHorasExtras() { return totalHorasExtras; }
        public int getDiasConExtras() { return diasConExtras; }
    }

    @Override
    protected JRDataSource getDataSource() throws Exception {

        Map[] claves = tab.getSelectedKeys();
        if (claves.length == 0) {
            claves = tab.getAllKeys();
        }

        fechaMin = null;
        fechaMax = null;

        // Agrupamos por id de empleado
        Map<String, ResumenHorasExtras> resumenPorEmpleado = new LinkedHashMap<>();

        for (Map clave : claves) {
            Asistencia a = (Asistencia) MapFacade.findEntity(getModelName(), clave);

            Empleado e = a.getEmpleado();
            if (e == null) continue;

            String idEmpleado = e.getId();
            String codigo = e.getCodigo();
            String nombreCompleto = e.getNombre() + " " + e.getApellido();

            ResumenHorasExtras resumen = resumenPorEmpleado.get(idEmpleado);
            if (resumen == null) {
                resumen = new ResumenHorasExtras(codigo, nombreCompleto);
                resumenPorEmpleado.put(idEmpleado, resumen);
            }

            resumen.agregarHoras(a.getHorasExtras());

            LocalDateTime fh = a.getFechaHora();
            if (fh != null) {
                if (fechaMin == null || fh.isBefore(fechaMin)) fechaMin = fh;
                if (fechaMax == null || fh.isAfter(fechaMax)) fechaMax = fh;
            }
        }

        return new JRBeanCollectionDataSource(new ArrayList<>(resumenPorEmpleado.values()));
    }

    @Override
    protected String getJRXML() {
        return "reports/HorasExtrasPorEmpleado.jrxml";
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
