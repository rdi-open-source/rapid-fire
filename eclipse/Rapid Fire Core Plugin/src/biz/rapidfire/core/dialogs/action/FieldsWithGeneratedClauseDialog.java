/*******************************************************************************
 * Copyright (c) 2012-2018 iSphere Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.dialogs.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.jface.dialogs.Size;
import biz.rapidfire.core.jface.dialogs.XDialog;
import biz.rapidfire.core.model.FieldWithGeneratedClause;
import biz.rapidfire.core.model.IRapidFireJobResource;
import biz.rapidfire.core.model.dao.IJDBCConnection;
import biz.rapidfire.core.swt.widgets.WidgetFactory;

public class FieldsWithGeneratedClauseDialog extends XDialog {

    private boolean isContinue;
    private Shell shell;
    private IJDBCConnection dao;
    private IRapidFireJobResource job;
	private TableViewer tableViewerFields;
	private Table tableFields;

    public FieldsWithGeneratedClauseDialog(Shell shell, IJDBCConnection dao, IRapidFireJobResource job) {
        super(shell);

        this.isContinue = false;
        
        this.shell = shell;
        this.dao = dao;
        this.job = job;
        
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(Messages.bindParameters(Messages.DialogTitle_Fields_with_GENERATED_clause_A, job.getName()));
    }

    @Override
    protected Control createDialogArea(Composite parent) {

        Composite container = new Composite(parent, SWT.BORDER);
        container.setLayout(new GridLayout());
        container.setLayoutData(new GridData(GridData.FILL_BOTH));
        container.setBackground(RapidFireCorePlugin.getDefault().getColor(RapidFireCorePlugin.COLOR_DIALOG_MODE_FOREGROUND));

        Label header = new Label(container, SWT.CENTER);
        header.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        header.setText("! ! !   A T T E N T I O N   ! ! !");
        header.setBackground(RapidFireCorePlugin.getDefault().getColor(RapidFireCorePlugin.COLOR_DIALOG_MODE_FOREGROUND));

        WidgetFactory.createLineFiller(container);

        Text textInfoText = WidgetFactory.createMultilineLabel(container, SWT.V_SCROLL);
        textInfoText.setLayoutData(new GridData(GridData.FILL_BOTH));
        textInfoText.setText(loadMessage());
        textInfoText.setBackground(RapidFireCorePlugin.getDefault().getColor(RapidFireCorePlugin.COLOR_DIALOG_MODE_FOREGROUND));
        
		tableViewerFields = new TableViewer(container, SWT.FULL_SELECTION | SWT.BORDER);
		tableViewerFields.setSorter(new SorterFields());
		tableViewerFields.setLabelProvider(new LabelProviderFields());
		tableViewerFields.setContentProvider(new ContentProviderFields());

		tableFields = tableViewerFields.getTable();
		tableFields.setLinesVisible(true);
		tableFields.setHeaderVisible(true);
		final GridData gridDataFields = new GridData(SWT.FILL, SWT.FILL, true, true);
		tableFields.setLayoutData(gridDataFields);

		final TableColumn tableColumnLibrary = new TableColumn(tableFields, SWT.NONE);
		tableColumnLibrary.setWidth(Size.getSize(100));
		tableColumnLibrary.setText(Messages.Label_Library);

		final TableColumn tableColumnFile = new TableColumn(tableFields, SWT.NONE);
		tableColumnFile.setWidth(Size.getSize(100));
		tableColumnFile.setText(Messages.Label_File);

		final TableColumn tableColumnField = new TableColumn(tableFields, SWT.NONE);
		tableColumnField.setWidth(Size.getSize(100));
		tableColumnField.setText(Messages.Label_Field);

		final TableColumn tableColumnText = new TableColumn(tableFields, SWT.NONE);
		tableColumnText.setWidth(Size.getSize(500));
		tableColumnText.setText(Messages.Label_Text);
		
		tableViewerFields.setInput(new Object());
        
        return container;
    }
    
	private class LabelProviderFields extends LabelProvider implements ITableLabelProvider {
		
		public String getColumnText(Object element, int columnIndex) {
			FieldWithGeneratedClause field = (FieldWithGeneratedClause)element;
			if (columnIndex == 0) {
				return field.getLibrary();
			} 
			else if (columnIndex == 1) {
				return field.getFile();
			} 
			else if (columnIndex == 2) {
				return field.getField();
			} 
			else if (columnIndex == 3) {
				return field.getText();
			} 
			return "*UNKNOWN";
		}
	
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}
	}
	
	private class ContentProviderFields implements IStructuredContentProvider {
	
		public Object[] getElements(Object inputElement) {
			return FieldWithGeneratedClause.getFields(shell, dao, job);
		}
	
		public void dispose() {
		}
	
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	class SorterFields extends ViewerSorter {
	
		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
			
			FieldWithGeneratedClause field1 = (FieldWithGeneratedClause)e1;
			FieldWithGeneratedClause field2 = (FieldWithGeneratedClause)e2;
			
			int result = field1.getLibrary().compareTo(field2.getLibrary());
			if (result == 0) {
				result = field1.getFile().compareTo(field2.getFile());
				if (result == 0) {
					result = field1.getField().compareTo(field2.getField());
				}
			}
			
			return result;
		}
	}
    
    private String loadMessage() {

        InputStream in = null;
        StringBuilder buffer = new StringBuilder();

        try {

            in = getClass().getClassLoader().getResourceAsStream("/biz/rapidfire/core/dialogs/action/resources/fields_with_generated_clause.text");
            BufferedReader r = new BufferedReader(new InputStreamReader(in));

            // reads each line
            String line;
            while ((line = r.readLine()) != null) {
                if (line.trim().length() == 0) {
                    buffer.append("\n\n");
                } else {
                    buffer.append(line.trim());
                    buffer.append(" ");
                }
            }
            in.close();

        } catch (IOException e) {
            MessageDialog.openError(getShell(), Messages.E_R_R_O_R, ExceptionHelper.getLocalizedMessage(e));
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }

        return buffer.toString();
    }

    @Override
    protected void okPressed() {

        isContinue = true;

        super.okPressed();
    }

    public boolean isContinue() {
        return isContinue;
    }

    /**
     * Overridden to make this dialog resizable.
     */
    @Override
    protected boolean isResizable() {
        return true;
    }

    /**
     * Overridden to provide a default size to {@link XDialog}.
     */
    @Override
    protected Point getDefaultSize() {
        return getShell().computeSize(Size.getSize(800), SWT.DEFAULT, true);
    }

    /**
     * Overridden to let {@link XDialog} store the state of this dialog in a
     * separate section of the dialog settings file.
     */
    @Override
    protected IDialogSettings getDialogBoundsSettings() {
        return super.getDialogBoundsSettings(RapidFireCorePlugin.getDefault().getDialogSettings());
    }
    
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, Messages.Label_Continue, true);
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.Label_Cancel, false);
	}
	
}
