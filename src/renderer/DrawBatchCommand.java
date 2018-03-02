package renderer;

import java.util.ArrayList;
import java.util.List;

public class DrawBatchCommand implements DrawCommand {
    public final List<DrawCommand> commands;

    public DrawBatchCommand(List<DrawCommand> commands) {this.commands = commands;}

    public List<DrawCommand> unpack() {
        List<DrawCommand> commands = new ArrayList<>();
        for (DrawCommand cmd : this.commands) {
            if (cmd instanceof DrawBatchCommand) {
                commands.addAll(((DrawBatchCommand) cmd).unpack());
            } else {
                commands.add(cmd);
            }
        }
        return commands;
    }
}
