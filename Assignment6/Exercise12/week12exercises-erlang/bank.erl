% raup@itu.dk * 2024-11-14

-module(bank).

%% export any necessary functions
-export([start/0, init/0]).

start() ->
    spawn(?MODULE, init, []).

init() ->
    loop().

loop() ->
    receive
        {transaction, Account1, Account2, Amount} ->
            Account1 ! {deposit, -Amount},
            Account2 ! {deposit, Amount},
            loop()
    end.