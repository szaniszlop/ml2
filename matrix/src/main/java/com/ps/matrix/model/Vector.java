package com.ps.matrix.model;

public interface Vector  extends Tensor, Result{
	/**
	 * Returns the scalar value from the i-th index
	 * @param i index
	 * @return Scalar value from the i-th index
	 * The implementation may or may not raise an exception in case there is no value on the specified index
	 */
	public Scalar getItem(int i);
	
	/**
	 * Sets the item on the i-th index to a specified value
	 * @param index 
	 * @param value
	 * @return the value that vas on this place originaly or null if there was no value specified before
	 */
	public Scalar setItem(int index, Scalar value);
	
	/**
	 * Adds a new value to the end of the vector
	 * @param value
 	 * @return the new number of items in the vector
	 */
	public int addItem(Scalar value);
	
	/**
	 * Returns the number of items in the vector
	 * @return number of items stored in the vector starting with 0 for the first item
	 */
	public int getNumItems();	
	
	
}
