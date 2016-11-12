import java.util.ArrayList;
/**
 * Cell/tile in the world
 * 
 * @author Ezekiel Elin
 * @author Unknown Author
 * @version 11/02/2016
 */
public class Cell {

    private World world;
    private Animal animal;
    private Plant plant;
    private boolean mountain = false;

    public boolean flag = false;

    private Point location = null;

    public Cell(World w) {
        this.world = w;
    }

    public Cell(World w, boolean mountain) {
        this.world = w;
        this.mountain = mountain;
    }

    public Cell(Animal a) {
        this.animal = a;
    }

    public Cell(Plant p) {
        this.plant = p;
    }

    public Cell(Animal a, Plant p) {
        this.animal = a;
        this.plant = p;
    }

    public void setLocation(Point p) {
        this.location = p;
    }

    public Point getLocation() {
        if (this.location != null) return this.location;

        for(int i = 0; i < this.world.getWidth(); i++) {
            for(int j = 0; j < this.world.getHeight(); j++) {
                try {
                    if (this.world.get(i, j) == this) {
                        Point p = new Point(i, j);
                        this.setLocation(p);
                        return p;
                    }
                } catch(Exception e) {}
            }
        }
        return null;
    }

    /**
     * Gets bordering cell from given relative position
     * @param int r - r is the horizontal position relative to the current cell, int c - c is the vertical position relative to the current cell
     * @return Cell - a cell that is in the given position relative to the current cell, null if not found or non existent
     */
    public Cell getAdjacent(int r, int c) {
        if((r < -1 || r > 1) || (c < -1 || c > 1) || (r == 0 && c == 0)) return null;
        Point p = this.getLocation();
        try {
            return this.world.get(p.x+r,p.y+c);
        } catch(Exception e) {    
            return null;
        }
    }

    public Animal getAnimal() {
        return this.animal;
    }

    public boolean setAnimal(Animal a) {
        if (this.mountain) {
            return false;
        }
        this.animal = a;
        return true;
    }

    public Plant getPlant() {
        return this.plant;
    }

    public boolean setPlant(Plant p) {
        if (this.mountain) {
            return false;
        }
        this.plant = p;
        return true;
    }

    public World getWorld() {
        return this.world;
    }

    public boolean isMountain() {
        return this.mountain;
    }

    public void setMountain(boolean b) {
        this.mountain = b;
    }

    /**
     * Fetch all cells within a certain range
     * @param int range to fetch
     * @param boolean whether animals obstruct
     */
    public ArrayList<Cell> getLOSCells(int range, boolean animalsObstruct) {
        range += 1;
        ArrayList<Cell> valids = new ArrayList<Cell>();
        Point current = this.getLocation();
        for(int i = current.x - range + 1; i < current.x + range; i++) {
            if (i >= 0 && i < this.world.getWidth()) {
                for(int j = current.y - range + 1; j < current.y + range; j++) {
                    if (j >= 0 && j < this.world.getHeight()) {
                        Point p = new Point(i, j);

                        /* Line Algorithm */

                        double x1 = (double)current.x;
                        double x2 = (double)p.x;
                        double y1 = (double)current.y;
                        double y2 = (double)p.y;

                        boolean s = false;

                        if (Math.abs(x1 - x2) < Math.abs(y1 - y2)) {
                            /* Reverse the line */
                            double tx1 = x1;
                            x1 = y1;
                            y1 = tx1;

                            double tx2 = x2;
                            x2 = y2;
                            y2 = tx2;

                            s = true;
                        }

                        if (x1 > x2) {
                            double tx1 = x1;
                            x1 = x2;
                            x2 = tx1;

                            double ty1 = y1;
                            y1 = y2;
                            y2 = ty1;
                        }

                        Cell previous = null;
                        boolean success = true;
                        for(double x = x1; x <= x2; x += 1) {
                            double t = (x - x1) / (x2 - x1);
                            double y = y1 * (1.0 - t) + y2 * t;

                            Cell currC = null;
                            if (s) {
                                currC = world.get((int)y, (int)x);
                            } else {
                                currC = world.get((int)x, (int)y);
                            }

                            if (currC == previous) {
                                continue;
                            }

                            if (currC.isMountain() && (!animalsObstruct || currC.getAnimal() == null)) {
                                success = false;
                            } else if (previous != null) {
                                /* check there's a path through mountain range */
                                Point currCLoc = currC.getLocation();
                                Point prevLoc = previous.getLocation();
                                try {
                                    if (prevLoc.x == currCLoc.x - 1 && prevLoc.y == currCLoc.y - 1) {
                                        if (world.get(currCLoc.x, currCLoc.y - 1).isMountain() && world.get(currCLoc.x - 1, currCLoc.y).isMountain()) {
                                            success = false;
                                        }
                                        if (animalsObstruct && world.get(currCLoc.x, currCLoc.y - 1).getAnimal() == null && world.get(currCLoc.x - 1, currCLoc.y).getAnimal() == null) {
                                            success = false;
                                        }
                                    }
                                    if (prevLoc.x == currCLoc.x - 1 && prevLoc.y == currCLoc.y + 1) {
                                        if (world.get(currCLoc.x, currCLoc.y + 1).isMountain() && world.get(currCLoc.x - 1, currCLoc.y).isMountain()) {
                                            success = false;
                                        }
                                        if (animalsObstruct && world.get(currCLoc.x, currCLoc.y + 1).getAnimal() == null && world.get(currCLoc.x - 1, currCLoc.y).getAnimal() == null) {
                                            success = false;
                                        }
                                    }
                                    if (prevLoc.x == currCLoc.x + 1 && prevLoc.y == currCLoc.y - 1) {
                                        if (world.get(currCLoc.x, currCLoc.y - 1).isMountain() && world.get(currCLoc.x + 1, currCLoc.y).isMountain()) {
                                            success = false;
                                        }
                                        if (animalsObstruct && world.get(currCLoc.x, currCLoc.y - 1).getAnimal() == null && world.get(currCLoc.x + 1, currCLoc.y).getAnimal() == null) {
                                            success = false;
                                        }
                                    }
                                    if (prevLoc.x == currCLoc.x + 1 && prevLoc.y == currCLoc.y + 1) {
                                        if (world.get(currCLoc.x, currCLoc.y + 1).isMountain() && world.get(currCLoc.x + 1, currCLoc.y).isMountain()) {
                                            success = false;
                                        }
                                        if (animalsObstruct && world.get(currCLoc.x, currCLoc.y + 1).getAnimal() == null && world.get(currCLoc.x + 1, currCLoc.y).getAnimal() == null) {
                                            success = false;
                                        }
                                    }
                                } catch(Exception e) {}
                            }

                            previous = currC;
                        }

                        if (success) {
                            valids.add(world.get(p.x, p.y));
                        }

                        /* End Line Algorithm */

                    }
                }
            }
        }
        return valids;
    }
}
