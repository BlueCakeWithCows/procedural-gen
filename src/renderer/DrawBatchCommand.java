package renderer;

import java.util.List;

public class DrawBatchCommand implements DrawCommand {
    public final List<DrawCommand> commands;

    public DrawBatchCommand(List<DrawCommand> commands) {this.commands = commands;}
}
