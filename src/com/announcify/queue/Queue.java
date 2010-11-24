package com.announcify.queue;

import java.util.LinkedList;

import android.content.Context;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;

import com.announcify.sql.model.PluginModel;
import com.announcify.tts.Speaker;

public class Queue implements OnUtteranceCompletedListener {
	private final Context context;
	private final Speaker speaker;
	private final SleepyThread thread;
	private final WakeLocker wakeLocker;
	private final PluginModel model;

	private LinkedList<LittleQueue> queue;

	private boolean granted;

	public Queue(final Context context, final Speaker speaker) {
		this.context = context;
		wakeLocker = new WakeLocker(context);
		this.speaker = speaker;
		thread = new SleepyThread(this);
		model = new PluginModel(context);
	}

	public void grant() {
		granted = true;
		if (!wakeLocker.isLocked()) {
			wakeLocker.lock();
		}

		next();
	}

	public void deny() {
		granted = false;
		if (wakeLocker.isLocked()) {
			wakeLocker.unlock();
		}

		interrupt();
	}

	public void next() {
		if (!granted) {
			return;
		}

		checkNext();

		final Object toDo = queue.getFirst().getNext();
		if (toDo instanceof String) {
			speaker.speak((String) toDo);
		} else if (toDo instanceof Integer) {
			thread.sleepFor((Integer) toDo);
		}
	}

	private void checkNext() {
		if (queue.size() == 0) {
			end();
		}

		if (queue.getFirst().isEmpty()) {
			queue.getFirst().stop();
			queue.removeFirst();

			if (model.getActive(queue.getFirst().getPluginName())) {
				queue.getFirst().start();
			} else {
				queue.removeFirst();
				checkNext();
			}
		}

		if (queue.size() == 0) {
			end();
		}
	}

	public void onUtteranceCompleted(final String utteranceId) {
		next();
	}

	public void putLast(final LittleQueue little) {
		queue.add(little);
	}

	public void putFirst(final LittleQueue little) {
		queue.add(0, little);
		interrupt();
		next();
	}

	private void interrupt() {
		thread.interrupt();
		speaker.interrupt();
	}

	private void end() {
		// TODO: send broadcast?

		interrupt();

		model.close();

		if (wakeLocker.isLocked()) {
			wakeLocker.unlock();
		}
	}
}