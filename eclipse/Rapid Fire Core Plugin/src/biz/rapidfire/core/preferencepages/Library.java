/*******************************************************************************
 * Copyright (c) 2017-2021 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.preferencepages;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import com.ibm.as400.access.AS400;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.handlers.install.TransferRapidFireLibraryHandler;
import biz.rapidfire.core.helpers.ExceptionHelper;
import biz.rapidfire.core.helpers.IntHelper;
import biz.rapidfire.core.helpers.RapidFireHelper;
import biz.rapidfire.core.helpers.StringHelper;
import biz.rapidfire.core.preferences.Preferences;
import biz.rapidfire.core.swt.widgets.ISystemHostCombo;
import biz.rapidfire.core.swt.widgets.WidgetFactory;
import biz.rapidfire.core.validators.Validator;
import biz.rapidfire.rsebase.helpers.SystemConnectionHelper;

public class Library extends PreferencePage implements IWorkbenchPreferencePage {

    private String rapidFireLibrary;
    private Validator validatorLibrary;

    private String aspGroup;
    private Validator validatorASPGroup;

    private ISystemHostCombo comboConnection;
    private Text textFtpPortNumber;
    private Text textProductLibrary;
    private Combo comboASPGroup;
    private Label textProductLibraryVersion;
    private Button buttonUpdateProductLibraryVersion;
    private Button buttonTransfer;

    private boolean enableUpdateProductLibraryVersion;

    public Library() {
        super();

        setPreferenceStore(RapidFireCorePlugin.getDefault().getPreferenceStore());
        getPreferenceStore();

        this.enableUpdateProductLibraryVersion = false;
        setControlEnablement();
    }

    @Override
    public Control createContents(Composite parent) {

        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(new GridLayout(3, false));

        comboConnection = WidgetFactory.createSystemHostCombo(container, SWT.NONE);
        comboConnection.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false, 3, 1));
        comboConnection.setToolTipText(Messages.Tooltip_Connection_name);
        comboConnection.getCombo().setToolTipText(Messages.Tooltip_Connection_name);
        comboConnection.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent event) {
                setErrorMessage(null);
                if (!isSlowConnection()) {
                    updateProductLibraryVersion();
                } else {
                    clearProductLibraryVersion();
                }
            }

            public void widgetDefaultSelected(SelectionEvent event) {
                widgetSelected(event);
            }
        });

        WidgetFactory.createLabel(container, Messages.Label_FTP_port_number_colon, Messages.Tooltip_FTP_port_number);

        textFtpPortNumber = WidgetFactory.createIntegerText(container);
        textFtpPortNumber.setToolTipText(Messages.Tooltip_FTP_port_number);
        textFtpPortNumber.setTextLimit(5);
        textFtpPortNumber.setLayoutData(createTextLayoutData());

        WidgetFactory.createLabel(container, Messages.Label_Rapid_Fire_library_colon, Messages.Tooltip_Rapid_Fire_library);

        textProductLibrary = WidgetFactory.createNameText(container);
        textProductLibrary.setToolTipText(Messages.Tooltip_Rapid_Fire_library);
        textProductLibrary.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                rapidFireLibrary = textProductLibrary.getText().toUpperCase().trim();
                if (rapidFireLibrary.equals("") || !validatorLibrary.validate(rapidFireLibrary)) {
                    setErrorMessage(Messages.The_name_of_the_Rapid_Fire_library_is_invalid);
                    setValid(false);
                } else {
                    setErrorMessage(null);
                    setValid(true);
                }
            }
        });
        textProductLibrary.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent arg0) {
                if (!isSlowConnection()) {
                    updateProductLibraryVersion();
                } else {
                    clearProductLibraryVersion();
                }
            }
        });
        textProductLibrary.setLayoutData(createTextLayoutData());
        textProductLibrary.setTextLimit(10);

        validatorLibrary = Validator.getLibraryNameInstance();

        WidgetFactory.createLabel(container, Messages.Label_ASP_group_colon, Messages.Tooltip_ASP_group);

        comboASPGroup = WidgetFactory.createNameCombo(container);
        comboASPGroup.setToolTipText(Messages.Tooltip_ASP_group);
        comboASPGroup.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent arg0) {
                aspGroup = comboASPGroup.getText().toUpperCase().trim();
                if (aspGroup.equals("") || !validatorASPGroup.validate(aspGroup)) {
                    setErrorMessage(Messages.The_name_of_the_asp_group_is_invalid);
                    setValid(false);
                } else {
                    setErrorMessage(null);
                    setValid(true);
                    if (!isSlowConnection()) {
                        updateProductLibraryVersion();
                    } else {
                        clearProductLibraryVersion();
                    }
                }
            }
        });
        comboASPGroup.setLayoutData(createTextLayoutData());
        comboASPGroup.setTextLimit(10);
        comboASPGroup.add("*NONE");

        validatorASPGroup = Validator.getNameInstance();
        validatorASPGroup.addSpecialValue("*NONE");

        WidgetFactory.createLabel(container, Messages.Label_Version_colon, Messages.Tooltip_Version);

        textProductLibraryVersion = new Label(container, SWT.NONE);
        textProductLibraryVersion.setToolTipText(Messages.Tooltip_Version);
        textProductLibraryVersion.setLayoutData(createTextLayoutData(1));

        buttonUpdateProductLibraryVersion = WidgetFactory.createPushButton(container);
        buttonUpdateProductLibraryVersion.setImage(RapidFireCorePlugin.getDefault().getImageRegistry().get(RapidFireCorePlugin.IMAGE_REFRESH));
        buttonUpdateProductLibraryVersion.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent arg0) {
                updateProductLibraryVersion();
            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
            }
        });

        buttonTransfer = WidgetFactory.createPushButton(container);
        buttonTransfer.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                String connectionName = comboConnection.getConnectionName();
                int ftpPort = IntHelper.tryParseInt(textFtpPortNumber.getText(), Preferences.getInstance().getDefaultFtpPortNumber());
                TransferRapidFireLibraryHandler handler = new TransferRapidFireLibraryHandler(connectionName, ftpPort, rapidFireLibrary, aspGroup);
                try {
                    handler.execute(null);
                } catch (Throwable e) {
                    RapidFireCorePlugin.logError("Failed to transfer the Rapid Fire library.", e); //$NON-NLS-1$
                }
            }
        });
        buttonTransfer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
        buttonTransfer.setText(Messages.ActionLabel_Transfer_Rapid_Fire_library);
        buttonTransfer.setToolTipText(Messages.ActionTooltip_Transfer_Rapid_Fire_library);

        createSectionLabelDecorations(container);

        setScreenToValues();

        return container;
    }

    private void createSectionLabelDecorations(Composite parent) {

        WidgetFactory.createLineFiller(parent);

        GridLayout layout = (GridLayout)parent.getLayout();

        Group group = new Group(parent, SWT.NONE);
        group.setLayout(new GridLayout(3, false));
        group.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, layout.numColumns, 1));
        group.setText(Messages.Label_Uninstall_library);

        Link lnkHelp = new Link(group, SWT.NONE);
        lnkHelp.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1));
        lnkHelp.setText(Messages.bindParameters(Messages.Label_Uninstal_library_instructions, "<a>", "</a>")); //$NON-NLS-1$ //$NON-NLS-2$
        lnkHelp.pack();
        lnkHelp.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                PlatformUI.getWorkbench().getHelpSystem().displayHelpResource("/biz.rapidfire.help/html/uninstall/uninstall_library.html"); //$NON-NLS-1$
            }
        });
    }

    private boolean isSlowConnection() {
        return Preferences.getInstance().isSlowConnection();
    }

    @Override
    protected void performApply() {
        setStoreToValues();
        super.performApply();
    }

    @Override
    protected void performDefaults() {
        setScreenToDefaultValues();
        super.performDefaults();
    }

    @Override
    public boolean performOk() {
        setStoreToValues();
        return super.performOk();
    }

    private void setControlEnablement() {

        if (buttonUpdateProductLibraryVersion != null) {
            if (!enableUpdateProductLibraryVersion) {
                buttonUpdateProductLibraryVersion.setEnabled(false);
            } else {
                buttonUpdateProductLibraryVersion.setEnabled(true);
            }
        }
    }

    private void setStoreToValues() {

        Preferences.getInstance().setRapidFireLibrary(rapidFireLibrary);
        Preferences.getInstance().setConnectionName(comboConnection.getConnectionName());
        Preferences.getInstance()
            .setFtpPortNumber(IntHelper.tryParseInt(textFtpPortNumber.getText(), Preferences.getInstance().getDefaultFtpPortNumber()));
        Preferences.getInstance().setASPGroup(aspGroup);

    }

    private void setScreenToValues() {

        rapidFireLibrary = Preferences.getInstance().getRapidFireLibrary();
        String connectionName = Preferences.getInstance().getHostName();
        if (!StringHelper.isNullOrEmpty(connectionName)) {
            if (!comboConnection.selectConnection(connectionName)) {
                setErrorMessage(Messages.bindParameters(Messages.Connection_A_not_found, connectionName));
            }
        }
        textFtpPortNumber.setText(Integer.toString(Preferences.getInstance().getFtpPortNumber()));
        aspGroup = Preferences.getInstance().getASPGroup();

        setScreenValues();

        enableUpdateProductLibraryVersion = true;
        setControlEnablement();
    }

    private void setScreenToDefaultValues() {

        rapidFireLibrary = Preferences.getInstance().getDefaultRapidFireLibrary();
        String connectionName = Preferences.getInstance().getDefaultHostName();
        if (!comboConnection.selectConnection(connectionName)) {
            setErrorMessage(Messages.bindParameters(Messages.Connection_A_not_found, connectionName));
        }
        textFtpPortNumber.setText(Integer.toString(Preferences.getInstance().getDefaultFtpPortNumber()));
        aspGroup = Preferences.getInstance().getDefaultASPGroup();

        setScreenValues();
    }

    private void setScreenValues() {

        textProductLibrary.setText(rapidFireLibrary);
        comboASPGroup.setText(aspGroup);

    }

    public void init(IWorkbench workbench) {
    }

    private GridData createLabelLayoutData() {
        return new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
    }

    private GridData createTextLayoutData() {
        return createTextLayoutData(2);
    }

    private GridData createTextLayoutData(int horizontalSpan) {
        return new GridData(SWT.FILL, SWT.CENTER, true, false, horizontalSpan, 1);
    }

    private void clearProductLibraryVersion() {
        textProductLibraryVersion.setText(""); //$NON-NLS-1$
    }

    private void updateProductLibraryVersion() {
        String text = getProductLibraryVersion(comboConnection.getHostName(), textProductLibrary.getText());
        if (text == null) {
            return;
        }

        textProductLibraryVersion.setText(text);
    }

    private String getProductLibraryVersion(String hostName, String library) {

        if (!enableUpdateProductLibraryVersion) {
            return ""; //$NON-NLS-1$
        }

        if (StringHelper.isNullOrEmpty(hostName) || StringHelper.isNullOrEmpty(library)) {
            return Messages.Enter_a_host_name;
        }

        String version;

        try {

            AS400 as400 = SystemConnectionHelper.findSystem(hostName);
            if (as400 == null) {
                return Messages.bind(Messages.Host_A_not_found_in_configured_RSE_connections, hostName);
            }

            /*
             * From here on start updating library version automatically.
             */

            version = RapidFireHelper.getRapidFireLibraryVersion(as400, library);
            if (version == null) {
                return Messages.bindParameters(Messages.Rapid_Fire_version_information_not_found_in_library_A, library);
            }

            String buildDate = RapidFireHelper.getRapidFireLibraryBuildDate(as400, library);
            if (StringHelper.isNullOrEmpty(buildDate)) {
                return version;
            }

            DateFormat dateFormatter = Preferences.getInstance().getDateFormatter();
            DateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd"); //$NON-NLS-1$
            version = version + " - " + dateFormatter.format(dateParser.parse(buildDate)); //$NON-NLS-1$
            return version;

        } catch (Throwable e) {
            return Messages.bindParameters(Messages.Could_not_rereieve_Rapid_Fire_version_information_due_to_backend_error_A,
                ExceptionHelper.getLocalizedMessage(e));
        }
    }
}