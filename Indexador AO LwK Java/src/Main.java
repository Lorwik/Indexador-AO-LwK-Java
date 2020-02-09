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
	static int GrhCount;
	static long version;
	static GrhData[] GrhData;
	
	/**
	 * 
	 * @param bigendian
	 * @return
	 */
    private static int bigToLittle_Int(int bigendian){
        ByteBuffer buf = ByteBuffer.allocate(4);

        buf.order(ByteOrder.BIG_ENDIAN);
        buf.putInt(bigendian);

        buf.order(ByteOrder.LITTLE_ENDIAN);
        return buf.getInt(0);
    }

	/**
	 * 
	 * @param bigendian
	 * @return
	 */
    private static float bigToLittle_Float(float bigendian){
        ByteBuffer buf = ByteBuffer.allocate(4);

        buf.order(ByteOrder.BIG_ENDIAN);
        buf.putFloat(bigendian);

        buf.order(ByteOrder.LITTLE_ENDIAN);
        return buf.getFloat(0);
    }
    
	/**
	 * 
	 * @param bigendian
	 * @return
	 */
    private static short bigToLittle_Short(short bigendian){
        ByteBuffer buf = ByteBuffer.allocate(2);

        buf.order(ByteOrder.BIG_ENDIAN);
        buf.putShort(bigendian);

        buf.order(ByteOrder.LITTLE_ENDIAN);
        return buf.getShort(0);
    }
    
    /**
     * Metodo que lee un archivo de acceso aleatorio
     * @return
     */
	static Boolean LoadGrhData() {
		int Grh;
		
		File Archivo = new File("Graficos.ind");
		
		RandomAccessFile file = null;
		
		try {
			System.out.println("Comenzando a leer desde " + Archivo.getAbsolutePath());
			
			file = new RandomAccessFile(Archivo, "r");
			
			//Nos posicionamos al inicio del fichero
			file.seek(0);
			
			//Leemos la versión del archivo
			version = bigToLittle_Int(file.readInt());

			//Leemos la cantidad de Grh indexados
			GrhCount = bigToLittle_Int(file.readInt());

			//Iniciamos el array de GrhData
			GrhData = new GrhData[GrhCount + 1];

			//Mientras no llegue al final del archivo leemos...
			for(;;) {
				//Leemos el Grh que toca
				Grh =  bigToLittle_Int(file.readInt());

				//Construimos el nuevo Objeto
				GrhData[Grh] = new GrhData();

				//Leemos el numero de frames
				GrhData[Grh].setNumFrames(bigToLittle_Short(file.readShort()));
				
				//El numero de Frames es mayor a 1 es que se trata de una animación
				if(GrhData[Grh].getNumFrames() > 1) {
					int[]frames = new int[GrhData[Grh].getNumFrames() + 1];
					
                    for(int i = 1; i <= GrhData[Grh].getNumFrames(); i++){
                        frames[i] = bigToLittle_Int(file.readInt());
                    }
                    
                    GrhData[Grh].setFrames(frames);

                    GrhData[Grh].setSpeed((int)bigToLittle_Float(file.readFloat()));
                    	
				}else { //De lo contrario se trata de una sola imagen
					//Leemos el numero de la imagen
					GrhData[Grh].setFileNum(bigToLittle_Int(file.readInt()));

					//Leemos la posicion del frame
					GrhData[Grh].setsX(bigToLittle_Short(file.readShort()));
					GrhData[Grh].setsY(bigToLittle_Short(file.readShort()));
					
					//Leemos el tamaño de la imagen
					GrhData[Grh].setTileWidth(bigToLittle_Short(file.readShort()));
					GrhData[Grh].setTileHeight(bigToLittle_Short(file.readShort()));
				}
				
				//Si he recorrido todos los bytes salgo del bucle
				if (file.getFilePointer() == file.length())break;
			}
			
		} catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            return false;
        } catch (EOFException e) {
            System.out.println("Fin de fichero");
            return false;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
        	try {
				file.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		return true;
	}
	
	/**
	 * Metodo que imprime los Grh cargados en memoria
	 */
	static void PrintGrh() {
		for (int i = 1; i <= GrhCount + 1; i++) {
			System.out.print("Grh" + i + "=" + GrhData[i].getNumFrames() + "-");
			//¿Se trata de una animacion?
			if(GrhData[i].getNumFrames() > 1) {
				int[]frames = new int[GrhData[i].getNumFrames() + 1];
				
                for(int j = 1; j <= GrhData[i].getNumFrames(); j++){
                    System.out.print(frames[j] + "-");
                }
                
                System.out.println(GrhData[i].getSpeed());
			}else {
				System.out.println(GrhData[i].getFileNum() + "-" + GrhData[i].getsX() + "-" + GrhData[i].getsY() + "-" + GrhData[i].getTileWidth() + "-" + GrhData[i].getTileHeight());
			}
		}
	}
	
	/**
	 * Metodo que guarda todos los Grh en un archivo de texto plano
	 * @throws IOException
	 */
	static void ExportarGrh() throws IOException {
		String GrhStr;
		
		//Indicamos la ruta del archivo
		File archivo = new File("Graficos.ini");
		FileWriter fw = null;
		BufferedWriter bw = null;
		
		try {
			
			//Iniciamos la escritura
			fw = new FileWriter(archivo);
			bw = new BufferedWriter(fw);
		
			//Escribimos la cabecera
			bw.write("[INIT]\n");
			bw.write("NumGrh=" + GrhCount + "\n");
			bw.write("Version=" + version + "\n\n");
			
			bw.write("[Graphics]\n");
			
			for (int i = 1; i <= GrhCount; i++) {
				//¿El objeto existe?
				if (GrhData[i] != null) {
				
					GrhStr = "Grh" + i + "=" + GrhData[i].getNumFrames() + "-";
					//¿Se trata de una animacion?
					if(GrhData[i].getNumFrames() > 1) {
						int[]frames = new int[GrhData[i].getNumFrames() + 1];
						
						frames = GrhData[i].getFrames();
						
		                for(int j = 1; j <= GrhData[i].getNumFrames(); j++){
		                	GrhStr += frames[j] + "-";
		                }
		                
		                GrhStr += Integer.toString(GrhData[i].getSpeed());
					}else {
						GrhStr += GrhData[i].getFileNum() + "-" + GrhData[i].getsX() + "-" + GrhData[i].getsY() + "-" + GrhData[i].getTileWidth() + "-" + GrhData[i].getTileHeight();
					}
					
					bw.write(GrhStr + "\n");
				}
			}
			
			System.out.println("Exportación completada!");
		} catch (NullPointerException e) {
			System.out.println("ERROR: " + e);
		} finally {
			bw.close();
		}
	}
	
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
			
			System.out.println("¿Que quieres hacer?");
			System.out.println("1 - Cargar GrhData.ind");
			System.out.println("2 - Mostrar lista de Grh cargados");
			System.out.println("3 - Exportar Grh a Graficos.ini");
			System.out.println("9 - Salir");
			
			opcion = Entrada.nextInt();
			
			switch(opcion) {
				case 1: //Cargar Graficos.ind en memoria
					System.out.println("Cargando GrhData.ind");
					if (LoadGrhData() == true) {
						System.out.println("Graficos.ind leidos correctmente");
						System.out.println(GrhCount + " Grh leidos");
						System.out.println("Versión de Graficos.ind " + version);
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
						ExportarGrh();
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
}