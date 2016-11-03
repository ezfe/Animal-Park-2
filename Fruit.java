import java.util.*;

/**
 * Fruit class
 * 
 * @author Ezekiel Elin
 * @author Unknown Author
 * @version 11/02/2016
 */
public class Fruit extends Plant {
    public Fruit(String n, String sym, List<String> s, double dm, double ds, double be, double me, double le, double ie, double pm, double ps) {    
        super(n, sym, s, dm, ds, be, me, le, ie, pm, ps);
    }
    
    public Fruit(Species parent) {    
        super(parent.name, parent.symbol, parent.energySources, parent.deathMedian, parent.deathStd, parent.birthEnergy, parent.maxEnergy, parent.livingEnergy, parent.initialEnergy, parent.popMedian, parent.popStd);
    }
}
