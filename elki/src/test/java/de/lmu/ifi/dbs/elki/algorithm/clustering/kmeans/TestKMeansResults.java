package de.lmu.ifi.dbs.elki.algorithm.clustering.kmeans;

/*
 This file is part of ELKI:
 Environment for Developing KDD-Applications Supported by Index-Structures

 Copyright (C) 2014
 Ludwig-Maximilians-Universität München
 Lehr- und Forschungseinheit für Datenbanksysteme
 ELKI Development Team

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import org.junit.Test;

import de.lmu.ifi.dbs.elki.JUnit4Test;
import de.lmu.ifi.dbs.elki.algorithm.AbstractSimpleAlgorithmTest;
import de.lmu.ifi.dbs.elki.algorithm.clustering.kmeans.initialization.PAMInitialMeans;
import de.lmu.ifi.dbs.elki.algorithm.clustering.kmeans.parallel.ParallelLloydKMeans;
import de.lmu.ifi.dbs.elki.data.Clustering;
import de.lmu.ifi.dbs.elki.data.DoubleVector;
import de.lmu.ifi.dbs.elki.data.model.MedoidModel;
import de.lmu.ifi.dbs.elki.database.Database;
import de.lmu.ifi.dbs.elki.utilities.ClassGenericsUtil;
import de.lmu.ifi.dbs.elki.utilities.optionhandling.ParameterException;
import de.lmu.ifi.dbs.elki.utilities.optionhandling.parameterization.ListParameterization;

/**
 * Performs a full KMeans run, and compares the result with a clustering derived
 * from the data set labels. This test ensures that KMeans's performance doesn't
 * unexpectedly drop on this data set (and also ensures that the algorithms
 * work, as a side effect).
 * 
 * @author Katharina Rausch
 * @author Erich Schubert
 */
public class TestKMeansResults extends AbstractSimpleAlgorithmTest implements JUnit4Test {
  /**
   * Run KMeans with fixed parameters and compare the result to a golden
   * standard.
   * 
   * @throws ParameterException
   */
  @Test
  public void testKMeansLloyd() {
    Database db = makeSimpleDatabase(UNITTEST + "different-densities-2d-no-noise.ascii", 1000);

    // Setup algorithm
    ListParameterization params = new ListParameterization();
    params.addParameter(KMeans.K_ID, 5);
    params.addParameter(KMeans.SEED_ID, 2);
    AbstractKMeans<DoubleVector, ?> kmeans = ClassGenericsUtil.parameterizeOrAbort(KMeansLloyd.class, params);
    testParameterizationOk(params);

    // run KMeans on database
    Clustering<?> result = kmeans.run(db);
    testFMeasure(db, result, 0.998005);
    testClusterSizes(result, new int[] { 199, 200, 200, 200, 201 });
  }

  /**
   * Run KMeans with fixed parameters and compare the result to a golden
   * standard.
   * 
   * @throws ParameterException
   */
  @Test
  public void testParallelKMeansLloyd() {
    Database db = makeSimpleDatabase(UNITTEST + "different-densities-2d-no-noise.ascii", 1000);

    // Setup algorithm
    ListParameterization params = new ListParameterization();
    params.addParameter(KMeans.K_ID, 5);
    params.addParameter(KMeans.SEED_ID, 2);
    AbstractKMeans<DoubleVector, ?> kmeans = ClassGenericsUtil.parameterizeOrAbort(ParallelLloydKMeans.class, params);
    testParameterizationOk(params);

    // run KMeans on database
    Clustering<?> result = kmeans.run(db);
    testFMeasure(db, result, 0.998005);
    testClusterSizes(result, new int[] { 199, 200, 200, 200, 201 });
  }

  /**
   * Run KMeans with fixed parameters and compare the result to a golden
   * standard.
   * 
   * @throws ParameterException
   */
  @Test
  public void testKMeansMacQueen() {
    Database db = makeSimpleDatabase(UNITTEST + "different-densities-2d-no-noise.ascii", 1000);

    // Setup algorithm
    ListParameterization params = new ListParameterization();
    params.addParameter(KMeans.K_ID, 5);
    params.addParameter(KMeans.SEED_ID, 2);
    AbstractKMeans<DoubleVector, ?> kmeans = ClassGenericsUtil.parameterizeOrAbort(KMeansMacQueen.class, params);
    testParameterizationOk(params);

    // run KMeans on database
    Clustering<?> result = kmeans.run(db);
    testFMeasure(db, result, 0.998005);
    testClusterSizes(result, new int[] { 199, 200, 200, 200, 201 });
  }

  /**
   * Run KMedians with fixed parameters and compare the result to a golden
   * standard.
   * 
   * @throws ParameterException
   */
  @Test
  public void testKMediansLloyd() {
    Database db = makeSimpleDatabase(UNITTEST + "different-densities-2d-no-noise.ascii", 1000);

    // Setup algorithm
    ListParameterization params = new ListParameterization();
    params.addParameter(KMeans.K_ID, 5);
    params.addParameter(KMeans.SEED_ID, 2);
    AbstractKMeans<DoubleVector, ?> kmedians = ClassGenericsUtil.parameterizeOrAbort(KMediansLloyd.class, params);
    testParameterizationOk(params);

    // run KMedians on database
    Clustering<?> result = kmedians.run(db);
    testFMeasure(db, result, 0.998005);
    testClusterSizes(result, new int[] { 199, 200, 200, 200, 201 });
  }

  /**
   * Run KMedians PAM with fixed parameters and compare the result to a golden
   * standard.
   * 
   * @throws ParameterException
   */
  @Test
  public void testKMedoidsPAM() {
    Database db = makeSimpleDatabase(UNITTEST + "different-densities-2d-no-noise.ascii", 1000);

    // Setup algorithm
    ListParameterization params = new ListParameterization();
    params.addParameter(KMeans.K_ID, 5);
    KMedoidsPAM<DoubleVector> kmedians = ClassGenericsUtil.parameterizeOrAbort(KMedoidsPAM.class, params);
    testParameterizationOk(params);

    // run KMedians on database
    Clustering<MedoidModel> result = kmedians.run(db);
    testFMeasure(db, result, 0.998005);
    testClusterSizes(result, new int[] { 199, 200, 200, 200, 201 });
  }

  /**
   * Run CLARA with fixed parameters and compare the result to a golden
   * standard.
   * 
   * @throws ParameterException
   */
  @Test
  public void testCLARA() {
    Database db = makeSimpleDatabase(UNITTEST + "different-densities-2d-no-noise.ascii", 1000);

    // Setup algorithm
    ListParameterization params = new ListParameterization();
    params.addParameter(KMeans.K_ID, 5);
    params.addParameter(CLARA.Parameterizer.NUMSAMPLES_ID, 2);
    params.addParameter(CLARA.Parameterizer.SAMPLESIZE_ID, 50);
    CLARA<DoubleVector> kmedians = ClassGenericsUtil.parameterizeOrAbort(CLARA.class, params);
    testParameterizationOk(params);

    // run KMedians on database
    Clustering<MedoidModel> result = kmedians.run(db);
    testFMeasure(db, result, 0.998005);
    testClusterSizes(result, new int[] { 199, 200, 200, 200, 201 });
  }

  /**
   * Run KMedoidsEM with fixed parameters and compare the result to a golden
   * standard.
   * 
   * @throws ParameterException
   */
  @Test
  public void testKMedoidsEM() {
    Database db = makeSimpleDatabase(UNITTEST + "different-densities-2d-no-noise.ascii", 1000);

    // Setup algorithm
    ListParameterization params = new ListParameterization();
    params.addParameter(KMeans.K_ID, 5);
    params.addParameter(KMeans.INIT_ID, PAMInitialMeans.class);
    KMedoidsEM<DoubleVector> kmedians = ClassGenericsUtil.parameterizeOrAbort(KMedoidsEM.class, params);
    testParameterizationOk(params);

    // run KMedians on database
    Clustering<MedoidModel> result = kmedians.run(db);
    testFMeasure(db, result, 0.998005);
    testClusterSizes(result, new int[] { 199, 200, 200, 200, 201 });
  }
}