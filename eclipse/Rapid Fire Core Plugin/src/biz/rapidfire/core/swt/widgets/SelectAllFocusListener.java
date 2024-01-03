/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.swt.widgets;

import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Text;

public class SelectAllFocusListener implements FocusListener {

    public void focusGained(FocusEvent event) {

        if (event.getSource() instanceof Text) {
            Text text = (Text)event.getSource();
            text.selectAll();
        }
    }

    public void focusLost(FocusEvent event) {
    }

}
