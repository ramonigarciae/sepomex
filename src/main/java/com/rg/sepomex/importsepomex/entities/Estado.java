package com.rg.sepomex.importsepomex.entities;

import javax.persistence.*;

@Entity

public class Estado {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_estado")
	private Integer id;

	@Column(name = "nombre_estado", length = 200)
	private String nombre;

	@Column(name = "nombre_estado_oficial", length = 200)
	private String nombreOficial;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombreOficial() {
		return nombreOficial;
	}

	public void setNombreOficial(String nombreOficial) {
		this.nombreOficial = nombreOficial;
	}

}
