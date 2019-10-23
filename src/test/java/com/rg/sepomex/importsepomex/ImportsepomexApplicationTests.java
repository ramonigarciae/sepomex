package com.rg.sepomex.importsepomex;

import com.rg.sepomex.importsepomex.business.LectorExcel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Clase de prueba que le indica la ruta del archvo a leer
 * 
 * @author Ramon Garcia
 *
 */
@SpringBootTest
class ImportsepomexApplicationTests {

	@Autowired
	private LectorExcel lectorService;

	@Test
	void cargar_excel_test() {
		lectorService.leerArchivo("C:\\SEPOMEX.xlsx");
	}

}
