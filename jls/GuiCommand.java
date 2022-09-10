package jls;

public class GuiCommand
{
    // general request for the thread to stop working,
    // return the data for changes and remember what it was previously doing
    // so that it could continue if needed
    public static final int CMD_STOP = 0;
    
    // command to continue work
    public static final int CMD_CONTINUE = 1;
    
    private static final String [] CMD_NAMES = { "CMD_STOP", "CMD_CONTINUE" };
    
    private final int command;
    
    public GuiCommand(int cmd)
    {
        command = cmd;
    }

    public int getCommand()
    {
        return command;
    }
    
    public String getCommandName()
    {
        return CMD_NAMES[command];
    }
}
