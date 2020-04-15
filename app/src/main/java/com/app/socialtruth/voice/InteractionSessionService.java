package com.app.socialtruth.voice;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.service.voice.VoiceInteractionSession;
import android.service.voice.VoiceInteractionSessionService;

public class InteractionSessionService extends VoiceInteractionSessionService {
    public InteractionSessionService() {
    }

    @Override
    public VoiceInteractionSession onNewSession(Bundle args) {
        return new InteractionSession(this);
    }


}
