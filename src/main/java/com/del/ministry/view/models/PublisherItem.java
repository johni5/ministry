package com.del.ministry.view.models;

import com.del.ministry.db.Publisher;

public class PublisherItem implements Comparable<PublisherItem> {

    private Publisher publisher;
    private boolean fullName;

    public PublisherItem(Publisher publisher, boolean fullName) {
        this.publisher = publisher;
        this.fullName = fullName;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    @Override
    public String toString() {
        return fullName ? publisher.getFullFIO() : publisher.getFIO();
    }

    @Override
    public int compareTo(PublisherItem o) {
        return publisher.getFullFIO().compareTo(o.publisher.getFullFIO());
    }

}
