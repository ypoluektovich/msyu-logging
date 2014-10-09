package org.msyu.logging.example;

import org.msyu.logging.LogFactory;
import org.msyu.logging.PreserveParameterNames;
import org.msyu.logging.Slf4jLogBackend;

public class Main {

	@PreserveParameterNames
	private static interface Log {
		void outerStart(int count);
		void innerStart(int outer, int count);
		void inner(int value);
	}

	static {
		LogFactory.setDefaultBackend(new Slf4jLogBackend());
	}

	private static final Log log = LogFactory.getDefault().createLogFor(Log.class);

	public static void main(String[] args) {
		processOuter(3);
	}

	private static void processOuter(int count) {
		log.outerStart(count);
		for (int i = 0; i < count; ++i) {
			processInner(i, i + 1);
		}
	}

	private static void processInner(int outer, int count) {
		log.innerStart(outer, count);
		for (int i = 0; i < count; ++i) {
			int value = outer * 10 + i;
			log.inner(value);
		}
	}

}
