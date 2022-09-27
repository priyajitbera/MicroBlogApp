package com.priyajit.microblogapp.entity;

public interface EntityOwnerDetails {

    String getOwner();

    boolean matchOwnerWithAutenticatedUser();
}
