package com.ps.matrix.component.simple;

import com.ps.matrix.component.generators.ZeroGenerator;
import com.ps.matrix.component.operators.DeferedOperatorFactory;
import com.ps.matrix.model.Matrix;
import com.ps.matrix.model.OperatorFactory;
import com.ps.matrix.model.Result;
import com.ps.matrix.model.Tensor;
import com.ps.matrix.model.ValueGenerator;
import com.ps.matrix.model.ValueType;
import com.ps.matrix.model.Vector;

public class SimpleMatrix extends BaseTensor implements Matrix {

	private double[] data;
	private int _rows, _columns;
	
	public SimpleMatrix(int rows, int columns, ValueGenerator generator, OperatorFactory of){
		super(of);
		init(rows, columns, generator);
	}

	public SimpleMatrix(int rows, int columns, OperatorFactory of) {
		this(rows, columns, new ZeroGenerator(), of);
	}
	
	public SimpleMatrix(OperatorFactory of){
		this(0,0, of);
	}
	
	public SimpleMatrix(int rows, int columns){
		this(rows, columns, new ZeroGenerator(), new DeferedOperatorFactory());
	}

	public SimpleMatrix(int rows, int columns, ValueGenerator generator) {
		this(rows, columns, generator, new DeferedOperatorFactory());
	}
	
	public SimpleMatrix(){
		this(0,0);
	}
	
	@Override
	public ValueType getType() {
		return ValueType.MATRIX;
	}

	@Override
	public double[] getRawData() {
		return data;
	}

	@Override
	public int[] getDimensions() {
		return new int[]{getNumRows(), getNumCols()};
	}

	@Override
	public Vector getColumn(int column) {
		assert(column < getNumCols());
		double[] colData = new double[getNumRows()];
		for(int i = 0 ; i < getNumRows() ; i++){
			colData[i] = this.data[i * getNumCols() + column];
		}
		return new SimpleVector(colData);
	}

	@Override
	public Vector getRow(int row) {
		assert(row < getNumRows());
		double[] rowData = new double[getNumCols()];
		System.arraycopy(this.data, row * getNumCols(), rowData, 0, getNumCols());
		return new SimpleVector(rowData);
	}

	@Override
	public void addRow(Vector row) {
		addRow(row.getRawData());
	}

	@Override
	public void addRow(double[] row) {
		assert(row.length == getNumCols());
		if(data.length <= getNumCols() * getNumRows() + row.length){
			double[] newData = new double[2 * (getNumCols() * getNumRows() + row.length) ];
			System.arraycopy(this.data, 0, newData, 0, getNumCols() * getNumRows());
			this.data = newData;
		}
		System.arraycopy(row, 0, this.data, getNumCols() * getNumRows(), row.length);
		this._rows++;
		this._columns = (this._columns == 0) ? row.length : this._columns;  
	}
	
	@Override
	public void addRow(Tensor row) {
		addRow(row.getRawData());
	}

	@Override
	public void addColumn(Tensor column) {
		addColumn(column.getRawData());		
	}

	@Override
	public void addColumn(Vector column) {
		addColumn(column.getRawData());
	}

	@Override
	public void addColumn(double[] column) {
		assert((getNumRows() == 0 && getNumCols() == 0)  || column.length == getNumRows());
		double[] newData = new double[getNumCols() * getNumRows() + column.length];
		if((getNumRows() == 0 && getNumCols() == 0)){
			System.arraycopy(column, 0, newData, 0, column.length);
		} else {
			for(int i = 0 ; i < column.length ; i++){
				System.arraycopy(this.data, i*getNumCols(), newData, i * (getNumCols() + 1), getNumCols());
				newData[i * (getNumCols() + 1) + getNumCols()] = column[i];
			}
		}
		this.data = newData;
		this._columns++;
		this._rows = (this._rows == 0) ? column.length : this._rows;
	}

	@Override
	public void init(int rows, int columns, ValueGenerator generator) {
		assert(rows > 0);
		assert(columns > 0);
		this.data = new double[rows * columns];
		for(int i = 0 ; i < rows ; i++){
			for(int j = 0; j < columns ; j++){
				this.data[ i * columns + j ] = generator.generate(i, j);
			}
		}
		this._rows = rows;
		this._columns = columns;
	}

	@Override
	public int getNumCols() {
		return this._columns;
	}

	@Override
	public int getNumRows() {
		return this._rows;
	}

	@Override
	public Tensor getValue() {
		return this;
	}

	@Override
	public int getDimension() {
		return 2;
	}

	@Override
	public Tensor getRowValue(int row) {
		return getRow(row);
	}

	@Override
	public Tensor getColumnValue(int column) {
		return getColumn(column);
	}

	@Override
	public Tensor getValue(int row, int column) {
		return getRowValue(row).getColumnValue(column);
	}

	@Override
	public String toString(){
		StringBuffer buffer = new StringBuffer(this.getClass().getCanonicalName() + "\n");
		for(int i = 0 ; i < getNumRows(); i++){
			buffer.append("[");
			for(int j = 0 ; j < getNumCols() ; j++){
				buffer.append(this.data[i*getNumCols() + j]).append(j<getNumCols() - 1 ? "," : "");
			}
			buffer.append("]\n");
		}
		
		return buffer.toString();
	}

}
