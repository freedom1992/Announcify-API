package com.announcify.queue;

public class SleepyThread {
	// TODO: use ThreadPool?
	private Thread thread;

	private int sleepSeconds;
	private final Queue queue;

	public SleepyThread(final Queue queue) {
		this.queue = queue;
	}

	public void sleepFor(final int seconds) {
		sleepSeconds = seconds;

		thread = new Thread(new SleepyRunnable());
		thread.start();
	}

	public void interrupt() {
		if (thread != null && thread.isAlive()) {
			thread.interrupt();
		}
	}

	private class SleepyRunnable implements Runnable {
		public void run() {
			try {
				Thread.sleep(sleepSeconds);

				queue.next();
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}