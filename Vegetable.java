import java.util.*;

/**
 * Vegetable class
 * 
 * @author Ezekiel Elin
 * @author Unknown Author
 * @version 11/02/2016
 */
public class Vegetable extends Plant {
    public Vegetable(String n, String sym, TreeSet<String> s, double dm, double ds, double be, double me, double le, double ie, double pm, double ps, double mr, double dr, double hr) {    
        super(n, sym, s, dm, ds, be, me, le, ie, pm, ps, mr, dr, hr);
    }

    public Vegetable(Species parent) {    
        super(parent.name, parent.symbol, parent.energySources, parent.deathMedian, parent.deathStd, parent.birthEnergy, parent.maxEnergy, parent.livingEnergy, parent.initialEnergy, parent.popMedian, parent.popStd, parent.moveRange, parent.detectRange, parent.hungerThreshold);
    }
}
