package com.rrhh.Entity;

import jakarta.persistence.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "trabajador")
public class Trabajador {
    @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id_trabajador", unique = true, nullable = false)
    public int id;

    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "^[0-9]{8}$", message = "El DNI debe contener 8 dígitos")
    @Column (name = "nrodocumento")
    public String nrodocumento;

    @NotBlank(message = "El Codigo de Planilla es obligatorio")
    @Column (name = "codplanilla")
    public String codplanilla;

    @NotBlank(message = "El nombre del Trabajador es obligatorio")
    @Column (name = "nombre")
    public String nombre;

    @NotBlank(message = "El apellido del Trabajador es obligatorio")
    @Column (name = "apellido")
    public String apellido;

    @Min(value = 18, message = "La edad del Trabajador debe ser mayor o igual a 18")
    @Column (name = "edad")
    public int edad;

    //@NotBlank(message = "El celular del Trabajador es obligatorio")
    @Pattern(regexp = "^9[0-9]{8}$", message = "El celular debe contener 9 dígitos y empezar con 9")
    @Column (name = "celular")
    public  String celular;

    @Email
    @Pattern(regexp = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "El formato del correo electrónico es inválido")
    @Column (name = "correo")
    public String correo;


    @NotBlank(message = "La Direccion del Trabajador es obligatorio")
    @Column (name = "direccion")
    public String direccion;

    @NotBlank(message = "El Genero del Trabajador es obligatorio")
    @Column (name = "sexo")
    public String sexo;



    @Column(name = "fecha_trabajador")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date fechaTrabajador;

    public int estado;

    @Column (name = "nrohijos")
    public int nrohijos;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date fechanacimiento;

    @ManyToOne(targetEntity = Condicion.class)
    @JoinColumn(name = "id_condicion")
    public Condicion condicion;

    @ManyToOne(targetEntity = GrupoOcupacional.class)
    @JoinColumn(name = "id_grupoOcupacional")
    public GrupoOcupacional grupoOcupacional;







}

