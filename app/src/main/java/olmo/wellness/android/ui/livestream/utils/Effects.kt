package olmo.wellness.android.ui.livestream.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

class Effects {
    companion object {
        @Composable
        fun Disposable(lifeCycleOwner: LifecycleOwner, onStart: () -> Unit, onStop: () -> Unit) {
            DisposableEffect(lifeCycleOwner) {
                val observer = LifecycleEventObserver { _, event ->
                    when(event){
                        Lifecycle.Event.ON_START->{
                            onStart()
                        }
                        Lifecycle.Event.ON_RESUME->{

                        }
                        Lifecycle.Event.ON_PAUSE->{

                        }
                        Lifecycle.Event.ON_STOP->{
                            onStop()
                        }
                    }
                }

                // Add the observer to the lifecycle
                lifeCycleOwner.lifecycle.addObserver(observer)

                onDispose {
                    lifeCycleOwner.lifecycle.removeObserver(observer)
                }
            }
        }
    }
}