package plg.visualizer;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

import org.processmining.plugins.graphviz.visualisation.DotPanel;

import plg.model.Process;
import plg.model.activity.Task;
import plg.model.data.DataObject;
import plg.model.data.IDataObjectOwner;
import plg.model.data.IntegerDataObject;
import plg.model.data.StringDataObject;
import plg.visualizer.listeners.DataObjectListener;
import plg.visualizer.listeners.TaskListener;
import plg.visualizer.model.DotModel;
import plg.visualizer.util.ImagesCollection;

import javax.swing.*;

/**
 * This widget is used to visualize a BPMN model.
 * 
 * @author Andrea Burattin
 */
public class GraphvizBPMNVisualizer extends DotPanel {

	private static final long serialVersionUID = -8441909033110442685L;
	protected Set<TaskListener> activityListeners = new HashSet<TaskListener>();
	protected Set<DataObjectListener> dataObjectListeners = new HashSet<DataObjectListener>();
	protected Process process;
	
	/**
	 * Class constructor
	 * 
	 * @param process the process graph to show
	 */
	public GraphvizBPMNVisualizer(Process process) {
		super(new DotModel(process));

		this.process = process;

		setOpaque(true);
		setBackground(Color.WHITE);

		configureListeners();
	}

	/**
	 * This method register the provided {@link TaskListener}
	 *
	 * @param listener the listener to register
	 */
	public void addTaskListener(TaskListener listener) {
		activityListeners.add(listener);
	}

	/**
	 * This method register the provided {@link DataObjectListener}
	 *
	 * @param listener the listener to register
	 */
	public void addDataObjectListener(DataObjectListener listener) {
		dataObjectListeners.add(listener);
	}

	private void configureListeners() {
		DotModel dotModel = (DotModel) getDot();
		for (final Task task : process.getTasks()) {
			dotModel.getNode(task.getId()).addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					if (SwingUtilities.isRightMouseButton(e)) {
						JPopupMenu menu = generateContextMenu(task);
						menu.show(e.getComponent(), e.getX(), e.getY());
					}
				}
			});
		}
	}

	private JPopupMenu generateContextMenu(final Task task) {
		// activity data objects
		JMenu dataObjActivity = new JMenu("Generated Data Object");
		dataObjActivity.setIcon(ImagesCollection.ICON_DATA_OBJ);
		boolean added = false;
		for (final DataObject obj : task.getDataObjects(IDataObjectOwner.DATA_OBJECT_DIRECTION.GENERATED)) {
			JMenu objMenu = new JMenu(obj.getName());
			JMenuItem objMenuEdit = new JMenuItem("Edit", ImagesCollection.ICON_DATA_OBJ_EDIT);
			JMenuItem objMenuDelete = new JMenuItem("Delete", ImagesCollection.ICON_DATA_OBJ_DELETE);

			objMenuEdit.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					for (DataObjectListener l : dataObjectListeners) {
						l.editDataObjects(obj);
					}
				}
			});
			objMenuDelete.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					for (DataObjectListener l : dataObjectListeners) {
						l.removeDataObjects(obj);
					}
				}
			});

			objMenu.add(objMenuEdit);
			objMenu.add(objMenuDelete);

			dataObjActivity.add(objMenu);
			added = true;
		}

		JMenuItem addDataObjPlain = new JMenuItem("Plain data object");
		JMenuItem addDataObjScriptInteger = new JMenuItem("Script data object (integer)");
		JMenuItem addDataObjScriptString = new JMenuItem("Script data object (string)");
		addDataObjPlain.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (DataObjectListener l : dataObjectListeners) {
					l.addDataObjects(task, IDataObjectOwner.DATA_OBJECT_DIRECTION.GENERATED, DataObject.class);
				}
			}
		});
		addDataObjScriptInteger.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (DataObjectListener l : dataObjectListeners) {
					l.addDataObjects(task, IDataObjectOwner.DATA_OBJECT_DIRECTION.GENERATED, IntegerDataObject.class);
				}
			}
		});
		addDataObjScriptString.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (DataObjectListener l : dataObjectListeners) {
					l.addDataObjects(task, IDataObjectOwner.DATA_OBJECT_DIRECTION.GENERATED, StringDataObject.class);
				}
			}
		});
		if (added) {
			dataObjActivity.addSeparator();
		}
		JMenu addDataObjMenu = new JMenu("New...");
		addDataObjMenu.setIcon(ImagesCollection.ICON_DATA_OBJ_NEW);
		addDataObjMenu.add(addDataObjPlain);
		addDataObjMenu.add(addDataObjScriptString);
		addDataObjMenu.add(addDataObjScriptInteger);
		dataObjActivity.add(addDataObjMenu);

		// incoming data objects
		JMenu dataObjIncoming = new JMenu("Required Data Object");
		dataObjIncoming.setIcon(ImagesCollection.ICON_DATA_OBJ);
		added = false;
		for (final DataObject obj : task.getDataObjects(IDataObjectOwner.DATA_OBJECT_DIRECTION.REQUIRED)) {
			JMenu objMenu = new JMenu(obj.getName());
			JMenuItem objMenuEdit = new JMenuItem("Edit", ImagesCollection.ICON_DATA_OBJ_EDIT);
			JMenuItem objMenuDelete = new JMenuItem("Delete", ImagesCollection.ICON_DATA_OBJ_DELETE);

			objMenuEdit.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					for (DataObjectListener l : dataObjectListeners) {
						l.editDataObjects(obj);
					}
				}
			});
			objMenuDelete.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					for (DataObjectListener l : dataObjectListeners) {
						l.removeDataObjects(obj);
					}
				}
			});

			objMenu.add(objMenuEdit);
			objMenu.add(objMenuDelete);

			dataObjIncoming.add(objMenu);
			added = true;
		}

		JMenuItem addIncomingDataObjPlain = new JMenuItem("Plain data object");
		JMenuItem addIncomingDataObjScriptInteger = new JMenuItem("Script data object (integer)");
		JMenuItem addIncomingDataObjScriptString = new JMenuItem("Script data object (string)");
		addIncomingDataObjPlain.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (DataObjectListener l : dataObjectListeners) {
					l.addDataObjects(task, IDataObjectOwner.DATA_OBJECT_DIRECTION.REQUIRED, DataObject.class);
				}
			}
		});
		addIncomingDataObjScriptInteger.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (DataObjectListener l : dataObjectListeners) {
					l.addDataObjects(task, IDataObjectOwner.DATA_OBJECT_DIRECTION.REQUIRED, IntegerDataObject.class);
				}
			}
		});
		addIncomingDataObjScriptString.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (DataObjectListener l : dataObjectListeners) {
					l.addDataObjects(task, IDataObjectOwner.DATA_OBJECT_DIRECTION.REQUIRED, StringDataObject.class);
				}
			}
		});
		if (added) {
			dataObjIncoming.addSeparator();
		}
		JMenu addIncomingDataObjMenu = new JMenu("New...");
		addIncomingDataObjMenu.setIcon(ImagesCollection.ICON_DATA_OBJ_NEW);
		addIncomingDataObjMenu.add(addIncomingDataObjPlain);
		addIncomingDataObjMenu.add(addIncomingDataObjScriptString);
		addIncomingDataObjMenu.add(addIncomingDataObjScriptInteger);
		dataObjIncoming.add(addIncomingDataObjMenu);

		// time menus
		JMenuItem duration = new JMenuItem("Activity time", ImagesCollection.ICON_TIME);
		duration.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (TaskListener l : activityListeners) {
					l.setTaskTime(task);
				}
			}
		});

		JPopupMenu menu = new JPopupMenu();
		menu.add(duration);
		menu.addSeparator();
		menu.add(dataObjActivity);
		menu.add(dataObjIncoming);

		return menu;
	}
}
