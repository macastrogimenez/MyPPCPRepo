% raup@itu.dk * 2024-11-14

-module(mobile_app).

-export([start/1, init/1, send_N_requests/4]).

start(BankPid) ->
        spawn(?MODULE, init, [BankPid]).

init(BankPid) ->
    loop(BankPid).

loop(BankPid) ->
    receive
        {payment_request, Account1, Account2, Amount} ->
            BankPid ! {transaction, Account1, Account2, Amount},
            loop(BankPid)
    end.

send_N_requests(BankPid, N, Account1, Account2) when N > 0 ->
    BankPid ! {transaction, Account1, Account2, 1},
    send_N_requests(BankPid, N - 1, Account1, Account2);

send_N_requests(_BankPid, 0, _Acc1, _Acc2) ->
    ok.
