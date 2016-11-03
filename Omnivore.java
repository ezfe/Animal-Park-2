import java.util.*;

public class Omnivore extends Animal
{
    public Omnivore(String n, String sym, List<String> s, double dm, double ds, double be, double me, double le, double ie, double pm, double ps) {    
        super(n, sym, s, dm, ds, be, me, le, ie, pm, ps);
    }
    
    public Omnivore(Species parent) {    
        super(parent.name, parent.symbol, parent.energySources, parent.deathMedian, parent.deathStd, parent.birthEnergy, parent.maxEnergy, parent.livingEnergy, parent.initialEnergy, parent.popMedian, parent.popStd);
    }

    /**
     * Calls methods from Animal class to try to eat
     * @return boolean - true if the animal eats, false otherwise
     */
    public boolean eat() {
        //This works really well with Java shortcircuiting
        //If it successfully eats a plant, it won't try to eat an animal because the or statement is already true
        //No matter what happens, the statement returns the correct boolean value
        return this.eatPlant() || this.eatAnimal();
    }
}
