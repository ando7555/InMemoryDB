
# In-Memory Database Project

## Overview

This project involves the creation of a very simple, single-threaded, in-memory database. The database is designed to handle a limited set of data and transaction commands, focusing on core functionalities that include setting, getting, deleting variables, and managing transactions.

### Duration

- **Review of Problem Statement**: 15 minutes
- **Coding**: 75 minutes
- **Code Review and Discussion**: 30 minutes

## Features

### Data Commands

The database supports the following basic commands:

- `SET [name] [value]`: Assigns a value to a variable. Neither variable names nor values contain spaces.
- `GET [name]`: Retrieves the value associated with a variable. Returns `NULL` if the variable is not set.
- `DELETE [name]`: Removes a variable from the database.
- `COUNT [value]`: Returns the count of variables that are set to a specific value. Returns `0` if no such value exists.
- `END`: Terminates the program.

### Transaction Commands

In addition to the basic data manipulation commands, the database supports transactional operations:

- `BEGIN`: Starts a new transaction block.
- `ROLLBACK`: Reverts the changes made in the most recent transaction block.
- `COMMIT`: Applies all changes made in the current and nested transaction blocks permanently.

Note: Both `ROLLBACK` and `COMMIT` will output `NO TRANSACTION` if no transaction blocks are open.

## Examples

### Basic Operations

```
>>> SET a 10
>>> GET a
10
>>> DELETE a
>>> GET a
NULL
>>> END
```

```
>>> SET a 10
>>> SET b 10
>>> COUNT 10
2
>>> COUNT 20
0
>>> DELETE a
>>> COUNT 10
1
>>> SET b 30
>>> COUNT 10
0
>>> END
```

### Transaction Operations

```
>>> BEGIN
>>> SET a 10
>>> GET a
10
>>> SET a 20
>>> GET a
20
>>> ROLLBACK
>>> GET a
10
>>> ROLLBACK
>>> GET a
NULL
>>> END
```

```
>>> BEGIN
>>> SET a 30
>>> BEGIN
>>> SET a 40
>>> COMMIT
>>> GET a
40
>>> ROLLBACK
NO TRANSACTION
>>> END
```

## Input Processing

Commands are processed one at a time. Input can be taken via stdin, function calls, or driven by unit tests, depending on your preference.

## Performance Considerations

### Time Complexity

The database should be designed with efficiency in mind, ensuring operations like `GET`, `SET`, `DELETE`, and `COUNT` have a worst-case time complexity of O(log(N)) or better, where N is the total number of variables stored.

### Space Complexity

The implementation should be memory-efficient, especially in handling transactions to ensure the memory usage does not significantly increase with each `BEGIN` command.
