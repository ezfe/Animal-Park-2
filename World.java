import java.util.*;

/**
 * Main World Class
 * 
 * @author Ezekiel Elin
 * @author Unknown Author
 * @version 11/02/2016
 */
public class World {

    private List<List<Cell>> board;
    private int lightEnergy;
    private int steps;
    
    public World(int m, int n, int l) {
        lightEnergy = l;
        steps = 0;
        board = new ArrayList<List<Cell>>();
        for(int i = 0; i < m; i++) {
            board.add(new ArrayList<Cell>());
            for(int j = 0; j < n; j++) {
                board.get(i).add(new Cell(this));
            }
        }
    }
    
    public int getLightEnergy() {
        return lightEnergy;
    }
    
    public Cell get(int row, int col) {
        return this.board.get(row).get(col);
    }
    
    public int getHeight() {
        return this.board.size();
    }
    
    public int getWidth() {
        return this.board.get(0).size();
    }
    
    public int getSteps() {
        return this.steps;
    }
    
    /**
     * Goes through each cell and runs the activities for each species
     * Also adds a new element to the birth and death lists so they can be tracker for the turn
     */
    public void turn() {
        int curSteps = this.getSteps();
        if(Species.getDeaths().size() == curSteps) {
            Species.getDeaths().add(new ArrayList<Integer>());
            for(int i = 0; i < Species.getSpecies().size(); i++) {
                Species.getDeaths().get(curSteps).add(0);
            }
        }
        if(Species.getBirths().size() == curSteps) {
            Species.getBirths().add(new ArrayList<Integer>());
            for(int i = 0; i < Species.getSpecies().size(); i++) {
                Species.getBirths().get(curSteps).add(0);
            }
        }
        for(int i = 0; i < board.size(); i++) {
            List<Cell> row = board.get(i);
            for(int j = 0; j < row.size(); j++) {
                Cell cell = row.get(j);
                if(cell.getAnimal() != null) {
                    cell.getAnimal().activity();
                }
                if(cell.getPlant() != null) {
                    cell.getPlant().activity();
                }
            }
        }
        steps++;
    }
    
    /**
     * Prints the board in an easy to view way
     */
    public void print() {
        for(int i = 0; i < board.size(); i++) {
            List<Cell> row = board.get(i);
            System.out.print("|");
            for(int j = 0; j < row.size(); j++) {
                Cell cell = row.get(j);
                String message = "";
                if(cell.getAnimal() != null) {
                    message += cell.getAnimal().getRepresentation();
                }
                if(message.length() > 0 && cell.getPlant() != null) {
                    message += "/";
                }
                if(cell.getPlant() != null) {
                    message += cell.getPlant().getRepresentation();
                }
                System.out.print(message + "\t|");
            }
            System.out.println();
            System.out.println("-----------------------------------------------------------------------------------------------------");
        }
    }
    
    /**
     * Takes a species and generates random coordinates until it finds an empty cell for it
     * @param Species s - the species that should be randomly added to the world
     */
    public void randomAddToWorld(Species s) {
        Random generator = new Random(Simulation.SEED);
        boolean found = false;
        int count = 0; //Prevents infinite loop in case more species are trying to be added than there are spaces for them
        while(!found && count < 10000) {
            count++;
            int row = generator.nextInt(this.board.size());
            int col = generator.nextInt(this.board.get(0).size());
            Cell cell = this.board.get(row).get(col);
            
            /* cannot use mountain spaces */
            if (cell.isMountain()) {
                continue;
            }
            
            if(s instanceof Animal) {
                if(cell.getAnimal() == null) {
                    cell.setAnimal((Animal)s);
                    s.setCell(cell);
                    found = true;
                }
            } else if(s instanceof Plant) {
                if(cell.getPlant() == null) {
                    cell.setPlant((Plant)s);
                    s.setCell(cell);
                    found = true;
                }
            }
        }
    }
}
