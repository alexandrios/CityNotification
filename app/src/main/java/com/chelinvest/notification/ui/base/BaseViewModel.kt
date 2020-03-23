package com.chelinvest.notification.ui.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chelinvest.notification.additional.Commands.LOAD
import com.chelinvest.notification.additional.Commands.SIT_IDLE
import com.chelinvest.notification.additional.Commands.STOP_LOADING
import com.chelinvest.notification.additional.Signal
import com.chelinvest.notification.additional.Signal.Companion.FLAG_CAUSES_NAVIGATION
import com.chelinvest.notification.additional.Signal.Companion.FLAG_REQUIRES_LOADING
import java.util.*
import kotlin.collections.HashSet

/**Функционал отправки сигналов фрагменту*/
abstract class BaseViewModel(application: Application): AndroidViewModel(application) {

    val signature: String = this::class.java.name

    //в ожидании подтверждения о выполнении от фрагмента
    private var isWaitingForAcknowledgement: Boolean = false

    //очередь команд на отправку
    private val signalsQueue = LinkedList<Signal>()
    private val signalsEmitterMLive = MutableLiveData<Signal>()
    //на это подписывается фрагмент
    val signalsEmitter: LiveData<Signal> = signalsEmitterMLive

    open fun acknowledgeSignal(signal: Signal){
        if (signal.flags.contains(FLAG_CAUSES_NAVIGATION)){
            isWaitingForAcknowledgement = false
            signalsQueue.clear()
            signalsEmitterMLive.value = Signal(SIT_IDLE, signature, HashSet())
        } else if (signal.signature == signature)
            pollNext()
    }

    //сюда кидаем команду на отправку
    protected fun enqueueCommand(command: String, vararg flags: Int) {
        val flagsSet = flags.toCollection(HashSet())
        if (flagsSet.contains(FLAG_REQUIRES_LOADING)) {
            signalsQueue.add(Signal(LOAD, signature, flagsSet))
            signalsQueue.add(Signal(command, signature, flagsSet))
            signalsQueue.add(Signal(STOP_LOADING, signature, flagsSet))
        } else {
            signalsQueue.add(Signal(command, signature, flagsSet))
        }
        if (!isWaitingForAcknowledgement) {
            isWaitingForAcknowledgement = true
            pollNext()
        }
    }

    private fun pollNext() {
        if (signalsQueue.isEmpty()) {
            isWaitingForAcknowledgement = false
        } else
            signalsEmitterMLive.postValue(signalsQueue.poll())
    }

}