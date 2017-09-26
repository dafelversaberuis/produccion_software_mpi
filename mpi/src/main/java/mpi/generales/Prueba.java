package mpi.generales;

import java.text.SimpleDateFormat;

public class Prueba {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {

			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			// String cliente = " 1234569 "; // "1234569 - 9";

			String FechaI = "2017-12-31 05:40";

			String FechaF = "2018-01-01 05:44";
			ConsultarFuncionesAPI funciones = new ConsultarFuncionesAPI();
			System.out.println(funciones.getMinutosDosFechas(formato.parse(FechaI), formato.parse(FechaF)));

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
