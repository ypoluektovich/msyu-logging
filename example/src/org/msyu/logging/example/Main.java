package org.msyu.logging.example;

import org.msyu.logging.LogFactory;
import org.msyu.logging.PreserveParameterNames;
import org.msyu.logging.SystemOutLogBackend;

public class Main {

	private static interface Desu {
		public static final String[] desu = {};
	}

	@PreserveParameterNames
	private static interface Log {
		void outerStart(int count);
		void innerStart(int outer, int count);
		void inner(int value);
	}

	private static final Log log = new LogFactory(new SystemOutLogBackend()).createLogFor(Log.class);

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
