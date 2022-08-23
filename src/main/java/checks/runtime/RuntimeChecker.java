package checks.runtime;

import java.io.File;
import java.io.IOException;

/**
 * This class is responsible for checking the runtime attributes of the application being tested.
 * The application will be run and the exit code checked for any runtime errors.
 *
 * @author Jack Seaton
 * @version 1.0
 * @since 2022-08-23
 **/
public class RuntimeChecker {

    //The relative location of the executable being tested.
    private String pathToExecutable;

    /**
     * The constructor of the class, which when passed in the path to the executable, will save the location for use by it methods.
     *
     * @param pathToExecutable The path to the executable relative to the root directory of the project.
     * @return Nothing.
     **/
    public RuntimeChecker(String pathToExecutable) {
        this.pathToExecutable = pathToExecutable.trim();
    }

    /**
     * This method will check for the existence of the executable.
     * @params None
     * @return boolean Returns whether or not the executable exists.
     **/
    public boolean doesExecutableExist() {
        File executable = new File(pathToExecutable);
        //Will check that it exists and that it is a file.
        if(executable.isFile()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method will run the executable for 3 seconds, if the executable crashes the exitcode is recorded.
     * The results are stored in a helper class RuntimeCheckResult which will be returned from the function.
     * @params None
     * @return boolean Returns true or false based on whether the application crashed in under 3 seconds.
     **/
    public boolean runExecutable() {

        System.out.println("Running executable at " + pathToExecutable);

        boolean success = true;

        try {
            Runtime runtime = Runtime.getRuntime();
            //Run the executable.
            Process process = runtime.exec(pathToExecutable);

            try {
                //Sleep the thread for 3 seconds, to allow the program to execute.
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                //Thread has been interrupted, something has gone wrong.
                success = false;
                //TODO(Jack): Error handling
                e.printStackTrace();
            }

            System.out.println("Closing executable");
            process.destroy();
        } catch (IOException e) {
            //TODO(Jack): Error handling
            e.printStackTrace();
            success = false;
        }

        return success;
    }
}
