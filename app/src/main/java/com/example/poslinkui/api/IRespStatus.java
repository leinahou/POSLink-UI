package com.example.poslinkui.api;

import android.os.Bundle;
import android.support.annotation.Nullable;

public interface IRespStatus {
    void onAccepted();

    void onDeclined(@Nullable Bundle bundle);
}
