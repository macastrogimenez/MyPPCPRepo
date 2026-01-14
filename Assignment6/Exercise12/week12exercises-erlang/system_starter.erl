-module(system_starter).
-export([system_start2/0, make_payments/1]).

system_start2() ->
    % Start 2 accounts
    Account1 = spawn(account, init, []),
    Account2 = spawn(account, init, []),
    
    % Start 2 banks (each needs to know about both accounts)
    Bank1 = spawn(bank, init, []),
    Bank2 = spawn(bank, init, []),
    
    % Start 2 mobile apps (each connected to a bank)
    MobileApp1 = spawn(mobile_app, init, [Bank1]),
    MobileApp2 = spawn(mobile_app, init, [Bank2]),
    
    % Return PIDs for easy access from the shell
    #{
        accounts => [Account1, Account2],
        banks => [Bank1, Bank2],
        mobile_apps => [MobileApp1, MobileApp2]
    }.

make_payments(System) ->
    #{mobile_apps := [MApp1, MApp2], accounts := [Acc1, Acc2]} = System,
    
    MApp1 ! {payment_request, Acc1, Acc2, 100},
    MApp2 ! {payment_request, Acc2, Acc1, 50},

    ok.