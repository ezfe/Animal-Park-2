import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class FruitTest
{
    World world;
    
    public FruitTest()
    {
    }

    /* Called before every test case method. */
    @Before
    public void setUp()
    {
        world = new World(5,5,5);
    }

    /* Called after every test case method. */
    @After
    public void tearDown()
    {
    }
    
    @Test
    public void testEat() {
        ArrayList<String> energySources = new ArrayList<String>();
        energySources.add("light");
        Species testFruit = new Fruit("grape", "g", energySources, 10.0, 1.0, 40.0, 300.0, 10.0, 30.0, 10.0, 1.0);
        world.get(2,2).setPlant((Plant)testFruit);
        testFruit.setCell(world.get(2,2));
        double initEnergy = testFruit.getEnergy();
        assert testFruit.eat() && testFruit.getEnergy() > initEnergy : "Didn't gain energy";
    }
    
    @Test
    public void testDie() {
        Species testEnergy = new Fruit("grape", "g", new ArrayList<String>(), 10.0, 1.0, 40.0, 30.0, 100.0, 30.0, 10.0, 1.0);
        Species testAge = new Fruit("banana", "b", new ArrayList<String>(), 1.0, 0.25, 40.0, 30.0, 10.0, 30.0, 10.0, 1.0);
        world.get(1,1).setPlant((Plant)testEnergy);
        testEnergy.setCell(world.get(1,1));
        world.get(2,2).setPlant((Plant)testAge);
        testAge.setCell(world.get(2,2));
        assert testEnergy.die() : "Didn't die of energy";
        assert testAge.die() || testAge.die() : "Didn't die of age"; //Will fail occasionally depending on the Gaussian distribution
    }
    
    @Test
    public void testBirth() {
        Species testBirth = new Fruit("banana", "b", new ArrayList<String>(), 10.0, 0.25, 5.0, 30.0, 10.0, 30.0, 10.0, 1.0);
        world.get(2,2).setPlant((Plant)testBirth);
        testBirth.setCell(world.get(2,2));
        assert testBirth.birth() : "Didn't give birth";
    }
    
    @Test
    public void testMove() {
        Species testMove = new Fruit("banana", "b", new ArrayList<String>(), 10.0, 0.25, 50.0, 30.0, 10.0, 30.0, 10.0, 1.0);
        world.get(2,2).setPlant((Plant)testMove);
        testMove.setCell(world.get(2,2));
        assert !testMove.move() : "Did move";
    }
    
    @Test
    public void testActivity() {
        ArrayList<String> energySources = new ArrayList<String>();
        energySources.add("light");
        Species testFruit = new Fruit("grape", "g", energySources, 10.0, 1.0, 100.0, 300.0, 0.0, 30.0, 10.0, 1.0);
        world.get(2,2).setPlant((Plant)testFruit);
        testFruit.setCell(world.get(2,2));
        double initEnergy = testFruit.getEnergy();
        testFruit.activity();
        assert testFruit.getEnergy() > initEnergy : "Didn't gain energy";

        world = new World(5,5,5);
        Species testEnergy = new Fruit("grape", "g", new ArrayList<String>(), 10.0, 1.0, 40.0, 30.0, 100.0, 30.0, 10.0, 1.0);
        Species testAge = new Fruit("banana", "b", new ArrayList<String>(), 1.0, 0.25, 40.0, 30.0, 10.0, 30.0, 10.0, 1.0);
        world.get(1,1).setPlant((Plant)testEnergy);
        testEnergy.setCell(world.get(1,1));
        world.get(2,2).setPlant((Plant)testAge);
        testAge.setCell(world.get(2,2));
        testEnergy.activity();
        testAge.activity();
        assert getPopulation() == 0 : "Didn't die"; //Will fail occasionally depending on the Gaussian distribution

        world = new World(5,5,5);
        Species testBirth = new Fruit("banana", "b", new ArrayList<String>(), 10.0, 0.25, 5.0, 30.0, 10.0, 30.0, 10.0, 1.0);
        world.get(2,2).setPlant((Plant)testBirth);
        testBirth.setCell(world.get(2,2));
        testBirth.activity();
        assert getPopulation() == 2 : "Didn't give birth";

        world = new World(5,5,5);
        Species testMove = new Fruit("banana", "b", new ArrayList<String>(), 10.0, 0.25, 50.0, 30.0, 10.0, 30.0, 10.0, 1.0);
        world.get(2,2).setPlant((Plant)testMove);
        testMove.setCell(world.get(2,2));
        testMove.activity();
        assert world.get(2,2).getPlant() != null : "Did move";
    }
    
    public int getPopulation() {
        int pop = 0;
        for(int i = 0; i < world.getHeight(); i++) {
            for(int j = 0; j < world.getWidth(); j++) {
                if(world.get(i,j).getAnimal() != null) {
                    pop++;
                }
                if(world.get(i,j).getPlant() != null) {
                    pop++;
                }
            }
        }
        return pop;
    }
}