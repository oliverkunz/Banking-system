package backend.api;

import java.io.Serializable;

/**
 * Whether transaction is a standard or interests transaction
 * 
 * @author fkg
 *
 */
public enum TransactionType implements Serializable {
    NORMAL, INTERESTS
}
