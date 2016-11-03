import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class CellTest {
    World world;
    Cell cell;
    Species testAnimal;
    
    /* Called before every test case method. */
    @Before
    public void setUp()
    {
        world = new World(5,5,5);
        //cell = new Cell(world);
        testAnimal = new Carnivore("bear", "b", new ArrayList<String>(), 10.0, 1.0, 10.0, 10.0, 10.0, 10.0, 10.0, 1.0);
    }

    @Test
    public void testGetAdjacent() {
        world.get(3,3).setAnimal((Animal)testAnimal);
        assert world.get(2,2).getAdjacent(1,1).getAnimal() != null : "Get Adjacent Not Working";
        assert world.get(2,2).getAdjacent(2,2) == null : "Get Adjacent 1";
        assert world.get(4,4).getAdjacent(1,1) == null : "Get Adjacent 2";
        assert world.get(2,2).getAdjacent(0,0) == null : "Get Adjacent 3";
    }
}
