package biz.rapidfire.core.maintenance.wizard;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.helpers.StringHelper;
import biz.rapidfire.core.swt.widgets.WidgetFactory;
import biz.rapidfire.core.validators.Validator;
import biz.rapidfire.rsebase.swt.widgets.SystemHostCombo;

public class DataLibraryPage extends AbstractWizardPage {

    public static final String NAME = "DATA_LIBRARY_PAGE"; //$NON-NLS-1$

    private SystemHostCombo comboConnection;
    private Text textDataLibrary;

    private String connectionName;
    private String dataLibraryName;

    private Validator libraryValidator;

    public DataLibraryPage() {
        super(NAME);

        this.libraryValidator = Validator.getLibraryNameInstance();

        setTitle(Messages.Wizard_Page_Data_Library);
        setDescription(Messages.Wizard_Page_Data_Library_description);
    }

    @Override
    public void setFocus() {
        textDataLibrary.setFocus();
    }

    public String getConnectionName() {
        return connectionName;
    }

    public void setConnectionName(String connectionName) {
        this.connectionName = connectionName;
    }

    public String getDataLibraryName() {
        return dataLibraryName;
    }

    public void setDataLibraryName(String dataLibraryName) {
        this.dataLibraryName = dataLibraryName;
    }

    @Override
    public void createContent(Composite parent) {

        comboConnection = WidgetFactory.createSystemHostCombo(parent, SWT.NONE);
        comboConnection.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false, 2, 1));
        comboConnection.setToolTipText(Messages.Tooltip_Connection_name);
        comboConnection.getCombo().setToolTipText(Messages.Tooltip_Connection_name);

        WidgetFactory.createLabel(parent, Messages.Label_Rapid_Fire_library_colon, Messages.Tooltip_Rapid_Fire_library);

        textDataLibrary = WidgetFactory.createNameText(parent);
        textDataLibrary.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
        textDataLibrary.setToolTipText(Messages.Tooltip_Job);
    }

    @Override
    protected void setInputData() {

        if (connectionName != null) {
            comboConnection.selectConnection(connectionName);
        } else {
            comboConnection.selectConnection(getPreferences().getWizardConnection());
        }

        if (dataLibraryName != null) {
            textDataLibrary.setText(dataLibraryName);
        } else {
            textDataLibrary.setText(getPreferences().getWizardRapidFireLibrary());
        }
    }

    @Override
    protected void addControlListeners() {

        comboConnection.addSelectionListener(this);
        textDataLibrary.addModifyListener(this);
    }

    @Override
    protected void updatePageComplete(Object source) {

        String message = null;

        if (StringHelper.isNullOrEmpty(comboConnection.getConnectionName())) {
            // comboConnection.getCombo().setFocus();
            message = Messages.Connection_is_missing;
        } else if (StringHelper.isNullOrEmpty(textDataLibrary.getText())) {
            // textDataLibrary.setFocus();
            message = Messages.The_Rapid_Fire_product_library_name_is_missing;
        } else if (!libraryValidator.validate(textDataLibrary.getText())) {
            message = Messages.bindParameters(Messages.Library_name_A_is_not_valid, textDataLibrary.getText());
        }

        updateValues();

        if (message == null) {
            setPageComplete(true);
        } else {
            setPageComplete(false);
        }

        setErrorMessage(message);
    }

    private void updateValues() {

        this.connectionName = comboConnection.getConnectionName();
        this.dataLibraryName = textDataLibrary.getText();
    }

    @Override
    protected void storePreferences() {

        getPreferences().setConnectionName(connectionName);
        getPreferences().setRapidFireLibrary(dataLibraryName);
    }
}
