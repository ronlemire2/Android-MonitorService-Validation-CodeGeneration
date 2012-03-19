package dev.ronlemire.personprovider;
/*
 * An interface implemented typically by an activity
 * so that a worker class can report back
 * on what happened.
 */
public interface ITesterReportBack {
	public void reportBack(String tag, String message);
	public void clear();
}