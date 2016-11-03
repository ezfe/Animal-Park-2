/**
 * Cell/tile in the world
 * 
 * @author Ezekiel Elin
 * @author Unknown Author
 * @version 11/02/2016
 */
public class Cell {
    
    private World world;
    private Animal animal;
    private Plant plant;
    private boolean mountain = false;
    
    public Cell(World w) {
        this.world = w;
    }
    
    public Cell(World w, boolean mountain) {
        this.world = w;
        this.mountain = mountain;
    }
    
    public Cell(Animal a) {
        this.animal = a;
    }
    
    public Cell(Plant p) {
        this.plant = p;
    }
    
    public Cell(Animal a, Plant p) {
        this.animal = a;
        this.plant = p;
    }
    
    /**
     * Gets bordering cell from given relative position
     * @param int r - r is the vertical position relative to the current cell, int c - c is the horizontal position relative to the current cell
     * @return Cell - a cell that is in the given position relative to the current cell, null if not found or non existent
     */
    public Cell getAdjacent(int r, int c) {
        if((r < -1 || r > 1) || (c < -1 || c > 1) || (r == 0 && c == 0)) {return null;}
        for(int i = 0; i < this.world.getHeight(); i++) {
            for(int j = 0; j < this.world.getWidth(); j++) {
                try {
                    if(this.world.get(i,j) == this) {
                        return this.world.get(i+r,j+c);
                    }
                } catch(Exception e) {}
            }
        }
        return null;
    }
    
    public Animal getAnimal() {
        return this.animal;
    }
    
    public boolean setAnimal(Animal a) {
        if (this.mountain) {
            return false;
        }
        this.animal = a;
        return true;
    }
    
    public Plant getPlant() {
        return this.plant;
    }
    
    public boolean setPlant(Plant p) {
        if (this.mountain) {
            return false;
        }
        this.plant = p;
        return true;
    }
    
    public World getWorld() {
        return this.world;
    }
    
    public boolean isMountain() {
        return this.mountain;
    }
}
