% raup@itu.dk * 2024-11-14

-module(account).

-export([start/0, init/0]).

-record(account_state, { balance }).

start() ->
    spawn(?MODULE, init, []).

init() ->
    State = #account_state{balance = 0},
    loop(State).

loop(State) ->
    receive
        {deposit, Amount} ->
            NewBalance = State#account_state.balance + Amount,
            NewState = State#account_state{balance = NewBalance},
            loop(NewState);
        
        {From, get_balance} ->
            From ! {self(), State#account_state.balance},
            loop(State);

        {From, print_balance} ->
            From ! {self(), State#account_state.balance},
            loop(State)
    end.
