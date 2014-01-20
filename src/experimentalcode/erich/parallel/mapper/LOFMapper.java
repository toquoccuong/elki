package experimentalcode.erich.parallel.mapper;

/*
 This file is part of ELKI:
 Environment for Developing KDD-Applications Supported by Index-Structures

 Copyright (C) 2013
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

import de.lmu.ifi.dbs.elki.database.datastore.DataStore;
import de.lmu.ifi.dbs.elki.database.datastore.DoubleDataStore;
import de.lmu.ifi.dbs.elki.database.ids.DBIDIter;
import de.lmu.ifi.dbs.elki.database.ids.DBIDRef;
import de.lmu.ifi.dbs.elki.database.ids.DBIDUtil;
import de.lmu.ifi.dbs.elki.database.ids.KNNList;
import experimentalcode.erich.parallel.MapExecutor;
import experimentalcode.erich.parallel.SharedDouble;

/**
 * Mapper for computing the LOF.
 * 
 * @author Erich Schubert
 */
public class LOFMapper extends AbstractDoubleMapper {
  /**
   * KNN store
   */
  private DataStore<? extends KNNList> knns;

  /**
   * LRD store
   */
  private DoubleDataStore lrds;

  /**
   * Exclude object itself from computation.
   */
  private boolean noself;

  /**
   * Constructor.
   * 
   * @param knns k nearest neighbors
   * @param kdists k distances
   * @param noself Exclude self from neighbors
   */
  public LOFMapper(DataStore<? extends KNNList> knns, DoubleDataStore lrds, boolean noself) {
    super();
    this.knns = knns;
    this.lrds = lrds;
    this.noself = noself;
  }

  @Override
  public Instance instantiate(MapExecutor mapper) {
    return new Instance(output.instantiate(mapper));
  }

  /**
   * Mapper instance
   * 
   * @author Erich Schubert
   */
  private class Instance extends AbstractDoubleMapper.Instance {
    /**
     * Constructor.
     * 
     * @param output Output variable
     */
    protected Instance(SharedDouble.Instance output) {
      super(output);
    }

    @Override
    public void map(DBIDRef id) {
      // Own density
      final double lrdp = lrds.doubleValue(id);
      if (Double.isInfinite(lrdp)) {
        output.set(1.0);
        return;
      }
      // Compute average neighbor density:
      KNNList knn = knns.get(id);
      double avlrd = 0.0;
      int cnt = 0;
      for (DBIDIter n = knn.iter(); n.valid(); n.advance()) {
        if (noself && DBIDUtil.equal(n, id)) {
          continue;
        }
        avlrd += lrds.doubleValue(n);
        cnt++;
        if (Double.isInfinite(avlrd)) {
          break;
        }
      }
      avlrd = (cnt > 0) ? (avlrd / cnt) : 0;
      output.set(avlrd / lrdp);
    }
  }
}
