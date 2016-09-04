/**
 *  Copyright 2012-2016 Gunnar Morling (http://www.gunnarmorling.de/)
 *  and/or other contributors as indicated by the @authors tag. See the
 *  copyright.txt file in the distribution for a full listing of all
 *  contributors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.mapstruct.ap.test.builtin.bean.jodatime;

import java.util.TimeZone;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import static org.assertj.core.api.Assertions.assertThat;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.ap.test.builtin.bean.jodatime.bean.DateTimeBean;
import org.mapstruct.ap.test.builtin.bean.jodatime.bean.XmlGregorianCalendarBean;
import org.mapstruct.ap.test.builtin.bean.jodatime.mapper.DateTimeToXmlGregorianCalendar;
import org.mapstruct.ap.test.builtin.bean.jodatime.mapper.XmlGregorianCalendarToDateTime;
import org.mapstruct.ap.testutil.IssueKey;
import org.mapstruct.ap.testutil.WithClasses;
import org.mapstruct.ap.testutil.runner.AnnotationProcessorTestRunner;

/**
 *
 * @author Sjaak Derksen
 */
@WithClasses({
    DateTimeBean.class,
    XmlGregorianCalendarBean.class
})
@RunWith(AnnotationProcessorTestRunner.class)
@IssueKey( "689" )
public class JodaTimeTest {

    @Test
    @WithClasses(DateTimeToXmlGregorianCalendar.class)
    public void shouldMapDateTimeToXmlGregorianCalendar() {

        DateTimeBean  in = new DateTimeBean();
        DateTime dt = new DateTime(2010, 1, 15, 1, 1, 1, 100, DateTimeZone.forOffsetHours( -1 ) );
        in.setDateTime( dt );
        XmlGregorianCalendarBean res = DateTimeToXmlGregorianCalendar.INSTANCE.toXmlGregorianCalendarBean( in );

        assertThat( res.getxMLGregorianCalendar().getYear() ).isEqualTo( 2010 );
        assertThat( res.getxMLGregorianCalendar().getMonth() ).isEqualTo( 1 );
        assertThat( res.getxMLGregorianCalendar().getDay() ).isEqualTo( 15 );
        assertThat( res.getxMLGregorianCalendar().getHour() ).isEqualTo( 1 );
        assertThat( res.getxMLGregorianCalendar().getMinute() ).isEqualTo( 1 );
        assertThat( res.getxMLGregorianCalendar().getSecond() ).isEqualTo( 1 );
        assertThat( res.getxMLGregorianCalendar().getMillisecond() ).isEqualTo( 100 );
        assertThat( res.getxMLGregorianCalendar().getTimezone() ).isEqualTo( -60 );
    }

    @Test
    @WithClasses(DateTimeToXmlGregorianCalendar.class)
    public void shouldMapIncompleteDateTimeToXmlGregorianCalendar() {

        DateTimeBean  in = new DateTimeBean();
        DateTime dt = new DateTime(2010, 1, 15, 1, 1 );
        in.setDateTime( dt );
        XmlGregorianCalendarBean res = DateTimeToXmlGregorianCalendar.INSTANCE.toXmlGregorianCalendarBean( in );

        assertThat( res.getxMLGregorianCalendar().getYear() ).isEqualTo( 2010 );
        assertThat( res.getxMLGregorianCalendar().getMonth() ).isEqualTo( 1 );
        assertThat( res.getxMLGregorianCalendar().getDay() ).isEqualTo( 15 );
        assertThat( res.getxMLGregorianCalendar().getHour() ).isEqualTo( 1 );
        assertThat( res.getxMLGregorianCalendar().getMinute() ).isEqualTo( 1 );
    }

    @Test
    @WithClasses(XmlGregorianCalendarToDateTime.class)
    public void shouldMapXmlGregorianCalendarToDateTime() throws Exception {

        XmlGregorianCalendarBean in = new XmlGregorianCalendarBean();
        XMLGregorianCalendar xcal =
            DatatypeFactory.newInstance().newXMLGregorianCalendar( 2010, 1, 15, 1, 1, 1, 100, 60 );
        in.setxMLGregorianCalendar( xcal );

        DateTimeBean res = XmlGregorianCalendarToDateTime.INSTANCE.toDateTimeBean( in );
        assertThat( res.getDateTime().getYear() ).isEqualTo( 2010 );
        assertThat( res.getDateTime().getMonthOfYear() ).isEqualTo( 1 );
        assertThat( res.getDateTime().getDayOfMonth() ).isEqualTo( 15 );
        assertThat( res.getDateTime().getHourOfDay() ).isEqualTo( 1 );
        assertThat( res.getDateTime().getMinuteOfHour() ).isEqualTo( 1 );
        assertThat( res.getDateTime().getSecondOfMinute() ).isEqualTo( 1 );
        assertThat( res.getDateTime().getMillisOfSecond() ).isEqualTo( 100 );
        assertThat( res.getDateTime().getZone().getOffset( null ) ).isEqualTo( 3600000 );
    }

    @Test
    @WithClasses(XmlGregorianCalendarToDateTime.class)
    public void shouldMapXmlGregorianCalendarWithoutTimeZoneToDateTimeWithDefaultTimeZone() throws Exception {

        XmlGregorianCalendarBean in = new XmlGregorianCalendarBean();
        XMLGregorianCalendar xcal = DatatypeFactory.newInstance().newXMLGregorianCalendar();
        xcal.setYear( 1999 );
        xcal.setMonth( 5 );
        xcal.setDay( 25 );
        xcal.setHour( 23 );
        xcal.setMinute( 34 );
        xcal.setSecond( 45 );
        xcal.setMillisecond( 500 );
        in.setxMLGregorianCalendar( xcal );

        DateTimeBean res = XmlGregorianCalendarToDateTime.INSTANCE.toDateTimeBean( in );
        assertThat( res.getDateTime().getYear() ).isEqualTo( 1999 );
        assertThat( res.getDateTime().getMonthOfYear() ).isEqualTo( 5 );
        assertThat( res.getDateTime().getDayOfMonth() ).isEqualTo( 25 );
        assertThat( res.getDateTime().getHourOfDay() ).isEqualTo( 23 );
        assertThat( res.getDateTime().getMinuteOfHour() ).isEqualTo( 34 );
        assertThat( res.getDateTime().getSecondOfMinute() ).isEqualTo( 45 );
        assertThat( res.getDateTime().getMillisOfSecond() ).isEqualTo( 500 );
        assertThat( res.getDateTime().getZone().getID() ).isEqualTo( TimeZone.getDefault().getID() );
    }

    @Test
    @WithClasses(XmlGregorianCalendarToDateTime.class)
    public void shouldMapXmlGregorianCalendarWithoutMillisToDateTime() throws Exception {

        XmlGregorianCalendarBean in = new XmlGregorianCalendarBean();
        XMLGregorianCalendar xcal = DatatypeFactory.newInstance().newXMLGregorianCalendar();
        xcal.setYear( 1999 );
        xcal.setMonth( 5 );
        xcal.setDay( 25 );
        xcal.setHour( 23 );
        xcal.setMinute( 34 );
        xcal.setSecond( 45 );
        xcal.setTimezone( 60 );
        in.setxMLGregorianCalendar( xcal );

        DateTimeBean res = XmlGregorianCalendarToDateTime.INSTANCE.toDateTimeBean( in );
        assertThat( res.getDateTime().getYear() ).isEqualTo( 1999 );
        assertThat( res.getDateTime().getMonthOfYear() ).isEqualTo( 5 );
        assertThat( res.getDateTime().getDayOfMonth() ).isEqualTo( 25 );
        assertThat( res.getDateTime().getHourOfDay() ).isEqualTo( 23 );
        assertThat( res.getDateTime().getMinuteOfHour() ).isEqualTo( 34 );
        assertThat( res.getDateTime().getSecondOfMinute() ).isEqualTo( 45 );
        assertThat( res.getDateTime().getMillisOfSecond() ).isEqualTo( 0 );
        assertThat( res.getDateTime().getZone().getOffset( null ) ).isEqualTo( 3600000 );
    }

    @Test
    @WithClasses(XmlGregorianCalendarToDateTime.class)
    public void shouldMapXmlGregorianCalendarWithoutMillisAndTimeZoneToDateTimeWithDefaultTimeZone() throws Exception {

        XmlGregorianCalendarBean in = new XmlGregorianCalendarBean();
        XMLGregorianCalendar xcal = DatatypeFactory.newInstance().newXMLGregorianCalendar();
        xcal.setYear( 1999 );
        xcal.setMonth( 5 );
        xcal.setDay( 25 );
        xcal.setHour( 23 );
        xcal.setMinute( 34 );
        xcal.setSecond( 45 );
        in.setxMLGregorianCalendar( xcal );

        DateTimeBean res = XmlGregorianCalendarToDateTime.INSTANCE.toDateTimeBean( in );
        assertThat( res.getDateTime().getYear() ).isEqualTo( 1999 );
        assertThat( res.getDateTime().getMonthOfYear() ).isEqualTo( 5 );
        assertThat( res.getDateTime().getDayOfMonth() ).isEqualTo( 25 );
        assertThat( res.getDateTime().getHourOfDay() ).isEqualTo( 23 );
        assertThat( res.getDateTime().getMinuteOfHour() ).isEqualTo( 34 );
        assertThat( res.getDateTime().getSecondOfMinute() ).isEqualTo( 45 );
        assertThat( res.getDateTime().getMillisOfSecond() ).isEqualTo( 0 );
        assertThat( res.getDateTime().getZone().getID() ).isEqualTo( TimeZone.getDefault().getID() );
    }

    @Test
    @WithClasses(XmlGregorianCalendarToDateTime.class)
    public void shouldMapXmlGregorianCalendarWithoutSecondsToDateTime() throws Exception {

        XmlGregorianCalendarBean in = new XmlGregorianCalendarBean();
        XMLGregorianCalendar xcal = DatatypeFactory.newInstance().newXMLGregorianCalendar();
        xcal.setYear( 1999 );
        xcal.setMonth( 5 );
        xcal.setDay( 25 );
        xcal.setHour( 23 );
        xcal.setMinute( 34 );
        xcal.setTimezone( 60 );
        in.setxMLGregorianCalendar( xcal );

        DateTimeBean res = XmlGregorianCalendarToDateTime.INSTANCE.toDateTimeBean( in );
        assertThat( res.getDateTime().getYear() ).isEqualTo( 1999 );
        assertThat( res.getDateTime().getMonthOfYear() ).isEqualTo( 5 );
        assertThat( res.getDateTime().getDayOfMonth() ).isEqualTo( 25 );
        assertThat( res.getDateTime().getHourOfDay() ).isEqualTo( 23 );
        assertThat( res.getDateTime().getMinuteOfHour() ).isEqualTo( 34 );
        assertThat( res.getDateTime().getSecondOfMinute() ).isEqualTo( 0 );
        assertThat( res.getDateTime().getMillisOfSecond() ).isEqualTo( 0 );
        assertThat( res.getDateTime().getZone().getOffset( null ) ).isEqualTo( 3600000 );
    }

    @Test
    @WithClasses(XmlGregorianCalendarToDateTime.class)
    public void shouldMapXmlGregorianCalendarWithoutSecondsAndTimeZoneToDateTimeWithDefaultTimeZone() throws Exception {

        XmlGregorianCalendarBean in = new XmlGregorianCalendarBean();
        XMLGregorianCalendar xcal = DatatypeFactory.newInstance().newXMLGregorianCalendar();
        xcal.setYear( 1999 );
        xcal.setMonth( 5 );
        xcal.setDay( 25 );
        xcal.setHour( 23 );
        xcal.setMinute( 34 );
        in.setxMLGregorianCalendar( xcal );

        DateTimeBean res = XmlGregorianCalendarToDateTime.INSTANCE.toDateTimeBean( in );
        assertThat( res.getDateTime().getYear() ).isEqualTo( 1999 );
        assertThat( res.getDateTime().getMonthOfYear() ).isEqualTo( 5 );
        assertThat( res.getDateTime().getDayOfMonth() ).isEqualTo( 25 );
        assertThat( res.getDateTime().getHourOfDay() ).isEqualTo( 23 );
        assertThat( res.getDateTime().getMinuteOfHour() ).isEqualTo( 34 );
        assertThat( res.getDateTime().getSecondOfMinute() ).isEqualTo( 0 );
        assertThat( res.getDateTime().getMillisOfSecond() ).isEqualTo( 0 );
        assertThat( res.getDateTime().getZone().getID() ).isEqualTo( TimeZone.getDefault().getID() );
    }

    @Test
    @WithClasses(XmlGregorianCalendarToDateTime.class)
    public void shouldNotMapXmlGregorianCalendarWithoutMinutes() throws Exception {

        XmlGregorianCalendarBean in = new XmlGregorianCalendarBean();
        XMLGregorianCalendar xcal = DatatypeFactory.newInstance().newXMLGregorianCalendar();
        xcal.setYear( 1999 );
        xcal.setMonth( 5 );
        xcal.setDay( 25 );
        xcal.setHour( 23 );
        in.setxMLGregorianCalendar( xcal );

        DateTimeBean res = XmlGregorianCalendarToDateTime.INSTANCE.toDateTimeBean( in );
        assertThat( res.getDateTime() ).isNull();
    }


    @Test
    @WithClasses({DateTimeToXmlGregorianCalendar.class, XmlGregorianCalendarToDateTime.class})
    public void shouldMapRoundTrip() {

        DateTimeBean  dtb1 = new DateTimeBean();
        DateTime dt = new DateTime(2010, 1, 15, 1, 1, 1, 100, DateTimeZone.forOffsetHours( -1 ) );
        dtb1.setDateTime( dt );
        XmlGregorianCalendarBean xcb1 = DateTimeToXmlGregorianCalendar.INSTANCE.toXmlGregorianCalendarBean( dtb1 );
        DateTimeBean dtb2 = XmlGregorianCalendarToDateTime.INSTANCE.toDateTimeBean( xcb1 );
        XmlGregorianCalendarBean xcb2 = DateTimeToXmlGregorianCalendar.INSTANCE.toXmlGregorianCalendarBean( dtb2 );

        assertThat( dtb1.getDateTime() ).isEqualTo( dtb2.getDateTime() );
        assertThat( xcb1.getxMLGregorianCalendar() ).isEqualTo( xcb2.getxMLGregorianCalendar() );

    }

}