package model;// ----------------------------------------------------------------
// The contents of this file are distributed under the CC0 license.
// See http://creativecommons.org/publicdomain/zero/1.0/
// ----------------------------------------------------------------

import static java.lang.System.out;

public abstract class SupervisedLearner
{
	/// Return the name of this learner
	public abstract String name();

	/// Train this supervised learner
	public abstract void train(Matrix features, Matrix labels);

	/// Make a prediction
	abstract void predict(double[] sample, double[] prediction);

	/// Measures the misclassifications with the provided test data
	public int countMisclassifications(Matrix features, Matrix labels)
	{
		if(features.getNumRows() != labels.getNumRows())
			throw new IllegalArgumentException("Mismatching number of getNumRows");
		double[] pred = new double[labels.getNumCols()];
		int mis = 0;
		for(int i = 0; i < features.getNumRows(); i++)
		{
			double[] feat = features.row(i);
			predict(feat, pred);
			double[] lab = labels.row(i);
			for(int j = 0; j < lab.length; j++)
			{
				if(pred[j] != lab[j])
					mis++;
			}
		}
		return mis;
	}
}
