package org.bp.state;


import java.util.HashMap;


public class StateService {
	private HashMap<String, StateMachine> processingStates=new HashMap<>();

	public StateService(StateMachineBuilder stateMachineBuilder) {
		this.stateMachineBuilder = stateMachineBuilder;
	}


	private StateMachineBuilder stateMachineBuilder = null;

	public ProcessingState sendEvent(String shopReqId, ProcessingEvent event) {
		StateMachine stateMachine;
		synchronized(this){
			stateMachine = processingStates.get(shopReqId);
			if (stateMachine==null) {
				stateMachine=stateMachineBuilder.build();
				processingStates.put(shopReqId, stateMachine);
			}
		}
		return stateMachine.sendEvent(event);
		
	}

	public void removeState(String bookingId) {
		processingStates.remove(bookingId);
	}

	public synchronized ProcessingState getState(String id) {
		return processingStates.get(id).getState();
	}
}
