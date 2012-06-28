package dojo

import org.scalatest.FlatSpec

class TimerClock(seconds:Int, val callback: ()=>Unit) {

	def triggerCallback() {
		this.callback();
	}
}

class Pomodoro(val duration:Int=25*60) {
	var finished=false;
	var isRunning=false;
	var clock:TimerClock = null;
	def start() {
		isRunning = true;
		finished = false;
		clock = new TimerClock(duration, ()=>{
			finished=true;
		})
	}
	def hasFinished():Boolean = {
		if(!isRunning) {
			throw new IllegalStateException();
		} 
		return finished;
	}
}

class SampleTest extends FlatSpec {
  

  "A pomodoro" must "last 25 minutes" in {
     val pomodoro = new Pomodoro()
     assert(pomodoro.duration === 25*60)
   }

   it can "be created with an arbitrary duration" in {
   	val pomodoro = new Pomodoro(10*60);
   	assert(pomodoro.duration === 10*60)
   }

   it must "be stopped right after creation" in {
   	val pomodoro = new Pomodoro();
   	assert(false === pomodoro.isRunning)
   }

   it must "start the countdown when started" in {
   	val pomodoro = new Pomodoro();
   	assert(pomodoro.clock == null)
   	pomodoro.start();
   	assert(pomodoro.clock != null)
   }

   it can "not finish if it has not been started before" in {
   	val pomodoro = new Pomodoro();
   	intercept [java.lang.IllegalStateException] {
   		pomodoro.hasFinished();
   	}
   }

   it must "finish when the time is over" in {
   	val pomodoro = new Pomodoro();
   	pomodoro.start();
   	pomodoro.clock.triggerCallback()
   	assert(pomodoro.hasFinished()===true)
   }

}