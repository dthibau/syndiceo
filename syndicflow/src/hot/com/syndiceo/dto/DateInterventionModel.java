package com.syndiceo.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.richfaces.model.CalendarDataModel;
import org.richfaces.model.CalendarDataModelItem;

@Name("dateInterventionModel")
@AutoCreate
@Scope(ScopeType.APPLICATION)
public class DateInterventionModel implements CalendarDataModel {
    private static final String WEEKEND_DAY_CLASS = "wdc";
    private static final String BOUNDARY_DAY_CLASS = "rf-ca-boundary-dates";
 
 
 
    private boolean checkWeekend(Calendar calendar) {
        return (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY);
    }
 
    public CalendarDataModelItem[] getData(Date[] dateArray) {

        CalendarDataModelItem[] modelItems = new CalendarModelItem[dateArray.length];
        Calendar current = GregorianCalendar.getInstance();
        Calendar today = GregorianCalendar.getInstance();
        today.setTime(new Date());
        for (int i = 0; i < dateArray.length; i++) {
            current.setTime(dateArray[i]);
            CalendarModelItem modelItem = new CalendarModelItem();
            if (current.before(today)) {
                modelItem.setEnabled(false);
                modelItem.setStyleClass(BOUNDARY_DAY_CLASS);
            } else if (checkWeekend(current)) {
                modelItem.setEnabled(false);
                modelItem.setStyleClass(WEEKEND_DAY_CLASS);
            } else {
                modelItem.setEnabled(true);
                modelItem.setStyleClass("");
            }
            modelItems[i] = modelItem;
        }
 
        return modelItems;
    }
 
    public Object getToolTip(Date date) {
        // TODO Auto-generated method stub
        return null;
    }
}


