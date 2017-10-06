package biz.rapidfire.rse.subsystem.actions;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import biz.rapidfire.core.handlers.EndJobHandler;

public class EndJobAction extends AbstractJobAction {

    private EndJobHandler handler = new EndJobHandler();

    public void execute(ExecutionEvent event) throws ExecutionException {

        System.out.println("Calling handler: Ending Rapid Fire job ...");
        handler.execute(event);
    }

}