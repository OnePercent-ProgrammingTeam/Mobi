/* 
import graphics.Intro;
import javafx.scene.text.Text;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IntroTest {

    @Test
    public void testCreateText() {
        Intro obj = new Intro(); 
        Text[] result = obj.createText();

        assertNotNull(result);
        assertEquals(7, result.length);
        assertEquals("Mobi", result[0].getText());
        assertEquals("Welcome to Mobi - your Docker Companion! ", result[1].getText());
        assertTrue(result[2].getText().startsWith("You are running Mobi on:"));
        assertEquals("Mobi offers a suite of powerful tools allowing you to:", result[3].getText());
        assertEquals("1.Manage Containers", result[4].getText());
        assertEquals("2.Manage Images", result[5].getText());
        assertEquals("3.Container Analytics", result[6].getText());
    }
}
*/
