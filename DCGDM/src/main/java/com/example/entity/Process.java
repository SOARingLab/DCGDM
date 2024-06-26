package com.example.entity;

import java.util.HashSet;
import java.util.Set;

public class Process {
    String participant;
    Set<String> MessageSent;
    Set<String> MessageReceived;


    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    public Set<String> getMessageSent() {
        return MessageSent;
    }

    public void setMessageSent(Set<String> messageSent) {
        MessageSent = messageSent;
    }

    public Set<String> getMessageReceived() {
        return MessageReceived;
    }

    public void setMessageReceived(Set<String> messageReceived) {
        MessageReceived = messageReceived;
    }

    public Process(String participant, Set<String> messageSent, Set<String> messageReceived) {
        this.participant = participant;
        MessageSent = messageSent;
        MessageReceived = messageReceived;
    }

    public Process() {
        this.participant = "";
        this.MessageSent = new HashSet<String>();;
        this.MessageReceived = new HashSet<String>();;
    }

    @Override
    public String toString() {
        return "Process{" +"\n" +
                "participant='" + participant + "'" + "\n" +
                "MessageSent=" + MessageSent +"\n" +
                "MessageReceived=" + MessageReceived +"\n" +
                '}';
    }

    public Process(String filename) {
        this.participant = filename;
        this.MessageSent = new HashSet<String>();;
        this.MessageReceived = new HashSet<String>();;
    }

}
