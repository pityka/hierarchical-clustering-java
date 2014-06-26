/*******************************************************************************
 * Copyright 2013 Lars Behnke
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.apporiented.algorithm.clustering;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PDistClusteringAlgorithm implements ClusteringAlgorithm {

	@Override
	public <T> Cluster<T> performClustering(double[][] distances,
			T[] clusterNames, LinkageStrategy linkageStrategy) {

		/* Argument checks */
		if (distances == null || distances.length == 0) {
			throw new IllegalArgumentException("Invalid distance matrix");
		}
		if (distances[0].length != clusterNames.length
				* (clusterNames.length - 1) / 2) {
			throw new IllegalArgumentException("Invalid cluster name array");
		}
		if (linkageStrategy == null) {
			throw new IllegalArgumentException("Undefined linkage strategy");
		}

		/* Setup model */
		List<Cluster<T>> clusters = createClusters(clusterNames);
		LinkedList<ClusterPair> linkages = createLinkages(distances, clusters);

		/* Process */
		HierarchyBuilder builder = new HierarchyBuilder(clusters, linkages);
		while (!builder.isTreeComplete()) {
			builder.agglomerate(linkageStrategy);
		}

		return builder.getRootCluster();
	}

	private <T> LinkedList<ClusterPair> createLinkages(double[][] distances,
			List<Cluster<T>> clusters) {
		LinkedList<ClusterPair> linkages = new LinkedList<ClusterPair>();
		for (int col = 0; col < clusters.size(); col++) {
			Cluster cluster_col = clusters.get(col);
			for (int row = col + 1; row < clusters.size(); row++) {
				ClusterPair link = new ClusterPair();
				link.setLinkageDistance(distances[0][accessFunction(row, col,
						clusters.size())]);
				link.setlCluster(cluster_col);
				link.setrCluster(clusters.get(row));
				linkages.add(link);
			}
		}
		return linkages;
	}

	private <T> List<Cluster<T>> createClusters(T[] clusterNames) {
		List<Cluster<T>> clusters = new ArrayList<Cluster<T>>();
		for (T clusterName : clusterNames) {
			Cluster cluster = new Cluster(clusterName);
			clusters.add(cluster);
		}
		return clusters;
	}

	// Credit to this function goes to
	// http://stackoverflow.com/questions/13079563/how-does-condensed-distance-matrix-work-pdist
	private static int accessFunction(int i, int j, int n) {
		return n * j - j * (j + 1) / 2 + i - 1 - j;
	}

}
