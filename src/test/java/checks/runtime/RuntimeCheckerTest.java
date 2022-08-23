package checks.runtime;
import static org.junit.Assert.*;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class RuntimeCheckerTest {
    @Test
    public void assertFileExists() {
        //Create file
        File testFile = null;
        try {
            testFile = new File("test.txt");
            testFile.createNewFile();
        } catch (IOException e) {
            System.err.println("assertFileExists - Failed to create file");
            e.printStackTrace();
        }

        //Check function is true
        assertEquals(new RuntimeChecker("test.txt").doesExecutableExist(), true);

        //Delete file
        if (testFile != null) {
            testFile.delete();
        }
    }

    @Test
    public void assertFileDoesNotExist() {
        //Assert file does not exist.
        String fileLocation = "/dummy_file_path/file.exe";
        assertEquals(new RuntimeChecker(fileLocation).doesExecutableExist(), false);
    }
}
