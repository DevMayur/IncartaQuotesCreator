package com.incarta.quotescreator.synchronization_service;

public interface OnUpdateDatabase {
    public void onStart();
    public void onUpdateAvailable();
    public void onAlreadyUpdated();
    public void onUpdateDownloading();
    public void onUpdateFailed();
    public void onUpdateSucceed();
}
