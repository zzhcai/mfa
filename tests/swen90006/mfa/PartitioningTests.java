package swen90006.mfa;

import org.junit.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PartitioningTests
{
    protected MFA mfa;

    //Any method annotated with "@Before" will be executed before each test,
    //allowing the tester to set up some shared resources.
    @Before public void setUp()
    throws DuplicateUserException, InvalidUsernameException, InvalidPasswordException
    {
        mfa = new MFA();
        mfa.register("UserNameA", "Password1!", "");
    }

    //Any method annotated with "@After" will be executed after each test,
    //allowing the tester to release any shared resources used in the setup.
    @After public void tearDown()
    {
    }

    /*
        ///////////////////////////////////////////////////////////////////////
        /////////////////  register equivalence partitioning  /////////////////
        ///////////////////////////////////////////////////////////////////////

    */
    @Test(expected = InvalidUsernameException.class)
    public void registerEC1() throws Throwable
    {
        String username = "me";
        String password = "@un1Me16";
        String deviceID = null;
        mfa.register(username, password, deviceID);
        assertFalse(mfa.isUser(username));
    }

    @Test(expected = InvalidUsernameException.class)
    public void registerEC2() throws Throwable
    {
        String username = "\n \\\"a[-1]";
        String password = "@un1Me16";
        String deviceID = null;
        mfa.register(username, password, deviceID);
        assertFalse(mfa.isUser(username));
    }

    @Test(expected = InvalidPasswordException.class)
    public void registerEC3() throws Throwable
    {
        String username = "student";
        String password = "@u0M";
        String deviceID = null;
        mfa.register(username, password, deviceID);
    }

    @Test(expected = InvalidPasswordException.class)
    public void registerEC4() throws Throwable
    {
        String username = "student";
        String password = "@2022.8.18";
        String deviceID = null;
        mfa.register(username, password, deviceID);
    }

    @Test(expected = InvalidPasswordException.class)
    public void registerEC5() throws Throwable
    {
        String username = "student";
        String password = "@unimelb";
        String deviceID = null;
        mfa.register(username, password, deviceID);
    }

    @Test(expected = InvalidPasswordException.class)
    public void registerEC6() throws Throwable
    {
        String username = "student";
        String password = "ATun1me16";
        String deviceID = null;
        mfa.register(username, password, deviceID);
    }

    @Test(expected = DuplicateUserException.class)
    public void registerEC7() throws Throwable
    {
        String username = "student";
        String password = "@un1Me16";
        String deviceID = null;
        mfa.register(username, password, deviceID);
        mfa.register(username, password, deviceID);
        assertTrue(mfa.isUser(username));
    }

    @Test
    public void registerEC8() throws Throwable
    {
        String username = "student";
        String password = "@un1Me16";
        String deviceID = null;
        mfa.register(username, password, deviceID);
        assertTrue(mfa.isUser(username));
    }

    @Test
    public void registerEC9() throws Throwable
    {
        String username = "student";
        String password = "@un1Me16";
        String deviceID = "iphone$$$";
        mfa.register(username, password, deviceID);
        assertTrue(mfa.isUser(username));
    }

    /*
        ///////////////////////////////////////////////////////////////////////
        //////////////////  login equivalence partitioning  ///////////////////
        ///////////////////////////////////////////////////////////////////////

    */
    @Test(expected = NoSuchUserException.class)
    public void loginEC1() throws Throwable
    {
        String username = "student";
        String password = "@un1Me16";
        MFA.AuthenticationStatus status = mfa.login(username, password);
        assertEquals(MFA.AuthenticationStatus.NONE, status);
    }

    @Test(expected = IncorrectPasswordException.class)
    public void loginEC2() throws Throwable
    {
        String username = "UserNameA";
        String password = "Password2!";
        MFA.AuthenticationStatus status = mfa.login(username, password);
        assertEquals(MFA.AuthenticationStatus.NONE, status);
    }

    @Test
    public void loginEC3() throws Throwable
    {
        String username = "UserNameA";
        String password = "Password1!";
        MFA.AuthenticationStatus status = mfa.login(username, password);
        assertEquals(MFA.AuthenticationStatus.SINGLE, status);
    }

    @Test
    public void loginEC4() throws Throwable
    {
        String username = "student";
        String password = "@un1Me16";
        mfa.register(username, password, null);
        MFA.AuthenticationStatus status = mfa.login(username, password);
        assertEquals(MFA.AuthenticationStatus.SINGLE, status);
    }

    /*
        ///////////////////////////////////////////////////////////////////////
        ////////  respondToPushNotification equivalence partitioning  /////////
        ///////////////////////////////////////////////////////////////////////

    */
    @Test(expected = NoSuchUserException.class)
    public void respondToPushNotificationEC1() throws Throwable
    {
        String username = "student";
        String deviceID = "";
        mfa.respondToPushNotification(username, deviceID);
    }

    /**
     * Cannot test respondToPushNotificationEC2 here,
     * because we have no write access to the private variable authenticationStatus
     */

    @Test
    public void respondToPushNotificationEC3() throws Throwable
    {
        String username = "UserNameA";
        String deviceID = "";
        MFA.AuthenticationStatus status = mfa.respondToPushNotification(username, deviceID);
        assertEquals(MFA.AuthenticationStatus.NONE, status);
    }

    @Test
    public void respondToPushNotificationEC4() throws Throwable
    {
        String username = "UserNameA";
        String deviceID = "wrong-device";
        mfa.login(username, "Password1!");
        mfa.respondToPushNotification(username, "");
        MFA.AuthenticationStatus status = mfa.respondToPushNotification(username, deviceID);
        assertEquals(MFA.AuthenticationStatus.DOUBLE, status);
    }

    @Test
    public void respondToPushNotificationEC5() throws Throwable
    {
        String username = "student";
        String deviceID = "fake-device";
        mfa.register(username, "@un1Me16", null);
        mfa.login(username, "@un1Me16");
        MFA.AuthenticationStatus status = mfa.respondToPushNotification(username, deviceID);
        assertEquals(MFA.AuthenticationStatus.SINGLE, status);
    }
    
    @Test(expected = IncorrectDeviceIDException.class)
    public void respondToPushNotificationEC6() throws Throwable
    {
        String username = "UserNameA";
        String deviceID = "wrong-device";
        mfa.login(username, "Password1!");
        MFA.AuthenticationStatus status = mfa.respondToPushNotification(username, deviceID);
        assertEquals(MFA.AuthenticationStatus.SINGLE, status);
    }

    @Test
    public void respondToPushNotificationEC7() throws Throwable
    {
        String username = "UserNameA";
        String deviceID = "";
        mfa.login(username, "Password1!");
        MFA.AuthenticationStatus status = mfa.respondToPushNotification(username, deviceID);
        assertEquals(MFA.AuthenticationStatus.DOUBLE, status);
    }

    /*
        ///////////////////////////////////////////////////////////////////////
        /////////////////  getData equivalence partitioning  //////////////////
        ///////////////////////////////////////////////////////////////////////

    */
    @Test(expected = NoSuchUserException.class)
    public void getDataEC1() throws Throwable
    {
        String username = "student";
        int index = 0;
        mfa.getData(username, index);
    }

    @Test(expected = UnauthenticatedUserException.class)
    public void getDataEC2() throws Throwable
    {
        String username = "UserNameA";
        int index = 0;
        mfa.getData(username, index);
        assertFalse(mfa.isAuthenticated(username));
    }

    @Test(expected = UnauthenticatedUserException.class)
    public void getDataEC3() throws Throwable
    {
        String username = "student";
        int index = 0;
        mfa.register(username, "@un1Me16", null);
        mfa.getData(username, index);
        assertFalse(mfa.isAuthenticated(username));
    }

    @Test(expected = UnauthenticatedUserException.class)
    public void getDataEC4() throws Throwable
    {
        String username = "UserNameA";
        int index = 0;
        mfa.login(username, "Password1!");
        mfa.getData(username, index);
        assertFalse(mfa.isAuthenticated(username));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getDataEC5() throws Throwable
    {
        String username = "UserNameA";
        int index = 0;
        mfa.login(username, "Password1!");
        mfa.respondToPushNotification(username, "");
        mfa.getData(username, index);
    }

    @Test
    public void getDataEC6() throws Throwable
    {
        String username = "UserNameA";
        int index = 0;
        List<Integer> data = Arrays.asList(-1);
        mfa.login(username, "Password1!");
        mfa.respondToPushNotification(username, "");
        mfa.addData(username, data);
        List<Integer> data_ = mfa.getData(username, index);
        assertEquals(data, data_);
    }

    @Test
    public void getDataEC7() throws Throwable
    {
        String username = "UserNameA";
        int index = 0;
        List<Integer> data0 = Arrays.asList(-1,0,2,5,9);
        mfa.login(username, "Password1!");
        mfa.respondToPushNotification(username, "");
        mfa.addData(username, data0);
        for (int i = 0; i < 4; i++) {
            mfa.addData(username, new ArrayList<>());
        }
        List<Integer> data0_ = mfa.getData(username, index);
        assertEquals(data0, data0_);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getDataEC8() throws Throwable
    {
        String username = "student";
        int index = -1;
        List<Integer> data = Arrays.asList(-1,0,2,5,9);
        mfa.register(username, "@un1Me16", null);
        mfa.login(username, "@un1Me16");
        mfa.addData(username, data);
        mfa.getData(username, index);
    }

    @Test
    public void getDataEC9() throws Throwable
    {
        String username = "student";
        int index = 3;
        List<Integer> data3 = Arrays.asList(-1,0,2,5,9);
        mfa.register(username, "@un1Me16", null);
        mfa.login(username, "@un1Me16");
        mfa.addData(username, new ArrayList<>());
        mfa.addData(username, new ArrayList<>());
        mfa.addData(username, new ArrayList<>());
        mfa.addData(username, data3);
        mfa.addData(username, new ArrayList<>());
        List<Integer> data3_ = mfa.getData(username, index);
        assertEquals(data3, data3_);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getDataEC10() throws Throwable
    {
        String username = "student";
        int index = 5;
        List<Integer> data = Arrays.asList(-1,0,2,5,9);
        mfa.register(username, "@un1Me16", null);
        mfa.login(username, "@un1Me16");
        mfa.addData(username, data);
        mfa.getData(username, index);
    }

}
