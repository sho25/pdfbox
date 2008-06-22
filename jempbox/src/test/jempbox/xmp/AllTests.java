begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
package|package
name|test
operator|.
name|jempbox
operator|.
name|xmp
package|;
end_package

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestSuite
import|;
end_import

begin_comment
comment|/**  * Suite for all tests in test.jempbox.xmp.  *   * @author $Author: coezbek $  * @version $Revision: 1.1 $ ($Date: 2006/12/30 17:27:46 $)  *   */
end_comment

begin_class
specifier|public
class|class
name|AllTests
block|{
comment|/**      * Hide constructor.      */
specifier|protected
name|AllTests
parameter_list|()
block|{     }
comment|/**      * Method returns a test representing all tests in the package      * test.jempbox.xmp.      *       * @return The test representing all tests in the current package.      */
specifier|public
specifier|static
name|Test
name|suite
parameter_list|()
block|{
name|TestSuite
name|suite
init|=
operator|new
name|TestSuite
argument_list|(
literal|"Test for test.jempbox.xmp"
argument_list|)
decl_stmt|;
comment|// $JUnit-BEGIN$
name|suite
operator|.
name|addTestSuite
argument_list|(
name|XMPSchemaTest
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// $JUnit-END$
return|return
name|suite
return|;
block|}
block|}
end_class

end_unit

