/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.nhinclib;

import java.util.List;

/**
 *
 * @author rayj
 */
public class NullChecker {

    public static boolean isNullish(String value) {
        boolean result = false;
        if ((value == null) || (value.contentEquals(""))) {
            result = true;
        }
        return result;
    }
    public static boolean isNotNullish(String value) {
        return (!isNullish(value));
    }

    public static boolean isNullish(List value) {
        boolean result = false;
        if ((value == null) || (value.size() == 0)) {
            result = true;
        }
        return result;
    }
    public static boolean isNotNullish(List value) {
        return (!isNullish(value));
    }


}