package swen90006.mfa;

public class UnauthenticatedUserException extends Exception 
{
    public UnauthenticatedUserException(String username)
    {
        super("User " + username + " is not sufficiently authenticated");
    }
}
