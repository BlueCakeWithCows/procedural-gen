package byog.levelBuilder;

import java.util.Map;

public interface Generator {

    String getDess();

    World generate(long seed, Player player, Map<String, Object> param);
}
