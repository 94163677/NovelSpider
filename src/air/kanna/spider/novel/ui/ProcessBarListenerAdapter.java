package air.kanna.spider.novel.ui;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

import air.kanna.spider.novel.spider.ProcessListener;

public class ProcessBarListenerAdapter implements ProcessListener {
	private JProgressBar progressBar;
	private JLabel messageLb;
	
	private int max = 100;
	private int current = 0;
	
	public ProcessBarListenerAdapter(JProgressBar progressBar, JLabel messageLb){
		if(progressBar == null || messageLb == null){
			throw new NullPointerException("JProgressBar or JLabel is null");
		}
		this.progressBar = progressBar;
		this.messageLb = messageLb;
	}
	
	@Override
	public void setMax(int max) {
		if(max <= 0){
			throw new IllegalArgumentException("ProcessBar's max must > 0");
		}
		this.max = max;
		this.current = 0;
		
		progressBar.setMaximum(max);
		progressBar.setMinimum(0);
		progressBar.setValue(0);
		messageLb.setText("");
	}

	@Override
	public void next(String message) {
		current++;
		if(current > max){
			progressBar.setValue(max);
		}else{
			progressBar.setValue(current);
		}
		messageLb.setText(message);
	}

	@Override
	public void setPosition(int current, String message) {
		if(current <= 0){
			progressBar.setValue(0);
			this.current = 0;
		}else
		if(current > max){
			progressBar.setValue(max);
			this.current = max;
		}else{
			progressBar.setValue(current);
			this.current = current;
		}
		messageLb.setText(message);
	}

	@Override
	public void finish(String message) {
		progressBar.setValue(max);
		messageLb.setText(message);
	}

}
