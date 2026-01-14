% raup@itu.dk * 2024-11-22

-module(server).
-export([start/2, init/2, debug_idle_workers/1, debug_state/1]). % Added debug function
-include("defs.hrl").

% 1. State
% The server maintains:
% - busy_workers: List of worker PIDs currently computing tasks
% - idle_workers: List of worker PIDs available for new tasks
% - pending_tasks: Queue of {Task, Client} tuples waiting for available workers
-record(server_state, {min_workers, max_workers, busy_workers, idle_workers, pending_tasks}).

% 2. Start
% Spawns a new server process with the specified number of workers
start(MinWorkers, MaxWorkers) when MinWorkers =< MaxWorkers ->
    spawn(?MODULE, init, [MinWorkers, MaxWorkers]).

% 3. Initialization
% Creates WorkersCount worker processes and initializes the server state
% All workers start as idle, no busy workers, no pending tasks
% Sets up monitors for all workers to detect crashes
init(MinWorkers, MaxWorkers) ->
    Workers = [start_monitored_worker() || _ <- lists:seq(1, MinWorkers)],
    State = #server_state{
        min_workers = MinWorkers,
        max_workers = MaxWorkers,
        busy_workers = [],
        idle_workers = Workers,
        pending_tasks = []
    },
    io:format("Server started with ~p minimum workers and ~p maximum workers~n", 
              [MinWorkers, MaxWorkers]),
    loop(State).

% Helper function to start a worker and monitor it
start_monitored_worker() ->
    WorkerPid = worker:start(self()),
    monitor(process, WorkerPid),
    WorkerPid.

% 4. Behavior upon receiving messages
loop(State) ->
    receive
        {compute, Client, Tasks} ->
            % Client sends a list of tasks to be computed
            NewState = handle_request(Tasks, Client, State),
            loop(NewState);
        {work_done, Worker} ->
            % Worker notifies completion and becomes available
            NewState = handle_work_done(Worker, State),
            loop(NewState);
        {'DOWN', _Ref, process, Worker, Reason} ->
            % Worker crashed or terminated
            NewState = case Reason of
                normal ->
                    % Normal termination (elastic scale-down), just clean up
                    handle_worker_termination(Worker, State);
                _ ->
                    % Abnormal termination (crash), create replacement
                    handle_worker_crash(Worker, Reason, State)
            end,
            loop(NewState);
        {debug_idle_workers, From} ->
            % Debug message to check idle workers
            From ! {idle_workers, State#server_state.idle_workers},
            io:format("Idle workers: ~p~n", [State#server_state.idle_workers]),
            io:format("Busy workers: ~p~n", [State#server_state.busy_workers]),
            io:format("Pending tasks: ~p~n", [length(State#server_state.pending_tasks)]),
            loop(State);
        {debug_state, From} ->
            % Debug message to check full server state
            TotalWorkers = length(State#server_state.idle_workers) + 
                          length(State#server_state.busy_workers),
            From ! {state, State#server_state.min_workers, 
                    State#server_state.max_workers, TotalWorkers,
                    length(State#server_state.idle_workers),
                    length(State#server_state.busy_workers),
                    length(State#server_state.pending_tasks)},
            io:format("Min workers: ~p, Max workers: ~p~n", 
                     [State#server_state.min_workers, State#server_state.max_workers]),
            io:format("Total workers: ~p (Idle: ~p, Busy: ~p)~n",
                     [TotalWorkers, 
                      length(State#server_state.idle_workers),
                      length(State#server_state.busy_workers)]),
            io:format("Pending tasks: ~p~n", [length(State#server_state.pending_tasks)]),
            loop(State)
    end.

% 5. Message handlers

% handle_request/3
% Processes a list of tasks from a client
% For each task:
% - If idle workers available: assign task to idle worker (moves to busy)
% - If no idle workers: add task to pending queue
handle_request([], _Client, State) ->
    State;
handle_request([Task|Rest], Client, State) ->
    #server_state{idle_workers = Idle, busy_workers = Busy, pending_tasks = Pending, max_workers = MaxWorkers} = State,
    
    TotalWorkers = length(Idle) + length(Busy),

    case Idle of
        [Worker|RestIdle] ->
            % Idle worker available: assign task immediately
            Worker ! {compute, Client, Task},
            NewState = State#server_state{
                idle_workers = RestIdle,
                busy_workers = [Worker|Busy]
            },
            handle_request(Rest, Client, NewState);
        [] when TotalWorkers < MaxWorkers ->
            % No idle workers: create a new worker
            NewWorker = start_monitored_worker(),
            io:format("Creating new worker ~p (total: ~p/~p)~n", 
                     [NewWorker, TotalWorkers + 1, MaxWorkers]),
            NewWorker ! {compute, Client, Task},
            NewState = State#server_state{
                busy_workers = [NewWorker | Busy]
            },
            handle_request(Rest, Client, NewState);
        [] ->
            % No idle workers: queue the task
            NewState = State#server_state{
                pending_tasks = Pending ++ [{Task, Client}]
            },
            handle_request(Rest, Client, NewState)
    end.

% handle_work_done/2
% Handles worker completion notification
% - Removes worker from busy list
% - If pending tasks exist: assign next task to this worker (stays busy)
% - If no pending tasks and total workers > min_workers: terminate excess worker
% - If no pending tasks and total workers <= min_workers: move worker to idle list
handle_work_done(Worker, State) ->
    #server_state{busy_workers = Busy, idle_workers = Idle, pending_tasks = Pending, min_workers = MinWorkers} = State,
    NewBusy = lists:delete(Worker, Busy),
    
    case Pending of
        [{Task, Client}|RestPending] ->
            % Pending task available: assign to worker immediately
            Worker ! {compute, Client, Task},
            State#server_state{
                busy_workers = [Worker|NewBusy],
                pending_tasks = RestPending
            };
        [] ->
            % No pending tasks: check if we should terminate excess workers
            TotalWorkers = length(NewBusy) + length(Idle) + 1, % +1 for current worker
            if
                TotalWorkers > MinWorkers ->
                    % Terminate excess worker to maintain elasticity
                    Worker ! stop,
                    State#server_state{
                        busy_workers = NewBusy,
                        idle_workers = Idle
                    };
                true ->
                    % Keep worker as idle (at or below minimum)
                    State#server_state{
                        busy_workers = NewBusy,
                        idle_workers = [Worker|Idle]
                    }
            end
    end.

% handle_worker_termination/2
% Handles normal worker termination (elastic scale-down)
% - Removes worker from busy or idle list
% - Does NOT create a replacement worker
% This is called when a worker receives 'stop' and exits normally
handle_worker_termination(Worker, State) ->
    #server_state{busy_workers = Busy, idle_workers = Idle} = State,
    
    % Remove terminated worker from busy or idle list
    NewBusy = lists:delete(Worker, Busy),
    NewIdle = lists:delete(Worker, Idle),
    
    State#server_state{
        busy_workers = NewBusy,
        idle_workers = NewIdle
    }.

% handle_worker_crash/3
% Handles worker crash or abnormal termination
% - Removes worker from busy or idle list
% - Prints error message with crash reason
% - Creates a new worker and adds it to idle list
% - The task that caused the crash is dismissed (not retried)
handle_worker_crash(Worker, Reason, State) ->
    #server_state{busy_workers = Busy, idle_workers = Idle} = State,
    
    % Print error message
    io:format("ERROR: Worker ~p crashed with reason: ~p~n", [Worker, Reason]),
    
    % Remove crashed worker from busy or idle list
    NewBusy = lists:delete(Worker, Busy),
    NewIdle = lists:delete(Worker, Idle),
    
    % Create a new replacement worker and add to idle list
    NewWorker = start_monitored_worker(),
    io:format("Created replacement worker ~p~n", [NewWorker]),
    
    State#server_state{
        busy_workers = NewBusy,
        idle_workers = [NewWorker | NewIdle]
    }.

% Debug function to check idle workers from client
debug_idle_workers(ServerPid) ->
    ServerPid ! {debug_idle_workers, self()},
    receive
        {idle_workers, Workers} ->
            io:format("Received idle workers: ~p~n", [Workers]),
            Workers
    after 1000 ->
        io:format("Timeout waiting for idle workers response~n"),
        timeout
    end.

debug_state(ServerPid) ->
    ServerPid ! {debug_state, self()},
    receive
        {state, Min, Max, Total, Idle, Busy, Pending} ->
            io:format("Server state: Min=~p, Max=~p, Total=~p, Idle=~p, Busy=~p, Pending=~p~n",
                     [Min, Max, Total, Idle, Busy, Pending]),
            {Min, Max, Total, Idle, Busy, Pending}
    after 1000 ->
        io:format("Timeout waiting for state response~n"),
        timeout
    end.