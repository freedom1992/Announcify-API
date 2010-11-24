package com.announcify.queue;

import java.util.LinkedList;

import android.content.Context;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;

import com.announcify.tts.Speaker;

public class Queue implements OnUtteranceCompletedListener {
	private final Context context;
	private final Speaker speaker;
	private final SleepyThread thread;
	private final WakeLocker wakeLocker;

	private LinkedList<LinkedList<Object>> queue;

	private boolean granted;

	public Queue(Context context, final Speaker speaker) {
		this.context = context;
		wakeLocker = new WakeLocker(context);
		this.speaker = speaker;
		thread = new SleepyThread(this);
	}

	public void grant() {
		granted = true;
		if (!wakeLocker.isLocked()) wakeLocker.lock();

		next();
	}

	public void deny() {
		granted = false;
		if (wakeLocker.isLocked()) wakeLocker.unlock();

		interrupt();
	}

	public void next() {
		if (!granted) {
			return;
		}

		if (queue.size() == 0) {
			end();
		}

		if (queue.getFirst().size() == 0) {
			finished();
		}

		if (queue.size() == 0) {
			end();
		}

		final Object toDo = queue.getFirst().poll();
		if (toDo instanceof String) {
			speaker.speak((String) toDo);
		} else if (toDo instanceof Integer) {
			thread.sleepFor((Integer) toDo);
		}
	}

	public void onUtteranceCompleted(final String utteranceId) {
		next();
	}

	public void putLast(final LinkedList<Object> list) {
		queue.add(list);
	}

	public void putFirst(final LinkedList<Object> list) {
		queue.add(0, list);
		interrupt();
		next();
	}

	public void interrupt() {
		thread.interrupt();
		speaker.interrupt();
	}

	private void finished() {
		// TODO: save list-head (Serializable?) to repeat it later (if user wants to do so, using RemoteControlDialog)
		queue.removeFirst();
		// TODO: send broadcast?
	}

	private void end() {
		if (wakeLocker.isLocked()) wakeLocker.unlock();
		// TODO: send broadcast?
	}
}