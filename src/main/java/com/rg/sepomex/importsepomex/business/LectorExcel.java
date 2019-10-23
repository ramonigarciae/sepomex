package com.rg.sepomex.importsepomex.business;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rg.sepomex.importsepomex.entities.Asentamiento;
import com.rg.sepomex.importsepomex.entities.Ciudad;
import com.rg.sepomex.importsepomex.entities.Estado;
import com.rg.sepomex.importsepomex.entities.Municipio;
import com.rg.sepomex.importsepomex.entities.TipoAsentamiento;
import com.rg.sepomex.importsepomex.repository.AsentamientoRepository;
import com.rg.sepomex.importsepomex.repository.CiudadRepository;
import com.rg.sepomex.importsepomex.repository.EstadoRepository;
import com.rg.sepomex.importsepomex.repository.MunicipioRepository;
import com.rg.sepomex.importsepomex.repository.TipoAsentamientoRepository;

/**
 * Clase que lee un archivo de excel con los datos de estados, municipios,
 * ciudades, para posteriormente guardarlos en base de datos
 * 
 * @author Ramon Garcia
 *
 */
@Service
public class LectorExcel {

	private final int COLUMNA_CODIGO_POSTAL = 0;
	private final int COLUMNA_ASENTAMIENTO = 1;
	private final int COLUMNA_TIPO_ASENTAMIENTO = 2;
	private final int COLUMNA_MUNICIPIO = 3;
	private final int COLUMNA_CIUDAD = 5;

	@Autowired
	private EstadoRepository estadoRepository;

	@Autowired
	private MunicipioRepository municipioRepository;

	@Autowired
	private CiudadRepository ciudadRepository;

	@Autowired
	private AsentamientoRepository asentamientoRepository;

	@Autowired
	private TipoAsentamientoRepository tipoAsentamientoRepository;

	private Map<String, Long> tipoAsentamientos = new HashMap<>();

	/**
	 * Guarda un estado
	 * 
	 * @param name Nombre del estado
	 * @return el identificador del estado guardado
	 */
	private Integer guardarEstado(String name) {
		Estado estado = new Estado();
		estado.setNombre(name);
		estado.setNombreOficial(name);

		estado = estadoRepository.save(estado);
		return estado.getId();
	}

	/**
	 * Guarda un municipio
	 * 
	 * @param name     Nombre del municipio
	 * @param idEstado Identificador del estado al que pertenece
	 * @return Identificador del municipio guardado
	 */
	private Long guardarMunicipio(String name, Integer idEstado) {
		Municipio municipio = new Municipio();
		municipio.setNombre(name);
		Estado estado = new Estado();
		estado.setId(idEstado);
		municipio.setEstado(estado);
		municipio = municipioRepository.save(municipio);
		return municipio.getId();
	}

	/**
	 * Guarda un tipo de asentamiento
	 * 
	 * @param name  Nombre del asentamiento
	 * @param clave Clave del asentamiento
	 * @return Identificador de asentamiento guardado
	 */
	private Long guardarTipoAsentamiento(String name, String clave) {
		TipoAsentamiento tipoAsentamiento = new TipoAsentamiento();
		tipoAsentamiento.setNombre(name);
		tipoAsentamiento.setClaveAsentamiento(clave);
		tipoAsentamiento = tipoAsentamientoRepository.save(tipoAsentamiento);
		return tipoAsentamiento.getId();
	}

	/**
	 * Guarda una ciudad
	 * 
	 * @param name        Nombre de la ciudad
	 * @param idMunicipio Identificador del municipio al que pertenece
	 * @return Identificador de la ciudad guardada
	 */
	private Long guardarCiudad(String name, Long idMunicipio) {
		Ciudad ciudad = new Ciudad();
		ciudad.setNombre(name);
		Municipio municipio = new Municipio();
		municipio.setId(idMunicipio);

		ciudad.setMunicipio(municipio);

		ciudad = ciudadRepository.save(ciudad);
		return ciudad.getId();
	}

	/**
	 * Guarda un asentamiento
	 * 
	 * @param cp                 Codigo postal
	 * @param name               Nombre del asentamiento
	 * @param idTipoAsentamiento Identificador de tipo de asentamiento al que
	 *                           pertenece
	 * @param idMunicipio        Identificador de municipio al que pertenece
	 * @param idCiudad           Identificador de la ciudad al que pertenece
	 */
	private void guardarAsentamiento(String cp, String name, Long idTipoAsentamiento, Long idMunicipio, Long idCiudad) {
		Asentamiento asentamiento = new Asentamiento();
		asentamiento.setCodigoPostal(Integer.valueOf(cp));
		asentamiento.setNombre(name);
		TipoAsentamiento tipo = new TipoAsentamiento();
		tipo.setId(idTipoAsentamiento);
		Municipio municipio = new Municipio();
		municipio.setId(idMunicipio);
		Ciudad ciudad = new Ciudad();
		ciudad.setId(idCiudad);
		asentamiento.setTipoAsentamiento(tipo);
		asentamiento.setMunicipio(municipio);
		if (idCiudad != null)
			asentamiento.setCiudad(ciudad);
		asentamientoRepository.save(asentamiento);
	}

	/**
	 * Recorre cada una de las hojas del libro, cada hoja a partir de la segunda es
	 * cada uno de los estado de México
	 * 
	 * @param workbook Libro excel
	 */
	private void leerEstados(Workbook workbook) {
		for (int i = 1; i < 33; i++) {
			Sheet sheetEstate = workbook.getSheetAt(i);
			String nameSheet = sheetEstate.getSheetName().replace("_", " ");
			Integer idEstado = guardarEstado(nameSheet);

			Map<String, Long> municipios = leerMunicipios(sheetEstate, idEstado);
			Map<String, Long> ciudades = leerCiudades(sheetEstate, municipios);
			leerTiposAsentamientos(sheetEstate);
			leerAsentamientos(sheetEstate, municipios, ciudades);
		}
	}

	/**
	 * Recorre cada una de las filas de una hoja para guardar los municipios
	 * 
	 * @param sheet Hoja de libro excel
	 * @param idEstado Identificador del estado
	 * @return Mapa de municipios en un estado
	 */
	private Map<String, Long> leerMunicipios(Sheet sheet, Integer idEstado) {
		Iterator<?> iterator = sheet.iterator();
		Map<String, Long> municipios = new HashMap<>();
		iterator.next();
		while (iterator.hasNext()) {
			Row nextRow = (Row) iterator.next();
			Cell cell = nextRow.getCell(COLUMNA_MUNICIPIO);
			String name = "";
			if (cell != null) {
				name = nextRow.getCell(COLUMNA_MUNICIPIO).getStringCellValue();
			}
			if (name != null && name.trim() != "") {

				if (!municipios.containsKey(name)) {
					Long id = guardarMunicipio(name, idEstado);
					municipios.put(name, id);
				}
			}
		}
		return municipios;
	}

	/**
	 * Recorre cada una de las filas de una hoja para guardar las ciudades
	 * @param sheet Hoja del libro excel
	 * @param municipios Mapa de municipios en el Estado
	 * @return Mapa con las ciudades en un estado
	 */
	private Map<String, Long> leerCiudades(Sheet sheet, Map<String, Long> municipios) {
		Iterator<?> iterator = sheet.iterator();
		Map<String, Long> ciudades = new HashMap<>();
		iterator.next();
		while (iterator.hasNext()) {
			Row nextRow = (Row) iterator.next();
			Cell cell = nextRow.getCell(COLUMNA_CIUDAD);
			if (cell == null) {
				continue;
			}
			String name = nextRow.getCell(COLUMNA_CIUDAD).getStringCellValue();
			String municipio = nextRow.getCell(COLUMNA_MUNICIPIO).getStringCellValue();
			if (name != null && name.trim() != "") {
				Long idMunicipio = municipios.get(municipio);
				if (!ciudades.containsKey(name)) {
					Long idCiudad = guardarCiudad(name, idMunicipio);
					ciudades.put(name, idCiudad);
				}
			}
		}
		return ciudades;
	}

	/**
	 * Recorre cada una de las filas de una hoja para registrar los tipos de asentamiento
	 * @param sheet Hoja de excel
	 */
	private void leerTiposAsentamientos(Sheet sheet) {
		Iterator<?> iterator = sheet.iterator();
		iterator.next();
		while (iterator.hasNext()) {
			Row nextRow = (Row) iterator.next();
			String name = nextRow.getCell(COLUMNA_TIPO_ASENTAMIENTO).getStringCellValue();
			String clave = nextRow.getCell(10).getStringCellValue();
			if (name != null && name.trim() != "") {
				if (!tipoAsentamientos.containsKey(name)) {
					System.out.println("Tipo de asentamiento " + name);
					Long idTipoAsentamiento = guardarTipoAsentamiento(name, clave);
					tipoAsentamientos.put(name, idTipoAsentamiento);
				}
			}
		}
	}

	/**
	 * Recorre cada una de las filas de una hoja para registrar los asentamientos
	 * @param sheet Hoja de un archivo excel
	 * @param municipios Mapa con los municipios de un estado
	 * @param ciudades Mapa con las ciudades de un estado
	 */
	private void leerAsentamientos(Sheet sheet, Map<String, Long> municipios, Map<String, Long> ciudades) {
		Iterator<?> iterator = sheet.iterator();
		iterator.next();
		while (iterator.hasNext()) {
			Row nextRow = (Row) iterator.next();
			String cp = nextRow.getCell(COLUMNA_CODIGO_POSTAL).getStringCellValue();
			String name = nextRow.getCell(COLUMNA_ASENTAMIENTO).getStringCellValue();
			if (cp != null && cp.trim() != "") {

				String tipoAsentamiento = nextRow.getCell(COLUMNA_TIPO_ASENTAMIENTO).getStringCellValue();
				String municipio = nextRow.getCell(COLUMNA_MUNICIPIO).getStringCellValue();
				Cell cell = nextRow.getCell(COLUMNA_CIUDAD);
				String ciudad = "";
				Long idCiudad = null;
				if (cell != null) {
					ciudad = nextRow.getCell(COLUMNA_CIUDAD).getStringCellValue();
					idCiudad = ciudades.get(ciudad);
				}
				Long idTipoAsentamiento = tipoAsentamientos.get(tipoAsentamiento);
				Long idMunicipio = municipios.get(municipio);
				guardarAsentamiento(cp, name, idTipoAsentamiento, idMunicipio, idCiudad);
			}
		}
	}

	/**
	 * 
	 */
	public void leerArchivo(String ruta) {
		try {
			String rutaArchivoExcel = ruta;
			FileInputStream inputStream = new FileInputStream(new File(rutaArchivoExcel));
			Workbook workbook = new XSSFWorkbook(inputStream);
			leerEstados(workbook);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
