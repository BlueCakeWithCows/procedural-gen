package levelBuilder;

import java.util.HashMap;

public interface Generator {

    String getDess();

    World generate(long seed, Player player, HashMap<String, Object> param);
}
