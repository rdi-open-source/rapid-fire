package biz.rapidfire.rse.subsystem.actions;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import biz.rapidfire.core.handlers.StartJobHandler;

public class StartJobAction extends AbstractJobAction {

    private StartJobHandler handler = new StartJobHandler();

    @Override
    public void execute(ExecutionEvent event) throws ExecutionException {

        System.out.println("Calling handler: Starting Rapid Fire job ...");
        handler.execute(event);
    }

}