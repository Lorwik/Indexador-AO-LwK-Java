import fileio.Graphics;
import utils.ByteMigration;

import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Scanner;

public class Main {
	private static final Graphics graphics = Graphics.getInstance();

	/**
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		int opcion = 0;
		boolean cargado = false;
		
		System.out.println("@####################################################################@");
		System.out.println("# Bienvenido al Indexador de AO LwK en Java                          #");
		System.out.println("# Este Indexador ha sido desarrollado por Lorwik con fines educativos#");
		System.out.println("@####################################################################@");
		
		Scanner Entrada = new Scanner(System.in);
		
		while(opcion != 9) {
			
			System.out.println("�Que quieres hacer?");
			System.out.println("1 - Cargar GrhData.ind");
			System.out.println("2 - Mostrar lista de Grh cargados");
			System.out.println("3 - Exportar Grh a Graficos.ini");
			System.out.println("9 - Salir");
			
			opcion = Entrada.nextInt();
			
			switch(opcion) {
				case 1: //Cargar Graficos.ind en memoria
					System.out.println("Cargando GrhData.ind");
					if (graphics.loadGrhData()) {
						System.out.println("Graficos.ind leidos correctmente");
						System.out.println(graphics.getGrhCount() + " Grh leidos");
						System.out.println("Versi�n de Graficos.ind " + graphics.getVersion());
						cargado = true;
					}else {
						System.out.println("No se pudo cargar Graficos.ind");
						cargado = false;
					}
				break;
				
				case 2:
					if (cargado == true) {
						System.out.println("Mostrando lista de Grh");
						PrintGrh();
					}else {
						System.out.println("No se han cargado los graficos en memoria.");
					}
					break;
					
				case 3:
					if (cargado == true) {
						System.out.println("Exportando Grh's a Graficos.ini");
						graphics.exportar();
					}else {
						System.out.println("No se han cargado los graficos en memoria.");
					}
					break;
					
				case 9:
					System.out.println("Saliendo...");
					break;
					
				default:
					System.out.println("Opción incorrecta");
					break;
			}
		}
		Entrada.close();	
	}

	/**
	 * Metodo que imprime los Grh cargados en memoria
	 */
	static void PrintGrh() {
		for (int i = 1; i <= graphics.getGrhCount() + 1; i++) {
			System.out.print("Grh" + i + "=" + graphics.getGrhData()[i].getNumFrames() + "-");
			//�Se trata de una animacion?
			if(graphics.getGrhData()[i].getNumFrames() > 1) {
				int[]frames = new int[graphics.getGrhData()[i].getNumFrames() + 1];

				for(int j = 1; j <= graphics.getGrhData()[i].getNumFrames(); j++){
					System.out.print(frames[j] + "-");
				}

				System.out.println(graphics.getGrhData()[i].getSpeed());
			}else {
				System.out.println(graphics.getGrhData()[i].getFileNum() + "-" + graphics.getGrhData()[i].getsX() + "-" + graphics.getGrhData()[i].getsY() + "-" + graphics.getGrhData()[i].getTileWidth() + "-" + graphics.getGrhData()[i].getTileHeight());
			}
		}
	}
}