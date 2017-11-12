package biz.rapidfire.rse.subsystem.actions;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import biz.rapidfire.core.handlers.ChangeJobHandler;

public class ChangeJobAction extends AbstractJobAction {

    private ChangeJobHandler handler = new ChangeJobHandler();

    public void execute(ExecutionEvent event) throws ExecutionException {

        handler.execute(event);
    }

}