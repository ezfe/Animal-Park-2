import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class WorldTest {
    World world;
    
    /* Called before every test case method. */
    @Before
    public void setUp()
    {
        world = new World(5,5,5);
    }

    @Test
    public void testTurn() {
        testActivityCarnivore();
        testActivityHerbivore();
        testActivityOmnivore();
        testActivityFruit();
        testActivityVegetable();
    }
    
    @Test
    public void testRandomAddToWorld() {
        Species testAnimal = new Carnivore("bear", "b", new TreeSet<String>(), 10.0, 1.0, 10.0, 10.0, 10.0, 10.0, 10.0, 1.0, 2.0, 4.0, 15.0);
        assert getPopulation() == 0 : "World not empty";
        world.randomAddToWorld(testAnimal);
        assert getPopulation() == 1 : "World not one";
        world.randomAddToWorld(testAnimal);
        assert getPopulation() == 2 : "World not two";
        
        world = new World(5,5,5);
        Species testPlant = new Fruit("banana", "b", new TreeSet<String>(), 10.0, 1.0, 10.0, 10.0, 10.0, 10.0, 10.0, 1.0, 0.0, 0.0, 0.0);
        assert getPopulation() == 0 : "World not empty";
        world.randomAddToWorld(testPlant);
        assert getPopulation() == 1 : "World not one";
        world.randomAddToWorld(testPlant);
        assert getPopulation() == 2 : "World not two";
    }
    
    public void testActivityCarnivore() {
        world = new World(5,5,5);
        TreeSet<String> energySources = new TreeSet<String>();
        energySources.add("rabbit");
        Species testPrey = new Herbivore("rabbit", "r", new TreeSet<String>(), 10.0, 1.0, 40.0, 30.0, 10.0, 30.0, 10.0, 1.0, 2.0, 4.0, 15.0);
        Species testAnimal = new Carnivore("bear", "b", energySources, 10.0, 1.0, 40.0, 30.0, 10.0, 30.0, 10.0, 1.0, 2.0, 4.0, 15.0);
        world.get(2,2).setAnimal((Animal)testPrey);
        testPrey.setCell(world.get(2,2));
        world.get(1,1).setAnimal((Animal)testAnimal);
        testAnimal.setCell(world.get(1,1));
        world.turn();
        assert getPopulation() == 1 : "Didn't eat animal" + getPopulation();

        world = new World(5,5,5);
        Species testEnergy = new Carnivore("tiger", "t", new TreeSet<String>(), 10.0, 1.0, 40.0, 30.0, 100.0, 30.0, 10.0, 1.0, 2.0, 4.0, 15.0);
        Species testAge = new Carnivore("bear", "b", new TreeSet<String>(), 1.0, 0.25, 40.0, 30.0, 10.0, 30.0, 10.0, 1.0, 2.0, 4.0, 15.0);
        world.get(1,1).setAnimal((Animal)testEnergy);
        testEnergy.setCell(world.get(1,1));
        world.get(2,2).setAnimal((Animal)testAge);
        testAge.setCell(world.get(2,2));
        world.turn();
        assert getPopulation() == 0 : "Didn't die"; //Will fail occasionally depending on the Gaussian distribution

        world = new World(5,5,5);
        Species testBirth = new Carnivore("bear", "b", new TreeSet<String>(), 10.0, 0.25, 5.0, 30.0, 10.0, 30.0, 10.0, 1.0, 2.0, 4.0, 15.0);
        world.get(2,2).setAnimal((Animal)testBirth);
        testBirth.setCell(world.get(2,2));
        world.turn();
        assert getPopulation() == 2 : "Didn't give birth";

        world = new World(5,5,5);
        Species testMove = new Carnivore("bear", "b", new TreeSet<String>(), 10.0, 0.25, 50.0, 30.0, 10.0, 30.0, 10.0, 1.0, 2.0, 4.0, 15.0);
        world.get(2,2).setAnimal((Animal)testMove);
        testMove.setCell(world.get(2,2));
        world.turn();
        assert world.get(2,2).getAnimal() == null : "Didn't move";
    }
    
    public void testActivityHerbivore() {
        world = new World(5,5,5);
        TreeSet<String> energySources = new TreeSet<String>();
        energySources.add("wheat");
        Species testVegetable = new Vegetable("wheat", "w", new TreeSet<String>(), 10.0, 1.0, 10.0, 10.0, 10.0, 10.0, 10.0, 1.0, 0.0, 0.0, 0.0);
        Species testAnimal = new Herbivore("bear", "b", energySources, 10.0, 1.0, 100.0, 100.0, 10.0, 100.0, 10.0, 1.0, 2.0, 4.0, 15.0);
        world.get(2,2).setPlant((Plant)testVegetable);
        testVegetable.setCell(world.get(2,2));
        world.get(2,2).setAnimal((Animal)testAnimal);
        testAnimal.setCell(world.get(2,2));
        world.turn();
        assert getPopulation() == 1 : "Didn't eat animal";

        world = new World(5,5,5);
        Species testEnergy = new Herbivore("tiger", "t", new TreeSet<String>(), 10.0, 1.0, 40.0, 30.0, 100.0, 30.0, 10.0, 1.0, 2.0, 4.0, 15.0);
        Species testAge = new Herbivore("bear", "b", new TreeSet<String>(), 1.0, 0.25, 40.0, 30.0, 10.0, 30.0, 10.0, 1.0, 2.0, 4.0, 15.0);
        world.get(1,1).setAnimal((Animal)testEnergy);
        testEnergy.setCell(world.get(1,1));
        world.get(2,2).setAnimal((Animal)testAge);
        testAge.setCell(world.get(2,2));
        world.turn();
        assert getPopulation() == 0 : "Didn't die"; //Will fail occasionally depending on the Gaussian distribution

        world = new World(5,5,5);
        Species testBirth = new Herbivore("bear", "b", new TreeSet<String>(), 10.0, 0.25, 5.0, 30.0, 10.0, 30.0, 10.0, 1.0, 2.0, 4.0, 15.0);
        world.get(2,2).setAnimal((Animal)testBirth);
        testBirth.setCell(world.get(2,2));
        world.turn();
        assert getPopulation() == 2 : "Didn't give birth";

        world = new World(5,5,5);
        Species testMove = new Herbivore("bear", "b", new TreeSet<String>(), 10.0, 0.25, 50.0, 30.0, 10.0, 30.0, 10.0, 1.0, 2.0, 4.0, 15.0);
        world.get(2,2).setAnimal((Animal)testMove);
        testMove.setCell(world.get(2,2));
        world.turn();
        assert world.get(2,2).getAnimal() == null : "Didn't move";
    }
    
    public void testActivityOmnivore() {
        world = new World(5,5,5);
        TreeSet<String> energySources = new TreeSet<String>();
        energySources.add("wheat");
        Species testVegetable = new Vegetable("wheat", "w", new TreeSet<String>(), 10.0, 1.0, 10.0, 10.0, 10.0, 10.0, 10.0, 1.0, 0.0, 0.0, 0.0);
        Species testAnimal = new Omnivore("bear", "b", energySources, 10.0, 1.0, 100.0, 100.0, 10.0, 100.0, 10.0, 1.0, 2.0, 4.0, 15.0);
        world.get(2,2).setPlant((Plant)testVegetable);
        testVegetable.setCell(world.get(2,2));
        world.get(2,2).setAnimal((Animal)testAnimal);
        testAnimal.setCell(world.get(2,2));
        world.turn();
        assert getPopulation() == 1 : "Didn't eat plant";
        
        world = new World(5,5,5);
        energySources = new TreeSet<String>();
        energySources.add("rabbit");
        Species testPrey = new Herbivore("rabbit", "r", new TreeSet<String>(), 10.0, 1.0, 100.0, 100.0, 10.0, 100.0, 10.0, 1.0, 2.0, 4.0, 15.0);
        testAnimal = new Omnivore("bear", "b", energySources, 10.0, 1.0, 100.0, 100.0, 10.0, 100.0, 10.0, 1.0, 2.0, 4.0, 15.0);
        world.get(2,2).setAnimal((Animal)testPrey);
        testPrey.setCell(world.get(2,2));
        world.get(1,1).setAnimal((Animal)testAnimal);
        testAnimal.setCell(world.get(1,1));
        world.turn();
        assert getPopulation() == 1 : "Didn't eat animal";

        world = new World(5,5,5);
        Species testEnergy = new Omnivore("tiger", "t", new TreeSet<String>(), 10.0, 1.0, 40.0, 30.0, 100.0, 30.0, 10.0, 1.0, 2.0, 4.0, 15.0);
        Species testAge = new Omnivore("bear", "b", new TreeSet<String>(), 1.0, 0.25, 40.0, 30.0, 10.0, 30.0, 10.0, 1.0, 2.0, 4.0, 15.0);
        world.get(1,1).setAnimal((Animal)testEnergy);
        testEnergy.setCell(world.get(1,1));
        world.get(2,2).setAnimal((Animal)testAge);
        testAge.setCell(world.get(2,2));
        world.turn();
        assert getPopulation() == 0 : "Didn't die"; //Will fail occasionally depending on the Gaussian distribution

        world = new World(5,5,5);
        Species testBirth = new Omnivore("bear", "b", new TreeSet<String>(), 10.0, 0.25, 5.0, 30.0, 10.0, 30.0, 10.0, 1.0, 2.0, 4.0, 15.0);
        world.get(2,2).setAnimal((Animal)testBirth);
        testBirth.setCell(world.get(2,2));
        world.turn();
        assert getPopulation() == 2 : "Didn't give birth";

        world = new World(5,5,5);
        Species testMove = new Omnivore("bear", "b", new TreeSet<String>(), 10.0, 0.25, 50.0, 30.0, 10.0, 30.0, 10.0, 1.0, 2.0, 4.0, 15.0);
        world.get(2,2).setAnimal((Animal)testMove);
        testMove.setCell(world.get(2,2));
        world.turn();
        assert world.get(2,2).getAnimal() == null : "Didn't move";
    }
    
    public void testActivityFruit() {
        world = new World(5,5,5);
        TreeSet<String> energySources = new TreeSet<String>();
        energySources.add("light");
        Species testFruit = new Fruit("grape", "g", energySources, 10.0, 1.0, 100.0, 300.0, 0.0, 30.0, 10.0, 1.0, 0.0, 0.0, 0.0);
        world.get(2,2).setPlant((Plant)testFruit);
        testFruit.setCell(world.get(2,2));
        double initEnergy = testFruit.getEnergy();
        world.turn();
        assert testFruit.getEnergy() > initEnergy : "Didn't gain energy";

        world = new World(5,5,5);
        Species testEnergy = new Fruit("grape", "g", new TreeSet<String>(), 10.0, 1.0, 40.0, 30.0, 100.0, 30.0, 10.0, 1.0, 0.0, 0.0, 0.0);
        Species testAge = new Fruit("banana", "b", new TreeSet<String>(), 1.0, 0.25, 40.0, 30.0, 10.0, 30.0, 10.0, 1.0, 0.0, 0.0, 0.0);
        world.get(1,1).setPlant((Plant)testEnergy);
        testEnergy.setCell(world.get(1,1));
        world.get(2,2).setPlant((Plant)testAge);
        testAge.setCell(world.get(2,2));
        world.turn();
        assert getPopulation() == 0 : "Didn't die"; //Will fail occasionally depending on the Gaussian distribution

        world = new World(5,5,5);
        Species testBirth = new Fruit("banana", "b", new TreeSet<String>(), 10.0, 0.25, 5.0, 300.0, 10.0, 300.0, 10.0, 1.0, 0.0, 0.0, 0.0);
        world.get(2,2).setPlant((Plant)testBirth);
        testBirth.setCell(world.get(2,2));
        world.turn();
        assert getPopulation() == 2 : "Didn't give birth" + getPopulation();

        world = new World(5,5,5);
        Species testMove = new Fruit("banana", "b", new TreeSet<String>(), 10.0, 0.25, 50.0, 30.0, 10.0, 30.0, 10.0, 1.0, 0.0, 0.0, 0.0);
        world.get(2,2).setPlant((Plant)testMove);
        testMove.setCell(world.get(2,2));
        world.turn();
        assert world.get(2,2).getPlant() != null : "Did move";
    }
    
    public void testActivityVegetable() {
        world = new World(5,5,5);
        TreeSet<String> energySources = new TreeSet<String>();
        energySources.add("light");
        Species testVegetable = new Vegetable("broccoli", "g", energySources, 10.0, 1.0, 100.0, 300.0, 0.0, 30.0, 10.0, 1.0, 0.0, 0.0, 0.0);
        world.get(2,2).setPlant((Plant)testVegetable);
        testVegetable.setCell(world.get(2,2));
        double initEnergy = testVegetable.getEnergy();
        world.turn();
        assert testVegetable.getEnergy() > initEnergy : "Didn't gain energy";

        world = new World(5,5,5);
        Species testEnergy = new Vegetable("carrot", "c", new TreeSet<String>(), 10.0, 1.0, 40.0, 30.0, 100.0, 30.0, 10.0, 1.0, 0.0, 0.0, 0.0);
        Species testAge = new Vegetable("broccoli", "b", new TreeSet<String>(), 1.0, 0.25, 40.0, 30.0, 10.0, 30.0, 10.0, 1.0, 0.0, 0.0, 0.0);
        world.get(1,1).setPlant((Plant)testEnergy);
        testEnergy.setCell(world.get(1,1));
        world.get(2,2).setPlant((Plant)testAge);
        testAge.setCell(world.get(2,2));
        world.turn();
        assert getPopulation() == 0 : "Didn't die"; //Will fail occasionally depending on the Gaussian distribution

        world = new World(5,5,5);
        Species testBirth = new Vegetable("broccoli", "b", new TreeSet<String>(), 10.0, 0.25, 5.0, 30.0, 10.0, 30.0, 10.0, 1.0, 0.0, 0.0, 0.0);
        world.get(2,2).setPlant((Plant)testBirth);
        testBirth.setCell(world.get(2,2));
        world.turn();
        assert getPopulation() == 2 : "Didn't give birth";

        world = new World(5,5,5);
        Species testMove = new Vegetable("broccoli", "b", new TreeSet<String>(), 10.0, 0.25, 50.0, 30.0, 10.0, 30.0, 10.0, 1.0, 0.0, 0.0, 0.0);
        world.get(2,2).setPlant((Plant)testMove);
        testMove.setCell(world.get(2,2));
        world.turn();
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
