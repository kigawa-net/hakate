package net.kigawa.hakate.impl.state

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import net.kigawa.hakate.api.state.StateContext
import net.kigawa.hakate.api.state.StateDispatcher
import net.kigawa.hakate.impl.Utl.suspendApply

class StateContextImpl(
    private val dispatcher: StateDispatcher,
    private val scope: CoroutineScope,
) : StateContext {
    override fun dispatcher(): StateDispatcher = dispatcher
    override fun dispatch(
        block: suspend StateContext.() -> Unit,
    ): Job {
        return scope.launch {
            supervisorScope {
                StateContextImpl(dispatcher, this@supervisorScope).suspendApply {
                    block()
                }
            }
        }
    }
}