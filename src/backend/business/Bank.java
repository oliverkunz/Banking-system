package backend.business;

import backend.api.ATM;
import backend.api.Administration;
import backend.api.Banking;

/**
 * Common interface used internally between banks
 * 
 * @author fkg
 *
 */
public interface Bank extends ATM, Administration, Banking {

}
