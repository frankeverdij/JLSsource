package jls;

public class ThreadMessage
{
// asynchronous requests, search continues running    
    // report of search engine type
    public static final int MSG_ENGINE = 0;
    // request to display a string
    public static final int MSG_STRING = 1;
    // request to display a message box with a message
    public static final int MSG_DIALOG = 2;

// synchronous requests, search is stopped or paused after the command
    // response to STOP command
    public static final int MSG_STOPPED = 3;
    
    // request to save current state and continue
    public static final int MSG_SAVE = 4;
    // request to display the current state and continue
    public static final int MSG_DISPLAY = 5;
    // search finished (or paused)
    public static final int MSG_DONE = 6;
    // search cannot start because of error (immediate return to EDIT mode)
    public static final int MSG_ERROR = 7;
    
    private static final String[] MSG_NAMES = { "MSG_ENGINE", "MSG_STRING", "MSG_DIALOG",
            "MSG_STOPPED", "MSG_SAVE", "MSG_DISPLAY", "MSG_DONE", "MSG_ERROR" };

    public static String getMessageName(int type)
    {
        return MSG_NAMES[type];
    }

    private int message;
    private Object data;

    public ThreadMessage(int message, Object data)
    {
        this.message = message;
        this.data = data;
    }
    
    public int getMessage()
    {
        return message;
    }
    
    public Object getData()
    {
        return data;
    }
    
    public String getMessageName()
    {
        return MSG_NAMES[message];
    }
}
