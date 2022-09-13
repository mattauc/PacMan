/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package ghost;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import processing.core.PApplet;

class GameTest {

    //Testing game collectible activation
    @Test
    public void gameCollectibleTest() throws InterruptedException{
        Ghost.setFrightened(false);
        Ghost.setInvisible(false);

        SuperFruit.setActive(true);
        assertTrue(App.manager.collectibles());

        Soda.setActive(true);
        assertTrue(App.manager.collectibles());

        Ghost.setFrightened(true);
        Ghost.setInvisible(true);

        SuperFruit.setActive(true);
        assertFalse(App.manager.collectibles());

        Soda.setActive(true);
        assertFalse(App.manager.collectibles());

        Ghost.setFrightened(false);
        Ghost.setInvisible(false);
        Thread.sleep(1000);
    }

    //Testing game frameRate conditions and time
    @Test
    public void frameTest() throws InterruptedException {
        App appTest = new App() {
            public void exit() {
                this.dispose();
            }
        };
        PApplet.runSketch(new String[]{"App"}, appTest);
        App.manager.loadGame();
        App.manager.frameCheck(0);
        assertTrue(App.time == 0);

        App.manager.frameCheck(60);
        assertTrue(App.time == 1);
        assertTrue(Ghost.time == 1);
        appTest.exit();
        Thread.sleep(1000);

    }

    //Testing user input response
    @Test
    public void keyTest() throws InterruptedException {
        App appTest = new App() {
            public void exit() {
                this.dispose();
            }
        };
        PApplet.runSketch(new String[]{"App"}, appTest);
        App.manager.loadGame();

        assertFalse(App.manager.keyManager(5));
        assertFalse(App.manager.keyManager(50));
        assertTrue(App.manager.keyManager(37));
        assertTrue(App.manager.keyManager(38));
        assertTrue(App.manager.keyManager(39));
        assertTrue(App.manager.keyManager(40));
        assertTrue(App.manager.keyManager(32));
        appTest.exit();
        Thread.sleep(1000);
    }

    //Testing losing conditions
    @Test
    public void loseConditionTest() throws InterruptedException {
        App appTest = new App() {
            public void exit() {
                this.dispose();
            }
        };
        PApplet.runSketch(new String[]{"App"}, appTest);

        App.manager.loadGame();
        App.manager.resetConditions();
        App.manager.resetConditions();
        App.manager.resetConditions();
        assertTrue(App.manager.gameOver());
        appTest.exit();
        Thread.sleep(1000);
    }

    //Testing winning conditions
    @Test
    public void winConditionTest() throws InterruptedException {
        App appTest = new App() {
            public void exit() {
                this.dispose();
            }
        };
        PApplet.runSketch(new String[]{"App"}, appTest);
        App.manager.loadGame();
        Fruit.resetPop();
        App.manager.winCheck();
        assertTrue(App.manager.gameOver());
        appTest.exit();
        Thread.sleep(1000);
    }

    //Testing debugger and some collision amongst Entitites in gameManager
    @Test
    public void dynamicCollisionTest() throws InterruptedException {
        App appTest = new App() {
            public void exit() {
                this.dispose();
            }
        };
        PApplet.runSketch(new String[]{"App"}, appTest);
        Ghost.setFrightened(false);
        App.manager.loadGame();
        App.manager.dynamicAction();
        assertTrue(App.manager.keyManager(32));
        App.manager.dynamicAction();
        assertTrue(App.manager.keyManager(32));
        appTest.exit();
        Thread.sleep(1000);
    }

    //Testing whether boardFormat correctly handles null files
    @Test 
    public void boardFormatTest() throws InterruptedException {
        BoardFormat bfTest = new BoardFormat(null);
        assertFalse(bfTest.readMap());  
        Thread.sleep(1000);
    }
}