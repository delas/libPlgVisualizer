package plg.visualizer.model.nodes;

import org.processmining.plugins.graphviz.dot.DotNode;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 
 * @author Andrea Burattin
 */
public class DotTask extends DotNode {

	public DotTask(String label) {
		super(null, null);
		
		setSelectable(true);
		
		setOption("shape", "box");
		setOption("style", "rounded,filled");
		setOption("fillcolor", "#ffffff:#ffffcb");
		setOption("gradientangle", "300");
		setOption("color", "#303030");
		setOption("width", "0.5");
		setOption("fontcolor", "#303030");
		setOption("fontname", "sans-serif");
		setOption("fontsize", "12");
		setOption("penwidth", "1");
		
		setLabel("<<table border='0'><tr><td height='30'>" + label + "</td></tr></table>>");

//		addMouseListener(new MouseAdapter() {
//			@Override
//			public void mousePressed(MouseEvent e) {
//				if (SwingUtilities.isRightMouseButton(e)) {
//					System.out.println("aaaa -> " + label);
//
//					JPopupMenu menu = new JPopupMenu();
//					menu.add(new JMenuItem("Edit"));
//					menu.addSeparator();
//
//					menu.show(e.getComponent(), e.getX(), e.getY());
//				}
//			}
//		});
	}
}
