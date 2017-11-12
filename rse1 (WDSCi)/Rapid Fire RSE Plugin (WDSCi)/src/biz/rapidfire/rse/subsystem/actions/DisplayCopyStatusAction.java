package biz.rapidfire.rse.subsystem.actions;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import biz.rapidfire.core.handlers.DisplayCopyStatusHandler;

public class DisplayCopyStatusAction extends AbstractJobAction {

    private DisplayCopyStatusHandler handler = new DisplayCopyStatusHandler();

    @Override
    public void execute(ExecutionEvent event) throws ExecutionException {

        handler.execute(event);
    }

}