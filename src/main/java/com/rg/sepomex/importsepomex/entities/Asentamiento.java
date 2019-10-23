package com.rg.sepomex.importsepomex.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Asentamiento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_asentamiento")
	private Long id;

	@Column(name = "nombre_asentamiento", length = 200)
	private String nombre;

	@Column(name = "codigo_postal")
	private Integer codigoPostal;

	@ManyToOne
	@JoinColumn(name = "id_municipio")
	private Municipio municipio;

	@ManyToOne
	@JoinColumn(name = "id_ciudad")
	private Ciudad ciudad;

	@ManyToOne
	@JoinColumn(name ="id_tipo_asentamiento")
	private TipoAsentamiento tipoAsentamiento;

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

	public Integer getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(Integer codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}

	public Ciudad getCiudad() {
		return ciudad;
	}

	public void setCiudad(Ciudad ciudad) {
		this.ciudad = ciudad;
	}

	public TipoAsentamiento getTipoAsentamiento() {
		return tipoAsentamiento;
	}

	public void setTipoAsentamiento(TipoAsentamiento tipoAsentamiento) {
		this.tipoAsentamiento = tipoAsentamiento;
	}

}
