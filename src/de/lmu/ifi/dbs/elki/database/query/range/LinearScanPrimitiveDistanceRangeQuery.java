package de.lmu.ifi.dbs.elki.database.query.range;

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

import de.lmu.ifi.dbs.elki.database.ids.DBIDIter;
import de.lmu.ifi.dbs.elki.database.ids.DBIDRef;
import de.lmu.ifi.dbs.elki.database.ids.DBIDUtil;
import de.lmu.ifi.dbs.elki.database.ids.DoubleDBIDList;
import de.lmu.ifi.dbs.elki.database.ids.ModifiableDoubleDBIDList;
import de.lmu.ifi.dbs.elki.database.query.distance.PrimitiveDistanceQuery;
import de.lmu.ifi.dbs.elki.database.relation.Relation;
import de.lmu.ifi.dbs.elki.distance.distancefunction.PrimitiveDistanceFunction;

/**
 * Default linear scan range query class.
 * 
 * Subtle optimization: for primitive distances, retrieve the query object only
 * once from the relation.
 * 
 * @author Erich Schubert
 * 
 * @apiviz.uses PrimitiveDistanceQuery
 * 
 * @param <O> Database object type
 */
public class LinearScanPrimitiveDistanceRangeQuery<O> extends AbstractDistanceRangeQuery<O> {
  /**
   * Unboxed distance function.
   */
  private PrimitiveDistanceFunction<? super O> rawdist;

  /**
   * Constructor.
   * 
   * @param distanceQuery Distance function to use
   */
  public LinearScanPrimitiveDistanceRangeQuery(PrimitiveDistanceQuery<O> distanceQuery) {
    super(distanceQuery);
    rawdist = distanceQuery.getDistanceFunction();
  }

  @Override
  public DoubleDBIDList getRangeForDBID(DBIDRef id, double range) {
    // Note: subtle optimization. Get "id" only once!
    final O obj = relation.get(id);
    ModifiableDoubleDBIDList result = DBIDUtil.newDistanceDBIDList();
    linearScan(relation, relation.iterDBIDs(), rawdist, obj, range, result);
    result.sort();
    return result;
  }

  @Override
  public DoubleDBIDList getRangeForObject(O obj, double range) {
    ModifiableDoubleDBIDList result = DBIDUtil.newDistanceDBIDList();
    linearScan(relation, relation.iterDBIDs(), rawdist, obj, range, result);
    result.sort();
    return result;
  }

  private static <O> void linearScan(Relation<? extends O> relation, DBIDIter iter, PrimitiveDistanceFunction<? super O> rawdist, O obj, double range, ModifiableDoubleDBIDList result) {
    while(iter.valid()) {
      final double doubleDistance = rawdist.distance(obj, relation.get(iter));
      if(doubleDistance <= range) {
        result.add(doubleDistance, iter);
      }
      iter.advance();
    }
  }
}