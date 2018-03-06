package Core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SaveGame implements Serializable {

    public static boolean saveData(String gameState) {
        try {
            ObjectOutputStream os =
                new ObjectOutputStream(new FileOutputStream(new File("proj2save.txt")));
            os.writeObject(gameState);
            os.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String loadData() {
        try {
            ObjectInputStream oos =
                new ObjectInputStream(new FileInputStream(new File("proj2save.txt")));
            String gameState = (String) oos.readObject();
            oos.close();
            return gameState;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
