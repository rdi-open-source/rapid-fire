package biz.rapidfire.rse.subsystem.actions;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import biz.rapidfire.core.handlers.ResetJobAfterAbortionHandler;

public class ResetJobAfterAbortionAction extends AbstractJobAction {

    private ResetJobAfterAbortionHandler handler = new ResetJobAfterAbortionHandler();

    public void execute(ExecutionEvent event) throws ExecutionException {

        System.out.println("Calling handler: Resetting Rapid Fire job after abortion ...");
        handler.execute(event);
    }

}