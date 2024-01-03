/*******************************************************************************
 * Copyright (c) 2017-2017 Rapid Fire Project Team
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package biz.rapidfire.core.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import biz.rapidfire.core.RapidFireCorePlugin;
import biz.rapidfire.core.model.IFileCopyStatus;
import biz.rapidfire.core.preferences.Preferences;

public class ProgressBarPainter implements Listener {

    private Table table;
    private int columnIndex;

    private Color colorForeground = RapidFireCorePlugin.getDefault().getColor(RapidFireCorePlugin.COLOR_PROGRESS_BAR_FOREGROUND);
    private Color colorBackground = RapidFireCorePlugin.getDefault().getColor(RapidFireCorePlugin.COLOR_PROGRESS_BAR_BACKGROUND);

    private boolean isLArgeProgressBar = Preferences.getInstance().isLargeProgressBar();

    public ProgressBarPainter(Table table, int columnIndex) {
        this.table = table;
        this.columnIndex = columnIndex;
    }

    public void enableLargeProgressBar(boolean enable) {
        isLArgeProgressBar = enable;
    }

    public void handleEvent(Event event) {

        if (event.index == columnIndex) {
            switch (event.type) {
            case SWT.EraseItem:
                handleEraseItem(event);
                break;

            case SWT.PaintItem:
                handlePaintItem(event);
                break;
            }
        }
    }

    private void handleEraseItem(Event event) {

        int percentDone = getPercentDone(event);

        int barOffsetY;
        int barWidth;
        int barHeight;
        if (isLArgeProgressBar) {
            barOffsetY = 1;
            barWidth = getPercentageOf(event.width, percentDone);
            barHeight = event.height - 1;
        } else {
            barOffsetY = 0;
            barWidth = getPercentageOf(event.width, percentDone);
            barHeight = 4;
        }

        GC gc = event.gc;
        Color oldForeground = gc.getForeground();
        Color oldBackground = gc.getBackground();

        gc.setBackground(colorForeground);
        gc.fillRectangle(event.x, event.y + event.height - barHeight - barOffsetY, barWidth, barHeight);

        gc.setBackground(colorBackground);
        gc.fillRectangle(event.x + barWidth, event.y + event.height - barHeight - barOffsetY, event.width - barWidth, barHeight);

        gc.setForeground(oldForeground);
        gc.setBackground(oldBackground);

        event.detail &= ~SWT.BACKGROUND;
        event.detail &= ~SWT.FOREGROUND;
    }

    public void handlePaintItem(Event event) {

        TableItem item = (TableItem)event.item;
        String text = item.getText(event.index);

        /* center column vertically */
        Point size = event.gc.textExtent(text);
        int yOffset = Math.max(0, (event.height - size.y) / 2);

        /* center column horizontally */
        int columnWidth = table.getColumn(columnIndex).getWidth();

        int percentDone = getPercentDone(event);
        int xOffset = Math.max(1, getPercentageOf((columnWidth - size.x) / 2, percentDone));
        event.gc.drawText(text, event.x + xOffset, event.y + yOffset, true);
    }

    private int getPercentDone(Event event) {

        IFileCopyStatus fileCopyStatus = (IFileCopyStatus)event.item.getData();

        int percentDone = fileCopyStatus.getPercentDone();

        return percentDone;
    }

    private int getPercentageOf(int origin, int percentage) {

        float width = origin / 100.0f * percentage;
        int value = Math.round(width);

        return value;
    }
}
