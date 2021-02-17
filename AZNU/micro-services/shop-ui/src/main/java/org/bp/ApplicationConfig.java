package org.bp;



import org.apache.camel.CamelContext;
import org.apache.camel.impl.saga.InMemorySagaService;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.bp.state.ProcessingEvent;
import org.bp.state.ProcessingState;
import org.bp.state.StateMachineBuilder;
import org.bp.state.StateService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;


@Configuration
public class ApplicationConfig {


    @Bean
    public CamelContextConfiguration integrationContextMetadataConfiguration() {
        return new CamelContextConfiguration() {
 
            @Override
            public void beforeApplicationStart(CamelContext camelContext) {
			    try {
					camelContext.addService(new InMemorySagaService());
					camelContext.setMessageHistory(true);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
            }
 
            @Override
            public void afterApplicationStart(CamelContext camelContext) {
                // nop
            }
        };
    }

    @Bean(name="basicStateMachineBuilder")
    public StateMachineBuilder basicStateMachineBuilder() {
        StateMachineBuilder smb = new StateMachineBuilder();
        smb.initialState(ProcessingState.NONE)
                .add(ProcessingState.NONE, ProcessingEvent.START,ProcessingState.STARTED)
                .add(ProcessingState.STARTED,ProcessingEvent.FINISH,ProcessingState.FINISHED)
                .add(ProcessingState.NONE,ProcessingEvent.CANCEL,ProcessingState.CANCELLED)
                .add(ProcessingState.STARTED,ProcessingEvent.CANCEL,ProcessingState.CANCELLED)
                .add(ProcessingState.FINISHED,ProcessingEvent.CANCEL,ProcessingState.CANCELLED)
                .add(ProcessingState.CANCELLED,ProcessingEvent.START,ProcessingState.CANCELLED)
                .add(ProcessingState.CANCELLED,ProcessingEvent.CANCEL,ProcessingState.CANCELLED)
                .add(ProcessingState.CANCELLED,ProcessingEvent.FINISH,ProcessingState.CANCELLED)
                .add(ProcessingState.FINISHED,ProcessingEvent.COMPLETE,ProcessingState.COMPLETED)
                .add(ProcessingState.CANCELLED,ProcessingEvent.COMPLETE,ProcessingState.COMPLETED)
        ;
        return smb;
    }

    @Bean
    @Scope("prototype")
    public StateService stateService(@Qualifier("basicStateMachineBuilder") StateMachineBuilder stateMachineBuilder) {
        return new StateService (stateMachineBuilder);
    }

}