package com.iniesta.zoofx.views;

import javafx.concurrent.Service;
import javafx.scene.control.ProgressBar;

public class ServiceWorker {

	private ProgressBar progress;
	
	public ServiceWorker(ProgressBar progress) {
		this.progress = progress;
	}

	public void setProgress(ProgressBar progress){
		this.progress = progress;
	}
	
	public void bind(Service<?> service){
		if(progress!=null){
			progress.progressProperty().bind(service.progressProperty());
		}
	}
}
