package org.eclipse.gef.ui.palette;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;

class EditPartTipHelper 
	extends org.eclipse.draw2d.PopUpHelper
{

private static EditPartTipHelper currentHelper;

private static void setHelper(EditPartTipHelper helper) {
	if (currentHelper != null && currentHelper != helper && currentHelper.isShowing())
		currentHelper.hide();
	currentHelper = helper;
}

public EditPartTipHelper(Control c){
	super(c);	
}

/**
 * Sets the LightWeightSystem object's contents  
 * to the passed tooltip, and displays the tip at 
 * the coordianates specified by tipPosX and tipPosY.
 *
 * @param tip  The tool tip to be displayed.
 * @param tipPosX X coordiante of tooltip to be displayed
 * @param tipPosY Y coordinate of tooltip to be displayed
 */
public void displayToolTipAt(IFigure tip, int tipPosX, int tipPosY){
	if(tip != null){
		EditPartTipHelper.setHelper(this);
		getLightweightSystem().setContents(tip);
		Dimension tipSize = tip.getPreferredSize();
		setShellBounds(tipPosX, tipPosY, tipSize.width, tipSize.height);
		show();
		getShell().setCapture(true);
	}
}

/**
 * @see org.eclipse.draw2d.PopUpHelper#hide()
 */
protected void hide() {
	super.hide();
	currentHelper = null;
}

protected void hookShellListeners(){

	/* If the cursor leaves the tip window, hide the tooltip and
	   dispose of its shell */
	getShell().addMouseTrackListener(new MouseTrackAdapter(){
		public void mouseExit(MouseEvent e){
			getShell().setCapture(false);	
			dispose();
		}
	});
	/* If the mouseExit listener does not get called,
	   dispose of the shell if the cursor is no longer in the 
	   tooltip. This occurs in the rare case that a mouseEnter 
	   is not received on the tooltip when it appears.*/
	getShell().addMouseMoveListener(new MouseMoveListener(){
		public void mouseMove(MouseEvent e){
			Point eventPoint = getShell().toDisplay(new Point(e.x,e.y));
			if(!getShell().getBounds().contains(eventPoint)){
				if (isShowing())
					getShell().setCapture(false);
				dispose();
			}
		}
	});
}

}