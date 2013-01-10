/*
 * LensKit, an open source recommender systems toolkit.
 * Copyright 2010-2013 Regents of the University of Minnesota and contributors
 * Work on LensKit has been funded by the National Science Foundation under
 * grants IIS 05-34939, 08-08692, 08-12148, and 10-17697.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package org.grouplens.lenskit.eval.data.crossfold;

import static java.lang.Math.max;
import static java.lang.Math.round;

import java.util.List;

/**
 * Partition a list by fraction.
 *
 * @param <E>
 * @author Michael Ekstrand <ekstrand@cs.umn.edu>
 */
public class FractionPartition<E> implements PartitionAlgorithm<E> {

    private double fraction;

    /**
     * The fraction to hold out (put in the second partition).
     *
     * @param f The fraction of users to hold out.
     */
    public FractionPartition(double f) {
        fraction = f;
    }

    @Override
    public int partition(List<E> data) {
        int n = (int) round(data.size() * fraction);
        return max(0, data.size() - n);
    }
}
