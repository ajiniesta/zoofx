package com.iniesta.zoofx.services;

import com.iniesta.zoofx.views.Dialogs;

import javafx.beans.value.ChangeListener;
import javafx.concurrent.Service;

public abstract class ThrowableService<T> extends Service<T> {

	public ThrowableService() {
		stateProperty()
				.addListener((ChangeListener<javafx.concurrent.Worker.State>) (observable, oldValue, newValue) -> {
					Throwable ex = getException();
					if(ex!=null){
						Dialogs.showException(ex);
					}
				});
	}

}
