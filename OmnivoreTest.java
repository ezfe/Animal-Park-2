import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class OmnivoreTest {
    World world;

    /* Called before every test case method. */
    @Before
    public void setUp() {
        world =  new World(5,5,5);
    }

    @Test
    public void testEat() {
        ArrayList<String> energySources = new ArrayList<String>();
        energySources.add("wheat");
        Species testVegetable = new Vegetable("wheat", "w", new ArrayList<String>(), 10.0, 1.0, 10.0, 10.0, 10.0, 10.0, 10.0, 1.0);
        Species testAnimal = new Omnivore("bear", "b", energySources, 10.0, 1.0, 10.0, 10.0, 10.0, 10.0, 10.0, 1.0);
        world.get(2,2).setPlant((Plant)testVegetable);
        testVegetable.setCell(world.get(2,2));
        world.get(2,2).setAnimal((Animal)testAnimal);
        testAnimal.setCell(world.get(2,2));
        assert testAnimal.eat() : "Didn't eat plant";

        world = new World(5,5,5);
        energySources = new ArrayList<String>();
        energySources.add("rabbit");
        Species testPrey = new Herbivore("rabbit", "r", new ArrayList<String>(), 10.0, 1.0, 10.0, 10.0, 10.0, 10.0, 10.0, 1.0);
        testAnimal = new Omnivore("bear", "b", energySources, 10.0, 1.0, 10.0, 10.0, 10.0, 10.0, 10.0, 1.0);
        world.get(1,1).setAnimal((Animal)testPrey);
        testPrey.setCell(world.get(1,1));
        world.get(2,2).setAnimal((Animal)testAnimal);
        testAnimal.setCell(world.get(2,2));
        assert testAnimal.eat() : "Didn't eat animal";
    }

    @Test
    public void testDie() {
        Species testEnergy = new Omnivore("tiger", "t", new ArrayList<String>(), 10.0, 1.0, 40.0, 30.0, 100.0, 30.0, 10.0, 1.0);
        Species testAge = new Omnivore("bear", "b", new ArrayList<String>(), 1.0, 0.25, 40.0, 30.0, 10.0, 30.0, 10.0, 1.0);
        world.get(1,1).setAnimal((Animal)testEnergy);
        testEnergy.setCell(world.get(1,1));
        world.get(2,2).setAnimal((Animal)testAge);
        testAge.setCell(world.get(2,2));
        assert testEnergy.die() : "Didn't die of energy";
        assert testAge.die() || testAge.die() : "Didn't die of age"; //Will fail occasionally depending on the Gaussian distribution
    }

    @Test
    public void testBirth() {
        Species testBirth = new Omnivore("bear", "b", new ArrayList<String>(), 10.0, 0.25, 5.0, 30.0, 10.0, 30.0, 10.0, 1.0);
        world.get(2,2).setAnimal((Animal)testBirth);
        testBirth.setCell(world.get(2,2));
        assert testBirth.birth() : "Didn't give birth";
    }

    @Test
    public void testMove() {
        Species testMove = new Omnivore("bear", "b", new ArrayList<String>(), 10.0, 0.25, 50.0, 30.0, 10.0, 30.0, 10.0, 1.0);
        world.get(2,2).setAnimal((Animal)testMove);
        testMove.setCell(world.get(2,2));
        assert testMove.move() : "Didn't move";
    }

    @Test
    public void testActivity() {
        ArrayList<String> energySources = new ArrayList<String>();
        energySources.add("wheat");
        Species testVegetable = new Vegetable("wheat", "w", new ArrayList<String>(), 10.0, 1.0, 10.0, 10.0, 10.0, 10.0, 10.0, 1.0);
        Species testAnimal = new Omnivore("bear", "b", energySources, 10.0, 1.0, 10.0, 10.0, 10.0, 10.0, 10.0, 1.0);
        world.get(2,2).setPlant((Plant)testVegetable);
        testVegetable.setCell(world.get(2,2));
        world.get(2,2).setAnimal((Animal)testAnimal);
        testAnimal.setCell(world.get(2,2));
        testAnimal.activity();
        assert getPopulation() == 1 : "Didn't eat animal";

        world = new World(5,5,5);
        energySources = new ArrayList<String>();
        energySources.add("rabbit");
        Species testPrey = new Herbivore("rabbit", "r", new ArrayList<String>(), 10.0, 1.0, 10.0, 10.0, 10.0, 10.0, 10.0, 1.0);
        testAnimal = new Omnivore("bear", "b", energySources, 10.0, 1.0, 10.0, 10.0, 10.0, 10.0, 10.0, 1.0);
        world.get(1,1).setAnimal((Animal)testPrey);
        testPrey.setCell(world.get(1,1));
        world.get(2,2).setAnimal((Animal)testAnimal);
        testAnimal.setCell(world.get(2,2));
        testAnimal.activity();
        assert getPopulation() == 1 : "Didn't eat animal";

        world = new World(5,5,5);
        Species testEnergy = new Omnivore("tiger", "t", new ArrayList<String>(), 10.0, 1.0, 40.0, 30.0, 100.0, 30.0, 10.0, 1.0);
        Species testAge = new Omnivore("bear", "b", new ArrayList<String>(), 1.0, 0.25, 40.0, 30.0, 10.0, 30.0, 10.0, 1.0);
        world.get(1,1).setAnimal((Animal)testEnergy);
        testEnergy.setCell(world.get(1,1));
        world.get(2,2).setAnimal((Animal)testAge);
        testAge.setCell(world.get(2,2));
        testEnergy.activity();
        testAge.activity();
        assert getPopulation() == 0 : "Didn't die"; //Will fail occasionally depending on the Gaussian distribution

        world = new World(5,5,5);
        Species testBirth = new Omnivore("bear", "b", new ArrayList<String>(), 10.0, 0.25, 5.0, 30.0, 10.0, 30.0, 10.0, 1.0);
        world.get(2,2).setAnimal((Animal)testBirth);
        testBirth.setCell(world.get(2,2));
        testBirth.activity();
        assert getPopulation() == 2 : "Didn't give birth";

        world = new World(5,5,5);
        Species testMove = new Omnivore("bear", "b", new ArrayList<String>(), 10.0, 0.25, 50.0, 30.0, 10.0, 30.0, 10.0, 1.0);
        world.get(2,2).setAnimal((Animal)testMove);
        testMove.setCell(world.get(2,2));
        testMove.activity();
        assert world.get(2,2).getAnimal() == null : "Didn't move";
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
