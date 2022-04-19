 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.jndi;

import java.util.Hashtable;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;

/**
 *
 * @author fran
 */
public class InitialDirContextCloseable extends InitialDirContext implements AutoCloseable{

    public InitialDirContextCloseable(Hashtable< ?, ?> enviroment) throws NamingException {
        super(enviroment);
    }
    
    
}
