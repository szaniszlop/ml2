package com.ps.matrix.component.simple;

import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.ps.matrix.component.generators.ConstantDiagonalGenerator;
import com.ps.matrix.component.generators.GausianDistributionGenerator;
import com.ps.matrix.component.operators.DeferedOperatorFactory;
import com.ps.matrix.model.BinaryOperator;
import com.ps.matrix.model.Function;
import com.ps.matrix.model.Matrix;
import com.ps.matrix.model.MatrixFactory;
import com.ps.matrix.model.OperatorFactory;
import com.ps.matrix.model.Result;
import com.ps.matrix.model.UnaryOperator;
import com.ps.matrix.model.ValueType;

public class SimpleMatrixTest {

	private Matrix matrix;
	private MatrixFactory factory;
	private OperatorFactory of;
	
	@Before
	public void init(){
		factory = new SimpleMatrixFactory();
		matrix = factory.newMatrix(3, 2, new ConstantDiagonalGenerator(1));
		of = DeferedOperatorFactory.getInstance();
		System.out.println(matrix.toString());
	}
	
	@Test
	public void testGetType() {
		if(ValueType.MATRIX != matrix.getType()){
			fail("Incorrect Operand Type " + matrix.getType().name());
		}
	}

	@Test
	public void testGetDimension() {
		Assert.assertArrayEquals(new int[]{3,2}, matrix.getDimensions());
	}

	@Test
	public void testGetColumn() {
		Assert.assertArrayEquals(new double[]{1, 0, 0}, matrix.getColumn(0).getRawData(), 0d);
		Assert.assertArrayEquals(new double[]{0, 1, 0}, matrix.getColumn(1).getRawData(), 0d);
	}

	@Test
	public void testGetRow() {
		Assert.assertArrayEquals(new double[]{1,0}, matrix.getRow(0).getRawData(), 0d);
		Assert.assertArrayEquals(new double[]{0,1}, matrix.getRow(1).getRawData(), 0d);
		Assert.assertArrayEquals(new double[]{0,0}, matrix.getRow(2).getRawData(), 0d);
	}

	@Test
	public void testAddRowDoubleArray() {
		matrix.addRow(new double[]{2d, 3d});
		System.out.println(matrix.toString());
		Assert.assertArrayEquals(new double[]{2d,3d}, matrix.getRow(3).getRawData(), 0d);
		Assert.assertEquals(4, matrix.getNumRows());
		Assert.assertEquals(2, matrix.getNumCols());
	}

	@Test
	public void testAddColumnDoubleArray() {
		matrix.addColumn(new double[]{2d, 3d, 4d});
		System.out.println(matrix.toString());
		System.out.println(matrix.getColumn(2));
		Assert.assertArrayEquals(new double[]{2d, 3d, 4d}, matrix.getColumn(2).getRawData(), 0d);
		Assert.assertEquals(3, matrix.getNumRows());
		Assert.assertEquals(3, matrix.getNumCols());
	}

	@Test
	public void testGetNumCols() {
		Assert.assertEquals(2, matrix.getNumCols());;
	}

	@Test
	public void testGetNumRows() {
		Assert.assertEquals(3, matrix.getNumRows());;
	}

	@Test
	public void testAddition(){
		Matrix m = factory.newMatrix(3,2,new GausianDistributionGenerator(0, 2));
		System.out.println(m);
		BinaryOperator op =  of.add(of.operand("A", matrix), of.operand("B", m));
		Result r = op.getResult();
		System.out.println(r);
	}
	
	@Test
	public void testAddition2(){
		Matrix m = factory.newMatrix(3,2,new GausianDistributionGenerator(0, 2));
		System.out.println(m);
		Result r = matrix.plus(m);
		System.out.println(r);
	}	

	@Test
	public void testTranspose(){
		UnaryOperator op = of.transpose(of.operand("A",matrix));
		Result r = op.getResult();
		System.out.println(r);
	}

	@Test
	public void testTranspose1(){
		matrix = factory.newMatrix(3, 2, new ConstantDiagonalGenerator(1));
		Result r = matrix.T();
		double[] result = r.getRawData();
		double[] expectedResult = new double[]{1d, 0d, 0d, 0d, 1d, 0d};
		System.out.println(r);
		Assert.assertArrayEquals(expectedResult, result, 0d);
	}

	@Test
	public void testTranspose2(){
		matrix = factory.newMatrix(2, 3, new double[]{1d, 2d, 3d, 4d, 5d, 6d});
		Result r = matrix.T();
		double[] result = r.getRawData();
		double[] expectedResult = new double[]{1d, 4d, 2d, 5d, 3d, 6d};
		System.out.println("testTranspose2: " + r);
		Assert.assertArrayEquals(expectedResult, result, 0d);
	}
	
	@Test
	public void testTranspos3(){
		matrix = factory.newMatrix(3, 2, new double[]{1d, 2d, 3d, 4d, 5d, 6d});
		Result r = matrix.T();
		double[] result = r.getRawData();
		double[] expectedResult = new double[]{1d, 3d, 5d, 2d, 4d, 6d};
		System.out.println("testTranspose3: " + r);
		Assert.assertArrayEquals(expectedResult, result, 0d);
	}

	
	@Test
	public void testMultiplication(){
		Matrix m = factory.newMatrix(2,3, new GausianDistributionGenerator(0, 2));
		BinaryOperator op = of.multiply(of.operand("A", matrix), of.operand("B", m));
		Result r = op.getResult();
		System.out.println(r);
	}

	@Test
	public void testMultiplication2(){
		Matrix m = factory.newMatrix(2,3, new GausianDistributionGenerator(0, 2));
		Result r = matrix.multiply(m);
		System.out.println(r);
	}
	
	
	@Test
	public void testcombineOperation(){
		System.out.println("testcombineOperation");
		Matrix m = factory.newMatrix(2,3, new ConstantDiagonalGenerator(2));
		BinaryOperator op1 = of.multiply(of.operand("A", matrix), of.operand("B", m));
		Result multiplicationResult = op1.getResult();
		UnaryOperator op = of.transpose(of.operand("AB", multiplicationResult));
		Result r = op.getResult();
		System.out.println(r);
	}

	@Test
	public void testcombineOperation2(){
		System.out.println("testcombineOperation 2");
		Matrix m = factory.newMatrix(2,3, new ConstantDiagonalGenerator(2));
		matrix.multiply(m).T();
		Result r = matrix.multiply(m).T();;
		System.out.println(r);
	}

	@Test
	public void testcombineOperation3(){
		System.out.println("testcombineOperation 3");
		Matrix m = factory.newMatrix(3,3,new GausianDistributionGenerator(0, 2));
		Result r = m.multiply(m.T()).plus(m).minus(m).T();
		System.out.println(m);
		System.out.println(r);
	}
	
	@Test
	public void testApplyFunction(){
		System.out.println("testApplyFunction ln");
		Matrix m = factory.newMatrix(3,3,new GausianDistributionGenerator(0, 2));
		Result r = m.apply(new LogarimusE());
		System.out.println(m);
		System.out.println(r);
	}
	
	@Test
	public void testAdScalar(){
		System.out.println("testAdScalar 3");
		Matrix m = factory.newMatrix(3,3,new GausianDistributionGenerator(0, 2));
		Result r = m.plus(new SimpleScalar(3));
		System.out.println(m);
		System.out.println(r);
	}

	@Test
	public void testSubScalar(){
		System.out.println("testSubScalar 3");
		Matrix m = factory.newMatrix(3,3,new GausianDistributionGenerator(0, 2));
		Result r = m.minus(new SimpleScalar(3));
		System.out.println(m);
		System.out.println(r);
	}
	
	@Test
	public void testMultScalar(){
		System.out.println("testMultScalar 3");
		Matrix m = factory.newMatrix(3,3,new GausianDistributionGenerator(0, 2));
		Result r = m.multiply(new SimpleScalar(3));
		System.out.println(m);
		System.out.println(r);
	}
	
	@Test
	public void testDivScalar(){
		System.out.println("testDivScalar 3");
		Matrix m = factory.newMatrix(3,3,new GausianDistributionGenerator(0, 2));
		Result r = m.divide(new SimpleScalar(3));
		System.out.println(m);
		System.out.println(r);
	}

	@Test
	public void testcombineOperation4(){
		System.out.println("testcombineOperation 4");
		Matrix m = factory.newMatrix(3,3,new GausianDistributionGenerator(0, 2));
		Result r = m.multiply(m.T()).plus(new SimpleScalar(3)).minus(m).minus(new SimpleScalar(1.5)).T().divide(new SimpleScalar(3));
		System.out.println(m);
		System.out.println(r);
	}
	
	private static final class LogarimusE implements Function{
		public double apply(double[] input){
			return Math.log(input[0]);
		}
		
		@Override
		public double apply(double a, double b) {
			throw new UnsupportedOperationException();
		}

		public String getName(){
			return "ln";
		}		
	}
	
	
	
	
	
}
