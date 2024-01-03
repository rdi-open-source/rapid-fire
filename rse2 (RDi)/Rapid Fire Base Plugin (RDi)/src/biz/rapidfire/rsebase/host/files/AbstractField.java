/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.rsebase.host.files;

import com.ibm.etools.iseries.services.qsys.api.IQSYSDatabaseField;
import com.ibm.etools.iseries.services.qsys.api.IQSYSFileField;

public abstract class AbstractField {

    protected AbstractField() {
    }

    void setField(IQSYSFileField tField) {

        setName(tField.getName());
        setText(tField.getDescription());
        setType(tField.getDataType());
        setLength(tField.getLength());
        setDecimalPosition(tField.getDecimalPosition());

        if (tField instanceof IQSYSDatabaseField) {
            IQSYSDatabaseField tDatabaseField = (IQSYSDatabaseField)tField;
            setAbsoluteReferencedField(tDatabaseField.getReferencedField());
        }
    }

    protected abstract void setName(String aName);

    protected abstract void setText(String aText);

    protected abstract void setType(char aType);

    protected abstract void setLength(Integer aLength);

    protected abstract void setDecimalPosition(Integer aDecimalPosition);

    protected abstract void setAbsoluteReferencedField(String aReferencedField);

    protected abstract void setNumeric(boolean isNumeric);
}
