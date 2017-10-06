package biz.rapidfire.rse.subsystem.actions;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import biz.rapidfire.core.handlers.ResetJobHandler;

public class ResetJobAction extends AbstractJobAction {

    private ResetJobHandler handler = new ResetJobHandler();

    public void execute(ExecutionEvent event) throws ExecutionException {

        System.out.println("Calling handler: Resetting Rapid Fire job ...");
        handler.execute(event);
    }

}