package com.rrhh.Entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "grupo_ocupacional")
public class GrupoOcupacional {

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    //@Column
    @Column(unique = true)
    public  String nombre;
}
