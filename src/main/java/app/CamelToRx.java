package app;

import io.reactivex.Flowable;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.reactive.streams.api.CamelReactiveStreamsService;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Configuration
public class CamelToRx {

    @Component
    public static class BasicCamelToReactorExampleStreams {
        private static final Logger LOG = LoggerFactory.getLogger(CamelToRx.class);

        @Autowired
        private CamelReactiveStreamsService camel;

        @PostConstruct
        public void setupStreams() {
            Publisher<Integer> numbers = camel.fromStream("numbers", Integer.class);
            Publisher<String> strings = camel.fromStream("strings", String.class);

            Flowable.fromPublisher(numbers)
                    .zipWith(strings, (a, b) -> "BasicCamelToReactor - " + a + " -> " + b)
                    .doOnNext(LOG::info)
                    .subscribe();
        }

    }

    @Component
    public static class CamelToRxRoute extends RouteBuilder {

        @Override
        public void configure() throws Exception {
            from("timer:clock?period=5000")
                    .setBody().header(Exchange.TIMER_COUNTER)
                    .to("reactive-streams:numbers");

            from("timer:clock2?period=4900&delay=2000")
                    .setBody().simple("Hello World ${header.CamelTimerCounter}!")
                    .to("reactive-streams:strings");

        }

    }

}
