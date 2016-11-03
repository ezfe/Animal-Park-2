import java.util.*;

public abstract class Plant extends Species
{
    Random generator;
    
    public Plant(String n, String sym, List<String> s, double dm, double ds, double be, double me, double le, double ie, double pm, double ps) {    
        super(n, sym, s, dm, ds, be, me, le, ie, pm, ps);
        generator = new Random(Simulation.SEED);
    }
    
    /**
     * This method provides and easy way to enforce the order of behaviors the species can make
     * Each behavior method returns true if it works, which in turn stops any further behaviors from happening that turn
     */
    public void activity() {
        if(die()) {
            //System.out.println("Plant Die");
            return;
        }
        if(birth()) {
            //System.out.println("Plant Birth");
            return;
        }
        if(eat()) {
            //System.out.println("Plant Eat");
            return;
        }
        if(move()) {
            //System.out.println("Plant Move");
            return;
        }
    }
    
    /**
     * Checks if the species doesn't have enough energy or is too old
     * The species is added to the list of deaths at the corresponding turn and species indices
     * @return boolean - true if the animal dies, false otherwise
     */
    public boolean die() {
        //Check for energy
        this.energy -= this.livingEnergy;
        if(this.energy <= 0) {
            int index = species.indexOf(this.name);
            try {
                deaths.get(this.getCell().getWorld().getSteps()).set(index, deaths.get(this.getCell().getWorld().getSteps()).get(index) + 1);
            } catch(Exception e) {}
            this.getCell().setPlant(null);
            this.setCell(null);
            return true;
        }
        this.age++;
        //Check for age
        int deathAge = (int)(this.deathMedian + (this.deathStd * this.generator.nextGaussian()));
        if(age >= deathAge) {
            int index = species.indexOf(this.name);
            try {
                deaths.get(this.getCell().getWorld().getSteps()).set(index, deaths.get(this.getCell().getWorld().getSteps()).get(index) + 1);
            } catch(Exception e) {}
            this.getCell().setPlant(null);
            this.setCell(null);
            return true;
        }
        return false;
    }
    
    /**
     * Checks if the species has enough energy and room nearby to give birth
     * The child is added to the list of births at the corresponding turn and species indices
     * @return boolean - true if the animal gives birth, false otherwise
     */
    public boolean birth() {
        if(this.energy >= this.birthEnergy) {
            //This loops through the adjacent cells, denoted as ranging from -1 to 1 in the horizontal and vertical directions in relation to the current cell
            for(int i = -1; i <= 1; i++) {
                for(int j = -1; j <= 1; j++) {
                    Cell place = this.getCell().getAdjacent(i,j); //Get adjacent uses the -1 to 1 range to find the cell
                    if(i == 0 && j == 0) {}
                    else if(place != null && place.getPlant() == null) {
                        place = this.getCell().getAdjacent(i,j);
                        double parentEnergy = this.getEnergy()/2.0; //Important to halve the parent's energy to give to the child
                        this.setEnergy(parentEnergy);
                        Species child = null;
                        if(this instanceof Vegetable) {
                            child = new Vegetable(this);
                        } else if(this instanceof Fruit) {
                            child = new Fruit(this);
                        }
                        child.setEnergy(parentEnergy);
                        place.setPlant((Plant)child);
                        child.setCell(place);
                        int index = species.indexOf(this.name);
                        try {
                            births.get(this.getCell().getWorld().getSteps()).set(index, births.get(this.getCell().getWorld().getSteps()).get(index) + 1);
                        } catch(Exception e) {}
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * Absorbs light according to the energy level dictated by the config file
     * @return boolean - true because plants will absorb energy if given the opportunity
     */
    public boolean eat() {
        this.energy += this.getCell().getWorld().getLightEnergy();
        if(this.energy > this.maxEnergy) {
            this.energy = this.maxEnergy;
        }
        return true;
    }
    
    /**
     * Plants never move
     * @return boolean - false because plants don't move
     */
    public boolean move() {
        return false;
    }
}
