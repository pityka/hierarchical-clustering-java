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
import java.util.List;

public class Cluster<T> {

	private T name;
	
	private Cluster<T> parent;

	private List<Cluster<T>> children;

	private Double distance;

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public List<Cluster<T>> getChildren() {
		if (children == null) {
			children = new ArrayList<Cluster<T>>();
		}

		return children;
	}

	public void setChildren(List<Cluster<T>> children) {
		this.children = children;
	}

	public Cluster getParent() {
		return parent;
	}

	public void setParent(Cluster parent) {
		this.parent = parent;
	}

	
	public Cluster(T name) {
		this.name = name;
	}

	public T getName() {
		return name;
	}

	public void setName(T name) {
		this.name = name;
	}

	public void addChild(Cluster cluster) {
		getChildren().add(cluster);

	}

	public boolean contains(Cluster cluster) {
		return getChildren().contains(cluster);
	}

	@Override
	public String toString() {
		return "Cluster " + name;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Cluster))
			return false;
		if (obj == this)
			return true;
		if (obj == null)
			return false;
		
		return ((Cluster)obj).getName().equals(getName());

		// String otherName = obj != null ? obj.toString() : "";
		// return toString().equals(otherName);
	}

	@Override
	public int hashCode() {
		return getName().hashCode();
	}

	public boolean isLeaf() {
		return getChildren().size() == 0;
	}
	
	public int countLeafs() {
	    return countLeafs(this, 0);
	}

    public int countLeafs(Cluster<T> node, int count) {
        if (node.isLeaf()) count++;
        for (Cluster child : node.getChildren()) {
            count += child.countLeafs();
        }
        return count;
    }
    
    public void toConsole(int indent) {
        for (int i = 0; i < indent; i++) {
            System.out.print("  ");
            
        }
        String name = getName() + (isLeaf() ? " (leaf)" : "") + (distance != null ? "  distance: " + distance : "");
        System.out.println(name);
        for (Cluster child : getChildren()) {
            child.toConsole(indent + 1);
        }
    }

    public double getTotalDistance() {
        double dist = getDistance() == null ? 0 : getDistance();
        if (getChildren().size() > 0) {
            dist += children.get(0).getTotalDistance();
        }
        return dist;

    }
	   
}
