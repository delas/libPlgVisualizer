package plg.visualizer.prototype;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import plg.exceptions.IllegalSequenceException;
import plg.exceptions.InvalidProcessException;
import plg.generator.process.ProcessGenerator;
import plg.generator.process.RandomizationConfiguration;
import plg.model.Process;
import plg.model.activity.Task;
import plg.model.data.DataObject;
import plg.model.data.IDataObjectOwner.DATA_OBJECT_DIRECTION;
import plg.model.event.EndEvent;
import plg.model.event.StartEvent;
import plg.model.gateway.Gateway;
import plg.visualizer.BPMNVisualizer;
import plg.visualizer.BPMNVisualizer2;

public class PlgVisualizerPrototype {

	public static void main(String[] args) throws IllegalSequenceException, InvalidProcessException {
		Process p = new Process("test");
//		p = generateProcess();
		ProcessGenerator.randomizeProcess(p, RandomizationConfiguration.BASIC_VALUES);
		BPMNVisualizer2 v = new BPMNVisualizer2(p);
		
		JFrame f = new JFrame("Test Frame");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(800, 300);
		f.setLayout(new BorderLayout());
		f.add(v, BorderLayout.CENTER);
		f.setVisible(true);
	}
	
	private static Process generateProcess() throws IllegalSequenceException, InvalidProcessException {
		Process p = new Process("test");
		StartEvent start = p.newStartEvent();
		EndEvent end = p.newEndEvent();
		Gateway split = p.newParallelGateway();
		Gateway join = p.newExclusiveGateway();
		Task a = p.newTask("Test activity");
		Task b = p.newTask("b");
		Task c = p.newTask("c");
		Task d = p.newTask("d");
		Task e = p.newTask("e");
		Task f = p.newTask("f");
		p.newSequence(start, a);
		p.newSequence(a, split);
		p.newSequence(split, b); p.newSequence(b, join);
		p.newSequence(split, c); p.newSequence(c, join);
		p.newSequence(split, d); p.newSequence(d, join);
		p.newSequence(split, e); p.newSequence(e, join);
		p.newSequence(e, join);
		p.newSequence(join, f);
		p.newSequence(f, end);
		
		new DataObject(p).set("d1", "v1");
		new DataObject(p, b, DATA_OBJECT_DIRECTION.REQUIRED).set("d2", "v2");
		new DataObject(p, c, DATA_OBJECT_DIRECTION.GENERATED).set("d3", "v3");
		
		p.check();
		return p;
	}
}