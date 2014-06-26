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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.HashMap;

public class HierarchyBuilder<T> {

	private List<Cluster<T>> clusters;
	private HashMap<Cluster<T>,PriorityQueue<ClusterPair>> distancesIndex;
	private HashMap<Cluster<T>,HashMap<Cluster<T>,ClusterPair>> distancesIndex2D;

	public List<Cluster<T>> getClusters() {
		return clusters;
	}

	public HierarchyBuilder(List<Cluster<T>> clusters, LinkedList<ClusterPair> distances) {
		this.clusters = clusters;
		this.distancesIndex = new HashMap();
		this.distancesIndex2D = new HashMap();


		for (ClusterPair link : distances) {
			Cluster<T> left = link.getlCluster();
			Cluster<T> right = link.getrCluster();

			if (distancesIndex.containsKey(left)) {
				PriorityQueue<ClusterPair> list = distancesIndex.get(left);
				list.add(link);
			} else {
				PriorityQueue<ClusterPair> list = new PriorityQueue();
				list.add(link);
				distancesIndex.put(left,list);
			}
			if (distancesIndex.containsKey(right)) {
				PriorityQueue<ClusterPair> list = distancesIndex.get(right);
				list.add(link);
			} else {
				PriorityQueue<ClusterPair> list = new PriorityQueue();
				list.add(link);
				distancesIndex.put(right,list);
			}

			if (distancesIndex2D.containsKey(left)) {
				distancesIndex2D.get(left).put(right,link);
			} else {
				HashMap<Cluster<T>,ClusterPair> list = new HashMap();
				list.put(right,link);
				distancesIndex2D.put(left,list);
			}
			if (distancesIndex2D.containsKey(right)) {
				distancesIndex2D.get(right).put(left,link);
			} else {
				HashMap<Cluster<T>,ClusterPair> list = new HashMap();
				list.put(left,link);
				distancesIndex2D.put(right,list);
			}

		} 

	}

	public ClusterPair getMin() {
		ClusterPair min = null;
		PriorityQueue<ClusterPair> minpq = null;
		for (PriorityQueue<ClusterPair> pq : distancesIndex.values()) {
			ClusterPair cp = pq.peek();
			if (cp != null && (min == null || cp.getLinkageDistance() < min.getLinkageDistance())) {
				min = cp;
				minpq = pq;
			}
		}
		minpq.poll();
		distancesIndex.get(min.getlCluster()).remove(min);
		distancesIndex.get(min.getrCluster()).remove(min);
		return min;
	}

	public void agglomerate(LinkageStrategy linkageStrategy) {
		ClusterPair minDistLink = getMin();
		if (minDistLink != null) {			
			
			clusters.remove(minDistLink.getrCluster());
			clusters.remove(minDistLink.getlCluster());

			Cluster oldClusterL = minDistLink.getlCluster();
			Cluster oldClusterR = minDistLink.getrCluster();
			Cluster newCluster = minDistLink.agglomerate(null);
			PriorityQueue<ClusterPair> newList = new PriorityQueue();
			HashMap<Cluster<T>,ClusterPair> newMap = new HashMap();

			for (Cluster iClust : clusters) {
				ClusterPair link1 = findByClusters(iClust, oldClusterL);
				ClusterPair link2 = findByClusters(iClust, oldClusterR);
				ClusterPair newLinkage = new ClusterPair();
				newList.add(newLinkage);
				newMap.put(iClust,newLinkage);

				newLinkage.setlCluster(iClust);
				newLinkage.setrCluster(newCluster);
				Collection<Double> distanceValues = new ArrayList<Double>();
				if (link1 != null) {
					distanceValues.add(link1.getLinkageDistance());
					distancesIndex.get(oldClusterL).remove(link1);
					distancesIndex.get(iClust).remove(link1);
				}
				if (link2 != null) {
					distanceValues.add(link2.getLinkageDistance());
					distancesIndex.get(oldClusterR).remove(link2);
					distancesIndex.get(iClust).remove(link2);
				}
				Double newDistance = linkageStrategy.calculateDistance(distanceValues);
				newLinkage.setLinkageDistance(newDistance);				
				distancesIndex.get(iClust).add(newLinkage);

				
				distancesIndex2D.get(iClust).put(newCluster,newLinkage);


			}
			
			distancesIndex.put(newCluster,newList);
			distancesIndex2D.put(newCluster,newMap);
			clusters.add(newCluster);
		}
	}

	private ClusterPair findByClusters(Cluster c1, Cluster c2) {	
		return distancesIndex2D.get(c1).get(c2);
	}

	public boolean isTreeComplete() {
		return clusters.size() == 1;
	}

	public Cluster getRootCluster() {
		if (!isTreeComplete()) {
			throw new RuntimeException("No root available");
		}
		return clusters.get(0);
	}

}
