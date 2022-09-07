package com.uuc.system.uuc.sync.service;

import com.uuc.system.uuc.sync.domain.SyncBody;
import com.uuc.system.uuc.sync.domain.UucResourceDto;

public interface ResourceSyncService {

    public UucResourceDto syncBody(Object oldObject, Object newObject, String operationType);

    public SyncBody toSyncBody(String jsonString);
}
