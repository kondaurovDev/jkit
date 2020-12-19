package jkit.entry;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.ArrayList;

class UserLog implements Publisher<String> {

    public static UserLog create() {
        return new UserLog();
    }

    private final ArrayList<Subscriber<? super String>> subscribers =
        new ArrayList<>();

    public void subscribe(Subscriber<? super String> s) {
        subscribers.add(s);
    }

    public Publisher<String> getPublisher() {
        return this;
    }

    public void end() {
        subscribers.forEach(Subscriber::onComplete);
        subscribers.clear();
    }

    public void add(String msg) {
        subscribers.forEach(s -> s.onNext(msg));
    }

}
