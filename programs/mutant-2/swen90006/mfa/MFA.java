package swen90006.mfa;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * An implementation of a multi-factor (two-factor) authentication
 * server API,
 * (https://en.wikipedia.org/wiki/Multi-factor_authentication) with
 * access to generic data.
 *
 * When a user registers, they provide a username and password, and
 * can optionally added a physical device. If they add a physical
 * device, then when they log in, a push will be sent to the device to
 * verify that the user has possession of the device (two-factor
 * authentication).
 *
 * If the user does not register a device, only single-factor
 * authentication is used.
 *
 * At any point in time, a user is correctly authenticated if and only
 * if:
 *
 * - They are authenticated to authentication status SINGLE and
 *   have not registered a device;
 *
 * or
 *
 * - They are authenticated to authentifcation status DOUBLE, have
 *   registed a device, and have validated they have access to the
 *   device (responded to a push notification)
 *
 * Once logged in, a user can access their own data, as described below,
 * provided that they are correctly authenticated.
 *
 * The data is simply an unbounded list of records, with each record being an
 * unbounded list of integers.
 *
 * The server code is below. For simplicity for the assignment, the
 * data storage is implemented as a Java data structure, and the push
 * notification to the device ID is not implemented.
 */
public class MFA
{
    /** The minimum length of a username */
    public final static int MINIMUM_USERNAME_LENGTH = 4;

    /** The minimum length of a password */
    public final static int MINIMUM_PASSWORD_LENGTH = 8;

    /**
     * The authentication status of a user: not authenticated,
     * single-factor only, or double-factor.
     */
    public enum AuthenticationStatus {NONE, SINGLE, DOUBLE};

    //The passwords for all users (non encrypted!!!)
    //I'm not claiming this code maintains privacy! :)
    private Map<String, String> passwords;

    //The device ID that a push notification is sent to
    //for each user
    private Map<String, String> deviceIDs;

    //The data stored for each user: a list of lists
    private Map<String, List<List<Integer>>> data;

    //The authentication status of each user
    private Map<String, AuthenticationStatus> authenticationStatus;

    /**
     * Constructs a new MFA server with no users
     */
    public MFA()
    {
	passwords = new HashMap<String, String>();
        deviceIDs = new HashMap<String, String>();
	data = new HashMap<String, List<List<Integer>>>();
        authenticationStatus = new HashMap<String, AuthenticationStatus>();
    }

    /**
     * Registers a new user with authentication status NONE and with
     * empty data records.
     *
     * The username must be at least four characters long and contain
     * only lower and upper-cass letters
     *
     * The password must conform to the following requirements:
     *  - Must be at least eight characters long
     *  - Must contain at least one letter (a-z, A-Z)
     *  - Must contain at least one digit (0-9)
     *  - Must contain at least one special  character (that is, other than a-z, A-Z, or 0-9)
     *
     * @param username   the username for the user to be added
     * @param password   the password for the user
     * @param deviceID   the physical device used for two-factor authentication
     * @throws DuplicateUserException    if the username is already registered
     * @throws InvalidUsernameException  if the username does not fit
     *          the requirements
     * @throws InvalidPasswordException  if the password does not fit
     *          the requirements
     *
     * Assumption: username and password are non-null
     */
    public void register(String username, String password, String deviceID)
	throws DuplicateUserException, InvalidUsernameException, InvalidPasswordException
    {
	//Check if this user exists
	if (passwords.containsKey(username)) {
	    throw new DuplicateUserException(username);
	}
	//Check that the username and password are long enough
	else if (username.length() < MINIMUM_USERNAME_LENGTH) {
	    throw new InvalidUsernameException(username);
	}
        else if (password.length() < MINIMUM_PASSWORD_LENGTH) {
            throw new InvalidPasswordException(password);
        }
	else {
	    //check the username contains only lower- and upper-case letters
	    for (char c : username.toCharArray()) {
		if (!('a' <= c && c <= 'z' || 'A' <= c)) {    /* Conditional Operator Replacement */
		    throw new InvalidUsernameException(username);
		}
	    }

	    //check the password contains at least one special character
	    boolean letter = false;
	    boolean digit = false;
            boolean special = false;
	    for (char c : password.toCharArray()) {
		if ('a' <= c && c <= 'z' || 'A' <= c && c <= 'Z') {
		    letter = true;
		}
		else if ('0' <= c && c <= '9') {
		    digit = true;
		}
		else {
                    special = true;
		}
	    }
            if (!(letter && digit && special)) {
                throw new InvalidPasswordException(password);
            }
	}

	passwords.put(username, password);
        data.put(username, new ArrayList<List<Integer>>());
	authenticationStatus.put(username, AuthenticationStatus.NONE);

	if (deviceID != null) {
	    deviceIDs.put(username, deviceID);
	}
    }

    /**
     * Checks if a user exists
     * @param username  the username
     * @return  true if and only if this user is registered
     *
     * Assumption: username is non-null
     */
    public boolean isUser(String username)
    {
	return passwords.containsKey(username);
    }

    /**
     * Logs a user in using their username and password, returning
     * their authentication status.
     *
     * If the user has registered a device, send a push notification
     * to that device.
     *
     * @param username   the username
     * @param password   the password
     *
     * @return NONE if and only if the username does not exist or the
     *           password is incorrect
     *         SINGLE if and only if the username and password are correct
     *
     * Assumption: username, password, and steps are non-null
     */
    public AuthenticationStatus login(String username, String password)
	throws NoSuchUserException, IncorrectPasswordException
    {
	if (checkUsernamePassword(username, password)) {
	    authenticationStatus.put(username, AuthenticationStatus.SINGLE);

	    //check whether two-factor authentication is required
	    if (deviceIDs.get(username) != null) {
		sendPushNotification(username, deviceIDs.get(username));
	    }
	}
	return authenticationStatus.get(username);
    }

    /**
     * Simulates a user responding to a push notification.  If the
     * correct deviceID is used and the user is already single-factor
     * authenticated, then the user's authentication states becomes
     * DOUBLE.
     *
     * If the user's current authenication status is NONE or DOUBLE, the response is ignored.
     *
     * @param username  the username
     * @param deviceID  the physical device ID the response came from
     *
     * @throws  NoSuchUserException if the user does not have an account
     * @throws  IncorrectDeviceIDException if the password is incorrect for this user
     *
     * Assumption: username and deviceID are non-null
     */
    public AuthenticationStatus respondToPushNotification(String username, String deviceID)
	throws NoSuchUserException, IncorrectDeviceIDException
    {
	if (!isUser(username)) {
	    throw new NoSuchUserException(username);
	}
	else if (authenticationStatus.get(username) == AuthenticationStatus.SINGLE) {
	    if (deviceIDs.get(username) != null && deviceIDs.get(username) != deviceID) {
		throw new IncorrectDeviceIDException(username, deviceID);
	    }
	    else if (deviceIDs.get(username) != null) {
		authenticationStatus.put(username, AuthenticationStatus.DOUBLE);
	    }
	}
	return authenticationStatus.get(username);
    }

    /**
     * @param username the username
     * @return true if and only if the user is correctly authenticated
     *
     * Assumption: username is non-null
     */
    public boolean isAuthenticated(String username)
	throws NoSuchUserException
    {
	if (!isUser(username)) {
	    throw new NoSuchUserException(username);
	}
	else if ((deviceIDs.get(username) == null &&
		  authenticationStatus.get(username) == AuthenticationStatus.SINGLE)
		 ||
		 (deviceIDs.get(username) != null &&
		  authenticationStatus.get(username) == AuthenticationStatus.DOUBLE)) {
	    return true;
	}
	else {
	    return false;
	}
    }

    /**
     * Add a new record data for a correctly authenticated user. 
     * The record is added to the end of the list of records.
     *
     * @throws  NoSuchUserException if the user does not have an account
     * @throws  UnauthenticatedUserException if the user is not
     *          correctly authenticated
     *
     * @param username  the username
     * @param record    a list of integers, the record to be added
     *
     * Assumption: username and record are non-null
     *
     */
    public void addData(String username, List<Integer> record)
        throws NoSuchUserException, UnauthenticatedUserException
    {
	if (!isAuthenticated(username)) {
	    throw new UnauthenticatedUserException(username);
	}
	else {
	    //Add the new record
	    data.get(username).add(record);
	}
    }

    /**
     * Read a record for a user if they are correctly authenticated.
     * The record at 'index' is returned.
     * The data is unchanged.
     *
     * @param   username the username
     * @param   index    the index of the record to be read
     *
     * @throws  NoSuchUserException if either user ('username' or 'user') does not have an account
     * @throws  UnauthenticatedUserException if the user is not
     *          correctly authenticated
     * @throws  A java.lang.OutOfBoundsException if index is not in [0, len(data)-1]
     *
     * Assumption: username is are non-null
     */
    public List<Integer> getData(String username, int index)
	throws NoSuchUserException, UnauthenticatedUserException
    {
	if (!isAuthenticated(username)) {
	    throw new UnauthenticatedUserException(username);
	}
	else {
	    return data.get(username).get(index);
	}
    }

    /**
     * Check a username and password combination, returning true if the
     * username and password are correct and throwing an exception otherwise.
     *
     * @throws  NoSuchUserException if the user does not have an account
     * @throws  IncorrectPasswordException if the password is incorrect for this user
     */
    private boolean checkUsernamePassword(String username, String password)
	throws NoSuchUserException, IncorrectPasswordException
    {
        //Check that the user exists
	if (!isUser(username)) {
	    throw new NoSuchUserException(username);
	}
	//Check the password
	else if (!passwords.get(username).equals(password)) {
	    throw new IncorrectPasswordException(username, password);
	}

        return true;
    }

    /**
     * Send a push notification to a user who has provided the correct password.
     *
     */
    void sendPushNotification(String username, String deviceID)
    {
	//Not implemented
    }
}
