import java.util.*;

/**
 * Animal abstract class
 * 
 * @author Ezekiel Elin
 * @author Unknown Author
 * @version 11/02/2016
 */
public abstract class Animal extends Species {
    Random generator;

    public Animal(String n, String sym, List<String> s, double dm, double ds, double be, double me, double le, double ie, double pm, double ps) {    
        super(n, sym, s, dm, ds, be, me, le, ie, pm, ps);
        generator = new Random(Simulation.SEED);
    }

    /**
     * This method provides and easy way to enforce the order of behaviors the species can make
     * Each behavior method returns true if it works, which in turn stops any further behaviors from happening that turn
     */
    public void activity() {
        if(die()) {
            //System.out.println("Animal Die");
            return;
        }
        if(birth()) {
            //System.out.println("Animal Birth");
            return;
        }
        if(eat()) {
            //System.out.println("Animal Eat");
            return;
        }
        if(move()) {
            //System.out.println("Animal Move");
            return;
        }
    }

    /**
     * Checks if the species doesn't have enough energy or is too old
     * The species is added to the list of deaths at the corresponding turn and species indices
     * @return boolean - returns true if the animal dies, false otherwise
     */
    public boolean die() {
        //Check for energy
        this.energy -= this.livingEnergy;
        if(this.energy <= 0) {
            int index = species.indexOf(this.name);
            try {
                deaths.get(this.getCell().getWorld().getSteps()).set(index, deaths.get(this.getCell().getWorld().getSteps()).get(index) + 1);
            } catch(Exception e) {}
            this.getCell().setAnimal(null);
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
            this.getCell().setAnimal(null);
            this.setCell(null);
            return true;
        }
        return false;
    }

    /**
     * Checks if the species has enough energy and room nearby to give birth
     * The child is added to the list of births at the corresponding turn and species indices
     * @return boolean - true if animal gives birth, false otherwise
     */
    public boolean birth() {
        if(this.energy >= this.birthEnergy) {
            //This loops through the adjacent cells, denoted as ranging from -1 to 1 in the horizontal and vertical directions in relation to the current cell
            for(int i = -1; i <= 1; i++) {
                for(int j = -1; j <= 1; j++) {
                    Cell birthCell = this.getCell().getAdjacent(i,j); //Get adjacent uses the -1 to 1 range to find the cell
                    if(i == 0 && j == 0) {}
                    else if(birthCell != null && birthCell.getAnimal() == null) {
                        Cell place = this.getCell().getAdjacent(i,j);
                        double parentEnergy = this.getEnergy()/2.0; //Important to halve the parent's energy to give to the child
                        this.setEnergy(parentEnergy);
                        Species child = null;
                        if(this instanceof Carnivore) {
                            child = new Carnivore(this);
                        } else if(this instanceof Herbivore) {
                            child = new Herbivore(this);
                        } else if(this instanceof Omnivore) {
                            child = new Omnivore(this);
                        }
                        child.setEnergy(parentEnergy);
                        place.setAnimal((Animal)child);
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

    public abstract boolean eat();

    /**
     * Looks at adjacent cells and checks if any animal it can eat is there
     * If it finds a prey, it is removed from the board and added to the death list
     * @return boolean - true if an acceptable animal is found and eaten, false otherwise
     */
    public boolean eatAnimal() {
        for(int i = -1; i <= 1; i++) {
            for(int j = -1; j <= 1; j++) {
                if(i == 0 && j == 0) {}
                else {
                    Cell preyCell = this.getCell().getAdjacent(i,j);
                    if(preyCell != null) {
                        Animal prey = preyCell.getAnimal();
                        if(prey != null && this.energySources.contains(prey.getName())) {
                            this.setEnergy(this.getEnergy() + prey.getEnergy());
                            if(this.getEnergy() > this.getMaxEnergy()) {
                                this.setEnergy(this.getMaxEnergy());
                            }
                            int curSteps = prey.getCell().getWorld().getSteps();

                            ArrayList<ArrayList<Integer>> deathAL = Species.getDeaths();
                            int index = Species.getSpecies().indexOf(prey.getName());

                            //System.out.println("Death by Eating");
                            try {
                                deathAL.get(curSteps).set(index, deathAL.get(curSteps).get(index) + 1);
                            } catch(Exception e) {}
                            prey.getCell().setAnimal(null);
                            prey.setCell(null);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Looks at current cell and checks if any plant it can eat is there
     * If it finds a prey, it is removed from the board and added to the death list
     * @return boolean - true if an acceptable plant is found and eaten, false otherwise
     */
    public boolean eatPlant() {
        Cell preyCell = this.getCell();
        if(preyCell != null) {
            Plant prey = preyCell.getPlant();
            if(prey != null && this.energySources.contains(prey.getName())) {
                this.setEnergy(this.getEnergy() + prey.getEnergy());
                int curSteps = prey.getCell().getWorld().getSteps();

                ArrayList<ArrayList<Integer>> deathAL = Species.getDeaths();
                int index = Species.getSpecies().indexOf(prey.getName());

                //System.out.println("Death by Eating");
                try {
                    deathAL.get(curSteps).set(index, deathAL.get(curSteps).get(index) + 1);
                } catch(Exception e) {}
                prey.getCell().setPlant(null);
                prey.setCell(null);
                return true;
            }
        }
        return false;
    }

    /**
     * If all else fails, the animal tries to move to an adjacent space
     * @return boolean - true if the animal moves, false otherwise
     */
    public boolean move() {
        for(int i = 0; i < 20; i++) {
            Cell temp = this.getCell().getAdjacent(generator.nextInt(3)-1,generator.nextInt(3)-1);
            if(temp != null && temp.getAnimal() == null) {
                temp.setAnimal(this);
                this.getCell().setAnimal(null);
                this.setCell(temp);
                return true;
            }
        }
        return false;
    }
}
