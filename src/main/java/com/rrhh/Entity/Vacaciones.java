package com.rrhh.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "vacaciones")
public class Vacaciones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vacaciones")
    private int idvacaciones;


    @Column(name = "fecha_inicio")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date fechaInicio;


    @Column(name = "fecha_fin")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date fechaFin;

    @Max(value = 30, message = "Dias Totales no puede exceder los  90 dias")  // Bean Validation annotation
    @Column(name = "dias_totales")
    public Integer diasTotales;

    @Max(value = 30, message = "Dias Acumulados no puede exceder los  90 dias")  // Bean Validation annotation
    @Column(name = "dias_acumulados")
    public Integer diasAcumulados;

    @Column(name = "estado")
    public Integer estado;



    @ManyToOne
    @JoinColumn(name = "id_trabajador")
    private Trabajador trabajador;




    // Add validation to setter method (optional)
    public void setDiasTotales(Integer diasTotales) {
        if (diasTotales != null && diasTotales > 90) {
            throw new IllegalArgumentException("Dias Totales se excedio 90 days");
        }
        this.diasTotales = diasTotales;
    }

    //


}
