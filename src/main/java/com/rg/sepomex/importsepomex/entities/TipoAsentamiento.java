package com.rg.sepomex.importsepomex.entities;

import javax.persistence.*;

@Entity
public class TipoAsentamiento {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_tipo_asentamiento")
	private Long id;

	@Column(name = "nombre_tipo_asentamiento", length = 200)
	private String nombre;

	@Column(name = "cve_tipo_asentamiento")
	private String claveAsentamiento;

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

	public String getClaveAsentamiento() {
		return claveAsentamiento;
	}

	public void setClaveAsentamiento(String claveAsentamiento) {
		this.claveAsentamiento = claveAsentamiento;
	}

}
