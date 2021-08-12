package com.abapblog.classicOutline.tree;

import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.widgets.Event;

public class TreeCellLabelProvider extends StyledCellLabelProvider {

	public TreeCellLabelProvider() {
	}

	@Override
	public void update(ViewerCell cell) {
		Object element = cell.getElement();

		if (element instanceof TreeNode) {
			TreeNode node = (TreeNode) element;
			StyledString styledString = new StyledString(node.getName());
			String decoration = " " + node.getDescription(); //$NON-NLS-1$
			styledString.append(decoration, StyledString.COUNTER_STYLER);

			cell.setText(styledString.toString());
			cell.setStyleRanges(styledString.getStyleRanges());
			cell.setImage(node.getImage());
		}

		super.update(cell);
	}

	@Override
	protected void measure(Event event, Object element) {
		super.measure(event, element);
	}

}
