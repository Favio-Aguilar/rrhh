package com.rrhh.Controller;

import com.rrhh.Entity.Trabajador;
import com.rrhh.Entity.Vacaciones;
import com.rrhh.Repository.TrabajadorRepository;
import com.rrhh.Repository.VacacionesRepository;
import com.rrhh.dto.*;
import com.rrhh.service.ITrabajadorService;
import com.rrhh.service.IVacacionesService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class VacacionesController {


    @Autowired
    VacacionesRepository vacacionesRepository;
    @Autowired
    ITrabajadorService trabajadorService;
    @Autowired
    TrabajadorRepository trabajadorRepository;

    @Autowired
    IVacacionesService vacacionesService;

    @GetMapping("/dashboard/vacaciones")
    public String listarVacaciones(Model model,@RequestParam("page") Optional<Integer> page,
                                   @RequestParam("size") Optional<Integer> size,@RequestParam(required = false) String nombreTrabajador) {

        int currentPage = page.orElse(1); // Use 1 as default if no page is specified
        int pageSize = size.orElse(5);

        if (currentPage < 1) {
            // Handle invalid page number (e.g., redirect to the first page)
            return "redirect:/vistas/vacaciones/ListadoDetalle";
        }

        // Get list of workers with pagination
        Page<TrabajadorDiasTotalesDTO> bookPage;
        try {
            bookPage = vacacionesService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
            model.addAttribute("vacaciones", bookPage.getContent());
        } catch (IllegalArgumentException e) {
            // Handle potential exception from service (e.g., log error, redirect)
            model.addAttribute("error", "Error: Invalid page number.");
            bookPage = Page.empty();
        }


        model.addAttribute("bookPage", bookPage);

        // Calculate and add page numbers (ensure all pages are positive)
        int totalPages = Math.max(bookPage.getTotalPages(), 1); // Avoid negative totalPages
        List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
        model.addAttribute("pageNumbers", pageNumbers);

        System.out.println("NOMBRE : " + nombreTrabajador);
        List<TrabajadorDiasTotalesDTO> dtos;
        if (nombreTrabajador != null && !nombreTrabajador.isEmpty()) {
            List<Object[]> datos;
            System.out.println("NOMBRE BUSCA : " + nombreTrabajador);
            datos = vacacionesRepository.findVacacionesPorTrabajadorNombreYPeriodo(nombreTrabajador);
            System.out.println("DATOS BUSADOS: " + datos.isEmpty());


            dtos = datos.stream()
                    .map(row -> new TrabajadorDiasTotalesDTO(
                            (Integer) row[0],
                            (String) row[1],
                            (String) row[2],
                            ((BigDecimal) row[3]).intValue(),
                            ((BigDecimal) row[4]).intValue()
                    ))
                    .collect(Collectors.toList());
            model.addAttribute("vacaciones", dtos );

            // Handle case where no vacations are found for the provided nombreTrabajador
            if (dtos.isEmpty() && nombreTrabajador != null && !nombreTrabajador.isEmpty()) {
                System.out.println("NOMBRE VACIO : " + nombreTrabajador);
                model.addAttribute("mensajeExitoso", "No se encontraron vacaciones para el trabajador '" + nombreTrabajador + "'");

                // List<Object[]> resumen = vacacionesRepository.findTrabajadoresDiasTotales();
                List<Object[]> resumen = vacacionesRepository.findVacacionesPorPeriodo();
                // Convertir Object[] a objetos DTO (opcional)
                List<TrabajadorDiasTotalesDTO> dto = resumen.stream()
                        .map(row -> new TrabajadorDiasTotalesDTO(
                                (Integer) row[0],
                                (String) row[1],
                                (String) row[2],
                                ((BigDecimal) row[3]).intValue(),
                                ((BigDecimal) row[4]).intValue()
                        ))
                        .collect(Collectors.toList());
                model.addAttribute("vacaciones", dto );
            }

        } else {
            // List<Object[]> resumen = vacacionesRepository.findTrabajadoresDiasTotales();
            List<Object[]> resumen = vacacionesRepository.findVacacionesPorPeriodo();
            // Convertir Object[] a objetos DTO (opcional)
            List<TrabajadorDiasTotalesDTO> dto = resumen.stream()
                    .map(row -> new TrabajadorDiasTotalesDTO(
                            (Integer) row[0],
                            (String) row[1],
                            (String) row[2],
                            ((BigDecimal) row[3]).intValue(),
                            ((BigDecimal) row[4]).intValue()
                    ))
                    .collect(Collectors.toList());
            model.addAttribute("vacaciones", bookPage.getContent() );
        }


        return "vistas/vacaciones/ListadoDetalle";
    }



    //
    @GetMapping("/dashboard/vacaciones/registrar")
    public String ventaform(Model model, HttpServletRequest request, @RequestParam(required = false) String documentNumber) {

        List<Trabajador> trabajadores = trabajadorService.obtenerTrabajadores(); // List for dropdown (optional)
        model.addAttribute("trabajadores", trabajadores);

        // Búsqueda por nrodocumento si se proporciona
        Trabajador trabajador = null;
        if (documentNumber != null) {
            trabajador = trabajadorService.buscarTrabajadorPorNroDocumento(documentNumber); // Buscar por ID en el servicio
            if (trabajador == null) {
                // No se encontró trabajador con ese documento
                model.addAttribute("warning", "No se encontró trabajador con el nrodocumento: " + documentNumber);
            }
        }

        // Agregar detalle de vacaciones (asumiendo que ya lo tienes implementado en obtenervacaciones)
        model.addAttribute("detalle", obtenervacaciones(request));

        return "vistas/vacaciones/vacacionesform";
    }


    @PostMapping("/dashboard/vacaciones/agregarTrabajador")
    public String agregarTrabajadorVacaciones( @RequestParam(name = "nroducumento", required = false) String nroducumento, RedirectAttributes redirectAttributes, HttpServletRequest request) {

        Trabajador trabajador = null;
        if (nroducumento != null) {
            trabajador = trabajadorService.buscarTrabajadorPorNroDocumento(nroducumento); // Buscar por ID en el servicio

        }

        if (trabajador == null) {

            redirectAttributes.addFlashAttribute("warning", "Por favor, Ingrese su DNI");
            return "redirect:/dashboard/vacaciones/registrar";
        }

        VacacionesDTO obtenerVacaciones = obtenervacaciones(request);
        VacacionesDTO vacaciones = vacacionesService.agregarTrabajadorVacaciones(obtenerVacaciones, trabajador);

        System.out.println("DETALLE TRABAJADOR : " + trabajador.getId() + "// " + trabajador.getNombre());
        System.out.println("// " + vacaciones.getNombreTrabajadores());
        guardarVacacionSession(vacaciones, request);
        return "redirect:/dashboard/vacaciones/registrar";
    }

    @PostMapping(value = "/dashboard/vacaciones/registrarFechaInicio")
    public String registrarfechaInicioFin(@RequestParam(name = "fechaInicio", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaInicio, @RequestParam(name = "fechaFin", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaFin, RedirectAttributes redirectAttributes, HttpServletRequest request) {

        // Validate that both dates are provided
        if (fechaInicio == null) {
            redirectAttributes.addFlashAttribute("warning", "La fecha de inicio es obligatoria");
            return "redirect:/dashboard/vacaciones/registrar";
        }
        if (fechaFin == null) {
            redirectAttributes.addFlashAttribute("warning", "La fecha de fin es obligatoria");
            return "redirect:/dashboard/vacaciones/registrar";
        }
        // Additional validation: Check if end date is before start date
        if (fechaFin.compareTo(fechaInicio) < 0) {
            redirectAttributes.addFlashAttribute("danger", "La fecha de fin no puede ser anterior a la fecha de inicio");
            return "redirect:/dashboard/vacaciones/registrar";
        }
        if (fechaFin.compareTo(fechaInicio) < 0 || fechaFin.compareTo(fechaInicio) == 0) {
            redirectAttributes.addFlashAttribute("danger", "La fecha de fin no puede ser anterior ni igual a la fecha de inicio");
            return "redirect:/dashboard/vacaciones/registrar";
        }


        // Retrieve existing vacation data (potentially with previously set start date)
        VacacionesDTO obtenervacaciones = obtenervacaciones(request);

        // Update retrieved data with new start and end dates
        VacacionesDTO vacaciones = vacacionesService.registrarfechaInicioFin(obtenervacaciones, fechaInicio, fechaFin);

        //ANDRES LE QUEDAN 11 DIAS DE VACACIONES DEL AÑO 2020-2021, POR LO TANTO EN ESE RANGO DE FECHA TIENE QUE COMPLETAR SUS 30 DIAS DE VACACIONES
        //|||||||||||||||||||||||||||||||||||||||||||||
        //INGRESA LOS DATOS : 2020-07-01 / 2020-07-12 | diasGozados: 07 y sin gozar:23

        if ( (vacaciones.getDiasTotales() == null) && (vacaciones.getDiasAcumulados() == null )) {
            //CALCULAD
                //diasGozados = 19
                int diasGozados = calculateVacationDays(fechaInicio, fechaFin);
                vacaciones.setDiasTotales(diasGozados);//
                System.out.println("DIAS TOTALES :  " + diasGozados);
                ////////////////////////////////////////////
            //buscar por el id del trabajador que es Andres con codigo 2 y el año  :
                int diasAcumulados = calculateAcumulados(diasGozados);//11
                //vacaciones.setDiasAcumulados(diasAcumulados);
                //estado -> 0 = no gozado || 1 = = gozado
            if (diasGozados > 30){
                redirectAttributes.addFlashAttribute("danger", "Ha excedido el límite anual de vacaciones (30 días)");
                return "redirect:/dashboard/vacaciones/registrar";
            } else {

                    }if (diasGozados > diasAcumulados || diasAcumulados > diasGozados){
                                vacaciones.setEstado(0);//va deber vacaciones
                    } else {
                                vacaciones.setEstado(1);
                    }

        }

        // Save vacation data in session (optional based on your application logic)
        guardarVacacionSession(vacaciones, request);

        return "redirect:/dashboard/vacaciones/registrar";
    }
    /*||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||*/
    private int calculateVacationDays(Date fechaInicio, Date fechaFin) {
        // Implement your logic to calculate the difference in days between fechaInicio and fechaFin
        // This example uses a simple difference calculation (modify as needed)
        long milliseconds = fechaFin.getTime() - fechaInicio.getTime();
        int days = (int) (milliseconds / (1000 * 60 * 60 * 24));
        return days;
    }
    private int calculateAcumulados(int diasTotales){
        int dia_acumulado = 30 - diasTotales;

        return dia_acumulado;
    }

    /*||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||*/
    /*REGISTRO */
    @PostMapping("/dahsboard/vacaciones/guardarVacaciones")
    public String guardarVacaciones(HttpServletRequest request, RedirectAttributes redirectAttrs) {
        VacacionesDTO vacaciones = obtenervacaciones(request);

        if (vacaciones.getIdTrabajador() <= 0) {
            redirectAttrs.addFlashAttribute("warning", "Complete todos los datos , por favor ");
            return "redirect:/dashboard/vacaciones/registrar";
        }

        Date fechaInicio = vacaciones.getFechaInicio();
        if (fechaInicio == null) {
            // Handle the case where FechaInicio is null
            redirectAttrs.addFlashAttribute("danger", "Error: La fecha de inicio de vacaciones es obligatoria");
            return "redirect:/dashboard/vacaciones/registrar";
        }

        int anio = getAnioDeFechaInicio(fechaInicio);
        int mes = getMesDeFechaInicio(fechaInicio);

        // Buscar por id y año para saber los datos de acuerdo al mes y año
        Vacaciones existingVacation = vacacionesRepository.findVacacionesPorTrabajadorMesYAnioNative(vacaciones.getIdTrabajador(), anio, mes);
        int consultDiasTotales = 0;
        if (existingVacation != null) {
            consultDiasTotales = existingVacation.getDiasTotales();
        }

        // Consulta por año agrupado para saber si completo sus vacaciones anuales ya que su maximo de dias es 30
        VacacionesIdAnio result = vacacionesRepository.findVacacionesPorTrabajadorYAnioAgrupadoPrimero(vacaciones.getIdTrabajador(), anio);
        int diastotalesAgrupado = 0;
        if (result != null) {
            diastotalesAgrupado = result.getdiastotales();
        }

        int ingresoDiasTotales = vacaciones.getDiasTotales();
        int totalDias = ingresoDiasTotales + consultDiasTotales;
        //int totalDias =

        // Si el estado == 0 (no gozados) y ya se completan las vacaciones del año
        if (diastotalesAgrupado == 30 && existingVacation != null && existingVacation.getEstado() == 0) {
            redirectAttrs.addFlashAttribute("danger", "YA TIENE SUS VACACIONES GOZADAS EN EL AÑO " + result.getAnio());
            return "redirect:/dashboard/vacaciones/registrar";
        }


        // Si el total de días de vacaciones supera el máximo anual (30 días)
        Optional<VacacionesIdAnio> optionalVacaciones1 = Optional.ofNullable(result);
        //int consultdiastotales = list.getDiasTotales();
        int diasTotales = optionalVacaciones1.map(VacacionesIdAnio::getdiastotales).orElse(0);
        System.out.println("DIAS TOTALES QUE LE QUEDAN : " + diasTotales);
        if (diasTotales == 30) {
            redirectAttrs.addFlashAttribute("danger", "No puede exceder los 30 días de vacaciones anuales");
            return "redirect:/dashboard/vacaciones/registrar";
        }

        // Si se completan las vacaciones del año (30 días)
        if (totalDias == 30) {
            vacaciones.setDiasAcumulados(0);
            if (existingVacation != null) {
                existingVacation.setEstado(1);
                existingVacation.setDiasAcumulados(0);
            }
            vacacionesService.guardarVacaciones(vacaciones);
            if (existingVacation != null) {
                vacacionesService.guardarVacaciones(vacaciones);
            }
            eliminarVentaSession(request);
            redirectAttrs.addFlashAttribute("success", "Vacaciones registrado correctamente 2");
            return "redirect:/dashboard/vacaciones";
        }

        // Si no se completan las vacaciones del año, se guarda la nueva solicitud

        Optional<VacacionesIdAnio> optionalVacaciones = Optional.ofNullable(result);
        //int consultdiastotales = list.getDiasTotales();
        int diasacumulados = optionalVacaciones.map(VacacionesIdAnio::getdiasacumulados).orElse(0);
        System.out.println("DIAS ACUMULADOS QUE LE QUEDAN : " + diasacumulados);
       if(diasacumulados == 0){
           int diasacumulado = calculateAcumulados(ingresoDiasTotales);
           vacaciones.setDiasAcumulados(diasacumulado);
           vacacionesService.guardarVacaciones(vacaciones);
           eliminarVentaSession(request);
           redirectAttrs.addFlashAttribute("success", "Vacaciones registrado correctamente 1");
           return "redirect:/dashboard/vacaciones";
       } else if (ingresoDiasTotales > diasacumulados){
            System.out.println("DIAS TOTALES INGRESADO : " + diasTotales);
            redirectAttrs.addFlashAttribute("danger", "Los dias Solcitados que son : " + ingresoDiasTotales + ",  excede a los dias que le quedan " + diasacumulados);
            return "redirect:/dashboard/vacaciones/registrar";
       }
        int diasAcumuladosDisponibles = 30 - consultDiasTotales;
        if (ingresoDiasTotales > diasAcumuladosDisponibles) {
            redirectAttrs.addFlashAttribute("warning", "Le faltan " + (ingresoDiasTotales - diasAcumuladosDisponibles) + " dias para completar sus vacaciones");
            eliminarVentaSession(request);
            return "redirect:/dashboard/vacaciones/registrar";
        }


        /**/
        int diassolicitados = vacaciones.getDiasTotales(); System.out.println("DIAS QUE SOLICITA EL TRABAJADOR : " + diassolicitados);
        if (diasacumulados == 0){
            vacaciones.setDiasAcumulados(30 -vacaciones.getDiasTotales() );
            vacacionesService.guardarVacaciones(vacaciones);
            eliminarVentaSession(request);
            redirectAttrs.addFlashAttribute("success", "Vacaciones registrado correctamente 1");
            return "redirect:/dashboard/vacaciones";
        }
        if(diassolicitados > diasacumulados){

            redirectAttrs.addFlashAttribute("warning", "LOS DIAS SOLICITADOS SOBREPASAN A SU DIAS QUE LE QUEDAN => " + diasacumulados);
            return "redirect:/dashboard/vacaciones/registrar";
        }

        //esto me devuelve el ultimo resultado
        Vacaciones Unresult = vacacionesRepository.findVacacionesPorTrabajadorYAnio(vacaciones.getIdTrabajador(), anio);
        int diastotales1result = 0;
        if (result != null) {
             Unresult.setDiasAcumulados(0);
             int unresu = result.getdiastotales(); System.out.println("RESULTADOS DE DIAS TOTALES SUMA POR AÑO : " + unresu);
            int sumaactual= unresu + vacaciones.getDiasTotales();

             vacaciones.setDiasAcumulados(30 - sumaactual);
            vacacionesService.guardarVacaciones(vacaciones);
            eliminarVentaSession(request);
            redirectAttrs.addFlashAttribute("success", "Vacaciones registrado correctamente 2");
            return "redirect:/dashboard/vacaciones";
        }

        //vacaciones.setDiasAcumulados(30 - ingresoDiasTotales);
        vacacionesService.guardarVacaciones(vacaciones);
        eliminarVentaSession(request);
        redirectAttrs.addFlashAttribute("success", "Vacaciones registrado correctamente 1");
        return "redirect:/dashboard/vacaciones";
    }


    public int sumadias (int num1, int num2){

        return num1 + num2;
    }
    public int getMesDeFechaInicio(Date fechaInicio) {
        // Create a Calendar instance
        Calendar calendar = Calendar.getInstance();

        // Set the calendar with the fechaInicio date
        calendar.setTime(fechaInicio);

        // Get the year from the calendar
        int year = calendar.get(Calendar.MONTH);

        // Return the year
        return year;
    }
    public int getAnioDeFechaInicio(Date fechaInicio) {
        // Create a Calendar instance
        Calendar calendar = Calendar.getInstance();

        // Set the calendar with the fechaInicio date
        calendar.setTime(fechaInicio);

        // Get the year from the calendar
        int year = calendar.get(Calendar.YEAR);

        // Return the year
        return year;
    }
    //



    //METODOS
    private VacacionesDTO obtenervacaciones(HttpServletRequest request) {
        VacacionesDTO vacaciones =(VacacionesDTO) request.getSession().getAttribute("vacaciones");
        if(vacaciones == null){
            vacaciones =new VacacionesDTO();
        }
        return vacaciones;
    }

    private void guardarVacacionSession(VacacionesDTO vacaciones, HttpServletRequest request) {
        request.getSession().setAttribute("vacaciones", vacaciones);
    }
    private void eliminarVentaSession(HttpServletRequest request) {
        request.getSession().removeAttribute("vacaciones");
    }

    ///////////////////////////////
    public int calcularDiasHabiles(Date fechaInicio, Date fechaFin) {

        // Convertir a Calendar para facilitar la manipulación
        Calendar calendarInicio = Calendar.getInstance();
        calendarInicio.setTime(fechaInicio);
        Calendar calendarFin = Calendar.getInstance();
        calendarFin.setTime(fechaFin);

        // Convertir a formato "yyyy-MM-dd"
        String fechaInicioStr = new SimpleDateFormat("yyyy-MM-dd").format(fechaInicio);
        String fechaFinStr = new SimpleDateFormat("yyyy-MM-dd").format(fechaFin);

        // Obtener la cantidad de días entre las dos fechas
        int diasTotales = calendarFin.get(Calendar.DAY_OF_YEAR) - calendarInicio.get(Calendar.DAY_OF_YEAR);

        // Ajustar por días no laborables (sábados, domingos y feriados)
        int diasNoLaborables = 0;
        while (calendarInicio.before(calendarFin)) {
            if (esFinDeSemana(calendarInicio) || esFeriado(calendarInicio)) {
                diasNoLaborables++;
            }
            calendarInicio.add(Calendar.DAY_OF_YEAR, 1);
        }

        // Retornar la cantidad de días hábiles
        return diasTotales - diasNoLaborables;
    }
    private boolean esFinDeSemana(Calendar calendar) {
        int diaSemana = calendar.get(Calendar.DAY_OF_WEEK);
        return diaSemana == Calendar.SATURDAY || diaSemana == Calendar.SUNDAY;
    }
    private boolean esFeriado(Calendar calendar) {
        // Lista de feriados fijos (ejemplo)
        List<String> feriados = Arrays.asList(
                "2024-01-01", // Año Nuevo
                "2024-04-02", // Viernes Santo
                "2024-04-09", // Semana Santa
                "2024-05-01", // Día del Trabajo
                "2024-07-28", // Fiestas Patrias
                "2024-12-25" // Navidad
        );

        // Convertir fecha a formato "yyyy-MM-dd"
        String fechaStr = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());

        // Verificar si la fecha está en la lista de feriados
        return feriados.contains(fechaStr);
    }

    ///////////////////////////
    //reportes

    //
    @GetMapping(value = "/dashboard/vacaciones/{id}/reporte")
    public ResponseEntity<byte[]> reportSuppliers(@PathVariable Integer id, HttpServletResponse response) throws FileNotFoundException, JRException, ParseException {


        try {
            List<VacacionesResponseTotalesDTO> vacaciones = vacacionesService.obtenerVacacionesTotalesPorCod(id);

            if (vacaciones.isEmpty()) {
                return ResponseEntity.notFound().build(); // Or return a custom message indicating no data found
            }

            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(vacaciones);
            JasperReport compilerReport = JasperCompileManager.compileReport(new FileInputStream("src/main/resources/reportes/Report2.jrxml"));

            Map<String, Object> map = new HashMap<>();
            // map.put("codigoTrabajador", vacaciones.getIdTrabajador()); // Add parameters if needed

            JasperPrint report = JasperFillManager.fillReport(compilerReport, map, beanCollectionDataSource);
            byte[] data = JasperExportManager.exportReportToPdf(report);

            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=Reporte.pdf");

            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(data);
        } catch (ResourceNotFoundException e) {
            // Handle ResourceNotFoundException (e.g., return 404 Not Found)
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // Handle other exceptions (log the error, return a generic error message)
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    ///////
    //
    @GetMapping(value = "/dashboard/vacaciones/reporte")
    public ResponseEntity<byte[]> reportVacaciones( HttpServletResponse response) throws FileNotFoundException, JRException, ParseException {





        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(vacacionesService.obtenerTodasLasVacaciones());
        JasperReport compilerReport = JasperCompileManager.compileReport(new FileInputStream("src/main/resources/reportes/Vacaciones.jrxml"));

        Map<String, Object> map = new HashMap<>();


        JasperPrint report = JasperFillManager.fillReport(compilerReport, map, beanCollectionDataSource);

        byte[] data = JasperExportManager.exportReportToPdf(report);

        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.set(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "inline; filename=REPORTE TOTAL.pdf");

        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(data);
    }
}