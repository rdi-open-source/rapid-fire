package biz.rapidfire.rse.subsystem.actions;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import biz.rapidfire.core.handlers.DisplayJobHandler;

public class DisplayJobAction extends AbstractJobAction {

    private DisplayJobHandler handler = new DisplayJobHandler();

    @Override
    public void execute(ExecutionEvent event) throws ExecutionException {

        System.out.println("Calling handler: Displaying Rapid Fire job ...");
        handler.execute(event);
    }

}