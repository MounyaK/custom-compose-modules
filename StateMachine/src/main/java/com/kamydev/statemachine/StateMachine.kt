package com.kamydev.statemachine


//TODO: Implement Side effects
//TODO: Add control (try/catch ect)
//TODO: Get rid of logging
class StateMachine<VIEWSTATE,EVENT,SIDE_EFFECT>(
    initialViewState :VIEWSTATE,
) {
    private val initialState = initialViewState
    var currentState = initialViewState
        private  set
    private val stateList = mutableListOf<State<VIEWSTATE, EVENT>>()
    private val sideEffectList = mutableListOf<SideEffect<SIDE_EFFECT>>()


    var onInvalidTransition: (EVENT)-> Unit = {} //Example trigger a reset/debugging Event

    @JvmName("triggerEvent")
    fun trigger(event:EVENT, data: Any?=null){
        val state = stateList.first { it.name == currentState }
        val transition = state.getTransition(event)
        if (transition == null){
            onInvalidTransition(event)
        } else {
            transition.actions(data)
            currentState = transition.destSTate
        }
    }
    @JvmName("triggerSideEffect")
    fun trigger(sideEffect: SIDE_EFFECT, data: Any? = null){
        val se = sideEffectList.first { it.name == sideEffect }
        se.actions(data)
    }

    fun whenState(state:VIEWSTATE,init:(State<VIEWSTATE, EVENT>)->Unit){
        val s = State<VIEWSTATE,EVENT>(state)
        init(s)
        stateList.add(s)
    }

    fun whenSideEffect(sideEffect: SIDE_EFFECT,init:(SideEffect<SIDE_EFFECT>)->Unit){
        val s = SideEffect(sideEffect)
        init(s)
        sideEffectList.add(s)
    }

    companion object Factory {

        fun <VIEWSTATE,EVENT,SIDE_EFFECT> create(initialState: VIEWSTATE, initialize: (StateMachine<VIEWSTATE, EVENT, SIDE_EFFECT>)->Unit) : StateMachine<VIEWSTATE, EVENT, SIDE_EFFECT> {
            val stateMachine = StateMachine<VIEWSTATE,EVENT,SIDE_EFFECT>(initialState)
            initialize(stateMachine)
            return stateMachine
        }

    }

    data class State<VIEWSTATE,EVENT>(
        val name:VIEWSTATE
    ){
        private val transitionMap = mutableMapOf<EVENT, Transition<VIEWSTATE>>()

        fun onEvent(event:EVENT, init:(Transition<VIEWSTATE>)->Unit){
            val transition = Transition(name)
            init(transition)
            transitionMap[event] = transition
        }

        fun getTransition(event:EVENT): Transition<VIEWSTATE>? {
            return transitionMap[event]
        }

    }

    data class SideEffect<SIDE_EFFECT>(
        val name:SIDE_EFFECT
    ){
        var actions:(Any?) -> Unit = {}
        fun run(actions:(Any?) -> Unit){
            this.actions = actions
        }

    }

    data class Transition<VIEWSTATE>(
        val startState:VIEWSTATE
    ){
        var destSTate:VIEWSTATE = startState
            private set
        var actions:(Any?) -> Unit = {}
            private set
        fun run(actions:(Any?) -> Unit){
            this.actions = actions
        }

        fun goTo(state:VIEWSTATE){
            destSTate = state
        }

    }

}