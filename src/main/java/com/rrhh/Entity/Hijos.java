package com.rrhh.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Hijos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    //@NotBlank(message = "El Nombre es obligatorio")
    public String nombre_hijo;

    //@NotBlank(message = "El Apellido es obligatorio")
    public String apellido_hijo;


    public int edad;

//    @Pattern(regexp = "^9[0-9]{8}$", message = "El celular debe contener 9 dígitos y empezar con 9")
    public String nro_documento;

    @ManyToOne(targetEntity = Trabajador.class)
    @JoinColumn(name = "id_trabajador")
    private Trabajador trabajador;


}
