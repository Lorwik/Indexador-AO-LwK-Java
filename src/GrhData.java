
public class GrhData {
	//Variables
    private int sX;
    private int sY;
    private int FileNum;
    private int TileWidth;
    private int TileHeight;
    private int NumFrames;
    private int Frames[];
    private int Speed;
    

    //Constructor por defecto
    GrhData(){
    	this.FileNum = 0;
    	this.NumFrames = 0;
    	this.sX = 0;
    	this.sY = 0;
    	this.TileWidth = 0;
    	this.TileHeight = 0;
    	this.Speed = 0;
    }
    
    //Metodos SET
    public void setFileNum(int FileNum) {this.FileNum = FileNum;}
    public void setNumFrames(int NumFrames) {this.NumFrames = NumFrames;}
    public void setFrames(int[] Frames) {this.Frames = Frames;} //En caso de que sea una animación
    public void setsX(int sX) {this.sX = sX;}
    public void setsY(int sY) {this.sY = sY;}
    public void setTileWidth(int TileWidth) {this.TileWidth = TileWidth;}
    public void setTileHeight(int TileHeight) {this.TileHeight = TileHeight;}
    public void setSpeed(int Speed) {this.Speed = Speed;}
    
    //Metodos GET
    public int getFileNum() {return this.FileNum;}
    public int getNumFrames() {return this.NumFrames;}
    public int[] getFrames() {return this.Frames;} //En caso de que sea una animación
    public int getsX() {return this.sX;}
    public int getsY() {return this.sY;}
    public int getTileWidth() {return this.TileWidth;}
    public int getTileHeight() {return this.TileHeight;}
    public int getSpeed() {return this.Speed;}
}
