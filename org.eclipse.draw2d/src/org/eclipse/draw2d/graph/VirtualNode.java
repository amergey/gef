/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.graph;

import org.eclipse.draw2d.geometry.Insets;

/**
 * @author hudsonr
 * @since 2.1
 */
public class VirtualNode extends Node {

/**
 * The previous node.
 */
public Node prev;
/**
 * The next node.
 */
public Node next;

/**
 * Constructs a virtual node.
 * @param e the edge
 * @param i the row
 */
public VirtualNode(Edge e, int i) {
	super(e);
	incoming.add(e);
	outgoing.add(e);
	width = e.width;
	height = 0;
	rank = i;
	padding = new Insets(0, e.padding, 0, e.padding);
}

/**
 * Returns the index of {@link #next}.
 */
public double medianOutgoing() {
	return next.index;
}

/**
 * Returns the index of {@link #prev}.
 */
public double medianIncoming() {
	return prev.index;
}

/**
 * For internal use only.  Returns the original edge weight multiplied by the omega value
 * for the this node and the node on the previous rank.
 * @return the weighted weight, or omega
 */
public int omega() {
	Edge e = (Edge)data;
	if (e.source.rank + 1 < rank  && rank < e.target.rank)
		return 8 * e.weight;
	return 2 * e.weight;
}

/**
 * @see java.lang.Object#toString()
 */
public String toString() {
	Edge e = (Edge)data;
	return "VN[" + (e.vNodes.indexOf(this) + 1) //$NON-NLS-1$
		+ "](" + data + ")"; //$NON-NLS-1$ //$NON-NLS-2$
}

}
