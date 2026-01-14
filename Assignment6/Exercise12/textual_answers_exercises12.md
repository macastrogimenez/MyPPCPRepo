# Exercise 12

## 12.1

### 12.1.1
-

### 12.1.2
```
HELLO
WORLD
!
.
hello
world
!
.
```

### 12.1.3
It is always the same. Presumably because I'm running the program on a local machine without a "real" network between the sender and the receiver. So the fact that UDP doesn't guarantee message ordering doesn't result in reordered messages.

## 12.2

### 12.2.1
-

### 12.2.2
-

### 12.2.3
```sh
$ gradle -PmainClass=week12exercises.MobileApp run

# no output
```

```sh
$ gradle -PmainClass=week12exercises.Bank run
# --- omitted ---
{8081,8082!100}
{8081,8082!-50}
{8082,8081!17}
{8081,8082!0}
```

```sh
$ gradle -PmainClass=week12exercises.Account1 run
# --- omitted ---
Mes: -100 Amount; -100
Account1; -100
Mes: 50 Amount; 50
Account1; -50
Mes: 17 Amount; 17
Account1; -33
Mes: 0 Amount; 0
Account1; -33
```

```sh
$ gradle -PmainClass=week12exercises.Account2 run
Mes: 100 Amount; 100
Account2; 100
Mes: -50 Amount; -50
Account2; 50
Mes: -17 Amount; -17
Account2; 33
Mes: 0 Amount; 0
Account2; 33
```

## 12.3

### 12.3.1

> Design and implement the Mobile App actor (see the file mobile_app.erl in the skeleton), including the messages it must handle. Explain your design choices, e.g., elements of the state (if any), how it is initialized, purpose of messages, etc.

The mobile_app actor is initialized with the process ID of a bank, which is the only piece of state. It can receive one message, `payment_request` which sends a `transaction` message to a bank.

### 12.3.2

> Design and implement the Bank actor (see the file bank.erl in the skeleton), including the messages it must handle. Explain your design choices, e.g., elements of the state (if any), how it is initialized, purpose of messages, etc.

The `Bank` actor is stateless. Initialization only runs the receiving loop. It receives `transaction` messages and sends `deposit` messages to account actors to increase/decrease their balance.

### 12.3.3

> Design and implement the Account actor (see the file account.erl in the skeleton), including the messages it must handle. Explain your design choices, e.g., elements of the state (if any), how it is initialized, purpose of messages, etc.

The `Account` actor holds a balance as the only piece of state. Initialization sets the balance to zero. It receives two messages: `deposit` to increase/decrease the balance, `get_balance` to send back the balance to the sender of the message.

### 12.3.4

> Write a function, system_start2, that starts 2 mobile apps, 2 banks, and 2 accounts. After executing the function, it must be possible to make payment requests via the mobile app actors. You may directly provide the code of the function so that it can be executed from the shell (erl), or you can create a special module containing the function; you may name this module system_starter.erl.

See `system_starter.erl`. Usage:

```
% Compile all modules
c(account).
c(bank).
c(mobile_app).
c(system_starter).

% Start the system
System = system_starter:system_start2().

system_starter:make_payments(System).

% Check balances
#{accounts := [Acc1, Acc2]} = System.
Acc1 ! {self(), get_balance}.
Acc2 ! {self(), get_balance}.

flush().
```

### 12.3.5

```
% Compile all modules
c(account).
c(bank).
c(mobile_app).
c(system_starter).

A1 = account:start().
A2 = account:start().
B = bank:start().
A = mobile_app:start(B).

mobile_app:send_N_requests(B, 10, A1, A2).

A1 ! {self(), get_balance}.
A2 ! {self(), get_balance}.

flush().
```

### 12.3.6

```
% Compile all modules
c(account).
c(bank).
c(mobile_app).
c(system_starter).

A1 = account:start().
A2 = account:start().
B = bank:start().
A = mobile_app:start(B).

mobile_app:send_N_requests(B, 100, A1, A2).

A1 ! {self(), get_balance}.
A2 ! {self(), get_balance}.

flush().
```

> What balance was printed?

A1: -100
A2: 100

> Is that the only possible balance that could be printed?

No. There's no guarantee that all the transactions have already finished (succesfully).

> What are the possible balances that could be printed after executing this function?

For A1, any value between -100 and 0. For A2, any value between 0 and 100. Since there are no guarantees that any of the operations succeed and the operation are not atomic (we could withdraw money from one account, but not deposit another account), we can get any value in between the possible ranges.

Only guarantee is that A1 will be decrementing towards -100 and A2 incrementing towards 100.

### 12.3.7

> Consider a case where two different bank actors send two deposits exactly at the same time to the same account actor. Can data races occur when updating the balance? Explain your answer.

No, no data races will occur because actors in erlang receive messages into an inbox and then process the messages in the inbox sequentially. Therefore, messages cannot interfere with each other.