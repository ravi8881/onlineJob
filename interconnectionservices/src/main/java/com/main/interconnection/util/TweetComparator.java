package com.main.interconnection.util;

import java.util.Comparator;

import org.springframework.social.twitter.api.Tweet;

public class TweetComparator implements Comparator<Tweet> {

    public int compare(Tweet t1, Tweet t2) {
       

        if (t1.getCreatedAt().getTime() < t2.getCreatedAt().getTime()) {
            return -1;
        } else if (t1.getCreatedAt().getTime() > t2.getCreatedAt().getTime()) {
            return 1;
        } else {
            return 0;
        }
    }
}