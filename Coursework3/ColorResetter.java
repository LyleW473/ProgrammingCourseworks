/**
 * Functional interface defined for specifying a custom reset (lambda) function to reset a cell's color.
 * Applies the reset function to the target cell that is passed in as a parameter.
 * 
 * @author K22039642 Gee-Lyle Wong, k22015880 Vatsal Patel 
 */

@FunctionalInterface
interface ColorResetter {
    void resetColor(NonImmuneCell cell);
}
