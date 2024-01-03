/*******************************************************************************
 * Copyright (c) 2017-2018 Rapid Fire Project Owners
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.maintenance.conversion;

import java.util.LinkedList;
import java.util.List;

public class FieldConversions {

    private static final String EMPTY = "";

    private int NUM_CONVERSIONS = 6;

    private List<String> conversions;

    public FieldConversions() {

        conversions = new LinkedList<String>();
        fillUpConversions();
    }

    public void setConversions(String[] conversions) {

        this.conversions.clear();

        for (String conversion : conversions) {
            this.conversions.add(conversion);
            if (this.conversions.size() >= NUM_CONVERSIONS) {
                break;
            }
        }

        fillUpConversions();
    }

    private void fillUpConversions() {

        while (this.conversions.size() < NUM_CONVERSIONS) {
            this.conversions.add(EMPTY);
        }
    }

    public String getConversion(int index) {
        return conversions.get(index);
    }

    public String[] getConversions() {
        return conversions.toArray(new String[conversions.size()]);
    }
}
