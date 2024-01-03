/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.swt.widgets;

import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import biz.rapidfire.core.Messages;
import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.exceptions.IllegalParameterException;
import biz.rapidfire.core.maintenance.MaintenanceMode;
import biz.rapidfire.core.swt.widgets.listeditors.librarylist.LibraryListEditor;
import biz.rapidfire.core.swt.widgets.listeditors.stringlist.StringListEditor;
import biz.rapidfire.rsebase.host.SystemFileType;
import biz.rapidfire.rsebase.swt.widgets.SystemMemberPrompt;

/**
 * Factory for creating SWT widgets.
 * 
 * @author Thomas Raddatz
 */
public final class WidgetFactory {

    private static final int NAME_FIELD_WIDTH_HINT = 90;

    /**
     * The instance of this Singleton class.
     */
    private static WidgetFactory instance;

    /**
     * Private constructor to ensure the Singleton pattern.
     */
    private WidgetFactory() {
    }

    /**
     * Thread-safe method that returns the instance of this Singleton class.
     */
    public synchronized static WidgetFactory getInstance() {
        if (instance == null) {
            instance = new WidgetFactory();
        }
        return instance;
    }

    /**
     * Produces a label for a grid layout.
     * 
     * @param parent - a composite control which will be the parent of the new
     *        instance (cannot be null)
     * @param text - text of the label
     * @param tooltip - tooltip of the label
     * @return label
     */
    public static Label createLabel(Composite parent, String text, String tooltip) {
        return WidgetFactory.getInstance().produceLabel(parent, text, tooltip);
    }

    /**
     * Produces a SystemHostCombo.
     * 
     * @param parent a composite control which will be the parent of the new
     *        instance (cannot be null)
     * @param style the style of control to construct
     * @return system host combo for selection a connection
     */
    public static ISystemHostCombo createSystemHostCombo(Composite parent, int style) {
        return createSystemHostCombo(parent, style, true);
    }

    /**
     * Produces a SystemHostCombo.
     * 
     * @param parent - a composite control which will be the parent of the new
     *        instance (cannot be null)
     * @param style - the style of control to construct
     * @param showNewButton - specifies whether to display the "New" button
     * @return system host combo for selection a connection
     */
    public static ISystemHostCombo createSystemHostCombo(Composite parent, int style, boolean showNewButton) {
        return WidgetFactory.getInstance().produceSystemHostCombo(parent, style, showNewButton);
    }

    /**
     * Produces a SystemMemberPrompt.
     * 
     * @param parent - a composite control which will be the parent of the new
     *        instance (cannot be null)
     * @param style - the style of control to construct
     * @param fileType - Pass one of the types of
     *        biz.rapidfire.rsebase.swt.widgets.SystemFileType such as SRC
     * @return system member prompt for selection a file member
     */
    public static SystemMemberPrompt createSystemMemberPrompt(Composite parent, int style, SystemFileType fileType) {
        return WidgetFactory.getInstance().produceSystemMemberPrompt(parent, style, true, true, fileType);
    }

    /**
     * Produces a SystemMemberPrompt.
     * 
     * @param parent - a composite control which will be the parent of the new
     *        instance (cannot be null)
     * @param style - the style of control to construct
     * @param allowGeneric - Pass true to allow generic names in the entry field
     * @param allowLibl - Pass false if allowGeneric is false, to restrict user
     *        from entering/selecting "*LIBL" for the library
     * @param fileType - Pass one of the types of
     *        biz.rapidfire.rsebase.swt.widgets.SystemFileType such as SRC
     * @return system member prompt for selection a file member
     */
    public static SystemMemberPrompt createSystemMemberPrompt(Composite parent, int style, boolean allowGeneric, boolean allowLibl,
        SystemFileType fileType) {
        return WidgetFactory.getInstance().produceSystemMemberPrompt(parent, style, allowGeneric, allowLibl, fileType);
    }

    /**
     * Produces a line filler.
     * 
     * @param parent - composite control which will be the parent of the new
     *        instance (cannot be null)
     */
    public static Control createLineFiller(Composite parent) {
        return WidgetFactory.getInstance().produceLineFiller(parent, SWT.DEFAULT);
    }

    /**
     * Produces a line filler.
     * 
     * @param parent - composite control which will be the parent of the new
     *        instance (cannot be null)
     * @param height - height hint or SWT.DEFAULT
     */
    public static Control createLineFiller(Composite parent, int height) {
        return WidgetFactory.getInstance().produceLineFiller(parent, height);
    }

    /**
     * Produces a StringListEditor.
     * 
     * @param parent a composite control which will be the parent of the new
     *        instance (cannot be null)
     * @return editor for editing a list of strings
     */
    public static StringListEditor createStringListEditor(Composite parent) {
        return createStringListEditor(parent, SWT.NONE);
    }

    /**
     * Produces a StringListEditor.
     * 
     * @param parent a composite control which will be the parent of the new
     *        instance (cannot be null)
     * @param style the style of control to construct
     * @return editor for editing a list of strings
     */
    public static StringListEditor createStringListEditor(Composite parent, int style) {
        return WidgetFactory.getInstance().produceStringListEditor(parent, style);
    }

    /**
     * Produces a LibraryListEditor.
     * 
     * @param parent a composite control which will be the parent of the new
     *        instance (cannot be null)
     * @return editor for editing a library list
     */
    public static LibraryListEditor createLibraryListEditor(Composite parent) {
        return createLibraryListEditor(parent, SWT.NONE);
    }

    /**
     * Produces a LibraryListEditor.
     * 
     * @param parent a composite control which will be the parent of the new
     *        instance (cannot be null)
     * @param style the style of control to construct
     * @return editor for editing a library lists
     */
    public static LibraryListEditor createLibraryListEditor(Composite parent, int style) {
        return WidgetFactory.getInstance().produceLibraryListEditor(parent, style);
    }

    /**
     * Produces a color selector control.
     * 
     * @param parent - parent composite
     * @return color selector
     */
    public static ColorSelector createColorSelector(Composite parent) {
        return new ColorSelector(parent);
    }

    /**
     * Produces a dialog "mode" sub-title label.
     * 
     * @param parent - parent composite
     * @param mode - dialog mode
     * @return dialog sub-title
     */
    public static Label createDialogSubTitle(Composite parent, MaintenanceMode mode) {
        return WidgetFactory.getInstance().produceDialogSubTitle(parent, mode);
    }

    /**
     * Produces a separator.
     * 
     * @param parent - parent composite
     * @return separator
     */
    public static Label createSeparator(Composite parent) {
        return createSeparator(parent, 1);
    }

    /**
     * Produces a separator, spanning multiple columns.
     * 
     * @param parent - parent composite
     * @param span - number of columns to span
     * @return separator
     */
    public static Label createSeparator(Composite parent, int span) {

        Label separator = WidgetFactory.getInstance().produceSeparator(parent, SWT.SEPARATOR | SWT.HORIZONTAL);

        return separator;
    }

    /**
     * Produces a 'name' text field. The field is upper-case only and limited to
     * 10 characters.
     * 
     * @param parent - parent composite
     * @return text field
     */
    public static Text createNameText(Composite parent) {

        return createNameText(parent, true);
    }

    /**
     * Produces a 'name' text field. The field is upper-case only and limited to
     * 10 characters.
     * 
     * @param parent - parent composite
     * @param widthHint - set default text width
     * @return text field
     */
    public static Text createNameText(Composite parent, boolean widthHint) {

        Text text = createUpperCaseText(parent);
        text.setTextLimit(10);

        if (widthHint) {
            GridData gd = new GridData();
            gd.widthHint = NAME_FIELD_WIDTH_HINT;
            text.setLayoutData(gd);
        }

        return text;
    }

    /**
     * Produces a 'name' text field. The field is upper-case only and limited to
     * 10 characters.
     * 
     * @param parent - parent composite
     * @return text field
     */
    public static Combo createNameCombo(Composite parent) {

        return createNameCombo(parent, true);
    }

    /**
     * Produces a 'name' text field. The field is upper-case only and limited to
     * 10 characters.
     * 
     * @param parent - parent composite
     * @param widthHint - set default text width
     * @return text field
     */
    public static Combo createNameCombo(Composite parent, boolean widthHint) {

        Combo text = createCombo(parent);
        text.addVerifyListener(new UpperCaseOnlyVerifier());
        text.setTextLimit(10);

        if (widthHint) {
            GridData gd = new GridData();
            gd.widthHint = NAME_FIELD_WIDTH_HINT;
            text.setLayoutData(gd);
        }

        return text;
    }

    /**
     * Produces a single line text field with a border.
     * 
     * @param parent - parent composite
     * @return text field
     */
    public static Text createText(Composite parent) {
        return WidgetFactory.getInstance().produceText(parent, SWT.NONE, true);
    }

    /**
     * Produces a read-only single line text field.
     * 
     * @param parent - parent composite
     * @return read-only text field
     */
    public static Text createReadOnlyText(Composite parent) {

        Text text = WidgetFactory.getInstance().produceText(parent, SWT.NONE, false);
        text.setEditable(false);

        return text;
    }

    /**
     * Produces a single line text field with a border. Input is upper-case
     * only.
     * 
     * @param parent - parent composite
     * @return text field
     */
    public static Text createUpperCaseText(Composite parent) {

        Text text = WidgetFactory.getInstance().produceText(parent, SWT.NONE, true);
        text.addVerifyListener(new UpperCaseOnlyVerifier());

        return text;
    }

    /**
     * Produces an upper-case, read-only single line text field.
     * 
     * @param parent - parent composite
     * @return read-only text field
     */
    public static Text createUpperCaseReadOnlyText(Composite parent) {

        Text text = WidgetFactory.getInstance().produceText(parent, SWT.NONE, false);
        text.addVerifyListener(new UpperCaseOnlyVerifier());
        text.setEditable(false);

        return text;
    }

    /**
     * Produces a 'description' text field. The field is limited to 35
     * characters.
     * 
     * @param parent - parent composite
     * @return text field
     */
    public static Text createDescriptionText(Composite parent) {

        Text description = WidgetFactory.getInstance().produceText(parent, SWT.NONE, true);
        description.setTextLimit(35);

        return description;
    }

    /**
     * Produces a password field with a border.
     * 
     * @param parent - parent composite
     * @return password field
     */
    public static Text createPassword(Composite parent) {
        return WidgetFactory.getInstance().produceText(parent, SWT.PASSWORD, true);
    }

    /**
     * Produces a label with selectable text.
     * 
     * @param parent - parent composite
     * @return label with selectable text
     */
    public static Text createSelectableLabel(Composite parent) {

        Text text = new Text(parent, SWT.NONE);
        text.setEditable(false);

        return text;
    }

    /**
     * Produces a multi-line line label with selectable text. If the text does
     * not fit into the field, a vertical scroll bar is displayed.
     * 
     * @param parent - parent composite
     * @return multi-line label with selectable text
     */
    public static Text createSelectableMultilineLabel(Composite parent) {

        Text text = new Text(parent, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);

        Listener scrollBarListener = new AutoScrollbarsListener();
        text.addListener(SWT.Resize, scrollBarListener);
        text.addListener(SWT.Modify, scrollBarListener);
        text.setEditable(false);

        // if (autoSelect) {
        // text.addFocusListener(new SelectAllFocusListener());
        // }

        return text;
    }

    /**
     * Produces a multi-line line text field with a border. If the text does not
     * fit into the field, a vertical scroll bar is displayed.
     * 
     * @param parent - parent composite
     * @return multi-line text field
     */
    public static Text createMultilineText(Composite parent) {
        return WidgetFactory.getInstance().produceMultilineText(parent, SWT.NONE, false);
    }

    /**
     * Produces a multi-line line text field with a border. If the text does not
     * fit into the field, a vertical scroll bar is displayed.
     * 
     * @param parent - parent composite
     * @param wordWrap - <code>true</code>, to enable word wrap
     * @param autoSelect - <code>true</code>, to select the field content when
     *        entering the field wit the cursor
     * @return multi-line text field
     */
    public static Text createMultilineText(Composite parent, boolean wordWrap, boolean autoSelect) {
        int style;
        if (wordWrap) {
            style = SWT.WRAP;
        } else {
            style = SWT.H_SCROLL;
        }
        return WidgetFactory.getInstance().produceMultilineText(parent, style, autoSelect);
    }

    /**
     * Produces a multi-line label with a border. If the text does not fit into
     * the field, a vertical scroll bar is displayed.
     * 
     * @param parent - parent composite
     * @return multi-line label
     */
    public static Text createMultilineLabel(Composite parent) {
        return WidgetFactory.getInstance().produceMultilineLabel(parent, SWT.NONE);
    }

    /**
     * Produces a multi-line label with a border. If the text does not fit into
     * the field, a vertical scroll bar is displayed.
     * 
     * @param parent - parent composite
     * @param style - the style of control to construct
     * @return multi-line label
     */
    public static Text createMultilineLabel(Composite parent, int style) {
        return WidgetFactory.getInstance().produceMultilineLabel(parent, style);
    }

    /**
     * Produces a read-only multiline line text field. If the text does not fit
     * into the field, a vertical scroll bar is displayed.
     * 
     * @param parent - parent composite
     * @return read-only multi-line text field
     */
    public static Text createReadOnlyMultilineText(Composite parent) {

        Text text = WidgetFactory.createMultilineText(parent);
        text.setEditable(false);

        return text;
    }

    /**
     * Produces a read-only multiline line text field. If the text does not fit
     * into the field, a vertical scroll bar is displayed.
     * 
     * @param parent - parent composite
     * @param wordWrap - <code>true</code>, to enable word wrap
     * @param autoSelect - <code>true</code>, to select the field content when
     *        entering the field wit the cursor
     * @return read-only multi-line text field
     */
    public static Text createReadOnlyMultilineText(Composite parent, boolean wordWrap, boolean autoSelect) {

        Text text = WidgetFactory.createMultilineText(parent, wordWrap, autoSelect);
        text.setEditable(false);

        return text;
    }

    /**
     * Produces an integer text field with a border. Only the character 0-9 are
     * allowed to be entered.
     * 
     * @param parent - parent composite
     * @return integer text field
     */
    public static Text createIntegerText(Composite parent) {
        return createIntegerText(parent, false);
    }

    /**
     * Produces an integer text field with a border. Only the character 0-9 are
     * allowed to be entered.
     * 
     * @param parent - parent composite
     * @param hasSign - specifies whether to enable the signs '+' and '-'.
     * @return integer text field
     */
    public static Text createIntegerText(Composite parent, boolean hasSign) {

        Text text = WidgetFactory.createText(parent);
        text.addVerifyListener(new NumericOnlyVerifyListener(false, hasSign));

        return text;
    }

    /**
     * Produces a read-only integer text field. Only the character 0-9 are
     * allowed to be entered.
     * 
     * @param parent - parent composite
     * @return read-only integer text field
     */
    public static Text createReadOnlyIntegerText(Composite parent) {

        Text text = WidgetFactory.createIntegerText(parent);
        text.setEditable(false);

        return text;
    }

    /**
     * Produces a decimal text field with a border. Only the character 0-9 and
     * comma are allowed to be entered.
     * 
     * @param parent - parent composite
     * @return decimal text field
     */
    public static Text createDecimalText(Composite parent) {
        return createDecimalText(parent, false);
    }

    /**
     * Produces a decimal text field with a border. Only the character 0-9 and
     * comma are allowed to be entered.
     * 
     * @param parent - parent composite
     * @param hasSign - specifies whether to enable the signs '+' and '-'.
     * @return decimal text field
     */
    public static Text createDecimalText(Composite parent, boolean hasSign) {

        Text text = WidgetFactory.createText(parent);
        text.addVerifyListener(new NumericOnlyVerifyListener(true, hasSign));

        return text;
    }

    /**
     * Produces a read-only decimal text field with a border. Only the character
     * 0-9 and comma are allowed to be entered.
     * 
     * @param parent - parent composite
     * @param border - specifies whether or not to add a border
     * @return read-only decimal text field
     */
    public static Text createReadOnlyDecimalText(Composite parent, boolean border) {

        Text text = WidgetFactory.createDecimalText(parent);
        text.setEditable(false);

        return text;
    }

    /**
     * Produces a combo field.
     * 
     * @param parent - parent composite
     * @return combo field
     */
    public static Combo createCombo(Composite parent) {
        return WidgetFactory.getInstance().produceComboField(parent, SWT.NONE);
    }

    /**
     * Produces an upper-case combo field.
     * 
     * @param parent - parent composite
     * @return combo field
     */
    public static Combo createUpperCaseCombo(Composite parent) {
        Combo combo = WidgetFactory.getInstance().produceComboField(parent, SWT.NONE);
        combo.addVerifyListener(new UpperCaseOnlyVerifier());
        return combo;
    }

    /**
     * Produces a read-only combo field.
     * 
     * @param parent - parent composite
     * @return read-only combo field
     */
    public static Combo createReadOnlyCombo(Composite parent) {
        return WidgetFactory.getInstance().produceComboField(parent, SWT.READ_ONLY);
    }

    /**
     * Produces a spinner field.
     * 
     * @param parent - parent composite
     * @return spinner field
     */
    public static Spinner createSpinner(Composite parent) {
        return WidgetFactory.getInstance().produceSpinnerField(parent, SWT.BORDER);
    }

    /**
     * Produces a read-only spinner field.
     * 
     * @param parent - parent composite
     * @return read-only spinner field
     */
    public static Spinner createReadOnlySpinner(Composite parent) {
        return WidgetFactory.getInstance().produceSpinnerField(parent, SWT.BORDER | SWT.READ_ONLY);
    }

    /**
     * Produces a checkbox field.
     * 
     * @param parent - parent composite
     * @return checkbox field
     */
    public static Button createCheckbox(Composite parent) {
        return WidgetFactory.getInstance().produceCheckboxField(parent, SWT.NONE, null, null);
    }

    /**
     * Produces a checkbox field.
     * 
     * @param parent - parent composite
     * @param label - label of the checkbox
     * @return checkbox field
     */
    public static Button createCheckbox(Composite parent, String label, String tooltip, int style) {
        return WidgetFactory.getInstance().produceCheckboxField(parent, style, label, tooltip);
    }

    /**
     * Produces a read-only checkbox field.
     * 
     * @param parent - parent composite
     * @return read-only checkbox field
     */
    public static Button createReadOnlyCheckbox(Composite parent) {

        Button checkBox = WidgetFactory.createCheckbox(parent);
        checkBox.setEnabled(false);

        return checkBox;
    }

    /**
     * Produces a push button.
     * 
     * @param parent - parent composite
     * @return push button
     */
    public static Button createPushButton(Composite parent) {
        return WidgetFactory.getInstance().producePushButton(parent);
    }

    /**
     * Produces a push button with a label.
     * 
     * @param parent - parent composite
     * @param string - button label
     * @return push button
     */
    public static Button createPushButton(Composite parent, String label) {
        Button button = WidgetFactory.getInstance().producePushButton(parent);
        button.setText(label);
        return button;
    }

    /**
     * Produces a push button with an image.
     * 
     * @param parent - parent composite
     * @param image - button image
     * @return push button
     */
    public static Button createPushButton(Composite parent, Image image) {
        Button button = WidgetFactory.getInstance().producePushButton(parent);
        button.setImage(image);
        return button;
    }

    /**
     * Produces a read-only push button field.
     * 
     * @param parent - parent composite
     * @return read-only push button field
     */
    public static Button createReadOnlyPushButton(Composite parent) {

        Button pushButton = WidgetFactory.createPushButton(parent);
        pushButton.setEnabled(false);

        return pushButton;
    }

    /**
     * Produces a toggle button.
     * 
     * @param parent - parent composite
     * @return toggle button
     */
    public static Button createToggleButton(Composite parent) {
        return WidgetFactory.getInstance().produceToggleButton(parent);
    }

    /**
     * Produces a toggle button with a label.
     * 
     * @param parent - parent composite
     * @param style - additional style options, such as {@link SWT#FLAT}
     * @return toggle button
     */
    public static Button createToggleButton(Composite parent, int style) {
        Button button = WidgetFactory.getInstance().produceToggleButton(parent);
        return button;
    }

    /**
     * Produces a toggle button with a label.
     * 
     * @param parent - parent composite
     * @param string - button label
     * @return toggle button
     */
    public static Button createToggleButton(Composite parent, String label) {
        Button button = WidgetFactory.getInstance().produceToggleButton(parent);
        button.setText(label);
        return button;
    }

    /**
     * Produces a toggle button with a label.
     * 
     * @param parent - parent composite
     * @param string - button label
     * @param style - additional style options, such as {@link SWT#FLAT}
     * @return toggle button
     */
    public static Button createToggleButton(Composite parent, String label, int style) {
        Button button = WidgetFactory.getInstance().produceToggleButton(parent);
        button.setText(label);
        return button;
    }

    /**
     * Produces a radio button field.
     * 
     * @param parent - parent composite
     * @return radio button field
     */
    public static Button createRadioButton(Composite parent) {
        return WidgetFactory.getInstance().produceRadioButton(parent);
    }

    /**
     * Produces a read-only radio button field.
     * 
     * @param parent - parent composite
     * @return read-only radio button field
     */
    public static Button createReadOnlyRadioButton(Composite parent) {

        Button radioButton = WidgetFactory.createRadioButton(parent);
        radioButton.setEnabled(false);

        return radioButton;
    }

    /*
     * Private worker procedures, doing the actual work.
     */
    private Text produceText(Composite parent, int style, boolean autoSelect) {

        Text text = new Text(parent, style | SWT.BORDER);
        if (autoSelect) {
            text.addFocusListener(new SelectAllFocusListener());
        }

        return text;
    }

    private Text produceMultilineText(Composite parent, int style, boolean autoSelect) {

        Text text = new Text(parent, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | style);

        Listener scrollBarListener = new AutoScrollbarsListener();
        text.addListener(SWT.Resize, scrollBarListener);
        text.addListener(SWT.Modify, scrollBarListener);

        if (autoSelect) {
            text.addFocusListener(new SelectAllFocusListener());
        }

        return text;
    }

    private Text produceMultilineLabel(Composite parent, int style) {

        Text text = new Text(parent, SWT.MULTI | SWT.WRAP | style);
        text.setEditable(false);

        Listener scrollBarListener = new AutoScrollbarsListener();
        text.addListener(SWT.Resize, scrollBarListener);
        text.addListener(SWT.Modify, scrollBarListener);

        return text;
    }

    private Combo produceComboField(Composite parent, int style) {

        Combo combo = new Combo(parent, style | SWT.DROP_DOWN);

        return combo;
    }

    private Spinner produceSpinnerField(Composite parent, int style) {

        Spinner spinner = new Spinner(parent, style);

        return spinner;
    }

    private Button produceCheckboxField(Composite parent, int style, String text, String tooltip) {

        if (!isOption(style, SWT.NONE) && !isOption(style, SWT.LEFT) && !isOption(style, SWT.RIGHT)) {
            throw new RuntimeException("Invalid style option: " + style);
        }

        int numColumns;
        if (text != null) {
            numColumns = 2;
        } else {
            numColumns = 1;
        }

        Composite inner = new Composite(parent, SWT.NONE);
        GridLayout gl = new GridLayout(numColumns, false);
        gl.marginHeight = 0;
        gl.marginWidth = 0;
        inner.setLayout(gl);

        Label labelControl = null;
        if (numColumns == 2 && isOption(style, SWT.LEFT)) {
            labelControl = createLabel(inner, text, tooltip);
        }

        Button checkBox = new Button(inner, SWT.CHECK);

        if (numColumns == 2 && isOption(style, SWT.RIGHT)) {
            labelControl = createLabel(inner, text, tooltip);
        }

        if (labelControl != null) {
            if (tooltip != null) {
                labelControl.setToolTipText(tooltip);
            }
        }

        return checkBox;
    }

    private Button producePushButton(Composite parent) {

        Button pushButton = new Button(parent, SWT.PUSH);

        return pushButton;
    }

    private Button produceToggleButton(Composite parent) {

        Button pushButton = new Button(parent, SWT.TOGGLE);

        return pushButton;
    }

    private Button produceRadioButton(Composite parent) {

        Button radioButton = new Button(parent, SWT.RADIO);

        return radioButton;
    }

    private Label produceSeparator(Composite parent, int span) {

        Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalSpan = span;
        separator.setLayoutData(gridData);

        return separator;
    }

    private Label produceDialogSubTitle(Composite parent, MaintenanceMode mode) {

        Color color = null;

        String title;
        if (MaintenanceMode.CREATE.equals(mode)) {
            title = Messages.DialogMode_CREATE;
            color = RapidFireCorePlugin.getDefault().getColor(RapidFireCorePlugin.COLOR_DIALOG_MODE_CREATE);
        } else if (MaintenanceMode.COPY.equals(mode)) {
            title = Messages.DialogMode_COPY;
            color = RapidFireCorePlugin.getDefault().getColor(RapidFireCorePlugin.COLOR_DIALOG_MODE_COPY);
        } else if (MaintenanceMode.CHANGE.equals(mode)) {
            title = Messages.DialogMode_CHANGE;
            color = RapidFireCorePlugin.getDefault().getColor(RapidFireCorePlugin.COLOR_DIALOG_MODE_CHANGE);
        } else if (MaintenanceMode.DELETE.equals(mode)) {
            title = Messages.DialogMode_DELETE;
            color = RapidFireCorePlugin.getDefault().getColor(RapidFireCorePlugin.COLOR_DIALOG_MODE_DELETE);
        } else if (MaintenanceMode.DISPLAY.equals(mode)) {
            title = Messages.DialogMode_DISPLAY;
            color = RapidFireCorePlugin.getDefault().getColor(RapidFireCorePlugin.COLOR_DIALOG_MODE_DISPLAY);
        } else {
            throw new IllegalParameterException("mode", mode.label()); //$NON-NLS-1$
        }

        Label label = new Label(parent, SWT.CENTER);
        label.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false, ((GridLayout)label.getParent().getLayout()).numColumns, 1));
        label.setForeground(RapidFireCorePlugin.getDefault().getColor(RapidFireCorePlugin.COLOR_DIALOG_MODE_FOREGROUND));
        label.setBackground(color);
        label.setText(title);

        return label;
    }

    private StringListEditor produceStringListEditor(Composite parent, int style) {
        return new StringListEditor(parent, style);
    }

    private LibraryListEditor produceLibraryListEditor(Composite parent, int style) {
        return new LibraryListEditor(parent, style);
    }

    private Control produceLineFiller(Composite parent, int height) {

        Label filler = new Label(parent, SWT.NONE);
        filler.setSize(height, 1);

        Layout layout = parent.getLayout();
        if (layout instanceof GridLayout) {
            GridLayout gridLayout = (GridLayout)layout;
            GridData gd = new GridData();
            gd.horizontalSpan = gridLayout.numColumns;
            if (height != SWT.DEFAULT) {
                gd.heightHint = height;
            }
            filler.setLayoutData(gd);
        }

        return filler;
    }

    private ISystemHostCombo produceSystemHostCombo(Composite parent, int style, boolean showNewButton) {

        ISystemHostCombo systemHostCombo = new SystemHostCombo(parent, style, showNewButton);

        return systemHostCombo;
    }

    private SystemMemberPrompt produceSystemMemberPrompt(Composite parent, int style, boolean allowGeneric, boolean allowLibl,
        SystemFileType fileType) {

        SystemMemberPrompt systemMemberPrompt = new SystemMemberPrompt(parent, style, allowGeneric, allowLibl, fileType);

        return systemMemberPrompt;
    }

    private Label produceLabel(Composite parent, String text, String tooltip) {

        Label label = new Label(parent, SWT.NONE);
        label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));

        if (text != null) {
            label.setText(text);
        }

        if (tooltip != null) {
            label.setToolTipText(tooltip);
        }

        return label;
    }

    private boolean isOption(int style, int option) {

        if ((style & option) == option) {
            return true;
        }

        return false;
    }

    /**
     * Thread-safe method that disposes the instance of this Singleton class.
     * <p>
     * This method is intended to be call from
     * {@link org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)}
     * to free the reference to itself.
     */
    public static void dispose() {
        if (instance != null) {
            instance = null;
        }
    }
}
