import java.util.*;
import java.io.*;

/**
 * Top-level representation of a species
 * 
 * @author Ezekiel Elin
 * @author Uknown Author
 * @version 11/02/2016
 */
public abstract class Species {
    //These static variables keep track of all of the species and the births and deaths by turn for all of them
    protected static World staticWorld = new World(0,0,0);
    protected static ArrayList<String> species = new ArrayList<String>();
    protected static ArrayList<ArrayList<Integer>> births = new ArrayList<ArrayList<Integer>>();
    protected static ArrayList<ArrayList<Integer>> deaths = new ArrayList<ArrayList<Integer>>();

    protected Cell cell;
    protected String name;
    protected String symbol;
    protected TreeSet<String> energySources;
    protected double deathMedian;
    protected double deathStd;
    protected double birthEnergy;
    protected double maxEnergy;
    protected double livingEnergy;
    protected double initialEnergy;
    protected double popMedian;
    protected double popStd;

    protected double moveRange;
    protected double detectRange;
    protected double hungerThreshold;
    
    protected double energy;
    protected int age;

    public String getName() {
        return this.name;
    }
    
    public boolean energySourcesContains(String s) {
        return this.energySources.contains(s);
    }
    
    public Species(String n, String sym, TreeSet<String> s, double dm, double ds, double be, double me, double le, double ie, double pm, double ps, double mr, double dr, double hr) {
        this.name = n;
        this.symbol = sym;
        this.energySources = s;
        this.deathMedian = dm;
        this.deathStd = ds;
        this.birthEnergy = be;
        this.maxEnergy = me;
        this.livingEnergy = le;
        this.initialEnergy = ie;
        this.popMedian = pm;
        this.popStd = ps;

        this.energy = ie;
        this.age = 0;

        this.moveRange = mr;
        this.detectRange = dr;
        this.hungerThreshold = hr;
        
        if(!species.contains(n)) {
            species.add(n);
        }
    }

    public abstract void activity();

    public abstract boolean die();

    public abstract boolean birth();

    public abstract boolean eat();

    public abstract boolean move();

    public static void setStaticWorld(World w) {
        staticWorld = w;
    }

    /**
     * Prints births and deaths between turns when the command is c
     */
    public static void printInfo() {
        for(int i = 0; i < species.size(); i++) {
            System.out.println(species.get(i) + " Births: " + births.get(births.size()-1).get(i));
            System.out.println(species.get(i) + " Deaths: " + deaths.get(deaths.size()-1).get(i));
            System.out.println();
        }
    }

    /**
     * Prints population, median energy, and median age when the command is r
     */
    public static void printStatus() {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileWriter(new File("output.csv"), true));
        } catch(Exception e) {System.out.println(e);}
        if(writer != null) {
            writer.print(Simulation.SEED + "," + staticWorld.getSteps());
        }
        for(int i = 0; i < species.size(); i++) {
            int population = 0;
            ArrayList<Double> energies = new ArrayList<Double>();
            ArrayList<Integer> ages = new ArrayList<Integer>();
            for(int j = 0; j < staticWorld.getWidth(); j++) {
                for(int k = 0; k < staticWorld.getHeight(); k++) {
                    Cell cell = staticWorld.get(j,k);
                    if(cell != null && cell.getAnimal() != null && cell.getAnimal().getName() == species.get(i)) {
                        population++;
                        energies.add(cell.getAnimal().getEnergy());
                        ages.add(cell.getAnimal().getAge());
                    } else if(cell != null && cell.getPlant() != null && cell.getPlant().getName() == species.get(i)) {
                        population++;
                        energies.add(cell.getPlant().getEnergy());
                        ages.add(cell.getPlant().getAge());
                    }
                }
            }

            double medEnergy = 0.0;
            Collections.sort(energies);
            if(energies.size() > 0) {
                if(energies.size() % 2 == 0) {
                    medEnergy = (energies.get((int)(energies.size()/2)-1) + energies.get(energies.size()/2)) / 2.0;
                } else {
                    medEnergy = energies.get((int)(energies.size()/2));
                }
            }

            int medAge = 0;
            Collections.sort(ages);
            if(ages.size() > 0) {
                if(ages.size() % 2 == 0) {
                    medAge = (ages.get((int)(ages.size()/2)-1) + ages.get(ages.size()/2)) / 2;
                } else {
                    medAge = ages.get((int)(ages.size()/2));
                }
            }

            System.out.println(species.get(i) + " Population: " + population);
            if(writer != null) {
                writer.print("," + population);
            }
            System.out.println(species.get(i) + " Median Energy: " + medEnergy);
            System.out.println(species.get(i) + " Median Age: " + medAge);
            System.out.println();
        }
        if(writer != null) {
            writer.println();
            writer.flush();
            writer.close();
        }
    }

    /**
     * Prints data every 50 turns and at the end
     * @param integer start - if start is 50, it prints data from the last 50 steps. otherwise, it prints data from the whole sim
     */
    public static void printSummary(int start) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileWriter(new File("output.csv"), true));
        } catch(Exception e) {System.out.println(e);}
        if(writer != null && start == 50) {
            writer.print(Simulation.SEED + "," + staticWorld.getSteps());
        }
        for(int i = 0; i < species.size(); i++) {
            int population = 0;
            for(int j = 0; j < staticWorld.getWidth(); j++) {
                for(int k = 0; k < staticWorld.getHeight(); k++) {
                    Cell cell = staticWorld.get(j,k);
                    if(cell != null && cell.getAnimal() != null && cell.getAnimal().getName() == species.get(i)) {
                        population++;
                    } else if(cell != null && cell.getPlant() != null && cell.getPlant().getName() == species.get(i)) {
                        population++;
                    }
                }
            }
            System.out.println(species.get(i) + " Population: " + population);
            if(writer != null && start == 50) {
                writer.print("," + population);
            }

            int startIndex = 0;
            String message = " since the start of the simulation (" + births.size() + " turns)";
            if(start == 50) {
                startIndex = births.size() - 50;
                message = " in the last 50 steps";
            }

            int birthsSince = 0;
            for(int j = startIndex; j < births.size(); j++) {
                birthsSince += births.get(j).get(i);
            }
            System.out.println(species.get(i) + " Births" + message + ": " + birthsSince);

            int deathsSince = 0;
            for(int j = startIndex; j < deaths.size(); j++) {
                deathsSince += deaths.get(j).get(i);
            }
            System.out.println(species.get(i) + " Deaths" + message + ": " + deathsSince);
            System.out.println();
        }
        System.out.println();
        System.out.println();
        System.out.println();
        if(writer != null && start == 50) {
            writer.println();
            writer.flush();
            writer.close();
        }
    }

    public static ArrayList<ArrayList<Integer>> getBirths() {
        return births; 
    }

    public static ArrayList<ArrayList<Integer>> getDeaths() {
        return deaths; 
    }

    public static void setBirths(ArrayList<ArrayList<Integer>> a) {
        births = a; 
    }

    public static void setDeaths(ArrayList<ArrayList<Integer>> a) {
        deaths = a;
    }

    public static ArrayList<String> getSpecies() {
        return species; 
    }

    public String getRepresentation() {
        return this.symbol;
    };

    public Cell getCell() {
        return this.cell;
    }

    public void setCell(Cell c) {
        this.cell = c;
    }

    public double getEnergy() {
        return this.energy;
    }

    public void setEnergy(double e) {
        this.energy = e;
    }

    public double getMaxEnergy() {
        return this.maxEnergy;
    }

    public int getAge() {
        return this.age;
    }
}
