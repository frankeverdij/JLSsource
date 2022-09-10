package jls;

import java.util.TimerTask;


public class SearchTimerTask extends TimerTask
{
    private int eventId;
    private SearchCellArray owner;

    public SearchTimerTask(SearchCellArray owner, int eventId)
    {
        this.eventId = eventId;
        this.owner = owner;
    }

    @Override
    public void run()
    {
        owner.externalAction(eventId);
    }
}