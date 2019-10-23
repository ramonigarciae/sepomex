package com.rg.sepomex.importsepomex.entities;

import javax.persistence.*;

@Entity
public class Ciudad {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_ciudad")
	private Long id;

	@Column(name = "nombre_ciudad", length = 200)
	private String nombre;

	@ManyToOne
	@JoinColumn(name = "id_municipio")
	private Municipio municipio;

	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
