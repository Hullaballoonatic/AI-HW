package model;// ----------------------------------------------------------------
// The contents of this file are distributed under the CC0 license.
// See http://creativecommons.org/publicdomain/zero/1.0/
// ----------------------------------------------------------------

public class BaselineLearner extends SupervisedLearner
{
	private double[] mode;

	public String name()
	{
		return "Baseline";
	}

	public void train(Matrix features, Matrix labels)
	{
		mode = new double[labels.getNumCols()];
		for(int i = 0; i < labels.getNumCols(); i++)
		{
			if(labels.valueCount(i) == 0)
				mode[i] = labels.columnMean(i);
			else
				mode[i] = labels.mostCommonValue(i);
		}
	}

	void predict(double[] sample, double[] prediction)
	{
		Vec.copy(prediction, mode);
	}
}
