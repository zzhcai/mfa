package swen90006.mfa;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.FileSystems;

import org.junit.*;
import static org.junit.Assert.*;

//By extending PartitioningTests, we inherit the tests from that class
public class BoundaryTests extends PartitioningTests
{
    /*
        ///////////////////////////////////////////////////////////////////////
        /////////////////  register boundary-value analysis  //////////////////
        ///////////////////////////////////////////////////////////////////////

    */
    @Test(expected = InvalidUsernameException.class)
    public void registerBVA1() throws Throwable
    {
        String username = "you";
        String password = "abc123@{";
        String deviceID = null;
        mfa.register(username, password, deviceID);
        assertFalse(mfa.isUser(username));
    }

    @Test(expected = InvalidUsernameException.class)
    public void registerBVA2_1() throws Throwable
    {
        String username = "@AZaz";
        String password = "abc123@{";
        String deviceID = null;
        mfa.register(username, password, deviceID);
        assertFalse(mfa.isUser(username));
    }

    @Test(expected = InvalidUsernameException.class)
    public void registerBVA2_2() throws Throwable
    {
        String username = "A[Zaz";
        String password = "abc123@{";
        String deviceID = null;
        mfa.register(username, password, deviceID);
        assertFalse(mfa.isUser(username));
    }

    @Test(expected = InvalidUsernameException.class)
    public void registerBVA2_3() throws Throwable
    {
        String username = "AZ`az";
        String password = "abc123@{";
        String deviceID = null;
        mfa.register(username, password, deviceID);
        assertFalse(mfa.isUser(username));
    }

    @Test(expected = InvalidUsernameException.class)
    public void registerBVA2_4() throws Throwable
    {
        String username = "AZa{z";
        String password = "abc123@{";
        String deviceID = null;
        mfa.register(username, password, deviceID);
        assertFalse(mfa.isUser(username));
    }

    @Test(expected = InvalidPasswordException.class)
    public void registerBVA3() throws Throwable
    {
        String username = "student";
        String password = "abc123@";
        String deviceID = null;
        mfa.register(username, password, deviceID);
    }

    @Test(expected = InvalidPasswordException.class)
    public void registerBVA4_1() throws Throwable
    {
        String username = "student";
        String password = "@0123459";
        String deviceID = null;
        mfa.register(username, password, deviceID);
    }

    @Test(expected = InvalidPasswordException.class)
    public void registerBVA4_2() throws Throwable
    {
        String username = "student";
        String password = "0678[`{9";
        String deviceID = null;
        mfa.register(username, password, deviceID);
    }

    @Test(expected = InvalidPasswordException.class)
    public void registerBVA5_1() throws Throwable
    {
        String username = "student";
        String password = "/:A!@[`{";
        String deviceID = null;
        mfa.register(username, password, deviceID);
    }

    @Test(expected = InvalidPasswordException.class)
    public void registerBVA5_2() throws Throwable
    {
        String username = "student";
        String password = "/:[`{Zaz";
        String deviceID = null;
        mfa.register(username, password, deviceID);
    }

    @Test(expected = InvalidPasswordException.class)
    public void registerBVA6_1() throws Throwable
    {
        String username = "student";
        String password = "0AaaZZzz";
        String deviceID = null;
        mfa.register(username, password, deviceID);
    }

    @Test(expected = InvalidPasswordException.class)
    public void registerBVA6_2() throws Throwable
    {
        String username = "student";
        String password = "99999999A";
        String deviceID = null;
        mfa.register(username, password, deviceID);
    }

    @Test(expected = DuplicateUserException.class)
    public void registerBVA7() throws Throwable
    {
        String username = "student";
        String password = "abc123@{";
        String deviceID = null;
        mfa.register(username, password, deviceID);
        mfa.register(username, password, deviceID);
        assertTrue(mfa.isUser(username));
    }
    
    @Test
    public void registerBVA8() throws Throwable
    {
        String username = "student";
        String password = "abc123@{";
        String deviceID = null;
        mfa.register(username, password, deviceID);
        assertTrue(mfa.isUser(username));
    }

    @Test
    public void registerBVA9() throws Throwable
    {
        String username = "student";
        String password = "abc123@{";
        String deviceID = "iphone$$$";
        mfa.register(username, password, deviceID);
        assertTrue(mfa.isUser(username));
    }

    /*
        ///////////////////////////////////////////////////////////////////////
        ///////////////////  login boundary-value analysis  ///////////////////
        ///////////////////////////////////////////////////////////////////////

    */
    @Test(expected = NoSuchUserException.class)
    public void loginBVA1() throws Throwable
    {
        String username = "student";
        String password = "abc123@{";
        MFA.AuthenticationStatus status = mfa.login(username, password);
        assertEquals(MFA.AuthenticationStatus.NONE, status);
    }

    @Test(expected = IncorrectPasswordException.class)
    public void loginBVA2() throws Throwable
    {
        loginEC2();
    }

    @Test
    public void loginBVA3() throws Throwable
    {
        loginEC3();
    }

    @Test
    public void loginBVA4() throws Throwable
    {
        String username = "student";
        String password = "abc123@{";
        mfa.register(username, password, null);
        MFA.AuthenticationStatus status = mfa.login(username, password);
        assertEquals(MFA.AuthenticationStatus.SINGLE, status);
    }

    /*
        ///////////////////////////////////////////////////////////////////////
        /////////  respondToPushNotification boundary-value analysis  /////////
        ///////////////////////////////////////////////////////////////////////

    */
    @Test(expected = NoSuchUserException.class)
    public void respondToPushNotificationBVA1() throws Throwable
    {
        respondToPushNotificationEC1();
    }

    /**
     * Cannot test respondToPushNotificationBVA2 here,
     * because we have no write access to the private variable authenticationStatus
     */

    @Test
    public void respondToPushNotificationBVA3() throws Throwable
    {
        respondToPushNotificationEC3();
    }

    @Test
    public void respondToPushNotificationBVA4() throws Throwable
    {
        String username = "UserNameA";
        String deviceID = "";
        mfa.login(username, "Password1!");
        mfa.respondToPushNotification(username, deviceID);
        MFA.AuthenticationStatus status = mfa.respondToPushNotification(username, deviceID);
        assertEquals(MFA.AuthenticationStatus.DOUBLE, status);
    }

    @Test
    public void respondToPushNotificationBVA5() throws Throwable
    {
        String username = "student";
        String deviceID = "iphone$$$";
        mfa.register(username, "abc123@{", null);
        mfa.login(username, "abc123@{");
        MFA.AuthenticationStatus status = mfa.respondToPushNotification(username, deviceID);
        assertEquals(MFA.AuthenticationStatus.SINGLE, status);
    }

    @Test(expected = IncorrectDeviceIDException.class)
    public void respondToPushNotificationBVA6() throws Throwable
    {
        respondToPushNotificationEC6();
    }

    @Test
    public void respondToPushNotificationBVA7() throws Throwable
    {
        respondToPushNotificationEC7();
    }

    /*
        ///////////////////////////////////////////////////////////////////////
        //////////////////  getData boundary-value analysis  //////////////////
        ///////////////////////////////////////////////////////////////////////

    */
    @Test(expected = NoSuchUserException.class)
    public void getDataBVA1() throws Throwable
    {
        getDataEC1();
    }

    @Test(expected = UnauthenticatedUserException.class)
    public void getDataBVA2() throws Throwable
    {
        getDataEC2();
    }

    @Test(expected = UnauthenticatedUserException.class)
    public void getDataBVA3() throws Throwable
    {
        String username = "student";
        int index = 0;
        mfa.register(username, "abc123@{", null);
        mfa.getData(username, index);
        assertFalse(mfa.isAuthenticated(username));
    }

    @Test(expected = UnauthenticatedUserException.class)
    public void getDataBVA4() throws Throwable
    {
        getDataEC4();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getDataBVA5() throws Throwable
    {
        getDataEC5();
    }

    @Test
    public void getDataBVA6() throws Throwable
    {
        getDataEC6();
    }

    @Test
    public void getDataBVA7() throws Throwable
    {
        String username = "UserNameA";
        int index = 1;
        List<Integer> data0 = new ArrayList<>();
        List<Integer> data1 = Arrays.asList(-1,4);
        mfa.login(username, "Password1!");
        mfa.respondToPushNotification(username, "");
        mfa.addData(username, data0);
        mfa.addData(username, data1);
        List<Integer> data1_ = mfa.getData(username, index);
        assertEquals(data1, data1_);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getDataBVA8() throws Throwable
    {
        String username = "student";
        int index = -1;
        List<Integer> data = new ArrayList<>();
        mfa.register(username, "abc123@{", null);
        mfa.login(username, "abc123@{");
        mfa.addData(username, data);
        mfa.getData(username, index);
    }

    @Test
    public void getDataBVA9() throws Throwable
    {
        String username = "student";
        int index = 0;
        List<Integer> data = new ArrayList<>();
        mfa.register(username, "abc123@{", null);
        mfa.login(username, "abc123@{");
        mfa.addData(username, data);
        List<Integer> data_ = mfa.getData(username, index);
        assertEquals(data, data_);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getDataBVA10() throws Throwable
    {
        String username = "student";
        int index = 1;
        List<Integer> data = new ArrayList<>();
        mfa.register(username, "abc123@{", null);
        mfa.login(username, "abc123@{");
        mfa.addData(username, data);
        mfa.getData(username, index);
    }

}
