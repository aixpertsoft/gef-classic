/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d;

import java.util.List;

/**
 * This class is a helper to the {@link SWTEventDispatcher}. It handles the task of 
 * determining which Figure will gain focus upon a tab/shift-tab. It also keeps track of 
 * the Figure with current focus.
 * <p>
 * Note: When a Canvas with a {@link LightweightSystem} gains focus, it gives focus to the 
 * child Figure who had focus when this Canvas lost focus. If the canvas is gaining focus 
 * for the first time, focus is given to its first child Figure.
 */
public class FocusTraverseManager {

IFigure currentFocusOwner;

/**
 * Default constructor.
 */
public FocusTraverseManager() { }

private IFigure findDeepestRightmostChildOf(IFigure fig) {
	while (fig.getChildren().size() != 0) {
		fig = (IFigure)fig.getChildren().get(fig.getChildren().size() - 1);
	}	
	return fig;
}

/**
 * Returns the IFigure that will receive focus upon a 'tab' traverse event.
 * 
 * @param root the {@link LightweightSystem LightweightSystem's} root figure
 * @param focusOwner the IFigure who currently owns focus
 * @return the next focusable figure
 */
public IFigure getNextFocusableFigure(IFigure root, IFigure focusOwner) {
	boolean found = false;
	IFigure nextFocusOwner = focusOwner;
	
	/* If no Figure currently has focus,
	 * apply focus to root figure's first focusable child
	 */
	if (focusOwner == null) {
		if (root.getChildren().size() != 0) {
			nextFocusOwner = ((IFigure)root.getChildren().get(0));
			if (isFocusEligible(nextFocusOwner))
				return nextFocusOwner;
		} else
			return null;
	}
	while (!found) {
		/*
		 * Figure traversal is implemented using the pre-order left to right
		 * tree traversal algorithm.
		 *  
		 * If the focused sibling has children, traverse to its leftmost child.
		 * If the focused sibling has no children, traverse to the sibling
		 * to its right.
		 * If there is no sibling to the right, go up the tree until a node
		 * with untraversed siblings is found.
		 */

		if (nextFocusOwner.getChildren().size() != 0) {
			nextFocusOwner = ((IFigure)(nextFocusOwner.getChildren().get(0)));		
			if (isFocusEligible(nextFocusOwner))				
				found = true;
		} else {
			IFigure parent = nextFocusOwner.getParent();
			List siblings = parent.getChildren();
			int siblingPos = siblings.indexOf(nextFocusOwner);		
			if (siblingPos < siblings.size() - 1) {
				nextFocusOwner = ((IFigure)(siblings.get(siblingPos + 1)));
				if (isFocusEligible(nextFocusOwner))
					found = true;
			} else {
				boolean untraversedSiblingFound = false;			
				while (!untraversedSiblingFound) {
					IFigure p = nextFocusOwner.getParent();	
					IFigure gp = p.getParent();
					
					if (gp != null) {
						int parentSiblingCount = gp.getChildren().size();
						int parentIndex = gp.getChildren().indexOf(p);
						if (parentIndex < parentSiblingCount - 1) {
							nextFocusOwner = ((IFigure)p.getParent()
														.getChildren()
														.get(parentIndex + 1));
							untraversedSiblingFound = true;
							if (isFocusEligible(nextFocusOwner))		
								found = true;
						} else
							nextFocusOwner = p;
					} else {
						nextFocusOwner = null;
						untraversedSiblingFound = true;
						found = true;
					}
				}
			}		
		}
	}
	return nextFocusOwner;
}

/**
 * Returns the IFigure that will receive focus upon a 'shift-tab' traverse event.
 * 
 * @param root The {@link LightweightSystem LightweightSystem's} root figure
 * @param focusOwner The IFigure who currently owns focus 
 * @return the previous focusable figure
 */
public IFigure getPreviousFocusableFigure(IFigure root, IFigure focusOwner) {
	if (focusOwner == null)
		return null;
	
	boolean found = false;
	IFigure nextFocusOwner = focusOwner;
 	while (!found) {
 		IFigure parent = nextFocusOwner.getParent();
		
		/* 
		 * At root, return null to indicate traversal
		 * is complete.
		 */
		if (parent == null)
			return null;
		
		List siblings = parent.getChildren();
		int siblingPos = siblings.indexOf(nextFocusOwner);
		
		/*		
		 * Figure traversal is implemented using the post-order right to left 
		 * tree traversal algorithm.
		 * 
		 * Find the rightmost child.
		 * If this child is focusable, return it
		 * If not focusable, traverse to its sibling and repeat.
		 * If there is no sibling, traverse its parent.
		 */
		if (siblingPos != 0) {
			IFigure child = 
					findDeepestRightmostChildOf((IFigure)siblings.get(siblingPos - 1));
			if (isFocusEligible(child)) {
				found = true;
				nextFocusOwner = child;
			} else if (child.equals(nextFocusOwner)) {
				if (isFocusEligible(nextFocusOwner))
					found = true;
			} else
				nextFocusOwner = child;			
		} else {
			nextFocusOwner = parent;
				if (isFocusEligible(nextFocusOwner))
					found = true;
		}
	}
	return nextFocusOwner;
}

/**
 * @return the figure that currently has focus
 */
public IFigure getCurrentFocusOwner() {
	return currentFocusOwner;
}	

private boolean isFocusEligible(IFigure fig) {
	return (fig != null && fig.isFocusTraversable() && fig.isShowing());
}

/**
 * Sets the currently focused figure.
 * @param fig the figure to get focus
 */
public void setCurrentFocusOwner(IFigure fig) {
	currentFocusOwner = fig;
}

}