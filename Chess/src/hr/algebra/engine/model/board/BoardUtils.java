/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.engine.model.board;

/**
 *
 * @author fran
 */
public class BoardUtils {

    private BoardUtils() {
        throw new RuntimeException("you cannnot instanciate me!");
    }
    
    public final static int TILE_NUMBER = 64;
    public final static int TILE_NUMBER_PER_ROW = 8;
    
    public static int[] getCoordinateAtPosition(int position) {
        String toOctalString = Integer.toOctalString(position);
        return new int[] {toOctalString.length() == 1 ? 
                0 : 
                Character.getNumericValue(toOctalString.charAt(0)), 
                toOctalString.length() == 1 ? 
                Character.getNumericValue(toOctalString.charAt(0)) : 
                Character.getNumericValue(toOctalString.charAt(1))};
    }
    
}
