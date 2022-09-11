package jls;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import javax.swing.SwingWorker;


public class SearchThread extends SwingWorker<Object, ThreadMessage>
{
    private ContentPane owner;
    private ArrayBlockingQueue<GuiCommand> inBox;
    private SearchCellArray searchCellArray = null;
    
    public SearchThread(ContentPane pwn, SearchOptions searchOptions)
    {
        owner = pwn;
        inBox = new ArrayBlockingQueue<GuiCommand>(100);
        searchCellArray = new SearchCellArray(this, searchOptions);
    }

    @Override
    public Object doInBackground()
    {
        try
        {
            while (true)
            {
                GuiCommand cmd = getCommand();
                //System.out.println(Thread.currentThread().getName() + " processing " + cmd.getCommandName());
                switch(cmd.getCommand())
                {
                case GuiCommand.CMD_STOP:
                    searchCellArray.stopTimerTasks();
                    publish(ThreadMessage.MSG_STOPPED, searchCellArray);
                    break;
                case GuiCommand.CMD_CONTINUE:
                    try 
                    {
                        int result = searchCellArray.process();
                        //System.out.println(Thread.currentThread().getName() + " process() end");
                        publish(result, searchCellArray);
                    }
                    catch (SearchThreadInterruptedException e)
                    {
                        //System.out.println(Thread.currentThread().getName() + " process() interrupt");
                        //ignore
                    }
                    break;
                }
            }
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            publish(ThreadMessage.MSG_DIALOG, t.toString());
            publish(ThreadMessage.MSG_STRING, "Background thread failed.");
        }
        return null;
    }
    
    // this function is executed asynchronously by the GUI thread
    // after publish() is invoked with a message
    @Override
    protected void process(List<ThreadMessage> resultList)
    {
        for (int i = 0; i < resultList.size(); ++i)
        {
            owner.processSearchThreadMessage(resultList.get(i));
        }
        owner.finishProcessingSearchThreadMessages();
    }
    
    // sends a message from the background thread to the GUI thread
    public void publish(int msgType, Object obj)
    {
        //System.out.println(Thread.currentThread().getName() + " publish " + ThreadMessage.getMessageName(msgType));
        publish(new ThreadMessage(msgType, obj));
    }

    // to be executed by GUI thread
    // leaves messages for the background thread
    public void sendCommand(GuiCommand cmd)
    {
        //System.out.println(Thread.currentThread().getName() + " sendCommand " + cmd.getCommandName());
        while (true)
        {
            try
            {
                inBox.put(cmd);
                searchCellArray.externalAction(SearchCellArray.EXTERNAL_EVENT_GUI_INTERRUPT);
                return;
            }
            catch (InterruptedException e)
            {
                // if interrupted, try again
            }
        }
    }
    
    // to be executed by search thread
    // checks if there is a command in the queue
    // returns the command if it is present
    // returns null if not
    public boolean isCommand()
    {
        return null != inBox.peek();
    }
    
    // check for interrupt is only done during preparations for search
    // when we are already running
    // so any requests to continue should be ignored
    public void checkInterrupt() throws SearchThreadInterruptedException
    {
        GuiCommand cmd = inBox.peek();
        if (cmd != null)
        {
            if (cmd.getCommand() != GuiCommand.CMD_CONTINUE)
            {
                throw(new SearchThreadInterruptedException());
            } else {
                inBox.poll();
            }
        }
    }
    
    // to be executed by search thread when it has nothing to do
    // waits for a command and returns it
    public GuiCommand getCommand()
    {
        while (true)
        {
            try
            {
                return inBox.take();
            }
            catch (InterruptedException e)
            {
                // if interrupted, try again
            }
        }
    }
}
