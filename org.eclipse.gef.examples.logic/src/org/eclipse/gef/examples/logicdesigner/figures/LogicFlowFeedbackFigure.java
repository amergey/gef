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
package org.eclipse.gef.examples.logicdesigner.figures;

import org.eclipse.draw2d.*;

public class LogicFlowFeedbackFigure
	extends RectangleFigure
{
	
public LogicFlowFeedbackFigure() {
	this.setFill(false);
	this.setXOR(true);
	setBorder(new LogicFlowFeedbackBorder());
}


}