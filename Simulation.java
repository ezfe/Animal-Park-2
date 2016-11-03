import java.util.*;
import java.io.*;

public class Simulation
{
    public static int SEED = 12345678;
    public File configFile;
    public World world;
    public int maxSteps;
    public int steps;
    
    public static void main(String[] args) {
        String filePath = "config.txt";
        int simulationSteps = 1000;
        SEED = 1;
        
        Simulation sim = new Simulation(filePath, simulationSteps);
        sim.run();
    }
    
    public Simulation(String fp, int s) {
        try {
            this.configFile = new File(fp);
        } catch(Exception e) {
            System.out.println(e);
            System.exit(0);
        }
        this.maxSteps = s;
        this.steps = 0;
    }
    
    public void setWorld(World w) {
        world = w;
    }
    
    /**
     * Creates world and starts loop of commands
     */
    public void run() {
        if(configFile == null) {System.exit(0);}
        initWorld();
        if(world == null) {
            System.out.println("Unable to read config file.");
            return;
        }
        world.print();
        
        Scanner reader = new Scanner(System.in);
        String line = reader.nextLine();
        while(canRun()) {
            if(line.equals("p")) {
                world.print();
            } else if(line.equals("c")) {
                world.turn();
                Species.printInfo();
                steps++;
                if(steps % 50 == 0) {
                    Species.printSummary(50);
                }
            } else if(line.equals("r")) {
                Species.printStatus();
            }
            
            if(line.equals("i")) {
                world.turn();
                steps++;
                if(steps % 50 == 0) {
                    Species.printSummary(50);
                }
            } else {
                line = reader.nextLine();
            }
        }
        world.print();
        Species.printSummary(-1);
        if(this.steps >= this.maxSteps)
            System.out.println("Simulation ended because the turn limit was reached.");
        else if(getPopulationOfWorld() == 0)
            System.out.println("Simulation ended because all of the species died.");
        else if(getChangesInPastSteps() == 0)
            System.out.println("Simulation ended because there were no changes in the last 50 turns.");
    }
    
    /**
     * Creates the world, gets size and light values, and adds all species to a list so they can be added to the world
     */
    public void initWorld() {
        int height = 0,width = 0,light = 0;
        List<Species> species = new ArrayList<Species>();
        Random generator = new Random(SEED);
        //Creates the file scanner
        Scanner fileReader = null;
        try {
            fileReader = new Scanner(configFile);
        } catch(Exception e) {}
        
        if(fileReader != null) {
            while(fileReader.hasNext()) {
                String line = fileReader.nextLine();
                String[] words = line.split(" ");
                if(words[0].equals("size")) {
                    height = Integer.parseInt(words[1]);
                    width = Integer.parseInt(words[2]);
                } else if(words[0].equals("light")) {
                    light = Integer.parseInt(words[1]);
                } else if(words[0].equals("species")) {
                    //Creates all variables to be passed as parameters to the species constructor
                    String name = words[1];
                    String type = words[2];
                    String symbol = words[3];
                    
                    String[] es = words[4].split(",");
                    List<String> energySources = new ArrayList<String>();
                    for(int i = 0; i < es.length; i++) {energySources.add(es[i]);}
                    
                    double popMedian = Double.parseDouble(words[5].split(",")[0]);
                    double popStd = Double.parseDouble(words[5].split(",")[1]);
                    int initialEnergy = Integer.parseInt(words[5].split(",")[2]);
                    double deathMedian = Double.parseDouble(words[6].split(",")[0]);
                    double deathStd = Double.parseDouble(words[6].split(",")[1]);
                    int birthEnergy = Integer.parseInt(words[7]);
                    int maxEnergy = Integer.parseInt(words[8]);
                    int livingEnergy = Integer.parseInt(words[9]);
                    
                    //Creates a number of the new species based on the population distribution and adds them to the list of species to be added to the world
                    Species tempSpecies = null;
                    int numToAdd = (int)(popMedian + (popStd * generator.nextGaussian()));
                    for(int i = 0; i < numToAdd; i++) {
                        if(type.equals("carnivore")) {
                            tempSpecies = new Carnivore(name, symbol, energySources, deathMedian, deathStd, birthEnergy, maxEnergy, livingEnergy, initialEnergy, popMedian, popStd);
                        } else if(type.equals("herbivore")) {
                            tempSpecies = new Herbivore(name, symbol, energySources, deathMedian, deathStd, birthEnergy, maxEnergy, livingEnergy, initialEnergy, popMedian, popStd);
                        } else if(type.equals("omnivore")) {
                            tempSpecies = new Omnivore(name, symbol, energySources, deathMedian, deathStd, birthEnergy, maxEnergy, livingEnergy, initialEnergy, popMedian, popStd);
                        } else if(type.equals("vegetable") || type.equals("vegetable")) {
                            tempSpecies = new Vegetable(name, symbol, energySources, deathMedian, deathStd, birthEnergy, maxEnergy, livingEnergy, initialEnergy, popMedian, popStd);
                        } else if(type.equals("fruit")) {
                            tempSpecies = new Fruit(name, symbol, energySources, deathMedian, deathStd, birthEnergy, maxEnergy, livingEnergy, initialEnergy, popMedian, popStd);
                        }
                        species.add(tempSpecies);
                    }
                }
            }
            //Creates world and adds species to it
            world = new World(height,width,light);
            Species.setStaticWorld(world);
            for(int i = 0; i < species.size(); i++) {
                world.randomAddToWorld(species.get(i));
            }
        } else {
            System.out.println("Unable to read config file.");
            return;
        }
    }
    
    /**
     * Checks the three conditions for ending the sim
     * @return boolean - returns true if the simulation should continue, false if one of the three rules is broken
     */
    public boolean canRun() {
        if(this.steps >= this.maxSteps)
            return false;
        else if(getPopulationOfWorld() == 0)
            return false;
        else if(getChangesInPastSteps() == 0)
            return false;
        else
            return true;
    }
    
    /**
     * Counts all species
     * @return int - number of instances of species in the world
     */
    public int getPopulationOfWorld() {
        int population = 0;
        for(int i = 0; i < world.getHeight(); i++) {
            for(int j = 0; j < world.getWidth(); j++) {
                Cell cell = world.get(i,j);
                if(cell != null && cell.getAnimal() != null) {
                    population++;
                }
                if(cell != null && cell.getPlant() != null) {
                    population++;
                }
            }
        }
        return population;
    }
    
    /**
     * Counts changes from birth and death lists
     * @return int - number of change in the last 50 steps
     */
    public int getChangesInPastSteps() {
        if(Species.births.size() < 50 && Species.deaths.size() < 50)
            return 1;
        int changes = 0;
        for(int i = Species.births.size()-50; i < Species.births.size(); i++) {
            for(int j = 0; j < Species.births.get(i).size(); j++) {
                changes += Species.births.get(i).get(j);
            }
        }
        for(int i = Species.deaths.size()-50; i < Species.deaths.size(); i++) {
            for(int j = 0; j < Species.deaths.get(i).size(); j++) {
                changes += Species.deaths.get(i).get(j);
            }
        }
        return changes;
    }
}
