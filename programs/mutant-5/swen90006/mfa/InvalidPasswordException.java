package swen90006.mfa;

public class InvalidPasswordException extends Exception
{
    public InvalidPasswordException(String password)
    {
        super("Password " + password + " does not comply with the requirements\n" +
	      "\t- must contains at least " +
	      MFA.MINIMUM_PASSWORD_LENGTH + " characters,\n" +
	      " at least one letter (a-z, A-Z),\n" +
	      " at least one digit (0-9), and \n" + 
              " at least one special character that is not a letter or digit");
    }
}
