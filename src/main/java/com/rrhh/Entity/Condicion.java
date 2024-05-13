package com.rrhh.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Condicion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   // @Column(name = "id_condicion", unique = true, nullable = false)

    public int id;

    //@Column
    @Column(unique = true)
    public String nombre;



}
