package biz.rapidfire.rse.subsystem.actions;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import biz.rapidfire.core.handlers.TestJobHandler;

public class TestJobAction extends AbstractJobAction {

    private TestJobHandler handler = new TestJobHandler();

    @Override
    public void execute(ExecutionEvent event) throws ExecutionException {

        System.out.println("Calling handler: Testing Rapid Fire job ...");
        handler.execute(event);
    }

}