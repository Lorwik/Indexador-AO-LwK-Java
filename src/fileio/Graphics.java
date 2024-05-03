package fileio;

import utils.ByteMigration;
import data.GrhData;

import java.io.*;

public class Graphics {

    // Instancia única de Graphics
    private static Graphics instance;

    private int GrhCount;
    private long version;
    private GrhData[] GrhData;

    /**
     * Constructor privado para evitar instanciación directa
     */
    private Graphics() {
        // Constructor privado para evitar instanciación directa
    }

    /**
     * Método estático para obtener la única instancia de ByteMigration
     * @return
     */
    public static Graphics getInstance() {
        if (instance == null) {
            instance = new Graphics();
        }
        return instance;
    }

    // GET
    public int getGrhCount() { return GrhCount; }
    public long getVersion() { return version; }
    public data.GrhData[] getGrhData() { return GrhData; }

    // SET
    public void setGrhCount(int grhCount) { GrhCount = grhCount; }
    public void setVersion(long version) { this.version = version; }
    public void setGrhData(data.GrhData[] grhData) { GrhData = grhData; }

    /**
     * Metodo que lee un archivo de acceso aleatorio
     * @return
     */
    public Boolean loadGrhData() {
        int Grh;

        // Obtener una instancia de ByteMigration
        ByteMigration byteMigration = ByteMigration.getInstance();

        File Archivo = new File("resources/Init/Graficos.ind");

        RandomAccessFile file = null;

        try {
            System.out.println("Comenzando a leer desde " + Archivo.getAbsolutePath());

            file = new RandomAccessFile(Archivo, "r");

            //Nos posicionamos al inicio del fichero
            file.seek(0);

            //Leemos la versi�n del archivo
            this.version = byteMigration.bigToLittle_Int(file.readInt());

            //Leemos la cantidad de Grh indexados
            this.GrhCount = byteMigration.bigToLittle_Int(file.readInt());

            //Iniciamos el array de GrhData
            this.GrhData = new GrhData[GrhCount + 1];

            //Mientras no llegue al final del archivo leemos...
            for(;;) {

                //Leemos el Grh que toca
                Grh = byteMigration.bigToLittle_Int(file.readInt());

                //Construimos el nuevo Objeto
                this.GrhData[Grh] = new GrhData();

                //Leemos el numero de frames
                this.GrhData[Grh].setNumFrames(byteMigration.bigToLittle_Short(file.readShort()));

                //El numero de Frames es mayor a 1 es que se trata de una animaci�n
                if(this.GrhData[Grh].getNumFrames() > 1) {
                    int[]frames = new int[this.GrhData[Grh].getNumFrames() + 1];

                    for(int i = 1; i <= this.GrhData[Grh].getNumFrames(); i++){
                        frames[i] = byteMigration.bigToLittle_Int(file.readInt());
                    }

                    this.GrhData[Grh].setFrames(frames);

                    this.GrhData[Grh].setSpeed((int)byteMigration.bigToLittle_Float(file.readFloat()));

                }else { //De lo contrario se trata de una sola imagen
                    //Leemos el numero de la imagen
                    this.GrhData[Grh].setFileNum(byteMigration.bigToLittle_Int(file.readInt()));

                    //Leemos la posicion del frame
                    this.GrhData[Grh].setsX(byteMigration.bigToLittle_Short(file.readShort()));
                    this.GrhData[Grh].setsY(byteMigration.bigToLittle_Short(file.readShort()));

                    //Leemos el tama�o de la imagen
                    this.GrhData[Grh].setTileWidth(byteMigration.bigToLittle_Short(file.readShort()));
                    this.GrhData[Grh].setTileHeight(byteMigration.bigToLittle_Short(file.readShort()));
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
     * Metodo que guarda todos los Grh en un archivo de texto plano
     * @throws IOException
     */
    public void exportar() throws IOException {
        String GrhStr;

        //Indicamos la ruta del archivo
        File archivo = new File("Graficos.ini");
        java.io.FileWriter fw = null;
        BufferedWriter bw = null;

        try {

            //Iniciamos la escritura
            fw = new java.io.FileWriter(archivo);
            bw = new BufferedWriter(fw);

            //Escribimos la cabecera
            bw.write("[INIT]\n");
            bw.write("NumGrh=" + this.GrhCount + "\n");
            bw.write("Version=" + this.version + "\n\n");

            bw.write("[Graphics]\n");

            for (int i = 1; i <= GrhCount; i++) {
                //�El objeto existe?
                if (GrhData[i] != null) {

                    GrhStr = "Grh" + i + "=" + GrhData[i].getNumFrames() + "-";
                    //�Se trata de una animacion?
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

            System.out.println("Exportaci�n completada!");
        } catch (NullPointerException e) {
            System.out.println("ERROR: " + e);
        } finally {
            bw.close();
        }
    }
}
