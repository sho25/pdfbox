begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|xmpbox
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Calendar
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * Test the date conversion utility.  *  */
end_comment

begin_class
specifier|public
class|class
name|DateConverterTest
block|{
comment|/**      * Test several ISO6801 date formats.      *       * Test with additional time zone      * information normally not supported by ISO8601      *      * @throws Exception when there is an exception      */
annotation|@
name|Test
specifier|public
name|void
name|testDateConversion
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|SimpleDateFormat
name|dateFormat
init|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"YYYY-MM-dd'T'HH:mm.ss.SSSXXX"
argument_list|)
decl_stmt|;
name|Calendar
name|jaxbCal
init|=
literal|null
decl_stmt|,
name|convDate
init|=
literal|null
decl_stmt|;
comment|// Test partial dates
name|convDate
operator|=
name|DateConverter
operator|.
name|toCalendar
argument_list|(
literal|"2015-02-02"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2015
argument_list|,
name|convDate
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|YEAR
argument_list|)
argument_list|)
expr_stmt|;
comment|// Test some time zone offsets
name|jaxbCal
operator|=
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|DatatypeConverter
operator|.
name|parseDateTime
argument_list|(
literal|"2015-02-02T16:37:19.192Z"
argument_list|)
expr_stmt|;
name|convDate
operator|=
name|DateConverter
operator|.
name|toCalendar
argument_list|(
literal|"2015-02-02T16:37:19.192Z"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|dateFormat
operator|.
name|format
argument_list|(
name|jaxbCal
operator|.
name|getTime
argument_list|()
argument_list|)
argument_list|,
name|dateFormat
operator|.
name|format
argument_list|(
name|convDate
operator|.
name|getTime
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|jaxbCal
operator|=
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|DatatypeConverter
operator|.
name|parseDateTime
argument_list|(
literal|"2015-02-02T16:37:19.192+00:00"
argument_list|)
expr_stmt|;
name|convDate
operator|=
name|DateConverter
operator|.
name|toCalendar
argument_list|(
literal|"2015-02-02T16:37:19.192Z"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|dateFormat
operator|.
name|format
argument_list|(
name|jaxbCal
operator|.
name|getTime
argument_list|()
argument_list|)
argument_list|,
name|dateFormat
operator|.
name|format
argument_list|(
name|convDate
operator|.
name|getTime
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|jaxbCal
operator|=
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|DatatypeConverter
operator|.
name|parseDateTime
argument_list|(
literal|"2015-02-02T16:37:19.192+02:00"
argument_list|)
expr_stmt|;
name|convDate
operator|=
name|DateConverter
operator|.
name|toCalendar
argument_list|(
literal|"2015-02-02T16:37:19.192+02:00"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|dateFormat
operator|.
name|format
argument_list|(
name|jaxbCal
operator|.
name|getTime
argument_list|()
argument_list|)
argument_list|,
name|dateFormat
operator|.
name|format
argument_list|(
name|convDate
operator|.
name|getTime
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|jaxbCal
operator|=
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|DatatypeConverter
operator|.
name|parseDateTime
argument_list|(
literal|"2015-02-02T16:37:19.192Z"
argument_list|)
expr_stmt|;
name|convDate
operator|=
name|DateConverter
operator|.
name|toCalendar
argument_list|(
literal|"2015-02-02T08:37:19.192PST"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|dateFormat
operator|.
name|format
argument_list|(
name|jaxbCal
operator|.
name|getTime
argument_list|()
argument_list|)
argument_list|,
name|dateFormat
operator|.
name|format
argument_list|(
name|convDate
operator|.
name|getTime
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|jaxbCal
operator|=
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|DatatypeConverter
operator|.
name|parseDateTime
argument_list|(
literal|"2015-02-02T16:37:19.192+01:00"
argument_list|)
expr_stmt|;
name|convDate
operator|=
name|DateConverter
operator|.
name|toCalendar
argument_list|(
literal|"2015-02-02T16:37:19.192Europe/Berlin"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|dateFormat
operator|.
name|format
argument_list|(
name|jaxbCal
operator|.
name|getTime
argument_list|()
argument_list|)
argument_list|,
name|dateFormat
operator|.
name|format
argument_list|(
name|convDate
operator|.
name|getTime
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

