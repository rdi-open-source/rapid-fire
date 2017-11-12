package biz.rapidfire.rse.subsystem.actions;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import biz.rapidfire.core.handlers.CopyJobHandler;

public class CopyJobAction extends AbstractJobAction {

    private CopyJobHandler handler = new CopyJobHandler();

    @Override
    public void execute(ExecutionEvent event) throws ExecutionException {

        System.out.println("Calling handler: Copying Rapid Fire job ...");
        handler.execute(event);
    }

}