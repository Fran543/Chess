/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.packets;

import java.io.Serializable;

/**
 *
 * @author fran
 */
public class ErrorPacket implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public String message;

    public ErrorPacket(String message) {
        this.message = message;
    }
}
