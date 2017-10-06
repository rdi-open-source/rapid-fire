package biz.rapidfire.rse.subsystem.actions;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import biz.rapidfire.core.handlers.DeleteJobHandler;

public class DeleteJobAction extends AbstractJobAction {

    private DeleteJobHandler handler = new DeleteJobHandler();

    public void execute(ExecutionEvent event) throws ExecutionException {

        System.out.println("Calling handler: Deleting Rapid Fire job ...");
        handler.execute(event);
    }

}