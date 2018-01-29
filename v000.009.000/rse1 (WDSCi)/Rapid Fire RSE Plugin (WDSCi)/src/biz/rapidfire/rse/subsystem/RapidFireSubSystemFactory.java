/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rse.subsystem;

import java.util.Vector;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Shell;

import biz.rapidfire.core.subsystem.RapidFireFilter;
import biz.rapidfire.rse.Messages;
import biz.rapidfire.rse.RapidFireRSEPlugin;
import biz.rapidfire.rse.subsystem.actions.job.NewJobFilterPopupMenuAction;

import com.ibm.etools.iseries.core.api.ISeriesConnection;
import com.ibm.etools.iseries.core.api.ISeriesDataElementWrapper;
import com.ibm.etools.systems.core.ui.actions.SystemBaseAction;
import com.ibm.etools.systems.dftsubsystem.impl.DefaultSubSystemFactoryImpl;
import com.ibm.etools.systems.filters.SystemFilter;
import com.ibm.etools.systems.filters.SystemFilterPool;
import com.ibm.etools.systems.filters.SystemFilterPoolManager;
import com.ibm.etools.systems.filters.SystemFilterPoolReference;
import com.ibm.etools.systems.filters.SystemFilterReference;
import com.ibm.etools.systems.filters.SystemFilterStringReference;
import com.ibm.etools.systems.filters.ui.actions.SystemChangeFilterAction;
import com.ibm.etools.systems.filters.ui.actions.SystemNewFilterAction;
import com.ibm.etools.systems.model.SystemConnection;
import com.ibm.etools.systems.subsystems.SubSystem;
import com.ibm.etools.systems.subsystems.SubSystemHelpers;

public class RapidFireSubSystemFactory extends DefaultSubSystemFactoryImpl {

    public static final String ID = "biz.rapidfire.rse.subsystem.RapidFireSubSystemFactory"; //$NON-NLS-1$

    public RapidFireSubSystemFactory() {
        super();
    }

    @Override
    public SubSystem createSubSystemInternal(SystemConnection connection) {
        RapidFireSubSystem subSystem = new RapidFireSubSystem(connection);
        return subSystem;
    }

    @Override
    protected void removeSubSystem(SubSystem subSystem) {
        getSubSystems(false);
        super.removeSubSystem(subSystem);
    }

    @Override
    public String getTranslatedFilterTypeProperty(SystemFilter selectedFilter) {
        return Messages.Rapid_Fire_filter_type;
    }

    @Override
    protected SystemFilterPool createDefaultFilterPool(SystemFilterPoolManager mgr) {

        SystemFilterPool defaultPool = super.createDefaultFilterPool(mgr);
        Vector<String> strings = new Vector<String>();

        RapidFireFilter instanceFilter = RapidFireFilter.getDefaultFilter();
        strings.add(instanceFilter.getFilterString());
        try {
            SystemFilter filter = mgr.createSystemFilter(defaultPool, Messages.My_Rapid_Fire, strings);
            filter.setType(Messages.Rapid_Fire_filter_type);
        } catch (Exception exc) {
        }
        return defaultPool;
    }

    @Override
    public boolean supportsFilters() {
        return true;
    }

    @Override
    public boolean supportsNestedFilters() {
        return true;
    }

    @Override
    public boolean supportsMultipleFilterStrings() {

        // For RDi see:
        // RapidFireSubSystemConfigurationAdapter.getChangeFilterAction()
        return false;
    }

    // protected IAction getNewNestedFilterAction(SystemFilter arg0, Shell
    // shell) {
    //
    // SystemBaseAction action = new SystemBaseAction("Create Foo Object",
    // shell) {
    // @Override
    // public void run() {
    // // TODO Auto-generated method stub
    // SystemFilterReference reference =
    // (SystemFilterReference)getFirstSelection();
    // // System.out.println(reference.getSubSystem().getHostAliasName());
    //
    // super.run();
    // }
    // };
    //
    // return null;
    // };

    @Override
    public IAction[] getFilterActions(SystemFilter filter, Shell shell) {

        Vector<IAction> actions = new Vector<IAction>();

        actions.add(new NewJobFilterPopupMenuAction(shell));

        return actions.toArray(new IAction[actions.size()]);
    }

    /*
     * Start of RDi/WDSCi specific methods.
     */

    @Override
    protected IAction[] getNewFilterPoolFilterActions(SystemFilterPool selectedPool, Shell shell) {

        SystemNewFilterAction filterAction = (SystemNewFilterAction)super.getNewFilterPoolFilterAction(selectedPool, shell);
        filterAction.setWizardPageTitle(Messages.Rapid_Fire_Filter);
        filterAction.setPage1Description(Messages.Create_a_new_filter_to_list_Rapid_Fire_jobs);
        filterAction.setType(Messages.Rapid_Fire_filter_type);
        filterAction.setText(Messages.Add_Rapid_Fire_Filter_dots);
        filterAction.setFilterStringEditPane(new RapidFireInstanceFilterStringEditPane(shell));
        IAction[] actions = new IAction[1];
        actions[0] = filterAction;
        return actions;
    }

    @Override
    protected IAction getChangeFilterAction(SystemFilter selectedFilter, Shell shell) {

        SystemChangeFilterAction action = (SystemChangeFilterAction)super.getChangeFilterAction(selectedFilter, shell);
        action.setDialogTitle("Change filter");
        action.setFilterStringEditPane(new RapidFireInstanceFilterStringEditPane(shell));

        return action;
    }

    @Override
    protected Vector getAdditionalSubSystemActions(SubSystem arg0, Shell arg1) {
        Vector actions = new Vector();
        return actions;
    }

    @Override
    protected Vector getAdditionalFilterActions(final SystemFilter selectedFilter, Shell shell) {
        Vector actions = new Vector();

        SystemBaseAction action = new SystemBaseAction("Create Foo Object", shell) {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                SystemFilterReference reference = (SystemFilterReference)getFirstSelection();
                // System.out.println(reference.getSubSystem().getHostAliasName());
                Object object = reference.getParentSystemFilterReferencePool().getExistingSystemFilterReference(selectedFilter);
                SubSystem subSystem = SubSystemHelpers.getParentSubSystem((SystemFilterReference)object);
                super.run();
            }
        };

        actions.add(action);

        return actions;
    }

    public ISeriesConnection getISeriesConnection(Object element) {
        ISeriesConnection iseriesConnection = null;
        // if ((element instanceof DataElement))
        // {
        // iseriesConnection =
        // ISeriesConnection.getConnection(ISeriesDataElementUtil.getFileSubSystem((DataElement)element).getSystemConnection());
        // }
        // else
        if ((element instanceof ISeriesDataElementWrapper)) {
            iseriesConnection = ((ISeriesDataElementWrapper)element).getISeriesConnection();
        } else if ((element instanceof SubSystem)) {
            iseriesConnection = ISeriesConnection.getConnection(((SubSystem)element).getSystemConnection());
        } else if ((element instanceof SystemFilterPoolReference)) {
            SubSystem ss = SubSystemHelpers.getParentSubSystem((SystemFilterPoolReference)element);
            iseriesConnection = ISeriesConnection.getConnection(ss.getSystemConnection());
        } else if ((element instanceof SystemFilterReference)) {
            SubSystem ss = SubSystemHelpers.getParentSubSystem((SystemFilterReference)element);
            iseriesConnection = ISeriesConnection.getConnection(ss.getSystemConnection());
        } else if ((element instanceof SystemFilterStringReference)) {
            SubSystem ss = SubSystemHelpers.getParentSubSystem((SystemFilterStringReference)element);
            iseriesConnection = ISeriesConnection.getConnection(ss.getSystemConnection());
        } else if ((element instanceof SystemConnection)) {
            iseriesConnection = ISeriesConnection.getConnection((SystemConnection)element);
        }
        return iseriesConnection;
    }

    @Override
    public ImageDescriptor getSystemFilterImage(SystemFilter filter) {
        return RapidFireRSEPlugin.getDefault().getImageRegistry().getDescriptor(RapidFireRSEPlugin.IMAGE_RAPID_FIRE_FILTER);
    }

}