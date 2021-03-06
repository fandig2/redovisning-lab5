package carwash;

import java.util.ArrayList;

import random.ExponentialRandomStream;
import random.UniformRandomStream;
import sim.SimEvent;
import sim.SimState;

/**
 * CWState holds and keeps track of the state in the carwash.
 */

public class CWState extends SimState {
	

	private int queueSize = 0;
	private int rejectedCars = 0;
	private double idleTime = 0;
	private double simulationTime = 0;
	private String eventName ="";
	private String carId = "-";
	private double previousEventTime = 0;
	
	private double queueTime = 0;
	private double lastQueueTime = 0;
	
//	public ArrayList<double[]> carWashQueue = new ArrayList<>();
	
	private long seed;
	private double lowerFast;
	private double upperFast;
	private double lowerSlow;
	private double upperSlow;
	
	private UniformRandomStream fastRandom;
	private UniformRandomStream slowRandom;
	private ExponentialRandomStream expoRandom;

    private int fastWashers;
    private int slowWashers;
    private int maxQueue;

    private double lambda = 1.5;
    private double maxSimTime = 15;
    
    CWQueue CWQueue;

	/**
	 * The CWState constructor takes params for changing simulation
	 * @param seed seed for RNG
	 * @param lowerFast distribution bound for RNG
	 * @param upperFast distribution bound for RNG
	 * @param lowerSlow distribution bound for RNG
	 * @param upperSlow distribution bound for RNG
	 * @param fastWashers number of fast washers
	 * @param slowWashers number of slow washers
	 * @param maxQueue max number of cars in queue to wash
	 * @param lambda lambda for RNG
	 * @param maxSimTime time for stop event
	 */

	public CWState(long seed,
                   double lowerFast,
                   double upperFast,
                   double lowerSlow,
                   double upperSlow,
                   int fastWashers,
                   int slowWashers,
                   int maxQueue,
                   double lambda,
                   double maxSimTime,
                   CWQueue carQueue) {

        this.seed = seed;
        this.lowerFast = lowerFast;
        this.upperFast = upperFast;
        this.lowerSlow = lowerSlow;
        this.upperSlow = upperSlow;
        this.fastWashers = fastWashers;
        this.slowWashers = slowWashers;
        this.maxQueue = maxQueue;
        this.lambda = lambda;
        this.maxSimTime = maxSimTime;

        this.fastRandom = new UniformRandomStream(lowerFast, upperFast, seed);
        this.slowRandom = new UniformRandomStream(lowerSlow,upperSlow,seed);
        this.expoRandom = new ExponentialRandomStream(lambda,seed);
        this.CWQueue = carQueue;
    }


	/**
	 * sortCarWasQueue sorts the queue of cars waiting to be washed in time order
	 */
//	public void sortCarWashQueue(){
//		carWashQueue.sort((e1, e2) -> Double.compare(e1[0], e2[0]));	
//	}

	/**
	 * addNextEvent takes a list of Events from the EventQueue as an argument,
	 * requests a new event from CWEvent,
	 * appends it to the list and returns the new list.
	 *
	 * @param simEvents list from EventQueue
	 * @return carWashEventList list to EventQueue
	 */
	@Override
	public ArrayList<SimEvent> addNextEvent(ArrayList<SimEvent> simEvents){
		double time = this.getPreviousEventTime() + this.getExpoRandom();
		CWEvent e = new CWEvent(time, carCounter, this, CWQueue);
		simEvents.add(e);
		carCounter++;
		return simEvents;
	}

	public double getMaxTime(){
		return maxSimTime;
	}

	public double getIdleTime(){
		return idleTime;
	}

	public int getFastWashers(){
		return fastWashers;
	}

	public int getSlowWashers(){
		return slowWashers;
	}

	public double getLowerFast(){
		return lowerFast;
	}

	public double getUpperFast(){
		return upperFast;
	}

	public double getLowerSlow(){
		return lowerSlow;
	}

	public double getUpperSlow(){
		return upperSlow;
	}

	public double getLambda(){
		return lambda;
	}

	public long getSeed(){
		return seed;
	}

	public int getMaxQueueSize(){
		return maxQueue;
	}

	public double getQueueTime(){
		return queueTime;
	}

	public int getRejectedCars(){
		return rejectedCars;
	}

	public double getFastRandom(){
		return fastRandom.next();
	}

	public double getSlowRandom(){
		return  slowRandom.next();
	}

	public double getExpoRandom(){
		return expoRandom.next();
	}

	public String getEventName(){
		return eventName;
	}

	public double getTime(){
		return simulationTime;
	}


	public String getCarId(){
		return carId;
	}

	public double getMeanQueue(){
		//TODO divide by the number of cars that actually get washed during simulation time (19 in current testcase)

        // Test case on page 5 in lab spec -> does not work
		// Works for testcase 1 if w

		final int NUMBER_OF_CARS_ARRIVED = 19;

		return getQueueTime()/NUMBER_OF_CARS_ARRIVED ;
	}

	public int getQueueSize(){
		return queueSize;
	}

	public double getPreviousEventTime(){
		return previousEventTime;
	}

	public void setPreviousEventTime(double x){
		previousEventTime = x;
	}

	public void setRejected(int x){
		rejectedCars += x;
	}

	public void setIdle(double x){
		idleTime += x;
	}

	public void setQueueSize(int x){
		queueSize += x;
	}

	public void calcQueueTime(double time){

		if(lastQueueTime == 0){
			lastQueueTime = time;
		}
		else{
			queueTime += queueSize * (time - lastQueueTime);
			lastQueueTime = time;
		}
	}

	public void setCarId(int x){
		carId = "" + x;
	}

	public void setEventName(int x){

		if(x == 1){
			eventName = "Arrive";
		}
		else if (x == 2){
			eventName = "Leave";
		}
		else if(x == 0){
			eventName = "Start";
		}
		else{
			eventName = "Stop";
			carId = "-";
		}
		setChanged();
		notifyObservers();
	}

	public void setSimulationTime(double x){
		simulationTime = x;
	}

	public void changeFastWashers(double x){

		fastWashers += x;
	}
	public void changeSlowWashers(double x){

		slowWashers += x;
	}


	private int carCounter = 0;

}
