import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class SimulationTest {
    Simulation sim;
    
    /* Called before every test case method. */
    @Before
    public void setUp() {
        sim = new Simulation("config.txt", 10);
    }
    
    @Test
    public void testFileInit() {
        Species.setBirths(new ArrayList<ArrayList<Integer>>());
        Species.setDeaths(new ArrayList<ArrayList<Integer>>());
        sim = new Simulation("test.txt", 10);
        sim.run();
    }
    
    @Test
    public void testCanRun() {
        sim.initWorld();
        World world = new World(5,5,5);
        sim.setWorld(world);
        assert !sim.canRun() : "Didn't stop due to population";
        
        sim = new Simulation("config.txt", 0);
        assert !sim.canRun() : "Didn't stop due to steps";
        
        sim = new Simulation("config.txt", 100);
        sim.initWorld();
        Species testPlant = new Vegetable("wheat","w",new TreeSet<String>(),1000.0,1.0,100.0,90.0,1.0,50.0,1.0,0.0, 0.0, 0.0, 0.0);
        world.get(2,2).setPlant((Plant)testPlant);
        testPlant.setCell(world.get(2,2));
        sim.setWorld(world);
        Species.setBirths(new ArrayList<ArrayList<Integer>>());
        Species.setDeaths(new ArrayList<ArrayList<Integer>>());
        for(int i = 0; i < 50; i++) {
            world.turn();
        }
        assert !sim.canRun() : "Didn't stop due to lack of change";
    }
    
    @Test
    public void testGetPop() {
        sim.initWorld();
        World world = new World(5,5,5);
        sim.setWorld(world);
        assert sim.getPopulationOfWorld() == 0 : "Pop 0";
        Species testPlant = new Vegetable("wheat","w",new TreeSet<String>(),1000.0,1.0,100.0,90.0,1.0,50.0,1.0,0.0, 0.0, 0.0, 0.0);
        world.get(2,2).setPlant((Plant)testPlant);
        testPlant.setCell(world.get(2,2));
        assert sim.getPopulationOfWorld() == 1 : "Pop 1";
        Species testAnimal = new Carnivore("bear","b",new TreeSet<String>(),1000.0,1.0,100.0,90.0,1.0,50.0,1.0,0.0, 0.0, 0.0, 0.0);
        world.get(3,3).setAnimal((Animal)testAnimal);
        testAnimal.setCell(world.get(3,3));
        assert sim.getPopulationOfWorld() == 2 : "Pop 2";
    }
    
    @Test
    public void testChanges() {
        sim.initWorld();
        assert sim.getChangesInPastSteps() == 1 : "Init";
        World world = new World(5,5,5);
        sim.setWorld(world);
        sim = new Simulation("config.txt", 100);
        Species testPlant = new Vegetable("wheat","w",new TreeSet<String>(),100.0,1.0,10.0,90.0,1.0,50.0,1.0,0.0, 0.0, 0.0, 0.0);
        world.get(2,2).setPlant((Plant)testPlant);
        testPlant.setCell(world.get(2,2));
        for(int i = 0; i < 50; i++) {
            world.turn();
        }
        assert sim.getChangesInPastSteps() > 0 : "50 steps";
    }
}
